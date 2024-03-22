import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;

public class AddProduct implements ActionListener {

    JFrame addProductFrame = new JFrame();
    JPanel backPanel = new JPanel();
    JPanel upPanel = new JPanel();
    JPanel lowPanel = new JPanel();

    JLabel titleLabel = new JLabel("Add Product:");
    JTable table = new JTable();

    JButton backButton = new RoundedButton("Back");
    JLabel categoryLabel = new JLabel("Category:");
    JLabel itemNameLabel = new JLabel("Item Name:");
    JLabel itemDescLabel = new JLabel("Item Description:");
    JLabel itemBrandLabel = new JLabel("Item Brand:");
    JLabel suppIDLabel = new JLabel("Supplier ID:");
    JLabel quantityLabel = new JLabel("Quantity:");
    JLabel priceLabel = new JLabel("Price:");
    JLabel prodIdLabel = new JLabel("Product ID:");

    JTextField categoryTextField = new JTextField();
    JTextField itemNameTextField = new JTextField();
    JTextField itemDescTextField = new JTextField();
    JTextField itemBrandTextField = new JTextField();
    JTextField suppIDTextField = new JTextField();
    JTextField quantityTextField = new JTextField();
    JTextField priceTextField = new JTextField();
    JTextField prodIdTextField = new JTextField();

    JButton addButton = new RoundedButton("Add");

    AddProduct() {
        addProductFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        addProductFrame.setSize(400, 400);
        addProductFrame.setLayout(null);
        addProductFrame.setResizable(false);
        addProductFrame.setLocationRelativeTo(null);
        addProductFrame.setVisible(true);

        backPanel.setLayout(null);
        backPanel.setBackground(new Color(84, 116, 251));
        backPanel.setBounds(0, 0, 1100, 750);

        titleLabel.setFont(new Font("Verdana", Font.BOLD, 20));
        titleLabel.setBounds(50, 20, 200, 40);
        titleLabel.setForeground(Color.white);

        prodIdLabel.setBounds(50, 70, 100, 20);
        prodIdTextField.setBounds(150, 70, 200, 20);
        categoryLabel.setBounds(50, 100, 100, 20);
        categoryTextField.setBounds(150, 100, 200, 20);
        itemNameLabel.setBounds(50, 130, 100, 20);
        itemNameTextField.setBounds(150, 130, 200, 20);
        itemDescLabel.setBounds(50, 160, 100, 20);
        itemDescTextField.setBounds(150, 160, 200, 20);
        itemBrandLabel.setBounds(50, 190, 100, 20);
        itemBrandTextField.setBounds(150, 190, 200, 20);
        suppIDLabel.setBounds(50, 220, 100, 20);
        suppIDTextField.setBounds(150, 220, 200, 20);
        quantityLabel.setBounds(50, 250, 100, 20);
        quantityTextField.setBounds(150, 250, 200, 20);
        priceLabel.setBounds(50, 280, 100, 20);
        priceTextField.setBounds(150, 280, 200, 20);
    
        addButton.setBounds(30, 320, 80, 30);
        addButton.setBackground(new Color(4, 76, 172));
        addButton.setFont(new Font("Verdana", Font.BOLD, 10));
        addButton.addActionListener(this);
    
        backButton.setBounds(270, 320, 80, 30);
        backButton.setBackground(new Color(4, 76, 172));
        backButton.setFont(new Font("Verdana", Font.BOLD, 10));
        backButton.addActionListener(this);
    
        addProductFrame.add(titleLabel);
        addProductFrame.add(prodIdLabel);
        addProductFrame.add(prodIdTextField);
        addProductFrame.add(categoryLabel);
        addProductFrame.add(categoryTextField);
        addProductFrame.add(itemNameLabel);
        addProductFrame.add(itemNameTextField);
        addProductFrame.add(itemDescLabel);
        addProductFrame.add(itemDescTextField);
        addProductFrame.add(itemBrandLabel);
        addProductFrame.add(itemBrandTextField);
        addProductFrame.add(suppIDLabel);
        addProductFrame.add(suppIDTextField);
        addProductFrame.add(quantityLabel);
        addProductFrame.add(quantityTextField);
        addProductFrame.add(priceLabel);
        addProductFrame.add(priceTextField);
        addProductFrame.add(addButton);
        addProductFrame.add(backButton);
        addProductFrame.add(backPanel);
    }

    private void addProductToDatabase(String category, String itemName, String itemDesc, String itemBrand, String suppID, int quantity, double price, String prodId) {
        String serverName = "MSI";
        String databaseName = "kenruss_db";
        String url = "jdbc:sqlserver://" + serverName + ";databaseName=" + databaseName + ";integratedSecurity=true;encrypt=true;trustServerCertificate=true";
    
        String insertQuery = "INSERT INTO Products (category, item_name, item_desc, item_brand, supp_id, quantity, price, prod_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        String checkIfExistsQuery = "SELECT COUNT(*) FROM Products WHERE prod_id = ?";
        String checkSupplierQuery = "SELECT COUNT(*) FROM Supplier WHERE supp_id = ?";
    
        try (Connection connection = DriverManager.getConnection(url);
             PreparedStatement preparedStatementCheck = connection.prepareStatement(checkIfExistsQuery);
             PreparedStatement preparedStatementInsert = connection.prepareStatement(insertQuery);
             PreparedStatement preparedStatementCheckSupplier = connection.prepareStatement(checkSupplierQuery)) {
    
            preparedStatementCheck.setString(1, prodId);
            ResultSet resultSet = preparedStatementCheck.executeQuery();
            resultSet.next();
            int count = resultSet.getInt(1);

            // checks if all the fields are all filled out 
            if (category.isEmpty() || itemName.isEmpty() || itemDesc.isEmpty() || itemBrand.isEmpty() || suppID.isEmpty() || prodId.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Please fill out all the fields.");
                return;
            }
    
            if (count > 0) {
                JOptionPane.showMessageDialog(null, "Product ID already exists. Please choose a different one.");
                return;
            }
    
            if (!suppID.isEmpty()) { 
                preparedStatementCheckSupplier.setString(1, suppID);
                resultSet = preparedStatementCheckSupplier.executeQuery();
                resultSet.next();
                count = resultSet.getInt(1);
    
                if (count == 0) {
                    JOptionPane.showMessageDialog(null, "Supplier ID does not exist. Please enter a valid supplier ID.");
                    return;
                }
            }
    
            preparedStatementInsert.setString(1, category);
            preparedStatementInsert.setString(2, itemName);
            preparedStatementInsert.setString(3, itemDesc);
            preparedStatementInsert.setString(4, itemBrand);
            if (suppID.isEmpty()) {
                preparedStatementInsert.setNull(5, java.sql.Types.VARCHAR); 
            } else {
                preparedStatementInsert.setString(5, suppID);
            }
            preparedStatementInsert.setInt(6, quantity);
            preparedStatementInsert.setDouble(7, price);
            preparedStatementInsert.setString(8, prodId);
    
            int rowsAffected = preparedStatementInsert.executeUpdate();
    
            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(null, "Product added successfully");
                addProductFrame.dispose();
            } else {
                JOptionPane.showMessageDialog(null, "Failed to add product");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Failed to add product: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == backButton) {
            addProductFrame.dispose();
        } else if (e.getSource() == addButton) {
            try {
                String category = categoryTextField.getText();
                String itemName = itemNameTextField.getText();
                String itemDesc = itemDescTextField.getText();
                String itemBrand = itemBrandTextField.getText();
                String suppID = suppIDTextField.getText();
                int quantity = Integer.parseInt(quantityTextField.getText());
                double price = Double.parseDouble(priceTextField.getText());
                String prodId = prodIdTextField.getText();
                addProductToDatabase(category, itemName, itemDesc, itemBrand, suppID, quantity, price, prodId);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "Invalid price, quantity, or product ID format. Please enter valid numbers.");
            }
        }
    }
}
