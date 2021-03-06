package com.example.btl_java.Fragment;

import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.Editable;
import android.text.TextWatcher;
import android.transition.TransitionManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.btl_java.JsonVolley;
import com.example.btl_java.R;
import com.example.btl_java.RecycleView.Home.ListChild.Book;
import com.example.btl_java.RecycleView.Home.ListChild.ContentBook;
import com.example.btl_java.RecycleView.Search.Adapter;
import com.example.btl_java.RecycleView.Search.AdapterSearched;
import com.example.btl_java.login.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SearchFragment extends Fragment {

    List<Book> books = new ArrayList<>();
    User user;
    List<ContentBook> contentBooks;
    RecyclerView rcv_all_item_search;
    EditText edt_search;
    ImageButton ibtn_menu;
    LinearLayout ln_search;
    RecyclerView rcv_book_near;
    TextView infor_search;
    RecyclerView rcv_item_search;

    public static SearchFragment newInstance(User user) {

        Bundle args = new Bundle();
        args.putParcelable("User",user);
//        args.putParcelableArrayList("contentBooks", (ArrayList<? extends Parcelable>) contentBooks);
        SearchFragment fragment = new SearchFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.search_fragment,container,false);
        rcv_all_item_search = view.findViewById(R.id.rcv_all_item_search);
        edt_search = view.findViewById(R.id.edt_search);
        ibtn_menu = view.findViewById(R.id.ibtn_menu);
        ln_search = view.findViewById(R.id.ln_search);
        rcv_book_near = view.findViewById(R.id.rcv_book_near);
        infor_search = view.findViewById(R.id.infor_search);
        rcv_item_search = view.findViewById(R.id.rcv_item_search);

        user = getArguments().getParcelable("User");

        GetAPI(user);
//        contentBooks = getArguments().getParcelableArrayList("contentbooks");

        return view;
    }

    private void GetAPI(final User user) {
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, "https://api-book-last-comment.herokuapp.com/api/books", null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        for (int i = 0; i < response.length(); i++) {
                            try {
                                JSONObject jsonObject = response.getJSONObject(i);
                                books.add(new Book(jsonObject.getInt("id"),
                                        jsonObject.getString("nameBook"),
                                        jsonObject.getString("linkBook"),
                                        jsonObject.getString("author"),
                                        jsonObject.getString("publishingCompany"),
                                        jsonObject.getString("language"),
                                        jsonObject.getString("createAt"),
                                        jsonObject.getInt("numberOfPages"), 1,
                                        jsonObject.getInt("viewBook"),
                                        jsonObject.getInt("likeBook"),
                                        jsonObject.getString("contentBook"),
                                        jsonObject.getString("updateAt"),
                                        jsonObject.getString("describeBook")));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        final RecyclerView.LayoutManager manager = new LinearLayoutManager(getContext(),RecyclerView.VERTICAL,false);
                        Adapter adapter = new Adapter(books,getContext(),user);
                        rcv_all_item_search.setAdapter(adapter);
                        rcv_all_item_search.setLayoutManager(manager);

                        edt_search.addTextChangedListener(new TextWatcher() {
                            @Override
                            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                            }

                            @Override
                            public void onTextChanged(CharSequence s, int start, int before, int count) {

                            }

                            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                            @Override
                            public void afterTextChanged(Editable s) {
                                TransitionManager.beginDelayedTransition((ViewGroup) getView());
                                if(s.length() == 0){
                                    ln_search.setVisibility(View.VISIBLE);
                                    infor_search.setVisibility(View.GONE);
                                    rcv_item_search.setVisibility(View.GONE);
                                }
                                else{
                                    List<Book> books_search = new ArrayList<>();
                                    for (int i = 0; i < books.size(); i++) {
                                        if(removeAccent(books.get(i).getNameBook()).toLowerCase().contains(removeAccent(s.toString().toLowerCase())))
                                            books_search.add(books.get(i));
                                    }
                                    ln_search.setVisibility(View.GONE);
                                    infor_search.setVisibility(View.VISIBLE);

                                    if(books_search.size() == 0)
                                        infor_search.setText("Kh??ng t???n t???i!");
                                    else
                                        infor_search.setText("C?? " + books_search.size() + " k???t qu??? ph?? h???p.");
                                    rcv_item_search.setVisibility(View.VISIBLE);
                                    RecyclerView.LayoutManager manager1 = new LinearLayoutManager(getContext(),RecyclerView.VERTICAL,false);
                                    AdapterSearched adapterSearched = new AdapterSearched(books_search,getContext(),user);
                                    rcv_item_search.setLayoutManager(manager1);
                                    rcv_item_search.setAdapter(adapterSearched);
                                }
                            }
                        });
                        rcv_item_search.setItemViewCacheSize(0);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        JsonVolley.getInstance(getContext()).getRequestQueue().add(request);
    }

    private static final char[] SOURCE_CHARACTERS = {'??', '??', '??', '??', '??', '??',
            '??', '??', '??', '??', '??', '??', '??', '??', '??', '??', '??', '??', '??',
            '??', '??', '??', '??', '??', '??', '??', '??', '??', '??', '??', '??', '??',
            '??', '??', '??', '??', '??', '??', '??', '??', '??', '??', '??', '??', '???',
            '???', '???', '???', '???', '???', '???', '???', '???', '???', '???', '???', '???', '???',
            '???', '???', '???', '???', '???', '???', '???', '???', '???', '???', '???', '???', '???',
            '???', '???', '???', '???', '???', '???', '???', '???', '???', '???', '???', '???', '???',
            '???', '???', '???', '???', '???', '???', '???', '???', '???', '???', '???', '???', '???',
            '???', '???', '???', '???', '???', '???', '???', '???', '???', '???', '???', '???', '???',
            '???', '???', '???', '???', '???', '???', '???', '???', '???', '???', '???', '???', '???',
            '???', '???', '???',};

    private static final char[] DESTINATION_CHARACTERS = {'A', 'A', 'A', 'A', 'E',
            'E', 'E', 'I', 'I', 'O', 'O', 'O', 'O', 'U', 'U', 'Y', 'a', 'a',
            'a', 'a', 'e', 'e', 'e', 'i', 'i', 'o', 'o', 'o', 'o', 'u', 'u',
            'y', 'A', 'a', 'D', 'd', 'I', 'i', 'U', 'u', 'O', 'o', 'U', 'u',
            'A', 'a', 'A', 'a', 'A', 'a', 'A', 'a', 'A', 'a', 'A', 'a', 'A',
            'a', 'A', 'a', 'A', 'a', 'A', 'a', 'A', 'a', 'A', 'a', 'E', 'e',
            'E', 'e', 'E', 'e', 'E', 'e', 'E', 'e', 'E', 'e', 'E', 'e', 'E',
            'e', 'I', 'i', 'I', 'i', 'O', 'o', 'O', 'o', 'O', 'o', 'O', 'o',
            'O', 'o', 'O', 'o', 'O', 'o', 'O', 'o', 'O', 'o', 'O', 'o', 'O',
            'o', 'O', 'o', 'U', 'u', 'U', 'u', 'U', 'u', 'U', 'u', 'U', 'u',
            'U', 'u', 'U', 'u',};

    private char removeAccent(char ch){
        int index = Arrays.binarySearch(SOURCE_CHARACTERS,ch);
        if(index >= 0)
            ch = DESTINATION_CHARACTERS[index];
        return ch;
    }
    private String removeAccent(String str){
        StringBuilder stringBuilder = new StringBuilder(str);
        for (int i = 0;i<stringBuilder.length();i++){
            stringBuilder.setCharAt(i,removeAccent(stringBuilder.charAt(i)));
        }
        return stringBuilder.toString();
    }
}
