using Prism.Commands;
using Prism.Events;
using Prism.Mvvm;
using Prism.Services.Dialogs;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Shapes;
using wschat.Service;
using wschat.Extensions;
using wschat.Shared.Dtos;
using wschat.Common;
using wschat.Shared.Contact;
using Newtonsoft.Json.Linq;
using System.Xml.Linq;
using wschat.Common.Events;

namespace wschat.ViewModels.Dialogs
{
    internal class LoginViewModel : BindableBase, IDialogAware
    {
        public LoginViewModel(ILoginService loginService, IEventAggregator aggregator)
        {
            UserDto = new ResgiterUserDto();
            ExecuteCommand = new DelegateCommand<string>(Execute);
            this.loginService = loginService;
            this.aggregator = aggregator;
        }

        private bool rememberMe;

        public bool RememberMe
        {
            get { return rememberMe; }
            set
            {
                if (rememberMe != value)
                {
                    rememberMe = value;
                    RaisePropertyChanged();
                }
            }
        }
        public string Title { get; set; } = "ToDo";

        public event Action<IDialogResult> RequestClose;

        public bool CanCloseDialog()
        {
            return true;
        }

        public void OnDialogClosed()
        {
            LoginOut();
        }

        public void OnDialogOpened(IDialogParameters parameters)
        {
        }

        #region Login

        private int selectIndex;

        public int SelectIndex
        {
            get { return selectIndex; }
            set { selectIndex = value; RaisePropertyChanged(); }
        }


        public DelegateCommand<string> ExecuteCommand { get; private set; }


        private string userName;

        public string UserName
        {
            get { return userName; }
            set { userName = value; RaisePropertyChanged(); }
        }

        private string password;
        private readonly ILoginService loginService;
        private readonly IEventAggregator aggregator;

        public string Password
        {
            get { return password; }
            set { password = value; RaisePropertyChanged(); }
        }

        private void Execute(string obj)
        {
            switch (obj)
            {
                case "Login": Login(); break;
                case "LoginOut": LoginOut(); break;
                case "Resgiter": ResgiterAsync(); break;
                case "ResgiterPage": SelectIndex = 1; break;
                case "ResgiterPageByMailPage": SelectIndex = 2; break;
                case "ResgiterByMail": ResgiterByMail(); break;
                case "SendCode": SendCodeAsync(); break;
                case "Return": SelectIndex = 0; break;
            }
        }

        private async Task ResgiterAsync()
        {

            if (string.IsNullOrWhiteSpace(UserDto.NickName) ||
                string.IsNullOrWhiteSpace(UserDto.UserName) ||
                string.IsNullOrWhiteSpace(UserDto.Password) ||
                string.IsNullOrWhiteSpace(UserDto.RePassword))
            {
                aggregator.SendMessage("请输入完整的注册信息！", "Login");
                return;
            }

            if (UserDto.Password != UserDto.RePassword)
            {
                aggregator.SendMessage("密码不一致,请重新输入！", "Login");
                return;
            }

            var resgiterResult = await loginService.Resgiter(new Shared.Dtos.ResgiterUserDto()
            {
                UserName = UserDto.UserName,
                NickName = UserDto.NickName,
                Password = UserDto.Password,
                RePassword = UserDto.RePassword,
            });

            if (resgiterResult != null && resgiterResult.Status)
            {
                aggregator.SendMessage("注册成功", "Login");
                //注册成功,返回登录页页面
                SelectIndex = 0;
            }
            else
                aggregator.SendMessage(resgiterResult.Message, "Login");
        }

        private async Task SendCodeAsync()
        {
            if (userDto.Email.Length <= 0)
            {
                return;
            }
            aggregator.GetEvent<CodeEvent>().Publish(new CodeModel
            {
                Message = "发送成功"
            });
            var loginResult = await loginService.SendCode(userDto.Email);

            if (loginResult != null && loginResult.Status)
            {
                
            }
            else
            {
                //登录失败提示...
                aggregator.SendMessage(loginResult.Message, "Login");
            }
        }

        private ResgiterUserDto userDto;

        public ResgiterUserDto UserDto
        {
            get { return userDto; }
            set { userDto = value; RaisePropertyChanged(); }
        }

        async void Login()
        {
            if (string.IsNullOrWhiteSpace(UserName) ||
                string.IsNullOrWhiteSpace(Password))
            {
                return;
            }

            var loginResult = await loginService.Login(new Shared.Dtos.UserDto()
            {
                UserName = UserName,
                Password = Password
            });

            if (loginResult != null && loginResult.Status)
            {
                AppSessionAid appsession = ((JObject)loginResult.Data).ToObject<AppSessionAid>();
                AppSession.UserId = appsession.UserId;
                AppSession.UserName= appsession.UserName;
                AppSession.NickName= appsession.NickName;
                AppSession.Token= appsession.Token;
                aggregator.GetEvent<UserChangeEvent>().Publish(new UserChangeModel { });
                RequestClose?.Invoke(new DialogResult(ButtonResult.OK));
            }
            else
            {
                //登录失败提示...
                aggregator.SendMessage(loginResult.Message, "Login");
            }
        }

        private async void ResgiterByMail()
        {
            if (string.IsNullOrWhiteSpace(UserDto.Code) ||
                string.IsNullOrWhiteSpace(UserDto.UserName) ||
                string.IsNullOrWhiteSpace(UserDto.Password) ||
                string.IsNullOrWhiteSpace(UserDto.RePassword)||
                string.IsNullOrWhiteSpace(UserDto.Email))
            {
                aggregator.SendMessage("请输入完整的注册信息！", "Login");
                return;
            }

            if (UserDto.Password != UserDto.RePassword)
            {
                aggregator.SendMessage("密码不一致,请重新输入！", "Login");
                return;
            }

            var resgiterResult = await loginService.ResgiterByMail(new Shared.Dtos.ResgiterUserDto()
            {
                UserName = UserDto.UserName,
                NickName = UserDto.NickName,
                Password = UserDto.Password,
                RePassword= UserDto.RePassword,
                Email = UserDto.Email,
                Code=userDto.Code
            });

            if (resgiterResult != null && resgiterResult.Status)
            {
                aggregator.SendMessage("注册成功", "Login");
                //注册成功,返回登录页页面
                SelectIndex = 0;
            }
            else
                aggregator.SendMessage(resgiterResult.Message, "Login");
        }

        void LoginOut()
        {
            RequestClose?.Invoke(new DialogResult(ButtonResult.No));
        }

        #endregion
    }

}
