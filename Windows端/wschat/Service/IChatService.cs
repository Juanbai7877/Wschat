using System;
using System.Collections.Generic;
using System.Collections.ObjectModel;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using wschat.Shared.Contact;
using wschat.Shared.Dtos;

namespace wschat.Service
{
    public interface IChatService : IBaseService<MsgDto>
    {
        public Task<ApiResponse<ObservableCollection<MsgDto>>> GetGroupMessage();
        public Task<ApiResponse<ObservableCollection<MsgDto>>> GetPrivateMessage();
        public Task<ApiResponse> SendGroupMessage(string content);
        public Task<ApiResponse> SendPrivateMessage(string sendMessages);
    }
}
