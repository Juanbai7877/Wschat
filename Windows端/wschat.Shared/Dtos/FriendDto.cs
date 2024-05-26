using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;



namespace wschat.Shared.Dtos
{
    public class FriendDto : BaseDto
    {
        private long userId;
        private string userName;
        private string nickName;
        private string email;
        private string avatarUrl;

        public long UserId
        {
            get { return userId; }
            set { userId = value; OnPropertyChanged(); }
        }
        public string UserName
        { get { return userName; } set {  userName = value; OnPropertyChanged(); } }
        public string NickName
        { get { return nickName; } set {  nickName = value; OnPropertyChanged(); } }
        public string Email
        { get { return email; } set {  email = value; OnPropertyChanged(); } }
        public string AvatarUrl
        { get { return avatarUrl; } set {  avatarUrl = value; OnPropertyChanged(); } }



    }
}
