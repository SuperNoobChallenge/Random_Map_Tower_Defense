package com.example.rmtd;

import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;

import java.util.ArrayList;

public class Tower {
    int range, real_range,count=0,x,y;
    long Damage;
    Enemy target = null, tempEnemy=null;
    Paint pt;
    Paint lapt;
    Tower(){
        real_range = 1000;
        range = real_range*real_range;
        Damage = 5;
        x=100;
        y=100;
        pt = new Paint();
        pt.setColor(Color.rgb(0,255,0));
    }
    Tower(int x, int y,int ran){
        this.x=x;
        this.y=y;
        real_range = ran;
        range = real_range*real_range;
        Damage = 5;
        pt = new Paint();
        lapt = new Paint();
        lapt.setStrokeWidth(7);
        lapt.setColor(Color.RED);
        pt.setColor(Color.rgb(0,255,0));
    }
    public void upRange(int ran){
        real_range += ran;
        range = real_range*real_range;
    }
    public void searchEnemy(ArrayList<Enemy> enemies){
        if(target != null && target.hp <=0){
            target=null;
        }
        if(target == null){
            count=0;
            while (count<enemies.size()){
                try {
                    tempEnemy = enemies.get(count);
                    if(range>(Math.pow(x-tempEnemy.x,2)+Math.pow(y- tempEnemy.y,2))){
                        tempEnemy.pt.setColor(Color.rgb(255,0,0));
                        target = tempEnemy;
                    }
                    count++;
                }catch (Exception e){
                    count++;
//                    Log.e("rmtd-Tower.java","searchEnemy");
                }
            }
        }else{
            if(range<(Math.pow(x-target.x,2)+Math.pow(y- target.y,2))){
                target=null;
            }
        }
    }
}
