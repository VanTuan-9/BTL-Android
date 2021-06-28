package com.example.btl_java;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.InputType;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
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
import com.example.btl_java.RecycleView.Home.ListChild.Comment;
import com.example.btl_java.RecycleView.Home.ListChild.ListCommentAdapter;
import com.example.btl_java.login.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ActivityCMT extends AppCompatActivity {
    private int bookID;
    private User user;
    TextView tvTotalCmt;
    RecyclerView rcCmt;
    CircleImageView imgAvtUser;
    EditText tvWriteCmt;
    ImageView imgSend;
    List<Comment> comments = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layoutfragmentcomment);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        tvTotalCmt = findViewById(R.id.tvTotalCmt);
        rcCmt = findViewById(R.id.rcCmt);
        imgAvtUser = findViewById(R.id.imgAvtUser);
        tvWriteCmt = findViewById(R.id.tvWriteCmt);
        imgSend = findViewById(R.id.imgSend);

        Intent intent = getIntent();
        bookID = intent.getIntExtra("BookID",0);
        user = intent.getParcelableExtra("User");
        Glide.with(getApplicationContext()).load(user.getLinkImg()).into(imgAvtUser);
        GetAPICMT(bookID);
        tvWriteCmt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                InputMethodManager keyboard = (InputMethodManager) getApplication().getSystemService(Context.INPUT_METHOD_SERVICE);
                if (hasFocus)
                    keyboard.showSoftInput(tvWriteCmt, 0);
                else
                    keyboard.hideSoftInputFromWindow(tvWriteCmt.getWindowToken(), 0);
            }
        });
        imgSend.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                if(tvWriteCmt.getText().length() == 0)
                    Toast.makeText(ActivityCMT.this, "Nội dung bình luận trống!", Toast.LENGTH_SHORT).show();
                else{
                    Comment comment = new Comment(user.getUsername(), LocalDate.now().toString(),tvWriteCmt.getText().toString(),bookID,user.getId());
                    comments.add(comment);
                    tvTotalCmt.setText(comments.size() + "");
                    JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject.put("name",comment.getUserName());
                        jsonObject.put("content",comment.getContent());
                        jsonObject.put("bookId",bookID);
                        jsonObject.put("UserId",user.getId());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    final String requestbody = jsonObject.toString();
                    String url1 = "https://api-book-last-comment.herokuapp.com/api/comments";
                    StringRequest request = new StringRequest(Request.Method.POST, url1, new Response.Listener<String>() {
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
                    RecyclerView.LayoutManager manager = new LinearLayoutManager(getApplicationContext(),RecyclerView.VERTICAL,false);
                    ListCommentAdapter adapter = new ListCommentAdapter(comments,getApplicationContext(),user.getLinkImg());
                    rcCmt.setLayoutManager(manager);
                    rcCmt.setAdapter(adapter);
                    Toast.makeText(ActivityCMT.this, "Thành công.", Toast.LENGTH_SHORT).show();
                    tvWriteCmt.setText("");
                    tvWriteCmt.clearFocus();
                }
            }
        });
    }

    private void GetAPICMT(final int bookID) {
        String url = "https://api-book-last-comment.herokuapp.com/api/books/" + bookID + "/comments";
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
                                        bookID,user.getId()));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        tvTotalCmt.setText(response.length() + "");
                        RecyclerView.LayoutManager manager = new LinearLayoutManager(getApplicationContext(),RecyclerView.VERTICAL,false);
                        ListCommentAdapter adapter = new ListCommentAdapter(comments,getApplicationContext(),user.getLinkImg());
                        rcCmt.setLayoutManager(manager);
                        rcCmt.setAdapter(adapter);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        JsonVolley.getInstance(getApplicationContext()).getRequestQueue().add(request);
    }
}
