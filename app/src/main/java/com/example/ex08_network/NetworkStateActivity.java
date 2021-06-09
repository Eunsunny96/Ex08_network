package com.example.ex08_network;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

public class NetworkStateActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_network_state);

        TextView result=findViewById(R.id.result);
        String sResult="";
        //네트워크 연결 관리자 객체
        ConnectivityManager mgr=
(ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        //네트워크 접속 정보
        NetworkInfo activeNetwork=mgr.getActiveNetworkInfo();
        if(activeNetwork!=null) { //네트워크 접속 상태
            if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI) {
                //wifi
                Toast.makeText(this, activeNetwork.getTypeName(),
                        Toast.LENGTH_LONG).show();
            } else if (activeNetwork.getType() ==
                    ConnectivityManager.TYPE_MOBILE) {
                //mobile
                Toast.makeText(this, activeNetwork.getTypeName(),
                        Toast.LENGTH_LONG).show();
            }
        }else{ //연결되지 않은 상태
            Toast.makeText(this, "인터넷에 연결되어 있지 않습니다.", Toast.LENGTH_LONG).show();
        }
        if(activeNetwork != null){
            sResult+="Active:\n"+activeNetwork.toString()+"\n";
            result.setText(sResult);
        }
    }
}




