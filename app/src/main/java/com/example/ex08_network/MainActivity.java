package com.example.ex08_network;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintAttribute;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    public void onClick(View v){
        Intent intent=null;
        switch (v.getId()){
            case R.id.button1:
                intent=new Intent(this, NetworkStateActivity.class);
                break;
            case R.id.button2:
                intent=new Intent(this, DownHtmlActivity.class);
                break;
            case R.id.button3:
                intent=new Intent(this, DownImageActivity.class);
                break;
            case R.id.button4:
                intent=new Intent(this, UploadActivity.class);
                break;
            case R.id.button5:
                intent=new Intent(this, BookListActivity.class);
                break;
            case R.id.button6:
                intent=new Intent(this, BookListJsonActivity.class);
                break;
            case R.id.button7:
                intent=new Intent(this, LoginActivity.class);
                break;
            case R.id.button8:
                intent=new Intent(this, Chatting.class);
                break;
        }
        startActivity(intent);
    }
}




