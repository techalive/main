package pl.prv.techalive.ui;

import javax.swing.*;

public class JDecryptTab extends JTab  {

    @Override
    protected JLabel getResultLabel() {
        return new JLabel("Rezultat deszyfrowania:");
    }

    @Override
    protected JLabel getTextLabel() {
        return new JLabel("Podaj hash:");
    }

    @Override
    JButton getActionButton() {
        return new JButton("Deszyfruj");
    }

    //  Main method for processing the algorithm on button click
    @Override
    protected String process(String text) {
        try {
            return encryptable.decrypt(text);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Wystapi³‚ b³¹d podczas deszyfrowania danych", "Wyst¹pi³ b³¹d", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
        return "";
    }
}
