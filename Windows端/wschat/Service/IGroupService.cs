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
using System.Security.Cryptography;

namespace wschat.Service
{
    public interface IGroupService : IBaseService<GroupDto>
    {
        Task<ApiResponse<ObservableCollection<GroupDto>>> SerachGroup(GroupDto groups);

        Task<ApiResponse> CreateGroups(GroupDto group);
        
        Task<ApiResponse>AddGroup(GroupDto group);

        Task<ApiResponse> Resgiter(GroupDto group);
        Task<ApiResponse> DeleteAsync(long groupId);
        Task<ApiResponse<ObservableCollection<GroupDto>>> SerachGroup();
    }
}
