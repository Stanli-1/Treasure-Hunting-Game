// Imports for scanner
import java.io.*;
import java.util.*;

class Main {

    // Number of Rows and columns of grid
    static int gridRows = 7;
    static int gridColumns = 7;

    // Grid of game (array of objects from base class, Tile)
    // Every object created for this array will have an x coor, y coord, and icon
    static Tile [][]grid = new Tile [gridRows][gridColumns];

    // The following three functions are to return whether a player, bomb, or key is 
    //     currently on a tile (given through parameters)
    private static boolean isPlayer (int x, int y) {
        if ( x == ((gridRows-1)/2) && y == ((gridRows-1)/2) ) 
            return true;
        else
            return false;
    } 

    private static boolean isBomb (int x, int y) {
        if  ( ((grid[x][y]).getClass().getSimpleName().equals("Bomb")) )
            return true;
        else
            return false;
    } 

    private static boolean isKey (int x, int y) {
        if ( ((grid[x][y]).getClass().getSimpleName().equals("Key")) ) 
            return true;
        else
            return false;
    } 
    
    public static void main(String[] args) {

    // Number of various items
    int bombs = 3;
    int keys = 1;
    int chests = 1;
    int lives = 3;
    int moves = 0;

    // Cheats
    boolean cheats = true;

    // Icons
    String heartIcon = "❤️";

    // Game conditions
        // c = ongoing game, w = win, l = lost
    String gameCondition = "c";
    boolean hasKey = false;
      

    // Testing x and y values that might be the coords of items such as bombs, keys, walls, etc.
    int tempRow;
    int tempCol;
    
    // Scanners
    Scanner stringScanner = new Scanner(System.in);  
    String name;
    String input;
      
      
//---------------------------------------------Set up Hidden Grid------------------------------------------------------------------------ 

    // Creates a player and then places it in the middle
    Player user = new Player();
    user.placePlayer( ((gridRows-1)/2), ((gridColumns-1)/2) );
      
    // Fill grid with empty tiles initially
    for( int row = 0; row < grid.length; row++ ) {
        for( int col = 0; col < grid[row].length; col++ ) {
            grid[row][col] = new Tile();
        }
    }
    
    // Place bombs randomly (from 1-7)
    // Note (-1) is because index starts at 0
    for (int i = 0; i < bombs; i++) {
        tempRow = (int)Math.floor(Math.random()*((gridRows-1)+1));
        tempCol = (int)Math.floor(Math.random()*((gridRows-1)+1));
        // If the function returns that it's not a bomb, and it isn't on the player, then make a bomb there
        if ( !isBomb(tempCol, tempRow) && !isPlayer(tempCol, tempRow) ) {
            grid[tempCol][tempRow] = new Bomb();
        }
        // Or else (there is a bomb there already) dont count this iteration
        else {
            i--;
        }
    } // Bomb
    
    // Place Key randomly
    // Note (-1) is because index starts at 0
    for (int i = 0; i < keys; i++) {
        tempRow = (int)Math.floor(Math.random()*((gridRows-1)+1));
        tempCol = (int)Math.floor(Math.random()*((gridRows-1)+1));
        // If there is not already a bomb or player, place a key
        if ( !isBomb(tempCol, tempRow) && !isPlayer(tempCol, tempRow) ) {
            grid[tempCol][tempRow] = new Key();
        }
        // Or else (there is something there already) dont count this iteration
        else {
            i--;
        }
    } // Key

    // Place Chest randomly
    // Note (-1) is because index starts at 0
    for (int i = 0; i < chests; i++) {
        tempRow = (int)Math.floor(Math.random()*((gridRows-1)+1));
        tempCol = (int)Math.floor(Math.random()*((gridRows-1)+1));
        // If there is not already a bomb, player, or key, place a chest
        if ( !isBomb(tempCol, tempRow) && !isPlayer(tempCol, tempRow) && !isKey(tempCol, tempRow) ) {
            grid[tempCol][tempRow] = new Chest();
        }
        // Or else (there is something there already) dont count this iteration
        else {
            i--;
        }
    } // Chest

      
//---------------------------------------------Intro-----------------------------------------------------------------------

      
    // Welcome message
    System.out.println("\n------------💰Welcome to Stan's Treasure Hunting Game!💰------------");
    System.out.print("\nBefore we start, what is your name?    ");
    name = stringScanner.nextLine( );

    // Activates hidden cheat code to see tiles if the user is me
    if ( name.equals("Stan") ) {
        cheats = true;
    }

    System.out.println("\nHello " + name + ", here are the rules of the game...");
    System.out.println("1. You will be placed in the middle of a " + gridRows + " by " + gridColumns + " grid.");
    System.out.println("2. There are " + bombs + " bomb(s), " + keys + " key(s) and " + chests + " chest(s).");
    System.out.println("3. You may move one tile at a time.");
    System.out.println("4. You start with " + lives + " lives and stepping on a bomb removes 1 life.");
    System.out.println("5. Bombs can be detonated multiple times.");
    System.out.println("6. Bring the key to the chest to win!");
    
    System.out.print("\nEnter any key to continue:    ");
    input = stringScanner.nextLine( );

    // Sleeping skill was learned from - https://www.geeksforgeeks.org/thread-sleep-method-in-java-with-examples/
    // In the lines below will "try" to print the prompt and wait for 1.5 second (1500ms)
    try {
        System.out.print("\nGood Luck!");
        Thread.sleep(1500);
    }
    // If there is an error, the code below is executed
    catch (Exception e) {
    }
    // Clear screen learned from https://www.javatpoint.com/how-to-clear-screen-in-java
    // In ANSI, \033[H\033[2J means to clear the console
    System.out.print("\033[H\033[2J");  
    // This line moves the cursor back to the top of the screen
    System.out.flush();  

      
//--------------------------------------------Start of game-----------------------------------------------------------
      
    // Makes the tile that the player spawns on 'discovered'
    grid[user.getY(0,0)][user.getX(0,0)].discovered();
      
    while ( gameCondition.equals("c") ) {

//----------------------------------------Check what user stepped on--------------------------------------------------
    
        // Depending on what the user steps on, there are different outcomes
            // the code inside the switch() returns the class name of the tile that the user is on
        switch( grid[user.getY(0,0)][user.getX(0,0)].getClass().getSimpleName() ) {

            // If user steps on bomb
            case "Bomb":
                System.out.println("You stepped on a bomb!");
                System.out.print("Enter any key to continue:    ");
                input = stringScanner.nextLine( );
                // Remove a life
                lives--;
                // Checks if game is over
                if ( lives <= 0 ) {
                    gameCondition = "l";
                    continue;
                }
                //Prompt
                System.out.println("\nOne life will be lost and you will return to the middle.");
                System.out.println("If you had the key before, you must retrieve it again.");
                System.out.print("Enter any key to continue:    ");
                input = stringScanner.nextLine( );
                // Removes key from user
                hasKey = false;
                // Places player back in origin
                user.placePlayer( ((gridRows-1)/2), ((gridColumns-1)/2) );
                break;
                
            case "Chest":
                System.out.println("You found the Chest!");
                // Either player has the key or doesnt
                    // if they have it they win
                if ( hasKey == true ) {
                    System.out.print("Enter any key to continue:    ");
                    input = stringScanner.nextLine( );
                    gameCondition = "w";
                    continue;
                }
                    // If they dont have the key then nothing happens
                else {
                    System.out.println("Bring the Key to the Chest to win!");
                    System.out.print("\nEnter any key to continue:    ");
                    input = stringScanner.nextLine( );
                }
                break;
                
            case "Key":
                System.out.println("You found the key!");
                System.out.println("Bring it to the Chest to win!");
                System.out.print("\nEnter any key to continue:    ");
                input = stringScanner.nextLine( );
                // Player now has key
                hasKey = true;
                break;
        }

//---------------------------------------------Print Game---------------------------------------------------------------------------------------
        
        // Prints lives 
        System.out.println("\n\n╍╍╍╍╍╍╍╍╍╍╍╍╍╍╍╍╍╍╍╍╍╍╍╍╍╍╍╍╍╍╍╍╍╍╍╍╍╍╍╍╍╍╍╍╍╍╍╍╍╍╍╍╍╍╍╍╍╍╍\n");
        System.out.print("\nLives: " );
        for ( int i = 0; i < lives; i++ ) {
            System.out.print( heartIcon + " ");
        }

        // Prints the number of moves
        System.out.print("           Moves: " + moves);
        
        // Prints whether the user has the key
        if ( hasKey == true ) {
            System.out.print("           You have the Key!");
        }

        // Prints top border of grid
        System.out.print("\n\n\t\t\t┏");
        for (int i = 0; i < grid.length; i++) {
            System.out.print("━━━━");
        }
        System.out.print("━━┓\n\t\t\t┃");
        // Margin between top border
        for (int i = 0; i < grid.length; i++) {
            System.out.print("    ");
        }
        System.out.println("  ┃");
        // Prints items 
        for (int row = 0; row < grid.length; row++)    {
            System.out.print("\t\t\t┃  ");
            for (int col = 0; col < grid[row].length; col++)    {
                // If user is on tile, return that the user is on it 
                // (second boolean is true)
                if ( user.getX( 0, 0 ) == col && user.getY( 0, 0 ) == row ) {
                    System.out.print( grid[row][col].icon( cheats, true ) + "  ");
                }
                // Otherwise print the regular tile
                else {
                    System.out.print( grid[row][col].icon( cheats, false ) + "  ");    
                }
            }
            System.out.print("┃\n\t\t\t┃");   
            // Margin
            for (int i = 0; i < grid.length; i++) {
                System.out.print("    ");
            }
            System.out.println("  ┃");
        }
        // Prints Bottom border
        System.out.print("\t\t\t┗");
        for (int i = 0; i < grid.length; i++) {
            System.out.print("━━━━");
        }
        System.out.print("━━┛\n");

        
//----------------------------------------User Controls----------------------------------------------------------------

        // Prompt for user to move 
        System.out.println("\n\nYou are the 🤖!");
        System.out.println("Please enter your next move.");
        System.out.print("Note: W = Up, D = Right, S = Down, A = Left    ");
        
        try {
            input = stringScanner.nextLine( );
            // If the user enters anything other than w, a, s, or d
            if ( !input.toLowerCase().equals("w") && !input.toLowerCase().equals("d") && !input.toLowerCase().equals("s") && !input.toLowerCase().equals("a") ) {
                throw new InvalidInputException("Please enter W, A, S, or D.");
            }
        }
        // Give user prompt to give valid input
        catch( InvalidInputException e ) {
            System.out.println( "\nThat is not a valid input!\n" + e );
            // Dont count this move
            moves--;
            System.out.print( "\nEnter any key to continue:    " );
            input = stringScanner.nextLine( );
        }
        // Switch case to alter the player's position 
        switch( input.toLowerCase() ) {
            // Move up
            case "w":
                // w = up, so 1 is passed into the y coord method 
                //     to indicate the user wants to go up
                // If they are going past 0,0 or the length of the grid,
                //     They are walking into a wall
                if ( user.getY(0,1) < 0 ) {
                    System.out.println("\nYou're running into a wall!");
                    System.out.print("Enter any key to continue    ");
                    input = stringScanner.nextLine( );
                    // "undos" the input and puts player back to original spot
                    user.getY(0,-1);
                    // Dont count this move
                    moves--;
                }
                // For the current position of the player, call the method 'discovered'
                    // This indicates to the computer that the tile should return its true icon
                    // this overrides the default parameters when printing the grid
                grid[user.getY(0,0)][user.getX(0,0)].discovered();
                break;

            // Move right
            case "d":
                if ( user.getX(0,1) >= grid.length ) {
                    System.out.println("\nYou're running into a wall!");  
                    System.out.print("Enter any key to continue    ");
                    input = stringScanner.nextLine( );
                    user.getX(0,-1);
                    moves--;
                }
                grid[user.getY(0,0)][user.getX(0,0)].discovered();
                break;

            // Move down
            case "s":
                if ( user.getY(1,0) >= grid[0].length ) {
                    System.out.println("\nYou're running into a wall!");  
                    System.out.print("Enter any key to continue    ");
                    input = stringScanner.nextLine( );
                    user.getY(-1,0);
                    moves--;
                }
                grid[user.getY(0,0)][user.getX(0,0)].discovered();
                break;

            // move left
            case "a":
                if ( user.getX(1,0) < 0 ) {
                    System.out.println("\nYou're running into a wall!");   
                    System.out.print("Enter any key to continue    ");
                    input = stringScanner.nextLine( );
                    user.getX(-1,0);
                    moves--;
                }
                grid[user.getY(0,0)][user.getX(0,0)].discovered();
                break;
                
        } // Switch
        
        // Clear screen for next turn
        System.out.print("\033[H\033[2J");  
        System.out.flush(); 

        // Increment number of moves
        moves++;
        
    } // While loop

//------------------------------------------------ End of game ----------------------------------------------------------------------

    // Clear's screen
    System.out.print("\033[H\033[2J");  
    System.out.flush(); 

    // If the user won
    if ( gameCondition.equals("w") ) {        
        System.out.println("\n🎊🎊🎊 Congratulations " + name + ", you won! 🎊🎊🎊");
        System.out.println(   "         You finished in " + moves + " moves!" );  
    }
    // If user lost
    else {
        System.out.println("\n☠️  You have run out lives " + name + "! ☠️");
        System.out.println(     "    Your number of moves: " + moves);
    }

        
  } // main
} // Main