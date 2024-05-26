<script setup>
import { ref ,onMounted} from 'vue'
import {useUserInfoStore} from '@/store/userInfo.js'
const userInfoStore = useUserInfoStore();
const formRef = ref(null);
const userInfo = ref({})
const rules = {
  nickName: [
    { required: true, message: '请输入用户昵称', trigger: 'blur' },
    {
      pattern: /^\S{2,18}$/,
      message: '昵称必须是2-18位的非空字符串',
      trigger: 'blur'
    }
  ],
  userName: [
    { required: true, message: '请输入用户名', trigger: 'blur' },
    {
      pattern: /^\S{5,32}$/,
      message: '用户名必须是5-32位的非空字符串',
      trigger: 'blur'
    }
  ],
  email:[
    { required: true, message: '请输入用户昵称', trigger: 'blur' }
  ],
}
//修改个人信息
import {userInfoService, userInfoUpdateService} from '@/api/user.js'
import {ElForm,ElFormItem, ElMessage} from 'element-plus'
const updateUserInfo = async ()=>{
  //表单验证
  if (!formRef.value) return;
  formRef.value.validate(async (valid) => {
    if (valid) {
      console.log('表单校验成功');
      // 在这里执行提交操作，如发送请求
      // 例如使用 fetch 或 axios
      console.log(userInfoStore.info.userId)
      let result = await userInfoUpdateService(userInfo.value);
      if (result.code === 0) {
        ElMessage.success(result.msg ? result.msg : '修改成功');
        userInfoStore.setInfo(userInfo.value)
      } else {
        ElMessage.error(result.msg ? result.msg : '意外错误');
      }
    } else {
      console.error('表单校验失败');
      ElMessage.error('请输入正确的格式失败');
    }
  });
  //调用接口

  //修改pinia中的个人信息
}

const getUserInfo = async ()=>{
  //调用接口
  console.log(userInfoStore.info.userId)
  let result = await userInfoService(userInfoStore.info.userId);
  if(result.code===0){
    userInfo.value = result.data;
  }
}
onMounted(() => {
  getUserInfo()
});


</script>
<template>
  <el-card>
    <template #header>
      <div class="header">
        <span>基本资料</span>
      </div>
    </template>
    <el-row>
      <el-col :span="12">
        <el-form ref="formRef" :model="userInfo" :rules="rules" label-width="100px" size="large">
          <el-form-item label="用户ID">
            <el-input v-model="userInfo.userId" disabled></el-input>
          </el-form-item>
          <el-form-item label="登录名称">
            <el-input v-model="userInfo.userName"></el-input>
          </el-form-item>
          <el-form-item label="用户昵称" prop="nickname">
            <el-input v-model="userInfo.nickName"></el-input>
          </el-form-item>
          <el-form-item label="用户邮箱" prop="email">
            <el-input v-model="userInfo.email"></el-input>
          </el-form-item>
          <el-form-item label="用户电话" prop="phone">
            <el-input v-model="userInfo.phone"></el-input>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="updateUserInfo">提交修改</el-button>
          </el-form-item>
        </el-form>
      </el-col>
    </el-row>
  </el-card>
</template>