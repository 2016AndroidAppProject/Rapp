package com.example.nick.ProgressMonitoringTool;

import android.content.Context;

import org.junit.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static org.junit.Assert.*;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
public class exportTableOrganizerTest {

    DatabaseOperations dop;
    Context ctx;


    @Test
    public void sortingNumericallyWorks() throws Exception {
        List<String> input = Arrays.asList("foo2", "foo3", "foo1");
        exportTableOrganizer sorter = new exportTableOrganizer();
        sorter.sort(input);
        List<String> output = sorter.array;

        assertEquals(output.get(0), "foo1");
        assertEquals(output.get(1), "foo2");
        assertEquals(output.get(2), "foo3");

    }


    @Test
    public void gettingWordIndexes() throws Exception {
        List<String> input = Arrays.asList("boo2", "bfo3", "boo3", "oof1");
        exportTableOrganizer sorter = new exportTableOrganizer();
        HashMap<String, Integer> indexes = sorter.getWordIndexes(input);
        int index1 = indexes.get("boo3");
        assertEquals(index1, 2);
       // assertEquals(output.get(2), "boo3");
      //  assertEquals(output.get(3), "oof1");




    }


    @Test
    public void sortingAlphabeticallyWorks() throws Exception {
        List<String> input = Arrays.asList("boo2", "bfo3", "boo3", "oof1");
        exportTableOrganizer sorter = new exportTableOrganizer();
        sorter.sort(input);
        List<String> output = sorter.array;

        assertEquals(output.get(0), "bfo3");
        assertEquals(output.get(1), "boo2");
        assertEquals(output.get(2), "boo3");
        assertEquals(output.get(3), "oof1");

    }
}