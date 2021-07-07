package com.example.btl_java;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
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
    List<User> users = new ArrayList<>();
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
        String name = intent.getStringExtra("UserName");
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
        NewUser(name);
        dialog.show();
    }

    private void NewUser(final String name) {
        final String url = "https://api-user-last.herokuapp.com/api/users";
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject jsonObject = response.getJSONObject(i);
                                users.add(new User(jsonObject.getInt("userID"),
                                        jsonObject.getString("userName"),
                                        jsonObject.getString("passWord"),
                                        jsonObject.getInt("money"),
                                        jsonObject.getString("email"),
                                        jsonObject.getString("phoneNumber"),
                                        jsonObject.getString("linkImage")));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        GetAPIBook(urlBook,name);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        JsonVolley.getInstance(getApplicationContext()).getRequestQueue().add(request);
    }

    private void GetAPIBook(String urlBook, final String name){
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, urlBook, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        ChangeJson(response,name);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        JsonVolley.getInstance(getApplicationContext()).getRequestQueue().add(request);
    }
    void ChangeJson(JSONArray jsonArray, String name){
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
        User user = new User();
        for (int i = 0; i < users.size(); i++) {
            if(users.get(i).getUsername().equals(name)){
                user = users.get(i);
                break;
            }
        }
        FragmentAdapter fragmentAdapter = new FragmentAdapter(getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT,user,books);
        viewPager.setAdapter(fragmentAdapter);
        tabLayout.setupWithViewPager(viewPager);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(viewPager));
        tabLayout.getTabAt(0).setIcon(R.drawable.ic_baseline_home_24);
        tabLayout.getTabAt(1).setIcon(R.drawable.ic_baseline_search_24);
        tabLayout.getTabAt(2).setIcon(R.drawable.ic_baseline_backup_24);
        tabLayout.getTabAt(3).setIcon(R.drawable.ic_baseline_account_circle_24);
//        View view = tabLayout.getChildAt(0);
//        if (view instanceof LinearLayout){
//            ((LinearLayout)view).setShowDividers(LinearLayout.SHOW_DIVIDER_MIDDLE);
//            GradientDrawable drawable = new GradientDrawable();
//            drawable.setColor(getResources().getColor(R.color.tabcolor));
//            drawable.setSize(2, 1);
//            ((LinearLayout) view).setDividerPadding(10);
//            ((LinearLayout) view).setDividerDrawable(drawable);
//        }
    }
}