package ui.form;

import java.awt.GridLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import dto.ComplaintDTO;
import enumeration.ComplaintStatus;
import enumeration.ResponseCode;
import service.complaint.ComplaintService;

public class ManagerComplaintDetailsForm extends JFrame {
    private JComboBox<ComplaintStatus> complaintStatusComboBox;

    public ManagerComplaintDetailsForm(ComplaintDTO complaint) {
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
        complaintStatusComboBox = new JComboBox<>(ComplaintStatus.values());
        complaintStatusComboBox.setSelectedItem(complaint.getStatus());
        getContentPane().add(complaintStatusComboBox);

        getContentPane().add(new JLabel("Solution:"));
        JTextArea solutionArea = new JTextArea(complaint.getSolution());
        solutionArea.setWrapStyleWord(true);
        solutionArea.setLineWrap(true);
        solutionArea.setEditable(true);
        getContentPane().add(new JScrollPane(solutionArea));

        JButton saveButton = new JButton("Save");
        getContentPane().add(saveButton);

        JButton closeButton = new JButton("Close");
        getContentPane().add(closeButton);

        saveButton.addActionListener(e -> {
            // Check if complaint is already resolved or rejected
            if (complaint.getStatus() == ComplaintStatus.RESOLVED || complaint.getStatus() == ComplaintStatus.REJECTED) {
                JOptionPane.showMessageDialog(this, "Complaint is already resolved or rejected.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // Check if solution is empty
            if (complaint.getStatus() == ComplaintStatus.RESOLVED && solutionArea.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Solution cannot be empty.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (complaint.getStatus() == ComplaintStatus.REJECTED && solutionArea.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Solution cannot be empty.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Check if complaint status is unchanged
            if (complaint.getStatus() == (ComplaintStatus) complaintStatusComboBox.getSelectedItem()) {
                JOptionPane.showMessageDialog(this, "Complaint status is unchanged.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            complaint.setStatus((ComplaintStatus) complaintStatusComboBox.getSelectedItem());
            complaint.setSolution(solutionArea.getText());
            // Update complaint status and solution in database
            ResponseCode response = ComplaintService.updateComplaint(complaint);
            if (response == ResponseCode.SUCCESS) {
                JOptionPane.showMessageDialog(this, "Complaint updated successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Failed to update complaint", "Error", JOptionPane.ERROR_MESSAGE);
            }
            
            dispose();
        });
        closeButton.addActionListener(e -> dispose());

        setLocationRelativeTo(null);
        setVisible(true);
    }

}
