package com.nobleseries.david.imgurviewer;

import android.os.AsyncTask;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

/**
 * Created by David-Samsung on 7/28/2015.
 */
public class loadSubreddit extends AsyncTask<String,Void,List<String>>{
    String url;
    String urlbase;
    String subreddit;
    List<String> urls = new ArrayList<String>();

    public loadSubreddit(String userInput){
        int currentPage = 0;
        subreddit = userInput;
        //url = "https://imgur.com/r/" + subreddit + "/page/0.xml";
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
        //printURLs(urls);
        return null;
    }

    public List<String> loadXML(){

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
        //printURLs(urls);
      return null;
    }

    private void printURLs(List<String> urls) {
        for(int i = 0; i < urls.size();i++){
            System.out.println(urls.get(i));
        }
    }
}
