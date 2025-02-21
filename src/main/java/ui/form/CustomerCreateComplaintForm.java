package ui.form;

import javax.swing.*;
import dto.ComplaintDTO;
import dto.OrderDTO;
import enumeration.ComplaintStatus;
import enumeration.ResponseCode;
import enumeration.ServiceType;
import service.general.SessionControlService;
import service.order.OrderService;
import service.complaint.ComplaintService;
import service.utils.IdGenerationUtils;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class CustomerCreateComplaintForm extends JFrame {
    private JComboBox<String> orderComboBox;

    public CustomerCreateComplaintForm() {
        setTitle("Create Complaint");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        getContentPane().setLayout(new GridLayout(5, 2, 5, 5));

        getContentPane().add(new JLabel("Complaint ID:"));
        getContentPane().add(new JLabel(IdGenerationUtils.getNextId(ServiceType.COMPLAIN, null, null)));

        getContentPane().add(new JLabel("Customer ID:"));
        getContentPane().add(new JLabel(SessionControlService.getId()));

        getContentPane().add(new JLabel("Order ID:"));
        orderComboBox = new JComboBox<>(getRecentOrders());
        getContentPane().add(orderComboBox);

        getContentPane().add(new JLabel("Description:"));
        JTextArea descriptionArea = new JTextArea();
        descriptionArea.setWrapStyleWord(true);
        descriptionArea.setLineWrap(true);
        getContentPane().add(new JScrollPane(descriptionArea));

        JButton submitButton = new JButton("Submit");
        getContentPane().add(new JLabel());
        getContentPane().add(submitButton);
        JButton closeButton = new JButton("Close");
        getContentPane().add(new JLabel());
        getContentPane().add(closeButton);

        submitButton.addActionListener(e -> {
            submitComplaint(orderComboBox.getSelectedItem().toString(), descriptionArea.getText());
        });

        closeButton.addActionListener(e -> dispose());

        setLocationRelativeTo(null);
        setVisible(true);
    }

    // Get the recent orders
    private String[] getRecentOrders() {
        List<String> orderIds = new ArrayList<>();
        List<OrderDTO> orders = OrderService.readCustomerOrders(SessionControlService.getId());
        for (OrderDTO order : orders) {
            orderIds.add(order.getId());
        }
        return orderIds.toArray(new String[0]);
    }

    // Submit the complaint
    private void submitComplaint(String orderId, String description) {
        ComplaintDTO complaint = new ComplaintDTO();
        complaint.setId(IdGenerationUtils.getNextId(ServiceType.COMPLAIN, null, null));
        complaint.setCustomerId(SessionControlService.getId());
        complaint.setOrderId(orderId);
        complaint.setDescription(description);
        complaint.setStatus(ComplaintStatus.PENDING);

        ResponseCode response = ComplaintService.createComplaint(complaint);
        if (response == ResponseCode.SUCCESS) {
            JOptionPane.showMessageDialog(this, "Complaint submitted successfully");
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Failed to submit complaint");
        }
    }
}

