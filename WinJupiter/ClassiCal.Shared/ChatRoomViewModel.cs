using System;
using System.Collections.Generic;
using System.Collections.ObjectModel;
using System.Text;

namespace ClassiCal
{
    public class ChatRoomViewModel : VMBase
    {
        private ObservableCollection<ChatContent> _chatHistory = new ObservableCollection<ChatContent>();
        private ChatRoomModel _chatroomModel;
        public ObservableCollection<ChatContent> ChatHistory { get { return _chatHistory; } }

        public ChatRoomViewModel(string classID)
        {
            _chatroomModel = new ChatRoomModel(classID);
            _chatroomModel.MessageArrived += _chatroomModel_MessageArrived;
        }

        void _chatroomModel_MessageArrived(object sender, EventArgs e)
        {
            ChatContent content = ((ChatRoomMessageEventArgs)e).Content;
            _chatHistory.Add(content);
        }

        public void SendMessage(string content)
        {
            var chatContent = new ChatContent()
            {
                Content = content,
                Sender = "Me",
                SentTime = DateTime.Now,
                IsMe = true,
                Sent = false,
            };
            ChatHistory.Add(chatContent);
            _chatroomModel.SendMessage(chatContent);
        }

        public void ResendMessage(ChatContent chatContent)
        {
            _chatroomModel.SendMessage(chatContent);
        }
    }

}
