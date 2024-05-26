using ImTools;
using Prism.Commands;
using Prism.Events;
using Prism.Ioc;
using Prism.Mvvm;
using System;
using System.CodeDom.Compiler;
using System.Collections.Generic;
using System.Collections.ObjectModel;
using System.ComponentModel;
using System.Globalization;
using System.Linq;
using System.Text;
using System.Text.RegularExpressions;
using System.Threading.Tasks;
using wschat.Common;
using wschat.Common.Events;
using wschat.Service;
using wschat.Shared.Dtos;

namespace wschat.ViewModels
{
    
    
    public class ChatViewModel : NavigationViewModel, INotifyPropertyChanged
    {
        private string targetNickName="未选择用户";
        public string TargetNickName
        {
            get { return targetNickName; }
            set { targetNickName = value; RaisePropertyChanged(); }
        }

        private ObservableCollection<MsgDto> messages;
        public ObservableCollection<MsgDto> Messages
        {
            get { return messages; }
            set { messages = value; RaisePropertyChanged(); }
        }

        private string sendMessages;
        public string SendMessages
        {
            get { return sendMessages; }
            set { sendMessages = value; RaisePropertyChanged(); }
        }

        private string targetName;
        public string TargetName
        {
            get { return targetName; }
            set { targetName = value; RaisePropertyChanged(); }
        }

        private readonly IEventAggregator _aggregator;

        public DelegateCommand<string> ExecuteCommand { get; private set; }


        private readonly IChatService chatService;








        public ChatViewModel(IContainerProvider provider,
            IDialogHostService dialog) : base(provider)
        {
            messages=new ObservableCollection<MsgDto>();
            ExecuteCommand = new DelegateCommand<string>(Execute);
            this._aggregator = provider.Resolve<IEventAggregator>();
            this.chatService = provider.Resolve<IChatService>();
            sendMessages = "";
            _aggregator.GetEvent<ChatEvent>().Subscribe(Flesh);

            RefleshMessages();
        }

        private void Flesh(ChatModel obj)
        {
            GetMessagesAsync();
            TargetNickName = AppSession.TargetName;

        }

        public async Task GetMessagesAsync()
        {
            messages.Clear();
           /* for ( int i = 0; i < 10; i++ )
            {
                messages.Add(new MsgDto()
                {
                    UserName = i.ToString(),
                    Content = i.ToString(),
                    Time = i.ToString()
                });
            }*/
            if(AppSession.GroupId == -1 && AppSession.FriendId == -1)
            {
                return;
            }

            else if(AppSession.GroupId == -1 && AppSession.FriendId != -1)
            {
                try
                {

                    var updateResult = await chatService.GetPrivateMessage();
                    if (updateResult.Status)
                    {
                        foreach (var item in updateResult.Data)
                        {
                            item.MessageTime = ConvertIsoDateTimeToString(item.MessageTime);
                            messages.Add(item);

                        }
                    }
                }
                catch (Exception ex)
                {
                }
                finally
                {
                }
            }
            else if (AppSession.GroupId != -1 && AppSession.FriendId == -1)
            {
                try
                {

                    var updateResult = await chatService.GetGroupMessage();
                    if (updateResult.Status)
                    {
                        foreach (var item in updateResult.Data)
                        {
                            item.MessageTime = ConvertIsoDateTimeToString(item.MessageTime);
                            messages.Add(item);
                            
                        }
                    }
                }
                catch (Exception ex)
                {
                }
                finally
                {
                }
            }
            else
            {
                messages.Add(new MsgDto()
                {
                    NickName = "error",
                    MessageText = "error",
                    MessageTime = "error",
                });
            }

            List<MsgDto> tempList = Messages.ToList();

            // 反转 List
            tempList.Reverse();

            // 清空当前 Messages 集合
            messages.Clear();

            // 将反转后的 List 元素添加回 ObservableCollection
            foreach (var item in tempList)
            {
                messages.Add(item);
            }
            var eventObject = aggregator.GetEvent<ScrollToBottomEvent>();
            if (eventObject != null)
            {
                eventObject.Publish(new ScrollToBottomModel { });
            }
            else
            {
                // 处理 GetEvent 返回 null 的情况
            }

        }

        public void RefleshMessages()
        {
            GetMessagesAsync();

        }

        private void Execute(string obj)
        {
            switch (obj)
            {
                case "刷新": RefleshMessages(); break;
                case "发送": GetSendMessagesAsync(); break;

            }
        }

        private async Task GetSendMessagesAsync()
        {
            if(sendMessages.Length <= 0)
            {
                return;
            }
            if (AppSession.GroupId == -1 && AppSession.FriendId == -1)
            {
                return;
            }

            else if (AppSession.GroupId == -1 && AppSession.FriendId != -1)
            {
                try
                {

                    var updateResult = await chatService.SendPrivateMessage(sendMessages);
                    if (updateResult.Status)
                    {
                        RefleshMessages();
                        SendMessages = "";
                    }
                }
                catch (Exception ex)
                {
                }
                finally
                {
                }
            }

            else if (AppSession.GroupId != -1 && AppSession.FriendId == -1)
            {
                try
                {

                    var updateResult = await chatService.SendGroupMessage(sendMessages);
                    if (updateResult.Status)
                    {
                        RefleshMessages();
                        SendMessages = "";
                    }
                }
                catch (Exception ex)
                {
                }
                finally
                {
                }
            }
        }

        public static string ConvertIsoDateTimeToString(string isoDateTimeString)
        {
            // 定义所需的格式
            string format = "某MM月某dd日 几点:几分";

            CultureInfo invariantCulture = CultureInfo.InvariantCulture;
            DateTimeStyles style = DateTimeStyles.AssumeUniversal | DateTimeStyles.AdjustToUniversal;

            // 使用 DateTimeOffset.ParseExact 方法来解析 ISO 8601 格式的字符串
            if (DateTimeOffset.TryParseExact(isoDateTimeString, "yyyy-MM-dd'T'HH:mm:ss.fffzzz",
                invariantCulture, style, out DateTimeOffset dateTimeOffset))
            {
                // 将 DateTimeOffset 转换为 DateTime（这里会丢失时区信息）
                DateTime dateTime = dateTimeOffset.UtcDateTime;

                // 替换格式字符串中的占位符
                string formattedDateString = format
                    .Replace("某MM",   dateTime.Month.ToString())
                    .Replace("某dd",  dateTime.Day.ToString())
                    .Replace("几点", dateTime.Hour.ToString())
                    .Replace("几分", dateTime.Minute.ToString());

                return formattedDateString;
            }

            // 如果无法解析，返回空字符串或错误信息
            return "无法解析日期时间字符串：" + isoDateTimeString;
        }
    }
}

