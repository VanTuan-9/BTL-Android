package com.example.btl_java.Adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.btl_java.Fragment.AccountFragment;
import com.example.btl_java.Fragment.HomeFragment;
import com.example.btl_java.Fragment.SearchFragment;
import com.example.btl_java.Fragment.UpBookFragment;
import com.example.btl_java.RecycleView.Home.ListChild.Book;
import com.example.btl_java.RecycleView.Home.ListChild.ContentBook;
import com.example.btl_java.login.User;

import java.util.ArrayList;
import java.util.List;

public class FragmentAdapter extends FragmentPagerAdapter {

    List<Book> books;
    User user;
//    List<ContentBook> contentBooks;

    public FragmentAdapter(@NonNull FragmentManager fm, int behavior, List<Book> books,User user) {
        super(fm, behavior);
        this.books = books;
        this.user = user;
//        this.contentBooks = contentBooks;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return HomeFragment.newInstance(books,user);
            case 1:
                return SearchFragment.newInstance(books,user);
            case 2:
                return UpBookFragment.newInstance();
            case 3:
                return AccountFragment.newInstance(books,user);
            default:
                return HomeFragment.newInstance(books,user);
        }
    }

    @Override
    public int getCount() {
        return 4;
    }
}
