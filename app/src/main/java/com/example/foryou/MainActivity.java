package com.example.foryou;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;


public class MainActivity extends ActionBarActivity {

    Handler handler = new Handler();
    int delay = 2000;
    Runnable runner = new Runnable() {
        public void run() {
            showYearList();
        }
};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ProgressBar spinner;
        spinner = (ProgressBar)findViewById(R.id.progressBar1);
        spinner.setIndeterminate(true);
        //spinner.getIndeterminateDrawable().setColorFilter(0xFFFF0000, android.graphics.PorterDuff.Mode.MULTIPLY); not working
        handler.postDelayed(runner,delay);
    }

    public void clickHandler(View view) {
        showYearList();
    }

    protected void showYearList() {
        Intent intent = new Intent(this, yearlist.class);
        startActivity(intent);
    }

    @Override
    protected void onResume(){
        super.onResume();
        handler.postDelayed(runner,delay);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent(this, developer.class);
            startActivity(intent);
            return true;
        } else if (id == R.id.share_button) {
            Intent s = new Intent(android.content.Intent.ACTION_SEND);
            s.setType("text/plain");
            s.putExtra(Intent.EXTRA_SUBJECT, "Download the IPU B-Tech Syllabus For You App ");
            s.putExtra(Intent.EXTRA_TEXT, "https://www.google.com");
            startActivity(Intent.createChooser(s, "Share via"));
            return true;
        } else if (id == R.id.rate_us) {
            Uri uri = Uri.parse("market://details?id=com.karmanishthdevelopers.colossusv4");
            Uri uri2 = Uri.parse("http://play.google.com/store/apps/details?id=com.karmanishthdevelopers.colossusv4");
            Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
            try {
                startActivity(goToMarket);
            } catch (ActivityNotFoundException e) {
                startActivity(new Intent(Intent.ACTION_VIEW, uri2));
            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
