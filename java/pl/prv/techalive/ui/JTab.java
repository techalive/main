package pl.prv.techalive.ui;

import pl.prv.techalive.core.Encryptable;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.Objects;
import java.util.function.Function;

public abstract class JTab extends JPanel implements EncryptableListener {

    protected Encryptable encryptable;

    private JPanel textPanel = new JPanel();
    protected JTextField textField = new JTextField();
    private JTextArea resultField = new JTextArea(5, 35);
    private JButton actionButton = getActionButton();

    JTab() {
        actionButton.setPreferredSize(new Dimension(200, 40));
        actionButton.addActionListener(this::onAction);

        resultField.setPreferredSize(new Dimension(250, 100));
        resultField.setLineWrap(true);

        textField.setPreferredSize(new Dimension(200, 20));
        textPanel.add(getTextLabel());
        textPanel.add(textField);
    }


    // Creates content for tab
    JPanel create() {
        JPanel resultPanel = new JPanel();
        resultPanel.setLayout(new BoxLayout(resultPanel, BoxLayout.Y_AXIS));

        JLabel encryptResultLabel = getResultLabel();
        encryptResultLabel.setPreferredSize(new Dimension(300, 40));

        resultPanel.add(encryptResultLabel);
        resultPanel.add(resultField);

        this.setPreferredSize(new Dimension(550, 250));
        this.add(textPanel);
        this.add(resultPanel);
        this.add(actionButton, BorderLayout.CENTER);
        return this;
    }

    protected abstract JLabel getResultLabel();

    @Override
    public void onEncryptableChange(Encryptable e) {
        this.encryptable = e;
    }

    // Triggered when action on button is fired
    // Validate input
    private void onAction(ActionEvent e) {
        if (encryptable == null) {
            JOptionPane.showMessageDialog(null, "Proszê wybraæ algorytm z listy!", "Wyst¹pi³ b³¹d", JOptionPane.ERROR_MESSAGE);
        } else if (Objects.isNull(textField.getText()) || "".equals(textField.getText())) {
            JOptionPane.showMessageDialog(null, "Proszê wpisaæ tekst", "Wyst¹pi³ b³¹d", JOptionPane.ERROR_MESSAGE);
        } else {
            Function<String, String> processingFunction = this::process;
            resultField.setText(processingFunction.apply(textField.getText()));
        }
    }

    protected abstract String process(String text);

    protected abstract JLabel getTextLabel();

    abstract JButton getActionButton();
}
