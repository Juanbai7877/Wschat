using wschat.Shared.Contact;
using wschat.Shared.Dtos;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace wschat.Service
{
    public interface ILoginService
    {


        Task<ApiResponse> Login(UserDto user);

        Task<ApiResponse> Resgiter(ResgiterUserDto user);
        Task<ApiResponse> ResgiterByMail(ResgiterUserDto resgiterUserDto);
        Task<ApiResponse> SendCode(string email);
    }
}
