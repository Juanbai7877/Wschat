using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace wschat.Shared.Dtos
{
    public class UserDto : BaseDto
    {
        public string userName { get; set; }

        public string UserName
        {
            get { return userName; }
            set { userName = value; OnPropertyChanged(); }
        }

        public string nickName { get; set; }
        //用户账号

        public string NickName
        {
            get { return nickName; }
            set { nickName = value; OnPropertyChanged(); }
        }



        public string email { get; set; }
        public string   Email
        {
            get { return email; }
            set { email = value; OnPropertyChanged(); }
        }

        public string password { get; set; }

        public string Password
        {
            get { return password; }
            set { password = value; OnPropertyChanged(); }
        }
        public string rePassword { get; set; }

        public string RePassword
        {
            get { return rePassword; }
            set { rePassword = value; OnPropertyChanged(); }
        }
    }
}
