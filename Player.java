public class Player {

    int row;
    int col;

    // Sets the players location
    public void placePlayer( int row, int col ) {
        this.row = row;
        this.col = col;
    }

    public int getX( int left, int right) {
        // If you move right, your col index increases 
        col = col + right;
        // If you move left, your col index decreases
        col = col - left;
        return col;
    }
    public int getY( int down, int up ) {
        // If you move up, ur row index decreases 
        row = row - up;
        // If you move down, your row number increases 
        row = row + down;
        return row;
    }
}