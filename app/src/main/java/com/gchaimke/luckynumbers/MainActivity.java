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

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    static TextView parsedHtmlNode;

    private StringBuilder strBuild = new StringBuilder();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        parsedHtmlNode = findViewById(R.id.column1);

        TextView lastTime = findViewById(R.id.lastCheck);
        lastTime.setText(getLastCheckTime());

        TextView showData = findViewById(R.id.show_data);
        showData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Date d = getLastCheckTime("app_settings");
                String strStartOut = JsoupAsyncTask.getSettings(getBaseContext());
                parsedHtmlNode.setText(getString(R.string.data_from_file)+strStartOut);
                TextView lastTime = findViewById(R.id.lastCheck);
                lastTime.setText(getLastCheckTime());
            }
        });

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getStats();
                TextView lastTime = findViewById(R.id.lastCheck);
                lastTime.setText(getLastCheckTime());
            }
        });

        Button btnGetData = findViewById(R.id.btnGetData);
        btnGetData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JsoupAsyncTask jsoupAsyncTask = new JsoupAsyncTask(getBaseContext());
                jsoupAsyncTask.execute();
                TextView lastTime = findViewById(R.id.lastCheck);
                lastTime.setText(getLastCheckTime());
            }
        });
    }

    private String getLastCheckTime(){
        SimpleDateFormat fmt = new SimpleDateFormat("dd-MM-yy HH:mm");
        File file =new File(this.getFilesDir().getAbsolutePath()+"/"+"app_settings");
        return fmt.format(file.lastModified());
    }

    private void getStats(){
        Statistic st =new Statistic();
        ArrayList<Integer> allNumsStats = st.countNumbers(getBaseContext());
        strBuild.setLength(0);
        for(int i=1;i<=70;i++){
            if(i%10==0){
                strBuild.append("\n");
            }
            strBuild.append(i).append("-").append(allNumsStats.get(i)).append(" ");
        }
        parsedHtmlNode.setText(strBuild);
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
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}
