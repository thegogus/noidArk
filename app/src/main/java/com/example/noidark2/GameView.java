package com.example.noidark2;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Build;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.Window;

import java.util.ArrayList;
import java.util.Random;

public class GameView extends SurfaceView implements Runnable {
    Thread gameThread = null;

    SurfaceHolder ourHolder;

    volatile boolean playing;

    Canvas canvas;
    Paint paint;

    Rect rect;

    long fps;

    int x;
    int y;

    int ballX;
    int ballY;

    float pixelsX;
    float pixelsY;

    boolean left;
    boolean up;
    boolean gameOver;

    private long timeThisFrame;

    long startFrameTime;

    int screenWidth;
    int screenHeight;

    Paddle paddle;
    Ball ball;

    boolean once;

    int speed;

    boolean first;
    Rect temp;

    ArrayList<Block> blocks = new ArrayList<Block>();

    public GameView(Context context){
        super(context);

        ourHolder = getHolder();
        paint = new Paint();
        rect = new Rect();
        DisplayMetrics displayMetrics = new DisplayMetrics();

        ((Activity) getContext()).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        screenWidth = displayMetrics.widthPixels;
        screenHeight = displayMetrics.heightPixels;

        left = true;
        up = true;
        gameOver = false;
        once = true;

        x = screenWidth / 2;
        y = 0;
        speed = 8;

        first = true;

        paddle = new Paddle(x, screenWidth, screenHeight, 16, 4);

        pixelsX = screenWidth / (float)64;
        pixelsY = screenHeight / (float)128;

        ballX = paddle.returnPaddle().left + 8 * (int)pixelsX;
        ballY = paddle.returnPaddle().top - 2 * (int)pixelsX;

        ball = new Ball(ballX,  ballY, screenWidth, screenHeight, 2);

        generateNewBlocks();
    }

    @Override
    public void run(){
        while(playing){
             startFrameTime = System.currentTimeMillis();

            update();

            draw();

            timeThisFrame = System.currentTimeMillis() - startFrameTime;
            if(timeThisFrame > 0)
                fps = 1000 / timeThisFrame;
        }
    }

    public void gameLogic(){


        if (ballX > screenWidth - ball.getBallSize()) {
            ballX = screenWidth - ball.getBallSize();
            left = true;
        }
        if (ballY > screenHeight - ball.getBallSize()){
            ballY = screenHeight - ball.getBallSize();
            up = true;
        }



        if (ballX < 0) {
            ballX = 0;
            left = false;
        }
        if (ballY < 0 ){
            ballY = 0;
            up = false;
        }

        temp = paddle.returnPaddle();
        if(ballY > temp.top - ball.getBallSize()) {
            if (ballX > temp.left && ballX < temp.right) {
                //left = !left;
                ballY = temp.top - ball.getBallSize();
                up = true;
            }
        }
        temp = null;

        for(int i = 0; i < 51; i++) {
            if(blocks.get(i) != null) {
                temp = blocks.get(i).returnBlock();
                if (ballY < temp.bottom && ballY > temp.top) {
                    if (ballX > temp.left && ballX < temp.right) {
                        ballY = temp.bottom;
                        up = false;
                        blocks.set(i, null);
                        speed += 1;
                    }
                }
            }

        }
        temp = null;
        if(ballY > screenHeight - 50){
            gameOver = true;
            once = true;
            first = true;
            up = true;
            x = screenWidth / 2;
        }
        if(!first) {
            if (up)
                ballY -= speed;
            else
                ballY += speed;

            if (left)
                ballX -= speed;
            else
                ballX += speed;
        }else{
            temp = paddle.returnPaddle();
            ballX = temp.left + 8 * (int)pixelsX- ball.getBallSize() / 2;
            ballY = temp.top - ball.getBallSize();
        }
        temp = null;


        paddle.changePos(x);
        ball.changePos(ballX, ballY);

    }

    public void update() {
        gameLogic();
    }

    public void draw(){
        if(ourHolder.getSurface().isValid()){
            canvas = ourHolder.lockCanvas();
            canvas.drawColor(Color.WHITE);
            paint.setColor(Color.BLACK);
            paint.setTextSize(45);
            canvas.drawText("FPS: " + fps, 20, 40, paint);
            paint.setColor(Color.RED);
            canvas.drawRect(ball.returnBall(), paint);
            paint.setColor(Color.BLACK);
            canvas.drawRect(paddle.returnPaddle(), paint);
            if(gameOver){
                paint.setColor(Color.BLACK);
                paint.setTextSize(50);
                canvas.drawText("GAME OVER", 0, screenHeight / 2, paint);
                if(once) {
                    blocks.clear();
                    generateNewBlocks();
                    once = false;
                }
                speed = 8;
            }
            for(int i = 0; i < 51; i++){
                if(blocks.get(i) != null) {
                    paint.setColor(blocks.get(i).returnColor());
                    canvas.drawRect(blocks.get(i).returnBlock(), paint);
                }
            }
            ourHolder.unlockCanvasAndPost(canvas);
        }

    }

    public void pause(){
        playing = false;
        try{
            gameThread.join();
        }catch(InterruptedException e){
            Log.e("Error: ", "joining thread");
        }
    }

    public void resume(){
        playing = true;
        gameThread = new Thread(this);
        gameThread.start();
    }

    @Override
    public boolean onTouchEvent(MotionEvent motionEvent) {
        x = (int)motionEvent.getX();
        y = (int)motionEvent.getY();
        first = false;
        gameOver = false;
        return true;
    }

    public void generateNewBlocks(){
        int amX = 16;
        int newXPos = 0;
        int newYPos = amX * (int)pixelsY;
        int r;
        int g;
        int b;
        Random rand = new Random();
        for(int i = 1; i <= 51; i++){
            r = rand.nextInt(256);
            g = rand.nextInt(256);
            b = rand.nextInt(256);
            blocks.add(new Block(newXPos, newYPos,4, 4, screenWidth, screenHeight, Color.argb(255, r, g, b)));
            newXPos += 4 * (int)pixelsX;
            if(i % 17 == 0){
                amX -= 4;
                newYPos = amX * (int)pixelsY;
                newXPos = 0;

            }
        }
    }

}
