package com.example.rmtd;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    GameView gameView;
    static TextView textCoin, textkill, textLife;
    static Button insTower, upTower;
    static double mylife = 10.0;
    //    mainThread trd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        gameView = findViewById(R.id.gameView);
        textCoin = findViewById(R.id.coinText);
        textkill = findViewById(R.id.killText);
        textLife = findViewById(R.id.lifeText);
        insTower = findViewById(R.id.insertTower);
        upTower = findViewById(R.id.upTower);
        gameView.getResource(textCoin, textkill, insTower, upTower);
    }
    public static void heal(){
        if(mylife < 10){
            mylife=mylife+0.1;
        }else {
            mylife = 10;
        }
    }
    public static void insertData(long coin, long score, double life) {
        textCoin.setText("Coin : "+coin);
        textkill.setText("Score : "+score);
        textLife.setText(String.format("Life %.1f", life));
    }
}