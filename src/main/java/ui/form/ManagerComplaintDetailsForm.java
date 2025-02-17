package ui.form;

import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import dto.ComplaintDTO;
import enumeration.ComplaintStatus;

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
        complaintStatusComboBox.setSelectedItem(complaint.getStatus()); // Fixed selection issue
        getContentPane().add(complaintStatusComboBox);

        getContentPane().add(new JLabel("Solution:"));
        JTextArea solutionArea = new JTextArea(complaint.getSolution());
        solutionArea.setWrapStyleWord(true);
        solutionArea.setLineWrap(true);
        solutionArea.setEditable(true);
        getContentPane().add(new JScrollPane(solutionArea));

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
        sampleComplaint.setOrderId("O789");
        sampleComplaint.setDescription("Item was damaged. Item was damaged. Item was damaged. Item was damaged. Item was damaged.");
        sampleComplaint.setStatus(ComplaintStatus.IN_REVIEW);
        sampleComplaint.setSolution("Replacement is being processed.");
        new ManagerComplaintDetailsForm(sampleComplaint); // Fixed incorrect class reference
    }
}
