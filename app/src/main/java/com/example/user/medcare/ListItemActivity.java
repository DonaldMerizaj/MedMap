package com.example.user.medcare;

import android.net.Uri;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.user.medcare.fragments.FarmaciFragment;
import com.example.user.medcare.fragments.Farmaci_Map;
import com.example.user.medcare.fragments.Qsh_Map;

import java.util.ArrayList;
import java.util.List;

public class ListItemActivity extends AppCompatActivity implements FarmaciFragment.OnFragmentInteractionListener {

    private ViewPager mViewPager;
    private TabLayout mTabLayout;
    private ListItemActivity.ViewPagerAdapter mPageAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_item);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarMain);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.

        mViewPager = (ViewPager) findViewById(R.id.container);
        setupViewPager(mViewPager);

        mTabLayout = (TabLayout) findViewById(R.id.tabs);
        mTabLayout.setupWithViewPager(mViewPager);
        setupTabTitles();
    }



    private void setupTabTitles() {
        mTabLayout.getTabAt(0).setText("FARMACI");
//        mTabLayout.getTabAt(1).setText("QSH");
    }

    private void setupViewPager(ViewPager viewPager) {

        mPageAdapter = new ListItemActivity.ViewPagerAdapter(getSupportFragmentManager());
        mPageAdapter.addFragment(new FarmaciFragment(), "");
//        mPageAdapter.addFragment(new Qsh_Map(), "");

        viewPager.setAdapter(mPageAdapter);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static ListItemActivity.PlaceholderFragment newInstance(int sectionNumber) {
            ListItemActivity.PlaceholderFragment fragment = new ListItemActivity.PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_farmaci_list, container, false);
            return rootView;
        }
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

}
