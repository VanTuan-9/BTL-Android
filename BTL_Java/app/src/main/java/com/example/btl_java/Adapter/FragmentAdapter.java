package com.example.btl_java.Adapter;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.btl_java.Fragment.AccountFragment;
import com.example.btl_java.Fragment.HomeFragment;
import com.example.btl_java.Fragment.SearchFragment;
import com.example.btl_java.Fragment.UpBookFragment;
import com.example.btl_java.RecycleView.Home.ListChild.Book;
import com.example.btl_java.login.User;

import java.util.List;

public class FragmentAdapter extends FragmentPagerAdapter {
    User user;
    List<Book> books;
//    List<ContentBook> contentBooks;

    public FragmentAdapter(@NonNull FragmentManager fm, int behavior, User user, List<Book> books) {
        super(fm, behavior);
        this.user = user;
        this.books = books;
//        this.contentBooks = contentBooks;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return HomeFragment.newInstance(user,books);
            case 1:
                return SearchFragment.newInstance(user);
            case 2:
                return UpBookFragment.newInstance(user.getId());
            case 3:
                return AccountFragment.newInstance(user);
            default:
                return HomeFragment.newInstance(user, books);
        }
    }

    @Override
    public int getCount() {
        return 4;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        super.destroyItem(container, position, object);
    }
}
