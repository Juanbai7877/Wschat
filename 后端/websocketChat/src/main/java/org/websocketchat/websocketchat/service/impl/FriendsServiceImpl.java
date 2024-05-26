package org.websocketchat.websocketchat.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.websocketchat.websocketchat.mapper.FriendsMapper;
import org.websocketchat.websocketchat.mapper.UserMapper;
import org.websocketchat.websocketchat.pojo.Friends;
import org.websocketchat.websocketchat.pojo.Result;
import org.websocketchat.websocketchat.pojo.Users;
import org.websocketchat.websocketchat.service.FriendsService;

import java.util.List;
import java.util.Objects;

/**
 * @author ALL
 * @date 2024/4/23
 * @Description
 */
@Slf4j
@Service
public class FriendsServiceImpl implements FriendsService {

    @Autowired
    FriendsMapper friendsMapper;

    @Autowired
    UserMapper userMapper;


//    @Override
//    public Result addFriend(String phone, String friendPhone) {
//        Users user1 = userMapper.findUserByPhone(phone);
//        Users user2 = userMapper.findUserByPhone(friendPhone);
//        if(user1==null||user2==null){
//            return Result.error("用户不存在");
//        }
//        Friends friends = new Friends();
//        friends.setUserId1(user1.getUserId());
//        friends.setUserId2(user2.getUserId());
//        friends.setStatus("applying");
//        friendsMapper.insert(friends);
//        return Result.success();
//    }
//
//    @Override
//    public Result agreeFriend(String phone, String friendPhone) {
//
//        Users user1 = userMapper.findUserByPhone(phone);
//        Users user2 = userMapper.findUserByPhone(friendPhone);
//        Friends friends = new Friends();
//        friends.setUserId1(user1.getUserId());
//        friends.setStatus("success");
//        friends.setUserId2(user2.getUserId());
//        UpdateWrapper<Friends> updateWrapper = new UpdateWrapper<>();
//        updateWrapper.eq("userId1", user1.getUserId());
//        updateWrapper.eq("userId2", user2.getUserId());
//        friendsMapper.update(friends,updateWrapper);
//        return Result.success();
//    }
//
//    @Override
//    public Result disagreeFriend(String phone, String friendPhone) {
//        Users user1 = userMapper.findUserByPhone(phone);
//        Users user2 = userMapper.findUserByPhone(friendPhone);
//        Friends friends = new Friends();
//        friends.setUserId1(user1.getUserId());
//        friends.setStatus("fail");
//        friends.setUserId2(user2.getUserId());
//        UpdateWrapper<Friends> updateWrapper = new UpdateWrapper<>();
//        updateWrapper.eq("userId1", user1.getUserId());
//        updateWrapper.eq("userId2", user2.getUserId());
//        friendsMapper.update(friends,updateWrapper);
//        return Result.success();
//    }

    @Override
    public Result addFriend(Long userId1, Long userId2) {
        QueryWrapper<Friends> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id1", userId1).eq("user_id2", userId2);
        Friends friends1 = friendsMapper.selectOne(queryWrapper);
        if(friends1!=null&&Objects.equals(friends1.getStatus(), "success")){
            return Result.error("已添加该用户");
        }
        Users user1 = userMapper.selectById(userId1);
        Users user2 = userMapper.selectById(userId2);
        if(user1==null||user2==null){
            return Result.error("用户不存在");
        }
        Friends friends = new Friends();
        friends.setUserId1(user1.getUserId());
        friends.setUserId2(user2.getUserId());
        friends.setStatus("applying");
        friendsMapper.insert(friends);
        return Result.success();
    }

    @Override
    public Result agreeFriend(Long userId1, Long userId2) {

        Users user1 = userMapper.selectById(userId1);
        Users user2 = userMapper.selectById(userId2);
        Friends friends = new Friends();
        friends.setUserId1(user1.getUserId());
        friends.setStatus("success");
        friends.setUserId2(user2.getUserId());
        UpdateWrapper<Friends> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("userId1", user1.getUserId());
        updateWrapper.eq("userId2", user2.getUserId());
        friendsMapper.update(friends,updateWrapper);
        return Result.success();
    }

    @Override
    public Result disagreeFriend(Long userId1, Long userId2) {
        Users user1 = userMapper.selectById(userId1);
        Users user2 = userMapper.selectById(userId2);
        Friends friends = new Friends();
        friends.setUserId1(user1.getUserId());
        friends.setStatus("fail");
        friends.setUserId2(user2.getUserId());
        UpdateWrapper<Friends> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("userId1", user1.getUserId());
        updateWrapper.eq("userId2", user2.getUserId());
        friendsMapper.update(friends,updateWrapper);
        return Result.success();
    }

    @Override
    public Result addFriendDirect(Long userId1, Long userId2) {

        QueryWrapper<Friends> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id1", userId1).eq("user_id2", userId2);
        Friends friends1 = friendsMapper.selectOne(queryWrapper);
        if(friends1!=null&&Objects.equals(friends1.getStatus(), "success")){
            return Result.error("已添加该用户");
        }

        Users user1 = userMapper.selectById(userId1);
        Users user2 = userMapper.selectById(userId2);
        if(user1==null||user2==null){
            return Result.error("用户不存在");
        }
        Friends friends = new Friends();
        friends.setUserId1(user1.getUserId());
        friends.setUserId2(user2.getUserId());
        friends.setStatus("success");
        friends.setMessageNum(0);
        friendsMapper.insert(friends);
        return Result.success();
    }

    @Override
    public Result getFriendsList(Long userId) {
        List<Users> users=friendsMapper.selectListByUserId(userId);
        return Result.success(users);
    }

    @Override
    public Result removeFriend(Long userId1, Long userId2) {
        QueryWrapper<Friends> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id1", userId1).eq("user_id2", userId2); // 假设性别字段是字符串类型
        int result = friendsMapper.delete(queryWrapper);
        if (result>0) {
            return Result.success("删除成功");
        } else {
            return Result.error("意外错误");
        }
    }

    @Override
    public Result getFriendApplying(Long userId) {
        QueryWrapper<Friends> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id2", userId).eq("status", "applying");
        List<Users> friends = friendsMapper.selectApplyingList(userId);
        return Result.success(friends);
    }
}
