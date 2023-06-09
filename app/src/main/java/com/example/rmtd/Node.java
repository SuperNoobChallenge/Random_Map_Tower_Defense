package com.example.rmtd;

public class Node {
    //상,하,좌,우 벽 여부
    //포탑 설치 가능 여부 => 우하단 설치
    Boolean wall_up, wall_down, wall_left, wall_right,notUse;
    TowerType type;
    Tower tower;
    Node(){
        wall_up=true;
        wall_down=true;
        wall_left=true;
        wall_right=true;
        notUse = true;
        tower=null;
        type=TowerType.NONE_TOWER;
    }
}

