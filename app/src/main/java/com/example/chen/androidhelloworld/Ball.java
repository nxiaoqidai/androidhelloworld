package com.example.chen.androidhelloworld;

import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import java.io.IOException;

/**
 * Created by chenxixiang on 15/9/24.
 */
public class Ball {


    private float ballRadius = 40;

    private WorldView worldView;
    private Bitmap bmp;
    public float x;
    public float y;
    private float xSpeed;
    private float ySpeed;
    private float totalSpeed = 24;
    private int worldWidth;
    private int worldHeight;
    private String playerName;

    float textBaseY;
    Paint paintCircle=new Paint();
    Paint paintName=new Paint();

    public Ball(WorldView worldView, Bitmap bitmap, int worldWidth, int worldHeight) {
        this.bmp = bitmap;
        this.worldHeight = worldHeight;
        this.worldWidth = worldWidth;
        this.worldView = worldView;

        setX(this.worldWidth / 2);
        setY(this.worldHeight / 2);
        setxSpeed(0);
        setySpeed(0);
        updatePosition(x, y);

        //wait for inputting player name.
        playerName= worldView.getUsername();

        paintCircle.setAntiAlias(true);
        paintCircle.setColor(Color.YELLOW);

        paintName.setAntiAlias(true);
        paintName.setColor(Color.BLACK);
        paintName.setTextSize(30);
        paintName.setTextAlign(Paint.Align.CENTER);
        Paint.FontMetrics fontMetrics = paintName.getFontMetrics();
        float fontHeight = fontMetrics.bottom - fontMetrics.top;
        textBaseY = worldView.getScreenHeight()/2 +fontHeight/4;        //there are some small problems in the calculation
    }

    public void resetCoordinate(float worldWidth, float worldHeight, float x, float y, float xSpeed, float ySpeed){
        this.x=(x/worldWidth)*this.worldWidth;
        this.y=(y/worldHeight)*this.worldHeight;
        this.xSpeed=xSpeed;
        this.ySpeed=ySpeed*(-1);
    }

    public void moveBall(){
        x=x+xSpeed;
        y=y+ySpeed;
    }

    public void updatePhysics(){
        if((x>worldWidth-ballRadius)&&(xSpeed>0))
            xSpeed=xSpeed/(-1);
        if((y>worldHeight-ballRadius)&&(ySpeed>0))
            ySpeed=ySpeed/(-1);
        if((x<ballRadius)&&(xSpeed<0))
            xSpeed=xSpeed/(-1);
        if((y<ballRadius)&&(ySpeed<0))
            ySpeed=ySpeed/(-1);

    }

    public void onDraw(Canvas canvas){



        updatePhysics();
        if(worldView.onScreen){
            moveBall();
            updatePosition(x, y);
            canvas.drawCircle(worldView.getScreenWidth() / 2, worldView.getScreenHeight() / 2, ballRadius, paintCircle);
            canvas.drawText(playerName,worldView.getScreenWidth()/2,textBaseY ,paintName);
        }
    }

    public void sendBluetoothMessage(){
        try{
            StringBuffer sb = new StringBuffer();
            sb.append("ShowOnScreen");
            sb.append(",");
            sb.append(String.valueOf(worldWidth));
            sb.append(",");
            sb.append(String.valueOf(worldHeight));
            sb.append(",");
            sb.append(String.valueOf(x));
            sb.append(",");
            sb.append(String.valueOf(y));
            sb.append(",");
            sb.append(String.valueOf(xSpeed));
            sb.append(",");
            sb.append(String.valueOf(ySpeed));
            sb.append("\n");

            worldView.onScreen=false;
            worldView.outputStream.write(sb.toString().getBytes());
            worldView.outputStream.flush();

        } catch (IOException e) {
//           e.printStackTrace();
        }
    }

    public void setX(float x) {
        this.x = x;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getY() {
        return y;
    }

    public float getX() {
        return x;
    }

    public float getxSpeed() {
        return xSpeed;
    }

    public float getySpeed() {
        return ySpeed;
    }

    public void setySpeed(float ySpeed) {
        this.ySpeed = ySpeed;
    }

    public void setxSpeed(float xSpeed) {
        this.xSpeed = xSpeed;
    }

    public float getBallRadius() {
        return ballRadius;
    }

    public void setBallRadius(float ballRadius) {
        this.ballRadius = ballRadius;
    }

    public void updatePosition(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public void setTotalSpeed(float totalSpeed) {
        this.totalSpeed = totalSpeed;
    }

    public float getTotalSpeed() {
        return totalSpeed;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }
}
