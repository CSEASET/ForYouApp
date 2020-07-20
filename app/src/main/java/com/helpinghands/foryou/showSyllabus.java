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

    protected JSONObject paper = null;
    Handler handler = new Handler();
    int delay = 0; // app is already heavy, no need for initial delay!
    int delayGap = 100;
    String[] romans = {"I", "II", "III", "IV", "V", "VI", "VII", "VIII", "IX", "X"};
    JSONArray papers = null;
    String state;
    int paperCount;

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
                "3Final Syllabus-CSE-3rd Semester4,5,6,7,8.pdf"
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


        //        paper = (JSONObject) ((JSONArray) findSubject(state).get(state)).get(0);
//        String paperTitle = paper.get("paperTitle").toString();
//        int paperCredits = Integer.parseInt(paper.get("paperCredits").toString());
//        JSONArray paperUnits = (JSONArray) paper.get("paperUnits");
//        //Log.d("fuck", paperUnits.toString());
//        viewHandler("paperCode", paperCode);
//        viewHandler("paperTitle", paperTitle);
//        viewHandler("paperCredits", paperCredits + "");
//
//        for (int i = 0; i < paperUnits.size(); i++) {
//            ;//Log.d("sadf", ((JSONObject) ((JSONObject) paperUnits.get(i)).get("unit")).get("unitDetails").toString());
//            viewHandler1(i, ((JSONObject) ((JSONObject) paperUnits.get(i)).get("unit")).get("unitTitle").toString(), ((JSONObject) ((JSONObject) paperUnits.get(i)).get("unit")).get("unitDetails").toString());
//        }
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

    protected void viewHandler(final String type, final String data) {
        handler.postDelayed(new Runnable() {
            public void run() {
                addView(type, data);
            }
        }, delay);
        delay += delayGap;
    }

    protected void viewHandler1(final int i, final String unitTitle, final String unitDetails) {
        handler.postDelayed(new Runnable() {
            public void run() {
                adpaperUnitsView(i, unitTitle, unitDetails);
            }
        }, delay);
        delay += delayGap;
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

    protected void addView(String type, String data) {
        LinearLayout container = (LinearLayout) findViewById(R.id.container);
        LinearLayout unit = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.list_item_unit, container, false);
        String text = null;
        switch (type) {
            case "paperCode":
                text = "Paper Code: " + data;
                break;
            case "paperTitle":
                text = "\"" + data + "\"";
                break;
            case "paperCredits":
                text = "Paper Credits: " + data;
                break;
            default:
                text = "An error occurred. Please report to the developers." + type + " : " + data;
        }
        ((TextView) unit.findViewById(R.id.unit)).setText(text);
        if (type == "paperTitle" || type == "paperCode") {
            ((TextView) unit.findViewById(R.id.unit)).setTypeface(null, Typeface.BOLD);
            ((TextView) unit.findViewById(R.id.unit)).setGravity(Gravity.CENTER);
        }
        container.addView(unit);
    }

    protected void adpaperUnitsView(int currentPaperUnit, String unitTitle, String unitDetails) {
        LinearLayout container = (LinearLayout) findViewById(R.id.container);
        LinearLayout unitView = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.list_item_paperunit, container, false);
        String unitText = "UNIT-" + romans[currentPaperUnit] + " :: " + unitTitle;
        ((TextView) unitView.findViewById(R.id.unitTitle)).setText(unitText);
        ((TextView) unitView.findViewById(R.id.unitTitle)).setTypeface(null, Typeface.BOLD);
        ((TextView) unitView.findViewById(R.id.unitTitle)).setPaintFlags(((TextView) unitView.findViewById(R.id.unitTitle)).getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        ((TextView) unitView.findViewById(R.id.unitDetails)).setText(unitDetails);

        container.addView(unitView);
    }


}
