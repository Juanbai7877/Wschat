//导入request.js请求工具
import request from '@/utils/request.js'
import {ref} from "vue";


export const reflashGroupService = (userId)=>{
    //借助于UrlSearchParams完成传递
    let querystring="?userId="+userId;
    return request.get('/groups/getGroupsList'+querystring);
}

export const addGroupService = (userId,groupId)=>{
    return request.get('/groups/addGroups?userId='+userId+"&groupId="+groupId);
}

export const deleteGroupService = (groupId,userId)=>{
    return request.get('/groups/deleteGroups?userId='+userId+"&groupId="+groupId);
}

export const createGroupService = (groupName,userId)=>{
    return request.get('/groups/createGroups?groupOwner='+userId+"&groupName="+groupName);
}

export const getGroupService = (groupId)=>{
    let querystring="?userId="+groupId;
    return request.get('/groups/getGroupsList'+querystring);
}