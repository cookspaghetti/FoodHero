package ui.form;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.concurrent.CompletableFuture;

import dto.OrderDTO;
import enumeration.OrderStatus;
import enumeration.ResponseCode;
import service.order.OrderService;
import service.processor.ItemProcessor;
import service.vendor.VendorService;

public class CustomerOrderDetailsForm extends JFrame {
    public CustomerOrderDetailsForm(OrderDTO order) {
        setTitle("Order Details");
        setSize(500, 450);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        getContentPane().setLayout(new GridLayout(8, 2, 5, 5));

        getContentPane().add(new JLabel("Order ID:"));
        getContentPane().add(new JLabel(order.getId()));

        getContentPane().add(new JLabel("Vendor Name:"));
        getContentPane().add(new JLabel(VendorService.readVendor(order.getVendorId()).getVendorName()));

        getContentPane().add(new JLabel("Items:"));
        getContentPane().add(new JLabel(processItemList(order.getVendorId(), order.getItems())));

        getContentPane().add(new JLabel("Notes:"));
        getContentPane().add(new JLabel(order.getNotes()));

        getContentPane().add(new JLabel("Total Amount:"));
        getContentPane().add(new JLabel("RM " + String.valueOf(order.getTotalAmount())));

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
            // Call the service to update the order status
            ResponseCode response = OrderService.updateOrder(order.getVendorId(), order);
            if (response == ResponseCode.SUCCESS) {
                JOptionPane.showMessageDialog(this, "Order has been cancelled.");
            } else {
                JOptionPane.showMessageDialog(this, "Failed to cancel order.");
            }
            dispose();
        }
    }

    private String processItemList(String vendorId, HashMap<String, Integer> items) {
        try {
            CompletableFuture<String> itemFuture = ItemProcessor.processItemListAsync(vendorId, items);
            return itemFuture.join();
        } catch (Exception e) {
            System.err.println("Error processing item list: " + e.getMessage());
            return "Error";
        }
    }
}
