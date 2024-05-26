package org.websocketchat.websocketchat.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

@Data
public class LeaveMessage {
  @TableId(type = IdType.AUTO)
  private Long leaveMessageId;
  private Long userId;
  private Long fromUserId;
  private java.sql.Timestamp messageTime;
  private String title;
  private String content;


}
