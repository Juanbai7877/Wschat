using Prism.Commands;
using Prism.Ioc;
using Prism.Regions;
using Prism.Services.Dialogs;
using System;
using System.Collections.Generic;
using System.Collections.ObjectModel;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using wschat.Common.Models;
using wschat.Common;
using wschat.Extensions;
using wschat.Shared.Dtos;
using System.CodeDom.Compiler;
using System.Numerics;
using wschat.Service;
using MaterialDesignThemes.Wpf;
using System.Text.RegularExpressions;
using Prism.Events;
using wschat.Common.Events;

namespace wschat.ViewModels
{
    public class FriendViewModel : NavigationViewModel
    {
        private string title;

        public string Title
        {
            get { return title; }
            set { title = value; RaisePropertyChanged(); }
        }

        public DelegateCommand<string> ExecuteCommand { get; private set; }

        private FriendDto currentDto;

        /// <summary>
        /// 编辑选中/新增时对象
        /// </summary>
        public FriendDto CurrentDto
        {
            get { return currentDto; }
            set { currentDto = value; RaisePropertyChanged(); }
        }


        private ObservableCollection<FriendDto> friends;

        public ObservableCollection<FriendDto> Friends
        {
            get { return friends; }
            set { friends = value; RaisePropertyChanged(); }
        }

        private ObservableCollection<FriendDto> searchFriends;

        public ObservableCollection<FriendDto> SearchFriends
        {
            get { return searchFriends; }
            set { searchFriends = value; RaisePropertyChanged(); }
        }

        public DelegateCommand<FriendDto> RemoveFriendCommand { get; private set; }

        public DelegateCommand<FriendDto> AddFriendCommand { get; private set; }

        public DelegateCommand<FriendDto> ChatCommand { get; private set; }


        private readonly IFriendService friendService;

        public string search;

        public string Search
        {
            get { return search; }
            set { search = value; RaisePropertyChanged(); }
        }

        private readonly IDialogHostService dialogHost;
        private readonly IRegionManager regionManager;
        private IRegionNavigationJournal journal;
        private readonly IEventAggregator _aggregator;


























        public FriendViewModel(IContainerProvider provider,
            IDialogHostService dialog) : base(provider)
        {
            Title = $"你好，{AppSession.UserName} {DateTime.Now.GetDateTimeFormats('D')[1].ToString()}";
            ExecuteCommand = new DelegateCommand<string>(Execute);
            searchFriends = new ObservableCollection<FriendDto>();
            friends = new ObservableCollection<FriendDto>();
            RemoveFriendCommand = new DelegateCommand<FriendDto>(RemoveFriend);
            AddFriendCommand = new DelegateCommand<FriendDto>(AddFriend);
            ChatCommand = new DelegateCommand<FriendDto>(Chat);
            this.friendService = provider.Resolve<IFriendService>();
            RefleshFriendsAsync();
            dialogHost = provider.Resolve<IDialogHostService>();
            this._aggregator= provider.Resolve<IEventAggregator>();
            this.regionManager = provider.Resolve<IRegionManager>();


 


        }

        private void Chat(FriendDto obj)
        {
            if (obj == null)
            {
                return;
            }

            AppSession.GroupId = -1;
            AppSession.FriendId = obj.UserId;
            AppSession.TargetName = obj.NickName;
            _aggregator.GetEvent<ChatEvent>().Publish(new ChatModel
            {
                Message = "ChatView"
            });
        }





        private async void AddFriend(FriendDto obj)
        {
            try
            {

                var updateResult = await friendService.AddFriend(obj);
                if (updateResult.Status)
                {
                    RefleshFriendsAsync();
                }
                else
                {

                }
            }
            catch (Exception ex)
            {

            }
            finally
            {

            }
        }

        private async void RemoveFriend(FriendDto obj)
        {
            try
            {
                var dialogResult = await dialogHost.Question("温馨提示", $"确认删除好友:{obj.NickName} ?");
                if (dialogResult.Result != Prism.Services.Dialogs.ButtonResult.OK) return;
                var updateResult = await friendService.RemoveFriend(obj.UserId);
                if (updateResult.Status)
                {
                    RefleshFriendsAsync();
                }
                else
                {

                }
            }
            catch (Exception ex)
            {

            }
            finally
            {

            }
        }

        private async void RefleshFriendsAsync()
        {

            friends.Clear();


            try
            {


                var updateResult = await friendService.GetFriends();
                if (updateResult.Status)
                {
                    foreach (var item in updateResult.Data)
                    {
                        friends.Add(item);
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





        private async void SearchFriendsAsync()
        {
            searchFriends.Clear();
            try
            {

                long outputLong;
                bool success = long.TryParse(search, out outputLong);

                var updateResult = await friendService.SerachFriends(search);
                if (updateResult.Status)
                {
                    foreach (var item in updateResult.Data)
                    {
                        searchFriends.Add(item);
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

        private void Execute(string obj)
        {
            switch (obj)
            {
                case "搜索好友": SearchFriendsAsync(); break;

            }
        }


    }
}
