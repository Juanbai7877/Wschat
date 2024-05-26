using System;
using System.Collections.Generic;
using System.Collections.ObjectModel;
using System.Linq;
using System.Reflection.Metadata;
using System.Text;
using System.Threading.Tasks;
using wschat.Common;
using wschat.Shared.Contact;
using wschat.Shared.Dtos;
using wschat.Shared.Parameters;

namespace wschat.Service
{
    public class ChatService : BaseService<MsgDto>, IChatService
    {
        private readonly HttpRestClient client;
        private readonly string serviceName = "chat";

        public ChatService(HttpRestClient client) : base(client, "Chat")
        {
            this.client = client;   
        }
        public async Task<ApiResponse<ObservableCollection<MsgDto>>> GetGroupMessage()
        {
            BaseRequest request = new BaseRequest();
            request.Method = RestSharp.Method.GET;
            request.Route = $"{serviceName}/getGroupsMessage";
            var groups = new { userId = AppSession.UserId, groupId = AppSession.GroupId };
            request.Parameter = groups;
            return await client.ExecuteAsync<ObservableCollection<MsgDto>>(request);
        }

        public async Task<ApiResponse<ObservableCollection<MsgDto>>> GetPrivateMessage()
        {
            BaseRequest request = new BaseRequest();
            request.Method = RestSharp.Method.GET;
            request.Route = $"{serviceName}/getPrivateMessageByNum";
            var groups = new { userId1 = AppSession.UserId, userId2 = AppSession.FriendId };
            request.Parameter = groups;
            return await client.ExecuteAsync<ObservableCollection<MsgDto>>(request);
        }

        public async Task<ApiResponse> SendGroupMessage(string content)
        {
            BaseRequest request = new BaseRequest();
            request.Method = RestSharp.Method.GET;
            request.Route = $"{serviceName}/sendGroupsMessage";
            var message = new { userId = AppSession.UserId, toGroupId = AppSession.GroupId, msg=content };
            request.Parameter = message;
            return await client.ExecuteAsync(request);
        }

        public async Task<ApiResponse> SendPrivateMessage(string sendMessages)
        {
            BaseRequest request = new BaseRequest();
            request.Method = RestSharp.Method.GET;
            request.Route = $"{serviceName}/sendPrivateMessage";
            var message = new { userId = AppSession.UserId, toUserId = AppSession.FriendId, msg = sendMessages };
            request.Parameter = message;
            return await client.ExecuteAsync(request);
        }
    }
}
