package ui.form;

import javax.swing.*;

import dto.ComplaintDTO;

import java.awt.*;

public class CustomerComplaintDetailsForm extends JFrame {
    public CustomerComplaintDetailsForm(ComplaintDTO complaint) {
        setTitle("Complaint Details");
        setSize(500, 450);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        getContentPane().setLayout(new GridLayout(7, 2, 5, 5));

        getContentPane().add(new JLabel("Complaint ID:"));
        getContentPane().add(new JLabel(complaint.getId()));

        getContentPane().add(new JLabel("Customer ID:"));
        getContentPane().add(new JLabel(complaint.getCustomerId()));

        getContentPane().add(new JLabel("Order ID:"));
        getContentPane().add(new JLabel(complaint.getOrderId()));

        getContentPane().add(new JLabel("Description:"));
        JTextArea descriptionArea = new JTextArea(complaint.getDescription());
        descriptionArea.setWrapStyleWord(true);
        descriptionArea.setLineWrap(true);
        descriptionArea.setEditable(false);
        getContentPane().add(new JScrollPane(descriptionArea));

        getContentPane().add(new JLabel("Status:"));
        getContentPane().add(new JLabel(complaint.getStatus().toString()));

        getContentPane().add(new JLabel("Solution:"));
        JTextArea solutionArea = new JTextArea(complaint.getSolution());
        solutionArea.setWrapStyleWord(true);
        solutionArea.setLineWrap(true);
        solutionArea.setEditable(false);
        getContentPane().add(new JScrollPane(solutionArea));

        JButton closeButton = new JButton("Close");
        getContentPane().add(new JLabel());
        getContentPane().add(closeButton);

        closeButton.addActionListener(e -> dispose());

        setLocationRelativeTo(null);
        setVisible(true);
    }
    
}
