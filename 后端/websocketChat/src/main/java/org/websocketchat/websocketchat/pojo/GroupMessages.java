package org.websocketchat.websocketchat.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import jakarta.annotation.sql.DataSourceDefinitions;
import lombok.Data;
import org.springframework.data.annotation.Id;

@Data
public class GroupMessages {
  @TableId(type = IdType.AUTO)
  private Long messageId;
  private Long senderId;
  private Long groupId;
  private String messageText;
  private java.sql.Timestamp messageTime;
  private String messageType;



}
