package com.example.btl_java.Fragment;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.btl_java.JsonVolley;
import com.example.btl_java.R;
import com.example.btl_java.RecycleView.Home.AutoScrollBook.Adapter;
import com.example.btl_java.RecycleView.Home.BXH.AdapterBXH;
import com.example.btl_java.RecycleView.Home.ListChild.Book;
import com.example.btl_java.RecycleView.Home.ListChild.ContentBook;
import com.example.btl_java.login.ActivityLogin;
import com.example.btl_java.login.User;
import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.ThreeBounce;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class HomeFragment extends Fragment {

    RecyclerView rcv_auto_scroll_image_book;
    RecyclerView rcv_item_bxh, rcv_type_book_new, rcv_type_book_view, rcv_type_book_like;
    Button btn_view_bxh, btn_like_bxh;
    ScrollView scroll_home;
    RelativeLayout rel_home;
    ImageButton ibtn_back;
    Dialog dialog;
    LinearLayout ln_home_fragment;
    List<Book> books = new ArrayList<>();
    User user = new User();
//    List<ContentBook> contentBooks = new ArrayList<>();
    int position;
    Timer timer;
    TimerTask timerTask;
    LinearLayoutManager manager;
    Adapter adapter;
    String urlBook = "https://api-book-last-comment.herokuapp.com/api/books";

    public static HomeFragment newInstance(List<Book> books,User user) {
        Bundle args = new Bundle();
        args.putParcelableArrayList("books", (ArrayList<? extends Parcelable>) books);
        args.putParcelable("User", user);
//        args.putParcelableArrayList("contentBooks", (ArrayList<? extends Parcelable>) contentBooks);
        HomeFragment fragment = new HomeFragment();
        fragment.setArguments(args);
        return fragment;
    }
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_fragment,container,false);
        rcv_auto_scroll_image_book = view.findViewById(R.id.rcv_auto_scroll_image_book);
        rcv_item_bxh = view.findViewById(R.id.rcv_item_bxh);
        rcv_type_book_new = view.findViewById(R.id.rcv_type_book_new);
        rcv_type_book_view = view.findViewById(R.id.rcv_type_book_view);
        rcv_type_book_like = view.findViewById(R.id.rcv_type_book_like);
        btn_view_bxh = view.findViewById(R.id.btn_view_bxh);
        btn_like_bxh = view.findViewById(R.id.btn_like_bxh);
        scroll_home = view.findViewById(R.id.scroll_home);
        rel_home = view.findViewById(R.id.rel_home);
        ibtn_back = view.findViewById(R.id.ibtn_back);
        ln_home_fragment = view.findViewById(R.id.ln_home_fragment);
        ibtn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(getContext(), ActivityLogin.class);
                startActivity(intent);
            }
        });
        user = getArguments().getParcelable("User");
        dialog = new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.progressbar_load_login);
        Window window = dialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        WindowManager.LayoutParams layoutParams =window.getAttributes();
        layoutParams.gravity = Gravity.CENTER;
        window.setAttributes(layoutParams);
        dialog.setCancelable(false);

        ProgressBar progressBar = dialog.findViewById(R.id.progress_login);
        Sprite style = new ThreeBounce();
        progressBar.setIndeterminateDrawable(style);
        ln_home_fragment.setVisibility(View.GONE);
        dialog.show();
        GetAPIBook(urlBook);
        return view;
    }

    private void PauseAutoScroll(){
        if(timer !=null && timerTask !=null){
            timer.cancel();
            timerTask.cancel();
            timer = null;
            timerTask = null;
            position = manager.findFirstVisibleItemPosition();
        }
    }

    private void AutoScroll(final RecyclerView recyclerView){
        if(timer == null && timerTask == null){
            timer = new Timer();
            timerTask = new TimerTask() {
                @Override
                public void run() {
                    if(position < adapter.getItemCount()) {
                        recyclerView.smoothScrollToPosition(position++);
                    }
                    else {
                        position = adapter.getItemCount()/2;
                        recyclerView.smoothScrollToPosition(adapter.getItemCount()/2);
                    }
                    Log.d("TAG",position+"");
                }
            };
            timer.schedule(timerTask,1000,1000);
        }
    }

    private void File() {
        books.add(new Book(1,
                "Làm Vợ Bác Sĩ",
                "https://sachvui.com/cover2/2020/doc-truyen-lam-vo-bac-si.jpg",
                "Văn Tuấn",
                "NXB Tự do",
                "VietNam",
                "21/04/2020",
                20,
                1,
                100320000,
                5040,
                "content",
                "21/03/2020",
                "Truyện Cô Vợ Nữ Cường Của Chu Tổng của tác giả Quỷ Quỷ kể về Diệp An Hạ, một cái tên rất bình thường nhưng lại mang một số phận không mấy suôn sẻ. Một cô gái không thể ở bên người mà mình chờ đợi mười hai năm. Bị cha xua đuổi, ghét bỏ chỉ vì mê tín dị đoan. Người chồng trên danh nghĩa thì luôn nghĩ cô là hạng đàn bà dơ bẩn.\n" +
                "\n" +
                "Bên cạnh đó, bạn cũng có thể đọc thêm Xuyên Qua Thời Không Ta Thành Vương Phi của cùng tác giả."));
        books.add(new Book(2,
                "Gian Nịnh Quốc Sư Yêu Tà Thê",
                "https://sachvui.com/cover2/2020/gian-ninh-quoc-su-yeu-ta-the.jpg",
                "Văn Tuấn",
                "NXB Tự do",
                "VietNam",
                "21/04/2020",
                20,
                1,
                10510000,
                111500,
                "content",
                "21/04/2020","Truyện Cô Vợ Nữ Cường Của Chu Tổng của tác giả Quỷ Quỷ kể về Diệp An Hạ, một cái tên rất bình thường nhưng lại mang một số phận không mấy suôn sẻ. Một cô gái không thể ở bên người mà mình chờ đợi mười hai năm. Bị cha xua đuổi, ghét bỏ chỉ vì mê tín dị đoan. Người chồng trên danh nghĩa thì luôn nghĩ cô là hạng đàn bà dơ bẩn.\n" +
                "\n" +
                "Bên cạnh đó, bạn cũng có thể đọc thêm Xuyên Qua Thời Không Ta Thành Vương Phi của cùng tác giả."));
        books.add(new Book(3,
                "Lấy Chồng Bạc Tỷ",
                "https://sachvui.com/cover2/2019/lay-chong-bac-ty.jpg",
                "Văn Tuấn",
                "NXB Tự do",
                "VietNam",
                "21/04/2020",
                20,
                1,
                12300000,
                12500,
                "content",
                "21/04/2020","Truyện Cô Vợ Nữ Cường Của Chu Tổng của tác giả Quỷ Quỷ kể về Diệp An Hạ, một cái tên rất bình thường nhưng lại mang một số phận không mấy suôn sẻ. Một cô gái không thể ở bên người mà mình chờ đợi mười hai năm. Bị cha xua đuổi, ghét bỏ chỉ vì mê tín dị đoan. Người chồng trên danh nghĩa thì luôn nghĩ cô là hạng đàn bà dơ bẩn.\n" +
                "\n" +
                "Bên cạnh đó, bạn cũng có thể đọc thêm Xuyên Qua Thời Không Ta Thành Vương Phi của cùng tác giả."));
        books.add(new Book(4,
                "Trò Chơi Tình Yêu",
                "https://sachvui.com/cover2/2020/tro-choi-tinh-yeu.jpg",
                "Văn Tuấn",
                "NXB Tự do",
                "VietNam",
                "21/04/2020",
                20,
                1,
                10000,
                500,
                "content",
                "21/04/2020","Truyện Cô Vợ Nữ Cường Của Chu Tổng của tác giả Quỷ Quỷ kể về Diệp An Hạ, một cái tên rất bình thường nhưng lại mang một số phận không mấy suôn sẻ. Một cô gái không thể ở bên người mà mình chờ đợi mười hai năm. Bị cha xua đuổi, ghét bỏ chỉ vì mê tín dị đoan. Người chồng trên danh nghĩa thì luôn nghĩ cô là hạng đàn bà dơ bẩn.\n" +
                "\n" +
                "Bên cạnh đó, bạn cũng có thể đọc thêm Xuyên Qua Thời Không Ta Thành Vương Phi của cùng tác giả."));
        books.add(new Book(5,
                "Cô Nàng Xinh Đẹp Của Tổng Tài",
                "https://sachvui.com/cover2/2020/co-nang-xinh-dep-cua-tong-tai.jpg",
                "Văn Tuấn",
                "NXB Tự do",
                "VietNam",
                "21/04/2020",
                20,
                1,
                112300000,
                123500,
                "content",
                "21/04/2020","Truyện Cô Vợ Nữ Cường Của Chu Tổng của tác giả Quỷ Quỷ kể về Diệp An Hạ, một cái tên rất bình thường nhưng lại mang một số phận không mấy suôn sẻ. Một cô gái không thể ở bên người mà mình chờ đợi mười hai năm. Bị cha xua đuổi, ghét bỏ chỉ vì mê tín dị đoan. Người chồng trên danh nghĩa thì luôn nghĩ cô là hạng đàn bà dơ bẩn.\n" +
                "\n" +
                "Bên cạnh đó, bạn cũng có thể đọc thêm Xuyên Qua Thời Không Ta Thành Vương Phi của cùng tác giả."));
        books.add(new Book(6,
                "Cao Thủ “Đổi Đen Thay Trắng”",
                "https://sachvui.com/cover2/2020/cao-thu-doi-den-thay-trang.jpg",
                "Văn Tuấn",
                "NXB Tự do",
                "VietNam",
                "21/04/2020",
                20,
                1,
                1032230000,
                512300,
                "content",
                "21/04/2020","Truyện Cô Vợ Nữ Cường Của Chu Tổng của tác giả Quỷ Quỷ kể về Diệp An Hạ, một cái tên rất bình thường nhưng lại mang một số phận không mấy suôn sẻ. Một cô gái không thể ở bên người mà mình chờ đợi mười hai năm. Bị cha xua đuổi, ghét bỏ chỉ vì mê tín dị đoan. Người chồng trên danh nghĩa thì luôn nghĩ cô là hạng đàn bà dơ bẩn.\n" +
                "\n" +
                "Bên cạnh đó, bạn cũng có thể đọc thêm Xuyên Qua Thời Không Ta Thành Vương Phi của cùng tác giả."));
        books.add(new Book(7,
                "Boss Cuồng Vợ Yêu",
                "https://sachvui.com/cover2/2020/boss-cuong-vo-yeu.jpg",
                "Văn Tuấn",
                "NXB Tự do",
                "VietNam",
                "21/04/2020",
                20,
                1,
                10003200,
                51200,
                "content",
                "21/04/2020","Truyện Cô Vợ Nữ Cường Của Chu Tổng của tác giả Quỷ Quỷ kể về Diệp An Hạ, một cái tên rất bình thường nhưng lại mang một số phận không mấy suôn sẻ. Một cô gái không thể ở bên người mà mình chờ đợi mười hai năm. Bị cha xua đuổi, ghét bỏ chỉ vì mê tín dị đoan. Người chồng trên danh nghĩa thì luôn nghĩ cô là hạng đàn bà dơ bẩn.\n" +
                "\n" +
                "Bên cạnh đó, bạn cũng có thể đọc thêm Xuyên Qua Thời Không Ta Thành Vương Phi của cùng tác giả."));
        books.add(new Book(8,
                "Xin Hãy Ôm Em",
                "https://sachvui.com/cover2/2020/xin-hay-om-em.jpg",
                "Văn Tuấn",
                "NXB Tự do",
                "VietNam",
                "21/04/2020",
                20,
                1,
                100000,
                500,
                "content",
                "21/04/2020","Truyện Cô Vợ Nữ Cường Của Chu Tổng của tác giả Quỷ Quỷ kể về Diệp An Hạ, một cái tên rất bình thường nhưng lại mang một số phận không mấy suôn sẻ. Một cô gái không thể ở bên người mà mình chờ đợi mười hai năm. Bị cha xua đuổi, ghét bỏ chỉ vì mê tín dị đoan. Người chồng trên danh nghĩa thì luôn nghĩ cô là hạng đàn bà dơ bẩn.\n" +
                "\n" +
                "Bên cạnh đó, bạn cũng có thể đọc thêm Xuyên Qua Thời Không Ta Thành Vương Phi của cùng tác giả."));
        books.add(new Book(9,
                "Hào Môn Tranh Đấu I: Người Tình Nhỏ Bên Cạnh Tổng Giám Đốc",
                "https://sachvui.com/cover2/2020/nguoi-tinh-nho-ben-canh-tong-giam-doc.jpg",
                "Văn Tuấn",
                "NXB Tự do",
                "VietNam",
                "21/04/2020",
                20,
                1,
                100000,
                500,
                "content",
                "21/04/2020","Truyện Cô Vợ Nữ Cường Của Chu Tổng của tác giả Quỷ Quỷ kể về Diệp An Hạ, một cái tên rất bình thường nhưng lại mang một số phận không mấy suôn sẻ. Một cô gái không thể ở bên người mà mình chờ đợi mười hai năm. Bị cha xua đuổi, ghét bỏ chỉ vì mê tín dị đoan. Người chồng trên danh nghĩa thì luôn nghĩ cô là hạng đàn bà dơ bẩn.\n" +
                "\n" +
                "Bên cạnh đó, bạn cũng có thể đọc thêm Xuyên Qua Thời Không Ta Thành Vương Phi của cùng tác giả."));
        books.add(new Book(10,
                "Khom Lưng Vì Anh",
                "https://sachvui.com/cover2/2020/khom-lung-vi-anh.jpg",
                "Văn Tuấn",
                "NXB Tự do",
                "VietNam",
                "21/04/2020",
                20,
                1,
                100000,
                500,
                "content",
                "21/04/2020","Truyện Cô Vợ Nữ Cường Của Chu Tổng của tác giả Quỷ Quỷ kể về Diệp An Hạ, một cái tên rất bình thường nhưng lại mang một số phận không mấy suôn sẻ. Một cô gái không thể ở bên người mà mình chờ đợi mười hai năm. Bị cha xua đuổi, ghét bỏ chỉ vì mê tín dị đoan. Người chồng trên danh nghĩa thì luôn nghĩ cô là hạng đàn bà dơ bẩn.\n" +
                "\n" +
                "Bên cạnh đó, bạn cũng có thể đọc thêm Xuyên Qua Thời Không Ta Thành Vương Phi của cùng tác giả."));
        books.add(new Book(11,
                "Phàm Nhân Tu Tiên Chi Tiên Giới Thiên (Phàm Nhân Tu Tiên 2)",
                "https://sachvui.com/cover2/2020/pham-nhan-tu-tien-chi-tien-gioi-thien-pham-nhan-tu-tien-2.jpg",
                "Văn Tuấn",
                "NXB Tự do",
                "VietNam",
                "21/04/2020",
                20,
                1,
                100000,
                500,
                "content",
                "21/04/2020","Truyện Cô Vợ Nữ Cường Của Chu Tổng của tác giả Quỷ Quỷ kể về Diệp An Hạ, một cái tên rất bình thường nhưng lại mang một số phận không mấy suôn sẻ. Một cô gái không thể ở bên người mà mình chờ đợi mười hai năm. Bị cha xua đuổi, ghét bỏ chỉ vì mê tín dị đoan. Người chồng trên danh nghĩa thì luôn nghĩ cô là hạng đàn bà dơ bẩn.\n" +
                "\n" +
                "Bên cạnh đó, bạn cũng có thể đọc thêm Xuyên Qua Thời Không Ta Thành Vương Phi của cùng tác giả."));
    }

    private void TypeBookNew(){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        Date currentDate = new Date();
        Date date1 = null;
        Date date2 = null;
        List<Book> demo = new ArrayList<>();
        for(int i = 0 ; i < books.size();i++)
            if(books.get(i).getCategory() == 1){
                Book book = new Book();
                book.setBook(books.get(i));
                demo.add(book);
            }
        for (int i = 0; i < demo.size() - 1; i++) {
            try {
                date1 = dateFormat.parse(demo.get(i).getTimeCreated());
                for (int j = i+1; j < demo.size(); j++) {
                    date2 = dateFormat.parse(demo.get(j).getTimeCreated());
                    if(date1.compareTo(date2) < 0){
                        Book tg = new Book();
                        tg.setBook(demo.get(i));
                        demo.get(i).setBook(demo.get(j));
                        demo.get(j).setBook(tg);
                    }
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        RecyclerView.LayoutManager manager = new LinearLayoutManager(getContext(),RecyclerView.HORIZONTAL,false);
        com.example.btl_java.RecycleView.Home.TypeBook.Item.Adapter adapter = new com.example.btl_java.RecycleView.Home.TypeBook.Item.Adapter(getContext(),demo,user);
        rcv_type_book_new.setAdapter(adapter);
        rcv_type_book_new.setLayoutManager(manager);
    }
    private void TypeBookView(){
        List<Book> demo = new ArrayList<>();
        for (int i = 0; i < books.size(); i++) {
            Book book = new Book();
            book.setBook(books.get(i));
            demo.add(book);
        }
        for (int i = 0; i < demo.size()-1; i++) {
            for (int j = i+1; j < demo.size(); j++) {
                if(demo.get(i).getView() < demo.get(j).getView()){
                    Book tg = new Book();
                    tg.setBook(demo.get(i));
                    demo.get(i).setBook(demo.get(j));
                    demo.get(j).setBook(tg);
                }
            }
        }
        RecyclerView.LayoutManager manager = new LinearLayoutManager(getContext(),RecyclerView.HORIZONTAL,false);
        com.example.btl_java.RecycleView.Home.TypeBook.Item.Adapter adapter = new com.example.btl_java.RecycleView.Home.TypeBook.Item.Adapter(getContext(),demo,user);
        rcv_type_book_view.setLayoutManager(manager);
        rcv_type_book_view.setAdapter(adapter);
    }
    private void TypeBookLike(){
        List<Book> demo = new ArrayList<>();
        for (int i = 0; i < books.size(); i++) {
            Book book = new Book();
            book.setBook(books.get(i));
            demo.add(book);
        }
        for (int i = 0; i < demo.size()-1; i++) {
            for (int j = i+1; j < demo.size(); j++) {
                if(demo.get(i).getLikes() < demo.get(j).getLikes()){
                    Book tg = new Book();
                    tg.setBook(demo.get(i));
                    demo.get(i).setBook(demo.get(j));
                    demo.get(j).setBook(tg);
                }
            }
        }
        RecyclerView.LayoutManager manager = new LinearLayoutManager(getContext(),RecyclerView.HORIZONTAL,false);
        com.example.btl_java.RecycleView.Home.TypeBook.Item.Adapter adapter = new com.example.btl_java.RecycleView.Home.TypeBook.Item.Adapter(getContext(),demo,user);
        rcv_type_book_like.setLayoutManager(manager);
        rcv_type_book_like.setAdapter(adapter);
    }
    private void GetAPIBook(String urlBook){
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, urlBook, null,
                new Response.Listener<JSONArray>() {
                    @RequiresApi(api = Build.VERSION_CODES.M)
                    @Override
                    public void onResponse(JSONArray response) {
                        ChangeJson(response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        JsonVolley.getInstance(getContext()).getRequestQueue().add(request);
    }
    @RequiresApi(api = Build.VERSION_CODES.M)
    void ChangeJson(JSONArray jsonArray) {
        JSONObject jsonObject;
        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                jsonObject = jsonArray.getJSONObject(i);
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
//                contentBooks.add(new ContentBook(jsonObject.getInt("id"),jsonObject.getString("contentBook").split("\\s")));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        ln_home_fragment.setVisibility(View.VISIBLE);
        dialog.dismiss();
        List<String> list_img_book = new ArrayList<>();
        for (int i = 0;i< books.size();i++)
            list_img_book.add(books.get(i).getUrlBook());
        manager = new LinearLayoutManager(getContext(),RecyclerView.HORIZONTAL,false);
        adapter = new Adapter(getContext(),list_img_book);
        rcv_auto_scroll_image_book.setLayoutManager(manager);
        rcv_auto_scroll_image_book.setAdapter(adapter);
        position = adapter.getItemCount()/2;
        rcv_auto_scroll_image_book.scrollToPosition(position);
        SnapHelper snapHelper = new LinearSnapHelper();
        snapHelper.attachToRecyclerView(rcv_auto_scroll_image_book);
        rcv_auto_scroll_image_book.smoothScrollBy(1,0);
        rcv_auto_scroll_image_book.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if(newState == 1)
                    PauseAutoScroll();
                else if(newState == 0)
                    AutoScroll(rcv_auto_scroll_image_book);
            }
        });
        final List<Book> book_like = new ArrayList<>();
        final List<Book> book_view = new ArrayList<>();
        for (int i =0;i < books.size();i++){
            book_like.add(books.get(i));
            book_view.add(books.get(i));
        }
        for (int i =0;i<book_like.size()-1;i++){
            for(int j = i+1;j<book_like.size();j++){
                if(book_like.get(i).getLikes() < book_like.get(j).getLikes()){
                    Book tg = new Book();
                    tg.setBook(book_like.get(i));
                    book_like.get(i).setBook(book_like.get(j));
                    book_like.get(j).setBook(tg);
                }
            }
        }

        for (int i =0;i<book_view.size()-1;i++){
            for(int j = i+1;j<book_view.size();j++){
                if(book_view.get(i).getView() < book_view.get(j).getView()){
                    Book tg = new Book();
                    tg.setBook(book_view.get(i));
                    book_view.get(i).setBook(book_view.get(j));
                    book_view.get(j).setBook(tg);
                }
            }
        }

        final LinearLayoutManager managerBXH = new LinearLayoutManager(getContext(),RecyclerView.VERTICAL,false);
        AdapterBXH adapterBXH = new AdapterBXH(book_like,getContext(),1,user);
        rcv_item_bxh.setLayoutManager(managerBXH);
        rcv_item_bxh.setAdapter(adapterBXH);

        btn_like_bxh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AdapterBXH adapterBXH = new AdapterBXH(book_like,getContext(),1,user);
                rcv_item_bxh.setLayoutManager(managerBXH);
                rcv_item_bxh.setAdapter(adapterBXH);
                btn_like_bxh.setBackgroundResource(R.drawable.background_select_bxh);
                btn_view_bxh.setBackground(null);
            }
        });
        btn_view_bxh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AdapterBXH adapterBXH = new AdapterBXH(book_view,getContext(),2,user);
                rcv_item_bxh.setLayoutManager(managerBXH);
                rcv_item_bxh.setAdapter(adapterBXH);
                btn_view_bxh.setBackgroundResource(R.drawable.background_select_bxh);
                btn_like_bxh.setBackground(null);
            }
        });

        TypeBookNew();
        TypeBookView();
        TypeBookLike();
        final int i = scroll_home.getScrollY()+20;
        scroll_home.setOnScrollChangeListener(new View.OnScrollChangeListener() {
            @Override
            public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if(i<scrollY)  rel_home.setVisibility(View.GONE);
                else    rel_home.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        books = new ArrayList<>();
    }
}
