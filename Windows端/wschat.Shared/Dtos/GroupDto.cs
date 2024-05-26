using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace wschat.Shared.Dtos
{
    public class GroupDto:BaseDto
    {
        public long groupId { set; get; }
        public long GroupId
        {
            get { return groupId; }
            set { groupId = value; OnPropertyChanged(); }
        }
        public string groupName { set; get; }
        public string GroupName
        {
            get { return groupName; }
            set { groupName = value; OnPropertyChanged(); }
        }
        public long groupOwner { set; get; }
        public long GroupOwner
        {
            get { return groupOwner; }
            set { groupOwner = value; OnPropertyChanged(); }
        }
        public long status { set; get; }
        public long Status
        {
            get { return status; }
            set { status = value; OnPropertyChanged(); }
        }

    }
}
