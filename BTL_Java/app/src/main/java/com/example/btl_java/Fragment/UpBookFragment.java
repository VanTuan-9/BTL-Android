package com.example.btl_java.Fragment;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
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
import com.example.btl_java.ActivityUpImage;
import com.example.btl_java.JsonVolley;
import com.example.btl_java.R;
import com.example.btl_java.RecycleView.Home.ListChild.BookUp;
import com.example.btl_java.RecycleView.UpBook.Adapter;
import com.example.btl_java.login.User;
import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.ThreeBounce;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class UpBookFragment extends Fragment {
    int userid;
    ImageView avtUser;
    TextView username, tvXu, tvSach, tvLike, tb_my_book_0, tb_my_book_1;
    RecyclerView rcv_my_book_0, rcv_my_book_1;
    ImageButton ibtn_add_book;
    User user;
    List<BookUp> bookUps = new ArrayList<>();
    private Dialog dialog;

    public static UpBookFragment newInstance(int userid) {

        Bundle args = new Bundle();
        args.putInt("userid",userid);
        UpBookFragment fragment = new UpBookFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_up_book,container,false);
        userid = getArguments().getInt("userid");

        avtUser = view.findViewById(R.id.avtUser);
        username = view.findViewById(R.id.username);
        tvXu = view.findViewById(R.id.tvXu);
        tvSach = view.findViewById(R.id.tvSach);
        tvLike = view.findViewById(R.id.tvLike);
        rcv_my_book_0 = view.findViewById(R.id.rcv_my_book_0);
        rcv_my_book_1 = view.findViewById(R.id.rcv_my_book_1);
        tb_my_book_0 = view.findViewById(R.id.tb_my_book_0);
        tb_my_book_1 = view.findViewById(R.id.tb_my_book_1);
        ibtn_add_book = view.findViewById(R.id.ibtn_add_book);

        dialog = new Dialog(container.getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.progressbar_load_login);
        final Window window = dialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        final WindowManager.LayoutParams layoutParams =window.getAttributes();
        layoutParams.gravity = Gravity.CENTER;
        window.setAttributes(layoutParams);
        dialog.setCancelable(false);

        ProgressBar progressBar = dialog.findViewById(R.id.progress_login);
        Sprite style = new ThreeBounce();
        progressBar.setIndeterminateDrawable(style);
        dialog.show();
        GetUser(userid);
        ibtn_add_book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(getContext());
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.dialog_add_book);
                Window window1 = dialog.getWindow();
                window1.setLayout(WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.MATCH_PARENT);
                window1.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                WindowManager.LayoutParams layoutParams1 = window1.getAttributes();
                layoutParams1.gravity = Gravity.CENTER;
                window1.setAttributes(layoutParams1);
                dialog.setCancelable(false);

                final EditText edt_name_bookup = dialog.findViewById(R.id.edt_name_bookup);
                final EditText edt_language_bookup = dialog.findViewById(R.id.edt_language_bookup);
                final CheckBox cb_status_bookup = dialog.findViewById(R.id.cb_status_bookup);
                final EditText edt_describe_bookup = dialog.findViewById(R.id.edt_describe_bookup);
                final EditText edt_content_bookup = dialog.findViewById(R.id.edt_content_bookup);
                Button btn_return = dialog.findViewById(R.id.btn_return);
                Button btn_submit = dialog.findViewById(R.id.btn_submit);

                btn_return.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                final int status;
                if(cb_status_bookup.isChecked())
                    status = 2;
                else status = 1;
                btn_submit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String[] listcontentbookup = edt_content_bookup.getText().toString().split("\\s");
                        int number_of_page = listcontentbookup.length/1000 + 1;
                        final BookUp bookUp = new BookUp(userid,edt_name_bookup.getText().toString()+"","",user.getUsername(),"Không có.",edt_language_bookup.getText().toString()+"",number_of_page,status,0,0,edt_content_bookup.getText().toString()+"",edt_describe_bookup.getText().toString()+"",0,100);
                        if(edt_name_bookup.getText().length() == 0 || edt_language_bookup.getText().length() == 0 || edt_describe_bookup.getText().length() == 0 || edt_content_bookup.getText().length() == 0) {
                            Toast.makeText(getContext(), "Chưa điền đầy đủ thông tin.", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        final Dialog dialog1 = new Dialog(getContext());
                        dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        dialog1.setContentView(R.layout.dialog_up_image);
                        Window window2 = dialog1.getWindow();
                        window2.setLayout(WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.WRAP_CONTENT);
                        window2.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        WindowManager.LayoutParams layoutParams2 = window2.getAttributes();
                        layoutParams2.gravity = Gravity.CENTER;
                        window2.setAttributes(layoutParams2);
                        dialog1.setCancelable(false);

                        Button btn_no_up_image = dialog1.findViewById(R.id.btn_no_up_image);
                        Button btn_yes_up_image = dialog1.findViewById(R.id.btn_yes_up_image);

                        btn_no_up_image.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                final Dialog dialog2 = new Dialog(getContext());
                                dialog2.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                dialog2.setContentView(R.layout.dialog_up_book);
                                Window window = dialog2.getWindow();
                                window.setLayout(WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.WRAP_CONTENT);
                                window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                WindowManager.LayoutParams layoutParams2 = window.getAttributes();
                                layoutParams2.gravity = Gravity.CENTER;
                                window.setAttributes(layoutParams2);
                                dialog2.setCancelable(false);
                                dialog2.show();
                                JSONObject jsonObject = new JSONObject();
                                try {
                                    jsonObject.put("nameBook",bookUp.getNameBook());
                                    jsonObject.put("user_id",bookUp.getIdUser());
                                    jsonObject.put("linkBook","https://upload.wikimedia.org/wikipedia/commons/thumb/3/39/Book.svg/1200px-Book.svg.png");
                                    jsonObject.put("author",bookUp.getAuthor());
                                    jsonObject.put("publishingCompany",bookUp.getNXB());
                                    jsonObject.put("language",bookUp.getLanguageBook());
                                    jsonObject.put("numberOfPages",bookUp.getNumberPages());
                                    jsonObject.put("status",bookUp.getCategory());
                                    jsonObject.put("view",bookUp.getView());
                                    jsonObject.put("like",bookUp.getLikes());
                                    jsonObject.put("content",bookUp.getContent());
                                    jsonObject.put("describe",bookUp.getShortContent());
                                    jsonObject.put("price",bookUp.getPrice());

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                final String requestBody = jsonObject.toString();
                                String url = "https://api-user-last.herokuapp.com/api/bookups";
                                StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        dialog2.dismiss();
                                        dialog.dismiss();
                                        dialog1.dismiss();
                                        Toast.makeText(getContext(), "Đã tải sách lên thành công.", Toast.LENGTH_SHORT).show();
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
                                        if(requestBody==null) return null;
                                        else {
                                            try {
                                                return requestBody.getBytes("utf-8");
                                            } catch (UnsupportedEncodingException e) {
                                                e.printStackTrace();
                                                return null;
                                            }
                                        }
                                    }
                                };
                                JsonVolley.getInstance(getContext()).getRequestQueue().add(request);
                            }
                        });
                        btn_yes_up_image.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(getContext(), ActivityUpImage.class);
                                intent.putExtra("BookUp",bookUp);
                                getContext().startActivity(intent);
                                dialog.dismiss();
                                dialog1.dismiss();
                            }
                        });
                        dialog1.show();
                    }
                });
                dialog.show();
            }
        });
        return view;
    }

    private void GetUser(final int userid) {
        final String url = "https://api-user-last.herokuapp.com/api/users/" + userid;
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            user = new User(response.getString("userName"),
                                            response.getString("passWord"),
                                            response.getInt("money"),
                                            response.getString("email"),
                                            response.getString("phoneNumber"),
                                            response.getString("linkImage"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Glide.with(getContext()).load(user.getLinkImg()).into(avtUser);
                        username.setText(user.getUsername());
                        tvXu.setText(((int) user.getMoney())+"");
                        String urlBook = "https://api-user-last.herokuapp.com/api/users/"+ userid +"/bookups";
                        JsonArrayRequest request1 = new JsonArrayRequest(Request.Method.GET, urlBook, null,
                                new Response.Listener<JSONArray>() {
                                    @Override
                                    public void onResponse(JSONArray response) {
                                        for (int i = 0; i < response.length(); i++) {
                                            try {
                                                JSONObject jsonObject = response.getJSONObject(i);
                                                bookUps.add(new BookUp(jsonObject.getInt("id"),
                                                        jsonObject.getInt("user_id_up"),
                                                        jsonObject.getString("nameBook"),
                                                        jsonObject.getString("linkBook"),
                                                        jsonObject.getString("author"),
                                                        jsonObject.getString("publishingCompany"),
                                                        jsonObject.getString("language"),
                                                        jsonObject.getString("createAt"),
                                                        jsonObject.getInt("numberOfPages"),
                                                        jsonObject.getInt("statusBook"),
                                                        jsonObject.getInt("viewBook"),
                                                        jsonObject.getInt("likeBook"),
                                                        jsonObject.getString("contentBook"),
                                                        jsonObject.getString("updateAt"),
                                                        jsonObject.getString("describeBook"),
                                                        jsonObject.getInt("classify"),
                                                        jsonObject.getInt("priceBook")));
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                        List<BookUp> bookUps_0 = new ArrayList<>();
                                        for (int i = 0; i < bookUps.size(); i++) {
                                            if(bookUps.get(i).getClassify() == 1)
                                                bookUps_0.add(bookUps.get(i));
                                        }
                                        if(bookUps_0.size()>0) {
                                            rcv_my_book_0.setVisibility(View.VISIBLE);
                                            tb_my_book_0.setVisibility(View.GONE);
                                        }
                                        else{
                                            rcv_my_book_0.setVisibility(View.GONE);
                                            tb_my_book_0.setVisibility(View.VISIBLE);
                                        }
                                        tvSach.setText(bookUps_0.size() + "");
                                        int like = 0;
                                        for (int i = 0; i < bookUps_0.size(); i++) {
                                            like+= bookUps_0.get(i).getLikes();
                                        }
                                        tvLike.setText(like+"");
                                        RecyclerView.LayoutManager manager = new LinearLayoutManager(getContext(),RecyclerView.HORIZONTAL,false);
                                        Adapter adapter0 = new Adapter(bookUps_0,getContext(), user);
                                        rcv_my_book_0.setAdapter(adapter0);
                                        rcv_my_book_0.setLayoutManager(manager);
                                        List<BookUp> bookUps_1 = new ArrayList<>();
                                        for (int i = 0; i < bookUps.size(); i++) {
                                            if(bookUps.get(i).getClassify() == 0)
                                                bookUps_1.add(bookUps.get(i));
                                        }
                                        if(bookUps_1.size()>0) {
                                            rcv_my_book_1.setVisibility(View.VISIBLE);
                                            tb_my_book_1.setVisibility(View.GONE);
                                        }
                                        else{
                                            rcv_my_book_1.setVisibility(View.GONE);
                                            tb_my_book_1.setVisibility(View.VISIBLE);
                                        }
                                        RecyclerView.LayoutManager manager1 = new LinearLayoutManager(getContext(),RecyclerView.HORIZONTAL,false);
                                        Adapter adapter1 = new Adapter(bookUps_1,getContext(),user);
                                        rcv_my_book_1.setAdapter(adapter1);
                                        rcv_my_book_1.setLayoutManager(manager1);
                                        dialog.dismiss();
                                    }
                                }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {

                            }
                        });
                        JsonVolley.getInstance(getContext()).getRequestQueue().add(request1);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        JsonVolley.getInstance(getContext()).getRequestQueue().add(request);
    }

    @Override
    public void onResume() {
        super.onResume();
        bookUps = new ArrayList<>();
    }
}
