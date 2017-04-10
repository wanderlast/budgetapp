package com.teamfrugal.budgetapp.ui;

public class box {
    public int origin[] = new int[2];
    public int color;
    public int id;
    public int once;
    box(int x, int y, int c, int i) {
        origin[0] = x;
        origin[1] = y;
        color = c;
        id = i;
        once=0;
    }
}
