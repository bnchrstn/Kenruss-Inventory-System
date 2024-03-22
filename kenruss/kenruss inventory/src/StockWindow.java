import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

public class StockWindow implements ActionListener {

    JFrame stockWindowFrame = new JFrame();
    JPanel backPanel = new JPanel();
    JPanel upPanel = new JPanel();
    JPanel lowPanel = new JPanel();

    JLabel titleLabel = new JLabel("STOCKS");
    JTable table = new JTable();
    DefaultTableModel model;

    JButton backButton = new RoundedButton("Back");
    JButton AddProd = new RoundedButton("Add Product");
    JButton DelProd = new RoundedButton("Delete Product");
    JButton AddStock = new RoundedButton("Add Stocks");
    JButton DelStock = new RoundedButton("Deduct Stocks");
    JButton EditInfo = new RoundedButton("Edit Product Info");
    JButton clearButton = new JButton("Clear Search");

    JTextField searchField = new JTextField();

    StockWindow() {
        stockWindowFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        stockWindowFrame.setSize(1100, 750);
        stockWindowFrame.setLayout(null);
        stockWindowFrame.setResizable(false);
        stockWindowFrame.setLocationRelativeTo(null);
        stockWindowFrame.setVisible(true);

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
        titleLabel.setBounds(455, 25, 800, 46);
        titleLabel.setForeground(Color.white);

        backButton.setBounds(952, 640, 100, 40);
        backButton.setBackground(new Color(4, 76, 172));
        backButton.setFont(new Font("Verdana", Font.BOLD, 20));
        backButton.addActionListener(this);

        EditInfo.setBounds(745, 640, 200, 40);
        EditInfo.setBackground(new Color(4, 76, 172));
        EditInfo.setFont(new Font("Verdana", Font.BOLD, 15));
        EditInfo.addActionListener(this);

        AddProd.setBounds(35, 640, 150, 40);
        AddProd.setBackground(new Color(4, 76, 172));
        AddProd.setFont(new Font("Verdana", Font.BOLD, 15));
        AddProd.addActionListener(this);

        DelProd.setBounds(190, 640, 180, 40);
        DelProd.setBackground(new Color(4, 76, 172));
        DelProd.setFont(new Font("Verdana", Font.BOLD, 15));
        DelProd.addActionListener(this);

        AddStock.setBounds(375, 640, 180, 40);
        AddStock.setBackground(new Color(4, 76, 172));
        AddStock.setFont(new Font("Verdana", Font.BOLD, 15));
        AddStock.addActionListener(this);

        DelStock.setBounds(560, 640, 180, 40);
        DelStock.setBackground(new Color(4, 76, 172));
        DelStock.setFont(new Font("Verdana", Font.BOLD, 15));
        DelStock.addActionListener(this);

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
                TableRowSorter<DefaultTableModel> sorter = (TableRowSorter<DefaultTableModel>) table.getRowSorter();
                if (sorter != null) {
                    sorter.setRowFilter(null);
                }
            }
        });
        lowPanel.add(clearButton);

        stockWindowFrame.add(EditInfo);
        stockWindowFrame.add(backButton);
        stockWindowFrame.add(AddProd);
        stockWindowFrame.add(DelProd);
        stockWindowFrame.add(AddStock);
        stockWindowFrame.add(DelStock);
        stockWindowFrame.add(titleLabel);
        stockWindowFrame.add(upPanel);
        stockWindowFrame.add(lowPanel);
        stockWindowFrame.add(backPanel);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBounds(10, 50, 1038, 480);
        lowPanel.add(scrollPane);

        Database();
    }

    private void Database() {
        String serverName = "MSI";
        String databaseName = "kenruss_db";
        String url = "jdbc:sqlserver://" + serverName + ";databaseName=" + databaseName + ";integratedSecurity=true;encrypt=true;trustServerCertificate=true";

        try (Connection connection = DriverManager.getConnection(url)) {
            System.out.println("Connected to the database");

            String sql = "SELECT * FROM Products " +
            "ORDER BY " +
            "ISNUMERIC(Products.prod_id), " +
            "Products.prod_id";
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
        TableRowSorter<DefaultTableModel> sorter = (TableRowSorter<DefaultTableModel>) table.getRowSorter();
        if (sorter == null) {
            sorter = new TableRowSorter<>(model);
            table.setRowSorter(sorter);
        }
        sorter.setRowFilter(RowFilter.regexFilter("(?i)" + text));

        if (table.getRowCount() == 0) {
            JOptionPane.showMessageDialog(stockWindowFrame, "No matching results found.");
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == backButton) {
            stockWindowFrame.dispose();
            HomePage homePage = new HomePage();
        } if (e.getSource() == AddStock) {
            AddStock addStock = new AddStock();
        } if (e.getSource() == DelStock) {
            DeductStock deductStock = new DeductStock();
        } if (e.getSource() == DelProd) {
            DeleteProduct deleteProduct = new DeleteProduct();
        } if (e.getSource() == AddProd) {
            AddProduct addProduct = new AddProduct();
        } if (e.getSource() == EditInfo) {
            EditProdInfo editProdInfo = new EditProdInfo();
        }
    }
}
