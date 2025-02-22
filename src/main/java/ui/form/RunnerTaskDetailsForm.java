package ui.form;

import javax.swing.*;

import dto.AddressDTO;
import dto.OrderDTO;
import dto.RunnerDTO;
import dto.TaskDTO;
import dto.VendorDTO;
import enumeration.OrderStatus;
import enumeration.ResponseCode;
import enumeration.TaskStatus;
import service.address.AddressService;
import service.general.SessionControlService;
import service.order.OrderService;
import service.runner.RunnerService;
import service.task.TaskService;
import service.vendor.VendorService;

import java.awt.*;

public class RunnerTaskDetailsForm extends JFrame {
    private JLabel orderIdLabel, vendorNameLabel, deliveryFeeLabel, taskDetailsLabel;
    private JTextArea vendorAddressArea, customerAddressArea;
    private JButton acceptButton, declineButton;

    private String vendorAddress;

    public RunnerTaskDetailsForm(TaskDTO task) {
        setTitle("Runner Task Details");
        setSize(400, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        getContentPane().setLayout(new GridLayout(7, 2, 5, 5));

        add(createCenteredLabel("Order ID:"));
        orderIdLabel = new JLabel(task.getOrderId());
        getContentPane().add(orderIdLabel);

        add(createCenteredLabel("Vendor Name:"));
        vendorNameLabel = new JLabel(getVendorName(task.getOrderId()));
        getContentPane().add(vendorNameLabel);

        add(createCenteredLabel("Vendor Address:"));
        vendorAddressArea = new JTextArea(getVendorAddress());
        vendorAddressArea.setLineWrap(true);
        vendorAddressArea.setWrapStyleWord(true);
        vendorAddressArea.setEditable(false);
        JScrollPane vendorScrollPane = new JScrollPane(vendorAddressArea);
        getContentPane().add(vendorScrollPane);

        add(createCenteredLabel("Customer Address:"));
        customerAddressArea = new JTextArea(getCustomerAddress(task.getCustomerAddress()));
        customerAddressArea.setLineWrap(true);
        customerAddressArea.setWrapStyleWord(true);
        customerAddressArea.setEditable(false);
        JScrollPane customerScrollPane = new JScrollPane(customerAddressArea);
        getContentPane().add(customerScrollPane);

        add(createCenteredLabel("Delivery Fee:"));
        deliveryFeeLabel = new JLabel("RM " + task.getDeliveryFee());
        getContentPane().add(deliveryFeeLabel);

        add(createCenteredLabel("Task Details:"));
        taskDetailsLabel = new JLabel(task.getTaskDetails());
        getContentPane().add(taskDetailsLabel);

        acceptButton = new JButton("Accept");
        acceptButton.addActionListener(e -> acceptTask(task.getId()));
        declineButton = new JButton("Decline");
        declineButton.addActionListener(e -> declineTask(task.getId()));
        getContentPane().add(acceptButton);
        getContentPane().add(declineButton);

        setLocationRelativeTo(null);
        setVisible(true);
    }

    private JLabel createCenteredLabel(String text) {
        JLabel label = new JLabel(text);
        label.setHorizontalAlignment(SwingConstants.CENTER);
        return label;
    }

    // Method to get vendor name
    private String getVendorName(String orderId) {
        OrderDTO order = OrderService.readOrder(orderId);
        VendorDTO vendor =  VendorService.readVendor(order.getVendorId());

        vendorAddress = vendor.getAddressId();
        
        return vendor.getVendorName();
    }

    // Method to get vendor address
    private String getVendorAddress() {
        AddressDTO address = AddressService.getAddressById(vendorAddress);
        return AddressService.concatAddress(address);
    }

    // Method to get customer address
    private String getCustomerAddress(String customerAddress) {
        AddressDTO address = AddressService.getAddressById(customerAddress);
        return AddressService.concatAddress(address);
    }

    // Method to accept task
    private void acceptTask(String taskId) {
        TaskDTO task = TaskService.readTask(taskId);
        RunnerDTO runner = RunnerService.readRunner(SessionControlService.getUser().getId());

        // Check if runner is already assigned to a task
        if (runner.getCurrentTask() != null) {
            JOptionPane.showMessageDialog(null, "Runner is already assigned to a task", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Check if task is already cancelled or completed
        if (task.getStatus() == TaskStatus.CANCELLED) {
            JOptionPane.showMessageDialog(null, "Task is already cancelled", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (task.getStatus() == TaskStatus.COMPLETED) {
            JOptionPane.showMessageDialog(null, "Task is already completed", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Update task status
        task.setStatus(TaskStatus.ACCEPTED);
        ResponseCode response = TaskService.updateTask(task);
        if (response != ResponseCode.SUCCESS) {
            JOptionPane.showMessageDialog(null, "Failed to accept task", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Update runner's current task
        runner.setCurrentTask(taskId);
        response = RunnerService.updateRunner(runner);
        if (response != ResponseCode.SUCCESS) {
            JOptionPane.showMessageDialog(null, "Failed to accept task", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Update order status
        OrderDTO order = OrderService.readOrder(task.getOrderId());
        order.setStatus(OrderStatus.RUNNER_ASSIGNED);
        response = OrderService.updateOrder(order);
        if (response != ResponseCode.SUCCESS) {
            JOptionPane.showMessageDialog(null, "Failed to accept task", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        JOptionPane.showMessageDialog(null, "Task accepted successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
        dispose();
    }

    // Method to decline task
    private void declineTask(String taskId) {
        TaskDTO task = TaskService.readTask(taskId);

        // Check if task is already cancelled or completed
        if (task.getStatus() == TaskStatus.CANCELLED) {
            JOptionPane.showMessageDialog(null, "Task is already cancelled", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (task.getStatus() == TaskStatus.COMPLETED) {
            JOptionPane.showMessageDialog(null, "Task is already completed", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Update task status
        task.setRunnerId("");
        ResponseCode response = TaskService.updateTask(task);
        if (response == ResponseCode.SUCCESS) {
            JOptionPane.showMessageDialog(null, "Task declined successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(null, "Failed to decline task", "Error", JOptionPane.ERROR_MESSAGE);
        }

        dispose();
    }
}

