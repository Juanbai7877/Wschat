import {defineStore} from "pinia";
import {ref} from "vue";

export const useTargetInfoStore = defineStore('targetInfo',()=>{
    //定义状态相关的内容
    const info = ref({
        friendId:0,
        groupId:0,
        targetName:'',
        targetType:-1,
        leaveMessageName:'',
        leaveMessageId:-1,
    })

    const setFriendId = (newInfo)=>{
        info.value.friendId = newInfo
    }

    const setGroupId = (newInfo)=>{
        info.value.groupId = newInfo
    }

    const setTargetName = (newInfo)=>{
        info.value.targetName = newInfo

    }

    const setTargetType = (newInfo)=> {
        info.value.targetType = newInfo
    }

    const getInfo = ()=>{
        return info.value
    }

    const setLeaveMessageName = (newInfo)=>{
        info.value.leaveMessageName = newInfo
    }

    const setLeaveMessageId = (newInfo)=>{
        info.value.leaveMessageId = newInfo
    }


    return {info,setFriendId,setGroupId,setTargetName,setTargetType,getInfo,setLeaveMessageName,setLeaveMessageId}

},{
    persist:true
});