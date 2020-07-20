package com.helpinghands.foryou;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.ActionMenuItemView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
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


public class subject_list extends AppCompatActivity {

    Object codes = null;
    JSONArray papers = null;
    String state;
    int paperCount;
    boolean favBra;
    Handler handler = new Handler();
    int delay = 200;
    Runnable runner = new Runnable() {
        public void run() {
            //Log.d("ss","ss");
            toggleFavoriteBranch();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.subject_list);
        Intent i = getIntent();
        state = i.getStringExtra("state");
        favBra = i.getBooleanExtra("favBra", false);
        readSubCodeFile();
        dialogHandler();
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        toggleSet = settings.getInt("toggled", 0);
        handler.postDelayed(runner, delay);
    }

    int toggleSet = 0;

    public CheckBox dontShowAgain;
    public static final String PREFS_NAME = "appPref";

    void dialogHandler() {
        AlertDialog.Builder adb = new AlertDialog.Builder(this);
        LayoutInflater adbInflater = LayoutInflater.from(this);
        View eulaLayout = adbInflater.inflate(R.layout.checkbox, null);
        dontShowAgain = (CheckBox) eulaLayout.findViewById(R.id.skip);
        adb.setView(eulaLayout);
        adb.setTitle("Attention");
        adb.setMessage("You can save this page as your favourite and skip the previous screens next time you start the For You App. To do so, just tap the 'Add to Favourite' button at the top.");
        adb.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                String checkBoxResult = "NOT checked";
                if (dontShowAgain.isChecked()) checkBoxResult = "checked";
                SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
                SharedPreferences.Editor editor = settings.edit();
                editor.putString("skipMessage", checkBoxResult);
                // Commit the edits!
                editor.commit();
                return;
            }
        });


        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        String skipMessage = settings.getString("skipMessage", "NOT checked");
        if (!skipMessage.equals("checked")) adb.show();
    }

    protected void readSubCodeFile() {
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
        //Log.d("sad", fileText);
        JSONParser parser = new JSONParser();
        try {
            codes = parser.parse(fileText);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        readSubjectFile();
        parseJSON();
    }

    protected void readSubjectFile() {
        BufferedReader reader = null;
        String fileText = null;
        try {
            reader = new BufferedReader(new InputStreamReader(getResources().openRawResource(getResources().getIdentifier("raw/papers_min", "raw", getPackageName()))));
            StringBuilder builder = new StringBuilder();
            String aux = "";
            while ((aux = reader.readLine()) != null) {
                builder.append(aux);
            }
            fileText = builder.substring(1).toString();
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
        //Log.d("sad", fileText);
        JSONParser parser = new JSONParser();
        try {
            papers = (JSONArray) parser.parse(fileText);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        paperCount = papers.size();
    }

    String year, sem, branch;

    void parseJSON() {
        year = state.charAt(0) + "";
        sem = state.charAt(1) + "";
        branch = state.substring(2);
        //Log.d("ass",((JSONObject) ((JSONObject)((JSONObject) obj).get(year)).get(sem)).get(branch).toString() + "s");
        JSONArray subList = (JSONArray) ((JSONObject) ((JSONObject) ((JSONObject) codes).get(year)).get(sem)).get(branch);
        addView(subList);
    }

    protected void addView(JSONArray subList) {
        LinearLayout ll = (LinearLayout) findViewById(R.id.subject_list);
        int count = subList.size();
        String subjectName;
        TextView t;
        String subCode;
        float scale = getResources().getDisplayMetrics().density;
        for (int i = 0; i < count; i++) {
            subCode = (String) subList.get(i);
            JSONObject subject = findSubject(subCode);
            JSONObject subjectObject = (JSONObject) subject.get(subCode);
            subjectName = (String) subjectObject.get("paperTitle");
            long subjectPage = (long) subjectObject.get("page");
            if( subjectObject.get("page_"+branch) != null){
                subjectPage = (long) subjectObject.get("page_"+branch);
            }

            t = (TextView) ll.getChildAt(i + 1);
            t.setTag(subCode+"::"+year+"::"+sem+"::"+branch + "::"+(subjectPage-1));
            LinearLayout.LayoutParams p = (LinearLayout.LayoutParams) t.getLayoutParams();
            p.height = (int) (70 * scale + 0.5f);
            t.setLayoutParams(p);
            t.setText(subjectName);
        }
    }

    JSONObject findSubject(String subCode) {
        //Log.d("sac", "finding " + subCode);
        JSONObject sub = null;
        for (int i = 0; i < paperCount; i++) {
            sub = (JSONObject) papers.get(i);
            if (sub.containsKey(subCode)) return sub;
        }
        return sub;
    }

    public void clickHandler(View view) {
        //Log.d("ass", view.getTag().toString());
        Intent intent = new Intent(this, showSyllabus.class);
        intent.putExtra("state", view.getTag().toString());
        startActivity(intent);
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
                startActivity(Intent.createChooser(s, "Share via"));
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
                toggleFavoriteBranch2();
                return true;
            case android.R.id.home:
                onBackPressed();
                //Log.d("sa","ss");
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressLint("RestrictedApi")
    void toggleFavoriteBranch() {
        if (toggleSet == 1) {
            SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
            String savedState = settings.getString("favBra", null);
            if (state.equals(savedState)) {
                Drawable fav = getResources().getDrawable(R.drawable.ic_action_favorite);
                fav.setColorFilter(Color.YELLOW, PorterDuff.Mode.MULTIPLY);
                ((ActionMenuItemView) findViewById(R.id.favorite_branch)).setIcon(fav);
                //  Log.d("sa", "here");
            } else
                toggleSet = 0;
        } else {
            Drawable fav2 = getResources().getDrawable(R.drawable.ic_action_favorite);
            fav2.setColorFilter(Color.WHITE, PorterDuff.Mode.MULTIPLY);
            ((ActionMenuItemView) findViewById(R.id.favorite_branch)).setIcon(fav2);
            //Log.d("sa","there");
        }
    }

    void toggleFavoriteBranch2() {
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putInt("toggled", (toggleSet == 1) ? (toggleSet = 0) : (toggleSet = 1));
        editor.putString("favBra", state);
        editor.commit();
        handler.postDelayed(runner, delay);
        if (toggleSet == 1)
            Toast.makeText(this, "Marked!", Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(this, "Unmarked!", Toast.LENGTH_SHORT).show();
    }

    public void onBackPressed() {
        if (favBra) {
            Intent intent = new Intent(this, yearlist.class);
            startActivity(intent);
            finish();
        } else
            super.onBackPressed();
    }
}
