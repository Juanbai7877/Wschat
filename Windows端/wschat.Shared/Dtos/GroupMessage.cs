using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace wschat.Shared.Dtos
{
    public class GroupMessage: BaseDto
    {
        public long messageId { set; get; }
        public long MessageId
        {
            get { return messageId; }
            set { messageId = value; OnPropertyChanged(); }
        }
        public long senderId { set; get; }
        public long SenderId
        {
            get { return senderId; }
            set { senderId = value; OnPropertyChanged(); }
        }
        public long groupId { set; get; }
        public long GroupId
        {
            get { return groupId; }
            set { groupId = value; OnPropertyChanged(); }
        }
        public string messageText { set; get; }
        public string MessageText
        {
            get { return messageText; }
            set { messageText = value; OnPropertyChanged(); }
        }
        public DateTime messageTime { set; get; }
        public DateTime  MessageTime
        {
            get { return messageTime; }
            set { messageTime = value; OnPropertyChanged(); }
        }

        public string messageType { set; get; }
        public string MessageType
        {
            get { return messageType; }
            set { messageType = value; OnPropertyChanged(); }
        }
    }
}
