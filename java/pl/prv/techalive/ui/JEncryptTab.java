package pl.prv.techalive.ui;

import javax.swing.*;

public class JEncryptTab extends JTab implements EncryptableListener {

    @Override
    protected JLabel getResultLabel() {
        return new JLabel("Rezultat szyfrowania:");
    }

    //  Main method for processing the algorithm on button click
    @Override
    protected String process(String text) {
        try {
            if (textField.getText().length() % encryptable.getLengthMultiple() != 0) {
                JOptionPane.showMessageDialog(null, "D³ugoœæ tekstu musi byæ wielokrotnoœci¹ liczby " + encryptable.getLengthMultiple(),
                        "Wyst¹pi³ b³¹d", JOptionPane.ERROR_MESSAGE);
            } else {
                return encryptable.encrypt(text);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Wyst¹pi³‚ b³¹d podczas szyfrowania danych", "Wyst¹pi³ b³¹d", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
        return "";
    }

    @Override
    protected JLabel getTextLabel() {
        return new JLabel("Podaj plain text:");
    }

    @Override
    JButton getActionButton() {
        return new JButton("Szyfruj");
    }
}
