package org.websocketchat.websocketchat.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.websocketchat.websocketchat.pojo.LeaveMessage;
import org.websocketchat.websocketchat.pojo.LeaveMessageDto;

import java.util.List;

/**
 * @author ALL
 * @date 2024/5/26
 * @Description
 */
@Mapper
public interface LeaveMessageMapper extends BaseMapper<LeaveMessage> {

    @Select("select users.nick_name," +
            "leave_message.user_id," +
            "leave_message.content," +
            "leave_message.from_user_id," +
            "leave_message.message_time," +
            "leave_message.leave_message_id," +
            "leave_message.title" +
            " from leave_message,users where leave_message.user_id = #{userId} and leave_message.from_user_id=users.user_id")
    List<LeaveMessageDto> selectByUserId(Long userId);
}
