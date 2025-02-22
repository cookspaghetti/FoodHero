package ui.review;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import dto.RunnerReviewDTO;
import dto.RunnerDTO;
import service.review.ReviewService;
import service.runner.RunnerService;
import ui.utils.MultiLineRenderer;

import java.awt.*;

import java.util.List;

public class RunnerReviewPage extends JFrame {
    private JLabel runnerIdLabel, runnerNameLabel;
    private JTable reviewTable;
    private DefaultTableModel tableModel;

    public RunnerReviewPage(String runnerId) {
        // Get runner details
        RunnerDTO runner = RunnerService.readRunner(runnerId);

        setTitle("Runner Reviews");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Header Panel
        JPanel headerPanel = new JPanel(new GridLayout(2, 1));
        runnerIdLabel = new JLabel("Runner ID: " + runner.getId(), SwingConstants.CENTER);
        runnerNameLabel = new JLabel("Runner Name: " + runner.getName(), SwingConstants.CENTER);
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

        loadReviews(runnerId);

        setVisible(true);
    }

    public void addReview(Object[] rowData) {
        tableModel.addRow(rowData);
    }

    private void loadReviews(String runnerId) {
        List<RunnerReviewDTO> reviews = ReviewService.readAllRunnerReview(runnerId);

        for (RunnerReviewDTO review : reviews) {
            Object[] rowData = {review.getId(), review.getOrderId(), review.getCustomerId(), review.getRating(), review.getComments()};
            addReview(rowData);
        }
    }
}
