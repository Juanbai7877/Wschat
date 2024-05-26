<script setup>
import {
  Edit,
  Delete,
  Search, Share, Upload,
} from '@element-plus/icons-vue'
import { ref ,onMounted } from 'vue'
import {useUserInfoStore }  from '@/store/userInfo.js'
import {useTargetInfoStore} from '@/store/targetInfo.js'
const userInfoStore = useUserInfoStore();

const targetInfoStore = useTargetInfoStore();

const targetInfo=targetInfoStore.info
const userInfo = userInfoStore.info
let createGroupFlag=ref(false)

const friends = ref([

])
//控制添加分类弹窗
const searchFriends=ref([

])

//调用接口,添加表单
import { ElMessage } from 'element-plus'
//删除分类
import {ElMessageBox} from 'element-plus'
import {
  addGroupService,
  createGroupService,
  deleteGroupService,
  getGroupService,
  reflashGroupService
} from "@/api/group.js";
import router from "@/router/index.js";
import {addFriendDirectService, flashFriendService, removeFriendService, searchFriendService} from "@/api/friend.js";
const showDeleteGroupDialog = (row) => {
  //提示用户  确认框
  ElMessageBox.confirm(
      '要删除该好友吗?',
      '温馨提示',
      {
        confirmButtonText: '确认',
        cancelButtonText: '取消',
        type: 'warning',
      }
  )
      .then(async () => {
        //调用接口
        console.log("test")
        let result = await deleteFriend(userInfo.userId,row.userId);
        //刷新列表
        await flashFreind();
      })
      .catch(() => {
        ElMessage({
          type: 'info',
          message: '用户取消了删除',
        })
      })
}

const toChat = (row) => {
  //跳转到聊天页面
  targetInfoStore.setTargetName(row.nickName)
  targetInfoStore.setTargetType('friend')
  targetInfoStore.setGroupId(0)
  targetInfoStore.setFriendId(row.userId)
  console.log(targetInfoStore.info)
  router.push('/chat')
}

const deleteFriend = async (userId1,userId2) => {
  let result = await removeFriendService(userId1,userId2);
  if(result.code===0){
    ElMessage.success( '删除成功')
  }
  else ElMessage.error(result.message ? result.message : '意外错误')
  console.log(result.data)
  await flashFreind()
}

const flashFreind=async () => {
  console.log(userInfoStore.info.userId)
  console.log(userInfoStore.getInfo())
  let result = await flashFriendService(userInfo.userId);
  console.log(result.data)
  friends.value=result.data;
}
onMounted(() => {
  flashFreind()
});
const searchStr=ref("")
const addFriend=async (row) => {
  let result = await addFriendDirectService(userInfo.userId,row.userId);
  if(result.code===0){
    ElMessage.success( '添加成功')
  }
  else ElMessage.error(result.message ? result.message : '意外错误')
  console.log(result.data)
  await flashFreind()
}
const searchFriend =async () => {
  console.log(searchStr.value)
  let result = await searchFriendService(searchStr.value);
  console.log(result.data)
  searchFriends.value=result.data;
}




</script>
<template style="overflow: hidden">
  <div class="flex-container">
    <el-container style="width: auto;height: 100%">
      <el-card class="page-container">
        <template #header>
          <div class="header">
            <span>好友列表</span>
            <div class="extra">
              <el-button type="primary" @click="flashFreind();">刷新</el-button>
            </div>
          </div>
        </template>
        <el-table class="groupList" :data="friends" style="width: 100%" @row-dblclick="toChat">
          <el-table-column label="FriendName"  prop="userName" class="friendNameStyle"> </el-table-column>
          <el-table-column label="NickName" prop="nickName" class="nickNameStyle"></el-table-column>
          <el-table-column label="FriendId" prop="userId" class="friendIdStyle"></el-table-column>
          <el-table-column label="操作" width="100">
            <template #default="{ row }">
              <el-button :icon="Delete" circle plain type="danger" @click="showDeleteGroupDialog(row)"></el-button>
            </template>
          </el-table-column>
          <template #empty>
            <el-empty description="没有数据" />
          </template>
        </el-table>
      </el-card>
    </el-container>
    <el-container class="searchGroupPage" style="width: 40%;height: 100%">
      <el-header class="searchPageHeader" style="width: 100%">
        <el-col :span="14"><div class="grid-content ep-bg-purple" />
          <el-input v-model="searchStr" placeholder="请输入好友昵称或用户名" style="width: 100%" @keyup.enter.native="searchFriend()"/>
        </el-col>
        <el-col :span="5" style="margin-left: 10px">
          <el-button :icon="Search" @click="searchFriend()">
            搜索
          </el-button>
        </el-col>
      </el-header>
      <el-main>
        <el-table :data="searchFriends" class="searchGroupTable" height="100%" style="width: 100%;height: 100%">
          <el-table-column label="FriendName"  prop="userName" class="friendNameStyle"> </el-table-column>
          <el-table-column label="NickName" prop="nickName" class="nickNameStyle"></el-table-column>
          <el-table-column label="UserId" prop="userId" class="friendIdStyle"></el-table-column>
          <el-table-column label="操作" style="width: 10%">
            <template #default="{ row }">
              <el-button @click="addFriend(row)">
                添加好友
              </el-button>
            </template>
          </el-table-column>
        </el-table>
      </el-main>
    </el-container>
  </div>
</template>

<style lang="scss" scoped>

.friendIdStyle{
  width: 25%;
}
.friendNameStyle{
  width: 35%;
}
.nickNameStyle{
  width: 30%;
}


.page-container {
  background-color: whitesmoke;
  min-height: 100%;
  box-sizing: border-box;
  width: 100%;
  .header {
    display: flex;
    align-items: center;
    justify-content: space-between;
  }
}

.groupList{
  background-color: whitesmoke;
}
.flex-container {
  display: flex; /* 使用Flexbox布局 */
  justify-content: space-between; /* 使两个容器水平排列，并且空间平均分布 */
  overflow: hidden;
  height: 100%
}
.searchGroupPage{
}

.el-row{
  display:flex;

}

.searchPageHeader {
  display: flex; /* 使用Flexbox布局 */
  justify-content: space-between; /* 元素之间平均分布空间 */
  align-items: center; /* 垂直居中对齐 */
}
.searchGroupTable{

}

</style>