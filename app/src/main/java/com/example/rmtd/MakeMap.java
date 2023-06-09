package com.example.rmtd;

import java.util.ArrayList;
import java.util.Stack;

public class MakeMap {
    public Node[][] makeMap(int mapH, int mapW, int commandX, int commandY) {
        Stack<IdxPoint> node_stack = new Stack<IdxPoint>();
        Node map[][] = new Node[mapH][mapW];
        for (int i = 0; i < mapH; i++) {
            for (int j = 0; j < mapW; j++) {
                map[i][j] = new Node();
            }
        }
        map[commandY][commandX].notUse = false;
        node_stack.push(new IdxPoint(commandX,commandY));

        ArrayList<String> lst = new ArrayList<String>();
        IdxPoint temp;
        String tempStr;
        int pointX, pointY;
        int mapY = map.length, mapX = map[0].length;
        while (!node_stack.isEmpty()) {
            temp = node_stack.peek();
            pointX = temp.x;
            pointY = temp.y;
            lst.clear();

            if(pointY != 0 && map[pointY-1][pointX].notUse){
                lst.add("UP");
            }
            if(pointY != mapY-1 && map[pointY+1][pointX].notUse){
                lst.add("DOWN");
            }
            if(pointX != 0 && map[pointY][pointX-1].notUse){
                lst.add("LEFT");
            }
            if(pointX != mapX-1 && map[pointY][pointX+1].notUse){
                lst.add("RIGHT");
            }
            if (!lst.isEmpty()) {
                switch (lst.get((int)(Math.random()*lst.size()))){
                    case "UP":
                        map[pointY][pointX].wall_up=false;
                        map[pointY-1][pointX].wall_down=false;
                        map[pointY-1][pointX].notUse = false;
                        pointY -= 1;
                        node_stack.push(new IdxPoint(pointX,pointY));
                        break;
                    case "DOWN":
                        map[pointY][pointX].wall_down=false;
                        map[pointY+1][pointX].wall_up=false;
                        map[pointY+1][pointX].notUse = false;
                        pointY += 1;
                        node_stack.push(new IdxPoint(pointX,pointY));
                        break;
                    case "LEFT":
                        map[pointY][pointX].wall_left=false;
                        map[pointY][pointX-1].wall_right=false;
                        map[pointY][pointX-1].notUse = false;
                        pointX -= 1;
                        node_stack.push(new IdxPoint(pointX,pointY));
                        break;
                    case "RIGHT":
                        map[pointY][pointX].wall_right=false;
                        map[pointY][pointX+1].wall_left=false;
                        map[pointY][pointX+1].notUse = false;
                        pointX += 1;
                        node_stack.push(new IdxPoint(pointX,pointY));
                        break;
                }
            } else {
                node_stack.pop();
            }
        }
        map[commandY][commandX].type = TowerType.COMMAND_CENTER;
        map[commandY][commandX].wall_left=false;
        map[commandY][commandX].wall_right=false;
        map[commandY][commandX].wall_up=false;
        map[commandY][commandX].wall_down=false;
        map[commandY-1][commandX].wall_down=false;
        map[commandY+1][commandX].wall_up=false;
        map[commandY][commandX+1].wall_left=false;
        map[commandY][commandX-1].wall_right=false;

        int xw = 1;
        int yw = 1;

        map[(commandY+yw)][(commandX+xw)].wall_left=false;
        map[(commandY+yw)][(commandX+xw)].wall_right=false;
        map[(commandY+yw)][(commandX+xw)].wall_up=false;
        map[(commandY+yw)][(commandX+xw)].wall_down=false;
        map[(commandY+yw)-1][(commandX+xw)].wall_down=false;
        map[(commandY+yw)+1][(commandX+xw)].wall_up=false;
        map[(commandY+yw)][(commandX+xw)+1].wall_left=false;
        map[(commandY+yw)][(commandX+xw)-1].wall_right=false;

        map[(commandY-yw)][(commandX+xw)].wall_left=false;
        map[(commandY-yw)][(commandX+xw)].wall_right=false;
        map[(commandY-yw)][(commandX+xw)].wall_up=false;
        map[(commandY-yw)][(commandX+xw)].wall_down=false;
        map[(commandY-yw)-1][(commandX+xw)].wall_down=false;
        map[(commandY-yw)+1][(commandX+xw)].wall_up=false;
        map[(commandY-yw)][(commandX+xw)+1].wall_left=false;
        map[(commandY-yw)][(commandX+xw)-1].wall_right=false;

        map[(commandY+yw)][(commandX-xw)].wall_left=false;
        map[(commandY+yw)][(commandX-xw)].wall_right=false;
        map[(commandY+yw)][(commandX-xw)].wall_up=false;
        map[(commandY+yw)][(commandX-xw)].wall_down=false;
        map[(commandY+yw)-1][(commandX-xw)].wall_down=false;
        map[(commandY+yw)+1][(commandX-xw)].wall_up=false;
        map[(commandY+yw)][(commandX-xw)+1].wall_left=false;
        map[(commandY+yw)][(commandX-xw)-1].wall_right=false;

        map[(commandY-yw)][(commandX-xw)].wall_left=false;
        map[(commandY-yw)][(commandX-xw)].wall_right=false;
        map[(commandY-yw)][(commandX-xw)].wall_up=false;
        map[(commandY-yw)][(commandX-xw)].wall_down=false;
        map[(commandY-yw)-1][(commandX-xw)].wall_down=false;
        map[(commandY-yw)+1][(commandX-xw)].wall_up=false;
        map[(commandY-yw)][(commandX-xw)+1].wall_left=false;
        map[(commandY-yw)][(commandX-xw)-1].wall_right=false;

        xw += 1;
        yw += 2;

        map[(commandY+yw)][(commandX+xw)].wall_left=false;
        map[(commandY+yw)][(commandX+xw)].wall_right=false;
        map[(commandY+yw)][(commandX+xw)].wall_up=false;
        map[(commandY+yw)][(commandX+xw)].wall_down=false;
        map[(commandY+yw)-1][(commandX+xw)].wall_down=false;
        map[(commandY+yw)+1][(commandX+xw)].wall_up=false;
        map[(commandY+yw)][(commandX+xw)+1].wall_left=false;
        map[(commandY+yw)][(commandX+xw)-1].wall_right=false;

        map[(commandY-yw)][(commandX+xw)].wall_left=false;
        map[(commandY-yw)][(commandX+xw)].wall_right=false;
        map[(commandY-yw)][(commandX+xw)].wall_up=false;
        map[(commandY-yw)][(commandX+xw)].wall_down=false;
        map[(commandY-yw)-1][(commandX+xw)].wall_down=false;
        map[(commandY-yw)+1][(commandX+xw)].wall_up=false;
        map[(commandY-yw)][(commandX+xw)+1].wall_left=false;
        map[(commandY-yw)][(commandX+xw)-1].wall_right=false;

        map[(commandY+yw)][(commandX-xw)].wall_left=false;
        map[(commandY+yw)][(commandX-xw)].wall_right=false;
        map[(commandY+yw)][(commandX-xw)].wall_up=false;
        map[(commandY+yw)][(commandX-xw)].wall_down=false;
        map[(commandY+yw)-1][(commandX-xw)].wall_down=false;
        map[(commandY+yw)+1][(commandX-xw)].wall_up=false;
        map[(commandY+yw)][(commandX-xw)+1].wall_left=false;
        map[(commandY+yw)][(commandX-xw)-1].wall_right=false;

        map[(commandY-yw)][(commandX-xw)].wall_left=false;
        map[(commandY-yw)][(commandX-xw)].wall_right=false;
        map[(commandY-yw)][(commandX-xw)].wall_up=false;
        map[(commandY-yw)][(commandX-xw)].wall_down=false;
        map[(commandY-yw)-1][(commandX-xw)].wall_down=false;
        map[(commandY-yw)+1][(commandX-xw)].wall_up=false;
        map[(commandY-yw)][(commandX-xw)+1].wall_left=false;
        map[(commandY-yw)][(commandX-xw)-1].wall_right=false;

        xw += 1;
        yw += 2;

        map[(commandY+yw)][(commandX+xw)].wall_left=false;
        map[(commandY+yw)][(commandX+xw)].wall_right=false;
        map[(commandY+yw)][(commandX+xw)].wall_up=false;
        map[(commandY+yw)][(commandX+xw)].wall_down=false;
        map[(commandY+yw)-1][(commandX+xw)].wall_down=false;
        map[(commandY+yw)+1][(commandX+xw)].wall_up=false;
        map[(commandY+yw)][(commandX+xw)+1].wall_left=false;
        map[(commandY+yw)][(commandX+xw)-1].wall_right=false;

        map[(commandY-yw)][(commandX+xw)].wall_left=false;
        map[(commandY-yw)][(commandX+xw)].wall_right=false;
        map[(commandY-yw)][(commandX+xw)].wall_up=false;
        map[(commandY-yw)][(commandX+xw)].wall_down=false;
        map[(commandY-yw)-1][(commandX+xw)].wall_down=false;
        map[(commandY-yw)+1][(commandX+xw)].wall_up=false;
        map[(commandY-yw)][(commandX+xw)+1].wall_left=false;
        map[(commandY-yw)][(commandX+xw)-1].wall_right=false;

        map[(commandY+yw)][(commandX-xw)].wall_left=false;
        map[(commandY+yw)][(commandX-xw)].wall_right=false;
        map[(commandY+yw)][(commandX-xw)].wall_up=false;
        map[(commandY+yw)][(commandX-xw)].wall_down=false;
        map[(commandY+yw)-1][(commandX-xw)].wall_down=false;
        map[(commandY+yw)+1][(commandX-xw)].wall_up=false;
        map[(commandY+yw)][(commandX-xw)+1].wall_left=false;
        map[(commandY+yw)][(commandX-xw)-1].wall_right=false;

        map[(commandY-yw)][(commandX-xw)].wall_left=false;
        map[(commandY-yw)][(commandX-xw)].wall_right=false;
        map[(commandY-yw)][(commandX-xw)].wall_up=false;
        map[(commandY-yw)][(commandX-xw)].wall_down=false;
        map[(commandY-yw)-1][(commandX-xw)].wall_down=false;
        map[(commandY-yw)+1][(commandX-xw)].wall_up=false;
        map[(commandY-yw)][(commandX-xw)+1].wall_left=false;
        map[(commandY-yw)][(commandX-xw)-1].wall_right=false;
        return map;
    }
    class IdxPoint{
        int x,y;
        IdxPoint(int x,int y){
            this.x = x;
            this.y = y;
        }
    }
}
