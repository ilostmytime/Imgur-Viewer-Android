package com.nobleseries.david.imgurviewer;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by David-Samsung on 8/17/2015.
 */
public class imageViewer extends Activity {
    Bitmap image = null;
    ImageView img;
    String subreddit = "";
    List<String> urls = new ArrayList<String>();

    public imageViewer(String userSubreddit){
        subreddit = userSubreddit;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.imageviewer);
       loadSubmittedSubreddit();
        img = (ImageView)findViewById(R.id.imageView);
        LoadimageFromURL loadImage = new LoadimageFromURL();
       loadImage.execute();
    }

    private void loadSubmittedSubreddit() {
        loadSubreddit loadSub = new loadSubreddit();
        urls = loadSub.loadXML(subreddit);
    }

    public class LoadimageFromURL extends AsyncTask<String,Void,Bitmap>{

        @Override
        protected Bitmap doInBackground(String... params) {

            try{
                URL url = new URL(urls.get(0));
                InputStream is = url.openConnection().getInputStream();
                Bitmap bitMap = BitmapFactory.decodeStream(is);
                return bitMap;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Bitmap result){
            super.onPostExecute(result);
            img.setImageBitmap(result);

        }
    }
}
