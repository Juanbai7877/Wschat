using Newtonsoft.Json;
using RestSharp;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using wschat.Shared.Contact;

namespace wschat.Service
{
    public class HttpRestClient
    {
        private readonly string apiUrl;
        protected readonly RestClient client;

        public HttpRestClient(string apiUrl)
        {
            this.apiUrl = apiUrl;
            client = new RestClient();
        }

        public async Task<ApiResponse> ExecuteAsync(BaseRequest baseRequest)
        {
            var request = new RestRequest(baseRequest.Method);
/*            request.AddHeader("Content-Type", baseRequest.ContentType);*/
            if (baseRequest.Parameter != null)
                request.AddObject(baseRequest.Parameter);
/*                request.AddParameter("application/json", JsonConvert.SerializeObject(baseRequest.Parameter), ParameterType.RequestBody);
*//*            request.AddParameter("param", JsonConvert.SerializeObject(baseRequest.Parameter), ParameterType.RequestBody);*/
            client.BaseUrl = new Uri(apiUrl + baseRequest.Route);
            var response = await client.ExecuteAsync(request);
            if (response.StatusCode == System.Net.HttpStatusCode.OK)
            {
                ApiResponse res = JsonConvert.DeserializeObject<ApiResponse>(response.Content);
                res.Status = true;
                if(res.Code=="0")
                    return res;
                else
                {
                    res.Status=false;
                    res.Result = null;
                    return res;
                }
            }
               

            else
                return new ApiResponse()
                {
                    Status = false,
                    Result = null,
                    Message = response.ErrorMessage
                };
        }

        public async Task<ApiResponse<T>> ExecuteAsync<T>(BaseRequest baseRequest)
        {
            var request = new RestRequest(baseRequest.Method);
            request.AddHeader("Content-Type", baseRequest.ContentType);

            if (baseRequest.Parameter != null)
                request.AddObject(baseRequest.Parameter);
/*            request.AddParameter("param", JsonConvert.SerializeObject(baseRequest.Parameter), ParameterType.RequestBody);
*/           
            client.BaseUrl = new Uri(apiUrl + baseRequest.Route);
            var response = await client.ExecuteAsync(request);
            if (response.StatusCode == System.Net.HttpStatusCode.OK)
            {
                ApiResponse<T> res = JsonConvert.DeserializeObject<ApiResponse<T>>(response.Content);
                res.Status = true;
                if (res.Code == "0")
                    return res;
                else
                {
                    res.Status = false;
                    return res;
                }
            }
            else
                return new ApiResponse<T>()
                {
                    Status = false,
                    Message = response.ErrorMessage
                };
        }
    }
}
