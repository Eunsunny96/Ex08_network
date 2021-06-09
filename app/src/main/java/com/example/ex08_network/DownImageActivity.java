package com.example.ex08_network;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class DownImageActivity extends AppCompatActivity {

    ImageView img1;
    Button btnLoad;
    String imgUrl=Common.SERVER_URL+"/mobile/images/tomato.jpg";
    Bitmap bm;

    // 백그라운드 스레드의 화면변경 요청 처리를 위한 핸들러
    Handler handler=new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            img1.setImageBitmap(bm);
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_down_image);

        btnLoad=findViewById(R.id.btnLoad);
        img1=findViewById(R.id.img1);
        btnLoad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                downImg(imgUrl);
            }
        });
    }
    void downImg(final String file){
        //백그라운드 스레드
        Thread th=new Thread(new Runnable() {
            @Override
            public void run() {
                URL url=null;
                try {
                    url=new URL(file);
                    HttpURLConnection conn=
                            (HttpURLConnection)url.openConnection();
                    conn.connect();
                    //스트림 생성
                    InputStream is=conn.getInputStream();
                    //스트림으로부터 비트맵을 생성
                    bm= BitmapFactory.decodeStream(is);
                    //핸들러에게 화면 변경 요청
                    handler.sendEmptyMessage(0);
                    conn.disconnect(); //접속 해제
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
        th.start(); //백그라운드 스레드 시작 요청
    }
}