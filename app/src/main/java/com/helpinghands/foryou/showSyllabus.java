package com.helpinghands.foryou;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Handler;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnPageErrorListener;
import com.github.barteksc.pdfviewer.util.FitPolicy;

import org.json.simple.*;
import org.json.simple.parser.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;


public class showSyllabus extends Activity implements OnPageErrorListener {

    String state;
    PDFView pdfView;
    final String TAG = "debug";

    @Override
    public void onPageError(int page, Throwable t) {
        Log.e(TAG, "Cannot load page " + page);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wireframe);
        Intent intent = getIntent();
        state = intent.getStringExtra("state");
        String[] parts = state.split("::?");
        String search_term = parts[3];
        String page_number = parts[4];
        if (search_term.contains("ALL")) {
            search_term = "Ist";
        }

        String[] file_name = {
                "1Final Scheme & Syllabus- Ist & 2nd Semester for the academic session 2014-15.pdf",
                "3Final Syllabus-CSE-3rd Semester4,5,6,7,8.pdf",
                "1Final Syllabus-CIVIL-3rd Semester4,5,6,7,8.pdf",
                "2Final Syllabus-Environment-3rd Semester4,5,6,7,8.pdf",
                "4Final Syllabus-IT-3rd Semester4,5,6,7,8.pdf",
                "Final Syllabus-ECE-3rd Semester4,5,6,7,8.pdf"
        };

        String fileCode = null;
        for (int i = 0; i < file_name.length; i++) {
            if (file_name[i].contains(search_term)) {
                fileCode = file_name[i];
                break;
            }
        }

        if (fileCode == null) {
            Toast.makeText(this, "Unable to show syllabus. This is a bug, please report this!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (page_number == null) {
            Toast.makeText(this, "Unable to show syllabus. This is a bug, please report this!", Toast.LENGTH_SHORT).show();
            return;
        }

        pdfView = findViewById(R.id.pdfView);
        pdfView.setBackgroundColor(Color.BLACK);
        pdfView.fromAsset(fileCode)
                .spacing(5)
                .defaultPage(Integer.parseInt(page_number))
                .pageFitPolicy(FitPolicy.WIDTH)
                .load();
    }


}
