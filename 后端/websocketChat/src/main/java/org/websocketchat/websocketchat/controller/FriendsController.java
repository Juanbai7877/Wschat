package org.websocketchat.websocketchat.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.websocketchat.websocketchat.service.FriendsService;
import jakarta.validation.constraints.Pattern;
import org.springframework.web.bind.annotation.GetMapping;
import org.websocketchat.websocketchat.pojo.Result;

/**
 * @author ALL
 * @date 2024/4/23
 * @Description
 */


@RestController
@Validated
@RequestMapping("/friend")
public class FriendsController {
    @Autowired
    private FriendsService friendsService;

//    @GetMapping("/addFriend")
//    public Result addFriend(String phone, String friendPhone){
//        return friendsService.addFriend(phone,friendPhone);
//    }
//
//    @GetMapping("/agreeFriend")
//    public Result agreeFriend(String phone, String friendPhone){
//        return friendsService.agreeFriend(phone,friendPhone);
//    }
//
//    @GetMapping("/disagreeFriend")
//    public Result disagreeFriend(String phone, String friendPhone){
//        return friendsService.disagreeFriend(phone,friendPhone);
//    }

    @GetMapping("/addFriend")
    public Result addFriend(Long userId1, Long userId2){
        return friendsService.addFriend(userId1,userId2);
    }

    @GetMapping("/addFriendDirect")
    public Result addFriendDirect(Long userId1, Long userId2){
        return friendsService.addFriendDirect(userId1,userId2);
    }

    @GetMapping("/agreeFriend")
    public Result agreeFriend(Long userId1, Long userId2){
        return friendsService.agreeFriend(userId1,userId2);
    }

    @GetMapping("/disagreeFriend")
    public Result disagreeFriend(Long userId1, Long userId2){
        return friendsService.disagreeFriend(userId1,userId2);
    }

    @GetMapping("/getFriendsList")
    public Result getFriendsList(Long userId){
        return friendsService.getFriendsList(userId);
    }

    @GetMapping("/removeFriend")
    public Result removeFriend(Long userId1, Long userId2){
        return friendsService.removeFriend(userId1,userId2);
    }

    @GetMapping("/getFriendApplying")
    public Result getFriendApplying(Long userId){
        return friendsService.getFriendApplying(userId);
    }



}
