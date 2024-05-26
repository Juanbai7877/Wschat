package org.websocketchat.websocketchat.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.websocketchat.websocketchat.pojo.UserGroups;

import java.util.List;

/**
 * @author ALL
 * @date 2024/4/24
 * @Description
 */
@Mapper
public interface UserGroupsMapper extends BaseMapper<UserGroups> {

    @Select("select * from user_groups " +
            "where user_id=#{userId} and group_id=#{groupId}")
    UserGroups selectByUserIdAndGroupId(long userId, long groupId);

    @Select("select user_groups.group_id,user_groups.user_id,user_groups.status from user_groups,groups where group_owner=#{userId} " +
            "and groups.group_id=user_groups.group_id " +
            "and status='applying'")
    List<UserGroups> selectApplyingByOwnerId(long userId);

}
