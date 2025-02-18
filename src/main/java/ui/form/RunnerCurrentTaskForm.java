package ui.form;

import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;

import dto.TaskDTO;
import enumeration.TaskStatus;
import service.order.OrderService;
import service.vendor.VendorService;

public class RunnerCurrentTaskForm extends JFrame {
    private JLabel orderIdLabel, vendorNameLabel, deliveryFeeLabel, taskDetailsLabel;
    private JTextArea vendorAddressArea, customerAddressArea;
    private JComboBox<TaskStatus> statusComboBox;
    private JButton saveButton, closeButton;
    
    public RunnerCurrentTaskForm(TaskDTO task) {
        setTitle("Runner Task Details");
        setSize(400, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        getContentPane().setLayout(new GridLayout(8, 2, 5, 5));

        add(createCenteredLabel("Order ID:"));
        orderIdLabel = new JLabel(task.getOrderId());
        getContentPane().add(orderIdLabel);

        add(createCenteredLabel("Vendor Name:"));
        vendorNameLabel = new JLabel(OrderService.readOrder(task.getOrderId()).getVendorId());
        getContentPane().add(vendorNameLabel);

        add(createCenteredLabel("Vendor Address:"));
        vendorAddressArea = new JTextArea(VendorService.readVendor(task.getOrderId()).getAddressId());
        vendorAddressArea.setLineWrap(true);
        vendorAddressArea.setWrapStyleWord(true);
        vendorAddressArea.setEditable(false);
        JScrollPane vendorScrollPane = new JScrollPane(vendorAddressArea);
        getContentPane().add(vendorScrollPane);

        add(createCenteredLabel("Customer Address:"));
        customerAddressArea = new JTextArea(task.getCustomerAddress());
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
        getContentPane().add(statusComboBox);
        
        saveButton = new JButton("Save");
        closeButton = new JButton("Close");
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

    public static void main(String[] args) {
    	TaskDTO task = new TaskDTO();
    	task.setId("0123");
        new RunnerCurrentTaskForm(task);
    }
}
