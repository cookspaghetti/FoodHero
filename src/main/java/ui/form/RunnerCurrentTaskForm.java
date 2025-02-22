package ui.form;

import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;

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

public class RunnerCurrentTaskForm extends JFrame {
    private JLabel orderIdLabel, vendorNameLabel, deliveryFeeLabel, taskDetailsLabel;
    private JTextArea vendorAddressArea, customerAddressArea;
    private JComboBox<TaskStatus> statusComboBox;
    private JButton saveButton, closeButton;

    private String vendorAddress;

    public RunnerCurrentTaskForm(TaskDTO task) {
        setTitle("Runner Task Details");
        setSize(400, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        getContentPane().setLayout(new GridLayout(8, 2, 5, 5));

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

        add(createCenteredLabel("Task Status:"));
        statusComboBox = new JComboBox<>(TaskStatus.values());
        statusComboBox.setSelectedItem(task.getStatus());
        getContentPane().add(statusComboBox);
        
        saveButton = new JButton("Save");
        saveButton.addActionListener(e -> updateTaskStatus(task));

        closeButton = new JButton("Close");
        closeButton.addActionListener(e -> dispose());
        getContentPane().add(saveButton);
        getContentPane().add(closeButton);

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
        VendorDTO vendor = VendorService.readVendor(order.getVendorId());

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

    // Method to update task status
    private void updateTaskStatus(TaskDTO task) {
        // Get latest task details and runner details
        TaskDTO latestTask = TaskService.readTask(task.getId());
        RunnerDTO runner = RunnerService.readRunner(SessionControlService.getUser().getId());

        // Check if task status is changed
        if (task.getStatus() == (TaskStatus) statusComboBox.getSelectedItem()) {
            JOptionPane.showMessageDialog(null, "No changes made", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Check invalid status selected
        if (statusComboBox.getSelectedItem() != TaskStatus.IN_PROGRESS && statusComboBox.getSelectedItem() != TaskStatus.COMPLETED) {
            JOptionPane.showMessageDialog(null, "Invalid status selected", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Check if task is already cancelled or completed
        if (latestTask.getStatus() == TaskStatus.CANCELLED || task.getStatus() == TaskStatus.CANCELLED) {
            JOptionPane.showMessageDialog(null, "Task is already cancelled", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (latestTask.getStatus() == TaskStatus.COMPLETED || task.getStatus() == TaskStatus.COMPLETED) {
            JOptionPane.showMessageDialog(null, "Task is already completed", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        task.setStatus((TaskStatus) statusComboBox.getSelectedItem());
        // Update task status
        ResponseCode response = TaskService.updateTask(task);
        if (response != ResponseCode.SUCCESS) {
            JOptionPane.showMessageDialog(null, "Failed to update task status", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Method to update order status and runner's current task if task is completed
        if (statusComboBox.getSelectedItem() == TaskStatus.COMPLETED) {
            // Update order status
            OrderDTO order = OrderService.readOrder(task.getOrderId());
            order.setStatus(OrderStatus.DELIVERED);
            response = OrderService.updateOrder(order);
            if (response != ResponseCode.SUCCESS) {
                JOptionPane.showMessageDialog(null, "Failed to update order status", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // Update runner's current task
            runner.setCurrentTask(null);
            response = RunnerService.updateRunner(runner);
            if (response == ResponseCode.SUCCESS) {
                JOptionPane.showMessageDialog(null, "Task status updated successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null, "Failed to update task status", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        } else {
            JOptionPane.showMessageDialog(null, "Task status updated successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
        }
    }
}
