package org.websocketchat.websocketchat.pojo;

import lombok.Data;

/**
 * @author ALL
 * @date 2024/5/13
 * @Description
 */
@Data
public class Messages {
    private Long messageId;
    private Long senderId;
    private String nickName;
    private String messageText;
    private java.sql.Timestamp messageTime;
    private String messageType;
}
