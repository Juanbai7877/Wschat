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
    public class GroupsViewModel : NavigationViewModel
    {
        private string title;

        public string Title
        {
            get { return title; }
            set { title = value; RaisePropertyChanged(); }
        }

        public DelegateCommand<string> ExecuteCommand { get; private set; }

        private GroupDto currentDto;

        /// <summary>
        /// 编辑选中/新增时对象
        /// </summary>
        public GroupDto CurrentDto
        {
            get { return currentDto; }
            set { currentDto = value; RaisePropertyChanged(); }
        }

        private bool isRightDrawerOpen;

        /// <summary>
        /// 右侧编辑窗口是否展开
        /// </summary>
        public bool IsRightDrawerOpen
        {
            get { return isRightDrawerOpen; }
            set { isRightDrawerOpen = value; RaisePropertyChanged(); }
        }

        private ObservableCollection<GroupDto> groups;

        public ObservableCollection<GroupDto> Groups
        {
            get { return groups; }
            set { groups = value; RaisePropertyChanged(); }
        }

        private ObservableCollection<GroupDto> searchGroups;

        public ObservableCollection<GroupDto> SearchGroups
        {
            get { return searchGroups; }
            set { searchGroups = value; RaisePropertyChanged(); }
        }

        public DelegateCommand<GroupDto> QuitGroupCommand { get; private set; }

        public DelegateCommand<GroupDto> AddGroupCommand { get; private set; }

        public DelegateCommand<GroupDto> ChatCommand { get; private set; }


        private readonly IGroupService groupService;

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

























        public GroupsViewModel(IContainerProvider provider,
            IDialogHostService dialog) : base(provider)
        {
            Title = $"你好，{AppSession.UserName} {DateTime.Now.GetDateTimeFormats('D')[1].ToString()}";
            ExecuteCommand = new DelegateCommand<string>(Execute);
            searchGroups = new ObservableCollection<GroupDto>();
            groups = new ObservableCollection<GroupDto>();
            QuitGroupCommand = new DelegateCommand<GroupDto>(Complted);
            AddGroupCommand = new DelegateCommand<GroupDto>(AddGroupAsync);
            ChatCommand = new DelegateCommand<GroupDto>(Chat);
            this.groupService = provider.Resolve<IGroupService>();
            RefleshGroupsAsync();
            dialogHost = provider.Resolve<IDialogHostService>();

            QuitGroupCommand = new DelegateCommand<GroupDto>(QuitGroupAsync);
            this.regionManager = provider.Resolve<IRegionManager>();
            this._aggregator = provider.Resolve<IEventAggregator>();


        }

        private void Chat(GroupDto obj)
        {

            if (obj == null)
            {
                return;
            }

            AppSession.GroupId = obj.GroupId;
            AppSession.FriendId = -1;
            AppSession.TargetName = obj.GroupName;
            _aggregator.GetEvent<ChatEvent>().Publish(new ChatModel
            {
                Message = "ChatView"
            });
        }





        private async void AddGroupAsync(GroupDto obj)
        {
            try
            {
               
                var updateResult = await groupService.AddGroup(obj);
                if (updateResult.Status)
                {
                    RefleshGroupsAsync();
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

        private async void QuitGroupAsync(GroupDto obj)
        {
            try
            {
                var dialogResult = await dialogHost.Question("温馨提示", $"确认退出该群:{obj.GroupName} ?");
                if (dialogResult.Result != Prism.Services.Dialogs.ButtonResult.OK) return;
                var updateResult = await groupService.DeleteAsync(obj.GroupId);
                if (updateResult.Status)
                {
                    RefleshGroupsAsync();
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

        private async void RefleshGroupsAsync()
        {
            
            groups.Clear();


            try
            {


                var updateResult = await groupService.SerachGroup();
                if (updateResult.Status)
                {
                    foreach (var item in updateResult.Data)
                    {
                        groups.Add(item);
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





        private async void SearchGroupAsync()
        {
            searchGroups.Clear();
            try
            {

                long outputLong;
                bool success = long.TryParse(search, out outputLong);

                GroupDto searchGroup = new GroupDto
                {
                    groupName = search,
                    groupId = outputLong
                };
                var updateResult = await groupService.SerachGroup(searchGroup);
                if (updateResult.Status)
                {
                    foreach (var item in updateResult.Data)
                    {
                        searchGroups.Add(item);
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
                case "新增": Add(); break;
                case "新建群组": CreateGroupAsync(); break;
                case "查询": SearchGroupAsync(); break;

            }
        }

        private async void CreateGroupAsync()
        {
            if (string.IsNullOrWhiteSpace(CurrentDto.GroupName))
            {
                return;
            }  
            UpdateLoading(true);
            try
            {
                
                
                var updateResult = await groupService.CreateGroups(CurrentDto);
                if (updateResult.Status)
                {
                    RefleshGroupsAsync();
                }
                IsRightDrawerOpen = false;
            }
            catch (Exception ex)
            {

            }
            finally
            {
                UpdateLoading(false);
            }
        }

        private void Add()
        {
            CurrentDto = new GroupDto();
            IsRightDrawerOpen = true;
        }


        private async void Complted(GroupDto obj)
        {
            try
            {
                UpdateLoading(true);
                var updateResult = await groupService.DeleteAsync(obj.groupId);
                if (updateResult.Status)
                {
                    var todo = groups.FirstOrDefault(t => t.GroupId.Equals(obj.GroupId));
                    if (todo != null)
                    {
                        groups.Remove(todo);
                        RefleshGroupsAsync();
                    }
                    aggregator.SendMessage("成功退出!");
                }
            }
            finally
            {
                UpdateLoading(false);
            }
        }

    }
}
