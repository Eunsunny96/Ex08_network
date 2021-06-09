package com.example.ex08_network;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;

public class BookListActivity extends AppCompatActivity {
    //변수 선언
    ArrayList<BookDTO> list;
    RecyclerView rv;
    RecyclerView.Adapter myAdapter;
    //핸들러 선언
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

        //웹서버에 접속해서 xml 리턴받아서 xml parsing(분석) 작업
        Thread th = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    list = new ArrayList<>();
                    // 웹서버의 url에 접속 시도
                    URL url = new URL(
                            Common.SERVER_URL + "/mobile/book/xml.jsp");
                    //xml 분석 객체 생성
                    XmlPullParser parser =
XmlPullParserFactory.newInstance().newPullParser();
                    // 웹서버에서 생성한 xml을 읽기 위한 스트림 생성
                    InputStream is = url.openStream();
                    // xml 분석기에 스트림을 입력
                    parser.setInput(is, "utf-8");
// xml 문서의 모든 노드를 하나씩 검사
                    int eventType = parser.getEventType();
                    String tag;
                    BookDTO dto = null;
// xml 문서의 끝이 아니면
                    while (eventType != XmlPullParser.END_DOCUMENT) {
                        Log.i("test","event:"+eventType);
                        switch (eventType) {
                            case XmlPullParser.START_TAG: //여는 태그
                                tag = parser.getName(); //태그의 이름
                                if (tag.equals("book_name")) {
                                    dto = new BookDTO();
// nextText() 태그 다음의 텍스트 <book_name>텍스트</book_name>
                                    dto.setBook_name(parser.nextText());
                                }
                                if (tag.equals("press")) {
                                    dto.setPress(parser.nextText());
                                }
                                break;
                            case XmlPullParser.END_TAG: //닫는 태그
                                tag = parser.getName();
                                if (tag.equals("book")) {
                                    list.add(dto); //dto를 리스트에 추가
                                    dto = null; //dto 초기화
                                }
                                break;
                        }
                        eventType = parser.next(); //다음 요소
                    }
                    Log.i("test", "book_list:" + list);
                    Log.i("test", "size:" + list.size());

                    handler.sendEmptyMessage(0);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        th.start();

    }

    //내부 클래스(커스텀 아답터)
    class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
        @Override
        public MyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                       int viewType) {
            View rowItem = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.book_row, parent, false);
            return new MyAdapter.ViewHolder(rowItem);
        }

        @Override
        public void onBindViewHolder(MyAdapter.ViewHolder holder,
                                     int position) {
            holder.txtBookName.setText(list.get(position).getBook_name());
            holder.txtPress.setText(list.get(position).getPress());
        }

        @Override
        public int getItemCount() {
            Log.i("test", "자료개수:" + list.size() + "");
            return list.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder
                implements View.OnClickListener {
            private TextView txtBookName, txtPress;

            public ViewHolder(View view) {
                super(view);
                view.setOnClickListener(this);
                this.txtBookName = view.findViewById(R.id.book_name);
                this.txtPress = view.findViewById(R.id.press);
            }

            @Override
            public void onClick(View view) {

            }
        }
    }
}






