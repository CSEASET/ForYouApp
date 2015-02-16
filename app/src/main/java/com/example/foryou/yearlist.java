package com.example.foryou;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;


public class yearlist extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.yearlist);
    }

    public void clickHandler(View view) {
        if (Integer.parseInt(view.getTag().toString()) == 1) {
            Intent intent = new Intent(this, subject_list.class);
            startActivity(intent);
        } else
            Toast.makeText(this, "This part is coming soon!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_yearlist, menu);
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
