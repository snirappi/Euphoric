using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Windows.UI.Xaml;
using Windows.UI.Xaml.Controls;

namespace ClassiCal
{
    public class ChatContent : VMBase
    {
        private bool _sent = false;
        private bool _sendFailed = false;

        public bool IsMe { get; set; }
        public bool Sent { get { return _sent; } set { SetProperty(ref _sent, value); } }
        public bool SendFailed { get { return _sendFailed; } set { SetProperty(ref _sendFailed, value); } }
        public string Sender { get; set; }
        public DateTime SentTime { get; set; }
        public string Content { get; set; }
    }
}
