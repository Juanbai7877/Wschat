<script setup lang="ts">
import {ref} from "vue";
import {Lock, User} from "@element-plus/icons-vue";
import {resetPasswordService} from "@/api/user.js";
import {ElMessage} from "element-plus";
import router from "@/router/index.js";
import {useUserInfoStore }  from '@/store/userInfo.js'
const userInfoStore = useUserInfoStore();
const updatePasswordData = ref({
  userName:'',
  oldPassword: '',
  password: '',
  rePassword: ''
})

const checkRePassword = (rule, value, callback) => {
  if (value === '') {
    callback(new Error('请再次确认密码'))
  } else if (value !== updatePasswordData.value.password) {
    callback(new Error('请确保两次输入的密码一样'))
  } else {
    callback()
  }
}
const rules = {
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { min: 5, max: 16, message: '长度为5~16位非空字符', trigger: 'blur' }
  ],
  oldPassword: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { min: 5, max: 16, message: '长度为5~16位非空字符', trigger: 'blur' }
  ],
  rePassword: [
    { validator: checkRePassword, trigger: 'blur' }
  ],
}

const resetPassword = async () => {
  updatePasswordData.value.userName=userInfoStore.info.userName
  console.log(updatePasswordData.value)
  // 发送请求
  let result = await resetPasswordService(updatePasswordData.value)
  // 成功后跳转到登录页面
  if (result.code === 0) {
    //成功了
    ElMessage.success('修改成功')
    await router.push('/login')
  } else {
    // 失败后提示用户
    console.log(result.message)
    ElMessage.error(result.message ? result.message : '意外错误')
  }

}

</script>

<template>
  <div class="outer" style="width: 70%; margin: 25% auto">
    <el-form :model="updatePasswordData" :rules="rules">
      <el-form-item prop="oldPassword">
        <el-input type="password" placeholder="请输入旧密码" v-model="updatePasswordData.oldPassword" :suffix-icon='User' />
      </el-form-item>
      <el-form-item prop="password">
        <el-input type="password" placeholder="请输入新密码" v-model="updatePasswordData.password" :suffix-icon='Lock'/>
      </el-form-item>
      <el-form-item prop="rePassword">
        <el-input type="password" placeholder="请确认新密码" v-model="updatePasswordData.rePassword" :suffix-icon='Lock'/>
      </el-form-item>
    </el-form>
    <div>
      <el-button @click="resetPassword">
        修改
      </el-button>
    </div>

  </div>
</template>

<style lang="scss" scoped>


</style>