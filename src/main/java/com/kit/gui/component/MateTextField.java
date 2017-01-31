package com.kit.gui.component;

import javax.swing.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

/**
 * Date: 07/12/2016
 * Time: 10:45
 *
 * @author Matt Collinge
 */
public class MateTextField extends JTextField {

    private String promptText;

    public MateTextField() {
        this("");
    }

    public MateTextField(String promptText) {
        this(promptText, "");
    }

    public MateTextField(String promptText, String value) {
        super(value);
        this.promptText = promptText;
        if (getText().isEmpty())
            setText(this.promptText);
        addFocusListener(new MateTextFieldFocusListener());
    }

    private class MateTextFieldFocusListener implements FocusListener {

        @Override
        public void focusGained(FocusEvent e) {
            if (MateTextField.this.getText().equals(MateTextField.this.promptText))
                MateTextField.this.setText("");
        }

        @Override
        public void focusLost(FocusEvent e) {
            if (MateTextField.this.getText().isEmpty())
                MateTextField.this.setText(MateTextField.this.promptText);
        }
    }

}
