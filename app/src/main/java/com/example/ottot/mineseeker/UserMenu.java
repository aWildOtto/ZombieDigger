package com.example.ottot.mineseeker;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.media.MediaPlayer;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import org.w3c.dom.Text;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class UserMenu extends AppCompatActivity {//this is the actual main activity
    //check out table.java
    private int dim_code = 0;//0= 4*6, 1 = 5*8, 2=6*8, 3=4*8, 4=5*12, 5= 6*15
    private static int num_of_dim = 6;
    private int mine_code;//0 = 6, 1 = 10, 2 = 15, 3 = 25, 4 = 35
    private static int TimePlayed;
    private static int[] BestScores = new int[num_of_dim];


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_menu);
        Intent welcome = MainActivity.makeIntent(UserMenu.this);
        startActivity(welcome);
        //initialize table data with default values
        TimePlayed=0;
        BestScores[dim_code]=0;
        GetPref();
        startGame();
        optionMenu();
        helpMenu();
        showInfo();
        //------------------------------------
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 1:
                if (resultCode==RESULT_CANCELED)return;
                BestScores[dim_code] = data.getIntExtra("bestScore",0);
                TimePlayed = data.getIntExtra("timesPlay",TimePlayed);
                savePref();
                showInfo();
                break;
            case 2:
                if (resultCode==RESULT_CANCELED){
                    return;
                }
                if (data.getIntExtra("resetTimeOrNot", 0) == 1) {
                    resetPlaytime();
                }
                if (data.getIntExtra("resetScoreOrNot", 0) == 1) {
                    resetBestScore();
                }
                dim_code = data.getIntExtra("dimension",0);
                mine_code = data.getIntExtra("mineNum",0);
                savePref();
                showInfo();
                break;
        }
    }



    private void startGame() {
        final Button startGame = (Button)findViewById(R.id.Game_start);
        startGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent game_Start = main_Game.makeIntent(UserMenu.this);
                game_Start.putExtra("dimCode",dim_code);
                game_Start.putExtra("mineCode",mine_code);
                game_Start.putExtra("time",TimePlayed);
                game_Start.putExtra("scr",BestScores[dim_code]);
                startActivityForResult(game_Start,1);
            }
        });
    }



    private void optionMenu() {
        final Button options = (Button)findViewById(R.id.options_btn);
        options.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent go_to_opt = options_page.makeIntent(UserMenu.this);
                go_to_opt.putExtra("TimePlay",TimePlayed);
                go_to_opt.putExtra("table_dim",dim_code);
                go_to_opt.putExtra("numberMine",mine_code);

                startActivityForResult(go_to_opt,2);
            }
        });
    }
    private void helpMenu() {
        final Button help = (Button)findViewById(R.id.help_btn);
        help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent go_to_help = help_page.makeIntent(UserMenu.this);
                startActivity(go_to_help);
            }
        });
    }
    private void showInfo() {
        GetPref();
        TextView timePlay = (TextView) findViewById(R.id.TimePlayed);
        timePlay.setText(""+TimePlayed);
        TextView bestScore = (TextView) findViewById(R.id.BestScore);
        Resources res = getResources();
        String[] dimStrArr = res.getStringArray(R.array.dimDD);
        String dimStr = dimStrArr[dim_code];
        TextView bestScoreTittle = (TextView) findViewById(R.id.BestScoreTittle);
        bestScoreTittle.setText("Your Best Score in " + "["+dimStr +"] is :");
        bestScore.setText(""+BestScores[dim_code]);
    }

    private void resetPlaytime(){
        TimePlayed = 0;
    }
    private void resetBestScore() {
        for (int i = 0;i<num_of_dim;i++){
            BestScores[i] = 0;
        }
    }

    public void savePref(){
        SharedPreferences dataTosave = getApplicationContext().getSharedPreferences("MyPref",0);
        SharedPreferences.Editor PrefEditor = dataTosave.edit();
        PrefEditor.putInt("dim_code",dim_code);
        PrefEditor.putInt("mine_code",mine_code);
        PrefEditor.putInt("timePlay",TimePlayed);
        PrefEditor.putInt("dim0",BestScores[0]);
        PrefEditor.putInt("dim1",BestScores[1]);
        PrefEditor.putInt("dim2",BestScores[2]);
        PrefEditor.putInt("dim3",BestScores[3]);
        PrefEditor.putInt("dim4",BestScores[4]);
        PrefEditor.putInt("dim5",BestScores[5]);
        PrefEditor.apply();

    }
    public void GetPref(){
        SharedPreferences dataToGet = getApplicationContext().getSharedPreferences("MyPref",0);
        if (dataToGet==null)return;
        dim_code = dataToGet.getInt("dim_code",0);
        mine_code = dataToGet.getInt("mine_code",0);
        TimePlayed = dataToGet.getInt("timePlay",0);
        BestScores[0] = dataToGet.getInt("dim0",0);
        BestScores[1] = dataToGet.getInt("dim1",0);
        BestScores[2] = dataToGet.getInt("dim2",0);
        BestScores[3] = dataToGet.getInt("dim3",0);
        BestScores[4] = dataToGet.getInt("dim4",0);
        BestScores[5] = dataToGet.getInt("dim5",0);

    }

    public static Intent makeIntent(Context context){
        return new Intent(context, UserMenu.class);
    }




}
