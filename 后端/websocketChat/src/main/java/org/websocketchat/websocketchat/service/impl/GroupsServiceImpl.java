package org.websocketchat.websocketchat.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.websocketchat.websocketchat.mapper.GroupsMapper;
import org.websocketchat.websocketchat.mapper.UserGroupsMapper;
import org.websocketchat.websocketchat.mapper.UserMapper;
import org.websocketchat.websocketchat.pojo.Groups;
import org.websocketchat.websocketchat.pojo.Result;
import org.websocketchat.websocketchat.pojo.UserGroups;
import org.websocketchat.websocketchat.pojo.Users;
import org.websocketchat.websocketchat.service.GroupsService;

import java.util.List;
import java.util.Objects;

/**
 * @author ALL
 * @date 2024/4/23
 * @Description
 */
@Slf4j
@Service
public class GroupsServiceImpl implements GroupsService {
    @Autowired
    private GroupsMapper groupsMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private UserGroupsMapper userGroupsMapper;

    @Override
    public Result getGroupsList(Long userId) {
        List<Groups> groups=groupsMapper.selectListByUserId(userId);
        return Result.success(groups);
    }

    @Override
    public Result createGroups(Long userId, String groupName) {
        Groups groups=new Groups();
        groups.setGroupName(groupName);
        groups.setGroupOwner(userId);
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("group_name",groupName);
        Groups group = groupsMapper.selectOne(wrapper);
        if(group!=null){
            return Result.error("该群已存在");
        }
        groupsMapper.insert(groups);
        group = groupsMapper.selectOne(wrapper);
        UserGroups userGroups= new UserGroups();
        userGroups.setUserId(userId);
        userGroups.setGroupId(group.getGroupId());
        userGroups.setStatus("success");
        userGroups.setMessageNum(0);
        userGroupsMapper.insert(userGroups);
        return Result.success(group);
    }

    @Override
    public Result deleteGroups(Long userId, Long groupId) {

        Groups groups = groupsMapper.selectById(groupId);
        if(groups.getGroupOwner()==userId){
            groupsMapper.deleteById(groupId);
            return Result.success();
        }
        return Result.error("删除失败");
    }

    @Override
    public Result addGroups(Long userId, Long groupId) {
        Groups groups=groupsMapper.selectById(groupId);
        UserGroups userGroups1=userGroupsMapper.selectByUserIdAndGroupId(userId,groupId);
        if(userGroups1==null){
            UserGroups userGroups=new UserGroups();
            userGroups.setUserId(userId);
            userGroups.setGroupId(groupId);
            userGroups.setStatus("success");
            userGroupsMapper.insert(userGroups);
            return Result.success();
        }
        if(Objects.equals(userGroups1.getStatus(), "delete")){
            userGroups1.setStatus("applying");
            userGroupsMapper.updateById(userGroups1);
            return Result.success();
        }
        return Result.error("用户已在该群中");
    }

    @Override
    public Result agreeAddGroups(Long userId, Long groupId, Long groupOwner) {
        UserGroups userGroups = userGroupsMapper.selectByUserIdAndGroupId(userId,groupId);
        if(userGroups==null){
            return Result.error("用户请求异常");
        }
        if(!Objects.equals(userGroups.getStatus(), "applying")){
            return Result.error("用户请求异常");
        }
        Groups groups=groupsMapper.selectById(groupId);
        if(groups.getGroupOwner()!=groupOwner){
            return Result.error("你没有权限");
        }
        UpdateWrapper<UserGroups> updateWrapper=new UpdateWrapper<>();
        updateWrapper.eq("user_id",userId);
        updateWrapper.eq("group_id",groupId);
        userGroups.setStatus("success");
        userGroupsMapper.update(userGroups, updateWrapper);
        return Result.success();
    }

    @Override
    public Result getGroupsUserList(Long groupId) {
        List<Users> users=userMapper.selectListByGroups(groupId);
        return Result.success(users);
    }

    @Override
    public Result getGroupsApplyingList(Long userId) {
        List<UserGroups> userGroups =userGroupsMapper.selectApplyingByOwnerId(userId);
        return Result.success(userGroups);
    }

    @Override
    public Result quitGroups(Long userId, Long groupId) {
        QueryWrapper<UserGroups> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);
        queryWrapper.eq("group_id", groupId);
        int result = userGroupsMapper.delete(queryWrapper);

        // 检查删除结果
        if (result>=1) {
            return Result.success("退出成功");
        } else {
            return Result.error("退出失败");
        }
    }

    @Override
    public Result searchGroups(String groupName, Long groupId) {
        Groups groups =groupsMapper.selectById(groupId);
        QueryWrapper<Groups> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("group_name", groupName); // name 字段等于张三
        List<Groups> groupsList=groupsMapper.selectList(queryWrapper);
        if(groups!=null){
            groupsList.add(groups);
        }
        return Result.success(groupsList);
    }

    @Override
    public Result deleteGroupsUser(Long userId, Long groupId, Long groupOwner) {
        Groups groups =groupsMapper.selectById(groupId);
        if(groups.getGroupOwner()!=groupOwner){
            return Result.error("你没有权限");
        }
        UserGroups userGroups = userGroupsMapper.selectByUserIdAndGroupId(userId,groupId);
        if(userGroups==null){
            return Result.error("该用户不存在");
        }
        UpdateWrapper<UserGroups> updateWrapper=new UpdateWrapper<>();
        updateWrapper.eq("user_id",userId);
        updateWrapper.eq("group_id",groupId);
        userGroups.setStatus("delete");
        int ok=userGroupsMapper.update(userGroups, updateWrapper);
        if(ok==1){
            return Result.success("删除成功");
        }
        return Result.error("删除失败");
    }


}
