package com.bhanchha.app;

import android.app.Activity;

import android.app.ActionBar;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.widget.DrawerLayout;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;


public class Main extends Activity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;
    private DrawerLayout mDrawerLayout;

    private boolean backAgainNotice = false;
    Handler timerHandler = new Handler();
    Runnable timerRunnable = new Runnable() {
        @Override
        public void run() { backAgainNotice = false; }
    };

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle = getString(R.string.title_section_browse_cook);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //getActionBar().setBackgroundDrawable(new ColorDrawable(0xff8f5c23));
        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getFragmentManager().findFragmentById(R.id.navigation_drawer);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                mDrawerLayout);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments or switching activity
        if (position == getResources().getInteger(R.integer.drawer_position_my_orders)) {
            Intent intent = new Intent(this, MyOrdersActivity.class);
            startActivity(intent);
        } else if (position == getResources().getInteger(R.integer.drawer_position_about_bhanchha)) {
            Intent intent = new Intent(this, AboutBhanchhaActivity.class);
            startActivity(intent);
        } else {
            FragmentManager fragmentManager = getFragmentManager();
            if (position == getResources().getInteger(R.integer.drawer_position_browse_food)) {
                fragmentManager.beginTransaction()
                        .replace(R.id.container, BrowseFoodFragment.newInstance())
                        .commit();
                mTitle = getString(R.string.title_section_browse_food);
            } else if (position == getResources().getInteger(R.integer.drawer_position_browse_cook)) {
                fragmentManager.beginTransaction()
                        .replace(R.id.container, BrowseCookFragment.newInstance())
                        .commit();
                mTitle = getString(R.string.title_section_browse_cook);
            } else if (position == getResources().getInteger(R.integer.drawer_position_browse_favorite)) {
                fragmentManager.beginTransaction()
                        .replace(R.id.container, BrowseFavoriteFragment.newInstance())
                        .commit();
                mTitle = getString(R.string.title_section_browse_favorite);
            } else { // that is if a unicorn shows itself
                             fragmentManager.beginTransaction()
                                     .replace(R.id.container, PlaceholderFragment.newInstance(position+1))
                                     .commit();
                         }
        }
    }

    public void onSectionAttached(int number) {
        // has become non functional for some unknown reason
        switch (number) {
            case 1:
                mTitle = getString(R.string.title_section_browse_food);
                break;
            case 2:
                mTitle = getString(R.string.title_section_browse_cook);
                break;
            case 3:
                mTitle = getString(R.string.title_section_browse_favorite);
                break;
        }
    }

    public void restoreActionBar() {
        ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        //actionBar.setHomeButtonEnabled(false);
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setTitle(mTitle);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.main, menu);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//        if (id == R.id.action_settings) {
//            return true;
//        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(Gravity.START))
            mDrawerLayout.closeDrawer(Gravity.START);
        else {
            if (backAgainNotice)
                super.onBackPressed();
            else {
                backAgainNotice = true;
                // set timer to set it to false
                timerHandler.postDelayed(timerRunnable, 3500);
                Toast.makeText(this, "Press again to exit", Toast.LENGTH_LONG).show();
            }
        }
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

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            ((TextView) rootView.findViewById(R.id.section_label))
                    .setText("This phucker is called Section " + getArguments().getInt(ARG_SECTION_NUMBER));
            return rootView;
        }

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
            ((Main) activity).onSectionAttached(
                    getArguments().getInt(ARG_SECTION_NUMBER));
        }
    }

}
