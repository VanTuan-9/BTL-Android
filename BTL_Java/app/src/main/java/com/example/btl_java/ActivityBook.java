package com.example.btl_java;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.example.btl_java.RecycleView.Home.ListChild.Book;
import com.example.btl_java.RecycleView.Home.ListChild.Comment;
import com.example.btl_java.RecycleView.Home.ListChild.ContentBook;
import com.example.btl_java.RecycleView.Home.ListChild.ListCommentAdapter;
import com.example.btl_java.login.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class ActivityBook extends AppCompatActivity {

    ImageView img_background_item_book;
    ImageView img_item_book;
    TextView tv_name_item_book;
    TextView tv_author_item_book;
    TextView tv_view_item_book;
    TextView tv_like_item_book;
    TextView tv_short_item_book;
    ImageButton ibtn_more_item_book;
    TextView tv_dayXB_detail_item_book;
    TextView tv_author_detail_item_book;
    TextView tv_numberPages_detail_item_book;
    TextView tv_type_detail_item_book;
    TextView tv_NXB_detail_item_book;
    RecyclerView rcv_comment_item_book;
    Button btn_read_book;
    List<Comment> comments = new ArrayList<>();
    Book book;
    User user1;
    ContentBook contentBook;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_book);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        overridePendingTransition(R.anim.animation_down,R.anim.animation_top);

        final Intent intent = getIntent();
        final User user = intent.getParcelableExtra("User");
        final Book book = intent.getParcelableExtra("book");
        contentBook = new ContentBook(book.getIdBook(),book.getContent().split("\\s"));
        GetBook(book.getIdBook(),user);
        AnhXa();
        Glide.with(this).load(book.getUrlBook()).into(img_background_item_book);
        Glide.with(this).load(book.getUrlBook()).into(img_item_book);

    }

    private void GetBook(int idBook, final User user) {
        final String url = "https://api-book-last-comment.herokuapp.com/api/books/" + idBook;
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            book = new Book(response.getInt("id"),
                                    response.getString("nameBook"),
                                    response.getString("linkBook"),
                                    response.getString("author"),
                                    response.getString("publishingCompany"),
                                    response.getString("language"),
                                    response.getString("createAt"),
                                    response.getInt("numberOfPages"), 1,
                                    response.getInt("viewBook"),
                                    response.getInt("likeBook"),
                                    response.getString("contentBook"),
                                    response.getString("updateAt"),
                                    response.getString("describeBook"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        String url1 = "https://api-user-last.herokuapp.com/api/users/" + user.getId();
                        JsonObjectRequest request1 = new JsonObjectRequest(Request.Method.GET, url1, null,
                                new Response.Listener<JSONObject>() {
                                    @Override
                                    public void onResponse(JSONObject response) {
                                        try {
                                            user1 = new User(response.getInt("userID"),response.getString("userName"),
                                                    response.getString("passWord"),
                                                    response.getInt("money"),
                                                    response.getString("email"),
                                                    response.getString("phoneNumber"),
                                                    response.getString("linkImage"));
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                        tv_name_item_book.setText(book.getNameBook());
                                        tv_author_item_book.setText(book.getAuthor());
                                        if(book.getView() >= 1000000)
                                            tv_view_item_book.setText((float)book.getView()/1000000 + "m");
                                        else if(book.getView() >= 1000)
                                            tv_view_item_book.setText((float)book.getView()/1000 + "k");
                                        else
                                            tv_view_item_book.setText(book.getView() + "");
                                        if(book.getLikes() >= 1000000)
                                            tv_like_item_book.setText((float)book.getLikes()/1000000 + "m");
                                        else if(book.getLikes() >= 1000)
                                            tv_like_item_book.setText((float)book.getLikes()/1000 + "k");
                                        else
                                            tv_like_item_book.setText(book.getLikes() + "");
                                        tv_short_item_book.setText(book.getShortContent());
                                        ibtn_more_item_book.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                if(tv_short_item_book.getMaxLines() == 3) {
                                                    tv_short_item_book.setMaxLines(10);
                                                    ibtn_more_item_book.setImageResource(R.drawable.ic_baseline_expand_less_24);
                                                }
                                                else {
                                                    tv_short_item_book.setMaxLines(3);
                                                    ibtn_more_item_book.setImageResource(R.drawable.ic_baseline_expand_more_24);
                                                }
                                            }
                                        });
                                        tv_dayXB_detail_item_book.setText(book.getDayXB());
                                        tv_author_detail_item_book.setText(book.getAuthor());
                                        tv_numberPages_detail_item_book.setText((contentBook.getListContentBook().length/1000 + 1) + "");
                                        if(book.getCategory() == 1)
                                            tv_type_detail_item_book.setText("Truyện mới");
                                        else if(book.getCategory() == 2)
                                            tv_type_detail_item_book.setText("Chưa hoàn thành.");
                                        else
                                            tv_type_detail_item_book.setText("Hoàn thành.");
                                        tv_NXB_detail_item_book.setText(book.getNXB());

                                        GetAPICMT(comments,book.getIdBook(),user);

                                        btn_read_book.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                PatchView(url,book);
                                                Intent intent1 = new Intent(ActivityBook.this,ActivityReadBook.class);
                                                intent1.putExtra("book", book);
                                                intent1.putExtra("User", user1);
                                                startActivity(intent1);
                                                finish();
                                            }
                                        });
                                    }
                                }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {

                            }
                        });


                        JsonVolley.getInstance(getApplicationContext()).getRequestQueue().add(request1);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        JsonVolley.getInstance(getApplicationContext()).getRequestQueue().add(request);
    }

    private void PatchView(String url, Book book) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("view",book.getView() + 1);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        final String requestbody = jsonObject.toString();
        StringRequest request = new StringRequest(Request.Method.PATCH, url, new Response.Listener<String>() {
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
            public byte[] getBody() throws AuthFailureError{
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
    }

    private void GetAPICMT(final List<Comment> comments, final int idBook, final User id) {
        String url = "https://api-book-last-comment.herokuapp.com/api/books/" + idBook + "/comments";
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        for (int i = 0; i < response.length(); i++) {
                            try {
                                JSONObject jsonObject = response.getJSONObject(i);
                                comments.add(new Comment(jsonObject.getString("name"),
                                        jsonObject.getString("createAt"),
                                        jsonObject.getString("content"),
                                        idBook,id.getId()));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        ListCommentAdapter adapter = new ListCommentAdapter(comments,ActivityBook.this,id.getLinkImg());
                        rcv_comment_item_book.setAdapter(adapter);
                        rcv_comment_item_book.setLayoutManager(new LinearLayoutManager(ActivityBook.this,RecyclerView.VERTICAL,false));
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        JsonVolley.getInstance(getApplicationContext()).getRequestQueue().add(request);
    }

    private void AnhXa() {
        img_background_item_book = findViewById(R.id.img_background_item_book);
        img_item_book = findViewById(R.id.img_item_book);
        tv_name_item_book = findViewById(R.id.tv_name_item_book);
        tv_author_item_book = findViewById(R.id.tv_author_item_book);
        tv_view_item_book = findViewById(R.id.tv_view_item_book);
        tv_like_item_book = findViewById(R.id.tv_like_item_book);
        tv_short_item_book = findViewById(R.id.tv_short_item_book);
        ibtn_more_item_book = findViewById(R.id.ibtn_more_item_book);
        tv_dayXB_detail_item_book = findViewById(R.id.tv_dayXB_detail_item_book);
        tv_author_detail_item_book = findViewById(R.id.tv_author_detail_item_book);
        tv_numberPages_detail_item_book = findViewById(R.id.tv_numberPages_detail_item_book);
        tv_type_detail_item_book = findViewById(R.id.tv_type_detail_item_book);
        tv_NXB_detail_item_book = findViewById(R.id.tv_NXB_detail_item_book);
        rcv_comment_item_book = findViewById(R.id.rcv_comment_item_book);
        btn_read_book = findViewById(R.id.btn_read_book);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
