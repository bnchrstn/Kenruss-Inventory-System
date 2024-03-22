import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

public class ViewSupplier implements ActionListener {

    JFrame supplierWindowFrame = new JFrame();
    JPanel backPanel = new JPanel();
    JPanel upPanel = new JPanel();
    JPanel lowPanel = new JPanel();

    JLabel titleLabel = new JLabel("ViewSupplier");
    JTable table = new JTable();
    DefaultTableModel model;
    JTextField searchField = new JTextField();
    JButton clearButton = new JButton("Clear Search");
    JButton addButton = new RoundedButton("Add");
    JButton deleteButton = new RoundedButton("Delete");
    JButton backButton = new RoundedButton("Back");
    JButton updateButton = new RoundedButton("Update");

    JTextField supplierIDField = new JTextField();
    JTextField companyField = new JTextField();
    JTextField contactInfoField = new JTextField();

    ViewSupplier() {
        supplierWindowFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        supplierWindowFrame.setSize(1100, 750);
        supplierWindowFrame.setLayout(null);
        supplierWindowFrame.setResizable(false);
        supplierWindowFrame.setLocationRelativeTo(null);
        supplierWindowFrame.setVisible(true);

        backPanel.setLayout(null);
        backPanel.setBackground(new Color(4, 76, 172));
        backPanel.setBounds(0, 0, 1100, 750);

        upPanel.setLayout(null);
        upPanel.setBackground(new Color(84, 116, 251));
        upPanel.setBounds(15, 0, 1058, 95);

        lowPanel.setLayout(null);
        lowPanel.setBackground(new Color(141, 189, 255));
        lowPanel.setBounds(15, 95, 1058, 605);

        titleLabel.setFont(new Font("Verdana", Font.BOLD, 46));
        titleLabel.setBounds(395, 25, 800, 46);
        titleLabel.setForeground(Color.white);

        backButton.setBounds(950, 630, 100, 40);
        backButton.setBackground(new Color(4, 76, 172));
        backButton.setFont(new Font("Verdana", Font.BOLD, 20));
        backButton.addActionListener(this);

        addButton.setBounds(10, 537, 150, 40);
        addButton.setBackground(new Color(4, 76, 172));
        addButton.setFont(new Font("Verdana", Font.BOLD, 20));
        addButton.addActionListener(this);

        deleteButton.setBounds(170, 537, 150, 40);
        deleteButton.setBackground(new Color(4, 76, 172));
        deleteButton.setFont(new Font("Verdana", Font.BOLD, 20));
        deleteButton.addActionListener(this);

        updateButton.setBounds(330, 537, 150, 40);
        updateButton.setBackground(new Color(4, 76, 172));
        updateButton.setFont(new Font("Verdana", Font.BOLD, 20));
        updateButton.addActionListener(this);
        lowPanel.add(updateButton);

        table = new JTable();
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBounds(10, 50, 1038, 460);
        lowPanel.add(scrollPane);

        searchField.setBounds(10, 10, 300, 30);
        lowPanel.add(searchField);

        searchField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                search(searchField.getText());
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                search(searchField.getText());
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                search(searchField.getText());
            }
        });

        clearButton.setBounds(320, 10, 120, 30);
        clearButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                searchField.setText("");
                table.setRowSorter(null);
            }
        });
        lowPanel.add(clearButton);

        lowPanel.add(addButton); 
        lowPanel.add(deleteButton);

        supplierWindowFrame.add(backButton);
        supplierWindowFrame.add(titleLabel);
        supplierWindowFrame.add(upPanel);
        supplierWindowFrame.add(lowPanel);
        supplierWindowFrame.add(backPanel);

        Database();
    }

    private void Database() {
        String serverName = "MSI";
        String databaseName = "kenruss_db";
        String url = "jdbc:sqlserver://" + serverName + ";databaseName=" + databaseName + ";integratedSecurity=true;encrypt=true;trustServerCertificate=true";

        try (Connection connection = DriverManager.getConnection(url)) {
            System.out.println("Connected to the database");

            String sql = "SELECT * FROM Supplier "+
            "ORDER BY " +
            "ISNUMERIC(Supplier.supp_id), " +
            "Supplier.supp_id";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            model = new DefaultTableModel();

            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();
            for (int columnIndex = 1; columnIndex <= columnCount; columnIndex++) {
                model.addColumn(metaData.getColumnName(columnIndex));
            }

            while (resultSet.next()) {
                Object[] rowData = new Object[columnCount];
                for (int i = 0; i < columnCount; i++) {
                    rowData[i] = resultSet.getObject(i + 1);
                }
                model.addRow(rowData);
            }

            table.setModel(model);

        } catch (SQLException e) {
            System.out.println("Failed to connect to the database");
            e.printStackTrace();
        }
    } 

    private void search(String text) {
        TableRowSorter<DefaultTableModel> rowSorter = new TableRowSorter<>(model);
        table.setRowSorter(rowSorter);
        rowSorter.setRowFilter(RowFilter.regexFilter("(?i)" + text));

        if (table.getRowCount() == 0) {
            JOptionPane.showMessageDialog(supplierWindowFrame, "No matching results found.");
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == backButton) {
            supplierWindowFrame.dispose();
            InventoryWindow inventoryWindow = new InventoryWindow();
        } else if (e.getSource() == addButton) {
            JTextField supplierIDField = new JTextField(10);
            JTextField companyField = new JTextField(50);
            JTextField contactInfoField = new JTextField(50);
    
            JPanel addPanel = new JPanel();
            addPanel.setLayout(new GridLayout(0, 1));
            addPanel.add(new JLabel("Supplier ID:"));
            addPanel.add(supplierIDField);
            addPanel.add(new JLabel("Company:"));
            addPanel.add(companyField);
            addPanel.add(new JLabel("Contact Info:"));
            addPanel.add(contactInfoField);
    
            int result = JOptionPane.showConfirmDialog(null, addPanel, "Add Supplier",
                    JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
            if (result == JOptionPane.OK_OPTION) {
                String supplierID = supplierIDField.getText();
                String company = companyField.getText();
                String contactInfo = contactInfoField.getText();
    
                if (supplierID.isEmpty() || company.isEmpty() || contactInfo.isEmpty()) {
                    JOptionPane.showMessageDialog(supplierWindowFrame, "Please fill in all fields.");
                } else if (supplierID.length() > 10 || company.length() > 50 || contactInfo.length() > 50) {
                    JOptionPane.showMessageDialog(supplierWindowFrame, "Input exceeds maximum length.");
                } else {
                    addSupplier(supplierID, company, contactInfo);
                    clearFields();
                    Database();
                }
            }
        } else if (e.getSource() == deleteButton) {
            String supplierID = JOptionPane.showInputDialog(supplierWindowFrame, "Enter Supplier ID to delete:");
            if (supplierID != null) {
                if (supplierID.isEmpty()) {
                    JOptionPane.showMessageDialog(supplierWindowFrame, "Please enter Supplier ID."); 
                } else if (supplierID.length() > 10) {
                    JOptionPane.showMessageDialog(supplierWindowFrame, "Supplier ID exceeds maximum length.");
                } else {
                    boolean deleted = deleteSupplier(supplierID);
                    if (deleted) {
                        JOptionPane.showMessageDialog(supplierWindowFrame, "Supplier deleted successfully.");
                    } else {
                        JOptionPane.showMessageDialog(supplierWindowFrame, "Supplier ID does not exist.");
                    }
                    clearFields();
                    Database();
                }
            }
        }  else if (e.getSource() == updateButton) {
            int selectedRow = table.getSelectedRow();
            if (selectedRow != -1) {
                String currentSupplierID = (String) table.getValueAt(selectedRow, 0);
                String currentCompany = (String) table.getValueAt(selectedRow, 1);
                String currentContactInfo = (String) table.getValueAt(selectedRow, 2);

                JTextField supplierIDField = new JTextField(10);
                JTextField companyField = new JTextField(50);
                JTextField contactInfoField = new JTextField(50);
                supplierIDField.setText(currentSupplierID);
                companyField.setText(currentCompany);
                contactInfoField.setText(currentContactInfo);

                JPanel updatePanel = new JPanel();
                updatePanel.setLayout(new GridLayout(0, 1));
                updatePanel.add(new JLabel("Supplier ID:"));
                updatePanel.add(supplierIDField);
                updatePanel.add(new JLabel("Company:"));
                updatePanel.add(companyField);
                updatePanel.add(new JLabel("Contact Info:"));
                updatePanel.add(contactInfoField);

                int result = JOptionPane.showConfirmDialog(null, updatePanel, "Update Supplier Info",
                        JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
                if (result == JOptionPane.OK_OPTION) {
                    String updatedSupplierID = supplierIDField.getText();
                    String updatedCompany = companyField.getText();
                    String updatedContactInfo = contactInfoField.getText();

                    if (updatedSupplierID.isEmpty() || updatedCompany.isEmpty() || updatedContactInfo.isEmpty()) {
                        JOptionPane.showMessageDialog(supplierWindowFrame, "Please fill in all fields.");
                    } else if (updatedSupplierID.length() > 10 || updatedCompany.length() > 50 || updatedContactInfo.length() > 50) {
                        JOptionPane.showMessageDialog(supplierWindowFrame, "Input exceeds maximum length.");
                    } else {
                        updateSupplier(currentSupplierID, updatedSupplierID, updatedCompany, updatedContactInfo);
                        clearFields();
                        Database();
                    }
                }
            } else {
                JOptionPane.showMessageDialog(supplierWindowFrame, "Please select a supplier to update.");
            }
        }
    }
    
    
    private void addSupplier(String supplierID, String company, String contactInfo) {
        String serverName = "MSI";
        String databaseName = "kenruss_db";
        String url = "jdbc:sqlserver://" + serverName + ";databaseName=" + databaseName + ";integratedSecurity=true;encrypt=true;trustServerCertificate=true";
    
        try (Connection connection = DriverManager.getConnection(url)) {
            System.out.println("Connected to the database");
    
            String checkIfExistsQuery = "SELECT COUNT(*) FROM Supplier WHERE supp_id = ?";
            PreparedStatement checkIfExistsStatement = connection.prepareStatement(checkIfExistsQuery);
            checkIfExistsStatement.setString(1, supplierID);
            ResultSet resultSet = checkIfExistsStatement.executeQuery();
            resultSet.next();
            int count = resultSet.getInt(1);
    
            if (count > 0) {
                JOptionPane.showMessageDialog(supplierWindowFrame, "Supplier ID already exists. Please choose a different one.");
                return; 
            }
    
            String sql = "INSERT INTO Supplier (supp_id, supp_company, contact_info) VALUES (?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, supplierID);
            statement.setString(2, company);
            statement.setString(3, contactInfo);
            statement.executeUpdate();
    
        } catch (SQLException ex) {
            System.out.println("Failed to add supplier to the database");
            ex.printStackTrace();
        }
    }
    
    private boolean deleteSupplier(String supplierID) {
        String serverName = "MSI";
        String databaseName = "kenruss_db";
        String url = "jdbc:sqlserver://" + serverName + ";databaseName=" + databaseName + ";integratedSecurity=true;encrypt=true;trustServerCertificate=true";
    
        try (Connection connection = DriverManager.getConnection(url)) {
            System.out.println("Connected to the database");

            String deleteSupplierSQL = "DELETE FROM Supplier WHERE supp_id = ?"; 
            PreparedStatement deleteSupplierStatement = connection.prepareStatement(deleteSupplierSQL);
            deleteSupplierStatement.setString(1, supplierID);
            int rowsAffected = deleteSupplierStatement.executeUpdate();
    
            if (rowsAffected > 0) {
                String deleteProductSupplierSQL = "UPDATE Products SET supp_id = NULL WHERE supp_id = ?";
                PreparedStatement deleteProductSupplierStatement = connection.prepareStatement(deleteProductSupplierSQL);
                deleteProductSupplierStatement.setString(1, supplierID);
                deleteProductSupplierStatement.executeUpdate();
            }
    
            return rowsAffected > 0;
        } catch (SQLException ex) {
            System.out.println("Failed to delete supplier from the database");
            ex.printStackTrace();
            return false;
        }
    }

    private void updateSupplier(String currentSupplierID, String updatedSupplierID, String updatedCompany, String updatedContactInfo) {
        String serverName = "MSI";
        String databaseName = "kenruss_db";
        String url = "jdbc:sqlserver://" + serverName + ";databaseName=" + databaseName + ";integratedSecurity=true;encrypt=true;trustServerCertificate=true";
    
        try (Connection connection = DriverManager.getConnection(url)) {
            System.out.println("Connected to the database");
 
            String checkIfExistsQuery = "SELECT COUNT(*) FROM Supplier WHERE supp_id = ?";
            PreparedStatement checkIfExistsStatement = connection.prepareStatement(checkIfExistsQuery);
            checkIfExistsStatement.setString(1, updatedSupplierID);
            ResultSet resultSet = checkIfExistsStatement.executeQuery();
            resultSet.next();
            int count = resultSet.getInt(1);
    
            if (count > 0 && !currentSupplierID.equals(updatedSupplierID)) {
                JOptionPane.showMessageDialog(supplierWindowFrame, "Updated Supplier ID already exists. Please choose a different one.");
                return; 
            }
    
            String updateSupplierQuery = "UPDATE Supplier SET supp_id = ?, supp_company = ?, contact_info = ? WHERE supp_id = ?";
            PreparedStatement updateSupplierStatement = connection.prepareStatement(updateSupplierQuery);
            updateSupplierStatement.setString(1, updatedSupplierID);
            updateSupplierStatement.setString(2, updatedCompany);
            updateSupplierStatement.setString(3, updatedContactInfo);
            updateSupplierStatement.setString(4, currentSupplierID);
            updateSupplierStatement.executeUpdate();
    
            String updateProductsQuery = "UPDATE Products SET supp_id = ? WHERE supp_id = ?";
            PreparedStatement updateProductsStatement = connection.prepareStatement(updateProductsQuery);
            updateProductsStatement.setString(1, updatedSupplierID);
            updateProductsStatement.setString(2, currentSupplierID);
            updateProductsStatement.executeUpdate();
    
        } catch (SQLException ex) {
            System.out.println("Failed to update supplier info in the database");
            ex.printStackTrace();
        }
    }
    
        private void clearFields() {
            supplierIDField.setText("");
            companyField.setText("");
            contactInfoField.setText("");
        }
    
    }
