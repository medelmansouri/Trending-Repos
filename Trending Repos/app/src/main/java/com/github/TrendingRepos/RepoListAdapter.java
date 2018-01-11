package com.github.TrendingRepos;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;
import java.text.DecimalFormat;
import java.util.ArrayList;


/**
 * Created by BlackJack on 2017-12-31.
 */

/** This Class extends froms BaseAdapter needs to be implemented to make customizable ListView's items**/
public class RepoListAdapter extends BaseAdapter {
    LayoutInflater mInflater;
    private Activity activity;
    private ArrayList data;
    private int currentPosition;


    public RepoListAdapter(Activity activity, ArrayList data) {
        super();
        this.activity = activity;
        this.data = data;
        mInflater = ( LayoutInflater )activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    @Override
    public int getCount() {
        if(data.size()<=0)
            return 1;
        return data.size();
    }

    @Override
    public Object getItem(int position) {

        return data.get(position);
    }

    @Override
    public long getItemId(int position) {

        return position;
    }
    public int getCurrentPosition(){return currentPosition;}

    public ArrayList<Repository> getList() {
        return new ArrayList<Repository>(data);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if(convertView == null) {
            convertView  = mInflater.inflate(R.layout.repo_item, null);
            holder = new ViewHolder();
            holder.Repo_name = (TextView) convertView.findViewById(R.id.repo_name);
            holder.Description = (TextView) convertView.findViewById(R.id.description);
            holder.Owner = (TextView) convertView.findViewById(R.id.owner);
            holder.nNb_stars = (TextView) convertView.findViewById(R.id.nb_stars);
            holder.Avatar = (ImageView) convertView.findViewById(R.id.avatar);
            holder.Star = (ImageView) convertView.findViewById(R.id.star);
            convertView.setTag(holder);
        } else{
            holder = (ViewHolder)convertView.getTag();
        }

        final  Repository repository =  (Repository) getItem(position);
        holder.Repo_name.setText(repository.getName());
        holder.Description.setText(repository.getDescription());
        holder.Owner.setText(repository.getRepoOwner());
        holder.Avatar.setImageResource(R.drawable.ic_account_box_black_24dp);

      /** used picasso to avoid freezing the main thread when downloading images from the server**/
      /** Very usefull tool**/
       Picasso
                .with(activity)
                .load(repository.getPhoto())
                .fit()
                .into(holder.Avatar);
        /** Adjast Number Format**/
            if(repository.getNumberStars()>1000){
                double nbStarDouble= (double) (repository.getNumberStars()/1000.0);
                DecimalFormat decimalFormat = new DecimalFormat("#.#");
                holder.nNb_stars.setText(decimalFormat.format(nbStarDouble)+"K");
            }else{
                holder.nNb_stars.setText(String.valueOf(repository.getNumberStars()));
            }
        currentPosition=position;

        return convertView;
    }

    /* Just to encapsulate the ListItems Views in one class to be easy to manage*/
    public class ViewHolder {
        TextView Repo_name,Description,Owner,nNb_stars;
        ImageView Avatar,Star;
    }



}


