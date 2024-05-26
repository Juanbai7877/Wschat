package org.websocketchat.websocketchat.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import jakarta.mail.Message;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.websocketchat.websocketchat.mapper.*;
import org.websocketchat.websocketchat.pojo.*;
import org.websocketchat.websocketchat.service.ChatService;
import org.websocketchat.websocketchat.service.GroupMessagesService;

import java.util.List;
import java.util.Objects;

/**
 * @author ALL
 * @date 2024/4/23
 * @Description
 */
@Slf4j
@Service
public class ChatServiceImpl implements ChatService {
    @Autowired
    FriendsMapper friendsMapper;
    @Autowired
    PrivateMessagesMapper privateMessagesMapper;
    @Autowired
    GroupMessagesMapper groupMessagesMapper;
    @Autowired
    LeaveMessageMapper leaveMessageMapper;
    @Autowired
    UserGroupsMapper userGroupsMapper;
    @Autowired
    FriendsMapper friendMapper;


    @Override
    public Result getAllPrivateMessage(Long userId) {
        List<Friends> friends = friendsMapper.selectUnReadListByUserId(userId);
        return Result.success(friends);
    }

    @Override
    public Result getPrivateMessage(Long userId, Long sendUserId) {
        List<PrivateMessages> privateMessages=privateMessagesMapper.selectUnreadByUserId(userId,sendUserId);

        Friends friends=friendsMapper.selectByUserId(userId,sendUserId);
        for (PrivateMessages privateMessage:privateMessages) {
            privateMessage.setMessageStatus("read");
            if(Objects.equals(friends.getUserId1(), userId)){
                friends.setMessageNum(friends.getMessageNum()-1);
            }else if(Objects.equals(friends.getUserId2(), userId)){
                friends.setMessageNum(friends.getMessageNum()+1);
            }
            privateMessagesMapper.updateById(privateMessage);
        }
        friendMapper.updateById(friends);
        return Result.success(privateMessages);
    }

    @Override
    public Result getGroupsMessage(Long userId, Long groupId) {
        UserGroups userGroups=userGroupsMapper.selectByUserIdAndGroupId(userId,groupId);
        if(userGroups==null){
            return Result.error("用户不在该群中");
        }
        List<Messages> GroupMessages=groupMessagesMapper.selectByGroupId(groupId,30);
        return Result.success(GroupMessages);
    }

    @Override
    public Result sendPrivateMessage(Long userId, Long toUserId, String msg) {
        PrivateMessages privateMessages = new PrivateMessages();
        privateMessages.setSenderId(userId);
        privateMessages.setReceiverId(toUserId);
        privateMessages.setMessageText(msg);
        privateMessagesMapper.insert(privateMessages);
        Friends friends=friendsMapper.selectByUserId(userId,toUserId);
        if(friends!=null){
            if(Objects.equals(friends.getUserId1(), userId)){
                friends.setMessageNum(friends.getMessageNum()-1);
            }
            else if(Objects.equals(friends.getUserId2(), userId)){
                friends.setMessageNum(friends.getMessageNum()+1);
            }
            return Result.success();
        }
        return Result.error("发送异常");
    }

    @Override
    public Result sendGroupsMessage(Long userId, Long toGroupId, String msg) {
        GroupMessages groupMessages = new GroupMessages();
        groupMessages.setSenderId(userId);
        groupMessages.setGroupId(toGroupId);
        groupMessages.setMessageText(msg);
        groupMessagesMapper.insert(groupMessages);
        return Result.success("发送成功");
    }

    @Override
    public Result searchPrivateMessage(Long userId, Long fromUserId, Long start) {

        List<PrivateMessages> privateMessages=privateMessagesMapper.selectByTime(userId,fromUserId,start,30);

        return Result.success(privateMessages);
    }

    @Override
    public Result searchGroupsMessage(Long userId, Long groupId, Long start) {
        List<GroupMessages> groupMessages=groupMessagesMapper.selectByTime(groupId,start,30);
        return Result.success(groupMessages);
    }

    @Override
    public Result getPrivateMessageBynum(Long userId1, Long userId2) {
        List<Messages> privateMessages=groupMessagesMapper.selectBynum(userId1,userId2,30);
        Friends friends=friendsMapper.selectByUserId(userId1,userId2);
        if(Objects.equals(friends.getUserId1(), userId1)&& friends.getMessageNum()>0){
            friends.setMessageNum(0);
        }else if(Objects.equals(friends.getUserId2(), userId2)&& friends.getMessageNum()<0){
            friends.setMessageNum(0);
        }
        friendMapper.updateById(friends);
        return Result.success(privateMessages);

    }

    @Override
    public Result leaveMessage(LeaveMessage leaveMessage) {

        leaveMessageMapper.insert(leaveMessage);
        return Result.success();
    }

    @Override
    public Result getLeaveMessage(Long messageId) {
        LeaveMessage leaveMessages=leaveMessageMapper.selectById(messageId);
        return Result.success(leaveMessages);
    }

    @Override
    public Result getAllLeaveMessage(Long userId) {
        List<LeaveMessageDto> leaveMessages=leaveMessageMapper.selectByUserId(userId);
        return Result.success(leaveMessages);
    }
}
