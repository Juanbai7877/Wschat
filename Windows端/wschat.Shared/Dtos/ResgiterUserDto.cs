using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace wschat.Shared.Dtos
{
    public class ResgiterUserDto : BaseDto
    {
        public string userName { get; set; }
        public string code { get; set; }
        public string nickName { get; set; }
        public string email { get; set; }
        public string password { get; set; }
        public string rePassword { get; set; }
        public string UserName
        {
            get { return userName; }
            set { userName = value; OnPropertyChanged(); }
        }



        public string NickName
        {
            get { return nickName; }
            set { nickName = value; OnPropertyChanged(); }
        }



        public string Password
        {
            get { return password; }
            set { password = value; OnPropertyChanged(); }
        }



        public string RePassword
        {
            get { return rePassword; }
            set { rePassword = value; OnPropertyChanged(); }
        }



        public string Email
        {
            get { return email; }
            set { email = value; OnPropertyChanged(); }
        }
        public string Code
        {
            get { return code; }
            set { code = value; OnPropertyChanged(); }
        }
    }
}
