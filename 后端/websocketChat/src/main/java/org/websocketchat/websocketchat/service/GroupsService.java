package org.websocketchat.websocketchat.service;

import org.websocketchat.websocketchat.pojo.Result;

/**
 * @author ALL
 * @date 2024/4/23
 * @Description
 */
public interface GroupsService {
    Result getGroupsList(Long userId);

    Result createGroups(Long userId,String groupName);

    Result deleteGroups(Long userId,Long groupId);

    Result addGroups(Long userId, Long groupId);

    Result agreeAddGroups(Long userId, Long groupId, Long groupOwner);

    Result getGroupsUserList(Long groupId);

    Result searchGroups(String groupName, Long groupId);

    Result deleteGroupsUser(Long userId, Long groupId, Long groupOwner);

    Result getGroupsApplyingList(Long groupId);

    Result quitGroups(Long userId, Long groupId);
}
