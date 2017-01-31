package com.kit.gui.view;


import com.kit.Application;
import com.kit.gui.component.MateButton;
import com.kit.gui.component.MateCheckBox;
import com.kit.gui.component.MateSeperator;
import com.kit.gui.component.MateTitleBar;
import com.kit.Application;
import com.kit.core.Session;
import com.kit.gui.component.MateButton;
import com.kit.gui.component.MateCheckBox;
import com.kit.gui.component.MateSeperator;
import com.kit.gui.component.MateTitleBar;
import com.kit.gui.controller.LoginController;
import com.kit.gui.util.ComponentResizer;
import net.miginfocom.layout.CC;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.net.URL;

/**
 * @author const_
 * @author A_C
 */
public class LoginView extends JFrame implements ActionListener, KeyListener {

    private static final Dimension STD_SIZE = new Dimension(300, 300);

    private LoginController loginController;
    private JPanel mainPanel;
    private MateTitleBar titleBar;
    private JTextField email;
    private JPasswordField password;
    private MateButton loginBtn;
    private MateButton createAccountBtn;
    private JLabel statusLbl;
    private MateCheckBox rememberMe;

    public LoginView(final LoginController loginController) {
        this.loginController = loginController;
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setMinimumSize(STD_SIZE);
        setPreferredSize(STD_SIZE);
        setResizable(true);
        setUndecorated(true);

        setLayout(new BorderLayout());

        ComponentResizer cr = new ComponentResizer();
        cr.setDragInsets(new Insets(4, 4, 4, 4));
        cr.registerComponent(this);

        JPopupMenu.setDefaultLightWeightPopupEnabled(false);

        titleBar = new MateTitleBar(this, true);
        getContentPane().add(titleBar, BorderLayout.NORTH);

        mainPanel = new JPanel();
        mainPanel.setBackground(Application.COLOUR_SCHEME.getDark());
        mainPanel.setLayout(new MigLayout("fillx, wrap"));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(0, 5, 5, 5));

        JLabel header = new JLabel("07Kit");
        header.setFont(header.getFont().deriveFont(26.0f).deriveFont(Font.BOLD));
        header.setForeground(Application.COLOUR_SCHEME.getText());
        CC componentConstraints = new CC();
        componentConstraints.alignX("center").spanX();
        mainPanel.add(header, componentConstraints);

        JLabel emailLbl = new JLabel("Email:");
        emailLbl.setForeground(Application.COLOUR_SCHEME.getText());
        mainPanel.add(emailLbl);
        email = new JTextField();
        email.setColumns(30);
        mainPanel.add(email);

        email.addKeyListener(this);

        JLabel passwordLbl = new JLabel("Password:");
        passwordLbl.setForeground(Application.COLOUR_SCHEME.getText());
        mainPanel.add(passwordLbl);
        password = new JPasswordField();
        password.setColumns(30);
        mainPanel.add(password);

        password.addKeyListener(this);

        rememberMe = new MateCheckBox("Remember me");
        rememberMe.setBackground(Application.COLOUR_SCHEME.getDark());
        rememberMe.setForeground(Application.COLOUR_SCHEME.getText());
        rememberMe.setMinimumSize(new Dimension(200, 20));
        componentConstraints = new CC();
        componentConstraints.growY(1.0f);
        mainPanel.add(rememberMe, componentConstraints);

        loginBtn = new MateButton("Login");
        loginBtn.setMinimumSize(new Dimension(260, 20));
        componentConstraints = new CC();
        componentConstraints.growY(1.0f);
        componentConstraints.alignX("center").spanX();
        mainPanel.add(loginBtn, componentConstraints);

        loginBtn.setActionCommand("login");
        loginBtn.addActionListener(this);

        MateSeperator seperator = new MateSeperator(280, 4);
        componentConstraints = new CC();
        componentConstraints.growY(1.0f);
        componentConstraints.alignX("center").spanX();
        mainPanel.add(seperator, componentConstraints);

        createAccountBtn = new MateButton("Create Account");
        componentConstraints = new CC();
        componentConstraints.growY(1.0f);
        componentConstraints.alignX("center").spanX();
        createAccountBtn.setMinimumSize(new Dimension(260, 20));
        mainPanel.add(createAccountBtn, componentConstraints);

        createAccountBtn.setActionCommand("create");
        createAccountBtn.addActionListener(this);

        statusLbl = new JLabel("Status: Not logged in");
        statusLbl.setForeground(Application.COLOUR_SCHEME.getText());
        componentConstraints = new CC();
        componentConstraints.growY(1.0f);
        componentConstraints.alignX("center").spanX();
        mainPanel.add(statusLbl, componentConstraints);

        getContentPane().add(mainPanel, BorderLayout.CENTER);

        if (Session.get().getEmail() != null) {
            email.setText(Session.get().getEmail().getValue());
            rememberMe.setSelected(true);
        }
        if (Session.get().getApiKey() != null) {
            password.setText("pre-entered");
            rememberMe.setSelected(true);
        }
        addWindowListener(new WindowAdapter() {
            public void windowOpened(WindowEvent e) {
                email.requestFocus();
            }
        });
        pack();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("login")) {
            String emailStr = email.getText().trim();
            String passwordStr = new String(password.getPassword());
            if (emailStr.length() > 0 && passwordStr.length() > 0) {
                statusLbl.setText("Status: Logging in");
                loginController.login(emailStr, passwordStr, rememberMe.isSelected());
            } else {
                statusLbl.setText("Status: Please enter email/password");
            }
        } else if (e.getActionCommand().equals("create")) {
            try {
                Desktop.getDesktop().browse(new URL("https://07kit.org").toURI());
            } catch (Exception ex) {
                ex.printStackTrace();
                statusLbl.setText("Status: Error opening browser");
            }
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
        if (e.getKeyChar() == KeyEvent.VK_ENTER) {
            if (e.getSource().equals(password)) {
                if (email.getText().trim().length() == 0) {
                    email.requestFocus();
                } else {
                    loginBtn.doClick();
                }
            } else if (e.getSource().equals(email)) {
                if (password.getPassword().length == 0) {
                    password.requestFocus();
                } else {
                    loginBtn.doClick();
                }
            }
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    public LoginController getLoginController() {
        return loginController;
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }

    public MateTitleBar getTitleBar() {
        return titleBar;
    }

    public JTextField getEmail() {
        return email;
    }

    public JPasswordField getPassword() {
        return password;
    }

    public MateButton getLoginBtn() {
        return loginBtn;
    }

    public MateButton getCreateAccountBtn() {
        return createAccountBtn;
    }

    public JLabel getStatusLbl() {
        return statusLbl;
    }

    public MateCheckBox getRememberMe() {
        return rememberMe;
    }
}

