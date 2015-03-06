using System;
using System.Collections.Generic;
using System.Text;
using Windows.UI.Xaml;
using Windows.UI.Xaml.Media;

namespace ClassiCal
{
    public static class FrameworkElementExtensions
    {
        public static object TryFindResource(this FrameworkElement element, object resourceKey)
        {
            var currentElement = element;

            while (currentElement != null)
            {
                if (currentElement.Resources.ContainsKey(resourceKey))
                    return currentElement.Resources[resourceKey];
                else
                    currentElement = VisualTreeHelper.GetParent(currentElement) as FrameworkElement;
            }

            return Application.Current.Resources[resourceKey];
        }
    }
}
