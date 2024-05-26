//导入request.js请求工具
import request from '@/utils/request.js'
import {ref} from "vue";


export const flashFriendService = (userId)=>{
    //借助于UrlSearchParams完成传递
    let querystring="?userId="+userId;
    return request.get('/friend/getFriendsList'+querystring);
}

export const addFriendDirectService = (userId1,userId2)=>{
    return request.get('/friend/addFriendDirect?userId1='+userId1+"&userId2="+userId2);
}

export const removeFriendService = (userId1,userId2)=>{
    return request.get('/friend/removeFriend?userId1='+userId1+"&userId2="+userId2);
}

export const searchFriendService = (msg)=>{
    return request.get('/user/searchUser?msg='+msg);
}



