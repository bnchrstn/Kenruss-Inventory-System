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
import javax.swing.JTextField;

public class EditProdInfo implements ActionListener {

    JFrame editProductFrame = new JFrame();
    JPanel backPanel = new JPanel();

    JLabel titleLabel = new JLabel("Edit Product Information:");
    JLabel idLabel = new JLabel("Enter Product ID:");
    JLabel categoryLabel = new JLabel("Category:");
    JLabel itemNameLabel = new JLabel("Item Name:");
    JLabel itemDescLabel = new JLabel("Item Description:");
    JLabel itemBrandLabel = new JLabel("Item Brand:");
    JLabel suppIDLabel = new JLabel("Supplier ID:");
    JLabel priceLabel = new JLabel("Price:");

    JTextField idTextField = new JTextField();
    JTextField categoryTextField = new JTextField();
    JTextField itemNameTextField = new JTextField();
    JTextField itemDescTextField = new JTextField();
    JTextField itemBrandTextField = new JTextField();
    JTextField suppIDTextField = new JTextField();
    JTextField priceTextField = new JTextField();

    JButton fetchButton = new RoundedButton("Get Product Information");
    JButton saveButton = new RoundedButton("Save");
    JButton backButton = new RoundedButton("Back");

    EditProdInfo() {
        editProductFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        editProductFrame.setSize(400, 415);
        editProductFrame.setLayout(null);
        editProductFrame.setResizable(false);
        editProductFrame.setLocationRelativeTo(null);
        editProductFrame.setVisible(true);

        backPanel.setLayout(null);
        backPanel.setBackground(new Color(84, 116, 251));
        backPanel.setBounds(0, 0, 400, 400);

        titleLabel.setFont(new Font("Verdana", Font.BOLD, 20));
        titleLabel.setBounds(50, 20, 300, 40);
        titleLabel.setForeground(Color.white);

        idLabel.setBounds(50, 70, 100, 20);
        idTextField.setBounds(150, 70, 200, 20);
        fetchButton.setBounds(100, 100, 200, 30);
        fetchButton.setBackground(new Color(4, 76, 172));
        fetchButton.setFont(new Font("Verdana", Font.BOLD, 10));
        fetchButton.addActionListener(this);

        categoryLabel.setBounds(50, 140, 100, 20);
        categoryTextField.setBounds(150, 140, 200, 20);
        itemNameLabel.setBounds(50, 170, 100, 20);
        itemNameTextField.setBounds(150, 170, 200, 20);
        itemDescLabel.setBounds(50, 200, 100, 20);
        itemDescTextField.setBounds(150, 200, 200, 20);
        itemBrandLabel.setBounds(50, 230, 100, 20);
        itemBrandTextField.setBounds(150, 230, 200, 20);
        suppIDLabel.setBounds(50, 260, 100, 20);
        suppIDTextField.setBounds(150, 260, 200, 20);
        priceLabel.setBounds(50, 290, 100, 20);
        priceTextField.setBounds(150, 290, 200, 20);

        saveButton.setBounds(30, 330, 80, 30);
        saveButton.setBackground(new Color(4, 76, 172));
        saveButton.setFont(new Font("Verdana", Font.BOLD, 10));
        saveButton.addActionListener(this);

        backButton.setBounds(270, 330, 80, 30);
        backButton.setBackground(new Color(4, 76, 172));
        backButton.setFont(new Font("Verdana", Font.BOLD, 10));
        backButton.addActionListener(this);

        editProductFrame.add(titleLabel);
        editProductFrame.add(idLabel);
        editProductFrame.add(idTextField);
        editProductFrame.add(fetchButton);
        editProductFrame.add(categoryLabel);
        editProductFrame.add(categoryTextField);
        editProductFrame.add(itemNameLabel);
        editProductFrame.add(itemNameTextField);
        editProductFrame.add(itemDescLabel);
        editProductFrame.add(itemDescTextField);
        editProductFrame.add(itemBrandLabel);
        editProductFrame.add(itemBrandTextField);
        editProductFrame.add(suppIDLabel);
        editProductFrame.add(suppIDTextField);
        editProductFrame.add(priceLabel);
        editProductFrame.add(priceTextField);
        editProductFrame.add(saveButton);
        editProductFrame.add(backButton);
        editProductFrame.add(backPanel);
    }

    private void fetchProductFromDatabase(String id) {
        String serverName = "MSI";
        String databaseName = "kenruss_db";
        String url = "jdbc:sqlserver://" + serverName + ";databaseName=" + databaseName + ";integratedSecurity=true;encrypt=true;trustServerCertificate=true";

        String selectQuery = "SELECT category, item_name, item_desc, item_brand, supp_id, price FROM Products WHERE prod_id = ?";

        try (Connection connection = DriverManager.getConnection(url);
             PreparedStatement preparedStatement = connection.prepareStatement(selectQuery)) {

            preparedStatement.setString(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                categoryTextField.setText(resultSet.getString("category"));
                itemNameTextField.setText(resultSet.getString("item_name"));
                itemDescTextField.setText(resultSet.getString("item_desc"));
                itemBrandTextField.setText(resultSet.getString("item_brand"));
                suppIDTextField.setText(resultSet.getString("supp_id"));
                priceTextField.setText(Double.toString(resultSet.getDouble("price")));
            } else {
                JOptionPane.showMessageDialog(null, "No product found with ID: " + id);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Failed to fetch product: " + e.getMessage());
            e.printStackTrace();
        }
    }
    private boolean doesSupplierExist(String suppID) {
        String serverName = "MSI";
        String databaseName = "kenruss_db";
        String url = "jdbc:sqlserver://" + serverName + ";databaseName=" + databaseName + ";integratedSecurity=true;encrypt=true;trustServerCertificate=true";
    
        String selectQuery = "SELECT COUNT(*) AS count FROM Supplier WHERE supp_id = ?";
    
        try (Connection connection = DriverManager.getConnection(url);
             PreparedStatement preparedStatement = connection.prepareStatement(selectQuery)) {
    
            preparedStatement.setString(1, suppID);
            ResultSet resultSet = preparedStatement.executeQuery();
    
            if (resultSet.next()) {
                int count = resultSet.getInt("count");
                return count > 0;
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Failed to check supplier existence: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }
    
    private void saveProductToDatabase(String id, String category, String itemName, String itemDesc, String itemBrand, String suppID,  double price) {
        String serverName = "MSI";
        String databaseName = "kenruss_db";
        String url = "jdbc:sqlserver://" + serverName + ";databaseName=" + databaseName + ";integratedSecurity=true;encrypt=true;trustServerCertificate=true";
    
        try (Connection connection = DriverManager.getConnection(url)) {
            System.out.println("Connected to the database");
    
            if (!suppID.isEmpty() && !doesSupplierExist(suppID)) {
                JOptionPane.showMessageDialog(null, "Supplier with ID: " + suppID + " does not exist.");
                return;
            }
    
            String updateQuery = "UPDATE Products SET category = ?, item_name = ?, item_desc = ?, item_brand = ?, supp_id = ?, price = ? WHERE prod_id = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(updateQuery)) {
                preparedStatement.setString(1, category);
                preparedStatement.setString(2, itemName);
                preparedStatement.setString(3, itemDesc);
                preparedStatement.setString(4, itemBrand);
                if (suppID.isEmpty()) {
                    preparedStatement.setNull(5, java.sql.Types.VARCHAR);
                } else {
                    preparedStatement.setString(5, suppID);
                }
                preparedStatement.setDouble(6, price);
                preparedStatement.setString(7, id);
    
                int rowsAffected = preparedStatement.executeUpdate();
    
                if (rowsAffected > 0) {
                    JOptionPane.showMessageDialog(null, "Product information updated successfully");
                } else {
                    JOptionPane.showMessageDialog(null, "Failed to update product information");
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Failed to update product information: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == backButton) {
            editProductFrame.dispose();
        } else if (e.getSource() == fetchButton) {
            try {
                String id = idTextField.getText();
                fetchProductFromDatabase(id);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "Invalid ID format. Please enter a valid integer ID.");
            }
        } else if (e.getSource() == saveButton) {
            try {
                String id = idTextField.getText();
                String category = categoryTextField.getText();
                String itemName = itemNameTextField.getText();
                String itemDesc = itemDescTextField.getText();
                String itemBrand = itemBrandTextField.getText();
                String suppID = suppIDTextField.getText();
                double price = Double.parseDouble(priceTextField.getText());
                saveProductToDatabase(id, category, itemName, itemDesc, itemBrand, suppID, price);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "Invalid price format. Please enter valid numbers.");
            }
        }
    }
}