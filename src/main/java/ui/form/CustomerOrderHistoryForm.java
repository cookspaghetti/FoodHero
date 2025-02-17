package ui.form;

import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

import dto.OrderDTO;
import service.vendor.VendorService;

public class CustomerOrderHistoryForm extends JFrame {
    public CustomerOrderHistoryForm(OrderDTO order) {
        setTitle("Order Details");
        setSize(500, 550);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        getContentPane().setLayout(new GridLayout(12, 2, 5, 5));

        getContentPane().add(new JLabel("Order ID:"));
        getContentPane().add(new JLabel(order.getId()));

        getContentPane().add(new JLabel("Customer ID:"));
        getContentPane().add(new JLabel(order.getCustomerId()));
        
        getContentPane().add(new JLabel("Vendor Name:"));
        getContentPane().add(new JLabel(VendorService.readVendor(order.getVendorId()).getVendorName()));
        
        getContentPane().add(new JLabel("Items:"));
        getContentPane().add(new JLabel(order.getItems().toString()));

        getContentPane().add(new JLabel("Total Amount:"));
        getContentPane().add(new JLabel(String.valueOf(order.getTotalAmount())));

        getContentPane().add(new JLabel("Placement Time:"));
        getContentPane().add(new JLabel(order.getPlacementTime().toString()));

        getContentPane().add(new JLabel("Completion Time:"));
        getContentPane().add(new JLabel(order.getCompletionTime().toString()));
        
        getContentPane().add(new JLabel("Notes:"));
        getContentPane().add(new JLabel(order.getNotes()));

        getContentPane().add(new JLabel("Status:"));
        getContentPane().add(new JLabel(order.getStatus().toString()));
        
        getContentPane().add(new JLabel("Ratings:"));
        getContentPane().add(new JLabel(String.valueOf(5)));
//        getContentPane().add(new JLabel(String.valueOf(order.getRating())));
        
        getContentPane().add(new JLabel("Review:"));
//        getContentPane().add(new JLabel(order.getReview()));
        getContentPane().add(new JLabel("It's GOOOOOD!"));
        
        JButton cancelButton = new JButton("Rate");
        JButton closeButton = new JButton("Reorder");
        getContentPane().add(cancelButton);
        getContentPane().add(closeButton);

        setLocationRelativeTo(null);
        setVisible(true);
    }
}
