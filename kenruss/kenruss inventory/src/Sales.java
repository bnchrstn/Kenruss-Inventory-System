import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class Sales implements ActionListener {

    JFrame salesWindowFrame = new JFrame();
    JPanel backPanel = new JPanel();
    JPanel upPanel = new JPanel();
    JPanel lowPanel = new JPanel();
    JTable table = new JTable();

    JLabel titleLabel = new JLabel("Sales");

    JButton backButton = new RoundedButton("Back");

    Sales() {
        salesWindowFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        salesWindowFrame.setSize(1100, 750);
        salesWindowFrame.setLayout(null);
        salesWindowFrame.setResizable(false);
        salesWindowFrame.setLocationRelativeTo(null);
        salesWindowFrame.setVisible(true);

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
        titleLabel.setBounds(460, 25, 800, 46);
        titleLabel.setForeground(Color.white);

        backButton.setBounds(950, 640, 100, 40);
        backButton.setBackground(new Color(4, 76, 172));
        backButton.setFont(new Font("Verdana", Font.BOLD, 20));
        backButton.addActionListener(this);

        salesWindowFrame.add(backButton);
        salesWindowFrame.add(titleLabel);
        salesWindowFrame.add(upPanel);
        salesWindowFrame.add(lowPanel);
        salesWindowFrame.add(backPanel);

        DefaultTableModel model = new DefaultTableModel();
        table.setModel(model);
        database(model);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBounds(10, 30, 1038, 500);
        lowPanel.add(scrollPane);
    }

    private void database(DefaultTableModel model) {
        String serverName = "MSI";
        String databaseName = "kenruss_db";
        String url = "jdbc:sqlserver://" + serverName + ";databaseName=" + databaseName + ";integratedSecurity=true;encrypt=true;trustServerCertificate=true";
    
        try (Connection connection = DriverManager.getConnection(url);
             Statement statement = connection.createStatement()) {
    
                String query = "SELECT s.sales_id, p.prod_id, p.category, p.item_name, p.item_desc, p.item_brand, s.num_of_sales, sp.supp_company, s.last_bought " +
                "FROM Sales s " +
                "INNER JOIN Products p ON s.prod_id = p.prod_id " +
                "LEFT JOIN Supplier sp ON p.supp_id = sp.supp_id " + 
                "ORDER BY s.num_of_sales DESC";
            ResultSet resultSet = statement.executeQuery(query);
    
            while (resultSet.next()) {
                String salesId = resultSet.getString("sales_id");
                String prodId = resultSet.getString("prod_id");
                int numOfSales = resultSet.getInt("num_of_sales");
                String category = resultSet.getString("category");
                String itemName = resultSet.getString("item_name");
                String itemDesc = resultSet.getString("item_desc");
                String itemBrand = resultSet.getString("item_brand");
                String suppName = resultSet.getString("supp_company"); 
                java.util.Date lastBought = resultSet.getDate("last_bought");
    
                if (model.getColumnCount() == 0) {
                    Vector<String> columnNames = new Vector<>();
                    columnNames.add("Sales ID");
                    columnNames.add("Product ID");
                    columnNames.add("Number of Sales");
                    columnNames.add("Category");
                    columnNames.add("Item Name");
                    columnNames.add("Item Description");
                    columnNames.add("Item Brand");
                    columnNames.add("Supplier Name");
                    columnNames.add("Last Bought");
                    model.setColumnIdentifiers(columnNames);
                }
    
                boolean found = false;
                for (int i = 0; i < model.getRowCount(); i++) {
                    if (model.getValueAt(i, 1).equals(prodId)) {
                        int currentNumOfSales = (int) model.getValueAt(i, 6);
                        model.setValueAt(currentNumOfSales + numOfSales, i, 6);
                        found = true;
                        break;
                    }
                }
    
                if (!found) {
                    Vector<Object> row = new Vector<>();
                    row.add(salesId);
                    row.add(prodId);
                    row.add(numOfSales);
                    row.add(category);
                    row.add(itemName);
                    row.add(itemDesc);
                    row.add(itemBrand);
                    row.add(suppName);
                    row.add(lastBought);
                    model.addRow(row);
                }
            }
        } catch (SQLException e) {
            System.out.println("Failed to connect to the database");
            e.printStackTrace();
        }
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == backButton) {
            salesWindowFrame.dispose();
            HomePage homePage = new HomePage();
        }
    }
}