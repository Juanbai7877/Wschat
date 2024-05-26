package org.websocketchat.websocketchat.service.impl;

import cn.hutool.Hutool;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.lang.UUID;
import cn.hutool.core.lang.Validator;
import cn.hutool.core.util.RandomUtil;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.PropertyValue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Component;
import org.websocketchat.websocketchat.dto.UserDTO;
import org.websocketchat.websocketchat.mapper.UserMapper;
import org.websocketchat.websocketchat.pojo.Groups;
import org.websocketchat.websocketchat.pojo.Result;
import org.websocketchat.websocketchat.pojo.UserWithToken;
import org.websocketchat.websocketchat.pojo.Users;
import org.websocketchat.websocketchat.service.GroupsService;
import org.websocketchat.websocketchat.service.UserService;
import org.websocketchat.websocketchat.utils.*;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.TimeUnit;

import static org.websocketchat.websocketchat.utils.RedisConstants.*;
import static org.websocketchat.websocketchat.utils.SystemConstants.USER_NICK_NAME_PREFIX;


/**
 * @author ALL
 * @date 2024/4/4
 * @Description
 */
@Slf4j
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    SendMailUtils sendMailUtils;
    @Autowired
    private JavaMailSenderImpl mailSender;


    @Value(value="${spring.mail.username}")
    private String formMail;

    @Value(value = "${spring.mail.password}")
    private String password;
    @Value("${aliyun.sms.sms-access-key-id}")
    private String accessKeyId;
    @Value(value = "${aliyun.sms.sms-access-key-secret}")
    private String accessKeySecret;

    @Autowired
    private UserMapper userMapper;

    @Resource
    private StringRedisTemplate stringRedisTemplate;
    @Override
    public Users findByUserName(String userName) {
        Users u=userMapper.findByUserName(userName);
        return u;
    }

    @Override
    public Result login(String username, String password, HttpSession session) throws NoSuchAlgorithmException {

        // 1 判断用户是否已存在
        Users t_user=userMapper.findByUserName(username);
        if(t_user==null){
            t_user=userMapper.findUserByEmail(username);
            if(t_user==null){
                return Result.error("用户不存在");
            }
        }
        // 2 判断密码是否正确
        if(!t_user.getPasswordHash().equals(SHAUtil.SHA256Encrypt(password))){
            return Result.error("密码错误");
        }
        String token = createAndSetToken(t_user);
        session.setAttribute("user", t_user.getUserName());
        UserWithToken user=new UserWithToken();
        user.setUserName(t_user.getUserName());
        user.setUserId(t_user.getUserId());
        user.setToken(token);
        user.setNickName(t_user.getNickName());
        return Result.success(user);
    }

    @Override
    public Result loginByPhone(String phone, String code){
        // 1 查看验证码是否一致
        String cacheCode = stringRedisTemplate.opsForValue().get(LOGIN_CODE_KEY + phone);
        if (cacheCode == null || !cacheCode.equals(code)) {
            // 不一致，报错
            return Result.error("验证码错误");
        }
        Users user=userMapper.findUserByPhone(phone);
        if(user==null){
            return Result.error("用户未注册");
        }
        String tokenKey = createAndSetToken(user);
        return Result.success(tokenKey);
    }

    private String createAndSetToken(Users user) {
        // 7.1.生成token，作为登录令牌

        String token = UUID.randomUUID().toString().replace("-", "");
        // 7.3.存储
        String tokenKey = LOGIN_USER_KEY + token;

//        // 7.2.将User对象转为HashMap存储
//        UserDTO userDTO = BeanUtil.copyProperties(user, UserDTO.class);
//        Map<String, Object> userMap = BeanUtil.beanToMap(userDTO, new HashMap<>(),
//                CopyOptions.create()
//                        .setIgnoreNullValue(true)
//                        .setFieldValueEditor((fieldName, fieldValue) -> fieldValue.toString()));
//        stringRedisTemplate.opsForHash().putAll(tokenKey, userMap);
//        // 7.4.设置token有效期
//        stringRedisTemplate.expire(tokenKey, LOGIN_USER_TTL, TimeUnit.MINUTES);
//        // 7.4.设置token有效期
//        stringRedisTemplate.expire(tokenKey, LOGIN_USER_TTL, TimeUnit.MINUTES);
        return token;

    }


    @Override
    public Result register(String username, String password,String rePassword, String phone,String code) throws NoSuchAlgorithmException {

        // 1 查看验证码是否一致
        String cacheCode = stringRedisTemplate.opsForValue().get(LOGIN_CODE_KEY + phone);
        if (cacheCode == null || !cacheCode.equals(code)) {
            // 不一致，报错
            return Result.error("验证码错误");
        }

        // 2 判断两次密码是否一致
        if(!rePassword.equals(password)){
            return Result.error("两次密码不一致");
        }

        // 3 判断用户是否已存在
        Users t_user=userMapper.findByUserName(username);
        if(t_user!=null){
            return Result.error("用户已存在");
        }

        // 4 创建用户
        Users user=createUserWithPhone(phone,username,password);

        String tokenKey = createAndSetToken(user);

        return Result.success(tokenKey);
    }

    @Override
    public Result registerWithoutEmail(String username, String password, String rePassword, String nickName) throws NoSuchAlgorithmException {
        // 2 判断两次密码是否一致
        if(!rePassword.equals(password)){
            return Result.error("两次密码不一致");
        }

        // 3 判断用户是否已存在
        Users t_user=userMapper.findByUserName(username);
        if(t_user!=null){
            return Result.error("用户已存在");
        }

        // 4 创建用户
        Users user=new Users();
        user.setUserName(username);
        user.setNickName(nickName);
        user.setPasswordHash(SHAUtil.SHA256Encrypt(password));
        userMapper.insert(user);
        String tokenKey = createAndSetToken(user);

        return Result.success(tokenKey);
    }

    private Users createUserWithPhone(String phone,String userName,String password) throws NoSuchAlgorithmException {
        // 1.创建用户
        Users user = new Users();
        user.setPhone(phone);
        user.setPasswordHash(SHAUtil.SHA256Encrypt(password));
        user.setUserName(userName);
        user.setNickName(USER_NICK_NAME_PREFIX + RandomUtil.randomString(10));
        //保存用户
        userMapper.insert(user);
        return user;
    }

    @Override
    public Result update(Users user) {
        user.setUpdatedTime(Timestamp.valueOf(LocalDateTime.now()));
        Users byUserName = findByUserName(user.getUserName());
        if(byUserName!=null&&!byUserName.getUserId().equals(user.getUserId())){
            return Result.error("用户已存在");
        }
        userMapper.updateById(user);
        return Result.success();

    }

    @Override
    public Result searchUser(String msg) {
        QueryWrapper<Users> queryWrapper = new QueryWrapper<>();
        Long UID= NumberUtils.parseToLongOrDefault(msg);
        queryWrapper.like("user_name", "%"+msg+"%").or().eq("email", msg).or().like("nick_name", "%"+msg+"%").or().eq("user_id", UID);
        queryWrapper.select("user_name", "email","user_id","avatar_url","nick_name");
        List<Users> users=userMapper.selectList(queryWrapper);

        return Result.success(users);
    }



    @Override
    public void updateAvatar(String avatarUrl) {
        Map<String,Object>map= ThreadLocalUtil.get();
        String userId = (String) map.get("id");
        userMapper.updateAvatar(avatarUrl,userId);

    }


    @Override
    public void updatePwd(String newPwd) {
        Map<String,Object> map = ThreadLocalUtil.get();
        Object uid =  map.get("id");
        Long id=Long.valueOf(String.valueOf(uid));
        userMapper.updatePwd(SHAUtil.SHAEncrypt(newPwd),id);
    }

    @Override
    public Result sendCode(String phone) {
        //1.校验手机号
        if (RegexUtils.isPhoneInvalid(phone)) {
            // 2.如果不符合，返回错误信息
            return Result.error("手机号格式错误！");
        }
        // 3.符合，生成验证码
        String code = RandomUtil.randomNumbers(6);

        // 4.保存验证码到 session
        stringRedisTemplate.opsForValue().set(LOGIN_CODE_KEY + phone, code, LOGIN_CODE_TTL, TimeUnit.MINUTES);

        //TODO 5.发送验证码
        log.debug("发送短信验证码成功，验证码：{}", code);

        //default 地域节点，默认就好  后面是 阿里云的 id和秘钥（这里记得去阿里云复制自己的id和秘钥哦）

//        DefaultProfile profile = DefaultProfile.getProfile("default", accessKeyId, accessKeySecret);
//        IAcsClient client = new DefaultAcsClient(profile);
//
//        //这里不能修改
//        CommonRequest request = new CommonRequest();
//        //request.setProtocol(ProtocolType.HTTPS);
//        request.setMethod(MethodType.POST);
//        request.setDomain("dysmsapi.aliyuncs.com");
//        request.setVersion("2017-05-25");
//        request.setAction("SendSms");
//
//        Map<String,String>params=new HashMap<>();
//        params.put("code", code);
//

//        request.putQueryParameter("PhoneNumbers", phone);                    //手机号
//        request.putQueryParameter("SignName", "阿里云短信测试");    //申请阿里云 签名名称（暂时用阿里云测试的，自己还不能注册签名）
//        request.putQueryParameter("TemplateCode", "SMS_154950909"); //申请阿里云 模板code（用的也是阿里云测试的）
//        request.putQueryParameter("TemplateParam", JSONObject.toJSONString(params));
//        try {
//            CommonResponse response = client.getCommonResponse(request);
//            System.out.println(response.getData());
//            return Result.success("OK");
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

        //返回ok
        return Result.success("OK");
    }

    @Override
    public Result getFriendsList(String userName) {
        Users user = userMapper.findByUserName(userName);
        List<Users> friends =userMapper.selectFriendList(user.getUserId());
        return Result.success(friends);
    }

    @Override
    public Result sendCodeByMail(String toMail) {
        //1.校验邮箱

        boolean isEmail= Validator.isEmail(toMail);
        if(!isEmail){
            return Result.error("请输入正确的邮箱");
        }
        // 3.符合，生成验证码
        String code = RandomUtil.randomNumbers(6);

        // 4.保存验证码到 session
        stringRedisTemplate.opsForValue().set(LOGIN_CODE_KEY + toMail, code, LOGIN_CODE_TTL, TimeUnit.MINUTES);

        //TODO 5.发送验证码
        log.debug("发送短信验证码成功，验证码：{}", code);
        String text="欢迎注册websocketchat，您的验证码是："+code+"\n十分钟内有效，请保存好您的验证码";
        sendMailUtils.sendText("websocketchat", text, "websocketchat@163.com",
                toMail);
        return Result.success("OK");
    }

    @Override
    public Result registerByMail(String userName, String nickName,String password, String rePassword, String email, String code) {
        // 1 查看验证码是否一致
        String cacheCode = stringRedisTemplate.opsForValue().get(LOGIN_CODE_KEY + email);
        if (cacheCode == null || !cacheCode.equals(code)) {
            // 不一致，报错
            return Result.error("验证码错误");
        }

        // 2 判断两次密码是否一致
        if(!rePassword.equals(password)){
            return Result.error("两次密码不一致");
        }
        if(!userName.isEmpty()){
            // 3 判断用户是否已存在
            Users t_user=userMapper.findByUserName(userName);
            if(t_user!=null){
                return Result.error("用户已存在");
            }
        }
        // 4 创建用户
        try {
            Users user=createUserWithEmail(email,nickName,userName,password);
            user.setNickName(nickName);
            String token = createAndSetToken(user);
            return Result.success(token);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }





    }

    @Override
    public Result findUserById(Long userId) {
        Users users = userMapper.selectById(userId);
        users.setPasswordHash(null);
        users.setUpdatedTime(null);
        users.setCreatedTime(null);
        return Result.success(users);
    }

    @Override
    public Result resetPassword(String userName, String oldPassword, String password, String rePassword) throws NoSuchAlgorithmException {
        Users t_user=userMapper.findByUserName(userName);
        if(t_user==null){
            return Result.error("用户不存在");
        }

        if(!t_user.getPasswordHash().equals(SHAUtil.SHA256Encrypt(oldPassword))){
            return Result.error("原密码错误");
        }

        if(!rePassword.equals(password)){
            return Result.error("两次密码不一致");

        }

        t_user.setPasswordHash(SHAUtil.SHA256Encrypt(password));
        userMapper.updateById(t_user);

        return Result.success("修改成功");
    }

    private Users createUserWithEmail(String email,String nickName,String userName,String password) throws NoSuchAlgorithmException {
        // 1.创建用户
        Users user = new Users();
        user.setEmail(email);
        try {
            user.setPasswordHash(SHAUtil.SHA256Encrypt(password));
        }catch (NoSuchAlgorithmException e){
            System.out.println(Arrays.toString(e.getStackTrace()));
            System.out.println(e.getMessage());
        }

        user.setUserName(userName);
        if(nickName==null||nickName.isEmpty()){
            user.setNickName(USER_NICK_NAME_PREFIX + RandomUtil.randomString(10));
        }else{
            user.setNickName(nickName);
        }
        //保存用户
        userMapper.insert(user);
        return user;
    }



}
