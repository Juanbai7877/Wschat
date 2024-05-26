package org.websocketchat.websocketchat.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import org.springframework.data.annotation.Id;


@Data
public class Users {
  @TableId(type = IdType.AUTO)
  private Long userId;
  private String userName;
  private String nickName;
  private String phone;
  private String passwordHash;
  private String email;
  private java.sql.Timestamp createdTime;
  private java.sql.Timestamp updatedTime;
  private String avatarUrl;



}
