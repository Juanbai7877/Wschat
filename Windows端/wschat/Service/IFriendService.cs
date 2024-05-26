using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using wschat.Shared.Dtos;
using wschat.Shared.Contact;
using System.Collections.ObjectModel;

namespace wschat.Service
{
    public interface IFriendService : IBaseService<FriendDto>
    {

        public Task<ApiResponse<ObservableCollection<FriendDto>>> GetFriends();
        public Task<ApiResponse<ObservableCollection<FriendDto>>> SerachFriends(string msg);

        public Task<ApiResponse> RemoveFriend(long content);
        public Task<ApiResponse> AddFriend(FriendDto friend);

    }
}
