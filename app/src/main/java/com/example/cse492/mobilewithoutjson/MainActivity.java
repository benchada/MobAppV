package com.example.cse492.mobilewithoutjson;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.widget.ListView;
import android.widget.TextView;

import org.w3c.dom.NodeList;

/**
 * Created by cse492 on 20/05/15.
 */



import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import java.util.ArrayList;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;


public class MainActivity extends ActionBarActivity {

    // All static variables
    static final String URL = "https://raw.githubusercontent.com/benchada/Video_MobileApp/master/videos_list2.xml";

    //static final String URL = "http://api.androidhive.info/music/music.xml";
    // XML node keys
    static final String KEY_SONG = "song"; // parent node
    static final String KEY_ID = "id";
    static final String KEY_TITLE = "title";
    static final String KEY_ARTIST = "artist";
    static final String KEY_DURATION = "duration";
    static final String KEY_THUMB_URL = "thumb_url";
    static final String KEY_VIDEO_URL = "video_url";

    ListView list;
    LazyAdapter adapter;
    TextView textview;
    NodeList nodelist;
    ProgressDialog pDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Locate a TextView in your activity_main.xml layout
        //textview = (TextView) findViewById(R.id.text); >>>>>>>>>>> NOT NEEDED ANYMORE!!
        // Execute DownloadXML AsyncTask
        new DownloadXML().execute(URL);
    }

    // DownloadXML AsyncTask
    private class DownloadXML extends AsyncTask<String, Void, Void> {

        ArrayList<HashMap<String, String>> songsList = new ArrayList<HashMap<String, String>>();
        XMLParser parser = new XMLParser();
        NodeList nl;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Create a progressbar
            pDialog = new ProgressDialog(MainActivity.this);
            // Set progressbar title
            pDialog.setTitle("Android Video Project");
            // Set progressbar message
            pDialog.setMessage("Loading...");
            pDialog.setIndeterminate(false);
            // Show progressbar
            pDialog.show();
        }

        @Override
        protected Void doInBackground(String... Url) {
            try
            {
                String xml = parser.getXmlFromUrl(URL); // getting XML from URL
                Document doc = parser.getDomElement(xml); // getting DOM element

                nl = doc.getElementsByTagName(KEY_SONG);

            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return null;

        }

        @Override
        protected void onPostExecute(Void args) {


            for (int I = 0; I < nl.getLength(); I++) {
                // creating new HashMap
                HashMap<String, String> map = new HashMap<String, String>();
                Element e = (Element) nl.item(I);
                // adding each child node to HashMap key => value
                map.put(KEY_ID, parser.getValue(e, KEY_ID));
                map.put(KEY_TITLE, parser.getValue(e, KEY_TITLE));
                map.put(KEY_ARTIST, parser.getValue(e, KEY_ARTIST));
                map.put(KEY_DURATION, parser.getValue(e, KEY_DURATION));
                map.put(KEY_THUMB_URL, parser.getValue(e, KEY_THUMB_URL));
                map.put(KEY_VIDEO_URL, parser.getValue(e, KEY_VIDEO_URL));

                // adding HashList to ArrayList
                songsList.add(map);
            }

            list=(ListView)findViewById(R.id.list);

            // Getting adapter by passing xml data ArrayList
            adapter=new LazyAdapter(com.example.cse492.mobilewithoutjson.MainActivity.this, songsList);
            list.setAdapter(adapter);
            // Close progressbar
            pDialog.dismiss();
        }
    }

    // getNode function
    private static String getNode(String sTag, Element eElement) {
        NodeList nlList = eElement.getElementsByTagName(sTag).item(0)
                .getChildNodes();
        Node nValue = (Node) nlList.item(0);
        return nValue.getNodeValue();
    }
}
