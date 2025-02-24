package ui.form;

import javax.swing.*;

import dto.AddTransactionDTO;
import dto.CustomerDTO;
import dto.NotificationDTO;
import dto.OrderDTO;
import enumeration.OrderStatus;
import enumeration.ResponseCode;
import enumeration.ServiceType;
import service.customer.CustomerService;
import service.notification.NotificationService;
import service.order.OrderService;
import service.processor.ItemProcessor;
import service.task.TaskService;
import service.transaction.TransactionService;
import service.utils.IdGenerationUtils;

import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.concurrent.CompletableFuture;

public class VendorOrderDetailsForm extends JFrame {
    private JTextField idField, customerIdField, totalAmountField, placementTimeField, notesField;
    private JTextArea itemField;
    private JComboBox<OrderStatus> statusComboBox;
    private JButton saveButton, cancelButton;

    public VendorOrderDetailsForm(OrderDTO order) {
        setTitle("Order Details");
        setSize(500, 450);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        getContentPane().setLayout(new GridLayout(8, 2, 5, 5));

        getContentPane().add(new JLabel("Order ID:"));
        idField = new JTextField(order.getId());
        idField.setEditable(false);
        getContentPane().add(idField);

        getContentPane().add(new JLabel("Customer ID:"));
        customerIdField = new JTextField(order.getCustomerId());
        customerIdField.setEditable(false);
        getContentPane().add(customerIdField);
        
        getContentPane().add(new JLabel("Items:"));
        itemField = new JTextArea(processItemList(order.getVendorId(), order.getItems()));
        itemField.setEditable(false);
        getContentPane().add(itemField);

        getContentPane().add(new JLabel("Notes:"));
        notesField = new JTextField(order.getNotes());
        notesField.setEditable(false);
        getContentPane().add(notesField);
        
        getContentPane().add(new JLabel("Total Amount:"));
        totalAmountField = new JTextField(String.valueOf(order.getTotalAmount()));
        totalAmountField.setEditable(false);
        getContentPane().add(totalAmountField);

        getContentPane().add(new JLabel("Placement Time:"));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        placementTimeField = new JTextField(order.getPlacementTime().format(formatter));
        placementTimeField.setEditable(false);
        getContentPane().add(placementTimeField);

        getContentPane().add(new JLabel("Status:"));
        statusComboBox = new JComboBox<>(OrderStatus.values());
        statusComboBox.setSelectedItem(order.getStatus());
        getContentPane().add(statusComboBox);

        saveButton = new JButton("Save");
        cancelButton = new JButton("Cancel");
        getContentPane().add(saveButton);
        getContentPane().add(cancelButton);

        saveButton.addActionListener(e -> updateOrder(order));
        cancelButton.addActionListener(e -> dispose());

        setLocationRelativeTo(null);
        setVisible(true);
    }

    // Method to update the order status
    private void updateOrder(OrderDTO order) {
        // Get the latest order details
        OrderDTO latestOrder = OrderService.readOrder(order.getId());

        // Check if the order is changed
        if (latestOrder.getStatus() == (OrderStatus) statusComboBox.getSelectedItem()) {
            JOptionPane.showMessageDialog(this, "No changes made to the order status.");
            return;
        }
        
        // Check order status
        if (latestOrder.getStatus() == OrderStatus.CANCELLED || latestOrder.getStatus() == OrderStatus.FAILED || latestOrder.getStatus() == OrderStatus.DELIVERED) {
            JOptionPane.showMessageDialog(this, "Order is already cancelled, failed or delivered.");
            return;
        }

        // Check invalid status change
        if (latestOrder.getStatus() == OrderStatus.PENDING && (OrderStatus) statusComboBox.getSelectedItem() == OrderStatus.DELIVERED) {
            JOptionPane.showMessageDialog(this, "Invalid status change.");
            return;
        }
        
        // Method to cancel order
        if ((OrderStatus) statusComboBox.getSelectedItem() == OrderStatus.CANCELLED && latestOrder.getStatus() == OrderStatus.PENDING) {
            int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to cancel the order?", "Cancel Order", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                // Update order status
                latestOrder.setStatus(OrderStatus.CANCELLED);
                ResponseCode response = OrderService.updateOrder(latestOrder);
                if (response != ResponseCode.SUCCESS) {
                    JOptionPane.showMessageDialog(this, "Failed to cancel order.");
                }
                // Delete task
                response = TaskService.deleteTask(latestOrder.getId());
                if (response != ResponseCode.SUCCESS) {
                    JOptionPane.showMessageDialog(this, "Failed to cancel task.");
                }
                // Refund customer
                AddTransactionDTO addTransaction = new AddTransactionDTO();
                addTransaction.setId(IdGenerationUtils.getNextId(ServiceType.ADD_TRANSACTION, null, null));
                addTransaction.setCustomerId(latestOrder.getCustomerId());
                addTransaction.setAmount(latestOrder.getTotalAmount());
                addTransaction.setDate(LocalDateTime.now());
                addTransaction.setDescription("Refund for order cancellation " + latestOrder.getId());
                response = TransactionService.createAddTransaction(addTransaction);
                if (response != ResponseCode.SUCCESS) {
                    JOptionPane.showMessageDialog(this, "Failed to refund customer.");
                }
                // Update customer balance
                CustomerDTO customer = CustomerService.readCustomer(latestOrder.getCustomerId());
                customer.setCredit(customer.getCredit() + latestOrder.getTotalAmount());
                response = CustomerService.updateCustomer(customer);
                if (response != ResponseCode.SUCCESS) {
                    JOptionPane.showMessageDialog(this, "Failed to update customer balance.");
                }
                JOptionPane.showMessageDialog(this, "Order has been cancelled. Refund has been made to the customer.");
                dispose();
            }
            return;
        }

        // Confirm status change
        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to change the order status?", "Confirm Status Change", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            latestOrder.setStatus((OrderStatus) statusComboBox.getSelectedItem());
            if (latestOrder.getStatus() == OrderStatus.DELIVERED || latestOrder.getStatus() == OrderStatus.DINE_IN) {
                latestOrder.setCompletionTime(LocalDateTime.now());
            } else if (latestOrder.getStatus() == OrderStatus.FAILED || latestOrder.getStatus() == OrderStatus.CANCELLED) {
                latestOrder.setCompletionTime(null);
            }
            // Call the service to update the order status
            ResponseCode response = OrderService.updateOrder(latestOrder);
            if (response == ResponseCode.SUCCESS) {
                NotificationDTO notification = new NotificationDTO();
                notification.setUserId(customerIdField.getText());
                notification.setTitle("Order Status Update");
                notification.setMessage("Your order " + latestOrder.getId() + " status has been updated to " + latestOrder.getStatus());
                notification.setRead(false);
                notification.setTimestamp(LocalDateTime.now());
                response = NotificationService.createNotification(notification);
                if (response != ResponseCode.SUCCESS) {
                    JOptionPane.showMessageDialog(this, "Failed to send notification to customer.");
                }
                JOptionPane.showMessageDialog(this, "Order status has been updated.");
            } else {
                JOptionPane.showMessageDialog(this, "Failed to update order status.");
            }
            dispose();
        }
    }

    // Method to process the list of items in the order
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

