//导入request.js请求工具
import request from '@/utils/request.js'
import {ref} from "vue";


export const getPrivateMessageService = (userId,sendUserId)=>{
    return request.get('/chat/getPrivateMessageByNum?userId1='+userId+"&userId2="+sendUserId);
}

export const getGroupsMessageService = (userId,groupId)=>{
    return request.get('/chat/getGroupsMessage?userId='+userId+"&groupId="+groupId);
}

export const sendPrivateMessageService = (userId,toUserId,msg)=>{
    return request.get('/chat/sendPrivateMessage?userId='+userId+"&toUserId="+toUserId+"&msg="+msg);
}

export const sendGroupsMessageService = (userId,toGroupId,msg)=>{
    return request.get('/chat/sendGroupsMessage?userId='+userId+"&toGroupId="+toGroupId+"&msg="+msg);
}

export const getLeaveMessagesService = (userId)=>{
    return request.get("/chat/getAllLeaveMessage?userId="+userId)
}

export const leaveMessagesService = (laeveMessageData)=>{
    const params = new URLSearchParams();
    for(let key in laeveMessageData){
        params.append(key,laeveMessageData[key])
    }
    return request.post('/chat/leaveMessage',params)
}



