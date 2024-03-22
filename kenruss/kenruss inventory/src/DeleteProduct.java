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

public class DeleteProduct implements ActionListener {

    JFrame deleteProductFrame = new JFrame();
    JPanel backPanel = new JPanel();

    JLabel titleLabel = new JLabel("Delete Product:");
    JLabel enterIDLabel = new JLabel("Enter ID:");
    JTextField enterIDTextField = new JTextField();

    JButton deleteButton = new RoundedButton("Delete");
    JButton backButton = new RoundedButton("Back");

    DeleteProduct() {
        deleteProductFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        deleteProductFrame.setSize(300, 230);
        deleteProductFrame.setLayout(null);
        deleteProductFrame.setResizable(false);
        deleteProductFrame.setLocationRelativeTo(null);
        deleteProductFrame.setVisible(true);

        backPanel.setLayout(null);
        backPanel.setBackground(new Color(84, 116, 251));
        backPanel.setBounds(0, 0, 400, 300);

        titleLabel.setFont(new Font("Verdana", Font.BOLD, 20));
        titleLabel.setBounds(50, 20, 200, 40);
        titleLabel.setForeground(Color.white);

        enterIDLabel.setBounds(50, 70, 100, 20);
        enterIDTextField.setBounds(50, 100, 200, 20);

        deleteButton.setBounds(30, 150, 80, 30);
        deleteButton.setBackground(new Color(4, 76, 172));
        deleteButton.setFont(new Font("Verdana", Font.BOLD, 10));
        deleteButton.addActionListener(this);

        backButton.setBounds(170, 150, 80, 30);
        backButton.setBackground(new Color(4, 76, 172));
        backButton.setFont(new Font("Verdana", Font.BOLD, 10));
        backButton.addActionListener(this);

        deleteProductFrame.add(titleLabel);
        deleteProductFrame.add(enterIDLabel);
        deleteProductFrame.add(enterIDTextField);
        deleteProductFrame.add(deleteButton);
        deleteProductFrame.add(backButton);
        deleteProductFrame.add(backPanel);
    }

    private void deleteProductFromDatabase(String id) {
        String serverName = "MSI";
        String databaseName = "kenruss_db";
        String url = "jdbc:sqlserver://" + serverName + ";databaseName=" + databaseName + ";integratedSecurity=true;encrypt=true;trustServerCertificate=true";
    
        String deleteQuery = "DELETE FROM Products WHERE prod_id = ?";
        
        Connection connection = null;
    
        try {
            connection = DriverManager.getConnection(url);
            connection.setAutoCommit(false); 
    
            try (PreparedStatement deleteStatement = connection.prepareStatement(deleteQuery)) {
                deleteStatement.setString(1, id);
                int rowsAffected = deleteStatement.executeUpdate();
    
                if (rowsAffected > 0) {
                    deleteRelatedRows(connection, id);
    
                    connection.commit(); 
                    JOptionPane.showMessageDialog(null, "Product deleted successfully for ID: " + id);
                    deleteProductFrame.dispose();
                } else {
                    JOptionPane.showMessageDialog(null, "ID not found in the database");
                }
            }
        } catch (SQLException e) {
            try {
                if (connection != null) {
                    connection.rollback(); 
                }
            } catch (SQLException rollbackException) {
                rollbackException.printStackTrace();
            }
            JOptionPane.showMessageDialog(null, "Failed to delete product: " + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException closeException) {
                closeException.printStackTrace();
            }
        }
    }
    
    private void deleteRelatedRows(Connection connection, String productId) throws SQLException {
        String[] tables = {"BestSellers", "Sales", "LowStocks"};
    
        for (String table : tables) {
            String deleteQuery = "DELETE FROM " + table + " WHERE prod_id = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(deleteQuery)) {
                preparedStatement.setString(1, productId);
                preparedStatement.executeUpdate();
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == backButton) {
            deleteProductFrame.dispose();
        } else if (e.getSource() == deleteButton) {
            String id = enterIDTextField.getText();
            deleteProductFromDatabase(id);
        }
    }
}