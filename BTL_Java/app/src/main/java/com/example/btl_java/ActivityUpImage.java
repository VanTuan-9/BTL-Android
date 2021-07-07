package com.example.btl_java;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.btl_java.Fragment.UpBook;
import com.example.btl_java.RecycleView.Home.ListChild.BookUp;
import com.example.btl_java.RecycleView.Home.ListChild.BookUpParam;
import com.example.btl_java.UpBook.RealPathUtil;

import java.io.File;
import java.io.IOException;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivityUpImage extends AppCompatActivity {
    private static final int MY_REQUEST_CODE = 10;
    ImageView img_bookup;
    Button btn_choose_image, btn_submit_image;
    TextView tv_name_bookup, tv_author_bookup, tv_language_bookup, tv_number_bookup, tv_status_bookup;
    BookUp bookUp;
    private Uri mUri;
    ProgressDialog progressDialog;

    private ActivityResultLauncher<Intent> mIntentActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if(result.getResultCode() == Activity.RESULT_OK){
                        Intent data = result.getData();
                        if (data == null)   return;
                        Uri uri = data.getData();
                        mUri = uri;
                        try {
                            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),uri);
                            img_bookup.setImageBitmap(bitmap);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_upimage);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        Intent intent = getIntent();
        bookUp = intent.getParcelableExtra("BookUp");
        progressDialog = new ProgressDialog(getApplicationContext());
        progressDialog.setMessage("Đang gửi...");
        img_bookup = findViewById(R.id.img_bookup);
        btn_choose_image = findViewById(R.id.btn_choose_image);
        btn_submit_image = findViewById(R.id.btn_submit_image);
        tv_name_bookup = findViewById(R.id.tv_name_bookup);
        tv_author_bookup =findViewById(R.id.tv_author_bookup);
        tv_language_bookup = findViewById(R.id.tv_language_bookup);
        tv_number_bookup = findViewById(R.id.tv_number_bookup);
        tv_status_bookup = findViewById(R.id.tv_status_bookup);

        Glide.with(this).load("https://upload.wikimedia.org/wikipedia/commons/thumb/3/39/Book.svg/1200px-Book.svg.png").into(img_bookup);
        tv_name_bookup.setText(bookUp.getNameBook());
        tv_author_bookup.setText(bookUp.getAuthor());
        tv_language_bookup.setText(bookUp.getLanguageBook());
        tv_number_bookup.setText(bookUp.getNumberPages()+"");
        if(bookUp.getCategory() == 1)
            tv_status_bookup.setText("Chưa hoàn thành.");
        else
            tv_status_bookup.setText("Đã hoàn thành.");
        btn_choose_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickRequestPermission();
            }
        });
        btn_submit_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mUri != null){
                    UpAPIBook();
                    progressDialog.show();
                }
                else Toast.makeText(ActivityUpImage.this, "Chưa chọn ảnh!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void onClickRequestPermission() {
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.M){
            OpenGallery();
            return;
        }
        if(checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
            OpenGallery();
        } else {
            String [] permission = {Manifest.permission.READ_EXTERNAL_STORAGE};
            requestPermissions(permission,MY_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == MY_REQUEST_CODE)
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                OpenGallery();
            }
    }

    private void OpenGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        mIntentActivityResultLauncher.launch(Intent.createChooser(intent,"Select Picture"));
    }

    private void UpAPIBook() {
        RequestBody requestBodyNameBook = RequestBody.create(MediaType.parse("multipart/form-data"), bookUp.getNameBook().trim());
        String url = RealPathUtil.getRealPath(this,mUri);
        File file = new File(url);
        RequestBody requestBodyLinkBook = RequestBody.create(MediaType.parse("multipart/form-data"),url);
        MultipartBody.Part part = MultipartBody.Part.createFormData("linkBook",file.getName(),requestBodyLinkBook);
        RequestBody requestBodyauthor = RequestBody.create(MediaType.parse("multipart/form-data"), bookUp.getAuthor().trim());
        RequestBody requestBodypublishingCompany = RequestBody.create(MediaType.parse("multipart/form-data"), bookUp.getNXB().trim());
        RequestBody requestBodylanguage = RequestBody.create(MediaType.parse("multipart/form-data"), bookUp.getLanguageBook().trim());
        RequestBody requestBodynumberOfPages = RequestBody.create(MediaType.parse("multipart/form-data"), bookUp.getNumberPages()+"");
        RequestBody requestBodystatus = RequestBody.create(MediaType.parse("multipart/form-data"), bookUp.getCategory()+"");
        RequestBody requestBodycontent = RequestBody.create(MediaType.parse("multipart/form-data"), bookUp.getContent().trim());
        RequestBody requestBodydescribe = RequestBody.create(MediaType.parse("multipart/form-data"), bookUp.getShortContent().trim());
        RequestBody requestBodyprice = RequestBody.create(MediaType.parse("multipart/form-data"), bookUp.getPrice()+"");
        RequestBody requestBodyuserId = RequestBody.create(MediaType.parse("multipart/form-data"), bookUp.getIdUser()+"");
        UpBook.api.registerBook(requestBodyNameBook,part,requestBodyauthor,requestBodypublishingCompany,requestBodylanguage,
                requestBodynumberOfPages,requestBodystatus,requestBodycontent,requestBodydescribe,requestBodyprice,requestBodyuserId).enqueue(new Callback<BookUpParam>() {
            @Override
            public void onResponse(Call<BookUpParam> call, Response<BookUpParam> response) {
                Toast.makeText(ActivityUpImage.this, "Xong", Toast.LENGTH_SHORT).show();
                progressDialog.cancel();
                finish();
            }

            @Override
            public void onFailure(Call<BookUpParam> call, Throwable t) {

            }
        });
    }
}
