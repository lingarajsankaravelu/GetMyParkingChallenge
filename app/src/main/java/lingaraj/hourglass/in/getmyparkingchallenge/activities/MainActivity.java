package lingaraj.hourglass.in.getmyparkingchallenge.activities;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.SeekBar;

import com.thefinestartist.finestwebview.FinestWebView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lingaraj.hourglass.in.getmyparkingchallenge.KickStarterApp;
import lingaraj.hourglass.in.getmyparkingchallenge.R;
import lingaraj.hourglass.in.getmyparkingchallenge.adapters.KickStarterAdapter;
import lingaraj.hourglass.in.getmyparkingchallenge.cloud.service.KickStarter;
import lingaraj.hourglass.in.getmyparkingchallenge.cloud.service.Response.KickStarterResponse;
import lingaraj.hourglass.in.getmyparkingchallenge.databinding.ActivityMainBinding;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class MainActivity extends AppCompatActivity implements android.support.v7.widget.SearchView.OnQueryTextListener {

    ActivityMainBinding activity_binding;
    private String TAG="MAINACT";
    private List<KickStarterResponse> project_list;
    private Map<String,KickStarterResponse> project_map = new HashMap<>();
    private KickStarterAdapter mAdapter;
    private boolean show_menu = false;
    int progress_tracker = 0;
    int progress_filter = 0;
    private int maximum_backed_by_value = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        activity_binding = DataBindingUtil.setContentView(this,R.layout.activity_main);
        setUpViews();
        setToolbar();
        showLoading();
        makerNetworkCall();
        activity_binding.loaderInclude.retryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                makerNetworkCall();
            }


        });

        activity_binding.seekbrInclude.seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
                progress_tracker = progress;
                activity_binding.seekbrInclude.progressDisplayText.setText(String.valueOf(progress));

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        activity_binding.seekbrInclude.saveProgress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progress_filter = progress_tracker;
                activity_binding.seekbrInclude.progressDisplayText.setText(String.valueOf(progress_filter));
                hidefilter();
                updateAdapter();
            }
        });
        activity_binding.seekbrInclude.cancelProgress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progress_tracker = 0;
                activity_binding.seekbrInclude.progressDisplayText.setText(String.valueOf(progress_filter));
            }
        });

    }

    private void setToolbar() {
        setSupportActionBar(activity_binding.toolbar);
        if (getSupportActionBar()!=null)
        {
            getSupportActionBar().setTitle(getString(R.string.main_activity_title));
        }
    }

    private void setUpViews() {
        mAdapter = new KickStarterAdapter(getApplicationContext(),new CardClick());
    }

    private void makerNetworkCall() {

        KickStarterApp app = (KickStarterApp) getApplication();
        if (app.isNetworkAvailable())
        {
            RestAdapter restAdapter = app.getCloudAdapter();
            KickStarter service = restAdapter.create(KickStarter.class);
            service.getKickstarterDemo(new Callback<List<KickStarterResponse>>() {
                @Override
                public void success(List<KickStarterResponse> kickStarterResponses, Response response) {
                    if (kickStarterResponses.size()>0)
                    {
                        project_list = kickStarterResponses;
                        generateDataMaps();
                        setData();
                        showContainer();
                    }
                }

                @Override
                public void failure(RetrofitError error) {
                 Log.d(TAG,"Network Call Failed");
                    showError();
                    Log.d(TAG,error.toString());
                }
            });
        }
        else
        {
            Log.d(TAG,"Network Unavailable to make Network Call");
            showError();
        }

    }

    private void showContainer() {
        showMenu();
        if (activity_binding.viewSwitcher.getDisplayedChild()!=1)
        {
            activity_binding.viewSwitcher.showNext();
        }
        Log.d(TAG,"Showing Data Container");
    }



    private void generateDataMaps() {
        int highest_backed_by_value = 0;
        for (KickStarterResponse record: project_list) {
            //add the current data to map with it's title as key, useful when searched by title.
            this.project_map.put(record.getTitle(),record);
            if (!record.getBackers().matches(".*[a-zA-Z]+.*"))
            {
                int backed_by_count = Integer.valueOf(record.getBackers());
                if (highest_backed_by_value<backed_by_count)
                {
                    highest_backed_by_value = backed_by_count;
                }
            }
        }
        maximum_backed_by_value  = highest_backed_by_value;
    }

    private void setData() {
        activity_binding.mRecyclerview.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        activity_binding.mRecyclerview.setHasFixedSize(true);
        progress_filter = maximum_backed_by_value;
        activity_binding.seekbrInclude.progressDisplayText.setText(String.valueOf(progress_filter));
        activity_binding.seekbrInclude.seekBar.setProgress(progress_filter);
        mAdapter.setData(project_map);
        activity_binding.mRecyclerview.setAdapter(mAdapter);
        Log.d(TAG,"Showing Container");



    }

    private void showLoading() {
        activity_binding.loaderInclude.bubbleAssessmentProgress.setVisibility(View.VISIBLE);
        activity_binding.loaderInclude.errorMessage.setText(getString(R.string.message_common_loading));
        activity_binding.loaderInclude.errorImage.setVisibility(View.GONE);
        activity_binding.loaderInclude.retryButton.setVisibility(View.GONE);
        hideMenu();
       if (activity_binding.viewSwitcher.getDisplayedChild()!=0)
       {
           activity_binding.viewSwitcher.showPrevious();
       }
        Log.d(TAG,"Showing Loader");
    }



    private void showError() {
        activity_binding.loaderInclude.bubbleAssessmentProgress.setVisibility(View.INVISIBLE);
        activity_binding.loaderInclude.errorMessage.setText(getString(R.string.message_common_network_error));
        activity_binding.loaderInclude.errorImage.setVisibility(View.VISIBLE);
        activity_binding.loaderInclude.errorImage.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),R.drawable.ic_network_off));
        activity_binding.loaderInclude.retryButton.setVisibility(View.VISIBLE);
        hideMenu();
        if (activity_binding.viewSwitcher.getDisplayedChild()!=0)
        {
            activity_binding.viewSwitcher.showPrevious();
        }
        Log.d(TAG,"Showing Retry option");

    }
    private void showMenu()
    {
        this.show_menu = true;
        this.invalidateOptionsMenu();
        Log.d(TAG,"Menu Hidden");
    }

    private void hideMenu()
    {
        this.show_menu =false;
        this.invalidateOptionsMenu();
        Log.d(TAG,"hide menu");
    }
    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        if(newText.isEmpty())
        {
            mAdapter.setData(this.project_map);
        }
        else {
            searchByTitle(newText);
        }

        return false;
    }

    private void searchByTitle(String query) {
        List<String> titles = new ArrayList<>(this.project_map.keySet());
        Map<String,KickStarterResponse> filtered_name_list_map = new HashMap<>();
        query = query.toLowerCase();
        for (int index = 0 ; index <titles.size();index ++)
        {
            String model_query = titles.get(index).toLowerCase();
            if(model_query.contains(query))
            {
                String title = titles.get(index);
                filtered_name_list_map.put(title,this.project_map.get(title));
            }

        }
        if (filtered_name_list_map.size()>0) {
            mAdapter.setData(filtered_name_list_map);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main_activity,menu);
        MenuItem search_menu_toolbar = menu.findItem(R.id.action_search_evaluated);
        final android.support.v7.widget.SearchView view_search = (android.support.v7.widget.SearchView) MenuItemCompat.getActionView(search_menu_toolbar);
        view_search.setOnQueryTextListener(this);
        return  true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (show_menu)
        {
            menu.getItem(0).setVisible(true);
            menu.getItem(1).setVisible(false);

        }
        else
        {
            menu.getItem(0).setVisible(false);
            menu.getItem(1).setVisible(false);

        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()==R.id.action_filter)
        {
            showFilter();
        }
        return super.onOptionsItemSelected(item);
    }

    public class CardClick implements View.OnClickListener
   {

       @Override
       public void onClick(View view) {
           Log.d(TAG,"Card Clicked");
           int position = activity_binding.mRecyclerview.getChildAdapterPosition(view);
           Log.d(TAG,"Clicked Position:"+position);
           String project_web_link = mAdapter.getWebLink(position);
           String project_title = mAdapter.getProjectTitle(position);
           loadWebPage(project_web_link,project_title);
       }
   }

    private void loadWebPage(String project_web_link,String project_title) {
        new FinestWebView.Builder(getApplicationContext()).theme(R.style.KickStarterTheme).titleDefault(project_title)
                .webViewBuiltInZoomControls(true)
                .webViewDisplayZoomControls(true)
                .webViewMixedContentMode(0)
                .showUrl(true)
                .swipeRefreshColorRes(R.color.colorPrimary)
                .statusBarColorRes(R.color.colorPrimaryDark)
                .toolbarColorRes(R.color.colorPrimary)
                .titleColorRes(R.color.finestWhite)
                .urlColorRes(R.color.colorPrimary)
                .iconDefaultColorRes(R.color.finestWhite)
                .progressBarColorRes(R.color.finestWhite)
                .stringResCopiedToClipboard(R.string.copied_to_clipboard)
                .stringResCopiedToClipboard(R.string.copied_to_clipboard)
                .stringResCopiedToClipboard(R.string.copied_to_clipboard)
                .dividerHeight(0)
                .gradientDivider(false)
                .setCustomAnimations(R.anim.activity_open_enter, R.anim.activity_open_exit,
                        R.anim.activity_close_enter, R.anim.activity_close_exit)
                .show(project_web_link);


    }

    private void updateAdapter() {
         List<String> titles = new ArrayList<String>(this.project_map.keySet());
        Map<String,KickStarterResponse> seekbar_filter_map = new HashMap<>();
        for (String title:titles) {
            if (!this.project_map.get(title).getBackers().matches(".*[a-zA-Z]+.*"))
            {
                int backers = Integer.valueOf(this.project_map.get(title).getBackers());
                if (backers<=progress_filter)
                {
                  seekbar_filter_map.put(title,this.project_map.get(title));
                }
            }

        }
        mAdapter.setData(seekbar_filter_map);
    }

    private void showFilter()
    {
        activity_binding.seekbrInclude.parentView.setVisibility(View.VISIBLE);
        activity_binding.toolbar.setVisibility(View.GONE);

    }

    private void hidefilter() {
        activity_binding.seekbrInclude.parentView.setVisibility(View.GONE);
        activity_binding.toolbar.setVisibility(View.VISIBLE);

    }
}
