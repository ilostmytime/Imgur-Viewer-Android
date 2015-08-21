package com.nobleseries.david.imgurviewer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;



public class MainActivity extends Activity {


    public String subreddit;
    Button btnLoad;
    EditText userInput;
    TextView lblSubreddit;
    public String url;
   // public List<String> imageUrls = new ArrayList<String>();
   // public List<String> imageTitles = new ArrayList<String>();
    public int currentPage = 0;
    public int imageCount = 45;
    public String baseUrl = "https://i.imgur.com/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnLoad = (Button)findViewById(R.id.btnLoad);
        onLoad();
    }

    public void onLoad(){
        userInput = (EditText)findViewById(R.id.txtSubreddit);
        lblSubreddit = (TextView)findViewById(R.id.lblSubreddit);
        btnLoad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                subreddit = (userInput.getText()).toString();
                lblSubreddit.setText(subreddit);
                System.out.println(subreddit);
                Intent displayImages = new Intent(MainActivity.this, imageViewer.class);
                displayImages.putExtra("Input",subreddit);
                startActivity(displayImages);
            }
        });
    }

}
