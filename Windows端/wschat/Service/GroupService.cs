using wschat.Shared.Parameters;
using Prism.Common;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Text.RegularExpressions;
using System.Threading.Tasks;
using wschat.Shared.Contact;
using wschat.Shared.Dtos;
using System.Collections.ObjectModel;
using wschat.Common;

namespace wschat.Service
{
    public class GroupService : BaseService<GroupDto>, IGroupService
    {

        private readonly HttpRestClient client;
        private readonly string serviceName = "groups";

        public GroupService(HttpRestClient client) :base(client,"Group")
        {
            this.client = client;
        }

        public async Task<ApiResponse<ObservableCollection<GroupDto>>> SerachGroup(GroupDto group)
        {
            BaseRequest request = new BaseRequest();
            request.Method = RestSharp.Method.GET;
            request.Route = $"{serviceName}/searchGroups";
            request.Parameter = group;
            return await client.ExecuteAsync<ObservableCollection<GroupDto>>(request);
        }

        public async Task<ApiResponse> AddGroup(GroupDto group)
        {
            BaseRequest request = new BaseRequest();
            request.Method = RestSharp.Method.GET;
            request.Route = $"{serviceName}/addGroups";
            var groups = new { userId = AppSession.UserId, groupId = group.GroupId };
            request.Parameter = groups;
            return await client.ExecuteAsync(request);
        }

        public async Task<ApiResponse> CreateGroups(GroupDto group)
        {
            BaseRequest request = new BaseRequest();
            request.Method = RestSharp.Method.GET;
            request.Route = $"{serviceName}/createGroups";
            group.GroupOwner = AppSession.UserId;
            request.Parameter = group;
            return await client.ExecuteAsync(request);
        }

        public async Task<ApiResponse> Resgiter(GroupDto group)
        {
            BaseRequest request = new BaseRequest();
            request.Method = RestSharp.Method.POST;
            request.Route = $"{serviceName}/register";
            request.Parameter = group;

            return await client.ExecuteAsync(request);
        }

        public async Task<ApiResponse> DeleteAsync(long groupId)
        {
            BaseRequest request = new BaseRequest();
            request.Method = RestSharp.Method.GET;
            request.Route = $"{serviceName}/quitGroups";
            var group = new { userId = AppSession.UserId, groupId = groupId };
            request.Parameter=group;
            return await client.ExecuteAsync(request);
        }

        public async Task<ApiResponse<ObservableCollection<GroupDto>>> SerachGroup()
        {
            BaseRequest request = new BaseRequest();
            request.Method = RestSharp.Method.GET;
            request.Route = $"{serviceName}/getGroupsList";
            var group = new { userId = AppSession.UserId };
            request.Parameter = group;
            return await client.ExecuteAsync<ObservableCollection<GroupDto>>(request);
        }
    }
}
