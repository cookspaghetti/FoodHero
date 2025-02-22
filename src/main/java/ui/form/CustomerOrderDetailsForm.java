package ui.form;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.concurrent.CompletableFuture;

import dto.AddTransactionDTO;
import dto.CustomerDTO;
import dto.OrderDTO;
import enumeration.OrderStatus;
import enumeration.ResponseCode;
import enumeration.ServiceType;
import service.customer.CustomerService;
import service.order.OrderService;
import service.processor.ItemProcessor;
import service.runner.RunnerService;
import service.task.TaskService;
import service.transaction.TransactionService;
import service.utils.IdGenerationUtils;
import service.vendor.VendorService;

public class CustomerOrderDetailsForm extends JFrame {
    public CustomerOrderDetailsForm(OrderDTO order) {
        setTitle("Order Details");
        setSize(500, 495);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        getContentPane().setLayout(new GridLayout(9, 2, 5, 5));

        getContentPane().add(new JLabel("Order ID:"));
        getContentPane().add(new JLabel(order.getId()));

        getContentPane().add(new JLabel("Vendor Name:"));
        getContentPane().add(new JLabel(VendorService.readVendor(order.getVendorId()).getVendorName()));

        getContentPane().add(new JLabel("Runner Name:"));
        getContentPane().add(new JLabel(order.getRunnerId() == null ? "Not assigned"
                : RunnerService.readRunner(order.getRunnerId()).getName()));

        getContentPane().add(new JLabel("Items:"));
        getContentPane().add(new JTextArea(processItemList(order.getVendorId(), order.getItems())));

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

        cancelButton.addActionListener(e -> cancelOrder(order.getId()));
        closeButton.addActionListener(e -> dispose());

        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void cancelOrder(String orderId) {
        OrderDTO order = OrderService.readOrder(orderId);

        // Check if cancellation still possible
        if (order.getStatus() != OrderStatus.PENDING) {
            JOptionPane.showMessageDialog(this, "Order cannot be cancelled.");
            return;
        }

        // Confirm cancellation
        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to cancel this order?",
                "Confirm Cancellation", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            // Update order status
            order.setStatus(OrderStatus.CANCELLED);
            ResponseCode response = OrderService.updateOrder(order);
            if (response != ResponseCode.SUCCESS) {
                JOptionPane.showMessageDialog(this, "Failed to cancel order.");
            }
            // Delete task
            response = TaskService.deleteTask(order.getId());
            if (response != ResponseCode.SUCCESS) {
                JOptionPane.showMessageDialog(this, "Failed to cancel task.");
            }
            // Refund customer
            AddTransactionDTO addTransaction = new AddTransactionDTO();
            addTransaction.setId(IdGenerationUtils.getNextId(ServiceType.ADD_TRANSACTION, null, null));
            addTransaction.setCustomerId(order.getCustomerId());
            addTransaction.setAmount(order.getTotalAmount());
            addTransaction.setDate(LocalDateTime.now());
            addTransaction.setDescription("Refund for order cancellation " + order.getId());
            response = TransactionService.createAddTransaction(addTransaction);
            if (response != ResponseCode.SUCCESS) {
                JOptionPane.showMessageDialog(this, "Failed to refund customer.");
            }
            // Update customer balance
            CustomerDTO customer = CustomerService.readCustomer(order.getCustomerId());
            customer.setCredit(customer.getCredit() + order.getTotalAmount());
            response = CustomerService.updateCustomer(customer);
            if (response != ResponseCode.SUCCESS) {
                JOptionPane.showMessageDialog(this, "Failed to update customer balance.");
            }
            JOptionPane.showMessageDialog(this, "Order has been cancelled. Refund has been made to the customer.");
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
