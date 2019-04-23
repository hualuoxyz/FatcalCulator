package fatcalculator.slaug.com;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.text.DecimalFormat;


public class MainActivity extends AppCompatActivity {

    private EditText waist;
    private EditText weight;
    private Boolean sexChoice;
    private int age;
    private MediaPlayer correct;
    private MediaPlayer wrong;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Spinner spinner = (Spinner)findViewById(R.id.spinner);
        Spinner agespinner = (Spinner)findViewById(R.id.agespinner);
        Button calculate = (Button)findViewById(R.id.calculate);
        TextView method = (TextView)findViewById(R.id.method);
        TextView standard = (TextView)findViewById(R.id.standard);

        waist = (EditText)findViewById(R.id.waist);
        weight = (EditText)findViewById(R.id.weight);


        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position==0){
                    sexChoice = true;
                }else {
                    sexChoice = false;
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        agespinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0)
                    age = 0;
                else if (position == 1)
                    age = 1;
                else
                    age =2;
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        calculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //关闭软键盘
                InputMethodManager inputMethodManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                if (inputMethodManager!=null){
                    inputMethodManager.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(),0);
                }

                //腰围和体重数据未输入
                if (TextUtils.isEmpty(waist.getText())&&TextUtils.isEmpty(weight.getText())){
                    lackAll();
                }
                //体重数据未输入
                if (!TextUtils.isEmpty(waist.getText())&&TextUtils.isEmpty(weight.getText())){
                    lackWeight();
                }
                //腰围数据为输入
                if (TextUtils.isEmpty(waist.getText())&&!TextUtils.isEmpty(weight.getText())){
                    lackWaist();
                }
                //腰围和体重数据均输入
                if (!TextUtils.isEmpty(waist.getText())&&!TextUtils.isEmpty(weight.getText())){
                    try {
                        float waistValue = Float.parseFloat(waist.getText().toString());
                        float weightValue = Float.parseFloat(weight.getText().toString());
                        if (sexChoice) {
                            float resultValue = (float) (waistValue * 0.74 - weightValue * 0.082 - 44.74) / weightValue * 100;
                            showMan(resultValue);
                         } else {
                            float resultValue = (float) (waistValue * 0.74 - weightValue * 0.082 - 34.89) / weightValue * 100;
                            showWoman(resultValue);
                        }
                    }catch (NumberFormatException e){
                        //数据输入错误
                        incrrectShow();
                    }
                }
            }
        });
        method.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,MethodActivity.class);
                startActivity(intent);
            }
        });
        standard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,StandardActivity.class);
                startActivity(intent);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu,menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.quit:
                MainActivity.this.finish();
                break;
                default:
                    break;
        }
        return true;
    }

    private void lackAll(){
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("提示");
        builder.setIcon(R.drawable.alert);
        builder.setMessage("请输入腰围和体重！");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.create().show();
        playWrong();
    }
    private void lackWaist(){
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("提示");
        builder.setIcon(R.drawable.alert);
        builder.setMessage("请输入腰围！");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.create().show();
        playWrong();
    }
    private void lackWeight(){
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("提示");
        builder.setIcon(R.drawable.alert);
        builder.setMessage("请输入体重！");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.create().show();
        playWrong();
    }
    private void incrrectShow(){
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("警告");
        builder.setIcon(R.drawable.alert);
        builder.setMessage("格式错误，请检查输入的数据！");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.create().show();
        playWrong();
    }
    private void showMan(float resultValue) {
        if (age == 0) {
            if (resultValue <= 0 || resultValue >= 100) {
                outShow();
            } else if (resultValue <= 10) {
                thinShow(getCorrectFloat(resultValue));
            } else if (resultValue <= 21) {
                normalShow(getCorrectFloat(resultValue));
            } else if (resultValue <= 26) {
                overShow(getCorrectFloat(resultValue));
            } else {
                fatShow(getCorrectFloat(resultValue));
            }
        }
        if (age == 1) {
            if (resultValue <= 0 || resultValue >= 100) {
                outShow();
            } else if (resultValue <= 11) {
               thinShow(getCorrectFloat(resultValue));
            } else if (resultValue <= 22) {
                normalShow(getCorrectFloat(resultValue));
            } else if (resultValue <= 27) {
                overShow(getCorrectFloat(resultValue));
            } else {
                fatShow(getCorrectFloat(resultValue));
            }
        }
        if (age == 2) {
            if (resultValue <= 0 || resultValue >= 100) {
                outShow();
            } else if (resultValue <= 13) {
                thinShow(getCorrectFloat(resultValue));
            } else if (resultValue <= 24) {
                normalShow(getCorrectFloat(resultValue));
            } else if (resultValue <= 29) {
                overShow(getCorrectFloat(resultValue));
            } else {
                fatShow(getCorrectFloat(resultValue));
            }
        }
    }
    private void showWoman(float resultValue) {
        if (age == 0) {
            if (resultValue <= 0 || resultValue >= 100) {
                outShow();
            } else if (resultValue <= 20) {
                thinShow(getCorrectFloat(resultValue));
            } else if (resultValue <= 34) {
                normalShow(getCorrectFloat(resultValue));
            } else if (resultValue <= 39) {
               overShow(getCorrectFloat(resultValue));
            } else {
                fatShow(getCorrectFloat(resultValue));
            }
        }
        if (age == 1) {
            if (resultValue <= 0 || resultValue >= 100) {
                outShow();
            } else if (resultValue <= 21) {
                thinShow(getCorrectFloat(resultValue));
            } else if (resultValue <= 35) {
                normalShow(getCorrectFloat(resultValue));
            } else if (resultValue <= 40) {
                overShow(getCorrectFloat(resultValue));
            } else {
                fatShow(getCorrectFloat(resultValue));
            }
        }
        if (age == 2) {
            if (resultValue <= 0 || resultValue >= 100) {
                outShow();
            } else if (resultValue <= 22) {
                thinShow(getCorrectFloat(resultValue));
            } else if (resultValue <= 36) {
                normalShow(getCorrectFloat(resultValue));
            } else if (resultValue <= 41) {
                overShow(getCorrectFloat(resultValue));
            } else {
                fatShow(getCorrectFloat(resultValue));
            }
        }
    }
    private String getCorrectFloat(Float resultValue){
        DecimalFormat decimalFormat = new DecimalFormat("0.0");
        return decimalFormat.format(resultValue);
    }
    private void outShow(){
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("提示");
        builder.setIcon(R.drawable.alert);
        builder.setMessage("误差过大，请重新输入！");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.create().show();
        playWrong();
    }
    private void thinShow(String result){
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("计算结果");
        builder.setIcon(R.drawable.thin);
        builder.setMessage("您的体脂率为："+result+"%"+"（偏瘦）");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.create().show();
        playCorrect();
    }
    private void normalShow(String result) {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("计算结果");
        builder.setIcon(R.drawable.normal);
        builder.setMessage("您的体脂率为：" + result + "%"+"（正常）");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.create().show();
        playCorrect();
    }
    private void overShow(String result) {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("计算结果");
        builder.setIcon(R.drawable.over);
        builder.setMessage("您的体脂率为：" + result + "%"+"（超重）");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.create().show();
        playCorrect();
    }
    private void fatShow(String result) {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("计算结果");
        builder.setIcon(R.drawable.fat);
        builder.setMessage("您的体脂率为：" + result +"%"+ "（肥胖）");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.create().show();
        playCorrect();
    }
    private void playCorrect(){
        correct = MediaPlayer.create(this,R.raw.correct);
        correct.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                correct.release();
            }
        });
        if (correct!=null)
            correct.stop();
        try{
            correct.prepare();
        }catch (IOException e){
            Toast.makeText(MainActivity.this,"音乐文件打开错误！",Toast.LENGTH_LONG).show();
        }
        correct.start();
    }
    private  void playWrong(){
        wrong = MediaPlayer.create(this,R.raw.wrong);
        wrong.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                wrong.release();
            }
        });
        if (wrong!=null){
            wrong.stop();
        }
        try{
            wrong.prepare();
        }catch (IOException e){
            Toast.makeText(MainActivity.this,"wrong打开错误！",Toast.LENGTH_LONG).show();
        }
        wrong.start();
    }
}
