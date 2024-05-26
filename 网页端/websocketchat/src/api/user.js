//导入request.js请求工具
import request from '@/utils/request.js'

//提供调用注册接口的函数
export const userRegisterService = (registerData)=>{
    //借助于UrlSearchParams完成传递
    const params = new URLSearchParams()
    for(let key in registerData){
        params.append(key,registerData[key]);
    }
    return request.post('/user/registerByMail',params);
}

//提供调用登录接口的函数
export const userLoginService = (loginData)=>{
    const params = new URLSearchParams();
    for(let key in loginData){
        params.append(key,loginData[key])
    }
    return request.post('/user/login',params)
}

export const sendCodeService = (email)=>{
    console.log('/user/codeByMail?mail='+email)
    return request.get('/user/codeByMail?mail='+email)
}


//获取用户详细信息
export const userInfoService = (userId)=>{
    return request.get('/user/userInfo?userId='+userId)
}

//修改个人信息
export const userInfoUpdateService = (userInfoData)=>{
    return request.put('/user/update',userInfoData)
}

//修改头像
export const userAvatarUpdateService = (avatarUrl)=>{
    const params = new URLSearchParams();
    params.append('avatarUrl',avatarUrl)
    return request.patch('/user/updateAvatar',params)
}

export const resetPasswordService= (updatePasswordData)=>{
    const params = new URLSearchParams();
    for(let key in updatePasswordData){
        params.append(key,updatePasswordData[key])
    }
    return request.post('/user/ResetPassword',params)
}