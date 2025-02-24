package ui.form;

import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;

import dto.RunnerReviewDTO;
import dto.VendorReviewDTO;
import enumeration.ResponseCode;
import service.order.OrderService;
import service.review.ReviewService;
import service.runner.RunnerService;
import service.vendor.VendorService;

public class CustomerReviewForm extends JFrame {

    public CustomerReviewForm(String vendorId, String runnerId, String orderId) {
        setTitle("Review");
        setSize(400, 480);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        getContentPane().setLayout(new GridLayout(8, 2, 5, 5));

        add(createCenteredLabel("Vendor Name:"));
        JLabel vendorNameLabel = new JLabel(VendorService.readVendor(vendorId).getVendorName());
        getContentPane().add(vendorNameLabel);

        add(createCenteredLabel("Order ID:"));
        JLabel orderIdLabel = new JLabel(orderId);
        getContentPane().add(orderIdLabel);

        add(createCenteredLabel("Rating:"));
        JComboBox<Integer> vendorRatingComboBox = new JComboBox<>(new Integer[]{1, 2, 3, 4, 5});
        vendorRatingComboBox.setSelectedIndex(4);
        getContentPane().add(vendorRatingComboBox);

        add(createCenteredLabel("Review:"));
        JTextArea vendorReviewArea = new JTextArea();
        vendorReviewArea.setLineWrap(true);
        vendorReviewArea.setWrapStyleWord(true);
        JScrollPane vendorReviewScrollPane = new JScrollPane(vendorReviewArea);
        getContentPane().add(vendorReviewScrollPane);

        if (runnerId == null) {
            setSize(400, 360);
            setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            getContentPane().setLayout(new GridLayout(5, 2, 5, 5));

            JButton submitButton = new JButton("Submit");
            submitButton.addActionListener(e -> {
                // Validate review
                String vendorReview = vendorReviewArea.getText();
                if (vendorReview.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Please fill in all fields", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // vendor review object
                VendorReviewDTO vendorReviewObj = new VendorReviewDTO();
                vendorReviewObj.setVendorId(vendorId);
                vendorReviewObj.setOrderId(orderId);
                vendorReviewObj.setCustomerId(OrderService.readOrder(orderId).getCustomerId());
                vendorReviewObj.setRating(vendorRatingComboBox.getSelectedIndex() + 1);
                vendorReviewObj.setComments(vendorReview);

                // Submit review
                ResponseCode response = ReviewService.createVendorReview(vendorReviewObj);
                
                if (response == ResponseCode.SUCCESS) {
                    JOptionPane.showMessageDialog(null, "Review submitted successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(null, "Failed to submit review", "Error", JOptionPane.ERROR_MESSAGE);
                }
            });
            getContentPane().add(submitButton);

            JButton closeButton = new JButton("Close");
            closeButton.addActionListener(e -> dispose());
            getContentPane().add(closeButton);
        } else {
            add(createCenteredLabel("Runner Name:"));
            JLabel runnerIdLabel = new JLabel(RunnerService.readRunner(runnerId).getName());
            getContentPane().add(runnerIdLabel);

            add(createCenteredLabel("Rating:"));
            JComboBox<Integer> runnerRatingComboBox = new JComboBox<>(new Integer[]{1, 2, 3, 4, 5});
            runnerRatingComboBox.setSelectedIndex(4);
            getContentPane().add(runnerRatingComboBox);

            add(createCenteredLabel("Review:"));
            JTextArea runnerReviewArea = new JTextArea();
            runnerReviewArea.setLineWrap(true);
            runnerReviewArea.setWrapStyleWord(true);
            JScrollPane runnerReviewScrollPane = new JScrollPane(runnerReviewArea);
            getContentPane().add(runnerReviewScrollPane);

            JButton submitButton = new JButton("Submit");
            submitButton.addActionListener(e -> {
                // Validate review
                String vendorReview = vendorReviewArea.getText();
                String runnerReview = runnerReviewArea.getText();
                if (vendorReview.isEmpty() || runnerReview.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Please fill in all fields", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // vendor review object
                VendorReviewDTO vendorReviewObj = new VendorReviewDTO();
                vendorReviewObj.setVendorId(vendorId);
                vendorReviewObj.setOrderId(orderId);
                vendorReviewObj.setCustomerId(OrderService.readOrder(orderId).getCustomerId());
                vendorReviewObj.setRating(vendorRatingComboBox.getSelectedIndex() + 1);
                vendorReviewObj.setComments(vendorReview);

                // runner review object
                RunnerReviewDTO runnerReviewObj = new RunnerReviewDTO();
                runnerReviewObj.setCustomerId(OrderService.readOrder(orderId).getCustomerId());
                runnerReviewObj.setRunnerId(runnerId);
                runnerReviewObj.setOrderId(orderId);
                runnerReviewObj.setRating(runnerRatingComboBox.getSelectedIndex() + 1);
                runnerReviewObj.setComments(runnerReview);

                // Submit review
                ResponseCode response = ReviewService.createVendorReview(vendorReviewObj);
                response = ReviewService.createRunnerReview(runnerReviewObj);
                
                if (response == ResponseCode.SUCCESS) {
                    JOptionPane.showMessageDialog(null, "Review submitted successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(null, "Failed to submit review", "Error", JOptionPane.ERROR_MESSAGE);
                }
            });
            getContentPane().add(submitButton);

            JButton closeButton = new JButton("Close");
            closeButton.addActionListener(e -> dispose());
            getContentPane().add(closeButton);
        }

        setLocationRelativeTo(null);
        setVisible(true);
    }

    private JLabel createCenteredLabel(String text) {
        JLabel label = new JLabel(text);
        label.setHorizontalAlignment(SwingConstants.CENTER);
        return label;
    }
}   
