package com.example.ottot.mineseeker;

import java.util.Random;
/**
 * Created by ottot on 2/10/2017.
 */

public class table {//use tableTest to see what it does
    private int tableCols; //width of the table, x value
    private int tableRows; //height of the table, y value
    private int[][] allBlocks;
    private int numOfMines; //total number of mines, set by user
    private int[] mineOfEachRows;
    private int[] mineOfEachCols;
    public int scanTimes;


    //initialization functions
    public table(int tableRows, int tableCols,int num_Mine) {
        this.tableCols = tableCols;
        this.tableRows = tableRows;
        this.numOfMines = num_Mine;
        allBlocks = new int[tableRows][tableCols];
        scanTimes = (int)(num_Mine + 0.35*tableCols*tableRows);
        generateMines();
        mineOfEachCols = new int[tableCols];
        mineOfEachRows = new int[tableRows];
        searchMines();
    }

    private void generateMines(){
        Random rg = new Random();
        int i = 0;

        while (i<numOfMines){
            int randRow = rg.nextInt(tableRows);
            int randCol = rg.nextInt(tableCols);
            if (allBlocks[randRow][randCol] != 1){
                allBlocks[randRow][randCol]=1;
                i++;
            }
        }
    }


    public void searchMines(){
        for (int i = 0; i< tableRows; i++){
            for (int j = 0; j< tableCols; j++){
                if (allBlocks[i][j]==1){
                    mineOfEachRows[i]++;
                    mineOfEachCols[j]++;
                }
            }
        }
    }
    //--------------------------------

    //User interaction functions
    //called when user taps a block
    public int guessMine(int rowIndex, int colIndex){ //pass in the coordinate of the guessed block, determines if it's a hit or miss
        if ((getBlock(rowIndex, colIndex) == 1) ){//1 means a hit
            mineOfEachRows[rowIndex]--;
            mineOfEachCols[colIndex]--;
            numOfMines --;
            allBlocks[rowIndex][colIndex] = 2;
            scanTimes--;
            return 1;
        }
        else if(getBlock(rowIndex,colIndex)==0){//0 means a miss
            scanTimes--;
            allBlocks[rowIndex][colIndex] = 3;//3 means an empty field already scanned, nothing can wil be done
            return 0;
        }
        else if(getBlock(rowIndex,colIndex)==2){//2 means a mine found already
            scanTimes--;
            allBlocks[rowIndex][colIndex]=4;
            return 2;
        }
        else if (getBlock(rowIndex,colIndex)==3){
            return 3;
        }
        else if (getBlock(rowIndex,colIndex)==4){//4 means a mine already scanned
            return 4;
        }
        return 0;
    }
    //called to get the number on the block, which is the sum of mines in the row and column
    public int numOfMines_at(int rowIndex, int colIndex){
        if (allBlocks[rowIndex][colIndex]==1){
            return mineOfEachCols[colIndex] + mineOfEachRows[rowIndex] - 1;
        }
        else return mineOfEachCols[colIndex] + mineOfEachRows[rowIndex];
    }
    //----------------------------------

    //test use, visualizing tools------------
    public void printTable(){
        for (int i = 0;i<tableRows;i++){
            for (int j = 0;j<tableCols;j++){
                System.out.print(allBlocks[i][j]);
            }
            System.out.print('\n');
        }
    }
    public void printNumArrays() {
        System.out.println("mineOfEachCols: ");
        for (int i=0;i<mineOfEachCols.length;i++){
            System.out.print(mineOfEachCols[i]);
        }

        System.out.println("\nmineOfEachRows: ");
        for (int i=0;i<mineOfEachRows.length;i++){
            System.out.print(mineOfEachRows[i]);
        }
        System.out.print("\n\n");
    }
    //----------------------------------------
    //getters & setters
    public int getBlock(int y, int x){
        return allBlocks[y][x];
    }
    public void setBlock(int y, int x, int val){
        allBlocks[y][x] = val;
        return;
    }
    public int getTableCols() {
        return tableCols;
    }
    public int getTableRows() {
        return tableRows;
    }
    public int getRemainScanTimes(){
        return scanTimes;
    }
    public void addMoreTime(){
        scanTimes = 10;
        return;
    }


    public void setTableCols(int tableCols) {
        this.tableCols = tableCols;
    }
    public void setTableRows(int tableRows) {
        this.tableRows = tableRows;
    }

    public int getNumOfMines() {
        return numOfMines;
    }

    //-----------------------------------------
}

