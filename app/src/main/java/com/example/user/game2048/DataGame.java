package com.example.user.game2048;

/**
 * Created by USER on 01/02/2018.
 */

public class DataGame {
    private int diemSo;
    private int color;
    private boolean isNew;


    public DataGame(int diemSo, int color, boolean isNew) {
        this.diemSo = diemSo;
        this.color = color;
        this.isNew = isNew;
    }

    public DataGame(DataGame dataGame) {
        this.diemSo = dataGame.getDiemSo();
        this.color = dataGame.getColor();
        this.isNew = dataGame.isNew();
    }

    public boolean isNew() {
        return isNew;
    }

    public void setNew(boolean aNew) {
        isNew = aNew;
    }

    public int getDiemSo() {
        return diemSo;
    }

    public void setDiemSo(int diemSo) {
        this.diemSo = diemSo;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }
}
