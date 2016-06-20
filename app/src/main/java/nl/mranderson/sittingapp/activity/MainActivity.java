package nl.mranderson.sittingapp.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import java.util.ArrayList;
import java.util.List;

import nl.mranderson.sittingapp.R;
import nl.mranderson.sittingapp.fragment.InfoFragment;
import nl.mranderson.sittingapp.fragment.MainFragment;
import nl.mranderson.sittingapp.fragment.SettingsFragment;

public class MainActivity extends AppCompatActivity {

    //TODO add images
    //TODO add text
    //TODO add that we know he is moving and the setting is on ! (change icon in TimerFragment and let them know in the tutorial)
    //TODO version 1.6                                  MAY To Alpha
    //TODO do refactoring and testing
    //TODO change screenshots
    //TODO create video?
    //TODO release version 3.0 to public                JUNE?

    private int[] tabIcons = {
            R.drawable.ic_directions_walk_white_48dp,
            R.drawable.ic_settings_white_36dp,
            R.drawable.ic_help_white_48dp
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        setupTabIcons(tabLayout);
    }

    private void setupTabIcons(TabLayout tabLayout) {
        tabLayout.getTabAt(0).setIcon(tabIcons[0]);
        tabLayout.getTabAt(1).setIcon(tabIcons[1]);
        tabLayout.getTabAt(2).setIcon(tabIcons[2]);
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new MainFragment(), "ONE");
        adapter.addFragment(new SettingsFragment(), "TWO");
        adapter.addFragment(new InfoFragment(), "THREE");
        viewPager.setAdapter(adapter);
    }

    private class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        ViewPagerAdapter(FragmentManager manager) {
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

        void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return null;
        }
    }
}
