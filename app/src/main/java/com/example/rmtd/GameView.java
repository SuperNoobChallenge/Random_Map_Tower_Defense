package com.example.rmtd;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import java.util.ArrayList;

public class GameView extends SurfaceView implements SurfaceHolder.Callback {
    long coin = 300;
    long score = 0;
    int path[][][];
    int map_size_H, map_size_W;
    int mx, my;
    int temp = Color.rgb(0, 0, 0);

    //맵 사이즈
    int box_size; // 한 칸에 차지하는 크기
    int box_pad; // 한칸에서 벽이 차지하는 크기

    //각종 스레드 객체
    MyThread thread;
    spawnEnemy spawnThread;
    TowerSearch towerSearch;
    YouAreAlreadyDead youAreAlreadyDead;
    EnemyMove enemyMove;

    // 맵 저장 변수
    Node map[][];

    //유닛 저장 변수
    ArrayList<Tower> towers = new ArrayList<Tower>();
    Tower tempTower = null;
    ArrayList<Enemy> enemies = new ArrayList<Enemy>();
    Enemy tempEnemy = null;
    ArrayList<Colony> colonies = new ArrayList<Colony>();
    Colony tempColony = null;

    //임시 페인트인데 그냥 사용할듯;;
    Paint temp_pt, temp_pt2;
    Paint noneTower, commdTower, singleTower;

    //길찾기 클레스 객체
    FindPath fa;
    MakeMap makeMap = new MakeMap();

    //버튼 눌린거 여부 저장하는 변수
    Boolean insertPressed = false;
    Boolean upgradePressed = false;

    Button insertTower = null, upgradeTower = null;

    //이게 왜됨? xml 객체 주소 가져오는 메소드
    void getResource(TextView textCoin, TextView textKill, Button insertTower, Button upgradeTower) {
        this.insertTower = insertTower;
        this.upgradeTower = upgradeTower;
    }

    public GameView(Context context) {
        super(context);
    }

    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        SurfaceHolder holder = getHolder();


        map_size_H = 16;//13
        map_size_W = 9;//7

        int cmdX = (int) (map_size_W / 2);
        int cmdY = (int) (map_size_H / 2);
        map = makeMap.makeMap(map_size_H, map_size_W, cmdX, cmdY);

        //임시 페인트
        temp_pt = new Paint();
        temp_pt.setColor(Color.rgb(0, 0, 255));
        temp_pt2 = new Paint();
        temp_pt2.setColor(Color.rgb(255, 255, 255));

        noneTower = new Paint();
        noneTower.setColor(Color.rgb(255, 255, 0));
        commdTower = new Paint();
        commdTower.setStrokeWidth(10);
        commdTower.setStyle(Paint.Style.STROKE);
        commdTower.setColor(Color.rgb(255, 0, 255));
        singleTower = new Paint();
        singleTower.setColor(Color.rgb(100, 100, 100));

        //지우면 안됨
        holder.addCallback(this);
        thread = new MyThread(holder);
        spawnThread = new spawnEnemy();
        towerSearch = new TowerSearch();
        enemyMove = new EnemyMove();
        youAreAlreadyDead = new YouAreAlreadyDead();
    }

    @Override
    public void surfaceCreated(@NonNull SurfaceHolder surfaceHolder) {
        thread.SetRunning(true);
        spawnThread.SetRunning(true);
        towerSearch.SetRunning(true);
        enemyMove.SetRunning(true);
        youAreAlreadyDead.SetRunning(true);
        thread.start();
        spawnThread.start();
        towerSearch.start();
        enemyMove.start();
        youAreAlreadyDead.start();
    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder surfaceHolder, int i, int i1, int i2) {

    }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder surfaceHolder) {
        boolean retry = true;
        thread.SetRunning(false);
        spawnThread.SetRunning(false);
        towerSearch.SetRunning(false);
        enemyMove.SetRunning(false);
        youAreAlreadyDead.SetRunning(false);
        while (retry) {
            try {
                thread.join();
                retry = false;
            } catch (InterruptedException e) {
            }
        }
    }

    public int[] selectTower(int x, int y) {
        int idx[] = new int[2];
        idx[0] = (x - box_size / 2) / box_size;
        idx[1] = (y - box_size / 2) / box_size;
        if (idx[0] < 0 || idx[0] >= map_size_W - 1 || idx[1] < 0 || idx[1] >= map_size_H - 1) {
            idx[0] = -1;
            idx[1] = -1;
            return idx;
        }
        return idx;
    }

    int tempx = 0, tempy = 0;

    //터치 이벤트 dd
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (insertPressed || upgradePressed) {
            tempx = (int) event.getX();
            tempy = (int) event.getY();
            int idx[] = new int[2];
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    idx = selectTower(tempx, tempy);
                    if (idx[0] != -1 || idx[1] != -1) {
                        if (insertPressed && map[idx[1]][idx[0]].type == TowerType.NONE_TOWER && coin >= 100) {
                            coin -= 100;
                            map[idx[1]][idx[0]].type = TowerType.SINGLE_TOWER;
                            Tower newtempTower = new Tower(idx[0] * box_size + box_size, idx[1] * box_size + box_size, 100);
                            map[idx[1]][idx[0]].tower = newtempTower;
                            towers.add(newtempTower);
//                            insertPressed = false;
                        }
                        if (upgradePressed && map[idx[1]][idx[0]].type == TowerType.SINGLE_TOWER && coin >= 200) {
                            coin-=200;
                            map[idx[1]][idx[0]].tower.upRange(50);
                            map[idx[1]][idx[0]].tower.Damage += 5;
//                            upgradePressed = false;
                        }
                    }
                    //손가락으로 화면을 누르기 시작했을 때 할 일
                    break;
                case MotionEvent.ACTION_MOVE:
                    //터치 후 손가락을 움직일 때
                    break;
                case MotionEvent.ACTION_UP:
                    //손가락을 화면에서 뗄 때 할 일
                    break;
                case MotionEvent.ACTION_CANCEL:
                    // 터치가 취소될 때 할 일
                    break;
                default:
                    break;
            }
        }
        return true;
    }

    //시작할떄 캔버스 크기 가져오는거
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        mx = MeasureSpec.getSize(widthMeasureSpec);
        my = MeasureSpec.getSize(heightMeasureSpec);
        box_size = mx / map_size_W;
        box_pad = 15;
        //지휘부로 가는 길 찾기
        int cmdX = (int) (map_size_W / 2);
        int cmdY = (int) (map_size_H / 2);
        fa = new FindPath();
        fa.marking(map);
        path = fa.findroute(cmdY, cmdX, box_size, box_pad);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    //데미지 입력 스레드
    public class YouAreAlreadyDead extends Thread {
        int count;
        boolean isRunning;
        Enemy target;

        @Override
        public void run() {
            while (isRunning) {
                count = 0;
                while (count < towers.size()) {
                    try {
                        tempTower = towers.get(count);
                        target = tempTower.target;
                        target.hp -= tempTower.Damage;
                        if (target.hp <= 0 && target.nani) {
                            target.nani = false;
                            coin += 1;
                        }
                        count++;
                    } catch (Exception e) {
                        count++;
                    }
                }
                if(MainActivity.mylife > 0){
                    MainActivity.heal();
                }
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        public void SetRunning(Boolean b) {
            isRunning = b;
        }
    }

    //적 이동 담당 스레드
    public class EnemyMove extends Thread {
        int count;
        boolean isRunning;

        @Override
        public void run() {
            while (isRunning) {
                count = 0;
                while (count < enemies.size()) {
                    try {
                        tempEnemy = enemies.get(count);
                        tempEnemy.move(path, box_size);
                        if (tempEnemy.hp <= 0) {
                            enemies.remove(count);
                            tempEnemy = null;
                            count--;
                        }
                        count++;
                    } catch (Exception e) {
                        count++;
                    }
                }
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        public void SetRunning(Boolean b) {
            isRunning = b;
        }
    }

    //타워 검색 스레드
    public class TowerSearch extends Thread {
        int count;
        boolean isRunning;

        @Override
        public void run() {
            while (isRunning) {
                count = 0;
                while (count < towers.size()) {
                    try {
                        tempTower = towers.get(count);
                        tempTower.searchEnemy(enemies);
                        if (tempTower.target != null) {
                            tempTower.pt.setColor(Color.rgb(255, 0, 0));
                        } else {
                            tempTower.pt.setColor(Color.rgb(0, 255, 0));
                        }
                        count++;
                    } catch (Exception e) {
                        count++;
                    }
                }
                try {
                    Thread.sleep(30);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        public void SetRunning(Boolean b) {
            isRunning = b;
        }
    }

    //적 스폰 스레드
    public class spawnEnemy extends Thread {
        int count;
        int ColonyCnt = 0;
        boolean isRunning;
        Colony tempColony;
        int sx = 0, sy = 0;
        int Xpad = 0;
        int Ypad = 0;

        @Override
        public void run() {
            while (isRunning) {
                count = 0;
                while (count < colonies.size()) {
                    tempColony = colonies.get(count);
                    for (int i = 0; i < tempColony.lv; i++) {
                        enemies.add(new Enemy(tempColony.x, tempColony.y, 20));
                        score += 1;
                    }
                    count++;
                }
                if (ColonyCnt <= (int) score / 400) {
                    ColonyCnt++;
                    Xpad = box_size * 8;
                    Ypad = box_pad * 8;
                    sx = mx - (Xpad);
                    sy = my - (Ypad);
                    sx = (int) (Math.random() * sx) - (mx - (Xpad)) / 2;
                    sy = (int) (Math.random() * sy) - (my - (Ypad)) / 2;
                    sx = sx >= 0 ? mx-sx-100 : sx * -1+100;
                    sy = sy >= 0 ? my-sy-100 : sy * -1+100;
                    colonies.add(new Colony(sx, sy));
                }
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        public void SetRunning(Boolean b) {
            isRunning = b;
        }
    }

    //화면 그려주는 스레드
    public class MyThread extends Thread {
        boolean isRunning;
        SurfaceHolder holder;

        public MyThread(SurfaceHolder holder) {
            this.holder = holder;
        }

        @Override
        public void run() {
            Canvas c = null;
            int count;
            while (isRunning) {
                MainActivity.insertData(coin, score, MainActivity.mylife);
                //버튼 누르는거 감지
                insertTower.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        upgradePressed = false;
                        insertPressed = insertPressed ? false : true;
                    }
                });
                upgradeTower.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        insertPressed = false;
                        upgradePressed = upgradePressed ? false : true;
                    }
                });

                try {
                    c = holder.lockCanvas();
                    c.drawColor(temp);
                    //맵 그리는거
                    for (int i = 0; i < map_size_H; i++) {
                        for (int j = 0; j < map_size_W; j++) {
                            c.drawRect(j * box_size, i * box_size, j * box_size + box_size, i * box_size + box_size, temp_pt);
                            c.drawRect(j * box_size + box_pad, i * box_size + box_pad, j * box_size + box_size - box_pad, i * box_size + box_size - box_pad, temp_pt2);
                            if (!map[i][j].wall_up) {
                                c.drawRect(j * box_size + box_pad, i * box_size, j * box_size + box_size - box_pad, i * box_size + box_pad, temp_pt2);
                            }
                            if (!map[i][j].wall_down) {
                                c.drawRect(j * box_size + box_pad, i * box_size + box_size - box_pad, j * box_size + box_size - box_pad, i * box_size + box_size, temp_pt2);
                            }
                            if (!map[i][j].wall_left) {
                                c.drawRect(j * box_size, i * box_size + box_pad, j * box_size + box_pad, i * box_size + box_size - box_pad, temp_pt2);
                            }
                            if (!map[i][j].wall_right) {
                                c.drawRect(j * box_size + box_size - box_pad, i * box_size + box_pad, j * box_size + box_size, i * box_size + box_size - box_pad, temp_pt2);
                            }
                        }
                    }

                    synchronized (this) {

                        //타워 그리는거
                        count = 0;
                        while (count < towers.size()) {
                            tempTower = towers.get(count);
                            c.drawCircle(tempTower.x, tempTower.y, 10, tempTower.pt);
                            if (tempTower.target != null) {
                                c.drawLine(tempTower.x, tempTower.y, tempTower.target.x, tempTower.target.y, tempTower.lapt);
                            }
                            count++;
                        }
                        for (int i = 0; i < map_size_H - 1; i++) {
                            for (int j = 0; j < map_size_W - 1; j++) {
                                switch (map[i][j].type) {
                                    case NONE_TOWER:
                                        if (insertPressed) {
                                            c.drawCircle(j * box_size + box_size, i * box_size + box_size, 10, noneTower);
                                        }
                                        break;
                                    case COMMAND_CENTER:
                                        c.drawCircle(j * box_size + (box_size / 2), i * box_size + (box_size / 2), 50, commdTower);
                                        break;
                                    case SINGLE_TOWER:
                                        if (upgradePressed) {
                                            c.drawCircle(j * box_size + box_size, i * box_size + box_size, 10, noneTower);
                                        }
                                        break;
                                }
                            }
                        }

                        //적 그려주는거
                        count = 0;
                        while (count < enemies.size()) {
                            try {
                                tempEnemy = enemies.get(count);
                                c.drawCircle(tempEnemy.x, tempEnemy.y, 10, tempEnemy.pt);
                                count++;
                            } catch (Exception e) {
                                count++;
                            }
                        }
//                        //타워 그리는거
//                        count = 0;
//                        while (count < towers.size()) {
//                            tempTower = towers.get(count);
//                            try {
//                                if (tempTower.target != null){
//                                    c.drawLine(tempTower.x, tempTower.y,tempTower.target.x,tempTower.target.y,tempTower.pt);
//                                    count++;
//                                }
//                            }catch (Exception e){
//                                count++;
//                            }
//                        }
                    }
                    if(MainActivity.mylife <= 0){
                        c.drawText("게임오버이고 나중에 뭐 추가할듯?",100,100,temp_pt);
                        thread.SetRunning(false);
                        spawnThread.SetRunning(false);
                        towerSearch.SetRunning(false);
                        youAreAlreadyDead.SetRunning(false);
                        enemyMove.SetRunning(false);
                        break;
                    }
                    Thread.sleep(30);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                } finally {
                    if (c != null) {
                        holder.unlockCanvasAndPost(c);
                    }
                }
            }
            while (isRunning) {
            }
        }
        public void SetRunning(Boolean b) {
            isRunning = b;
        }
    }
}
