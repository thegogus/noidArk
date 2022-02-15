package com.example.noidark2;

import android.graphics.Rect;

public class Ball {
    private Rect ballRect;
    private float pixelsX;
    private float pixelsY;
    private int ballSize;

    public Ball(int xPos, int yPos, int scr_width, int scr_height, int ball_size){
        ballRect = new Rect();
        pixelsX = scr_width / (float)64;
        ballSize = ball_size;
        ballRect.top = yPos;
        ballRect.left = xPos;
        ballRect.bottom = ballRect.top + ballSize * (int)pixelsX;
        ballRect.right = ballRect.left + ballSize * (int)pixelsX;
    }

    public void changePos(int xPos, int yPos){
        ballRect.top = yPos;
        ballRect.left = xPos;
        ballRect.bottom = ballRect.top + ballSize * (int)pixelsX;
        ballRect.right = ballRect.left + ballSize * (int)pixelsX;
    }

    public int getBallSize(){
        return ballSize * (int)pixelsX;
    }

    public Rect returnBall(){
        return ballRect;
    }


}
