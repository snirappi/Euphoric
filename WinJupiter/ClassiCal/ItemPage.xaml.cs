using ClassiCal.Common;
using ClassiCal.Data;
using System;
using System.Collections.Generic;
using System.Collections.ObjectModel;
using System.IO;
using System.Linq;
using System.Runtime.InteropServices.WindowsRuntime;
using System.Windows.Input;
using Windows.Foundation;
using Windows.Foundation.Collections;
using Windows.Graphics.Display;
using Windows.UI;
using Windows.UI.Xaml;
using Windows.UI.Xaml.Controls;
using Windows.UI.Xaml.Controls.Primitives;
using Windows.UI.Xaml.Data;
using Windows.UI.Xaml.Input;
using Windows.UI.Xaml.Media;
using Windows.UI.Xaml.Navigation;
using WinRTXamlToolkit.Controls.Extensions;

// The Pivot Application template is documented at http://go.microsoft.com/fwlink/?LinkID=391641

namespace ClassiCal
{
    /// <summary>
    /// A page that displays details for a single item within a group.
    /// </summary>
    public sealed partial class ItemPage : Page
    {
        private readonly NavigationHelper navigationHelper;
        private ChatRoomViewModel _chatroomViewModel = null;
        public CommandBar chatroomAppBar = new CommandBar();

        public ChatRoomViewModel ChatroomViewModel
        {
            get { return this._chatroomViewModel; }
            private set { this._chatroomViewModel = value; }
        }

        public ItemPage()
        {
            this.InitializeComponent();

            this.navigationHelper = new NavigationHelper(this);
            this.navigationHelper.LoadState += this.NavigationHelper_LoadState;
            this.navigationHelper.SaveState += this.NavigationHelper_SaveState;

            CreateChatroomAppBars();

            chatroomScrollViewer.LayoutUpdated += chatroomScrollViewer_LayoutUpdated;
            listviewMessages.Loaded += (sender, e) => 
            {
                ((ScrollViewer)listviewMessages.GetFirstDescendantOfType<ScrollViewer>()).LayoutUpdated
                    += listviewMessage_LayoutUpdated;
            };

        }

        #region Chatroom ListView Logic
        void listviewMessage_LayoutUpdated(object sender, object e)
        {
            ((ScrollViewer)listviewMessages.GetFirstDescendantOfType<ScrollViewer>()).ChangeView(0f, double.MaxValue, 1f);
        }

        private int _chatroomMessagesLast = 0;
        void chatroomScrollViewer_LayoutUpdated(object sender, object e)
        {
            if (listviewMessages.Items.Count != _chatroomMessagesLast)
            {
                _chatroomMessagesLast = listviewMessages.Items.Count;
                chatroomScrollViewer.ChangeView(0f, double.MaxValue, 1f);
            }
        }

        private void CreateChatroomAppBars()
        {
            Binding sendEnabledBinding = new Binding()
            {
                Path = new PropertyPath("Text"),
                Source = tbMessageContent,
                Converter = new HasTextToBoolConverter()
            };
            AppBarButton sendButon = new AppBarButton();
            sendButon.Icon = new SymbolIcon(Symbol.Send);
            sendButon.Label = "Send";
            sendButon.SetBinding(AppBarButton.IsEnabledProperty, sendEnabledBinding);
            sendButon.Click += sendButon_Click;
            chatroomAppBar.PrimaryCommands.Insert(0, sendButon);
        }

        void sendButon_Click(object sender, RoutedEventArgs e)
        {
            ChatroomViewModel.SendMessage(tbMessageContent.Text);
            tbMessageContent.Text = String.Empty;
            // Supress the weird textbox gotfocus after presing resend
            listviewMessages.Focus(FocusState.Pointer);
        }

        private bool tbMessageContentIn = false;
        private void moveInTbMessageContent()
        {
            if (!tbMessageContentIn)
            {
                outerScrollGrid.Children.Remove(tbMessageContent);
                innerScrollGrid.Children.Add(tbMessageContent);
                tbMessageContent.Focus(FocusState.Keyboard);
                tbMessageContentIn = true;
            }
        }

        private void tbMessageContent_GotFocus(object sender, RoutedEventArgs e)
        {
            moveInTbMessageContent();
        }

        #endregion

        #region NavigationHelper
        /// <summary>
        /// Gets the <see cref="NavigationHelper"/> associated with this <see cref="Page"/>.
        /// </summary>
        public NavigationHelper NavigationHelper
        {
            get { return this.navigationHelper; }
        }

        /// <summary>
        /// Populates the page with content passed during navigation. Any saved state is also
        /// provided when recreating a page from a prior session.
        /// </summary>
        /// <param name="sender">
        /// The source of the event; typically <see cref="NavigationHelper"/>.
        /// </param>
        /// <param name="e">Event data that provides both the navigation parameter passed to
        /// <see cref="Frame.Navigate(Type, Object)"/> when this page was initially requested and
        /// a dictionary of state preserved by this page during an earlier
        /// session.  The state will be null the first time a page is visited.</param>
        private void NavigationHelper_LoadState(object sender, LoadStateEventArgs e)
        {
            ChatroomViewModel = new ChatRoomViewModel((string)e.NavigationParameter);
        }

        /// <summary>
        /// Preserves state associated with this page in case the application is suspended or the
        /// page is discarded from the navigation cache.  Values must conform to the serialization
        /// requirements of <see cref="SuspensionManager.SessionState"/>.
        /// </summary>
        /// <param name="sender">The source of the event; typically <see cref="NavigationHelper"/>.</param>
        /// <param name="e">Event data that provides an empty dictionary to be populated with
        /// serializable state.</param>
        private void NavigationHelper_SaveState(object sender, SaveStateEventArgs e)
        {
            // TODO: Save the unique state of the page here.
        }

        #region NavigationHelper registration

        /// <summary>
        /// The methods provided in this section are simply used to allow
        /// NavigationHelper to respond to the page's navigation methods.
        /// <para>
        /// Page specific logic should be placed in event handlers for the  
        /// <see cref="NavigationHelper.LoadState"/>
        /// and <see cref="NavigationHelper.SaveState"/>.
        /// The navigation parameter is available in the LoadState method 
        /// in addition to page state preserved during an earlier session.
        /// </para>
        /// </summary>
        /// <param name="e">Provides data for navigation methods and event
        /// handlers that cannot cancel the navigation request.</param>
        protected override void OnNavigatedTo(NavigationEventArgs e)
        {
            this.navigationHelper.OnNavigatedTo(e);
        }

        protected override void OnNavigatedFrom(NavigationEventArgs e)
        {
            this.navigationHelper.OnNavigatedFrom(e);
        }

        #endregion

        #endregion

        private void ContentRoot_SelectionChanged(object sender, SelectionChangedEventArgs e)
        {
            switch (ContentRoot.SelectedIndex)
            {
                case 0:
                    BottomAppBar = chatroomAppBar;
                    return;
                case 1:
                    BottomAppBar = null;
                    return;
                default:
                    throw new InvalidOperationException("This pivot has only 2 items.");
            }
        }

        private void btnChatroomResend_Click(object sender, RoutedEventArgs e)
        {
            ChatContent toResend = (ChatContent)((Button)sender).DataContext;
            // Hide Resend button
            toResend.SendFailed = false;
            ChatroomViewModel.ResendMessage(toResend);
        }

    }

    public class ChatBubbleDataTemplateSelector : DataTemplateSelector
    {
        protected override DataTemplate SelectTemplateCore(object item, DependencyObject container)
        {
            FrameworkElement element = container as FrameworkElement;
            if (element != null && item != null && item is ChatContent)
            {
                ChatContent taskitem = item as ChatContent;
                
                if (taskitem.IsMe)
                    return
                        element.TryFindResource("chatBubbleMe") as DataTemplate;
                else
                    return
                        element.TryFindResource("chatBubbleOthers") as DataTemplate;
            }
            return null;
        }
    }
}