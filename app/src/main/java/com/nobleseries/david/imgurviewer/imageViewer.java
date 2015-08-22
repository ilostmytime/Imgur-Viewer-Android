package com.nobleseries.david.imgurviewer;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

/**
 * Created by David-Samsung on 8/17/2015.
 */
public class imageViewer extends Activity {
    Bitmap image = null;
    int swipeCounter = 0; //Counter used to tell where the user's swipe will be for URLs.get()
    ImageView img;
    TextView title;
    String subreddit = "";
    List<String> urls = new ArrayList<String>();
    List<String> imageTitles = new ArrayList<String>();
    private float x1,x2;
    static final int MIN_DISTANCE = 150;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.imageviewer);
        Bundle extras = getIntent().getExtras();
        subreddit = extras.getString("Input");
        loadSubreddit loadSub = new loadSubreddit(subreddit);
        loadSub.execute();
        img = (ImageView)findViewById(R.id.imageView);
        img.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        x1 = event.getX();
                        break;

                    case MotionEvent.ACTION_UP:
                        x2 = event.getX();
                        float deltaX = x2 - x1;
                        if (Math.abs(deltaX) > MIN_DISTANCE) {
                            Toast.makeText(imageViewer.this, "left2right swipe" + Math.abs(deltaX), Toast.LENGTH_SHORT).show();
                        } else {
                            System.out.println("This happens now");
                        }
                        break;
                }
                return false;
            }
        });
        LoadimageFromURL loadImage = new LoadimageFromURL();
        loadImage.execute();

    }

    private void setTitle() {
        title = (TextView)findViewById(R.id.lblTitle);
        title.setText(imageTitles.get(swipeCounter));
    }

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        LoadimageFromURL loadImage = new LoadimageFromURL();
        loadImage.execute();
        switch(event.getAction())
        {
            case MotionEvent.ACTION_DOWN:
                x1 = event.getX();
                break;

            case MotionEvent.ACTION_UP:
                x2 = event.getX();
                float deltaX = x2 - x1;
/*                if (Math.abs(deltaX) > MIN_DISTANCE)
                {
                    Toast.makeText(this, "Detla: " + Math.abs(deltaX) +" X1: " + x1 + " X2: " + x2, Toast.LENGTH_LONG).show();
                }*/
                if(x1 > x2){//Left Swipe (Next Picture)
                    Toast.makeText(this, "Left Swipe. NEXT Counter: " + swipeCounter, Toast.LENGTH_LONG).show();
                    swipeCounter++;
                    img.invalidate();
                }
                else if(x2 > x1){//Right swipe (Previous Picture)
                    Toast.makeText(this, "Right Swipe. PREVIOUS Counter: " + swipeCounter, Toast.LENGTH_LONG).show();
                    if(swipeCounter != 0){
                        swipeCounter--;
                        img.invalidate();
                    }
                }
                else
                {
                    Toast.makeText(this, "This happens now" + Math.abs(deltaX), Toast.LENGTH_SHORT).show();
                }
                break;
        }
        return super.onTouchEvent(event);
    }
    public class loadSubreddit extends AsyncTask<String,Void,List<String>>{
        String url;
        String urlbase;
        String subreddit;

        public loadSubreddit(String userInput){
            int currentPage = 0;
            subreddit = userInput;
            urlbase = "https://i.imgur.com/";
        }

        @Override
        protected List<String> doInBackground(String... params) {
            try {
                DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
                DocumentBuilder db = dbf.newDocumentBuilder();
                Document doc = db.parse("https://imgur.com/r/" + subreddit +"/page/0.xml");
                System.out.println("Root element: " + doc.getDocumentElement().getNodeName());

                NodeList nList = doc.getElementsByTagName("item");

                System.out.println("--------------------------------------");

                for(int temp = 0; temp<nList.getLength();temp++){
                    Node nNode = nList.item(temp);
                    if(nNode.getNodeType() == Node.ELEMENT_NODE){
                        Element eElement = (Element) nNode;
                        if(!(eElement.getElementsByTagName("hash").toString().equals(".gif"))) {
                            urls.add(urlbase + eElement.getElementsByTagName("hash").item(0).getTextContent() +
                                    eElement.getElementsByTagName("ext").item(0).getTextContent());
                            imageTitles.add(eElement.getElementsByTagName("title").item(0).getTextContent());
                        }
                    }
                }
                return urls;
            } catch (ParserConfigurationException e) {
                e.printStackTrace();
                System.out.println("Error 1");
            } catch (SAXException e) {
                System.out.println("Error 2");
                e.printStackTrace();
            } catch (IOException e) {
                System.out.println("Error 3");
                e.printStackTrace();
            }
            printURLs(urls);
            return null;
        }

        private void printURLs(List<String> urls) {
            for(int i = 0; i < urls.size();i++){
                System.out.println(urls.get(i));
            }
        }
        @Override
        protected void onPostExecute(List<String> result){
            super.onPostExecute(result);
        }
    }
    public class LoadimageFromURL extends AsyncTask<String,Void,Bitmap>{


        @Override
        protected Bitmap doInBackground(String... params) {

            try{
                URL url = new URL(urls.get(swipeCounter));
                InputStream is = url.openConnection().getInputStream();
                Bitmap bitMap = BitmapFactory.decodeStream(is);
                is.close();
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
            setTitle();
        }
    }
}
