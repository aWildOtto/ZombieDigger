package com.example.ottot.mineseeker;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Vibrator;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

public class main_Game extends AppCompatActivity {
    private table game_data;
    private int tableCol;
    private int tableRow;
    private int numOfMine;
    private int timePlayed;
    private int bestScr=0;
    private int guessed;
    private int currentScr = 0;
    private Button mine_btns[][];
    private int mine_icon_ID = R.mipmap.zombie;
    private int zombie_icon2 = R.mipmap.zombie2;
    private int zombie_icon3 = R.mipmap.zombie3;
    private int zombie_icon4 = R.mipmap.zombie4;
    private int[] zombie_collection = {mine_icon_ID,zombie_icon2,zombie_icon3,zombie_icon4};
    private int empty_field = R.mipmap.empty_field;
    private int frozen_icon = R.mipmap.frozen_icon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_game);
        Intent passedInData = getIntent();
        int dimC= passedInData.getIntExtra("dimCode",0);
        int mineC = passedInData.getIntExtra("mineCode",0);
        numOfMine = decodeMineC(mineC);
        timePlayed = passedInData.getIntExtra("time",0);
        bestScr = passedInData.getIntExtra("scr",0);
        timePlayed++;
        dimensionGenerate(dimC);
        mineFieldGenerate();
        setResult(RESULT_CANCELED);
        playSound(R.raw.tada);
    }

    private int decodeMineC(int mineC) {//0 = 6, 1 = 10, 2 = 15, 3 = 25, 4 = 35
        switch (mineC){
            case 0:
                return 6;
            case 1:
                return 10;
            case 2:
                return 15;
            case 3:
                return 25;
            case 4:
                return 35;
        }
        return 6;
    }

    private void mineFieldGenerate() {
        TableLayout mineField = (TableLayout)findViewById(R.id.mineField);
        game_data = new table(tableRow,tableCol,numOfMine);
        mine_btns = new Button[tableRow][tableCol];
        updateUI();


        for (int i = 0;i<tableRow;i++){
            TableRow newRow = new TableRow(this);
            newRow.setLayoutParams(new TableLayout.LayoutParams(
                    TableLayout.LayoutParams.MATCH_PARENT,
                    TableLayout.LayoutParams.MATCH_PARENT,
                    1.0f)
            );
            mineField.addView(newRow);
            for (int j = 0 ; j <tableCol;j++){
                final int FINALi = i;
                final int FINALj = j;
                Button newButton = new Button(this);
                newButton.setLayoutParams(new TableRow.LayoutParams(
                        TableRow.LayoutParams.MATCH_PARENT,
                        TableRow.LayoutParams.MATCH_PARENT,
                        1.0f)
                );
                newButton.setPadding(0,0,0,0);
                newButton.setBackgroundResource(empty_field);
                newRow.addView(newButton);
                mine_btns[i][j] = newButton;
                //set i j location for mine;
                newButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mineGuess(FINALi,FINALj);
                        Vibrate_click();
                        //FINALi FINALj is the location of mine
                    }
                });
            }
        }
    }

    private void mineGuess(int row,int col) {

        if (game_data.getRemainScanTimes() > 0) {
            int resultCode = game_data.guessMine(row, col);
            switch (resultCode) {
                case (0):
                    scrDeduct();
                    refreshABtn(row,col);
                    break;
                case (1):
                    Random rn = new Random();
                    mine_btns[row][col].setBackgroundResource(zombie_collection[rn.nextInt(4)]);
                    guessed++;
                    currentScr += 100;
                    if (guessed!=numOfMine){
                        playSound(R.raw.ohyear);
                    }
                    Vibrate_found();
                    updateUI();
                    break;
                case (2):
                    scrDeduct();
                    refreshABtn(row,col);
                    break;
                case (3):
                    break;
                case (4):
                    break;
                case (5):
                    break;
            }
            refreshTable();
            updateUI();

            if (guessed == numOfMine) {
                refreshTableWin();
                refreshNewScr();
                prepareIntent();
                playSound(R.raw.applause);
                android.support.v4.app.FragmentManager manager = getSupportFragmentManager();
                Congrats congrats = new Congrats();
                prepareIntent();
                refreshTableWin();
                congrats.show(manager,"congrats");
            }
        }
        else{
            refreshNewScr();
            updateUI();
            new AlertDialog.Builder(main_Game.this)
                    .setTitle("Out of Scan times")
                    .setMessage(R.string.inAppPurchase)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            game_data.addMoreTime();
                            Toast.makeText(main_Game.this,R.string.freeScan, Toast.LENGTH_SHORT).show();
                            updateUI();
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                            Toast.makeText(main_Game.this, getString(R.string.poorGuy), Toast.LENGTH_SHORT).show();
                            returnToMenu();
                        }
                    })
                    .show();
        }

    }



    private void playSound(int soundToplay){
        final MediaPlayer foundMusic = MediaPlayer.create(main_Game.this,soundToplay);
        foundMusic.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mp.release();
            }
        });
        foundMusic.start();
    }
    private void prepareIntent() {
        Intent gameResult = new Intent();
        gameResult.putExtra("bestScore",bestScr);
        gameResult.putExtra("timesPlay",timePlayed);
        setResult(Activity.RESULT_OK,gameResult);
    }

    private void scrDeduct() {
        if (currentScr>=20){
            currentScr-=20;
        }
        else
            currentScr=0;
    }

    private void updateUI() {
        TextView foundMine = (TextView)findViewById(R.id.found_mine);
        foundMine.setText(guessed + " / " + numOfMine + " zombies dug");

        TextView currScore = (TextView)findViewById(R.id.scoreBoard);
        currScore.setText("Score :" + currentScr);

        TextView bestScore = (TextView)findViewById(R.id.BestScrHis);
        bestScore.setText("Best Score:" + bestScr);

        TextView RemainScanTime = (TextView)findViewById(R.id.scanTimes);
        RemainScanTime.setText("Remaining Scan: " + game_data.getRemainScanTimes());
    }


    private void returnToMenu(){

        prepareIntent();
        finish();
    }

    private void dimensionGenerate(int Dcode) {
        if (Dcode==0){
            tableRow = 4;
            tableCol = 6;
        }
        else if (Dcode == 1){
            tableRow = 5;
            tableCol = 8;
        }
        else if (Dcode == 2){
            tableRow = 6;
            tableCol = 8;
        }
        else if (Dcode == 3){
            tableRow = 4;
            tableCol = 8;
        }
        else if (Dcode == 4){
            tableRow = 5;
            tableCol = 12;
        }
        else if (Dcode == 5){
            tableRow = 6;
            tableCol = 15;
        }
    }

    public void refreshABtn(int row, int col){
        mine_btns[row][col].setText("" + game_data.numOfMines_at(row, col));
    }

    public void refreshTable(){
        for (int i = 0; i < tableRow; i++){
            for (int j = 0; j < tableCol; j++){
                if (game_data.getBlock(i,j) == 3){
                    mine_btns[i][j].setText("" + game_data.numOfMines_at(i, j));
                }
            }
        }
    }
    public void refreshNewScr(){
        if (currentScr > bestScr){
            bestScr = currentScr;
            Toast.makeText(main_Game.this, getString(R.string.newRecord),Toast.LENGTH_SHORT).show();
        }
    }
    public void refreshTableWin() {
        for (int i = 0; i < tableRow; i++) {
            for (int j = 0; j < tableCol; j++) {
                int code = game_data.getBlock(i, j);
                if (code == 0 ){
                    game_data.setBlock(i,j,3);
                }
                if (code == 2){
                    game_data.setBlock(i,j,4);
                }
                if (code == 3) {
                    mine_btns[i][j].setBackgroundResource(frozen_icon);
                }
            }
        }
    }


    public void Vibrate_found(){
        Vibrator mineFound = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        mineFound.vibrate(500);
    }
    public void Vibrate_click(){
        Vibrator mineFound = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        mineFound.vibrate(100);
    }
    public static Intent makeIntent(Context context){
        return new Intent(context, main_Game.class);
    }
}
