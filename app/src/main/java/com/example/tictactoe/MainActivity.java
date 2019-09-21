package com.example.tictactoe;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener
{

    private ImageButton[][] buttons = new ImageButton[3][3];

    private boolean player1Turn = true;

    private int roundCount = 0;

    private int player1Points = 0;
    private int player2Points = 0;

    private TextView textViewPlayer1;
    private TextView textViewPlayer2;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textViewPlayer1 = findViewById(R.id.player_1_text_view);
        textViewPlayer2 = findViewById(R.id.player_2_text_view);

        for (int row = 0; row < 3; row++)
        {
            for (int column = 0; column < 3; column++)
            {
                String buttonId = "button_" + row + column;
                int buttonResId = getResources().getIdentifier(buttonId, "id", getPackageName());

                buttons[row][column] = findViewById(buttonResId);
                buttons[row][column].setOnClickListener(this);
                buttons[row][column].setTag(R.drawable.blank_black_button);
            }
        }

        Button buttonReset = findViewById(R.id.button_reset);
        buttonReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetGameState();
            }
        });
    }

    @Override
    public void onClick(View view)
    {
        if (!( (int) ((ImageButton) view).getTag() == R.drawable.blank_black_button))
        {
            return;
        }

        if (player1Turn)
        {
            ((ImageButton) view).setImageResource(R.drawable.mandelbrot);
            ((ImageButton) view).setTag(R.drawable.mandelbrot);
        }
        else
        {
            ((ImageButton) view).setImageResource(R.drawable.julia);
            ((ImageButton) view).setTag(R.drawable.julia);
        }

        roundCount++;
        if (checkIfPlayerWon())
        {
            if (player1Turn)
            {
                player1Won();
            }
            else
            {
                player2Won();
            }
        }
        else if (roundCount == 9)
        {
            catGame();
        }
        else
        {
            player1Turn = !player1Turn;
            setTitleBarColorForPlayerTurn();
        }
    }

    private boolean checkIfPlayerWon()
    {
        int[][] currentBoardState = new int[3][3];

        for (int row = 0; row < 3; row++)
        {
            for (int column = 0; column < 3; column++)
            {
                currentBoardState[row][column] = (int) buttons[row][column].getTag();
            }
        }

        for (int row = 0; row < 3; row++)
        {
            if (currentBoardState[row][0] == currentBoardState[row][1]
             && currentBoardState[row][0] == currentBoardState[row][2]
             && !(currentBoardState[row][0] == R.drawable.blank_black_button))
            {
                return true;
            }
        }

        for (int column = 0; column < 3; column++)
        {
            if (currentBoardState[0][column] == currentBoardState[1][column]
             && currentBoardState[0][column] == currentBoardState[2][column]
             && !(currentBoardState[0][column] == R.drawable.blank_black_button))
            {
                return true;
            }
        }

        if (currentBoardState[0][0] == currentBoardState[1][1]
         && currentBoardState[0][0] == currentBoardState[2][2]
         && !(currentBoardState[0][0] == R.drawable.blank_black_button))
        {
            return true;
        }

        if (currentBoardState[0][2] == currentBoardState[1][1]
         && currentBoardState[0][2] == currentBoardState[2][0]
         && !(currentBoardState[0][2] == R.drawable.blank_black_button))
        {
            return true;
        }

        return false;
    }

    private void player1Won()
    {
        player1Points++;
        Toast.makeText(this, "Player 1 Wins!", Toast.LENGTH_SHORT).show();
        updatePlayerPoints();
        resetBoardState();
    }

    private void player2Won()
    {
        player2Points++;
        Toast.makeText(this, "Player 2 Wins!", Toast.LENGTH_SHORT).show();
        updatePlayerPoints();
        resetBoardState();
    }

    private void catGame()
    {
        Toast.makeText(this, "Cat Game!", Toast.LENGTH_SHORT).show();
        resetBoardState();
    }

    private void updatePlayerPoints()
    {
        textViewPlayer1.setText("Player 1: " + player1Points);
        textViewPlayer2.setText("Player 2: " + player2Points);
    }

    private void resetBoardState()
    {
        for (int row = 0; row < 3; row++)
        {
            for (int column = 0; column < 3; column++)
            {
                buttons[row][column].setImageResource(R.drawable.blank_black_button);
                buttons[row][column].setTag(R.drawable.blank_black_button);
            }
        }

        player1Turn = true;
        roundCount = 0;

        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.mandelbrotRed)));
        getSupportActionBar().setTitle(R.string.player_1_turn);
    }

    private void resetGameState()
    {
        resetBoardState();
        player1Points = 0;
        player2Points = 0;
        updatePlayerPoints();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState)
    {
        super.onSaveInstanceState(outState);

        outState.putInt("roundCount", roundCount);
        outState.putInt("player1Points", player1Points);
        outState.putInt("player2Points", player2Points);
        outState.putBoolean("player1Turn", player1Turn);
        outState.putIntegerArrayList("imageButtonsState", saveImageButtonsState());
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState)
    {
        super.onRestoreInstanceState(savedInstanceState);

        roundCount = savedInstanceState.getInt("roundCount");
        player1Points = savedInstanceState.getInt("player1Points");
        player2Points = savedInstanceState.getInt("player2Points");
        player1Turn = savedInstanceState.getBoolean("player1Turn");
        restoreImageButtonsState(savedInstanceState.getIntegerArrayList("imageButtonsState"));
        setTitleBarColorForPlayerTurn();
    }

    private ArrayList<Integer> saveImageButtonsState()
    {
        ArrayList<Integer> imageButtonsResourceIds = new ArrayList<Integer>(9);
        for (int row = 0; row < 3; row++)
        {
            for (int column = 0; column < 3; column++)
            {
                imageButtonsResourceIds.add((int) buttons[row][column].getTag());
            }
        }

        return imageButtonsResourceIds;
    }

    private void restoreImageButtonsState(ArrayList<Integer> imageButtonsResourceIds)
    {
        for (int row = 0; row < 3; row++)
        {
            for (int column = 0; column < 3; column++)
            {
                buttons[row][column].setImageResource(imageButtonsResourceIds.get((row*3)+column));
                buttons[row][column].setTag(imageButtonsResourceIds.get((row*3)+column));
            }
        }
    }

    private void setTitleBarColorForPlayerTurn()
    {
        if (player1Turn)
        {
            getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.mandelbrotRed)));
            getSupportActionBar().setTitle(R.string.player_1_turn);
        }
        else
        {
            getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.juliaBlue)));
            getSupportActionBar().setTitle(R.string.player_2_turn);
        }
    }
}
