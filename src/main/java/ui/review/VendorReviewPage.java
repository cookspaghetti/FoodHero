package ui.review;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;

import dto.VendorDTO;
import dto.VendorReviewDTO;
import service.review.ReviewService;
import ui.utils.MultiLineRenderer;

public class VendorReviewPage extends JFrame {
    private JLabel runnerIdLabel, runnerNameLabel;
    private JTable reviewTable;
    private DefaultTableModel tableModel;

    public VendorReviewPage(VendorDTO vendor) {
        setTitle("Vendor Reviews");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Header Panel
        JPanel headerPanel = new JPanel(new GridLayout(2, 1));
        runnerIdLabel = new JLabel("Vendor ID: " + vendor.getId(), SwingConstants.CENTER);
        runnerNameLabel = new JLabel("Vendor Name: " + vendor.getName(), SwingConstants.CENTER);
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
        
        reviewTable.getColumnModel().getColumn(4).setCellRenderer(new MultiLineRenderer()); // For the "Comments" column (index 4)
        
        // Set column widths
        reviewTable.getColumnModel().getColumn(0).setPreferredWidth(80);  // ID
        reviewTable.getColumnModel().getColumn(1).setPreferredWidth(100); // Order ID
        reviewTable.getColumnModel().getColumn(2).setPreferredWidth(100); // Customer ID
        reviewTable.getColumnModel().getColumn(3).setPreferredWidth(80);  // Rating
        reviewTable.getColumnModel().getColumn(4).setPreferredWidth(400); // Comments (maximize)

        loadReviews(vendor);

        setVisible(true);
    }

    public void addReview(Object[] rowData) {
        tableModel.addRow(rowData);
    }

    public void loadReviews(VendorDTO vendor) {
        List<VendorReviewDTO> reviews = ReviewService.readAllVendorReview(vendor.getId());
        for (VendorReviewDTO review : reviews) {
            Object[] rowData = {review.getId(), review.getOrderId(), review.getCustomerId(), review.getRating(), review.getComments()};
            addReview(rowData);
        }
    }   

}
