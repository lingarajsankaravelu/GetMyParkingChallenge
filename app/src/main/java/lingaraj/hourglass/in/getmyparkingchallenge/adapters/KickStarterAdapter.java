package lingaraj.hourglass.in.getmyparkingchallenge.adapters;

/**
 * Created by lingaraj on 5/7/17.
 */

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.io.File;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

import lingaraj.hourglass.in.getmyparkingchallenge.Helpers;
import lingaraj.hourglass.in.getmyparkingchallenge.R;
import lingaraj.hourglass.in.getmyparkingchallenge.activities.MainActivity;
import lingaraj.hourglass.in.getmyparkingchallenge.cloud.service.Response.KickStarterResponse;



public class KickStarterAdapter extends RecyclerView.Adapter<KickStarterAdapter.ViewHolder> {

    private final MainActivity.CardClick click;
    private String TAG = "KICKSTARTADAP";
    private Context mContext;
    private List<String> project_titles = new ArrayList<String>();
    private Map<String,KickStarterResponse> project_map =new HashMap<>();
    private String regex_alphabet_check = File.separator+".*[a-zA-Z]+.*"+File.separator;


    public KickStarterAdapter(Context context,MainActivity.CardClick click) {
        this.click = click;
        this.mContext = context;
    }

    @Override
    public KickStarterAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View  view = null;
        view = LayoutInflater.from(this.mContext).inflate(R.layout.card_layout_main,parent,false);
        view.setOnClickListener(click);
        ViewHolder vh = new ViewHolder(view);
        return vh;
    }


    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    private void applyFilter() {

    }

    @Override
    public void onBindViewHolder(KickStarterAdapter.ViewHolder holder, int position) {
        String project_title = project_titles.get(position);
        KickStarterResponse  record = this.project_map.get(project_title);
        holder.location.setText(record.getLocation());
        holder.author.setText("By"+" "+record.getBy());
        holder.title.setText(project_title);
        holder.author.setTextColor(ContextCompat.getColor(this.mContext,R.color.colorTextDefault));
        holder.title.setTextColor(Helpers.getClassColor(this.mContext,getRandomInt()));

        if (record.getBackers().matches(".*[a-zA-Z]+.*"))
        {
            holder.backedBy.setText(record.getBackers());
            //In backer value one object contains a string value named cambridge instead of numbers, so checking it before processing for commas here.
        }
        else
        {
            holder.backedBy.setText(String.valueOf(NumberFormat.getNumberInstance(Locale.US).format(Integer.parseInt(record.getBackers()))));

        }
        String amount_pledged = String.valueOf(record.getAmtPledged());
        holder.pledgedAmount.setText("$"+" "+String.valueOf(NumberFormat.getNumberInstance(Locale.US).format(Integer.parseInt(amount_pledged))));
        holder.daysRemaining.setText(String.valueOf(String.valueOf(NumberFormat.getNumberInstance(Locale.US).format(Integer.parseInt(findRemainingDays(record.getDate()))))));
    }

    private String findRemainingDays(String date_input) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        int daysdiff = 0;
        Date date1;
        Date date2;
        try {
             date1 = dateFormat.parse(date_input);
            Calendar c = Calendar.getInstance();
            date2 = dateFormat.parse(dateFormat.format(new Date()));
            long diff =  date2.getTime() - date1.getTime() ;
            long diffDays = diff / (24 * 60 * 60 * 1000)+1;
            daysdiff = (int) diffDays;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Log.d(TAG,"Day Difference:"+daysdiff);
        return String.valueOf(daysdiff);

    }

    @Override
    public int getItemCount() {

        return project_titles.size();
    }

    public void setData(Map<String,KickStarterResponse> kick_starter_map)
    {
        this.project_map = kick_starter_map;
        this.project_titles = new ArrayList<>(kick_starter_map.keySet());
        notifyDataSetChanged();
    }

    public String getWebLink(int position) {
        String project_title = this.project_titles.get(position);
        String web_url = this.project_map.get(project_title).getUrl();
        String proper_url = "https://www.kickstarter.com"+web_url;
        Log.d(TAG,"Chosen Project URl:"+proper_url);
        return proper_url;
    }

    public String getProjectTitle(int position) {
        return this.project_titles.get(position);
    }

    private int getRandomInt() {
        Random r = new Random();
        int Low = 1;
        int High = 16;
        return r.nextInt(High - Low) + Low;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        TextView title;
        TextView author;
        TextView backedBy;
        TextView location;
        TextView pledgedAmount;
        TextView daysRemaining;

        public ViewHolder(View v) {
            super(v);
            title = (TextView) v.findViewById(R.id.title);
            author = (TextView) v.findViewById(R.id.author);
            backedBy = (TextView) v.findViewById(R.id.backed_by);
            location = (TextView) v.findViewById(R.id.location);
            pledgedAmount = (TextView) v.findViewById(R.id.pledged_amount);
            daysRemaining = (TextView) v.findViewById(R.id.days_remaining);
        }

    }


}
