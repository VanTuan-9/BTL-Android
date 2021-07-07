package com.example.btl_java.Fragment;

import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bumptech.glide.Glide;
import com.example.btl_java.JsonVolley;
import com.example.btl_java.R;
import com.example.btl_java.RecycleView.Home.ListChild.Book;
import com.example.btl_java.login.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class AccountFragment extends Fragment {
    public static AccountFragment newInstance(User user) {
        Bundle args = new Bundle();
        args.putParcelable("User", user);
        AccountFragment fragment = new AccountFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private User user = new User();
    User user1;
    ImageView avtUser, back;
    TextView username;
    TextView tvXu, tvSach, tvLike;
    RelativeLayout LO2, LO4, LO5, LO6, LO7;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layoutfragmentuser,container,false);
        user = getArguments().getParcelable("User");
        AnhXa(view);
        GetAPIUsser(user.getId());

        return view;
    }

    private void GetAPIUsser(int id) {
        String url1 = "https://api-user-last.herokuapp.com/api/users/" + id;
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url1, null,
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
                        Glide.with(getContext()).load(user.getLinkImg()).into(avtUser);
                        username.setText(user.getUsername());
                        tvXu.setText(((int) user.getMoney()) + "");
                        avtUser.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                            }
                        });
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        JsonVolley.getInstance(getContext()).getRequestQueue().add(request);
    }

    private void AnhXa(View view) {
        avtUser = view.findViewById(R.id.avtUser);
        back = view.findViewById(R.id.back);
        username = view.findViewById(R.id.username);
        tvXu = view.findViewById(R.id.tvXu);
        tvSach = view.findViewById(R.id.tvSach);
        tvLike = view.findViewById(R.id.tvLike);
        LO2 = view.findViewById(R.id.LO2);
        LO4 = view.findViewById(R.id.LO4);
        LO5 = view.findViewById(R.id.LO5);
        LO6 = view.findViewById(R.id.LO6);
        LO7 = view.findViewById(R.id.LO7);
    }
}
