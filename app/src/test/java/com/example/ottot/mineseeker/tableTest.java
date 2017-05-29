package com.example.ottot.mineseeker;

import org.junit.Test;

import static junit.framework.Assert.assertEquals;

/**
 * Created by ottot on 2/18/2017.
 */
public class tableTest {

    @Test
    public void searchMines() throws Exception {//since there is randomness in the implementation, this function is better to examine by eyes
        table test = new table(5,10,8);
        test.printTable();
        test.printNumArrays();
    }

    @Test
    public void guessMine() throws Exception {
        table test = new table(5,10,8);
        test.printTable();
        test.printNumArrays();
        for (int i = 0;i<test.getTableRows();i++){
            for (int j = 0; j<test.getTableCols();j++){
                assertEquals(test.getBlock(i,j), test.guessMine(i,j));
            }
        }
        assertEquals(0,test.getNumOfMines());

    }

    @Test
    public void numOfMines() throws Exception {
        table test = new table(5,10,8);
        test.printTable();
        System.out.print("number of mines at (0,1) is: " + test.numOfMines_at(0,1));
    }

    @Test
    public void getTableCols() throws Exception {
        table test = new table(5,10,8);
        assertEquals(10, test.getTableCols());
    }

    @Test
    public void getTableRows() throws Exception {
        table test = new table(5,10,8);
        assertEquals(5,test.getTableRows());
    }

    @Test
    public void setTableCols() throws Exception {
        table test = new table(5,10,8);
        test.setTableCols(6);
        assertEquals(6,test.getTableCols());
    }

    @Test
    public void setTableRows() throws Exception {
        table test = new table(5,10,8);
        test.setTableRows(6);
        assertEquals(6,test.getTableRows());
    }

}