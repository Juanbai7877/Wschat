using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace wschat.Common
{
    public static class AppSession 
    {
        public static string UserName { get; set; }
        public static string NickName { get; set; }
        public static long UserId { get; set; }
        public static string Token { get; set; }
        public static long GroupId { get; set; }
        public static long FriendId { get; set; }
        public static string TargetName { get; set; } = "无";

    }
}
