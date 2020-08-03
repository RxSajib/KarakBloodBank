package Adapter;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import TabPages.DonorPage;
import TabPages.RequestPage;

public class PageAdapter extends FragmentPagerAdapter {

    public PageAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:

            DonorPage donorPage = new DonorPage();
            return donorPage;

            case 1:
                RequestPage requestPage = new RequestPage();
                return requestPage;

            default:
                return null;
        }


    }

    @Override
    public int getCount() {
        return 2;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {


        switch (position){
            case 0:
                return "Donors";

            case 1:
                return "Request User";

        }
        return super.getPageTitle(position);
    }
}
