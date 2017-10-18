package com.sanfeng.hotelbutler.aigestudiocustomvie_712;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class MainActivity extends AppCompatActivity {
  private MyCircleWaveView waveView;
   private int progress=0;//进度
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if (progress==100)progress=0;
            Log.i("progress",progress+"");
            waveView.setmProgress(80);
            waveView.setmTubiNum("1234.5");
           handler.sendEmptyMessageDelayed(0,100);
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        waveView=(MyCircleWaveView) findViewById(R.id.waveViewCircle);
        handler.sendEmptyMessageDelayed(0,100);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        waveView.setmProgress(0);
       handler.removeMessages(0);
    }
}
