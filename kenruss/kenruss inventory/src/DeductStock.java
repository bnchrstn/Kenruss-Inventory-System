import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class DeductStock implements ActionListener {

    JFrame deductStockFrame = new JFrame();
    JPanel backPanel = new JPanel();

    JLabel titleLabel = new JLabel("Deduct Stock:");
    JLabel enterIDLabel = new JLabel("Enter ID:");
    JLabel numOfStocksLabel = new JLabel("Number of Stocks Deducted:");
    JTextField enterIDTextField = new JTextField();
    JTextField numOfStocksTextField = new JTextField();

    JButton deductButton = new RoundedButton("Deduct");
    JButton backButton = new RoundedButton("Back");

    DeductStock() {
        deductStockFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        deductStockFrame.setSize(300, 300);
        deductStockFrame.setLayout(null);
        deductStockFrame.setResizable(false);
        deductStockFrame.setLocationRelativeTo(null);
        deductStockFrame.setVisible(true);

        backPanel.setLayout(null);
        backPanel.setBackground(new Color(84, 116, 251));
        backPanel.setBounds(0, 0, 400, 300);

        titleLabel.setFont(new Font("Verdana", Font.BOLD, 20));
        titleLabel.setBounds(50, 20, 200, 40);
        titleLabel.setForeground(Color.white);

        enterIDLabel.setBounds(50, 70, 100, 20);
        enterIDTextField.setBounds(50, 100, 200, 20);
        numOfStocksLabel.setBounds(50, 130, 200, 20);
        numOfStocksTextField.setBounds(50, 160, 200, 20);

        deductButton.setBounds(30, 220, 90, 30);
        deductButton.setBackground(new Color(4, 76, 172));
        deductButton.setFont(new Font("Verdana", Font.BOLD, 10));
        deductButton.addActionListener(this);

        backButton.setBounds(170, 220, 80, 30);
        backButton.setBackground(new Color(4, 76, 172));
        backButton.setFont(new Font("Verdana", Font.BOLD, 10));
        backButton.addActionListener(this);

        deductStockFrame.add(titleLabel);
        deductStockFrame.add(enterIDLabel);
        deductStockFrame.add(enterIDTextField);
        deductStockFrame.add(numOfStocksLabel);
        deductStockFrame.add(numOfStocksTextField);
        deductStockFrame.add(deductButton);
        deductStockFrame.add(backButton);
        deductStockFrame.add(backPanel);
    }

    private void deductStockFromDatabase(String id, int quantityToDeduct, int quantityToAdd) {
        String serverName = "MSI";
        String databaseName = "kenruss_db";
        String url = "jdbc:sqlserver://" + serverName + ";databaseName=" + databaseName + ";integratedSecurity=true;encrypt=true;trustServerCertificate=true";
    
        String selectQuery = "SELECT quantity FROM Products WHERE prod_id = ?";
        String updateQuery = "UPDATE Products SET quantity = quantity - ? WHERE prod_id = ?";
        String selectSalesQuery = "SELECT * FROM Sales WHERE prod_id = ?";
        String updateSalesQuery = "UPDATE Sales SET num_of_sales = num_of_sales + ?, last_bought = ? WHERE prod_id = ?";
        String insertSalesQuery = "INSERT INTO Sales (prod_id, num_of_sales, last_bought) VALUES (?, ?, ?)";
    
        try (Connection connection = DriverManager.getConnection(url);
             PreparedStatement selectStatement = connection.prepareStatement(selectQuery);
             PreparedStatement updateStatement = connection.prepareStatement(updateQuery);
             PreparedStatement selectSalesStatement = connection.prepareStatement(selectSalesQuery);
             PreparedStatement updateSalesStatement = connection.prepareStatement(updateSalesQuery);
             PreparedStatement insertSalesStatement = connection.prepareStatement(insertSalesQuery)) {
    
            selectStatement.setString(1, id);
            ResultSet resultSet = selectStatement.executeQuery();
    
            if (resultSet.next()) {
                int currentQuantity = resultSet.getInt("quantity");
                if (currentQuantity >= quantityToDeduct) {
                    updateStatement.setInt(1, quantityToDeduct);
                    updateStatement.setString(2, id);
                    updateStatement.executeUpdate();
    
                    selectSalesStatement.setString(1, id);
                    ResultSet salesResultSet = selectSalesStatement.executeQuery();
                    if (salesResultSet.next()) {
                        updateSalesStatement.setInt(1, quantityToDeduct);
                        updateSalesStatement.setTimestamp(2, new Timestamp(System.currentTimeMillis()));
                        updateSalesStatement.setString(3, id);
                        updateSalesStatement.executeUpdate();
                    } else {
                        insertSalesStatement.setString(1, id);
                        insertSalesStatement.setInt(2, quantityToDeduct);
                        insertSalesStatement.setTimestamp(3, new Timestamp(System.currentTimeMillis()));
                        insertSalesStatement.executeUpdate();
                    }
                    JOptionPane.showMessageDialog(null, "Quantity updated successfully for ID: " + id);
                } else {
                    JOptionPane.showMessageDialog(null, "Invalid input: number of stocks to be deducted exceeded the existing quantity");
                }
            } else {
                JOptionPane.showMessageDialog(null, "ID not found in the database");
            }
            deductStockFrame.dispose();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Failed to update quantity: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == backButton) {
            deductStockFrame.dispose();
        } else if (e.getSource() == deductButton) {
            try {
                String id = enterIDTextField.getText();
                int quantityToDeduct = Integer.parseInt(numOfStocksTextField.getText());
                int quantityToAdd = Integer.parseInt(numOfStocksTextField.getText());
                deductStockFromDatabase(id, quantityToDeduct, quantityToAdd);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "Invalid quantity format. Please enter a valid number.");
            }
        }
    }
}