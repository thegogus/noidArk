package com.example.noidark2;

import android.graphics.Rect;

import java.lang.reflect.Array;

public class Paddle {

    private int paddleWidth;
    private int paddleHeight;
    private Rect paddleRect;
    private float pixelsX;
    private float pixelsY;

    public Paddle(int xPos,int scr_width, int scr_height, int pdl_width, int pdl_height){
        paddleRect = new Rect();
        paddleWidth = pdl_width;
        paddleHeight = pdl_height;
        pixelsY = scr_height / (float)128;
        pixelsX = scr_width / (float)64;
        paddleRect.top = 116 * (int)pixelsY;
        paddleRect.left = xPos - 8 * (int)pixelsX;
        paddleRect.bottom = paddleRect.top + 4 * (int)pixelsY;
        paddleRect.right = paddleRect.left + 16 * (int)pixelsX;
    }

    public void changePos(int xPos){
        paddleRect.top = 116 * (int)pixelsY;
        paddleRect.left = xPos - 8 * (int)pixelsX;
        paddleRect.bottom = paddleRect.top + 4 * (int)pixelsY;
        paddleRect.right = paddleRect.left + 16 * (int)pixelsX;
    }

    public Rect returnPaddle(){
        return paddleRect;
    }

    public int getPaddleWidth(){
        return -8 * (int)pixelsX;
    }

    public int[] getCoords(){
        int[] paddleCoords = {paddleRect.left, paddleRect.top, paddleRect.right, paddleRect.bottom};
        return paddleCoords;
    }
}
