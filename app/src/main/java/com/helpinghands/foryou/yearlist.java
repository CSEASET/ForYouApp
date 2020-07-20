package com.helpinghands.foryou;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;


public class yearlist extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.yearlist);
    }

    public void clickHandler(View view) {
        Intent intent = new Intent(this, sem_list.class);
        intent.putExtra("state", view.getTag().toString());
        startActivity(intent);
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
            s.putExtra(Intent.EXTRA_SUBJECT, "Download the GGSIPU B-Tech Syllabus For You App ");
            s.putExtra(Intent.EXTRA_TEXT, "https://play.google.com/store/apps/details?id=com.helpinghands.foryou");
            startActivity(Intent.createChooser(s, "Share via"));
            return true;
        } else if (id == R.id.rate_us) {
            Uri uri = Uri.parse("market://details?id=com.helpinghands.foryou");
            Uri uri2 = Uri.parse("http://play.google.com/store/apps/details?id=com.helpinghands.foryou");
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

    private static final int TIME_INTERVAL = 2000; // # milliseconds, desired time passed between two back presses.
    private long mBackPressed;

    @Override
    public void onBackPressed() {
        if (mBackPressed + TIME_INTERVAL > System.currentTimeMillis()) {
            super.onBackPressed(); // not calling the default onBackPressed
            //android.os.Process.killProcess(android.os.Process.myPid());
            //System.exit(1);
            return;
        } else {
            Toast.makeText(getBaseContext(), "Press back again to leave", Toast.LENGTH_SHORT).show();
        }

        mBackPressed = System.currentTimeMillis();
    }
}
