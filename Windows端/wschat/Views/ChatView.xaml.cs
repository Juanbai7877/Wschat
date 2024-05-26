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
using wschat.Common.Events;

namespace wschat.Views
{
    /// <summary>
    /// ChatView.xaml 的交互逻辑
    /// </summary>
    public partial class ChatView : UserControl
    {
        public ChatView(IEventAggregator aggregator)
        {
            InitializeComponent();
            aggregator.GetEvent<ScrollToBottomEvent>().Subscribe(ScrollToBottom);
        }


        public void ScrollToBottom(ScrollToBottomModel scroll)
        {
            if (messagesList.Items.Count > 0)
            {
                messagesList.ScrollIntoView(messagesList.Items[messagesList.Items.Count - 1]);

            }
            // 假设你有一个名为'listBox'的ListBox，并且你已经更新了它的ItemsSource

        }

    }
}
