package ui.form;

import javax.swing.*;
import dto.ComplaintDTO;
import java.awt.*;
import java.util.List;

public class CustomerCreateComplaintForm extends JFrame {
    private JComboBox<String> orderComboBox;

    public CustomerCreateComplaintForm(ComplaintDTO complaint, List<String> orderIds) {
        setTitle("Create Complaint");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        getContentPane().setLayout(new GridLayout(5, 2, 5, 5));

        getContentPane().add(new JLabel("Complaint ID:"));
        getContentPane().add(new JLabel(complaint.getId()));

        getContentPane().add(new JLabel("Customer ID:"));
        getContentPane().add(new JLabel(complaint.getCustomerId()));

        getContentPane().add(new JLabel("Order ID:"));
        orderComboBox = new JComboBox<>(orderIds.toArray(new String[0]));
        getContentPane().add(orderComboBox);

        getContentPane().add(new JLabel("Description:"));
        JTextArea descriptionArea = new JTextArea(complaint.getDescription());
        descriptionArea.setWrapStyleWord(true);
        descriptionArea.setLineWrap(true);
        descriptionArea.setEditable(false);
        getContentPane().add(new JScrollPane(descriptionArea));

        JButton closeButton = new JButton("Close");
        getContentPane().add(new JLabel()); // Placeholder
        getContentPane().add(closeButton);

        closeButton.addActionListener(e -> dispose());

        setLocationRelativeTo(null);
        setVisible(true);
    }

    public static void main(String[] args) {
        ComplaintDTO sampleComplaint = new ComplaintDTO();
        sampleComplaint.setId("C123");
        sampleComplaint.setCustomerId("U456");
        sampleComplaint.setDescription("Item was damaged.");

        List<String> sampleOrders = List.of("O789", "O790", "O791");

        new CustomerCreateComplaintForm(sampleComplaint, sampleOrders);
    }
}

