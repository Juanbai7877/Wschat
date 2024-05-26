package org.websocketchat.websocketchat.ws;

import com.fasterxml.jackson.databind.cfg.ConfigFeature;
import jakarta.servlet.http.HttpSession;
import jakarta.websocket.*;
import jakarta.websocket.server.ServerEndpoint;
import org.hibernate.validator.internal.util.stereotypes.ThreadSafe;
import org.springframework.stereotype.Component;
import org.websocketchat.websocketchat.config.GetHttpSessionConfig;
import org.websocketchat.websocketchat.utils.MessageUtil;

import java.io.IOException;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author ALL
 * @date 2024/4/19
 * @Description
 */
@Component
@ServerEndpoint(value = "/chat",configurator = GetHttpSessionConfig.class)
public class ChatEndPoint {

    private static final Map<String, Session> onlineUsers = new ConcurrentHashMap<>();
    private HttpSession httpSession;

    public ChatEndPoint(HttpSession httpSession) {
        this.httpSession = httpSession;
    }

    @OnOpen
    public void onOpen(Session session,EndpointConfig config) {
        //1. 将session保存
        this.httpSession = (HttpSession) config.getUserProperties().get(HttpSession.class.getName());
        String user = (String) this.httpSession.getAttribute("user");
        onlineUsers.put(user,session);
        //2. 广播消息
        String message= MessageUtil.getMessage(true,null,getFriends());

        broadcastAllUsers(message);


    }

    public Set getFriends(){
        Set<String> set = onlineUsers.keySet();
        return set;

    }


    public void broadcastAllUsers(String message){
        try {
            Set<Map.Entry<String, Session>> entries = onlineUsers.entrySet();
            for(Map.Entry<String, Session> entry : entries){
                Session session = entry.getValue();
                session.getBasicRemote().sendText(message);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @OnMessage
    public void onMessage(String message) {

    }

    @OnClose
    public void onClose(Session session) {
        //1. 从onlineUser剔除当前用户的session对象
        String user = (String) this.httpSession.getAttribute("user");

        onlineUsers.remove(user);
        //2. 通知其他所有用户当前用户下线

        String message= MessageUtil.getMessage(true,null,getFriends());

        broadcastAllUsers(message);
        //3.
    }

}
