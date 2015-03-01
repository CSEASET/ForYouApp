package com.helpinghands.foryou;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.internal.view.menu.ActionMenuItemView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;


public class subject_list extends ActionBarActivity {

    Object obj = null;
    String state;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.subject_list);
        Toast.makeText(this, "Set this page as your favorite using the button on the top", Toast.LENGTH_LONG).show();
        Intent i = getIntent();
        state = i.getStringExtra("state");
        readSubjectFile();
    }

    protected void readSubjectFile() {
        BufferedReader reader = null;
        String fileText = null;
        try {
            reader = new BufferedReader(new InputStreamReader(getResources().openRawResource(getResources().getIdentifier("raw/subject_list_min", "raw", getPackageName()))));
            StringBuilder builder = new StringBuilder();
            String aux = "";
            while ((aux = reader.readLine()) != null) {
                builder.append(aux);
            }
            fileText = builder.toString();
        } catch (IOException e) {
            //log the exception
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    //log the exception
                }
            }
        }
        Log.d("sad", fileText);
        JSONParser parser = new JSONParser();
        try {
            obj = parser.parse(fileText);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        parseJSON();
    }

    String year, sem, branch;

    void parseJSON() {
        year = state.charAt(0) + "";
        sem = state.charAt(1) + "";
        branch = state.substring(2);
        //Log.d("ass",((JSONObject) ((JSONObject)((JSONObject) obj).get(year)).get(sem)).get(branch).toString() + "s");
        JSONArray subList = (JSONArray) ((JSONObject) ((JSONObject) ((JSONObject) obj).get(year)).get(sem)).get(branch);
        addView(subList);
    }

    protected void addView(JSONArray subList) {
        LinearLayout ll = (LinearLayout) findViewById(R.id.subject_list);
        int count = subList.size();
        JSONObject subjectName;
        TextView t;
        float scale = getResources().getDisplayMetrics().density;
        for (int i = 0; i < count; i++) {
            subjectName = (JSONObject) subList.get(i);
            Log.d("ass", subjectName.get("n").toString());
            t = (TextView) ll.getChildAt(i + 1);
            t.setTag(i);
            LinearLayout.LayoutParams p = (LinearLayout.LayoutParams) t.getLayoutParams();
            p.height = (int) (70 * scale + 0.5f);
            t.setLayoutParams(p);
            t.setText(subjectName.get("n").toString());
        }
    }

    public void clickHandler(View view) {
        Log.d("ass", view.getTag().toString());
        Intent intent = new Intent(this, showSyllabus.class);
        intent.putExtra("paperID", Integer.parseInt(view.getTag().toString()));
        //startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_subject_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch (id) {
            case R.id.action_settings:
                Intent intent = new Intent(this, developer.class);
                startActivity(intent);
                return true;

            case R.id.share_button:
                Intent s = new Intent(android.content.Intent.ACTION_SEND);
                s.setType("text/plain");
                s.putExtra(Intent.EXTRA_SUBJECT, "Download the GGSIPU B-Tech Syllabus For You App ");
                s.putExtra(Intent.EXTRA_TEXT, "https://play.google.com/store/apps/details?id=com.helpinghands.foryou");
                startActivity(Intent.createChooser(s, "Share Via"));
                return true;
            case R.id.rate_us:
                Uri uri = Uri.parse("market://details?id=com.helpinghands.foryou");
                Uri uri2 = Uri.parse("https://play.google.com/store/apps/details?id=com.helpinghands.foryou");
                Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
                try {
                    startActivity(goToMarket);
                } catch (ActivityNotFoundException e) {
                    startActivity(new Intent(Intent.ACTION_VIEW, uri2));
                }
                return true;
            case R.id.favorite_branch:
                Toast.makeText(this, "Setting fav", Toast.LENGTH_LONG).show();
                toggleFavoriteBranch();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    void toggleFavoriteBranch() {
        Drawable fav = (Drawable) getResources().getDrawable(R.drawable.ic_action_favorite);
        fav.setColorFilter(Color.YELLOW, PorterDuff.Mode.MULTIPLY);
        ((ActionMenuItemView) findViewById(R.id.favorite_branch)).setIcon(fav);
    }
}
