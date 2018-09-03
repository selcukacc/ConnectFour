package com.example.selcuk.connectfourv20;

/*  Winter Homework
 *
 *   Name: Islam Goktan SELCUK
 *   No: 141044071
 * */

public class ConnectFour  {

    private Character[][] gameBoard;
    private int height;
    private int width;
    private int playerTurn = 0;
    private Integer[] fourInRow;
    private char winner;

    public ConnectFour(int height, int width) {
        this.height = height;
        this.width = width;
        fourInRow = new Integer[8];
        gameBoard = new Character[height][width];

        for (int i = 0; i < height; i++)
            for (int j = 0; j < width; j++)
                gameBoard[i][j] = '.';
    }

    public int getPlayerTurn() { return playerTurn; }

    public void setPlayerTurn(int playerTurn) {
        if(playerTurn > -1)
            this.playerTurn = playerTurn;
    }

    public boolean isGameEnd() {
        boolean result = true;
        for(int i = 0; i < height && result; i++) {
            for(int j = 0; j < width && result; j++) {
                if(gameBoard[i][j] == '.')
                    result = false;
            }
        }

        if( checkWinner('O') > 0 ||
                checkWinner('X') > 0 ) {
            result = true;
        }

        return result;
    }

    public void clear() {
        for(int i = 0; i < height; i++)
            for(int j = 0; j < width; j++)
                gameBoard[i][j] = '.';
        playerTurn = 0;
    }

    public int play(int column) {
        int row = height - 1;

        while(row >= 0 && (gameBoard[row][column] == 'X' || gameBoard[row][column] == 'O') )
            row--;

        if(row == -1)
            return -1;
        else {
            if(playerTurn % 2 == 0)
                gameBoard[row][column] = 'X';
            else
                gameBoard[row][column] = 'O';

            playerTurn++;
            //printBoard();
            System.out.println(isGameEnd());
            return row;
        }
    }

    public int[] play() {
        // data for winning moves
        int hor[] = new int[2];        // takes result from functions
        int ver[] = new int[2];
        // data for prevention moves
        int preventHor[] = new int[2];
        int preventVer[] = new int[2];

        int cros[] = new int[2];
        int preventCros[] = new int[2];

        int row;
        int column;
        int result = 0; // target place for move
        int found = 0;

        // prevention moves for user
        verticalMoveControl(preventVer, 'X');
        horizontalMoveControl(preventHor, 'X');
        crossControl(preventCros, 'X');

        // winning moves
        verticalMoveControl(ver, 'O');
        horizontalMoveControl(hor, 'O');
        crossControl(cros, 'O');

        // If winning or losing is close, makes that moves
        if(cros[1] == 3)
            result = cros[0];
        else if(ver[1] == 3)
            result = ver[0];
        else if(hor[1] == 3)
            result = hor[0];
        else if(preventCros[1] == 3)
            result = preventCros[0];
        else if(preventHor[1] == 3)
            result = preventHor[0];
        else if(preventVer[1] == 3)
            result = preventVer[0];
        else if(preventHor[1] == 2)
            result = preventHor[0];
        else if(preventCros[1] == 2)
            result = preventCros[0];
            // makes the most logical move
            // checks number of symbols side by side or over and over
        else if(ver[1] > hor[1])
            result = ver[0];
        else
            result = hor[0];


        // there is no logical move then it finds first empty cell
        if(result == 0)
            for(int i = 0; i < height && found == 0; i++)
                for(int j = 0; j < width && found == 0; j++)
                    if(gameBoard[i][j] == '.') {
                        result = j;
                        found = 1;
                    }

        column = result; // target place
        row = height - 1;


        // If desired row is filled by users or computer, row is reduced.
        //Row always initializes to row = height-1 in gameplay function.
        while(row >= 0 &&
                (gameBoard[row][column] == 'X' ||
                        gameBoard[row][column] == 'O'))
            row--;

        // If there is no row for move return 0
        if(row == -1) {
            int[] temp = {-1, -1};
            return temp; // It means invalid move
        }
        else { // // If move is valid, symbol('O' or 'X') is written.
            int[] temp = {row, column};
            if(playerTurn % 2 == 0)
                gameBoard[row][column] = 'X';
            else
                gameBoard[row][column] = 'O';
            playerTurn++;
            return temp; // It means valid move
        }
    }

    private void crossControl(int result[], char cellColour) {
        int counter = 0;
        int max = 0;

        for(int i = 0; i < height; i++) {
            for(int j = 0; j < width; j++) {

                for(int x = 0 ; x < 4 ; x++) {
                    if(i + x < height && j + x < width &&
                            gameBoard[i+x][j+x] == cellColour)
                        counter++;
                    else
                        break;
                }

                if(counter > max) {
                    if(j-1 >= 0 && i-1 >= 0 && gameBoard[i-1][j-1] == '.' &&
                            gameBoard[i][j-1] != '.') {
                        max = counter;
                        result[0] = j-1;
                    }
                }
                counter = 0;

                for(int x = 0 ; x < 4 ; x++) {
                    if(i + x < height && j - x > 0 &&
                            gameBoard[i+x][j-x] == cellColour)
                        counter++;
                    else
                        break;
                }

                if(counter > max) {
                    if(j+1 < width && j+1 >= 0 && i-1 >= 0 &&
                            gameBoard[i-1][j+1] == '.' &&
                            gameBoard[i][j+1] != '.') {
                        max = counter;
                        result[0] = j+1;
                    }
                }
                counter = 0;


            }
        }

        System.out.println(counter);
        result[1] = max;
    }

    private void verticalMoveControl(int result[], char cellColour) {
        int counter = 0; // keeps number of symbols over and over
        int max = 0;     // max number of symbols
        int i, j;
        int lastIndex;

        // bottom to up
        for(j = 0; j < width; j++) {
            for(i = height - 1; i >= 0; i--) {
                // counts number of symbols
                if (gameBoard[i][j] == cellColour)
                    counter++;
                    // If there isnt computer symbol, starts counting again
                else if(gameBoard[i][j] != cellColour) {
                    lastIndex = i;
                    // If maximum number is found and there is a empty cell,
                    // place is assigned to result
                    if(counter > max &&
                            gameBoard[lastIndex][j] == '.') {
                        max = counter;
                        result[0] = j;
                    }
                    counter = 0;
                }
            }
        }
        result[1] = max; // maximum number of symbols
    }

    private void horizontalMoveControl(int result[], char cellColour) {
        int counter = 0;
        int max = 0;
        int i, j, a, k; // counters
        char temp[] = new char[4]; // It keeps every side by side 4 cells in the board

        result[0] = 0;
        // left to right
        for(k = height - 1; k >= 0; k--) {
            for (i = 0; i <= width - 4; i++) {
                for (j = i, a = 0; a < 4; j++, a++)
                    temp[a] = gameBoard[k][j];//four cells from board

                counter = 0;
                for (a = 0; a < 4 && counter != -1; a++) {
                    if (temp[a] == cellColour) // counting symbols
                        counter++;
                        // If there is a opponent symbol, stops counting
                    else if ( (cellColour == 'X' &&
                            temp[a] == 'O') ||
                            (cellColour == 'O' && temp[a] == 'X') )
                        counter = -1;
                }
                if (counter > max) {
                    for(int b = 0, y1 = j - 4; b < 4; b++, y1++)
                        // If there is a empty cell and down of the cell is filled,
                        // assigns place for move
                        if (k + 1 == height ||
                                gameBoard[k + 1][y1] != '.')
                            if (gameBoard[k][y1] == '.') {
                                result[0] = y1;
                                max = counter;
                            }
                }
            }
        }

        result[1] = max;
    }

    public int checkWinner(Character cellColour) {
        int result = 0;
        int i, j;
        // It controls every cell in the board for finding four same symbols
        for (i = 0; i < height && result == 0; i++)
            for (j = 0; j < width && result == 0; j++) {
                // controls from bottom to up
                if (i >= 3 &&
                        gameBoard[i][j] == cellColour &&
                        gameBoard[i - 1][j] == cellColour &&
                        gameBoard[i - 2][j] == cellColour &&
                        gameBoard[i - 3][j] == cellColour) {
                    result = 1;
                    Integer[] temp = {i, j, i-1, j, i-2, j, i-3, j};
                    fourInRow = temp;
                }
                // controls from left to right
                else if (j <= width - 4 &&
                        gameBoard[i][j] == cellColour &&
                        gameBoard[i][j + 1] == cellColour &&
                        gameBoard[i][j + 2] == cellColour &&
                        gameBoard[i][j + 3] == cellColour) {
                    result = 1;
                    Integer[] temp = {i, j, i, j+1, i, j+2, i, j+3};
                    fourInRow = temp;
                }
                // controls left diagonal
                else if (i >= 3 && j >= 3 &&
                        gameBoard[i][j] == cellColour &&
                        gameBoard[i - 1][j - 1] == cellColour &&
                        gameBoard[i - 2][j - 2] == cellColour &&
                        gameBoard[i - 3][j - 3] == cellColour) {

                    result = 1;
                    Integer[] temp = {i, j, i-1, j-1, i-2, j-2, i-3, j-3};
                    fourInRow = temp;
                }
                // controls right diagonal
                else if (i >= 3 && j <= width - 4 &&
                        gameBoard[i][j] == cellColour &&
                        gameBoard[i - 1][j + 1] == cellColour &&
                        gameBoard[i - 2][j + 2] == cellColour &&
                        gameBoard[i - 3][j + 3] == cellColour) {

                    result = 1;
                    Integer[] temp = {i, j, i-1, j+1, i-2, j+2, i-3, j+3};
                    fourInRow = temp;
                }

            }
        // because the winner is determined by this data
        if (result == 1 && cellColour == 'O') {
            //it indicates for pvp user2 is winner, for pve winner is computer
            winner = 'Y';
            return 2;
        }
        else {
            winner = 'R';
            return result;
        }
    }

    public char getWinner() { return winner; }

    public Integer[] getFourInRow() { return fourInRow; }

    public void printBoard() {
        System.out.println("----");
        for(int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++)
                System.out.print(gameBoard[i][j]);
            System.out.println();
        }
        System.out.println("----");
    }

    public int deleteTheCell(int column) {
        int row = 0;
        for( ; row < height; row++)
            if(gameBoard[row][column] == 'O' ||
                    gameBoard[row][column] == 'X') {
                gameBoard[row][column] = '.';
                return row;
            }

        return 0;
    }
}