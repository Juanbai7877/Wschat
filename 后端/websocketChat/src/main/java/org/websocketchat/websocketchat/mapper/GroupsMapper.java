package org.websocketchat.websocketchat.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.websocketchat.websocketchat.pojo.Groups;
import org.websocketchat.websocketchat.pojo.Users;

import java.util.List;

/**
 * @author ALL
 * @date 2024/4/23
 * @Description
 */
@Mapper
public interface GroupsMapper extends BaseMapper<Groups> {


    @Select("select distinct group_name,groups.group_id\n" +
            "from groups,user_groups\n" +
            "where groups.group_id=user_groups.group_id \n" +
            "and user_groups.user_id=#{userId}\n" +
            ";")
    List<Groups> findListByUserId(Long userId);

    @Select("select groups.group_id,groups.group_name,groups.group_owner from groups,user_groups\n" +
            "where user_groups.user_id=#{userId} and user_groups.group_id=groups.group_id")
    List<Groups> selectListByUserId(long userId);

}
