package com.example.btl_java;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.example.btl_java.RecycleView.Home.ListChild.Book;
import com.example.btl_java.RecycleView.Home.ListChild.ContentBook;
import com.example.btl_java.RecycleView.Home.ListChild.ReadedBook;
import com.example.btl_java.login.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class ActivityReadBook extends AppCompatActivity {
    TextView tvTenSach;
    ScrollView rcr_read_book;
    ImageView imgBack;
    Button btn_cmt_read_book, btn_like_read_book;
    ImageButton bt_menu_read_book;
    TextView tv_read_book,tv_page_book;
    ImageButton ibtn_left_page, ibtn_right_page;
    Dialog dialog;
    List<String> list = new ArrayList<>();
    int x = 0,y = 0;
    User user;
    Book book;
    ContentBook contentBook;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_read_book);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        tvTenSach = findViewById(R.id.tvTenSach);
        tv_read_book = findViewById(R.id.tv_read_book);
        ibtn_left_page = findViewById(R.id.ibtn_left_page);
        ibtn_right_page = findViewById(R.id.ibtn_right_page);
        tv_page_book = findViewById(R.id.tv_page_book);
        rcr_read_book = findViewById(R.id.rcr_read_book);
        btn_cmt_read_book = findViewById(R.id.btn_cmt_read_book);
        btn_like_read_book = findViewById(R.id.btn_like_read_book);
        imgBack = findViewById(R.id.imgBack);


        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        Intent intent = getIntent();
        book = intent.getParcelableExtra("book");
        tvTenSach.setSelected(true);
        tvTenSach.setText(book.getNameBook());
        contentBook = new ContentBook(book.getIdBook(),book.getContent().split("\\s"));
        user = intent.getParcelableExtra("User");
        String a = "";
        for (int i = 0; i < contentBook.getListContentBook().length; i++) {
            a = a + " ";
            a = a.concat(contentBook.getListContentBook()[i]);
            if((i%1000 == 0 && i != 0) || i == contentBook.getListContentBook().length-1){
                list.add(a);
                a = "";
            }
        }
        tv_page_book.setText("Chương 1 " + " \"FREE\"");
        tv_page_book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ActivityReadBook.this, "Đừng click!!!", Toast.LENGTH_SHORT).show();
            }
        });
        tv_read_book.setText(list.get(x).trim().replaceAll("  ","\n") + " ...");

        GetAPIReaded();

        ibtn_left_page.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(x == 2) y = 1;
                if(x > 0) {
                    x--;
                    tv_read_book.setText(list.get(x).trim().replaceAll("  ","\n") + " ...");
                    if(x <2)
                        tv_page_book.setText("Chương "+ (x + 1) + " \"FREE\"");
                    else
                        tv_page_book.setText("Chương "+ (x + 1));
                    rcr_read_book.scrollTo(0,0);
                }
            }
        });


        btn_cmt_read_book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ActivityReadBook.this,ActivityCMT.class);
                intent.putExtra("BookID",book.getIdBook());
                intent.putExtra("User",user);
                startActivity(intent);
            }
        });
    }

    private void GetAPIReaded() {
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, "https://api-user-last.herokuapp.com/api/bookreads", null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Change(response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        JsonVolley.getInstance(getApplicationContext()).getRequestQueue().add(request);
    }

    private void Change(JSONArray response) {
        JSONObject jsonObject;
        final List<ReadedBook> readedBooks = new ArrayList<>();
        for (int i = 0; i < response.length(); i++) {
            try {
                jsonObject = response.getJSONObject(i);
                readedBooks.add(new ReadedBook(jsonObject.getInt("book_id_reading"),jsonObject.getInt("user_id_reading")));

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        ibtn_right_page.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(x < list.size()-1) {
                    x++;
                    if(x>2) y = 1;
                    if(x < 2)
                        tv_page_book.setText("Chương "+ (x + 1) + " \"FREE\"");
                    else{
                        int kt = 1;
                        if(x == 2 && readedBooks.size() > 0){
                            for (int i = 0; i < readedBooks.size(); i++) {
                                if(book.getIdBook() == readedBooks.get(i).getId_book() && user.getId() == readedBooks.get(i).getId_user()){
                                    kt = 0;
                                    break;
                                }

                            }
                        }
                        if(user.getMoney() < 100 && x == 2 && kt == 1 && y == 0){
                            dialog = new Dialog(ActivityReadBook.this);
                            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                            dialog.setContentView(R.layout.dialog_het_money);
                            Window window = dialog.getWindow();
                            if(window == null)  return;
                            window.setLayout(WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.WRAP_CONTENT);
                            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                            WindowManager.LayoutParams layoutParams = window.getAttributes();
                            layoutParams.gravity = Gravity.CENTER;
                            window.setAttributes(layoutParams);
                            dialog.setCancelable(false);
                            Button btn_nap_het_money = dialog.findViewById(R.id.btn_nap_het_money);
                            Button btn_return_het_money = dialog.findViewById(R.id.btn_return_het_money);
                            btn_return_het_money.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dialog.dismiss();
                                }
                            });
                            btn_nap_het_money.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                }
                            });
                            dialog.show();
                            x--;
                        }
                        else if(x == 2 && kt == 1 && y == 0){
                            dialog = new Dialog(ActivityReadBook.this);
                            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                            dialog.setContentView(R.layout.dialog_doc_tiep);
                            Window window = dialog.getWindow();
                            if(window == null)  return;
                            window.setLayout(WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.WRAP_CONTENT);
                            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                            WindowManager.LayoutParams layoutParams = window.getAttributes();
                            layoutParams.gravity = Gravity.CENTER;
                            window.setAttributes(layoutParams);
                            dialog.setCancelable(false);
                            Button btn_khong = dialog.findViewById(R.id.btn_khong);
                            Button btn_co = dialog.findViewById(R.id.btn_co);
                            btn_khong.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dialog.dismiss();
                                    x--;
                                }
                            });
                            btn_co.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    user.setMoney(user.getMoney() - 100);
                                    JSONObject object = new JSONObject();
                                    try {
                                        object.put("money",user.getMoney());
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    final String requestbody = object.toString();
                                    String url = "https://api-user-last.herokuapp.com/api/users/" + user.getId();
                                    StringRequest request = new StringRequest(Request.Method.PATCH, url,
                                            new Response.Listener<String>() {
                                                @Override
                                                public void onResponse(String response) {

                                                }
                                            }, new Response.ErrorListener() {
                                        @Override
                                        public void onErrorResponse(VolleyError error) {

                                        }
                                    }){
                                        @Override
                                        public String getBodyContentType() {
                                            return "application/json; charset = utf-8";
                                        }
                                        @Override
                                        public byte[] getBody() throws AuthFailureError {
                                            if(requestbody==null) return null;
                                            else {
                                                try {
                                                    return requestbody.getBytes("utf-8");
                                                } catch (UnsupportedEncodingException e) {
                                                    e.printStackTrace();
                                                    return null;
                                                }
                                            }
                                        }
                                    };
                                    JsonVolley.getInstance(getApplicationContext()).getRequestQueue().add(request);
                                    JSONObject jsonObject1 = new JSONObject();
                                    try {
                                        jsonObject1.put("book_id_reading",book.getIdBook());
                                        jsonObject1.put("userId",user.getId());
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    final String s = jsonObject1.toString();
                                    StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://api-user-last.herokuapp.com/api/bookreads",
                                            new Response.Listener<String>() {
                                                @Override
                                                public void onResponse(String response) {

                                                }
                                            }, new Response.ErrorListener() {
                                        @Override
                                        public void onErrorResponse(VolleyError error) {

                                        }
                                    }){
                                        @Override
                                        public String getBodyContentType() {
                                            return "application/json; charset = utf-8";
                                        }

                                        @Override
                                        public byte[] getBody() throws AuthFailureError {
                                            if(s==null) return null;
                                            else {
                                                try {
                                                    return s.getBytes("utf-8");
                                                } catch (UnsupportedEncodingException e) {
                                                    e.printStackTrace();
                                                    return null;
                                                }
                                            }
                                        }
                                    };
                                    JsonVolley.getInstance(getApplicationContext()).getRequestQueue().add(stringRequest);
                                    tv_page_book.setText("Chương " + (x + 1));
                                    Toast.makeText(ActivityReadBook.this, "Đã thanh toán.", Toast.LENGTH_SHORT).show();
                                    dialog.dismiss();
                                }
                            });
                            dialog.show();
                        }
                        else {
                            tv_page_book.setText("Chương " + (x + 1));
                        }
                    }
                    rcr_read_book.scrollTo(0,0);
                }
                if(x == list.size() - 1)
                    tv_read_book.setText(list.get(x).trim().replaceAll("  ","\n") + "\n\nHẾT.");
                else
                    tv_read_book.setText(list.get(x).trim().replaceAll("  ","\n") + " ...");
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
