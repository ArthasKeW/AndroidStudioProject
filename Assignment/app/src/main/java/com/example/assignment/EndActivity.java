package com.example.assignment;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;

public class EndActivity extends AppCompatActivity {
    TextView tv_result;
    public static SQLiteDatabase db;
    Cursor cursor = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_end);
        tv_result = findViewById(R.id.result);

        int correct = getIntent().getIntExtra("correct",0);
        int wrong = getIntent().getIntExtra("wrong",0);
        String playTime = "";
         playTime = getIntent().getExtras().getString("playTime");
         String totalQuestion ="";
         totalQuestion = getIntent().getExtras().getString("totalQuestion");
        int intPlayTime = Integer.parseInt(playTime);
        int intTotalQuestion = Integer.parseInt(totalQuestion);
        double averageTime = Double.valueOf(intPlayTime)/Double.valueOf(intTotalQuestion);


        tv_result.setText("Correct: "+correct+"\nWrong: "+wrong+"\nTotal play time: "+playTime+"\nAverage time on each question: "+averageTime);
        try{
            Context c = getApplicationContext();
            File outFile=c.getDatabasePath("asignmentDB");
            String outFileName=outFile.getPath();
            db = SQLiteDatabase.openDatabase(outFileName,null,SQLiteDatabase.CREATE_IF_NECESSARY);
            //db = SQLiteDatabase.openDatabase("/data/data/ict.mobile/asignmentDB", null, SQLiteDatabase.CREATE_IF_NECESSARY);



            Toast.makeText(EndActivity.this, "SUCESS FUCKING FUL",Toast.LENGTH_LONG).show();
        }catch (Exception e){
            Toast.makeText(EndActivity.this, e.getMessage().toString(),Toast.LENGTH_LONG).show();
        }

               String data = "";
       cursor = db.rawQuery("select * from QuestionsLog", null);
        if (cursor.moveToNext()) { /* return true if row exist, false if it doesn't */
            data = data+ cursor.getInt(cursor.getColumnIndex("isCorrect"));
        }
        Toast.makeText(EndActivity.this, data,Toast.LENGTH_LONG).show();
    }
}