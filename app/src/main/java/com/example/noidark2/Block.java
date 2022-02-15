package com.example.noidark2;

import android.graphics.Color;
import android.graphics.Rect;

public class Block {
    private Rect blockRect;
    private int blockColor;
    private float pixelsX;
    private float pixelsY;

    public Block(int xPos, int yPos, int blockWidth, int blockHeight, int screenWidth, int screenHeight, int color){
        blockRect = new Rect();

        pixelsX = screenWidth / (float)64;
        pixelsY = screenHeight / (float)128;

        blockRect.top = yPos;
        blockRect.left = xPos;
        blockRect.right = blockRect.left + blockWidth * (int)pixelsX;
        blockRect.bottom = blockRect.top + blockHeight * (int)pixelsY;

        blockColor = color;
    }

    public void changePos(int xPos, int yPos){
        blockRect.top = yPos;
        blockRect.left = xPos;
    }

    public int returnColor(){
        return blockColor;
    }

    public Rect returnBlock(){
        return blockRect;
    }
}
