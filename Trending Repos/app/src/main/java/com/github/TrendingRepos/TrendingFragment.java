package com.github.TrendingRepos;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;
import org.json.JSONArray;
import org.json.JSONObject;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by BlackJack on 2018-01-08.
 */


public class TrendingFragment extends Fragment {

    ArrayList<Repository> repositories;      //Array to Store Repositories
    ListView repositoriesList;               //to display repos
    RepoListAdapter RepositoriesAdapter;     //to populate the Listview from repositories Array
    int current_page = 1;                    //counter to increment the page number
    String link;                             //link of the API
    View loadMoreView;                       // to load the next page
    int currentScrollPosition=1;             //to maintain the position of the scroll
    LinearLayout noInternetLayout;           // Layout When no internet Connection
    Button bRetryConnected;
    int Last_nb_days=30;                     //to specify the range of search we want


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        // Inflate the layout for this fragment
         View view =inflater.inflate(R.layout.treding_fragment, container, false);
        repositoriesList = (ListView) view.findViewById(R.id.listview);
        loadMoreView = ((LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                .inflate(R.layout.loadmore, null, false);
        //Layout to show when no connection
        noInternetLayout = (LinearLayout) view.findViewById(R.id.no_internert);
        bRetryConnected = (Button) view.findViewById(R.id.recheck_connection);
        //add 'load more' Button to the bottom of the List
        repositoriesList.addFooterView(loadMoreView);

        //Check if any argument has passed from the main Activity
        // in this case check if the user want to change the filter, ex: select the most stared repos per weeek
        if (getArguments() != null) {
            Last_nb_days = getArguments().getInt("date");
        }
        link = "https://api.github.com/search/repositories?q=created:%3E"+getDateMinus(Last_nb_days)+"&sort=stars&order=desc";
        /*Check if repositories List is already created or contains some data*/
        /* in case Switching between Fragment, it will be no need to load data another Time from the server*/
        /* When switching between Fragments only the views destroyed not member variables and its containts*/
         if (repositories != null && !repositories.isEmpty())
        {
            RepositoriesAdapter = new RepoListAdapter(getActivity(),repositories);
            repositoriesList.setAdapter(RepositoriesAdapter);
        }
         else{
             repositories = new ArrayList<Repository>();
             //execute an asyncTask responsible of loading the API to avoid blocking UI
             new RetrieveData(getActivity(),listener).execute(link);
        }




        //execute the AsyncTask for the next page when Clicking on This button
        loadMoreView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // Starting a new async task
                getActivity().runOnUiThread(new Runnable() {
                    public void run() {
                        //increment the page in the Link
                        current_page += 1;
                        //to save the position of the scroll
                        currentScrollPosition = RepositoriesAdapter.getCount();
                        link+="&page="+current_page;
                        new RetrieveData(getActivity(),listener).execute(link);
                    }
                });
            }
        });


        bRetryConnected.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new RetrieveData(getActivity(),listener).execute(link);
            }
        });


        return view;

    }



    // Transfer the results from JSON to Repository Object
    public ArrayList<Repository> parseJsonToObjectArray(String result) {
        String ReposInJson = "";
        current_page++;
        try {
            ReposInJson = result;
            JSONObject page;
            JSONArray array_repos;
            JSONObject repo;
            JSONObject owner;
                page = new JSONObject(ReposInJson);
                //if the JSON page has a tag message so There is no more repositories to show or another problem occured
                if(page.has("message")){
                    Toast.makeText(getActivity(),"No more Repositories To Show !!",Toast.LENGTH_LONG).show();
                }else{
                    array_repos = new JSONArray(page.getString("items"));
                    for (int i = 0; i < array_repos.length(); i++) {
                        repo = array_repos.getJSONObject(i);
                        owner = repo.getJSONObject("owner");
                        repositories.add(new Repository(repo.getString("name"), repo.getString("description"), repo.getInt("stargazers_count"), owner.getString("login"), owner.getString("avatar_url")));
                    }
                }
        } catch (Exception e) {
            Log.v("Error",e.toString());
        }
        return repositories;
    }


    //This a callBack Method that Return the result of AsyncTask in result and invoke The parser and populate the list
    private RetrieveData.OnTaskCompleted listener = new RetrieveData.OnTaskCompleted() {
        public void onTaskCompleted(String result) {
            //if there internet Connection Show and populate the List wuth repositories
            if(result.equals(RetrieveData.NO_INTERNET)) {
                noInternetLayout.setVisibility(View.VISIBLE);
                repositoriesList.setVisibility(View.GONE);
            }else{
                noInternetLayout.setVisibility(View.GONE);
                repositoriesList.setVisibility(View.VISIBLE);
                repositories = parseJsonToObjectArray(result);
                RepositoriesAdapter = new RepoListAdapter(getActivity(),repositories);
                repositoriesList.setAdapter(RepositoriesAdapter);
                repositoriesList.setSelectionFromTop(currentScrollPosition -1, 0);
            }

        }
    };


    //get the date before x days from now
    public String getDateMinus(int days){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, -days);
        String dateMinusX = format.format(calendar.getTime());
        return dateMinusX;
    }
}