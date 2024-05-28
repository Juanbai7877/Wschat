<script setup>
import {
  Management,
  Promotion,
  UserFilled,
  User,
  Crop,
  EditPen,
  SwitchButton,
  CaretBottom, Document
} from '@element-plus/icons-vue'
import { ref ,onMounted } from 'vue'
import {useUserInfoStore }  from '@/store/userInfo.js'
import {useTargetInfoStore} from '@/store/targetInfo.js'
import {ElMessage} from "element-plus";
import {
  getGroupsMessageService,
  getPrivateMessageService,
  sendGroupsMessageService,
  sendPrivateMessageService
} from "@/api/chat.js";
const userInfoStore = useUserInfoStore();
const targetInfoStore = useTargetInfoStore();
const targetInfo=targetInfoStore.info
const userInfo = userInfoStore.info
const newMessageList=ref([
])
const messageList=ref([
  {
    "messageId":-1,
    "senderId":99999,
    "nickName":"wschat助手",
    "messageText":"欢迎使用wschat，您还未选取一名用户或群组聊天",
    "messageTime":'2024-05-25T07:15:31.440+00:00',
  }
])
const getMessage=()=>{
  console.log(messageList.value)
  console.log(newMessageList.value)
  if(targetInfo.targetType==='friend'){
    getFriendMessage();
  }
  else if(targetInfo.targetType==='group'){
    getGroupMessage();
  }else{
    return
  }
}

const getGroupMessage=async()=>{
  let result=await getGroupsMessageService(userInfo.userId,targetInfo.groupId)
  if(result.code===0){
    newMessageList.value=result.data
    console.log(newMessageList.value)
    if(messageList.value[0].messageId!==newMessageList.value[0].messageId){
      messageList.value=newMessageList.value
    }
      return;
  }
  else {
    ElMessage.error(result.message ? result.message : '意外错误')
  }
}

const  getFriendMessage=async()=>{
  let result=await getPrivateMessageService(userInfo.userId,targetInfo.friendId)
  if(result.code===0){
    newMessageList.value=result.data
    console.log(newMessageList.value)
    if(messageList.value[0].messageId!==newMessageList.value[0].messageId){
      messageList.value=newMessageList.value
    }
    return;
  }
  else {
    ElMessage.error(result.message ? result.message : '意外错误')
  }
}

const flashMessage=()=>{
  getMessage();
}

const message=ref('')

const SendPrivateMessage=async()=>{
  let result=await sendPrivateMessageService(userInfo.userId,targetInfo.friendId,message.value)
  if(result.code===0){
    message.value=''
    return;
  }
  else {
    ElMessage.error(result.message ? result.message : '意外错误')
  }
}

const SendGroupMessage=async()=>{
  let result=await sendGroupsMessageService(userInfo.userId,targetInfo.groupId,message.value)

  if(result.code===0){
    message.value=''
    return;
  }
  else {
    ElMessage.error(result.message ? result.message : '意外错误')
  }
}

const sendMessage=async()=>{
  if(message.value===''||message.value===null||message.value==='' +
      ''){
    return
  }
  if(targetInfo.targetType==='friend'){
    await SendPrivateMessage();
  }
  else if(targetInfo.targetType==='group'){
    await SendGroupMessage();
  }else{
    ElMessage.error("您还未选取一名好友或者群组")
  }
  flashMessage();
}

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

onMounted(async ()=>{
  getMessage();
})
setInterval(() => {
  getMessage();
}, 500);

</script>

<template>
  <div class="common-layout" style="height: 100%">
    <el-container class="mainPage">
      <el-header style="height: 25px;margin-top: 10px;">{{ targetInfo.targetName }}</el-header>
      <el-main class="chatPage">

        <div class="message-list">
          <div v-for="message in messageList" :key="message.messageTime" style="width: 100%;height: auto;">
            <div v-if="message.senderId===userInfo.userId" class="my-message">
                <div style="display: flex;flex-direction: column;  align-items: end; /* 昵称在右侧 */;">
                  <span class="message-time">{{ message.nickName }}</span>
                  <span class="message-time">{{formatMessageTime(message.messageTime) }}</span>
                </div>
                <div class="message-bubble">
                  <span class="message-text">{{ message.messageText }}</span>
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
        <div class="midSlice">

        </div>
      </el-main>

      <el-footer class="inputContainer">
        <div class="sendPage">
          <div class="contentInput">
            <el-input :rows="6"  type="textarea" class="contentInput0" v-model="message" @keyup.enter.shift.native="sendMessage()">
            </el-input>
          </div >
          <div class="send">
            <el-button @click="sendMessage()">
              发送
            </el-button>
          </div>
        </div>
      </el-footer>
    </el-container>
  </div>
</template>

<style scoped>
.el-textarea__inner,.el-input__inner {
  background: transparent !important;
}

.contentInput0{

  height: 100%;
}
.message-text{
  width: auto;
}

.inputContainer{
  height: 30%;
  border-radius: 3px;
  width: 100%;
  padding: 10px;
}

.contentInput {
 height: 100%;
}


.sendPage{
  height: 100%;
  position: relative;
}
.send{
  display: flex;
  flex-direction: column;
  align-items: end;
  position: absolute;
  top: 95px;
  right: 20px;
}




.mainPage{
  height: 100%;
}
.midSlice{
background-color: #2c3e50;
  width: 10%;
}

.chatPage {
  height: 40%;
  overflow-y: hidden; /* 添加滚动条 */
  display: flex;
  flex-direction: column;
  padding: 10px;
}

.message-list {
  flex: 1;
  display: flex;
  flex-direction: column-reverse;
  overflow: auto;
  width: auto;
  height: 100%;
  background-color: #f1daeb;
  border: 0px solid #ebeef5;
  border-radius: 5px;
  padding: 15px;
}


.message-bubble {
  border-radius: 18px;
  padding: 10px;
  max-width: 70%;
}
.my-message .message-bubble {
  background-color: rgba(46, 17, 65, 0.67); /* 绿色示例，您可以自定义颜色 */
  color: white;
}
.other-message .message-bubble {
  background-color: rgba(252, 251, 251, 0.75); /* 通常消息背景色 */
  color: #333;
}


.my-message {
  height: auto;
  display: flex;
  flex-direction: column;
  align-items: end;
  width: 100%;
  overflow: hidden;
}
/* 其他人的消息 */
.other-message {
  height: auto;
  display: flex;
  flex-direction: column;
  align-items: start;
  width: 100%;
  overflow: hidden;
}


:deep(.el-textarea__inner){
  background-color: rgba(152, 227, 238, 0.3);

}

</style>