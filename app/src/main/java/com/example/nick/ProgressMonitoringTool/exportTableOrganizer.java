package com.example.nick.ProgressMonitoringTool;

import android.database.Cursor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Nick on 6/5/2017.
 */
public class exportTableOrganizer {
//    private String[] array;
//    private String[] tempMergArr;
    private int length;
    public List<String> array = new ArrayList<String>();
    private List<String> tempMergArr = new ArrayList<String>();
    private Cursor results;

    public Cursor getResults() {
        return results;
    }

    public void setResults(Cursor results) {
        this.results = results;
    }

    //
//    public static void main(String a[]){
//
//        int[] inputArr = {45,23,11,89,77,98,4,28,65,43};
//        MyMergeSort mms = new MyMergeSort();
//        mms.sort(inputArr);
//        for(int i:inputArr){
//            System.out.print(i);
//            System.out.print(" ");
//        }
//    }
//
    public void sort(List<String> inputArr) {
        this.array = inputArr;
        this.length = inputArr.size();
        this.tempMergArr = new ArrayList<String>(length);
        for (int i = 0; i < length; i++) {
            tempMergArr.add(array.get(i));
        }

        Collections.sort(array, new Comparator<String>() {
            public int compare(String o1, String o2) {
                return extractInt(o1) - extractInt(o2);
            }

            int extractInt(String s) {
                String num = s.replaceAll("\\D", "");
                // return 0 if no digits found
                return num.isEmpty() ? 0 : Integer.parseInt(num);
            }
        });

        doMergeSort(0, length - 1);


    }


    public List<String> getListWords(Cursor results){
        List<String> words = new ArrayList<String>();
        HashMap<String, Integer> numTimes = new HashMap<String, Integer>();
        results.moveToFirst();

        do {
            if (!words.contains(results.getString(2))){
                words.add(results.getString(2));
                numTimes.put(results.getString(2), 1);
            } else if (words.contains(results.getString(2)) && results.getString(6).equals("Practice")){
                int attempts = numTimes.get(results.getString(2));
                words.add(results.getString(2) + attempts);
                numTimes.put(results.getString(2), attempts + 1);
            }
        } while (results.moveToNext());

        return words;
    }


    public HashMap<String, Integer> getWordIndexes(List<String> words){
        HashMap<String, Integer> wordIndexes = new HashMap<String, Integer>();
        int i = 0;
        sort(words);
        //words = sortWordArray(words);
        for (i = 0; i < words.size(); i++){
            wordIndexes.put(words.get(i), i);
        }
        return wordIndexes;
    }
//
    private void doMergeSort(int lowerIndex, int higherIndex) {

        if (lowerIndex < higherIndex) {
            int middle = lowerIndex + (higherIndex - lowerIndex) / 2;
            // Below step sorts the left side of the array
            doMergeSort(lowerIndex, middle);
            // Below step sorts the right side of the array
            doMergeSort(middle + 1, higherIndex);
            // Now merge both sides
            mergeParts(lowerIndex, middle, higherIndex);
        }
    }
//
    private void mergeParts(int lowerIndex, int middle, int higherIndex) {
        //tempMergArr.clear();
        for (int i = lowerIndex; i <= higherIndex; i++) {
            tempMergArr.set(i, array.get(i));
        }
        int i = lowerIndex;
        int j = middle + 1;
        int k = lowerIndex;
        while (i <= middle && j <= higherIndex) {
            if (tempMergArr.get(i).compareTo(tempMergArr.get(j)) < 0){
                    //<= tempMergArr[j]) {
                array.set(k,tempMergArr.get(i));
                i++;
            } else {
                array.set(k, tempMergArr.get(j));
                j++;
            }
            k++;
        }
        while (i <= middle) {
            array.set(k, tempMergArr.get(i));
            k++;
            i++;
        }

    }
}
//- See more at: http://www.java2novice.com/java-sorting-algorithms/merge-sort/#sthash.oSaQ0M7X.dpuf
