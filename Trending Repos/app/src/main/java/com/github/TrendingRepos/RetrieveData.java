package com.github.TrendingRepos;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.params.ConnManagerPNames;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;


/**
 * Created by BlackJack on 2017-12-31.
 */

/**Class That extends From AsyncTask To perform Retrieving Data from github API in the BackGround With Separated Thread*/
/**-----To Avoid freezing The Main Thread and block it with much Calculation------**/
public class RetrieveData extends AsyncTask<Object,Object,Object>
{
    private OnTaskCompleted listener;               //Interface Contains A callBack Method to be called in the Trending Fragment when Data retrieved from The API
    Activity activity;                              //Calling Activity
    ProgressDialog progressDialog;                  //Dialog when waiting for Data
    public static String NO_INTERNET="NO_INTERNET";


    //interface Contains a callBack method to be implemented in Trending Fragment
    public interface OnTaskCompleted{
        void onTaskCompleted(String result);
    }

    public RetrieveData(Activity activity,OnTaskCompleted listener) {
        this.listener = listener;
        this.activity=activity;
    }


    @Override
    protected void onPreExecute() {
        //Lunch the dialog box before executing the API link
        progressDialog = new ProgressDialog(activity);
        progressDialog.setTitle("Please Wait..");
        progressDialog.setMessage("Loading…");
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    @Override
    protected String doInBackground(Object ... objects)
    {

        //If Networking Available execute the httpget Query
        //if there is no feedBack in 5 seconds return NO_INTERNET To the Main Activity
        if(isNetworkAvailable(5000) && areWifiOrDataActivated()){
            return  httpGet(objects[0].toString());
        }else{
            return NO_INTERNET;
        }
    }


    //After The 'doInBackground' finished its work it send its result to this method
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onPostExecute(Object result ) {
        // TODO Auto-generated method stub
        //Hide the progress the dialog
        progressDialog.dismiss();
        // Call the interface method And send it the result to the Trending Fragment
        if (listener != null)
            listener.onTaskCompleted(result.toString());
             }


    private String httpGet(String arg){
        try
        {
            DefaultHttpClient httpclient = new DefaultHttpClient();
            HttpGet method = new HttpGet(arg);
            HttpResponse response = httpclient.execute(method);
            HttpEntity entity = response.getEntity();

            if(entity != null){

                return EntityUtils.toString(entity);
            }
            else{
                return "Nothing";
            }
        }
        catch(Exception e){
            return NO_INTERNET;
        }
    }



    /* Check if Wifi or Celullar Data are Activated*/
    private boolean areWifiOrDataActivated()
    {
        ConnectivityManager conMgr = (ConnectivityManager) activity.getSystemService (Context.CONNECTIVITY_SERVICE);

        if (conMgr.getActiveNetworkInfo() != null
                && conMgr.getActiveNetworkInfo().isAvailable()
                && conMgr.getActiveNetworkInfo().isConnected())
        {
            return true;
        }
        else
            return false;
    }

     /* Ping Github Server to Check if it is connected, In case wifi and data are activated */
    private boolean isNetworkAvailable(int timeOut) {
        InetAddress inetAddress = null;
        try {
            Future<InetAddress> future = Executors.newSingleThreadExecutor().submit(new Callable<InetAddress>() {
                @Override
                public InetAddress call() {
                    try {
                        return InetAddress.getByName("github.com");
                    } catch (UnknownHostException e) {
                        return null;
                    }
                }
            });
            inetAddress = future.get(timeOut, TimeUnit.MILLISECONDS);
            future.cancel(true);
        } catch (InterruptedException e) {
        } catch (ExecutionException e) {
        } catch (TimeoutException e) {
        }
        return inetAddress!=null && !inetAddress.equals("");
    }

    }
