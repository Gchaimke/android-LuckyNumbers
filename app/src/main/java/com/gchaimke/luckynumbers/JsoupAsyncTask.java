package com.gchaimke.luckynumbers;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.view.Menu;
import android.view.MenuItem;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

public class JsoupAsyncTask extends AsyncTask<Void, Void, Void> {
    private Context _context;
    private ArrayList<String> _mArrNumbers;
    private StringBuilder sb = new StringBuilder();
    private Document htmlDocument;
    private String htmlPageUrl = "https://www.pais.co.il/777/showMoreResults.aspx?fromIndex=1&amount=20";
    static String settingsFile="app_settings";

    public JsoupAsyncTask(Context context){
        this._context = context;
        _mArrNumbers = new ArrayList<>();
    }
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Void doInBackground(Void... params) {
        try {
            _mArrNumbers.clear();
            //sb.append("Getting Data from site...\n");
            htmlDocument = Jsoup.connect(htmlPageUrl).get();
            Elements allNumbers = htmlDocument.select("ol[class=cat_data_info archive _777]");
            int mElementsSize = allNumbers.size();
            for(int i=0;i<mElementsSize;i++){
                _mArrNumbers.add(allNumbers.eq(i).text());
            }
            for(int x=0;x<_mArrNumbers.size();x++){
                sb.append(_mArrNumbers.get(x)).append("\n");
            }
            writeToFile(sb.toString(),_context);//sb.toString()
        } catch (IOException e) {
            sb.append("Error getting data :\n").append(e.toString());
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void result) {
        sb.setLength(0);
        int rNum=randomWithRange(11,15);
        String numsFromSet=getSettings(_context);
        MainActivity.parsedHtmlNode.setText(sb.append("Get data from server\n").append(numsFromSet));
    }

    static void writeToFile(String data,Context context) {
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput(settingsFile, Context.MODE_PRIVATE));
            outputStreamWriter.write(data);
            outputStreamWriter.close();
        }
        catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }

    static String getSettings(Context context){
        String ret="from settings";
        try{
            InputStream inputStream = context.openFileInput(settingsFile);
            if(inputStream!=null){
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader =new BufferedReader(inputStreamReader);
                String receiveString;
                StringBuilder stringBuilder = new StringBuilder();

                while((receiveString=bufferedReader.readLine())!=null){
                    stringBuilder.append(receiveString).append("\n");
                }
                inputStream.close();
                ret=stringBuilder.toString();
            }
        }catch(FileNotFoundException e){
            Log.e("log Activity","File not found: "+ e.toString());
        }catch (IOException e){
            Log.e("log Activity","Can not read file: "+ e.toString());
        }
        return ret;
    }


    private int randomWithRange(int min,int max){
        int range=max-min+1;
        return (int)(Math.random()*range)+min;
    }
}