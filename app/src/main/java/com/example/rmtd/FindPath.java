package com.example.rmtd;

public class FindPath {
    int[] mgSortRun(int arr[]) {
        int arr2[] = arr.clone();
        int idx_arr[] = new int[arr2.length];
        for (int i = 0; i < idx_arr.length; i++) {
            idx_arr[i] = i;
        }
        mgSort(arr2, idx_arr, 0, arr2.length - 1);
        return idx_arr;
    }

    void mgSort(int arr[], int idx_arr[], int idx1, int idx2) {
        if (idx1 >= idx2) {
            return;
        }

        int mid = (idx1 + idx2) / 2;
        if (idx1 + 1 != idx2) {
            mgSort(arr, idx_arr, idx1, mid);
            mgSort(arr, idx_arr, mid + 1, idx2);
        }

        int temp[] = new int[idx2 - idx1 + 1];
        int idx_temp[] = new int[idx2 - idx1 + 1];
        int temp_idx = 0;
        int idx1_point = idx1;
        int idx2_point = mid + 1;
        while (idx1_point <= mid && idx2_point <= idx2) {
            if (arr[idx1_point] <= arr[idx2_point]) {
                temp[temp_idx] = arr[idx1_point];
                idx_temp[temp_idx] = idx_arr[idx1_point];
                idx1_point++;
                temp_idx++;
            } else {
                temp[temp_idx] = arr[idx2_point];
                idx_temp[temp_idx] = idx_arr[idx2_point];
                idx2_point++;
                temp_idx++;
            }
        }

        while (idx1_point <= mid) {
            temp[temp_idx] = arr[idx1_point];
            idx_temp[temp_idx] = idx_arr[idx1_point];
            idx1_point++;
            temp_idx++;
        }
        while (idx2_point <= idx2) {
            temp[temp_idx] = arr[idx2_point];
            idx_temp[temp_idx] = idx_arr[idx2_point];
            idx2_point++;
            temp_idx++;
        }
        temp_idx = 0;
        while (temp_idx < temp.length) {
            arr[idx1 + temp_idx] = temp[temp_idx];
            idx_arr[idx1 + temp_idx] = idx_temp[temp_idx];
            temp_idx++;
        }
    }


    public int[][][] findroute(int idx_x, int idx_y, int box_size, int box_pad) {
        findroute_run(idx_x, idx_y);
        return returnPath(box_size, box_pad);
    }

    int mark[][];
    int route[][];
    int map_h = 0, map_w = 0;

    public void findroute_run(int idx_x, int idx_y) {

        int start_node = idx_x * map_w + idx_y;
        //메인에서 최단거리 구하기
        route = new int[2][mark.length];
        Boolean isUse[] = new Boolean[mark.length];
        for (int i = 0; i < mark.length; i++) {
            route[0][i] = start_node;
            route[1][i] = mark[start_node][i];
            isUse[i] = false;
        }
        isUse[start_node] = true;
        for (int i = 0; i < mark.length - 1; i++) {
            int idx[] = mgSortRun(route[1]);
            for (int j = 0; j < idx.length; j++) {
                if (!isUse[idx[j]]) {// 다음 뭐 바꾸는지 까지 들어옴
                    for (int k = 0; k < idx.length; k++) {
                        if (route[1][k] > route[1][idx[j]] + mark[idx[j]][k]) {
                            route[1][k] = route[1][idx[j]] + mark[idx[j]][k];
                            route[0][k] = idx[j];
                        }
                    }
                    isUse[idx[j]] = true;
                    break;
                }
            }
        }
        for (int i = 0; i < route.length; i++) {
            route[0][i] = isUse[i] ? route[0][i] : start_node;
        }
    }

    public int[][] marking(Node map[][]) {
        map_h = map.length;
        map_w = map[0].length;
        mark = new int[map.length * map[0].length][map.length * map[0].length];
        for (int i = 0; i < mark.length; i++) {
            for (int j = 0; j < mark[0].length; j++) {
                mark[i][j] = Integer.MAX_VALUE / 10;
            }
        }
        for (int i = 0; i < mark.length; i++) {
            mark[i][i] = 0;
        }
        int idx1 = 0, idx2 = 0;
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[0].length; j++) {
                if (!map[i][j].wall_up) {
                    idx1 = (i) * (map[0].length) + j;
                    idx2 = (i - 1) * (map[0].length) + j;
                    mark[idx1][idx2] = 1;
                    mark[idx2][idx1] = 1;
                }
                if (!map[i][j].wall_down) {
                    idx1 = (i) * (map[0].length) + j;
                    idx2 = (i + 1) * (map[0].length) + j;
                    mark[idx1][idx2] = 1;
                    mark[idx2][idx1] = 1;
                }
                if (!map[i][j].wall_left) {
                    idx1 = (i) * (map[0].length) + j - 1;
                    idx2 = (i) * (map[0].length) + j;
                    mark[idx1][idx2] = 1;
                    mark[idx2][idx1] = 1;
                }
                if (!map[i][j].wall_right) {
                    idx1 = (i) * (map[0].length) + j + 1;
                    idx2 = (i) * (map[0].length) + j;
                    mark[idx1][idx2] = 1;
                    mark[idx2][idx1] = 1;
                }
            }
        }
        return mark;
    }

    //int route[][]; => 가까운거 저장된 배열
    //route[0] => 가야하는 방향
    //route[1] => 목적지까지 남은거리
    //map_h = map.length;
    //map_w = map[0].length;
    private int[][][] returnPath(int box_size, int box_pad) {
        int box_mid = box_size / 2;
        int path[][][] = new int[3][map_h][map_w];
        for(int i=0;i<path[0].length;i++){
            for(int j=0;j<path[0][0].length;j++){
                path[0][i][j]=-1;
                path[1][i][j]=-1;
                path[2][i][j]=-1;
            }
        }
        int pwd = 0;
        //path[0] => 남은 거리
        //path[1] => 이동하는 방향의 x 좌표
        //ptth[2] => 이동하는 방향의 y 좌표
        for (int i = 0; i < map_h; i++) {
            for (int j = 0; j < map_w; j++) {
                pwd = i * map_w + j; //현재 위치? 느낌으로 싸악
                path[0][i][j] = route[1][pwd];
                if (route[0][pwd] < pwd) { // 다음 경로가 좌, 상
                    if (route[0][pwd] == pwd - 1) { // 다음 경로가 좌
                        path[1][i][j] = box_size * i + box_mid;
                        path[2][i][j] = box_size * j - box_mid;
                    } else { // 다음 경로가 상
                        path[1][i][j] = box_size * i - box_mid;
                        path[2][i][j] = box_size * j + box_mid;
                    }
                } else if (route[0][pwd] > pwd) {// 다음 경로가 우 하
                    if (route[0][pwd] == pwd + 1) { // 다음 경로가 우
                        path[1][i][j] = box_size * i + box_mid;
                        path[2][i][j] = box_size * (j + 1) + box_mid;
                    } else { // 다음 경로가 하
                        path[1][i][j] = box_size * (i + 1) + box_mid;
                        path[2][i][j] = box_size * j + box_mid;
                    }
                } else {//목적지인 경우
                    path[0][i][j] = -1;
                    path[1][i][j] = box_size * i + box_mid;
                    path[2][i][j] = box_size * j + box_mid;
                }
            }
        }
        return path;
    }
}
