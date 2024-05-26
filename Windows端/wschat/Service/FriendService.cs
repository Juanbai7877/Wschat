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
    public class FriendService : BaseService<FriendDto>, IFriendService
    {
        private readonly HttpRestClient client;
        private readonly string serviceName = "friend";
        private readonly string userServiceName = "user";

        public FriendService(HttpRestClient client) : base(client, "friend")
        {
            this.client = client;
        }

        public async Task<ApiResponse> AddFriend(FriendDto friend)
        {
            BaseRequest request = new BaseRequest();
            request.Method = RestSharp.Method.GET;
            request.Route = $"{serviceName}/addFriendDirect";
            var msg = new { userId1 = AppSession.UserId, userId2 = friend.UserId };
            request.Parameter = msg;
            return await client.ExecuteAsync(request);
        }

        public async Task<ApiResponse<ObservableCollection<FriendDto>>> GetFriends()
        {
            BaseRequest request = new BaseRequest();
            request.Method = RestSharp.Method.GET;
            request.Route = $"{serviceName}/getFriendsList";
            var msg = new { userId = AppSession.UserId };
            request.Parameter = msg;
            return await client.ExecuteAsync<ObservableCollection<FriendDto>>(request);
        }

        public async Task<ApiResponse> RemoveFriend(long content)
        {
            BaseRequest request = new BaseRequest();
            request.Method = RestSharp.Method.GET;
            request.Route = $"{serviceName}/removeFriend";
            var msg = new { userId1 = AppSession.UserId, userId2 = content };
            request.Parameter = msg;
            return await client.ExecuteAsync(request);
        }

        public async Task<ApiResponse<ObservableCollection<FriendDto>>> SerachFriends(string mssg)
        {
            BaseRequest request = new BaseRequest();
            request.Method = RestSharp.Method.GET;
            request.Route = $"{userServiceName}/searchUser";
            var msg = new { msg = mssg };
            request.Parameter = msg;
            return await client.ExecuteAsync<ObservableCollection<FriendDto>>(request);
        }
    }
}
