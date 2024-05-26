package org.websocketchat.websocketchat.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.websocketchat.websocketchat.pojo.GroupMessages;
import org.websocketchat.websocketchat.pojo.Messages;

import java.util.List;

/**
 * @author ALL
 * @date 2024/4/24
 * @Description
 */
@Mapper
public interface GroupMessagesMapper extends BaseMapper<GroupMessages> {

    @Select("SELECT * FROM group_messages\n" +
            "WHERE group_id = #{groupId}\n" +
            "ORDER BY message_time DESC\n" +
            "LIMIT #{nums} offset #{nums};")
    List<GroupMessages> selectByTime(long userId, long l, long nums);

    @Select("SELECT sender_id,nick_name,message_text,message_time,message_type FROM group_messages,users\n" +
            "WHERE group_id = #{groupId}\n" +
            "and group_messages.sender_id=users.user_id "+
            " ORDER BY message_time DESC\n" +
            "LIMIT #{i};")
    List<Messages> selectByGroupId(long groupId, int i);

    @Select("SELECT message_id,sender_id,nick_name,message_text,message_time,message_type FROM private_messages,users\n" +
            "WHERE sender_id = #{userId1} and receiver_id=#{userId2} and sender_id=users.user_id\n" +
            "or sender_id = #{userId2} and receiver_id=#{userId1} and sender_id=users.user_id "+
            " ORDER BY message_time DESC\n" +
            "LIMIT #{num};")
    List<Messages> selectBynum(long userId1,long userId2,int num);
}
