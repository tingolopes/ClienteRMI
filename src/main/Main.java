package main;

import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import view.ClienteListarDialog;

public class Main {

    public static void main(String args[]) {
        try {
            ClienteListarDialog d = new ClienteListarDialog();
            d.setVisible(true);
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
