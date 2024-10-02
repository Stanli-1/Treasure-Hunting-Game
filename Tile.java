// Base class for all tiles in the game 
public class Tile {

    String playerIcon = "🤖";
    String undiscoveredIcon = "❔";
    String icon = "  ";
    
    boolean discovered = false;

    // returns an icon for printing
    public String icon( boolean defaultDiscovered, boolean playerOn) {
        // if the player is on it, it prints the player
        if ( playerOn == true ) {
            return playerIcon;
        }
        // if the user already traveled on the tile, return the true icon
        //     'discovered' is used here to override the parameter 'defaultDiscovered' 
        //      as it will always be passed in as  false
        if ( defaultDiscovered == true || discovered == true ) {
            return icon;
        }
        // if the user has not traveled on the tile, return an undiscovered icon
        else {
            return undiscoveredIcon;
        }
    }

    // Call this method whenever the user steps on a tile
    public void discovered() {
        discovered = true;
    }

} // Tile