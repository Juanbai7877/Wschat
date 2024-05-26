using Prism.Events;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Data;
using System.Windows.Documents;
using System.Windows.Input;
using System.Windows.Media;
using System.Windows.Media.Imaging;
using System.Windows.Navigation;
using System.Windows.Shapes;
using System.Windows.Threading;
using wschat.Common.Events;
using wschat.Extensions;

namespace wschat.Views
{
    /// <summary>
    /// LoginView.xaml 的交互逻辑
    /// </summary>
    public partial class LoginView : UserControl
    {
        private DispatcherTimer _timer;
        private int CountdownSeconds = 60;
        public LoginView(IEventAggregator aggregator)
        {
            InitializeComponent();
            InitializeTimer();
            aggregator.ResgiterMessage(arg =>
            {
                LoginSnakeBar.MessageQueue.Enqueue(arg.Message);
            }, "Login");
            aggregator.GetEvent<CodeEvent>().Subscribe(SendCodeButton_Click);
        }

        private void InitializeTimer()
        {
            _timer = new DispatcherTimer();
            _timer.Interval = TimeSpan.FromSeconds(1);
            _timer.Tick += Timer_Tick;
        }

        private void SendCodeButton_Click(CodeModel codeModel)
        {
            if (_timer.IsEnabled) return;
            _timer.Start();
            SendVerificationCode();
        }

        private void SendVerificationCode()
        {
            SendCodeButton.IsEnabled = false;
        }

        private void Timer_Tick(object sender, EventArgs e)
        {
            if (CountdownSeconds == 0)
            {
                _timer.Stop();
                SendCodeButton.Content = "发送验证码";
                SendCodeButton.IsEnabled = true;
                CountdownSeconds = 60;
            }
            else
            {
                SendCodeButton.Content = CountdownSeconds.ToString();
                CountdownSeconds--;
            }
        }
    }
}
