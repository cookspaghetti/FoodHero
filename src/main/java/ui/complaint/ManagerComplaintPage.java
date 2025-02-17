package ui.complaint;

import javax.swing.*;
import java.awt.*;
import javax.swing.table.DefaultTableModel;

import enumeration.ComplainStatus;

public class ManagerComplaintPage extends JFrame {
    private JTextField searchField;
    private JButton searchButton;
    private JComboBox<ComplainStatus> statusFilter;
    private JTable complaintTable;
    private JScrollPane tableScrollPane;
    private DefaultTableModel tableModel;

    public ManagerComplaintPage() {
        setTitle("Complaint Management");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Search Panel
        JPanel searchPanel = new JPanel();
        searchField = new JTextField(20);
        searchButton = new JButton("Search");
        statusFilter = new JComboBox<>(ComplainStatus.values());
        
        searchPanel.add(new JLabel("Search Complaint:"));
        searchPanel.add(searchField);
        searchPanel.add(searchButton);
        searchPanel.add(new JLabel("Filter by Status:"));
        searchPanel.add(statusFilter);
        add(searchPanel, BorderLayout.NORTH);

        // Table Panel
        String[] columnNames = {"Complaint ID", "Customer ID", "Order ID", "Description", "Status", "Actions"};
        tableModel = new DefaultTableModel(columnNames, 0);
        complaintTable = new JTable(tableModel);
        tableScrollPane = new JScrollPane(complaintTable);
        add(tableScrollPane, BorderLayout.CENTER);
        
        // Set column widths
        complaintTable.getColumnModel().getColumn(0).setPreferredWidth(80); // Complaint ID
        complaintTable.getColumnModel().getColumn(1).setPreferredWidth(80); // Customer ID
        complaintTable.getColumnModel().getColumn(2).setPreferredWidth(80); // Order ID
        complaintTable.getColumnModel().getColumn(3).setPreferredWidth(300); // Description
        complaintTable.getColumnModel().getColumn(4).setPreferredWidth(100); // Status
        complaintTable.getColumnModel().getColumn(5).setPreferredWidth(80); // Actions
        
        setVisible(true);
    }

    public static void main(String[] args) {
        new ManagerComplaintPage();
    }
}

