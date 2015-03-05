using System;
using System.Collections.Generic;
using System.Text;
using System.Threading.Tasks;

namespace ClassiCal
{
    public class ChatRoomModel
    {
        // NOTE: Subject to change:
        private string _classID;

        public event EventHandler MessageArrived;
        public event EventHandler MessageSent;
        public event EventHandler MessageFailedToSend;

        public ChatRoomModel(string classID)
        {
            _classID = classID;
        }

        Random rnd = new Random();
        public async void SendMessage(ChatContent chatContent)
        {
            bool messageSent = false;
            if (rnd.NextDouble() < 0.8)
                messageSent = true;

            await Task.Delay(2000);

            chatContent.Sent = messageSent;
            chatContent.SendFailed = !messageSent;

            if (messageSent)
            {
                RaiseEvent(MessageSent, new ChatRoomMessageEventArgs(chatContent));
            }
            else
            {
                RaiseEvent(MessageFailedToSend, new ChatRoomMessageEventArgs(chatContent));
            }
        }

        private void RaiseEvent(EventHandler handler, EventArgs arg)
        {
            if (handler != null)
            {
                handler(this, arg);
            }
        }
    }

    public class ChatRoomMessageEventArgs : EventArgs
    {
        public ChatContent Content { get; private set; }

        public ChatRoomMessageEventArgs(ChatContent content = null)
        {
            Content = content;
        }
    }
}
