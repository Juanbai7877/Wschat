package org.websocketchat.websocketchat.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import org.springframework.data.annotation.Id;

@Data
public class UserGroups {
  @TableId(type = IdType.AUTO)
  private Long userGroupsId;
  private Long userId;
  private Long groupId;
  private String status;
  private Integer messageNum;


}
