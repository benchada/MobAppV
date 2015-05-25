package com.example.cse492.mobilewithoutjson;

/**
 * Created by cse492 on 15/05/15.
 */

        import java.util.ArrayList;
        import java.util.HashMap;

        import org.w3c.dom.Document;
        import org.w3c.dom.Element;
        import org.w3c.dom.Node;
        import org.w3c.dom.NodeList;

        import android.app.Activity;
        import android.content.Intent;
        import android.os.Bundle;
        import android.view.View;
        import android.widget.AdapterView;
        import android.widget.AdapterView.OnItemClickListener;
        import android.widget.ListView;
        import android.widget.Toast;

/**
 * Created by Manel on 28-Apr-15.
 */
public class CustomizedListView extends Activity {
    // All static variables
    static final String URL = "http://api.androidhive.info/music/music.xml";
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
    NodeList nl;
    NodeList children;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ArrayList<HashMap<String, String>> songsList = new ArrayList<HashMap<String, String>>();

        XMLParser parser = new XMLParser();
        String xml = parser.getXmlFromUrl(URL); // getting XML from URL
        Document doc = parser.getDomElement(xml); // getting DOM element

        nl = doc.getElementsByTagName(KEY_SONG);
        // looping through all song nodes &lt;song&gt;
        for (int I = 0; I < nl.getLength(); I++) {
            // creating new HashMap
            HashMap<String, String> map = new HashMap<String, String>();
            Element e = (Element) nl.item(I);
            // adding each child node to HashMap key =&gt; value
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
        adapter=new LazyAdapter(this, songsList);
        list.setAdapter(adapter);

        // Click event for single list row
        list.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView <?> parent, View view,
                                    int position, long id) {

                String video_url;

                Toast.makeText(getApplicationContext(), "This row "+ nl.item(position).getNodeValue()+ " is clicked!",
                        Toast.LENGTH_LONG).show();

                //nl_children = nl.item(position).;
                Intent myIntent = new Intent( view.getContext(),VideoViewActivity.class);

                Node url = nl.item(position).getLastChild();
                video_url = url.getNodeValue();
                myIntent.putExtra("video_url",video_url);


                Toast.makeText(getApplicationContext(), "Activity of Video Viewing will start now!!!",
                        Toast.LENGTH_LONG).show();
                startActivity(myIntent);


            }
        });
    }
}