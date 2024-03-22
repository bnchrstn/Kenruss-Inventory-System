import java.awt.*;
import java.awt.event.*;
import javax.swing.*;


public class HomePage implements ActionListener {

    JFrame homePageFrame = new JFrame();
    JPanel backPanel = new JPanel();
    JPanel upPanel = new JPanel();
    JPanel lowPanel = new JPanel();

    JLabel titleLabel = new JLabel("KENRUSS MOTOR PARTS");
    JLabel imageLabel; // declaration for the image label
 
    JButton viewInventoryButton = new RoundedButton("View Inventory");
    JButton stockButton = new RoundedButton("Update Stock");
    JButton tableButton = new RoundedButton("Sales");
    JButton exitButton = new RoundedButton("Exit");

    HomePage() {
        homePageFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        homePageFrame.setSize(1100, 750);
        homePageFrame.setLayout(null);
        homePageFrame.setResizable(false);
        homePageFrame.setLocationRelativeTo(null);
        homePageFrame.setVisible(true);

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
        titleLabel.setBounds(220,25 , 800, 46);
        titleLabel.setForeground(Color.white);

         // loads image using relative path 
         ImageIcon imageIcon = new ImageIcon("kenruss.png"); 
         // resize image to fit in the label
         Image image = imageIcon.getImage().getScaledInstance(700, 700, Image.SCALE_SMOOTH);
         imageIcon = new ImageIcon(image);
         // create JLabel with the image
         imageLabel = new JLabel(imageIcon);
         imageLabel.setBounds(460, 130, 500, 500);

        viewInventoryButton.setBounds(100, 200, 250, 70);
        stockButton.setBounds(100, 320, 250, 70);
        tableButton.setBounds(100, 440, 250, 70);
        exitButton.setBounds(950, 640, 100, 40);

        viewInventoryButton.setBackground(new Color(4, 76, 172));
        stockButton.setBackground(new Color(4, 76, 172));
        tableButton.setBackground(new Color(4, 76, 172));
        exitButton.setBackground(new Color(4, 76, 172));

        viewInventoryButton.setFont(new Font("Verdana", Font.CENTER_BASELINE, 20));
        stockButton.setFont(new Font("Verdana", Font.CENTER_BASELINE, 20));
        tableButton.setFont(new Font("Verdana", Font.CENTER_BASELINE, 20));
        exitButton.setFont(new Font("Verdana", Font.CENTER_BASELINE, 20));

        viewInventoryButton.addActionListener(this);
        stockButton.addActionListener(this);
        tableButton.addActionListener(this);
        exitButton.addActionListener(this);

        homePageFrame.add(imageLabel);
        homePageFrame.add(viewInventoryButton);
        homePageFrame.add(stockButton);
        homePageFrame.add(tableButton);
        homePageFrame.add(exitButton);
        homePageFrame.add(titleLabel);
        homePageFrame.add(upPanel);
        homePageFrame.add(lowPanel);
        homePageFrame.add(backPanel);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == exitButton) {
            System.exit(0);
        }
        else if (e.getSource() == viewInventoryButton) {
            homePageFrame.dispose();
            InventoryWindow inventoryWindow = new InventoryWindow();
        }
        else if (e.getSource() == stockButton) {
            homePageFrame.dispose();
            StockWindow stockWindow = new StockWindow();
        }
        else if (e.getSource() == tableButton) {
            homePageFrame.dispose();
            Sales sales = new Sales();
        }
    }
}
