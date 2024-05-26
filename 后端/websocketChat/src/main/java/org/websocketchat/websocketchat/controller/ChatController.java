package org.websocketchat.websocketchat.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.websocketchat.websocketchat.pojo.LeaveMessage;
import org.websocketchat.websocketchat.pojo.Result;
import org.websocketchat.websocketchat.service.ChatService;
import org.websocketchat.websocketchat.service.FriendsService;

/**
 * @author ALL
 * @date 2024/4/23
 * @Description
 */
@RestController
@Validated
@RequestMapping("/chat")
public class ChatController {

    @Autowired
    private ChatService chatService;

    @GetMapping("/getAllPrivateMessage")
    public Result getAllPrivateMessage(Long userId){
        return chatService.getAllPrivateMessage(userId);
    }

    @GetMapping("/getPrivateMessage")
    public Result getPrivateMessage(Long userId, Long sendUserId){
        return chatService.getPrivateMessage(userId,sendUserId);
    }
    @GetMapping("/getPrivateMessageByNum")
    public Result getPrivateMessageByNum(Long userId1, Long userId2){
        return chatService.getPrivateMessageBynum(userId1,userId2);
    }

    @GetMapping("/getGroupsMessage")
    public Result getGroupsMessage(Long userId, Long groupId){
        return chatService.getGroupsMessage(userId,groupId);
    }

    @GetMapping("/sendPrivateMessage")
    public Result sendPrivateMessage(Long userId, Long toUserId,String msg){
        return chatService.sendPrivateMessage(userId,toUserId,msg);
    }
    @GetMapping("/sendGroupsMessage")
    public Result sendGroupsMessage(Long userId, Long toGroupId,String msg){
        return chatService.sendGroupsMessage(userId,toGroupId,msg);
    }

    @GetMapping("/searchPrivateMessage")
    public Result searchPrivateMessage(Long userId, Long toUserId,Long nums){
        return chatService.searchPrivateMessage(userId,toUserId,nums);
    }


    @GetMapping("/searchGroupsMessage")
    public Result searchGroupsMessage(Long userId, Long groupId,Long nums){
        return chatService.searchGroupsMessage(userId,groupId,nums);
    }


    @PostMapping("/leaveMessage")
    public Result leaveMessage(LeaveMessage leaveMessage){
        return chatService.leaveMessage(leaveMessage);
    }

    @GetMapping("/getAllLeaveMessage")
    public Result getAllLeaveMessage(Long userId){
        return chatService.getAllLeaveMessage(userId);
    }

    @GetMapping("/getLeaveMessage")
    public Result getLeaveMessage(Long messageId){
        return chatService.getLeaveMessage(messageId);
    }



}
