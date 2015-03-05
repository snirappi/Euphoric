package com.team3.classical.activities;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Button;
import android.widget.Toast;

import com.team3.classical.slidingtabs.R;

/**
 * Created by clevo on 2015/2/26.
 */
public class ClassSync  extends Activity {
    private static final String TAG = ClassSync.class.getSimpleName();

    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calendar);
    }

    public void startListener(View v) {
      /**  Button sync = (Button) v.findViewById(R.id.button);
        sync.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                syncCourses(courseNames, courseTime);
            }
        });**/
    }
    private int colors[] = {
            Color.rgb(0xee,0xff,0xff),
            Color.rgb(0xf0,0x96,0x09),
            Color.rgb(0x8c,0xbf,0x26),
            Color.rgb(0x00,0xab,0xa9),
            Color.rgb(0x99,0x6c,0x33),
            Color.rgb(0x3b,0x92,0xbc),
            Color.rgb(0xd5, 0x4d, 0x34),
            Color.rgb(0xcc,0xcc,0xcc)
    };

    public void syncCourses(char[][] names, int[] time) {
        LinearLayout ll1 = (LinearLayout)findViewById(R.id.ll1);
        LinearLayout ll2 = (LinearLayout)findViewById(R.id.ll2);
        LinearLayout ll3 = (LinearLayout)findViewById(R.id.ll3);
        LinearLayout ll4 = (LinearLayout)findViewById(R.id.ll4);
        LinearLayout ll5 = (LinearLayout)findViewById(R.id.ll5);
        //TODO adding classes and blanks
    }

    void setClass(LinearLayout ll, String title, String place,
                  String last, String time, int classes, int color)
    {
        View view = LayoutInflater.from(this).inflate(R.layout.item2, null);
        view.setMinimumHeight(dip2px(this,classes * 48));
        view.setBackgroundColor(colors[color]);
        ((TextView)view.findViewById(R.id.title)).setText(title);
        ((TextView)view.findViewById(R.id.place)).setText(place);
        ((TextView)view.findViewById(R.id.last)).setText(last);
        ((TextView)view.findViewById(R.id.time)).setText(time);
        view.setOnClickListener(new OnClickClassListener());
        TextView blank1 = new TextView(this);
        TextView blank2 = new TextView(this);
        blank1.setHeight(dip2px(this,classes));
        blank2.setHeight(dip2px(this,classes));
        ll.addView(blank1);
        ll.addView(view);
        ll.addView(blank2);
    }

    void setNoClass(LinearLayout ll,int classes, int color)
    {
        TextView blank = new TextView(this);
        if(color == 0)
            blank.setMinHeight(dip2px(this,classes * 50));
        blank.setBackgroundColor(colors[color]);
        ll.addView(blank);
    }

    class OnClickClassListener implements View.OnClickListener {

        public void onClick(View v) {
            String title;
            title = (String) ((TextView)v.findViewById(R.id.title)).getText();
            Toast.makeText(getApplicationContext(), "You click on " + title,
                    Toast.LENGTH_SHORT).show();
        }
    }

    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);}
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);}

}