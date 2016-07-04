package com.dong.http;

import android.app.Activity;
import android.os.Handler;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity {
    private Button button;
    private int count = 0;
    private Handler handler=new Handler(){
        public void handleMessage(android.os.Message msg){
            int result = msg.what;
            count+=result;
            if(count==3){
                textView.setText("下载完成");
            }
        };
    };
    private TextView textView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = (TextView) findViewById(R.id.textView);
        button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(){
                  @Override
                    public void run(){
                      DownLoad load = new DownLoad(handler);
                      load.downLoadFile("http://imgsrc.baidu.com/forum/pic/item/e1b80a7b02087bf4a8a2e433f2d3572c10dfcf48.jpg");
                  }
                }.start();

            }
        });
        //"http://imgsrc.baidu.com/forum/pic/item/e1b80a7b02087bf4a8a2e433f2d3572c10dfcf48.jpg";
    }
}
