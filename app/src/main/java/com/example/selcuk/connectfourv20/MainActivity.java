package com.example.selcuk.connectfourv20;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.support.annotation.DrawableRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.ListIterator;

public class MainActivity extends AppCompatActivity {

    CircularList<String> boardSizes = new CircularList<>();
    CircularList<Integer> boardImages = new CircularList<>();
    ListIterator<Integer> imageIter = boardImages.listIterator();
    ListIterator<String> textIter = boardSizes.listIterator();
    String currentSize = "5x5";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //set up full screen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_main);
        // Ekranin donmesini engeller
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        TextView boardSize = (TextView) findViewById(R.id.boardSize);
        boardSize.setText("6x7");
        initializeMainInfo();

    }

    public void initializeMainInfo() {

        textIter.add("6x6");
        textIter.add("6x7");
        textIter.add("7x7");

        imageIter.add(R.drawable.sixxsix);
        imageIter.add(R.drawable.cflogo2);
        imageIter.add(R.drawable.seven);

        imageIter = boardImages.listIterator(1);
        textIter = boardSizes.listIterator(1);
    }

    public void changeBoardSize(View view) {
        ImageView boardPic = (ImageView) findViewById(R.id.boardImage);
        TextView boardSize = (TextView) findViewById(R.id.boardSize);

        if(view.getId() == R.id.rightArrow) {
            boardPic.setImageResource(imageIter.next());
            currentSize = textIter.next();
            boardSize.setText(currentSize);
        } else if(view.getId() == R.id.leftArrow) {
            boardPic.setImageResource(imageIter.previous());
            currentSize = textIter.previous();
            boardSize.setText(currentSize);
        }
    }

    public void startTheGame(View view) {
        Intent intent = new Intent(this, GamePlay.class);
       // Ana ekranda alinan input degerleri icin degiskenler
        String[] boardSize = currentSize.split("x");
        int width = Integer.valueOf(boardSize[1]);
        int height = Integer.valueOf(boardSize[0]);
        int seconds = 0;
        int gameMode = 0;
        if(view.getId() == R.id.multiplayer)
            gameMode = 1;

        // Diger activity'i acmak icin olusturulan intent'e gonderilmek istenen
        // veriler eklenir
        intent.putExtra("widthValue", width);
        intent.putExtra("heightValue", height);
        intent.putExtra("timeValue", seconds);
        intent.putExtra("gameMode", gameMode);
        startActivity(intent);
    }
}
