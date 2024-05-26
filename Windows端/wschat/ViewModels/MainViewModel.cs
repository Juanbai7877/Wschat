using Prism.Commands;
using Prism.Ioc;
using Prism.Mvvm;
using Prism.Regions;
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
using System.ComponentModel;
using wschat.Common.Events;
using Prism.Events;

namespace wschat.ViewModels
{
    public class MainViewModel : BindableBase, IConfigureService, INotifyPropertyChanged
    {
        private string userNickName;

        private UserDto loginUser;
        public UserDto LoginUser
        {
            get { return loginUser; }
            set { loginUser = value; RaisePropertyChanged(); }
        }

        public string UserNickName
        {
            get { return userNickName; }
            set { userNickName = value; RaisePropertyChanged(); }
        }

        public DelegateCommand LoginOutCommand { get; private set; }
        public DelegateCommand<MenuBar> NavigateCommand { get; private set; }
        public DelegateCommand GoBackCommand { get; private set; }
        public DelegateCommand GoForwardCommand { get; private set; }

        private ObservableCollection<MenuBar> menuBars;
        private readonly IContainerProvider containerProvider;
        private readonly IRegionManager regionManager;
        private IRegionNavigationJournal journal;

        public ObservableCollection<MenuBar> MenuBars
        {
            get { return menuBars; }
            set { menuBars = value; RaisePropertyChanged(); }
        }

        private MenuBar selectedItem;
        public MenuBar SelectedItem
        {
            get { return selectedItem; }
            set
            {
                if (selectedItem != value)
                {
                    selectedItem = value;
                    RaisePropertyChanged();
                }
            }
        }
        private readonly IEventAggregator _aggregator;










        public MainViewModel(IContainerProvider containerProvider,
            IRegionManager regionManager)
        {
            MenuBars = new ObservableCollection<MenuBar>();
            NavigateCommand = new DelegateCommand<MenuBar>(Navigate);
            GoBackCommand = new DelegateCommand(() =>
            {
                if (journal != null && journal.CanGoBack)
                    journal.GoBack();
            });
            GoForwardCommand = new DelegateCommand(() =>
            {
                if (journal != null && journal.CanGoForward)
                    journal.GoForward();
            });
            LoginOutCommand = new DelegateCommand(() =>
            {
                //注销当前用户
                App.LoginOut(containerProvider);
            });
            this.containerProvider = containerProvider;
            this.regionManager = regionManager;

            this._aggregator = containerProvider.Resolve<IEventAggregator>();
            _aggregator.GetEvent<ChatEvent>().Subscribe(ToChat);
            _aggregator.GetEvent<UserChangeEvent>().Subscribe(UserChange);

        }

        private void UserChange(UserChangeModel obj)
        {
            userNickName = AppSession.NickName;
        }

        private void Navigate(MenuBar obj)
        {
            
            if (obj == null || string.IsNullOrWhiteSpace(obj.NameSpace))
                return;

            regionManager.Regions[PrismManager.MainViewRegionName].RequestNavigate(obj.NameSpace, back =>
            {
                journal = back.Context.NavigationService.Journal;
            });
        }

        public void ToChat(ChatModel chatModel)
        {
            // 假设 message 是你用来匹配项的属性
            MenuBar itemToSelect = menuBars.FirstOrDefault(i => i.NameSpace == chatModel.Message);
            if (itemToSelect != null)
            {
                SelectedItem = itemToSelect;
            }
            regionManager.Regions[PrismManager.MainViewRegionName].RequestNavigate("ChatView");
        }


        void CreateMenuBar()
        {
            MenuBars.Add(new MenuBar() { Icon = "NotebookOutline", Title = "聊天", NameSpace = "ChatView" });
            MenuBars.Add(new MenuBar() { Icon = "NotebookOutline", Title = "好友", NameSpace = "FriendView" });
            MenuBars.Add(new MenuBar() { Icon = "NotebookPlus", Title = "群组", NameSpace = "GroupsView" });
            MenuBars.Add(new MenuBar() { Icon = "Home", Title = "首页", NameSpace = "IndexView" });
            MenuBars.Add(new MenuBar() { Icon = "Cog", Title = "设置", NameSpace = "SettingsView" });

        }

        /// <summary>
        /// 配置首页初始化参数
        /// </summary>
        public void Configure()
        {
            userNickName = AppSession.UserName;
            CreateMenuBar();
            regionManager.Regions[PrismManager.MainViewRegionName].RequestNavigate("IndexView");

        }
    }
}
