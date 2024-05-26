package org.websocketchat.websocketchat.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import org.springframework.data.annotation.Id;

@Data
public class PrivateMessages {
  @TableId(type = IdType.AUTO)
  private Long messageId;
  private Long senderId;
  private Long receiverId;
  private String messageText;
  private java.sql.Timestamp messageTime;
  private String messageType;
  private String messageStatus;



}
