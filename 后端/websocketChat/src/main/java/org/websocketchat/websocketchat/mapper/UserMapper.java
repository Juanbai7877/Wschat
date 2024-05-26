package org.websocketchat.websocketchat.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.websocketchat.websocketchat.pojo.Users;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * @author ALL
 * @date 2024/4/4
 * @Description
 */
@Mapper
public interface UserMapper extends BaseMapper<Users> {

    @Select("select * from users where user_name=#{userName}")
    Users findByUserName(String userName);

    @Insert("insert into users(user_name,nick_name,password_hash,phone,role,created_time,updated_time) values(#{username},#{nickName},#{password},#{phone},'new_role',now(),now())")
    void add(String username,String password,String phone,String nickName);


    @Update("update users set avatar_url=#{avatarUrl},updated_time=now() where user_id=#{id}")
    void updateAvatar(String avatarUrl,String id);

    @Update("update users set password_hash=#{passwordHash},updated_time=now() where user_id=#{id}")
    void updatePwd(String passwordHash, Long id);

    @Select("select * from users where phone=#{phone}")
    Users selectByIdfindUserByPhone(String phone);

    @Select("select users.nick_name,users.avatar_url,users.user_name \n" +
            "from users,friends \n" +
            "where user_id1=user_id and user_id2 =#{userId} \n" +
            "or user_id2=user_id and user_id1 =#{userId}")
    List<Users> selectFriendList(Long userId);

    @Select("select users.nick_name,users.avatar_url,users.user_name,users.user_id\n" +
            "from users ,user_groups\n" +
            "where users.user_id=user_groups.user_id\n" +
            "and user_groups.group_id=#{groupId};")
    List<Users> selectListByGroups(long groupId);

    @Select("select * from users where email=#{userName}")
    Users findUserByEmail(String username);
    @Select("select * from users where phone=#{phone}")
    Users findUserByPhone(String phone);
}
