package com.example.nick.ProgressMonitoringTool;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

public class testSetupController extends AppCompatActivity {
    EditText numItems;
    EditText currentNumItemsNotice;
    CheckBox maintainProp;
    Button updateSettings;
    DatabaseOperations dop;
    Context ctx;
    Cursor propSettings;
    Cursor numItemSettings;

    boolean currentMaintainProp;
    String currentNumItemSetting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.testsetup_screen);
        ctx = this;

        dop = new DatabaseOperations(ctx);

        currentNumItemsNotice = (EditText) this.findViewById(R.id.currentNumItemSetting);

        maintainProp = (CheckBox) this.findViewById(R.id.maintainProp);

        propSettings = dop.getMaintainProp(dop);
        propSettings.moveToFirst();
        if (propSettings.getInt(0) == 1){
            currentMaintainProp = true;
            maintainProp.setChecked(true);
        } else{
            maintainProp.setChecked(false);
        }

        numItemSettings = dop.getNumItemsToTest(dop);
        numItemSettings.moveToFirst();
        currentNumItemSetting = numItemSettings.getString(0);

        currentNumItemsNotice.setText(currentNumItemSetting);


        numItems = (EditText) this.findViewById(R.id.changeNumItems);
        maintainProp = (CheckBox) this.findViewById(R.id.maintainProp);
        updateSettings = (Button) this.findViewById(R.id.updateSettings);


        updateSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (maintainProp.isChecked() && (currentMaintainProp = false)){
                    currentMaintainProp = true;
                    dop.updateMaintainProp(dop, true);
                }

                if (numItems.getText().toString().equals("")){

                } else {
                    dop.updateNumItemsToTest(dop, numItems.getText().toString());
                }

                Toast.makeText(getBaseContext(),
                        "Settings have been updated", Toast.LENGTH_LONG).show();
            }


        });
    }

    public static boolean isInteger(String s) {
        return isInteger(s,10);
    }

    public static boolean isInteger(String s, int radix) {
        if(s.isEmpty()) return false;
        for(int i = 0; i < s.length(); i++) {
            if(i == 0 && s.charAt(i) == '-') {
                if(s.length() == 1) return false;
                else continue;
            }
            if(Character.digit(s.charAt(i),radix) < 0) return false;
        }
        return true;
    }
}
