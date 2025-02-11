package com.example.infoeduca;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.icu.text.UnicodeSetSpanner;
import android.os.Bundle;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Random;

public class Puzzle extends AppCompatActivity {

    private static final int COLUMNS = 3;
    private  static final int DIMENSIONS = COLUMNS * COLUMNS;

    private static  String[] tileList;

    private static  GestureDetectGridView mGridView;

    private static  int mColumnWidth, mColumnHeight;

    public   static final String UP = "up";
    public static final String DOWN = "down";
    public  static final String RIGHT = "right";
    public static final String LEFT = "left";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_puzzle);

        init();

        scramble();



        setDimensions();
    }
    private void setDimensions(){
        ViewTreeObserver vto = mGridView.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                mGridView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                int displayWidth = mGridView.getMeasuredWidth();
                int displayHeight = mGridView.getMeasuredHeight();

                int statusbarHeight = getStatusBarHeight(getApplicationContext());
                int requireHeight = displayHeight - statusbarHeight;

                 mColumnWidth = displayWidth / COLUMNS;
                 mColumnHeight = requireHeight /COLUMNS;

                 display(getApplicationContext());
            }
        });
    }

    private int getStatusBarHeight(Context context){
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");

        if(resourceId > 0){
            result = context.getResources().getDimensionPixelSize(resourceId);
        }

        return result;
    }

    private  static  void display(Context context){
        ArrayList<Button> buttons = new ArrayList<>();
        Button button;

        for(int i = 0; i<tileList.length;i++){
            button = new Button(context);

            if(tileList[i].equals("0"))
                button.setBackgroundResource(R.drawable.pigeon_piece1);
            else if(tileList[i].equals("1"))

                button.setBackgroundResource(R.drawable.pigeon_piece2);
            else if(tileList[i].equals("2"))

                button.setBackgroundResource(R.drawable.pigeon_piece3);
            else if(tileList[i].equals("3"))

                button.setBackgroundResource(R.drawable.pigeon_piece4);
            else if(tileList[i].equals("4"))

                button.setBackgroundResource(R.drawable.pigeon_piece5);
            else if(tileList[i].equals("5"))

                button.setBackgroundResource(R.drawable.pigeon_piece6);
            else if(tileList[i].equals("6"))

                button.setBackgroundResource(R.drawable.pigeon_piece7);
            else if(tileList[i].equals("7"))

                button.setBackgroundResource(R.drawable.pigeon_piece8);
            else if(tileList[i].equals("8"))

                button.setBackgroundResource(R.drawable.pigeon_piece9);

            buttons.add(button);
            }
 mGridView.setAdapter(new CustomAdapter(buttons, mColumnWidth, mColumnHeight));
        }



    private  void scramble(){
        int index;
        String temp;
        Random random = new Random();

        for(int i = tileList.length -1; i > 0; i--){
            index = random.nextInt(i+1);
            temp = tileList[index];
            tileList[index] = tileList[i];
            tileList[i] = temp;
        }

    }

    private void init(){
    mGridView = (GestureDetectGridView) findViewById(R.id.grid);
    mGridView.setNumColumns(COLUMNS);
        tileList = new String[DIMENSIONS];
        for(int i = 0; i < DIMENSIONS; i++){
            tileList[i] = String.valueOf(i);
        }

    }
    private static void swap(Context context, int position, int swap){
    String newPosition = tileList[position+swap];
    tileList[position + swap] = tileList[position];
    tileList[position] = newPosition;

    display(context);

    if(isSolved()){
        Toast.makeText(context, "YOU WIN", Toast.LENGTH_SHORT).show();
    }
    }


    private static boolean isSolved() {
        boolean solved = false;

        for (int i = 0; i < tileList.length; i++) {
            if (tileList[i].equals(String.valueOf(i))) {
                solved = true;
            } else {
                solved = false;
                break;
            }
        }

        return solved;
    }

    public static void moveTiles(Context context, String direction, int position) {

        // Upper-left-corner tile
        if (position == 0) {

            if (direction.equals(RIGHT)) swap(context, position, 1);
            else if (direction.equals(DOWN)) swap(context, position, COLUMNS);
            else Toast.makeText(context, "Invalid move", Toast.LENGTH_SHORT).show();

            // Upper-center tiles
        } else if (position > 0 && position < COLUMNS - 1) {
            if (direction.equals(LEFT)) swap(context, position, -1);
            else if (direction.equals(DOWN)) swap(context, position, COLUMNS);
            else if (direction.equals(RIGHT)) swap(context, position, 1);
            else Toast.makeText(context, "Invalid move", Toast.LENGTH_SHORT).show();

            // Upper-right-corner tile
        } else if (position == COLUMNS - 1) {
            if (direction.equals(LEFT)) swap(context, position, -1);
            else if (direction.equals(DOWN)) swap(context, position, COLUMNS);
            else Toast.makeText(context, "Invalid move", Toast.LENGTH_SHORT).show();

            // Left-side tiles
        } else if (position > COLUMNS - 1 && position < DIMENSIONS - COLUMNS &&
                position % COLUMNS == 0) {
            if (direction.equals(UP)) swap(context, position, -COLUMNS);
            else if (direction.equals(RIGHT)) swap(context, position, 1);
            else if (direction.equals(DOWN)) swap(context, position, COLUMNS);
            else Toast.makeText(context, "Invalid move", Toast.LENGTH_SHORT).show();

            // Right-side AND bottom-right-corner tiles
        } else if (position == COLUMNS * 2 - 1 || position == COLUMNS * 3 - 1) {
            if (direction.equals(UP)) swap(context, position, -COLUMNS);
            else if (direction.equals(LEFT)) swap(context, position, -1);
            else if (direction.equals(DOWN)) {

                // Tolerates only the right-side tiles to swap downwards as opposed to the bottom-
                // right-corner tile.
                if (position <= DIMENSIONS - COLUMNS - 1) swap(context, position,
                        COLUMNS);
                else Toast.makeText(context, "Invalid move", Toast.LENGTH_SHORT).show();
            } else Toast.makeText(context, "Invalid move", Toast.LENGTH_SHORT).show();

            // Bottom-left corner tile
        } else if (position == DIMENSIONS - COLUMNS) {
            if (direction.equals(UP)) swap(context, position, -COLUMNS);
            else if (direction.equals(RIGHT)) swap(context, position, 1);
            else Toast.makeText(context, "Invalid move", Toast.LENGTH_SHORT).show();

            // Bottom-center tiles
        } else if (position < DIMENSIONS - 1 && position > DIMENSIONS - COLUMNS) {
            if (direction.equals(UP)) swap(context, position, -COLUMNS);
            else if (direction.equals(LEFT)) swap(context, position, -1);
            else if (direction.equals(RIGHT)) swap(context, position, 1);
            else Toast.makeText(context, "Invalid move", Toast.LENGTH_SHORT).show();

            // Center tiles
        } else {
            if (direction.equals(UP)) swap(context, position, -COLUMNS);
            else if (direction.equals(LEFT)) swap(context, position, -1);
            else if (direction.equals(RIGHT)) swap(context, position, 1);
            else swap(context, position, COLUMNS);
        }
    }
}
