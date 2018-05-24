package chessGame;


import java.net.URL;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 *
 * @author Vincenzo Scialpi 15072935
 */
public class Resource {
    protected static ResourceBundle resources;
    static {
        try {
            resources = ResourceBundle.getBundle("chessGame.res.ChessGameProperties",Locale.getDefault()); // try to get the resource bundle
        } catch(Exception e) {
            System.out.println("Property not found"); // if not display an error...
            javax.swing.JOptionPane.showMessageDialog(null,
                    "Property not found",
                    "Error",javax.swing.JOptionPane.ERROR_MESSAGE);
            System.exit(1); //...and leave the program
        }
    }
    public String getResourceString(String key){
        String str;
        try {
            str = resources.getString(key);
        } catch(Exception e) {
            str = null;
        }
        return str;
    }
    protected URL getResource(String key) {
        String name = getResourceString(key);
        if(name != null){
            URL url = this.getClass().getResource(name);
            return url;
        }
        return null;
    }
}
