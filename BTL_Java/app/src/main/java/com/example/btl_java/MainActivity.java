package com.example.btl_java;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.btl_java.Adapter.FragmentAdapter;
import com.example.btl_java.RecycleView.Home.ListChild.Book;
import com.example.btl_java.RecycleView.Home.ListChild.ContentBook;
import com.example.btl_java.login.User;
import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.CubeGrid;
import com.github.ybq.android.spinkit.style.ThreeBounce;
import com.google.android.material.tabs.TabLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    ViewPager viewPager;
    TabLayout tabLayout;
    String urlBook = "https://api-book-last-comment.herokuapp.com/api/books";
    List<Book> books = new ArrayList<>();
    User user;
    LinearLayout ln_home;
//    List<ContentBook> contentBooks = new ArrayList<>();
    Dialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        ln_home = findViewById(R.id.ln_home);
        viewPager = findViewById(R.id.viewpager);
        tabLayout = findViewById(R.id.tablayout);

        Intent intent = getIntent();
        user = intent.getParcelableExtra("User");
        NewUser(user);
        dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.progressbar_load_login);
        Window window = dialog.getWindow();
        if(window == null) return;
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        WindowManager.LayoutParams layoutParams =window.getAttributes();
        layoutParams.gravity = Gravity.CENTER;
        window.setAttributes(layoutParams);
        dialog.setCancelable(false);

        ProgressBar progressBar = dialog.findViewById(R.id.progress_login);
        Sprite style = new ThreeBounce();
        progressBar.setIndeterminateDrawable(style);
        ln_home.setVisibility(View.GONE);
        dialog.show();
        GetAPIBook(urlBook);
    }

    private void NewUser(final User user) {
        String url = "https://api-book-last-comment.herokuapp.com/api/books/"+user.getId();
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    User user1 = new User(response.getInt("userID"),
                                        response.getString("userName"),
                            response.getString("passWord"),
                            response.getInt("money"),
                            response.getString("email"),
                            response.getString("phoneNumber"),
                            response.getString("linkImage"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        JsonVolley.getInstance(getApplicationContext()).getRequestQueue().add(request);
    }

    private void GetAPIBook(String urlBook){
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, urlBook, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        ChangeJson(response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        JsonVolley.getInstance(getApplicationContext()).getRequestQueue().add(request);
    }
    void ChangeJson(JSONArray jsonArray){
        JSONObject jsonObject;
        for (int i = 0;i< jsonArray.length();i++){
            try {
                jsonObject = jsonArray.getJSONObject(i);
                books.add(new Book(jsonObject.getInt("id"),
                        jsonObject.getString("nameBook"),
                        jsonObject.getString("linkBook"),
                        jsonObject.getString("author"),
                        jsonObject.getString("publishingCompany"),
                        jsonObject.getString("language"),
                        jsonObject.getString("createAt"),
                        jsonObject.getInt("numberOfPages"),1,
                        jsonObject.getInt("viewBook"),
                        jsonObject.getInt("likeBook"),
                        jsonObject.getString("contentBook"),
                        jsonObject.getString("updateAt"),
                        jsonObject.getString("describeBook")));
//                contentBooks.add(new ContentBook(jsonObject.getInt("id"),jsonObject.getString("contentBook").split("\\s")));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        ln_home.setVisibility(View.VISIBLE);
        dialog.dismiss();
        FragmentAdapter fragmentAdapter = new FragmentAdapter(getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT,books,user);
        viewPager.setAdapter(fragmentAdapter);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.getTabAt(0).setIcon(R.drawable.ic_baseline_home_24);
        tabLayout.getTabAt(1).setIcon(R.drawable.ic_baseline_search_24);
        tabLayout.getTabAt(2).setIcon(R.drawable.ic_baseline_backup_24);
        tabLayout.getTabAt(3).setIcon(R.drawable.ic_baseline_account_circle_24);
    }
}