package com.example.ex08_network;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.view.ViewGroup;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

public class BookListJsonActivity extends AppCompatActivity {
    ArrayList<BookDTO> items;
    RecyclerView rv;
    RecyclerView.Adapter myAdapter;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            myAdapter.notifyDataSetChanged();
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_list);

        rv = findViewById(R.id.rv);
        rv.setLayoutManager(new LinearLayoutManager(this,
                RecyclerView.VERTICAL, false));
        rv.addItemDecoration(new DividerItemDecoration(rv.getContext(),
                DividerItemDecoration.VERTICAL));
        myAdapter = new MyAdapter();
        rv.setAdapter(myAdapter);

        final StringBuilder sb = new StringBuilder();
        Thread th = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    items = new ArrayList<BookDTO>();
                    String page = Common.SERVER_URL + "/mobile/book/json.jsp";
                    URL url = new URL(page);
                    HttpURLConnection conn = (HandlerThread)url.openConnection();
                    if(conn !=null){
                        conn.setConnectTimeout(10000);
                        conn.setUseCaches(false);
                        if(conn.getResponseCode()== HttpURLConnection.HTTP_OK) {
                            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"));
                            while (true) {
                                String line = br.readLine();
                                if (line == null) break;
                                sb.append(line + "\n");
                            }
                            br.close();
                        }
                            conn.disconnect();

                        }
                        JSONObject jsonObj = new JSONObject(sb.toString());
                        JSONArray jArray = (JSONArray) jsonObj.get("sendData");
                        for(int i =0 ; i< jArray.length(); i++){
                            JSONObject row = jArray.getJSONObject(i);
                            BookDTO dto = new BookDTO();
                            dto.setAmount(row.getInt("amount"));
                            dto.setBook_code(row.getInt("book_code"));
                            dto.setBook_name(row.getString("book_name"));
                            dto.setPress(row.getString("press"));
                            dto.setPrice(row.getInt("price"));
                            items.add(dto);

                        }
                        handler.sendEmptyMessage(0);
                    }catch(Exception e){
                        e.printStackTrace();
                    }
                }

        });
        th.start();
    }
    class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder>{

        @Override
        public MyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return null;
        }

        @Override
        public void onBindViewHolder(@NonNull @org.jetbrains.annotations.NotNull MyAdapter.ViewHolder holder, int position) {

        }

        @Override
        public int getItemCount() {
            return 0;
        }
    }
}