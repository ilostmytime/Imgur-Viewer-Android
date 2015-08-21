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
    ImageView img;
    String subreddit = "";
    List<String> urls = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.imageviewer);
        Bundle extras = getIntent().getExtras();
        subreddit = extras.getString("Input");
        loadSubreddit loadSub = new loadSubreddit(subreddit);
        loadSub.execute();
        img = (ImageView)findViewById(R.id.imageView);
        LoadimageFromURL loadImage = new LoadimageFromURL();
        loadImage.execute();
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
