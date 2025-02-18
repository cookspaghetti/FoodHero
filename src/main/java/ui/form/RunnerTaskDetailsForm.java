package ui.form;

import javax.swing.*;

import dto.TaskDTO;
import service.order.OrderService;
import service.vendor.VendorService;

import java.awt.*;

public class RunnerTaskDetailsForm extends JFrame {
    private JLabel orderIdLabel, vendorNameLabel, deliveryFeeLabel, taskDetailsLabel;
    private JTextArea vendorAddressArea, customerAddressArea;
    private JButton acceptButton, declineButton;

    public RunnerTaskDetailsForm(TaskDTO task) {
        setTitle("Runner Task Details");
        setSize(400, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        getContentPane().setLayout(new GridLayout(7, 2, 5, 5));

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

        acceptButton = new JButton("Accept");
        declineButton = new JButton("Decline");
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

    public static void main(String[] args) {
    	TaskDTO task = new TaskDTO();
    	task.setId("0123");
        new RunnerTaskDetailsForm(task);
    }
}

