package com.example.rmtd;

import android.graphics.Color;
import android.graphics.Paint;

public class Enemy {
    int yudori = 5;
    int x,y,hp,speed=5;
    int nextX, nextY;
    Boolean ismove;
    Boolean nani;
    Paint pt;
    Enemy(int x, int y, int hp){
        nani = true;
        this.x = x;
        this.y = y;
        this.nextX =x;
        this.nextY = y;
        this.hp = hp;
        pt = new Paint();
        pt.setColor(Color.rgb(255,0,0));
    }

    void move(int path[][][],int box_size){//int path[][][]
        ismove = false;
        if(!(nextX-yudori<=x && x<=nextX+yudori)){
            ismove = true;
            x += nextX > x ? speed : -speed;
        }
        if(!(nextY-yudori<=y && y<=nextY+yudori)){
            ismove = true;
            y += nextY > y ? speed : -speed;
        }
        if(!ismove){
            getNext(path,box_size);
        }
    }
    void getNext(int path[][][],int box_size){
        int idxX = (int)(x/box_size);
        int idxY = (int)(y/box_size);
        if(idxX>=path[0][0].length || idxY>=path[0].length || idxX<0 || idxY <0){
            return;
        }
        nextY = path[1][idxY][idxX]+(int)(Math.random()*60)-30;
        nextX = path[2][idxY][idxX]+(int)(Math.random()*60)-30;
        if(path[0][idxY][idxX] == -1 && hp >0){
            hp = 0;
            MainActivity.mylife -= 1;
        }
    }
}
