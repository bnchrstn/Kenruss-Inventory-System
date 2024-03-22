import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class AddStock implements ActionListener{

    JFrame addStockFrame = new JFrame();
    JPanel backPanel = new JPanel();

    JLabel titleLabel = new JLabel("Add Stock:");
    JLabel productIDLabel = new JLabel("Product ID:");
    JLabel quantityLabel = new JLabel("Quantity:");

    JTextField productIDTextField = new JTextField();
    JTextField quantityTextField = new JTextField();

    JButton addButton = new RoundedButton("Add");
    JButton backButton = new RoundedButton("Back");

    AddStock() {
        addStockFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        addStockFrame.setSize(300, 300);
        addStockFrame.setLayout(null);
        addStockFrame.setResizable(false);
        addStockFrame.setLocationRelativeTo(null);
        addStockFrame.setVisible(true);

        backPanel.setLayout(null);
        backPanel.setBackground(new Color(84, 116, 251));
        backPanel.setBounds(0, 0, 1100, 750);

        titleLabel.setFont(new Font("Verdana", Font.BOLD, 20));
        titleLabel.setBounds(50, 20, 200, 40);
        titleLabel.setForeground(Color.white);

        addButton.setBounds(30, 220, 80, 30);
        addButton.setBackground(new Color(4, 76, 172));
        addButton.setFont(new Font("Verdana", Font.BOLD, 10));
        addButton.addActionListener(this);

        backButton.setBounds(170, 220, 80, 30);
        backButton.setBackground(new Color(4, 76, 172));
        backButton.setFont(new Font("Verdana", Font.BOLD, 10));
        backButton.addActionListener(this);

        productIDLabel.setBounds(50, 70, 100, 20);
        productIDTextField.setBounds(50, 100, 200, 20);
        quantityLabel.setBounds(50, 130, 200, 20);
        quantityTextField.setBounds(50, 160, 200, 20);

        addStockFrame.add(addButton);
        addStockFrame.add(productIDLabel);
        addStockFrame.add(productIDTextField);
        addStockFrame.add(quantityLabel);
        addStockFrame.add(quantityTextField);
        addStockFrame.add(backButton);
        addStockFrame.add(titleLabel);
        addStockFrame.add(backPanel);
    }

    private void addStockToDatabase(String productID, int quantityToAdd) {
        String serverName = "MSI";
        String databaseName = "kenruss_db";
        String url = "jdbc:sqlserver://" + serverName + ";databaseName=" + databaseName + ";integratedSecurity=true;encrypt=true;trustServerCertificate=true";

        String updateQuery = "UPDATE Products SET quantity = quantity + ? WHERE prod_id = ?";

        try (Connection connection = DriverManager.getConnection(url);
             PreparedStatement preparedStatement = connection.prepareStatement(updateQuery)) {

            preparedStatement.setInt(1, quantityToAdd);
            preparedStatement.setString(2, productID);

            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(null, "Stock added successfully for Product ID: " + productID);
                addStockFrame.dispose();
            } else {
                JOptionPane.showMessageDialog(null, "Product ID not found in the database");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Failed to add stock: " + e.getMessage());
            e.printStackTrace();
        }
    }

   @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == backButton) {
            addStockFrame.dispose();
        } else if (e.getSource() == addButton) {
            try {
                String productID = productIDTextField.getText();
                int quantityToAdd = Integer.parseInt(quantityTextField.getText());
                addStockToDatabase(productID, quantityToAdd);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "Invalid quantity format. Please enter a valid number.");
            }
        }
    }
}
