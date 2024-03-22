import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.sql.*;

public class LoginPage implements ActionListener {

    JFrame loginPageFrame = new JFrame();
    JPanel backPanel = new JPanel();
    JPanel upPanel = new JPanel();
    JPanel lowPanel = new JPanel();

    JLabel titleLabel = new JLabel("KENRUSS MOTOR PARTS");
    JLabel usernameLabel = new JLabel("Username");
    JLabel passwordLabel = new JLabel("Password");

    JTextField usernameTextField = new JTextField();
    JPasswordField passwordTextField = new JPasswordField();
    JCheckBox showPassword = new JCheckBox("Show Password");

    JButton exitButton = new RoundedButton("Exit");
    JButton loginButton = new RoundedButton("Login");
    JButton resetButton = new RoundedButton("Reset");

    Connection connection;
    String jdbcUrl = "jdbc:sqlserver://MSI;databaseName=kenruss_db;integratedSecurity=true;encrypt=true;trustServerCertificate=true";

    LoginPage() {
        loginPageFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        loginPageFrame.setSize(1100, 750);
        loginPageFrame.setLayout(null);
        loginPageFrame.setResizable(false);
        loginPageFrame.setLocationRelativeTo(null);
        loginPageFrame.setVisible(true);

        backPanel.setLayout(null);
        backPanel.setBackground(new Color(4, 76, 172));
        backPanel.setBounds(0, 0, 1100, 750);

        upPanel.setLayout(null);
        upPanel.setBackground(new Color(84, 116, 251));
        upPanel.setBounds(15, 0, 1056, 95);

        lowPanel.setLayout(null);
        lowPanel.setBackground(new Color(141, 189, 255));
        lowPanel.setBounds(15, 95, 1056, 605);

        titleLabel.setFont(new Font("Verdana", Font.BOLD, 48));
        titleLabel.setBounds(215,25 , 800, 46);
        titleLabel.setForeground(Color.white);

        usernameLabel.setFont(new Font("Verdana", Font.BOLD, 28));
        usernameLabel.setBounds(175,180 , 200, 46);
        usernameLabel.setForeground(Color.white);

        passwordLabel.setFont(new Font("Verdana", Font.BOLD, 28));
        passwordLabel.setBounds(180,250 , 200, 46);
        passwordLabel.setForeground(Color.white);

        usernameTextField.setBounds(370, 180, 380, 45);
        usernameTextField.setBackground(new Color(4, 76, 172));
        usernameTextField.setForeground(Color.WHITE);
        usernameTextField.setFont(new Font("Verdana", Font.BOLD, 23));
        usernameTextField.setCaretColor(Color.white);

        passwordTextField.setBounds(370, 250, 380, 45);
        passwordTextField.setBackground(new Color(4, 76, 172));
        passwordTextField.setForeground(Color.WHITE);
        passwordTextField.setFont(new Font("Verdana", Font.BOLD, 23));
        passwordTextField.setCaretColor(Color.white);

        loginButton.setBounds(575, 330, 150, 40);
        loginButton.setBackground(new Color(4, 76, 172));
        loginButton.setFont(new Font("Verdana", Font.CENTER_BASELINE, 20));

        resetButton.setBounds(390, 330, 150, 40);
        resetButton.setBackground(new Color(4, 76, 172));
        resetButton.setFont(new Font("Verdana", Font.CENTER_BASELINE, 20));

        exitButton.setBounds(950, 640, 100, 40);
        exitButton.setBackground(new Color(4, 76, 172));
        exitButton.setFont(new Font("Verdana", Font.CENTER_BASELINE, 20));

        showPassword.setBounds(370, 295, 150, 30);
        showPassword.setBackground(new Color(141, 189, 255));
        showPassword.setForeground(Color.white);
        showPassword.setFont(new Font("Verdana", Font.CENTER_BASELINE, 12));

        loginPageFrame.add(exitButton);
        loginPageFrame.add(titleLabel);

        loginPageFrame.add(upPanel);
        loginPageFrame.add(lowPanel);
        loginPageFrame.add(backPanel);

        lowPanel.add(usernameLabel);
        lowPanel.add(passwordLabel);
        lowPanel.add(usernameTextField);
        lowPanel.add(passwordTextField);
        lowPanel.add(loginButton);
        lowPanel.add(resetButton);
        lowPanel.add(showPassword);

        loginButton.addActionListener(this);
        resetButton.addActionListener(this);
        exitButton.addActionListener(this);
        showPassword.addActionListener(this);

        try {
            connection = DriverManager.getConnection(jdbcUrl);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String enteredUsername = usernameTextField.getText();
        String enteredPassword = new String(passwordTextField.getPassword());
    
        if (e.getSource() == loginButton) {
            try {
                PreparedStatement statement = connection.prepareStatement("SELECT * FROM [User] WHERE user_username = ? AND user_pass = ?");
                statement.setString(1, enteredUsername);
                statement.setString(2, enteredPassword);
                ResultSet resultSet = statement.executeQuery();
    
                if (resultSet.next()) {
                    JOptionPane.showMessageDialog(loginPageFrame, "Login Successful!");
                    loginPageFrame.dispose();
                    HomePage homePage = new HomePage();
                } else {
                    JOptionPane.showMessageDialog(loginPageFrame, "Invalid username or password. Try again.");
                }
                statement.close();
                resultSet.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        } else if (e.getSource() == resetButton) {
            usernameTextField.setText("");
            passwordTextField.setText("");
        } else if (e.getSource() == exitButton) {
            System.exit(0);
        } else if (e.getSource() == showPassword) {
            if (showPassword.isSelected()) {
                passwordTextField.setEchoChar((char) 0);
            } else {
                passwordTextField.setEchoChar('•');
            }
        }
    }
}
