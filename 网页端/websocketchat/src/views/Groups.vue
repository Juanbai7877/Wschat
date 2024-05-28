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
const userInfo = userInfoStore.info

const targetInfoStore = useTargetInfoStore();
const targetInfo=targetInfoStore.info
let createGroupFlag=ref(false)
const createGroup=ref({
  groupName: "",
})

const groups = ref([

])
//控制添加分类弹窗
const searchGroups=ref([

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
const deleteCategory = (row) => {
  //提示用户  确认框
  ElMessageBox.confirm(
      '要退出该群吗?',
      '温馨提示',
      {
        confirmButtonText: '确认',
        cancelButtonText: '取消',
        type: 'warning',
      }
  )
      .then(async () => {
        //调用接口
        let result = await deleteGroup(row.groupId,userInfo.userId);
        ElMessage({
          type: 'success',
          message: '删除成功',
        })
        //刷新列表
        await flashGroup();
      })
      .catch(() => {
        ElMessage({
          type: 'info',
          message: '用户取消了删除',
        })
      })
}

const toChat = (row) => {
  console.log(row.groupName)
  console.log(useTargetInfoStore().info)
  console.log(row.groupName)


  //跳转到聊天页面
  targetInfoStore.setTargetName(row.groupName)
  targetInfoStore.setTargetType('group')
  targetInfoStore.setGroupId(row.groupId)
  targetInfoStore.setFriendId(0)
  router.push('/chat')
}
const deleteGroupFlag=ref(false)


const showDeleteGroupDialog = (row) => {
  ElMessageBox.confirm(
      '你确认要退出该群吗?',
      '温馨提示',
      {
        confirmButtonText: '确认',
        cancelButtonText: '取消',
        type: 'warning',
      }
  )
      .then(async () => {
        //调用接口
        let result = await deleteGroupService(row.groupId,userInfo.userId);
        if(result.code===0){
          ElMessage.success( '退出成功')
        }
        else ElMessage.error(result.message ? result.message : '意外错误')
        console.log(result.data)
        await flashGroup()

      })
      .catch(() => {
        ElMessage({
          type: 'info',
          message: '用户取消了删除',
        })
      })

}

const flashGroup=async () => {
  console.log(userInfoStore.info.userId)
  console.log(userInfoStore.getInfo())
  let result = await reflashGroupService(userInfo.userId);
  console.log(result.data)
  groups.value=result.data;
}
onMounted(() => {
  flashGroup()
});
const searchStr=ref("")
const addGroup=async (row) => {
  let result = await addGroupService(userInfo.userId,row.groupId);
  if(result.code===0){
    ElMessage.success( '加入成功')
  }
  else ElMessage.error(result.message ? result.message : '意外错误')
  console.log(result.data)
  await flashGroup()
}
const searchGroup =async (searchStr) => {
  console.log(searchStr.value)
  console.log(searchStr)
  let result = await getGroupService(searchStr);
  console.log(result.data)
  searchGroups.value=result.data;
}


const createGroup_fnc=async () => {
  if(createGroup.value.groupName.length<=0){
    ElMessage.error('请输入群名')
    return;
  }
  let result = await createGroupService(createGroup.value.groupName,userInfo.userId);
  if(result.code===0){
    ElMessage.success( '创建成功');

  }
  else ElMessage.error(result.message ? result.message : '意外错误')
  console.log(result.data)
  await flashGroup()
}



</script>
<template style="overflow: hidden">
  <div class="flex-container">
  <el-container style="width: auto;height: 100%">
    <el-card class="page-container">
      <template #header>
        <div class="header">
          <span>群组列表</span>
          <div class="extra">
            <el-button type="primary" @click="flashGroup();">刷新</el-button>
          </div>
        </div>
      </template>
      <el-table class="groupList" :data="groups" style="width: 100%" @row-dblclick="toChat">
        <el-table-column label="GroupId"  prop="groupId" width="100"> </el-table-column>
        <el-table-column label="GroupName" prop="groupName"></el-table-column>
        <el-table-column label="GroupOwnerId" prop="groupOwner"></el-table-column>
        <el-table-column label="操作" width="100">
          <template #default="{ row }">
            <el-button :icon="Delete" circle plain type="danger" @click="showDeleteGroupDialog(row);"></el-button>
          </template>
        </el-table-column>
        <template #empty>
          <el-empty description="没有数据" />
        </template>
      </el-table>
      <el-dialog v-model="createGroupFlag" width="30%">
        <el-form :model="createGroup" label-width="100px" style="padding-right: 30px">
          <el-form-item label="群名称" prop="categoryAlias">
            <el-input v-model="createGroup.groupName" minlength="1" maxlength="15"></el-input>
          </el-form-item>
        </el-form>
        <template #footer>
                <span class="dialog-footer">
                    <el-button @click="createGroupFlag = false">取消</el-button>
                    <el-button type="primary" @click="createGroup_fnc();createGroupFlag = false"> 确认 </el-button>
                </span>
        </template>
      </el-dialog>
    </el-card>
  </el-container>
  <el-container class="searchGroupPage" style="width: 40%;height: 100%">
      <el-header class="searchPageHeader" style="width: 100%;height: 70px">
        <el-col :span="14"><div class="grid-content ep-bg-purple" />
          <el-input v-model="searchStr" placeholder="请输入群组名称" style="width: 100%" @keyup.enter.native="searchGroup(searchStr)"/>
        </el-col>
        <el-col :span="5" style="margin-left: 10px">
          <el-button :icon="Search" @click="searchGroup(searchStr)">
            搜索
          </el-button>
        </el-col>
        <el-col :span="5" style="margin-right: 20px">
          <el-button @click="createGroupFlag=true;">
            建立群聊
          </el-button>
        </el-col>
      </el-header>
      <el-main style="height: auto">
        <el-table :data="searchGroups" class="searchGroupTable" height="100%" style="width: 100%;height: 100%">
          <el-table-column prop="groupId" label="GroupId" style="width: 20%" />
          <el-table-column prop="groupName" label="GroupName" style="width: auto;margin-left: 0px"  />
          <el-table-column label="操作" style="width: 60px">
            <template #default="{ row }">
              <el-button @click="addGroup(row)">
                加入该群
              </el-button>
            </template>
          </el-table-column>
        </el-table>
      </el-main>





  </el-container>
  </div>
</template>

<style lang="scss" scoped>
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