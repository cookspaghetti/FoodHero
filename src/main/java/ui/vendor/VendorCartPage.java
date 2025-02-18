package ui.vendor;

import javax.swing.*;
import java.awt.*;

public class VendorCartPage extends JFrame {
    private JTable cartTable;
    private JButton checkoutButton;
    
    public VendorCartPage(String vendorId) {
        setTitle("Cart");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // Header Panel
        JPanel headerPanel = new JPanel(new BorderLayout());
        JLabel titleLabel = new JLabel("Your Cart", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        headerPanel.add(titleLabel, BorderLayout.CENTER);
        add(headerPanel, BorderLayout.NORTH);
        
        // Table Data
        String[] columnNames = {"Item ID", "Item Name", "Price", "Quantity", "Total Price"};
        Object[][] data = {}; // Replace with actual cart data
        cartTable = new JTable(data, columnNames);
        JScrollPane scrollPane = new JScrollPane(cartTable);
        add(scrollPane, BorderLayout.CENTER);
        
        // Footer Panel
        JPanel footerPanel = new JPanel();
        checkoutButton = new JButton("Checkout");
        checkoutButton.addActionListener(e -> {
            handleCheckout();
        });
        footerPanel.add(checkoutButton);
        add(footerPanel, BorderLayout.SOUTH);
        
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void handleCheckout() {
        String[] options = {"Dine-In", "Takeaway", "Delivery"};
        String choice = (String) JOptionPane.showInputDialog(
                this, 
                "Select an option:", 
                "Checkout Option", 
                JOptionPane.QUESTION_MESSAGE, 
                null, 
                options, 
                options[0]
        );
        
        if (choice != null) {
            JOptionPane.showMessageDialog(this, "You selected: " + choice);
            // Implement further checkout logic based on choice
        }
    }
    
    public static void main(String[] args) {
        new VendorCartPage("V123");
    }
}
