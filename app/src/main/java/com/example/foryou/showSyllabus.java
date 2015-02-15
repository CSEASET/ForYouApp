package com.example.foryou;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.simple.*;
import org.json.simple.parser.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;


public class showSyllabus extends Activity {

    protected JSONObject paper = null;
    Handler handler = new Handler();
    int delay = 500;
    int delayGap = 100;
    String[] romans = {"I", "II", "III", "IV", "V", "VI", "VII", "VIII", "IX", "X"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wireframe);
        Intent intent = getIntent();
        int paperID = intent.getIntExtra("paperID", 0);
        readSyllabusFile(paperID);
        Toast.makeText(this, "Loading... " + paper.get("paperTitle").toString(), Toast.LENGTH_SHORT).show();
        String paperCode = paper.get("paperCode").toString();
        String paperTitle = paper.get("paperTitle").toString();
        int paperCredits = Integer.parseInt(paper.get("paperCredits").toString());
        JSONArray paperUnits = (JSONArray) paper.get("paperUnits");
        ;//Log.d("fuck", paperUnits.toString());
        viewHandler("paperCode", paperCode);
        viewHandler("paperTitle", paperTitle);
        viewHandler("paperCredits", paperCredits + "");

        for (int i = 0; i < paperUnits.size(); i++) {
            ;//Log.d("sadf", ((JSONObject) ((JSONObject) paperUnits.get(i)).get("unit")).get("unitDetails").toString());
            viewHandler1(i, ((JSONObject) ((JSONObject) paperUnits.get(i)).get("unit")).get("unitTitle").toString(), ((JSONObject) ((JSONObject) paperUnits.get(i)).get("unit")).get("unitDetails").toString());
        }
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

    protected void readSyllabusFile(int paperID) {
        BufferedReader reader = null;
        String fileText = null;
        try {
            reader = new BufferedReader(new InputStreamReader(getResources().openRawResource(getResources().getIdentifier("raw/datamin", "raw", getPackageName()))));
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
        ;//Log.d("sad", "sad ass");
        ;//Log.d("sad", fileText);
        fileText = fileText.substring(1);
        ;//Log.d("sad", fileText);
        JSONParser parser = new JSONParser();
        Object obj = null;
        try {
            obj = parser.parse(fileText);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        ;//Log.d("sad", "fap");
        ;//Log.d("sad", "fap" + paperID);
        paper = (JSONObject) ((JSONObject) ((JSONArray) ((JSONObject) obj).get("syllabus")).get(paperID)).get("paper");
        ;//Log.d("sad", "fap");
        ;//Log.d("sad", paper.get("paperTitle").toString());
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
