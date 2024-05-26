<script setup>
import {reactive, ref} from "vue";
let flag=ref(true);
import {Lock ,User,Message} from "@element-plus/icons-vue";
import Logo1 from '@/assets/wuwu.jpeg';
import Logo2 from '@/assets/waoku.jpg';
import {useUserInfoStore} from '@/store/userInfo.js'
import {useTokenStore} from "@/store/token.js";
const userInfoStore = useUserInfoStore();
const userTokenStore = useTokenStore();
const userInfo = ref({...userInfoStore.info})
const formRefLogin = ref(null);
const formRefRegister = ref(null);
const imgList=ref([Logo1,Logo2])
//控制注册与登录表单的显示， 默认显示注册
const preRef=ref('');
const mySwitch = ()=>{
  if(flag.value){
    preRef.value.style.background='#c9e0ed'
    preRef.value.style.transform = 'translateX(100%)'
  }else {
    preRef.value.style.background='#edd4dc'
    preRef.value.style.transform = 'translateX(0%)'
  }
  flag.value=!flag.value
}


const registerData = ref({
  userName: '',
  password: '',
  rePassword: '',
  email:'',
  code:''
})

const loginData = ref({
  userName: '',
  password: ''
})

import {userRegisterService, userLoginService, sendCodeService} from '@/api/user.js'
import {ElMessage} from "element-plus";
import router from "@/router/index.js";
const register = async () => {
  if (!formRefRegister.value) return;
  formRefRegister.value.validate(async (valid) => {
    if (valid) {
      //registerData是一个响应式对象,如果要获取值,需要.value
      console.log("result.code")
      let result = await userRegisterService(registerData.value);
       if (result.code === 0) {
          //成功了
          alert(result.msg ? result.msg : '注册成功');
          mySwitch()
      }else{
          //失败了
          alert('注册失败')
      }
    }
    else{
      console.error('表单校验失败');
      ElMessage.error('请输入正确的格式');}
  });
  //alert(result.msg ? result.msg : '注册成功');
  // ElMessage.success(result.message ? result.message : '注册成功')
}

const login = async () => {
  if (!formRefLogin.value) return;
  formRefLogin.value.validate(async (valid) => {
    if (valid) {
      let result = await userLoginService(loginData.value);
      if (result.code === 0) {
        //成功了
        ElMessage.success( '登录成功')
        userInfoStore.setInfo(result.data)
        console.log(userInfoStore.info)
        console.log(userInfo)
        userTokenStore.setToken(result.data.token)
        await router.push('/layout')
      }else{
        //失败了
        console.log(result.message)
        ElMessage.error(result.message ? result.message : '意外错误')
      }}
    else{
      console.error('表单校验失败');
      ElMessage.error('请输入正确的格式');}
  });
}

const sendCode = async ()=>{
  let result = await sendCodeService(registerData.value.email);
  if(result.code===0){
    ElMessage.success(result.message ? result.message : '发送成功')
  }
  else ElMessage.error(result.message ? result.message : '发送失败')
}

//校验密码的函数
const checkRePassword = (rule, value, callback) => {
  if (value === '') {
    callback(new Error('请再次确认密码'))
  } else if (value !== registerData.value.password) {
    callback(new Error('请确保两次输入的密码一样'))
  } else {
    callback()
  }
}
function checkEmail(rule, value, callback) {
  // 邮箱的正则表达式，可以根据需要进行调整
  const emailRegex = /^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,6}$/;

  if (value === '') {
    // 如果邮箱为空，根据业务逻辑，可以选择返回true或false
    // 这里我们选择返回true，允许邮箱为空
    callback();
  } else {
    if (emailRegex.test(value)) {
      // 如果邮箱格式正确，调用callback函数并传入true
      callback();
    } else {
      // 如果邮箱格式不正确，调用callback函数并传入错误信息
      callback(new Error('邮箱格式不正确'));
    }
  }
}
//定义表单校验规则
const rules = {
  username: [
    { required: true, message: '请输入用户名', trigger: 'blur' },
    { min: 5, max: 16, message: '长度为5~16位非空字符', trigger: 'blur' }
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { min: 5, max: 16, message: '长度为5~16位非空字符', trigger: 'blur' }
  ],
  rePassword: [
    { validator: checkRePassword, trigger: 'blur' }
  ],
  email: [
    { required: true, message: '请输入邮箱', trigger: 'blur' },
    { validator: checkEmail, trigger: 'blur' }
  ]
}






const bubleCreate=()=>{
  // 获取body元素
  const body = document.getElementsByClassName('bigBox')[0]
  // 创建泡泡元素
  console.log(body)
  // const buble = document.createElement('span')
  const buble = document.createElement('span')
  buble.className='bubble'
  // 设置泡泡半径
  let r = Math.random()*5 + 25 //半径大小为25~30
  // 设置泡泡的宽高
  buble.style.width=r+'px'
  buble.style.height=r+'px'
  buble.style.position='fixed'
  buble.style.left=Math.random()*innerWidth+'px'

  console.log(buble.style)
  // 为body添加buble元素
  body.appendChild(buble)
  // 4s清除一次泡泡
  setTimeout(()=>{
    buble.remove()
  },4000)
}
setInterval(() => {
  bubleCreate()
}, 200);



</script>

<template>
  <!-- 最外层的大盒子 -->
  <div class="bigbigbox">
    <div class="bigBox">
      <span class="bubble" style="width: 25px; height: 25px; left: 100px;"></span>

      <div class="box">
        <!-- 滑动盒子 -->
        <div class="pre-box" ref="preRef">
          <h1>WELCOME</h1>
          <p>To WS chat !</p>
          <div class="img-box">
            <img src="@/assets/waoku.jpg" alt="">
          </div>
        </div>
        <!-- 注册盒子 -->
        <div class="register-form">
          <!-- 标题盒子 -->
          <div class="title-box">
            <h1>注册</h1>
          </div>
          <!-- 输入框盒子 -->
          <el-form :model="registerData" :rules="rules" ref="formRefRegister" class="register-box">
            <el-form-item prop="userName">
              <el-input type="text" placeholder="用户名" v-model="registerData.userName" :suffix-icon='User'/>
            </el-form-item>
            <el-form-item prop="password">
              <el-input class="input-ps" type="password" placeholder="密码" v-model="registerData.password" :suffix-icon='Lock'/>
            </el-form-item>
            <el-form-item prop="rePassword">
              <el-input class="input-ps" type="password" placeholder="确认密码" :suffix-icon='Lock' v-model="registerData.rePassword"/>
            </el-form-item>
            <el-form-item prop="email">
              <el-input type="text" placeholder="邮箱" v-model="registerData.email" :suffix-icon='Message'/>
            </el-form-item>
            <el-form-item style="width: auto;height: auto;" prop="code">
              <el-col :span="11">
                <el-input type="text" v-model="registerData.code" placeholder="验证码"/>
              </el-col>
              <el-col :span="2" style="">
                <el-button class="button" type="primary" auto-insert-space @click="sendCode">
                  发送验证码
                </el-button>
              </el-col>
            </el-form-item>
          </el-form>
          <!-- 按钮盒子 -->
          <div class="btn-box" style="margin:10px">
            <el-button class="button" type="primary" auto-insert-space @click="register">
              注册
            </el-button>
            <!-- 绑定点击事件 -->
            <p @click="mySwitch">已有账号?去登录</p>
          </div>
        </div>
        <!-- 登录盒子 -->
        <div class="login-form">
          <!-- 标题盒子 -->
          <div class="title-box">
            <h1>登录</h1>
          </div>
          <!-- 输入框盒子 -->
          <el-form :model="loginData" :rules="rules" ref="formRefLogin">
            <el-form-item prop="userName">
              <el-input type="text" placeholder="用户名" v-model="loginData.userName" :suffix-icon='User' />
            </el-form-item>
            <el-form-item prop="password">
              <el-input type="password" placeholder="密码" v-model="loginData.password" :suffix-icon='Lock'/>
            </el-form-item>
          </el-form>
          <!-- 按钮盒子 -->
          <div class="btn-box">
            <el-button class="button" type="primary" auto-insert-space @click="login">
              登陆
            </el-button>
            <!-- 绑定点击事件 -->
            <p @click="mySwitch">没有账号?去注册</p>
          </div>
        </div>
      </div>
    </div>
  </div>


</template>

<style scoped>

* {
  /* 去除浏览器默认内外边距 */
  margin: 0;
  padding: 0;
  box-sizing: border-box;
}
/* 去除input的轮廓 */
input {
  outline: none;
}

.bigbigbox{
  height: 100vh;
  overflow-x: visible;
  overflow-y: visible;
}

html,
.bigBox {
  height: 100%;
}

.bigBox {
  /* 溢出隐藏 */
  overflow-x: hidden;
  z-index: 2;
  display: flex;
  /* 渐变方向从左到右 */
  background: linear-gradient(to right, rgb(247, 209, 215), rgb(191, 227, 241));
}

:deep(.bubble){
  bottom: 20px;
  position: fixed;
  z-index: 0;
  border-radius: 50%;
  /* 径向渐变 */
  background: radial-gradient(circle at 72% 28%, #fff 3px, #ff7edf 8%, #5b5b5b, #aad7f9 100%);
  /* 泡泡内阴影 */
  box-shadow: inset 0 0 6px #fff,
  inset 3px 0 6px #eaf5fc,
  inset 2px -2px 10px #efcde6,
  inset 0 0 60px #f9f6de,
  0 0 20px #fff;
  /* 动画 */
  animation: myMove 4s linear infinite;
}


span {
  position: absolute;
  bottom: 0;
  background: radial-gradient(circle at 72% 28%, #fff 3px, #ff7edf 8%,#5b5b5b,#aad7f9 100%);
  box-shadow: inset 0 0 6px #fff,
  inset 3px 0 6px #eaf5fc,
  inset 2px -2px 10px #efcde6,
  inset 0 0 60px #f9f6de,
  0 0 20px #fff;
  border-radius: 50%;
  animation: myMove 4s linear infinite;
  transition: 2s;
}

@keyframes myMove {
  0% {
    transform: translateY(0%);
    opacity: 1;
  }

  50% {
    transform: translate(10%,-1000%);
  }

  75% {
    transform: translate(-20%,-1200%);
  }

  99% {
    opacity: .9;
  }

  100% {
    transform: translateY(-1800%) scale(1.5);
    opacity: 0;
  }
}

/* 最外层的大盒子 */
.box {
  width: 1050px;
  height: 600px;
  display: flex;
  overflow: visible;
  /* 相对定位 */
  position: relative;
  z-index: 2;
  margin: auto;
  /* 设置圆角 */
  border-radius: 8px;
  /* 设置边框 */
  border: 1px solid rgba(255, 255, 255, .6);
  /* 设置盒子阴影 */
  box-shadow: 2px 1px 19px rgba(0, 0, 0, .1);
}

/* 滑动的盒子 */
.pre-box {
  /* 宽度为大盒子的一半 */
  width: 50%;
  /* width: var(--width); */
  height: 100%;
  /* 绝对定位 */
  position: absolute;
  /* 距离大盒子左侧为0 */
  left: 0;
  /* 距离大盒子顶部为0 */
  top: 0;
  z-index: 99;
  border-radius: 4px;
  background-color: #edd4dc;
  box-shadow: 2px 1px 19px rgba(0, 0, 0, .1);
  /* 动画过渡，先加速再减速 */
  transition: 0.5s ease-in-out;
}

/* 滑动盒子的标题 */
.pre-box h1 {
  margin-top: 150px;
  text-align: center;
  /* 文字间距 */
  letter-spacing: 5px;
  color: white;
  /* 禁止选中 */
  user-select: none;
  /* 文字阴影 */
  text-shadow: 4px 4px 3px rgba(0, 0, 0, .1);
}

/* 滑动盒子的文字 */
.pre-box p {
  height: 30px;
  line-height: 30px;
  text-align: center;
  margin: 20px 0;
  /* 禁止选中 */
  user-select: none;
  font-weight: bold;
  color: white;
  text-shadow: 4px 4px 3px rgba(0, 0, 0, .1);
}


/* 图片盒子 */
.img-box {
  width: 200px;
  height: 200px;
  margin: 20px auto;
  /* 设置为圆形 */
  border-radius: 50%;
  /* 设置用户禁止选中 */
  user-select: none;
  overflow: hidden;
  box-shadow: 4px 4px 3px rgba(0, 0, 0, .1);
}

/* 图片 */
.img-box img {
  width: 100%;
  transition: 0.5s;
}

/* 登录和注册盒子 */
.login-form,
.register-form {
  flex: 1;
  height: 100%;
}

/* 标题盒子 */
.title-box {
  height: 200px;
  line-height: 280px;

}

/* 标题 */
.title-box h1 {
  text-align: center;
  color: white;
  /* 禁止选中 */
  user-select: none;
  letter-spacing: 5px;
  text-shadow: 4px 4px 3px rgba(0, 0, 0, .1);

}

/* 输入框盒子 */
.el-form {
  display: flex;
  /* 纵向布局 */
  flex-direction: column;
  /* 水平居中 */
  align-items: center;
}

/* 输入框 */
.el-form-item {
  width: 60%;
  height: 40px;


}
/* 在你的 CSS 文件中 */
.register-box {
  display: flex;
  flex-direction: column;
}

.el-form-item {
  margin-bottom: 13px; /* 也可以继续使用 margin 来控制间隔 */
}



/* 按钮盒子 */
.btn-box {
  display: flex;
  justify-content: center;
}

/* 按钮 */
button {
  width: 100px;
  height: 30px;
  margin: 0 7px;
  line-height: 30px;
  border: none;
  border-radius: 4px;
  background-color: #69b3f0;
  color: white;
}

/* 按钮悬停时 */
button:hover {
  /* 鼠标小手 */
  cursor: pointer;
  /* 透明度 */
  opacity: .8;
}

/* 按钮文字 */
.btn-box p {
  height: 30px;
  line-height: 30px;
  /* 禁止选中 */
  user-select: none;
  font-size: 14px;
  color: white;

}

.btn-box p:hover {
  cursor: pointer;
  border-bottom: 1px solid white;
}
</style>