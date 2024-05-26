<script setup>

import {useTargetInfoStore} from '@/store/targetInfo.js'
import {useUserInfoStore} from '@/store/userInfo.js'
import {useRouter} from 'vue-router'
import {onMounted, ref} from 'vue'
import {getLeaveMessagesService, leaveMessagesService} from "@/api/chat.js";
import {ElMessage} from "element-plus";
const targetInfoStore = useTargetInfoStore()
const userInfoStore = useUserInfoStore()
const targetInfo=targetInfoStore.info
const userInfo=userInfoStore.info
const targetNameData=ref('we')
const targetId=ref(-1)
const leaveMessageList=ref([
  {
    fromUserId:userInfo.userId,
    userId:-1,
    content:'我真是个大帅哥',
    messageTime:"11.22",
    nickName: 'null'
  }

])
const leaveMessageData=ref({
  fromUserId:userInfo.userId,
  userId:targetId.value,
  content:'',
})
const getTargetName=()=>{
  if(targetInfoStore.info.leaveMessageId===-1){
    targetNameData.value=userInfo.nickName
    targetId.value=userInfo.userId
  }
  else{
    targetNameData.value=targetInfo.leaveMessageName
    targetId.value=targetInfo.leaveMessageId
  }
}
const toLeaveMessageData=ref('')
const getLeaveMessages=async () => {

  let result = await getLeaveMessagesService(targetId.value)
  if(result.code===0){
    leaveMessageList.value=result.data
  }else{
    ElMessage.error(result.message ? result.message : '意外错误')
  }
}
const leaveMessage=async () => {

  console.log(leaveMessageData.value)
  leaveMessageData.value.userId=targetId.value
  let result = await leaveMessagesService(leaveMessageData.value)
  if(result.code===0){
    ElMessage.success( '留言成功')
    leaveMessageData.value.content=''

    await getLeaveMessages()
  }else{
    ElMessage.error(result.message ? result.message : '意外错误')
  }
}

onMounted(()=>{
  getTargetName()
  getLeaveMessages()
})

const formatMessageTime=(messageTime)=> {
  // 解析 ISO 8601 时间字符串
  const date = new Date(messageTime);
  // 获取年月日时分秒
  const year = date.getFullYear();
  const month = (date.getMonth() + 1).toString().padStart(2, '0'); // 月份是从 0 开始的，所以需要+1
  const day = date.getDate().toString().padStart(2, '0');
  const hours = date.getHours().toString().padStart(2, '0');
  const minutes = date.getMinutes().toString().padStart(2, '0');
  // 组合成月日时分的格式
  return `${month}-${day} ${hours}:${minutes}`;
}
</script>

<template>
    <div class="leave-message">
      <div class="leave-message-content">
        <h1>{{targetNameData}}的留言板</h1>
        <div class="leave-message-content-main">
          <div class="message-list">
            <div v-for="message in leaveMessageList" :key="message.messageTime" style="width: 100%;height: auto;" class="message-item">
              <div v-if="message.fromUserId===userInfo.userId" class="my-message">
                <div style="display: flex;flex-direction: column;  align-items: end;">
                  <span class="message-time">{{ message.nickName }}</span>
                  <span class="message-time">{{formatMessageTime(message.messageTime) }}</span>
                </div>
                <div class="message-bubble">
                  <span class="message-text">{{ message.content }}</span>
                </div>
              </div>
              <div v-else class="other-message">
                <div style="display: flex;flex-direction: column; /* 昵称在右侧 */">
                  <span class="message-time">{{ message.nickName }}</span>
                  <span class="message-time">{{ formatMessageTime(message.messageTime) }}</span>
                </div>
                <div class="message-bubble">
                  <span class="message-text">{{ message.messageText }}</span>
                </div>
              </div>
            </div>
          </div>
        </div>
        <div class="sendPage">
          <div class="contentInput">
            <el-input :rows="6"  type="textarea" class="contentInput0" v-model="leaveMessageData.content" @keyup.enter.shift.native="sendMessage()">
            </el-input>
          </div >
          <div class="send">
            <el-button class="sendButton" @click="leaveMessage()">
              发送
            </el-button>
          </div>
        </div>
      </div>
    </div>
</template>

<style lang="scss" scoped>

*{
  padding:0;
  margin:0;
  box-sizing: border-box;
}
.sendButton{
  width: 50px;
}
.leave-message{
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  background-image: linear-gradient(90deg, #e0c3fc 0%, #fad0c4 99%);
  width: 100%;
  height: 100%;
}
.leave-message-content-main{
  margin-top: 50px;
  height: 60%;
}
.send{
  display: flex;
  flex-direction: column;
  align-items: end;
  position: absolute;
  top: 95px;
  right: 20px;
}
.sendPage{
  position: relative;
  height: auto;
}
.leave-message-content{
  position: relative;
  width: 60%;
  height: 100%;
  border-radius: 20px;
  background-color: #efcde6;
  -webkit-backdrop-filter: blur(5px);
  backdrop-filter: blur(50px);
  align-items: center;
  justify-content: center;
  padding: 20px;

  h1{
    margin-top: 20px;
    text-align: center;
  }
}
.message-item{
  display: flex;
  flex-direction: column;
  border-radius: 10px;
  border: 3px solid #ccc;
  padding: 10px;
}
</style>