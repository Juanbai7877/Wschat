
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using wschat.Shared.Contact;
using wschat.Shared.Dtos;

namespace wschat.Service
{
    public class LoginService : ILoginService
    {
        private readonly HttpRestClient client;
        private readonly string serviceName = "user";

        public LoginService(HttpRestClient client)
        {
            this.client = client;
        }

        public async Task<ApiResponse> Login(UserDto user)
        {
            BaseRequest request = new BaseRequest();
            request.Method = RestSharp.Method.POST;
            request.Route = $"{serviceName}/login";
            request.Parameter = user;
            ApiResponse res= await client.ExecuteAsync(request);
            
            return res;
        }

        public async Task<ApiResponse> Resgiter(ResgiterUserDto user)
        {
            BaseRequest request = new BaseRequest();
            request.Method = RestSharp.Method.POST;
            request.Route = $"{serviceName}/register";
            request.Parameter = user;
            return await client.ExecuteAsync(request);
        }

        public async Task<ApiResponse> ResgiterByMail(ResgiterUserDto resgiterUserDto)
        {
            BaseRequest request = new BaseRequest();
            request.Method = RestSharp.Method.POST;
            request.Route = $"{serviceName}/registerByMail";
            request.Parameter = resgiterUserDto;
            return await client.ExecuteAsync(request);
        }

        public async Task<ApiResponse> SendCode(string email)
        {
            BaseRequest request = new BaseRequest();
            request.Method = RestSharp.Method.GET;
            request.Route = $"{serviceName}/codeByMail";
            var msg = new { mail = email };
            request.Parameter = msg;
            return await client.ExecuteAsync(request);
        }
    }
}
