package ui.review;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import ui.utils.MultiLineRenderer;

import java.awt.*;

public class RunnerReviewPage extends JFrame {
    private JLabel runnerIdLabel, runnerNameLabel;
    private JTable reviewTable;
    private DefaultTableModel tableModel;

    public RunnerReviewPage(String runnerId) {
        setTitle("Runner Reviews");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Header Panel
        JPanel headerPanel = new JPanel(new GridLayout(2, 1));
        runnerIdLabel = new JLabel("Runner ID: " + runnerId, SwingConstants.CENTER);
        runnerNameLabel = new JLabel("Runner Name: " + "John Doe", SwingConstants.CENTER);
        headerPanel.add(runnerIdLabel);
        headerPanel.add(runnerNameLabel);
        add(headerPanel, BorderLayout.NORTH);

        // Table Setup
        String[] columnNames = {"ID", "Order ID", "Customer ID", "Rating", "Comments"};
        tableModel = new DefaultTableModel(columnNames, 0);
        reviewTable = new JTable(tableModel);
        JScrollPane tableScrollPane = new JScrollPane(reviewTable);
        add(tableScrollPane, BorderLayout.CENTER);
        reviewTable.setRowHeight(40);
        
        // Multi line fields
        reviewTable.getColumnModel().getColumn(4).setCellRenderer(new MultiLineRenderer());

        // Set column widths
        reviewTable.getColumnModel().getColumn(0).setPreferredWidth(80);  // ID
        reviewTable.getColumnModel().getColumn(1).setPreferredWidth(100); // Order ID
        reviewTable.getColumnModel().getColumn(2).setPreferredWidth(100); // Customer ID
        reviewTable.getColumnModel().getColumn(3).setPreferredWidth(80);  // Rating
        reviewTable.getColumnModel().getColumn(4).setPreferredWidth(400); // Comments (maximize)

        setVisible(true);
    }

    public void addReview(Object[] rowData) {
        tableModel.addRow(rowData);
    }

    public static void main(String[] args) {
        RunnerReviewPage page = new RunnerReviewPage("R001");
        page.addReview(new Object[]{"1", "O123", "C456", 5, "Great service!"});
        page.addReview(new Object[]{"2", "O124", "C789", 4, "On-time delivery."});
    }
}
