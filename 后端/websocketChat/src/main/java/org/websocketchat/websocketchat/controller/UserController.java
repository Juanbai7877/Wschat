package org.websocketchat.websocketchat.controller;

import jakarta.servlet.http.HttpSession;
import org.websocketchat.websocketchat.pojo.Result;
import org.websocketchat.websocketchat.pojo.Users;
import org.websocketchat.websocketchat.service.UserService;
import org.websocketchat.websocketchat.utils.SHAUtil;
import org.websocketchat.websocketchat.utils.ThreadLocalUtil;
import jakarta.validation.constraints.Pattern;
import org.hibernate.validator.constraints.URL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.security.NoSuchAlgorithmException;
import java.util.Map;

/**
 * @author ALL
 * @date 2024/4/4
 * @Description
 */

@RestController
@Validated
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @PostMapping("/registerByPhone")
    public Result register(@Pattern(regexp = "^\\S{5,32}$")String userName, @Pattern(regexp = "^\\S{5,16}$")String password, String rePassword,@Pattern(regexp = "/\\d{3}-\\d{8}|\\d{4}-\\d{7}|^1(3[0-9]|4[57]|5[0-35-9]|7[0678]|8[0-9])\\d{8}$")String phone,String code) throws NoSuchAlgorithmException {
            return userService.register(userName,password,rePassword,phone,code);
    }

    @PostMapping("/login")
    public Result login(@Pattern(regexp = "^\\S{5,32}$")String userName, @Pattern(regexp = "^\\S{5,16}$")String password, HttpSession session) throws NoSuchAlgorithmException {
        return userService.login(userName,password, session);
    }

    @PostMapping("/registerByMail")
    public Result registerByMail(@Pattern(regexp = "^\\S{5,32}$")String userName,String nickName, @Pattern(regexp = "^\\S{5,16}$")String password, String rePassword,String email,String code) throws NoSuchAlgorithmException {
        return userService.registerByMail(userName,nickName,password,rePassword,email,code);
    }


    @PostMapping("/register")
    public Result registerWithoutEmail(@Pattern(regexp = "^\\S{5,32}$")String userName, @Pattern(regexp = "^\\S{5,16}$")String password, String rePassword, String nickName) throws NoSuchAlgorithmException {
        return userService.registerWithoutEmail(userName,password,rePassword,nickName);
    }

    @PostMapping("/ResetPassword")
    public Result resetPassword(@Pattern(regexp = "^\\S{5,32}$")String userName, @Pattern(regexp = "^\\S{5,16}$")String password, String rePassword, String oldPassword) throws NoSuchAlgorithmException {
        return userService.resetPassword(userName,oldPassword,password,rePassword);
    }

//    @GetMapping("/loginByMail")
//    public Result loginByMail(@Pattern(regexp = "^\\S{5,32}$")String userName, @Pattern(regexp = "^\\S{5,16}$")String password, HttpSession session) throws NoSuchAlgorithmException {
//        return userService.loginByMail(userName,password, session);
//    }


    @GetMapping("/loginByPhone")
    public Result loginByPhone(@Pattern(regexp = "^\\S{5,32}$")String phone,String code){
        return userService.loginByPhone(phone,code);
    }

    @GetMapping("/searchUser")
    public Result searchUser(String msg){
        return userService.searchUser(msg);
    }

    @GetMapping("/getFriendsList")
    public Result getFriendsList(String userName){
        return userService.getFriendsList(userName);
    }

    // todo
    @GetMapping("/userInfo")
    public Result userInfo(Long userId) {

        return userService.findUserById(userId);
    }

    @PutMapping("/update")
    public Result update(@RequestBody @Validated Users user) {
        return userService.update(user);
    }

    @PatchMapping("/updateAvatar")
    public Result updateAvatar(@RequestParam @URL String avatarUrl) {
        userService.updateAvatar(avatarUrl);
        return Result.success();
    }

    @PatchMapping("/updatePwd")
    public Result updatePwd(@RequestBody Map<String, String> params,@RequestHeader("Authorization") String token) {
        //1.校验参数
        String oldPwd = params.get("old_pwd");
        String newPwd = params.get("new_pwd");
        String rePwd = params.get("re_pwd");

        if (!StringUtils.hasLength(oldPwd) || !StringUtils.hasLength(newPwd) || !StringUtils.hasLength(rePwd)) {
            return Result.error("缺少必要的参数");
        }

        //原密码是否正确
        //调用userService根据用户名拿到原密码,再和old_pwd比对
        Map<String,Object> map = ThreadLocalUtil.get();
        String username = (String) map.get("userName");
        Users loginUser = userService.findByUserName(username);
        if (!loginUser.getPasswordHash().equals(SHAUtil.SHAEncrypt(oldPwd))){
            return Result.error("原密码填写不正确");
        }

        //newPwd和rePwd是否一样
        if (!rePwd.equals(newPwd)){
            return Result.error("两次填写的新密码不一样");
        }

        //2.调用service完成密码更新
        userService.updatePwd(newPwd);
        //删除redis中对应的token
        ValueOperations<String, String> operations = stringRedisTemplate.opsForValue();
        operations.getOperations().delete(token);
        return Result.success();
    }

    @PostMapping("/code")
    public Result sendCode(@RequestParam String phone){
        return userService.sendCode(phone);
    }

    @GetMapping("/codeByMail")
    public Result codeByMail(String mail){
        return userService.sendCodeByMail(mail);
    }


}
