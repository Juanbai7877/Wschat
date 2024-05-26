package org.websocketchat.websocketchat.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.websocketchat.websocketchat.pojo.Friends;
import org.websocketchat.websocketchat.pojo.Users;

import java.util.List;

/**
 * @author ALL
 * @date 2024/4/23
 * @Description
 */
@Mapper
public interface FriendsMapper extends BaseMapper<Friends> {


    @Select("select *\n" +
            "from friends\n" +
            "where user_id1=#{userId} and user_id2=#{sendUserId} \n" +
            "   or user_id1=#{sendUserId} and user_id2=#{userId};")
    Friends selectByUserId(long userId, long sendUserId);


    @Select("select *\n" +
            "from friends\n" +
            "where user_id2=#{userId} and message_num<0 \n" +
            "   or user_id1=#{userId} and message_num>0;")
    List<Friends> selectUnReadListByUserId(long userId);


    @Select("select users.user_id,users.nick_name,users.user_name,users.avatar_url,users.email\n" +
            "from friends,users\n" +
            "where user_id2=#{userId} and user_id1=users.user_id" +
            " or user_id1=#{userId} and user_id2=users.user_id;")
    List<Users> selectListByUserId(Long userId);
   @Select("select users.user_id,users.nick_name,users.user_name from friends,users" +
           " where user_id2=#{userId} and user_id1=users.user_id and status='applying';")
    List<Users> selectApplyingList(Long userId);
}
