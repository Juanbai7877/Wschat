<script setup>
import {
  Management,
  Promotion,
  UserFilled,
  User,
  Crop,
  EditPen,
  SwitchButton,
  CaretBottom, Edit, ChatLineRound, ChatRound
} from '@element-plus/icons-vue'
import avatar from '@/assets/waoku.jpg'
import {userInfoService} from '@/api/user.js'
import {useUserInfoStore} from '@/store/userInfo.js'
import {useTokenStore} from '@/store/token.js'
const tokenStore = useTokenStore();
const userInfoStore = useUserInfoStore();
//调用函数,获取用户详细信息
import {useRouter} from 'vue-router'
const router = useRouter();
import {ElMessage,ElMessageBox} from 'element-plus'
const handleCommand = (command)=>{
  //判断指令
  if(command === 'logout'){
    //退出登录
    ElMessageBox.confirm(
        '您确认要退出吗?',
        '温馨提示',
        {
          confirmButtonText: '确认',
          cancelButtonText: '取消',
          type: 'warning',
        }
    )
        .then(async () => {
          //退出登录
          //1.清空pinia中存储的token以及个人信息
          tokenStore.removeToken()
          userInfoStore.removeInfo()

          //2.跳转到登录页面
          router.push('/login')
          ElMessage({
            type: 'success',
            message: '退出登录成功',
          })

        })
        .catch(() => {
          ElMessage({
            type: 'info',
            message: '用户取消了退出登录',
          })
        })
  }else{
    //路由
    router.push('/'+command)

  }
}

const layoutShow=()=>{
  router.push('/layout')
}

</script>

<template>
  <el-container class="layout-container">
    <!-- 左侧菜单 -->
    <el-aside width="200px" >
      <div style="border:transparent">
        <el-icon style="margin: 20% auto 5% auto;display: block;font-size: 80px">
          <ChatRound @click="layoutShow()"/>
        </el-icon>
        <div style="font-size: 40px;margin: 0 auto 10% auto;text-align: center">WsChat</div>
      </div>

      <!-- element-plus的菜单标签 -->
      <el-menu active-text-color="#ffd04b" background-color="beige"  text-color="black"
               router>
        <el-menu-item index="/chat">
          <el-icon><ChatLineRound /></el-icon>
          <span>聊天</span>
        </el-menu-item>
        <el-menu-item index="/friends">
          <el-icon><Management /></el-icon>
          <span>好友列表</span>
        </el-menu-item>
        <el-menu-item index="/groups">
          <el-icon>
            <Promotion />
          </el-icon>
          <span>群组列表</span>
        </el-menu-item>
        <el-sub-menu >
          <template #title>
            <el-icon>
              <UserFilled />
            </el-icon>
            <span>个人中心</span>
          </template>
          <el-menu-item index="/userinfo">
            <el-icon>
              <User />
            </el-icon>
            <span>基本资料</span>
          </el-menu-item>
          <el-menu-item index="/leaveMessage">
            <el-icon>
              <Crop />
            </el-icon>
            <span>留言板</span>
          </el-menu-item>
          <el-menu-item index="/userResetPassword">
            <el-icon>
              <EditPen />
            </el-icon>
            <span>重置密码</span>
          </el-menu-item>
        </el-sub-menu>
      </el-menu>
    </el-aside>
    <!-- 右侧主区域 -->
    <el-container class="mainViewRight" >
      <!-- 头部区域 -->
      <el-header>
        <div>
          <el-avatar src="https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png"></el-avatar>
        </div>
        <div>
          <strong>{{ userInfoStore.info.nickName }}</strong>
        </div>
        <!-- 下拉菜单 -->
        <!-- command: 条目被点击后会触发,在事件函数上可以声明一个参数,接收条目对应的指令 -->
        <el-dropdown placement="bottom-end" @command="handleCommand">
                    <span class="el-dropdown__box">
                        <el-icon>
                            <CaretBottom />
                        </el-icon>
                    </span>
          <template #dropdown>
            <el-dropdown-menu>
              <el-dropdown-item command="userinfo" :icon="User">基本资料</el-dropdown-item>
              <el-dropdown-item command="leaveMessage" :icon="Crop">留言板</el-dropdown-item>
              <el-dropdown-item command="userresetPassword" :icon="EditPen">重置密码</el-dropdown-item>
              <el-dropdown-item command="logout" :icon="SwitchButton">退出登录</el-dropdown-item>
            </el-dropdown-menu>
          </template>
        </el-dropdown>
      </el-header>
      <!-- 中间区域 -->
      <el-main class="mainView">
        <router-view></router-view>
      </el-main>
      <!-- 底部区域 -->
      <el-footer>wschat ©2024 Created by Quanyi Liang</el-footer>
    </el-container>
  </el-container>
</template>

<style scoped>


.mainViewRight{
  border: 1px solid gray;
  border-radius: 10px;
}

.layout-container {
  height: 100vh;

  .el-aside {
    background-color: beige;
    border-radius: 10px;
    border: gray 1px solid;


    .el-menu {
      border-right: none;
    }
  }

  .el-header {
    background-color: #fff;
    display: flex;
    align-items: center;
    justify-content: flex-end; /* 设置子元素从父容器的右边开始排列 */

    .el-avatar{

      margin-right: 10px;
    }

    .el-dropdown__box {
      display: flex;
      align-items: center;

      .el-icon {
        color: #999;
        margin-left: 10px;
      }

      &:active,
      &:focus {
        outline: none;
      }
    }
  }

  .el-footer {
    display: flex;
    align-items: center;
    justify-content: center;
    font-size: 14px;
    color: #666;
  }
}
.el-header {
  margin-bottom: 0;
}

/* 移除el-main的顶部外边距 */
.el-main {
  margin-top: 0;
  margin-left: 0;
}

/* 如果el-main有内边距，也一并移除 */
.el-main {
  padding: 0;
}

/* 使用Flexbox布局确保父容器没有间隙 */
.el-container {
  display: flex;
}

/* 确保el-header和el-main是el-container的直接子元素 */
.el-container > .el-header {
  margin-bottom: 0; /* 再次确保没有外边距 */
}

.el-container > .el-main {
  flex: 1; /* 让el-main占据剩余空间 */
}


.mainView{
  margin: 0;
  flex: 1;
  overflow: auto; /* 如果需要滚动条 */
}


</style>