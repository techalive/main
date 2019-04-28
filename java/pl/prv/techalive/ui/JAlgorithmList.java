package pl.prv.techalive.ui;

import pl.prv.techalive.core.Encryptable;
import pl.prv.techalive.encoder.methods.AES;
import pl.prv.techalive.encoder.methods.DES;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Creates a combo box with algorithm list based on Encryptable interface
 */
public class JAlgorithmList extends JPanel {

    private JComboBox<Encryptable> jComboBox;

    public JAlgorithmList(MainFrame frame) {
        jComboBox = new JComboBox<>(getAlgorithms());
        this.add(jComboBox);
        jComboBox.setSelectedIndex(-1);
        jComboBox.addItemListener(frame);
    }

    private Encryptable[] getAlgorithms() {
        List<Encryptable> list = new ArrayList<>();

        list.addAll(Arrays.asList(DES.Method.values()));
        list.addAll(Arrays.asList(AES.Method.values()));

        return list.toArray(new Encryptable[0]);
    }

}
