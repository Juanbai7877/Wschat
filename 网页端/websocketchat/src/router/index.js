import { createRouter, createWebHistory } from 'vue-router'

//导入组件
import LoginVue from '@/views/Login.vue'

import MainVue from "@/views/Main.vue";
import LayoutVue from "@/views/Layout.vue";
import FriendsVue from "@/views/Friends.vue";
import GroupsVue from "@/views/Groups.vue"
import ChatVue from "@/views/Chat.vue"
import MessageVue from "@/views/Message.vue"
import UserInfoVue from "@/views/UserInfo.vue"
import LeaveMessageVue from "@/views/LeaveMessage.vue"
import UserResetPasswordVue from "@/views/UserResetPassword.vue"


//定义路由关系

const routes = [
    { path: '/login', component: LoginVue },
    {
        path: '/', component: MainVue,redirect:'/login', children: [
            { path: '/layout', component: LayoutVue },
            { path: '/friends', component: FriendsVue },
            { path: '/groups', component: GroupsVue },
            { path: '/chat', component: ChatVue },
            { path: '/message', component: MessageVue },
            { path: '/userinfo', component: UserInfoVue },
            { path: '/leaveMessage', component: LeaveMessageVue },
            { path: '/userResetPassword', component: UserResetPasswordVue },
        ]
    }
]

//创建路由器
const router = createRouter({
    history: createWebHistory(),
    routes: routes
})

//导出路由
export default router
