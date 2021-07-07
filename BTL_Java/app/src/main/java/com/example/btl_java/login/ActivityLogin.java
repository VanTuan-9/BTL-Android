package com.example.btl_java.login;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.btl_java.AdminActivity;
import com.example.btl_java.MainActivity;
import com.example.btl_java.R;
import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.CubeGrid;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ActivityLogin extends AppCompatActivity {
    EditText edttendn, edtmkdn;
    Button btndn;
    TextView tvquenmk, tvdk, tvErr;
    List<User> list;
    String result;
    String url = "https://api-user-last.herokuapp.com/api/users";
    CheckBox cbsaved;
    Dialog dialog;
    int ktmkm = 0;
    static final String NAME_CACHE = "Account";
    static final String USERNAME_CACHE = "userName";
    static final String PASSWORD_CACHE = "passWord";
    public static final String USERNAME = "Administrator";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        AnhXa();
        DialogLogin();
    }

private void DialogLogin(){
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
    Sprite style = new CubeGrid();
    progressBar.setIndeterminateDrawable(style);

    RequestQueue requestQueue = Volley.newRequestQueue(ActivityLogin.this);
    StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {
            result = response;
            getJson(1);
        }
    }, new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            edttendn.setText("");
            edtmkdn.setText("");
            Log.d("TAG", "onCreateView: ");
            getJson(0);
        }
    });
    requestQueue.add(stringRequest);
    dialog.show();
}

    private void getJson(int mKT){
        list = new ArrayList<>();
        String username, password, email, phoneNumber, linkImg;
        double money;
        int id;
        try {
            if(mKT == 1){
                JSONArray jsonArray = new JSONArray(result);
                for(int i  = 0; i < jsonArray.length(); i++){
                    JSONObject object = jsonArray.getJSONObject(i);
                    id = object.getInt("userID");
                    username = object.getString("userName");
                    password = object.getString("passWord");
                    money = object.getDouble("money");
                    email = object.getString("email");
                    phoneNumber = object.getString("phoneNumber");
                    linkImg = object.getString("linkImage");
                    list.add(new User(id,username,password,money,email,phoneNumber,linkImg));
                }
            }
            else {
                list = new ArrayList<>();
                list.add(new User(0,"","",0,"","",""));
            }
            dialog.dismiss();
            btndn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String username = edttendn.getText().toString().trim();
                    String password = edtmkdn.getText().toString().trim();
                    if(edttendn.getText().toString().equals(USERNAME)){
                        Intent intent = new Intent(getApplicationContext(), AdminActivity.class);
                        startActivity(intent);
                        finish();
                    }

                    else {
                        if(list == null){
                            list.add(new User(0,"","",0,"","",""));
                        }
                        User user =new User();
                        boolean checkUser = false;
                        for(int i = 0; i < list.size(); i++){
                            if(username.equals(list.get(i).getUsername()) && password.equals(list.get(i).getPassword())){
                                checkUser = true;
                                user = list.get(i);
                                break;
                            }
                        }
                        if (checkUser == true && edttendn.getText().length() != 0 && edtmkdn.getText().length() != 0){
                            SharedPreferences sharedPreferences = getSharedPreferences(NAME_CACHE,MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            if(cbsaved.isChecked()){
                                editor.putString(USERNAME_CACHE,edttendn.getText().toString());
                                editor.putString(PASSWORD_CACHE,edtmkdn.getText().toString());
                                editor.commit();
                            }
                            else{
                                editor.clear().commit();
                            }
                            Intent intent = new Intent(ActivityLogin.this, MainActivity.class);
                            intent.putExtra("UserName", user.getUsername());
                            startActivity(intent);
                            finish();
                        }
                        else{
                            boolean checkPassword = false;
                            for (int i = 0; i < list.size(); i++){
                                if (username.compareTo(list.get(i).getUsername()) == 0 && password.compareTo(list.get(i).getPassword()) != 0){
                                    checkPassword = true;
                                    break;
                                }
                            }
                            if (checkPassword){
                                tvErr.setText("Sai mật khẩu");
                            }
                            else tvErr.setText("Tài khoản không hợp lệ");
                        }
                    }
                }});
            tvdk.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DialogDangKy(list);
                }
            });
            tvquenmk.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(list.get(list.size()-1).getId() == 0) {
                        Toast.makeText(ActivityLogin.this, "Chức năng không hỗ trợ khi chưa đăng nhập lần nào!", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    else if(ktmkm != 0){
                        Toast.makeText(ActivityLogin.this, "Không thể thực hiện!", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    String username = edttendn.getText().toString().trim();

                    boolean checkUsername = false;
                    for(int i = 0; i < list.size(); i++){
                        if(username.equals(list.get(i).getUsername())){
                            checkUsername = true;
                            break;
                        }
                    }
                    if (checkUsername){
                        for(int i = 0; i < list.size(); i++){
                            if (username.equals(list.get(i).getUsername())){
                                DialogQuenMK(list,i);
                            }
                        }
                    }
                    else tvErr.setText("Tài khoản không hợp lệ");
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public void DialogDangKy(final List<User> list){
        LayoutInflater inflater = getLayoutInflater();
        View alertLayout = inflater.inflate(R.layout.dialogdangky, null);

        final RequestQueue requestQueue = Volley.newRequestQueue(ActivityLogin.this);

        AlertDialog.Builder alert = new AlertDialog.Builder(ActivityLogin.this);
        alert.setView(alertLayout);
        alert.setCancelable(false);
        final EditText edtEmail = alertLayout.findViewById(R.id.edtemail);
        final EditText edttendk = alertLayout.findViewById(R.id.edttendk);
        final EditText edtmkdk = alertLayout.findViewById(R.id.edtmkdk);
        Button btndk = alertLayout.findViewById(R.id.btndk);
        TextView tvdn = (TextView)alertLayout.findViewById(R.id.tvdn);
        final TextView tvErrDK = alertLayout.findViewById(R.id.tvErrDK);
        final AlertDialog dialog = alert.create();

        btndk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = edttendk.getText().toString();
                String password = edtmkdk.getText().toString();
                String email = edtEmail.getText().toString();
                boolean checkUser = false;
                boolean checkEmail = false;
                for(int i = 0; i < list.size(); i++){
                    if(username.equals(list.get(i).getUsername()))
                        checkUser = true;
                    if(email.equals(list.get(i).getEmail()))
                        checkEmail = true;
                }
                if(edtEmail.getText().length() == 0 || edtmkdk.getText().length() == 0 || edttendk.getText().length() == 0)
                    Toast.makeText(ActivityLogin.this, "Chưa điền đủ thông tin.", Toast.LENGTH_SHORT).show();
                else if(checkUser){
                    tvErrDK.setText("Tài khoản đã tồn tại");
                }
                else if(checkEmail)
                    tvErrDK.setText("Email đã được sử dụng!");
                else{
                    JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject.put("userName", username);
                        jsonObject.put("passWord", password);
                        jsonObject.put("money",300);
                        jsonObject.put("email",email);
                        jsonObject.put("phoneNumber",null);
                        jsonObject.put("linkImage","https://i.pinimg.com/236x/2c/13/08/2c130877570e4d477694d1236b0b94e5.jpg");
                    } catch (JSONException e){
                        e.printStackTrace();
                    }
                    final String requestBody = jsonObject.toString();
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
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
                    requestQueue.add(stringRequest);
                    edttendn.setText(username);
                    edtmkdn.setText(password);
                    dialog.cancel();
                    Toast.makeText(getBaseContext(),"Đăng ký thành công",Toast.LENGTH_LONG).show();
                    try {
                        list.add(new User(0,username,password,jsonObject.getInt("money"),email,"0","https://i.pinimg.com/236x/2c/13/08/2c130877570e4d477694d1236b0b94e5.jpg"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });


       tvdn.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               dialog.cancel();
           }
       });
        dialog.show();
    }
    public void DialogQuenMK(final List<User> id, final int i){
        LayoutInflater inflater = getLayoutInflater();
        View alertLayout = inflater.inflate(R.layout.dialogquenmk, null);

        final RequestQueue requestQueue = Volley.newRequestQueue(ActivityLogin.this);

        AlertDialog.Builder alert = new AlertDialog.Builder(ActivityLogin.this);
        alert.setView(alertLayout);
        alert.setCancelable(false);
        final EditText edtmkm = alertLayout.findViewById(R.id.edtmkm);
        Button btnxn = alertLayout.findViewById(R.id.btnxn);
        TextView tvql = (TextView)alertLayout.findViewById(R.id.tvql);
        final TextView tvErrMk = alertLayout.findViewById(R.id.tvErrMK);
        final AlertDialog dialog = alert.create();
        Random random = new Random();
        final int kt = random.nextInt((999999-100000)+1)+100000;
        btnxn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    if(edtmkm.getText().toString().equals(id.get(i).getEmail())) {
                        JSONObject jsonObject = new JSONObject();
                        try {
                            jsonObject.put("passWord", kt);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        final String requestbody = jsonObject.toString();
                        StringRequest stringRequest = new StringRequest(Request.Method.PATCH, url + "/" + id.get(i).getId(), new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                dialog.cancel();
                                Toast.makeText(getBaseContext(), "Mật khẩu đã được gửi về email.", Toast.LENGTH_SHORT).show();
                                edtmkdn.setText("");
                                ktmkm ++;
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {

                            }
                        }) {
                            @Override
                            public String getBodyContentType() {
                                return "application/json; charset = utf-8";
                            }

                            @Override
                            public byte[] getBody() throws AuthFailureError {
                                if (requestbody == null) return null;
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
                        requestQueue.add(stringRequest);
                        JavaMailAPI javaMailAPI = new JavaMailAPI(ActivityLogin.this, edtmkm.getText().toString(), "Quên mật khẩu", "Mật khẩu mới để truy cập app của bạn là: " + kt);
                        javaMailAPI.execute();
                        id.get(i).setPassword(String.valueOf(kt));
                        tvErr.setText("");
                    }
                    else Toast.makeText(ActivityLogin.this, "Email không đúng...", Toast.LENGTH_SHORT).show();
            }
        });
        tvql.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });
        dialog.show();
    }
    private void AnhXa(){
        edttendn = findViewById(R.id.edtten);
        edtmkdn = findViewById(R.id.edtmk);
        btndn = findViewById(R.id.btndn);
        tvquenmk = findViewById(R.id.tvquenmk);
        tvdk = findViewById(R.id.tvdk);
        tvErr = findViewById(R.id.tverr);
        cbsaved = findViewById(R.id.cbsaved);
    }

    @Override
    protected void onStart() {
        super.onStart();
        SharedPreferences sharedPreferences =getSharedPreferences(NAME_CACHE,MODE_PRIVATE);
        edttendn.setText(sharedPreferences.getString(USERNAME_CACHE,null));
        edtmkdn.setText(sharedPreferences.getString(PASSWORD_CACHE,null));
        if(edttendn.getText().length() > 0 && edtmkdn.getText().length() > 0)
            cbsaved.setChecked(true);
        else
            cbsaved.setChecked(false);
    }
}