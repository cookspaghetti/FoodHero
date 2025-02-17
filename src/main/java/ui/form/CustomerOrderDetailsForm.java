package ui.form;

import javax.swing.*;
import java.awt.*;

import dto.OrderDTO;
import enumeration.OrderStatus;

public class CustomerOrderDetailsForm extends JFrame {
    public CustomerOrderDetailsForm(OrderDTO order) {
        setTitle("Order Details");
        setSize(500, 450);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        getContentPane().setLayout(new GridLayout(8, 2, 5, 5));

        getContentPane().add(new JLabel("Order ID:"));
        getContentPane().add(new JLabel(order.getId()));

        getContentPane().add(new JLabel("Vendor Name:"));
        getContentPane().add(new JLabel(order.getCustomerId()));

        getContentPane().add(new JLabel("Items:"));
        getContentPane().add(new JLabel(order.getItems().toString()));

        getContentPane().add(new JLabel("Notes:"));
        getContentPane().add(new JLabel(order.getNotes()));

        getContentPane().add(new JLabel("Total Amount:"));
        getContentPane().add(new JLabel(String.valueOf(order.getTotalAmount())));

        getContentPane().add(new JLabel("Placement Time:"));
        getContentPane().add(new JLabel(order.getPlacementTime().toString()));

        getContentPane().add(new JLabel("Status:"));
        getContentPane().add(new JLabel(order.getStatus().toString()));

        JButton cancelButton = new JButton("Cancel");
        JButton closeButton = new JButton("Close");
        getContentPane().add(cancelButton);
        getContentPane().add(closeButton);

        cancelButton.addActionListener(e -> cancelOrder(order));
        closeButton.addActionListener(e -> dispose());

        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void cancelOrder(OrderDTO order) {
        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to cancel this order?", "Confirm Cancellation", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            order.setStatus(OrderStatus.CANCELLED);
            JOptionPane.showMessageDialog(this, "Order has been cancelled.");
            dispose();
        }
    }
}
