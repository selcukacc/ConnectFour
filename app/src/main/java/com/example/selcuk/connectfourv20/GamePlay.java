package com.example.selcuk.connectfourv20;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

public class GamePlay extends AppCompatActivity {

    private int width;
    private int height;
    private int gameMode;
    private int seconds;
    private TextView subContext = null;

    private LinearLayout[] horizontalLayouts = null;
    private ImageButton[][] allCells = null;

    private ConnectFour game1 = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //set up full screen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_game_play);

        Intent intent = getIntent();
        width = intent.getIntExtra("widthValue", 0);
        height = intent.getIntExtra("heightValue", 0);
        gameMode = intent.getIntExtra("gameMode", 0);

        game1 = new ConnectFour(height, width);
        initializeBoard();
        if(gameMode == 0)
            addListenerForPvc();
        else
            addListenerForPvp();
    }

    private void initializeBoard() {
        LinearLayout mainLayout = (LinearLayout)findViewById(R.id.mainLayout);
        horizontalLayouts = new LinearLayout[height];
        allCells = new ImageButton[height][width];

        for(int i = 0; i < height; i++) {
            horizontalLayouts[i] = linearLayout();
            mainLayout.addView(horizontalLayouts[i]);
        }

        int counter = 0;
        for(int i = 0; i < height; i++) {
            for(int j = 0; j < width; j++) {
                allCells[i][j] = imageButton(counter++);
                horizontalLayouts[i].addView(allCells[i][j]);
            }
        }

        subContext = new TextView(this);
        subContext.setText("Player 1 Turn");
        mainLayout.addView(subContext);
        subContext.setTextSize(25);
        subContext.setTypeface(Typeface.DEFAULT_BOLD);
        subContext.setPadding(0,25,0,0);
        subContext.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
    }

    // player versus computer icin
    private void addListenerForPvc() {
        for(int i = 0; i < height; i++) {
            for(int j = 0; j < width; j++) {
                final int finalJ = j;
                final int finalI = i;
                // tum hucrelere listener eklenir
                allCells[i][j].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // kullanicinin hamlesi alinir
                        int tempRow = game1.play(finalJ);
                        // eger hamle gecerliyse tahtaya islenir
                        if(tempRow >= 0) {
                            //if(getSecond() > 0) {
                                allCells[tempRow][finalJ].setBackgroundResource(R.drawable.redcell);
                               // allMoves.add(finalJ);
                            //}
                            // oyun bitmemisse computer'ın hamlesi alinir
                            if(!game1.isGameEnd()) {
                                // computer icin yapilan hamlenin satir ve sutun olarak indexi alinir
                                int[] temp = game1.play();
                                // eger tahtada bosluk varsa hamle bulana kadar tekrarlanir
                                while (temp[0] < 0 && !game1.isGameEnd())
                                    temp = game1.play();
                                // hamle uygunsa uygulanir
                                if (temp[0] >= 0) {
                                    allCells[temp[0]][temp[1]].setBackgroundResource(R.drawable.yellowcell);
                                   // allMoves.add(temp[1]);
                                }
                            }
                        }

                        if(game1.isGameEnd()) {
                            // kazanana gore dialog mesaji yazilir
                            if(game1.checkWinner('O') > 0)
                                subContext.setText("Player 2 wins");
                                //dialogMessage = "Yellow player won!";
                            else if(game1.checkWinner('X') > 0)
                                subContext.setText("Player 1 wins");
                                //dialogMessage = "Red player won!";
                            else
                                subContext.setText("Nobody wins the game.");
                            //endOfGame = true; // oyunun sonunu belirtir*/
                            disablingListeners();
                            //createPopup();
                            //keepAppWatingForDialog();
                        }
                        // her hamleden sonra zaman tekrar ayarlanir
                        //seconds = setSeconds;
                    }
                });

            }
        }
    }

    private void addListenerForPvp() {
        for(int i = 0; i < height; i++) {
            for(int j = 0; j < width; j++) {
                final int finalJ = j;
                final int finalI = i;
                // her hucre icin listener atanir
                allCells[i][j].setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        // kullanici sirasi alinir
                        int tempPlayerTurn = game1.getPlayerTurn();
                        // kullanicinin hamlesinin gecerliligine bakilir
                        int tempRow = game1.play(finalJ);
                        // eger hamle valid ise tahtaya islenir
                        if(tempRow >= 0 && tempPlayerTurn % 2 == 0) {
                            allCells[tempRow][finalJ].setBackgroundResource(R.drawable.redcell);
                            subContext.setText("Player 2 Turn");
                            // undo komutu icin tum hamleler bir array'e kaydedilir
                            //allMoves.add(finalJ);
                        }
                        // ikinci kullanici icin aynı islemler yapilir
                        else if(tempRow >= 0 && tempPlayerTurn % 2 != 0) {
                            allCells[tempRow][finalJ].setBackgroundResource(R.drawable.yellowcell);
                            subContext.setText("Player 1 Turn");
                            //allMoves.add(finalJ);
                        }
                        // eger oyun biterse kazanana bagli olarak dialog mesaji kaydedilir
                        if(game1.isGameEnd()) {
                            if(game1.checkWinner('O') > 0)
                                subContext.setText("Player 2 wins");
                                //dialogMessage = "Yellow player won!";
                            else if(game1.checkWinner('X') > 0)
                                subContext.setText("Player 1 wins");
                                //dialogMessage = "Red player won!";
                            else
                                subContext.setText("Nobody wins the game.");

                            // daha fazla hamle yapılmaması icin listenerlar devre disi birakilir
                            disablingListeners();
                        }

                        // yapilan her hamleden sonra zaman tekrar ayarlanir
                        //seconds = setSeconds;
                    }
                });

            }
        }
    }

    private void paintWinner() {
        Integer indexes[] = game1.getFourInRow();
        char winner = game1.getWinner();
        if(winner == 'Y') {
            allCells[indexes[0]][indexes[1]].setBackgroundResource(R.drawable.winneryellow);
            allCells[indexes[2]][indexes[3]].setBackgroundResource(R.drawable.winneryellow);
            allCells[indexes[4]][indexes[5]].setBackgroundResource(R.drawable.winneryellow);
            allCells[indexes[6]][indexes[7]].setBackgroundResource(R.drawable.winneryellow);
        }
        else if(winner == 'R') {
            allCells[indexes[0]][indexes[1]].setBackgroundResource(R.drawable.winnerred);
            allCells[indexes[2]][indexes[3]].setBackgroundResource(R.drawable.winnerred);
            allCells[indexes[4]][indexes[5]].setBackgroundResource(R.drawable.winnerred);
            allCells[indexes[6]][indexes[7]].setBackgroundResource(R.drawable.winnerred);
        }

    }

    private void disablingListeners() {
        // tum hucrelerdeki listenerlar devre disi birakilirlar
        for(int i = 0; i < height; i++)
            for(int j = 0; j < width; j++)
                allCells[i][j].setOnClickListener(null);
        // oyunu kazanan varsa kazandigini gosteren hucreler boyanir
        if(game1.checkWinner('O') > 0 || game1.checkWinner('X') > 0)
            paintWinner();
    }

    private ImageButton imageButton(int id) {
        ImageButton button = new ImageButton(this);
        // ekran boyutu baz alinarak pixel buyuklugu icin gereken deger hesaplanir
        final float scale = getResources().getDisplayMetrics().density;
        int pixels = (int) (51.5 * scale + 0.5f);
        // olasi buyuklukleri ekrana sigdirmak icin
        if(width == 5)
            pixels = (pixels * 7) / 5;
        else if(width == 6)
            pixels = (pixels * 7) / 6;
        // butonlarin ozellikleri ayarlanir
        button.setLayoutParams(new LinearLayout.LayoutParams(pixels, pixels)); // boyut
        button.setBackgroundResource(R.drawable.boardcell); // arkaplan resmi
        button.setScaleType(ImageView.ScaleType.FIT_XY); // buttonun cercevesi icin

        // her butona bir id atanir
        String idName = "CF" + id;
        int resourceId = getResources().getIdentifier(idName, "id", getPackageName());
        button.setId(resourceId);

        return button;
    }

    private LinearLayout linearLayout() {
        LinearLayout child = new LinearLayout(this);
        // boyutu ayarlanir
        child.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        // oryantasyonu ayarlanir
        child.setOrientation(LinearLayout.HORIZONTAL);

        return child;
    }

}
