package pl.prv.techalive;

import pl.prv.techalive.ui.MainFrame;

import javax.swing.*;

/**
 * @author Adrian Czarniecki
 *
 */
public class Runner  {
    public static void main( String[] args )  {
        SwingUtilities.invokeLater(Runner::runApp);
    }
    private static void runApp() {
        new MainFrame().display();
    }
}
