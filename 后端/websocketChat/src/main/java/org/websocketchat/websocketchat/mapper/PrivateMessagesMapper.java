package org.websocketchat.websocketchat.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.websocketchat.websocketchat.pojo.PrivateMessages;

import java.util.List;

/**
 * @author ALL
 * @date 2024/4/23
 * @Description
 */
@Mapper
public interface PrivateMessagesMapper extends BaseMapper<PrivateMessages> {
    @Select("select * from private_messages " +
            "where receiver_id=#{userId} " +
            "and sender_id=#{sendUserId} " +
            "and message_status='unread'")
    List<PrivateMessages> selectUnreadByUserId(long userId, long sendUserId);

    @Select("SELECT * FROM private_messages\n" +
            "WHERE receiver_id = #{userId} " +
            "and sender_id = #{fromUserId} " +
            "order by message_time DESC\n" +
            "LIMIT #{nums} OFFSET #{start};")
    List<PrivateMessages> selectByTime(long userId, long fromUserId,long start, long nums);
}
