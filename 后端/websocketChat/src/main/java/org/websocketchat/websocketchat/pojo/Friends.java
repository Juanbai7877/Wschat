package org.websocketchat.websocketchat.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import org.springframework.data.annotation.Id;

@Data
public class Friends {
  @TableId(type = IdType.AUTO)
  private Long friendId;
  private Long userId1;
  private Long userId2;
  private String status;
  private Integer messageNum;
}
