package org.websocketchat.websocketchat.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.websocketchat.websocketchat.pojo.Result;
import org.websocketchat.websocketchat.service.GroupMessagesService;
import org.websocketchat.websocketchat.service.GroupsService;

/**
 * @author ALL
 * @date 2024/4/23
 * @Description
 */
@RestController
@Validated
@RequestMapping("/groups")
public class GroupsController {
    @Autowired
    private GroupsService groupsService;

    @Autowired
    private GroupMessagesService groupMessagesService;

    @GetMapping("/getGroupsList")
    public Result getGroupsList(Long userId){
        return groupsService.getGroupsList(userId);
    }

    @GetMapping("/createGroups")
    public Result createGroups(Long groupOwner,String groupName){
        return groupsService.createGroups(groupOwner,groupName);
    }

    @GetMapping("/deleteGroups")
    public Result deleteGroups(Long userId,Long groupId){
        return groupsService.deleteGroups(userId,groupId);
    }

        @GetMapping("/addGroups")
    public Result addGroups(Long userId,Long groupId){
        return groupsService.addGroups(userId,groupId);
    }

    @GetMapping("/agreeAddGroups")
    public Result agreeAddGroups(Long userId,Long groupId,Long groupOwner){
        return groupsService.agreeAddGroups(userId,groupId,groupOwner);
    }

    @GetMapping("/deleteGroupsUser")
    public Result deleteGroupsUser(Long userId,Long groupId,Long groupOwner){
        return groupsService.deleteGroupsUser(userId,groupId,groupOwner);
    }

    @GetMapping("/getGroupsUserList")
    public Result getGroupsUserList(Long groupId){
        return groupsService.getGroupsUserList(groupId);
    }

    @GetMapping("/getGroupsApplyingList")
    public Result getGroupsApplyingList(Long groupId){
        return groupsService.getGroupsApplyingList(groupId);
    }

    @GetMapping("/searchGroups")
    public Result searchGroups(String groupName,Long groupId){
        return groupsService.searchGroups(groupName,groupId);
    }

    @GetMapping("/quitGroups")
    public Result quitGroups(Long userId,Long groupId){
        return groupsService.quitGroups(userId,groupId);
    }




}
