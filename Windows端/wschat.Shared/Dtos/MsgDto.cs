using wschat.Shared.Dtos;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Net;
using System.Security.Principal;
using System.Text;
using System.Threading.Tasks;

namespace wschat.Shared.Dtos
{
    public class MsgDto : BaseDto
    {
        private string senderId;

        private string messageText;

        private string messageTime;

        private string nickName;
        
        private string messageType;

        private string color;
        public string SenderId
        {
            get { return senderId; }
            set { senderId = value; OnPropertyChanged(); }
        }
        public string MessageTime
        {
            get { return messageTime; }
            set { messageTime = value; OnPropertyChanged(); }
        }

        public string MessageText
        {
            get { return messageText; }
            set { messageText = value; OnPropertyChanged(); }
        }
        public string NickName
        {
            get { return nickName; }
            set { nickName = value; OnPropertyChanged(); }
        }
        public string MessageType
        {
            get { return messageType; }
            set { messageType = value; OnPropertyChanged(); }
        }
        public string Color
        {
            get { return color; }
            set { color = value; OnPropertyChanged(); }
        }

    }
}
