package com.example.ex08_network;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class DownHtmlActivity extends AppCompatActivity {

    String html;
    //핸들러(백그라운드 스레드의 메인UI 변경 작업 대행)
    Handler handler=new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            TextView result=findViewById(R.id.result);
            result.setText(html);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_down_html);

        Button btn=findViewById(R.id.down);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //백그라운드 스레드 생성
                Thread th=new Thread(new Runnable() {
                    @Override
                    public void run() {
                        //html 코드 다운로드
                        html=downloadHtml(
                                Common.SERVER_URL+"/mobile/main.jsp");
                        //핸들러에게 메시지 전달
                        handler.sendEmptyMessage(0);
                    }
                });
                th.start(); //스레드 시작 요청
            }
        });
    }
    String downloadHtml(String addr){
        StringBuffer html=new StringBuffer();
        try {
            //url 분석기
            URL url=new URL(addr);
            //url에 접속
            HttpURLConnection conn=(HttpURLConnection)url.openConnection();
            if(conn!=null){
                conn.setConnectTimeout(10000); //타임아웃 설정
                conn.setUseCaches(false); //캐쉬 사용 안함
                //정상 처리되었으면
                if(conn.getResponseCode()==HttpURLConnection.HTTP_OK){
                    //버퍼 생성
                    BufferedReader br=new BufferedReader(
new InputStreamReader(conn.getInputStream(),"utf-8"));
                    while(true){
                        String line=br.readLine(); //한 라인 읽고
                        if(line==null) break; //내용이 없으면 종료
                        html.append(line+"\n");
                    }
                    br.close(); //버퍼 닫기
                }
                conn.disconnect(); //url 접속 종료
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return html.toString();
    }
}