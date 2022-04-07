package com.example.assignment;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class Question extends AppCompatActivity {

    private long startTime =0;
    private long finishTime = 0;
    public static SQLiteDatabase db;
    String sql;
    String currentDateTimeString;
    TextView tv_question;
    RadioGroup radioGroup;
    RadioButton b_answer1,b_answer2,b_answer3,b_answer4;
    Button b_next;
    ProgressDialog pd;
    List<QuestionItem> questionItems;
    int true_answer;
    int currentQuestion = 0;
    int correct = 0, wrong = 0;
    Cursor cursor = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.question);
        currentDateTimeString =  new SimpleDateFormat("HH:mm:ss").format(new Date());
        startTime=System.currentTimeMillis();
        tv_question = findViewById(R.id.question);
        b_answer1 = findViewById(R.id.answer1);
        b_answer2 = findViewById(R.id.answer2);
        b_answer3 = findViewById(R.id.answer3);
        b_answer4 = findViewById(R.id.answer4);
        b_next = findViewById(R.id.buttonNext);
        radioGroup = findViewById(R.id.radioGroup);
        String json=getIntent().getStringExtra("json");
        try{
            Context c = getApplicationContext();
            File outFile=c.getDatabasePath("asignmentDB");
            String outFileName=outFile.getPath();
            db = SQLiteDatabase.openDatabase(outFileName,null,SQLiteDatabase.CREATE_IF_NECESSARY);
            //db = SQLiteDatabase.openDatabase("/data/data/ict.mobile/asignmentDB", null, SQLiteDatabase.CREATE_IF_NECESSARY);

            sql = "DROP TABLE IF EXISTS QuestionsLog;";
            db.execSQL(sql);
            sql = "DROP TABLE IF EXISTS TestsLog;";
            db.execSQL(sql);

            sql = "CREATE TABLE QuestionsLog (questionNo int PRIMARY KEY, question text, yourAnswer text, isCorrect bit);";
            db.execSQL(sql);

            sql = "CREATE TABLE TestsLog(testNo int PRIMARY KEY, testDate text,testTime text, duration int, correctCount int);";
            db.execSQL(sql);

            Toast.makeText(Question.this, "SUCESS FUCKING FUL",Toast.LENGTH_LONG).show();
        }catch (Exception e){
            Toast.makeText(Question.this, e.getMessage().toString(),Toast.LENGTH_LONG).show();
        }

        parseJSON(json);
        Collections.shuffle(questionItems);
        String questionString;
//        db.execSQL("INSERT INTO QuestionsLog(questionNo, question, yourAnswer, isCorrect) values"
//                + "(1,'j','j',0);");

//        Toast.makeText(Question.this, questionItems.get(0).getQuestion(),Toast.LENGTH_LONG).show();

        for(int i = 0;i<questionItems.size();i++){
            questionString = questionItems.get(i).getQuestion();

            insertDataToDBQuestion(i,questionString,"",false);
        }
//        insertDataToDBQuestion(0,"","",false);
//       String data = "";
//       cursor = db.rawQuery("select * from QuestionsLog", null);
//        if (cursor.moveToNext()) { /* return true if row exist, false if it doesn't */
//            data = data+ cursor.getString(cursor.getColumnIndex("question"));
//        }
//        Toast.makeText(Question.this, data,Toast.LENGTH_LONG).show();
        setQuestionScreen(currentQuestion);


       b_next.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               int isCorrect = 0;
               if(radioGroup.getCheckedRadioButtonId() == -1){
                   Toast.makeText(Question.this,"Select a answer.",Toast.LENGTH_SHORT).show();
               } else {
                   if(b_answer1.isChecked()){
                       if(b_answer1.getText().equals(questionItems.get(currentQuestion).getAnswer())){
                           correct++;
                           Toast.makeText(Question.this,"Correct!",Toast.LENGTH_SHORT).show();
                           isCorrect = 1;
                           updateDB(b_answer1.getText().toString(),isCorrect);
                       } else {
                           wrong++;
                            Toast.makeText(Question.this,"Wrong! Correct answer: "
                            +questionItems.get(currentQuestion).getAnswer(),Toast.LENGTH_SHORT).show();
                            isCorrect = 0;
                           updateDB(b_answer1.getText().toString(),isCorrect);
                       }
                   }
                   if(b_answer2.isChecked()){
                       if(b_answer2.getText().equals(questionItems.get(currentQuestion).getAnswer())){
                           correct++;
                           Toast.makeText(Question.this,"Correct!",Toast.LENGTH_SHORT).show();
                           isCorrect = 1;
                           updateDB(b_answer2.getText().toString(),isCorrect);
                       } else {
                           wrong++;
                           Toast.makeText(Question.this,"Wrong! Correct answer: "
                                   +questionItems.get(currentQuestion).getAnswer(),Toast.LENGTH_SHORT).show();
                           isCorrect = 0;
                           updateDB(b_answer2.getText().toString(),isCorrect);
                       }
                   }
                   if(b_answer3.isChecked()){
                       if(b_answer3.getText().equals(questionItems.get(currentQuestion).getAnswer())){
                           correct++;
                           Toast.makeText(Question.this,"Correct!",Toast.LENGTH_SHORT).show();
                           isCorrect = 1;
                           updateDB(b_answer3.getText().toString(),isCorrect);
                       } else {
                           wrong++;
                           Toast.makeText(Question.this,"Wrong! Correct answer: "
                                   +questionItems.get(currentQuestion).getAnswer(),Toast.LENGTH_SHORT).show();
                           isCorrect = 0;
                           updateDB(b_answer3.getText().toString(),isCorrect);
                       }
                   }
                   if(b_answer4.isChecked()){
                       if(b_answer4.getText().equals(questionItems.get(currentQuestion).getAnswer())){
                           correct++;
                           Toast.makeText(Question.this,"Correct!",Toast.LENGTH_SHORT).show();
                           isCorrect = 1;
                           updateDB(b_answer4.getText().toString(),isCorrect);
                       } else {
                           wrong++;
                           Toast.makeText(Question.this,"Wrong! Correct answer: "
                                   +questionItems.get(currentQuestion).getAnswer(),Toast.LENGTH_SHORT).show();
                           isCorrect = 0;
                           updateDB(b_answer4.getText().toString(),isCorrect);

                       }
                   }
                   nextQuestion();
               }
            }
        });


    }
    private void updateDB(String yourAnswer,int isCorrect){
        ContentValues pair = new ContentValues();
        pair.put("yourAnswer",yourAnswer);
        pair.put("isCorrect",isCorrect);
        String args[]={String.valueOf(currentQuestion)};
        db.update("QuestionsLog",pair,"questionNo = ?",args);

    }
    private void insertDataToDBQuestion (int questionNo,String question, String yourAnswer,boolean isCorrect){
        int correct = 0;
        if(isCorrect == false){
            correct = 0;
        } else {
            correct = 1;
        }
//        int a = questionNo;
//        String b = question;
//        String c = yourAnswer;
        //sql = "CREATE TABLE QuestionsLog (questionNo int PRIMARY KEY, question text, yourAnswer text, isCorrect bit);";
//                    + "(" + a + "," + b + "," + c + "," + correct + ");");
        db.execSQL("INSERT INTO QuestionsLog(questionNo, question, yourAnswer, isCorrect) values"
                + "("+questionNo+",'"+question+"','"+yourAnswer+"',"+ correct+");");


    }
    private void nextQuestion(){


        currentQuestion++;
        if((!(currentQuestion > questionItems.size()-1)) && currentQuestion!=5){
            setQuestionScreen(currentQuestion);
        } else{
            finish();

        }

    }
    private void setQuestionScreen(int number){




        tv_question.setText(questionItems.get(number).getQuestion());
        Random rand = new Random();
        int n = rand.nextInt(4); //0,1,2,3

        ArrayList<Integer> randNumber = new ArrayList<Integer>();
        for (int i = 1; i <= 10; ++i) randNumber.add(i);
        Collections.shuffle(randNumber);

        String answer = questionItems.get(number).getAnswer();
        int intAnswer = Integer.parseInt(answer);

        if(n==0){
            b_answer1.setText(answer);
            b_answer2.setText(String.valueOf(intAnswer+randNumber.get(0)));
            b_answer3.setText(String.valueOf(intAnswer+randNumber.get(1)));
            b_answer4.setText(String.valueOf(intAnswer+randNumber.get(2)));

        } else if(n==1){
            b_answer2.setText(answer);
            b_answer1.setText(String.valueOf(intAnswer+randNumber.get(0)));
            b_answer3.setText(String.valueOf(intAnswer+randNumber.get(1)));
            b_answer4.setText(String.valueOf(intAnswer+randNumber.get(2)));
        } else if(n==2){
            b_answer3.setText(answer);
            b_answer1.setText(String.valueOf(intAnswer+randNumber.get(0)));
            b_answer2.setText(String.valueOf(intAnswer+randNumber.get(1)));
            b_answer4.setText(String.valueOf(intAnswer+randNumber.get(2)));
        } else if(n==3){
            b_answer4.setText(answer);
            b_answer1.setText(String.valueOf(intAnswer+randNumber.get(0)));
            b_answer2.setText(String.valueOf(intAnswer+randNumber.get(1)));
            b_answer3.setText(String.valueOf(intAnswer+randNumber.get(2)));
        }
    }




    private void parseJSON (String jArray){
        questionItems = new ArrayList<>();

        try{
            JSONObject jsonObj = new JSONObject(jArray);
            JSONArray questions = jsonObj.getJSONArray("questions");
            for(int i = 0; i< questions.length();i++){
                JSONObject question = questions.getJSONObject(i);

                String questionString = question.getString("question");

                int answer = question.getInt("answer");
                String answerString = String.valueOf(answer);

                questionItems.add(new QuestionItem(
                        questionString,
                        answerString
                ));

            }


        } catch (Exception e){
            Toast.makeText(Question.this, e.getMessage().toString(),Toast.LENGTH_LONG).show();
        }


    }
    public void onFinish(View view){ finish();}
    public void finish(){
        finishTime = System.currentTimeMillis();
        int elapsedTime = (int)(finishTime - startTime)/1000;
        Intent intent = new Intent(getApplicationContext(),EndActivity.class);
        intent.putExtra("correct",correct);
        intent.putExtra("wrong",wrong);
        intent.putExtra("playTime",elapsedTime+"");
        intent.putExtra("totalQuestion",currentQuestion+"");
//        db.execSQL("INSERT INTO QuestionsLog(questionNo, question, yourAnswer, isCorrect) values"
//                + "("+questionNo+",'"+question+"','"+yourAnswer+"',"+ correct+");");
//        sql = "CREATE TABLE TestsLog(testNo int PRIMARY KEY, testDate text,testTime text, duration int, correctCount int);";
//        db.execSQL(sql);
        String date = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
        db.execSQL("INSERT INTO TestsLog(testNo,testDate,testTime,duration,correctCount) values "
                + "(" + getProfilesCount() + ",'" + date +"','"+currentDateTimeString+"',"+elapsedTime+","+correct+");");
        setResult(RESULT_OK,intent);
        startActivity(intent);
        super.finish();

    }
    public int getProfilesCount() {
        String countQuery = "SELECT  * FROM " + "TestsLog";

        Cursor cursor = db.rawQuery(countQuery, null);
        int count = cursor.getCount();
        cursor.close();
        return count;
    }

}
