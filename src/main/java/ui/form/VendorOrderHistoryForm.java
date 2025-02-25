package ui.form;

import java.awt.GridLayout;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.concurrent.CompletableFuture;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextArea;

import dto.OrderDTO;
import dto.ReviewDTO;
import service.processor.ItemProcessor;
import service.review.ReviewService;

public class VendorOrderHistoryForm extends JFrame {

    private String ratings;

    public VendorOrderHistoryForm(OrderDTO order) {
        setTitle("Order Details");
        setSize(500, 550);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        getContentPane().setLayout(new GridLayout(10, 2, 5, 5));

        getContentPane().add(new JLabel("Order ID:"));
        getContentPane().add(new JLabel(order.getId()));

        getContentPane().add(new JLabel("Customer ID:"));
        getContentPane().add(new JLabel(order.getCustomerId()));
        
        getContentPane().add(new JLabel("Items:"));
        getContentPane().add(new JTextArea(processItemList(order.getVendorId(), order.getItems())));

        getContentPane().add(new JLabel("Total Amount:"));
        getContentPane().add(new JLabel(String.valueOf(order.getTotalAmount())));

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        getContentPane().add(new JLabel("Placement Time:"));
        getContentPane().add(new JLabel(order.getPlacementTime().format(formatter)));

        getContentPane().add(new JLabel("Completion Time:"));
        getContentPane().add(new JLabel(order.getCompletionTime() == null ? "Not completed" : order.getCompletionTime().format(formatter)));
        
        getContentPane().add(new JLabel("Notes:"));
        getContentPane().add(new JLabel(order.getNotes()));

        getContentPane().add(new JLabel("Status:"));
        getContentPane().add(new JLabel(order.getStatus().toString()));
        
        getContentPane().add(new JLabel("Review:"));
        getContentPane().add(new JLabel(getReview(order.getVendorId(), order.getId())));

        getContentPane().add(new JLabel("Ratings:"));
        getContentPane().add(new JLabel(ratings));
        
        setLocationRelativeTo(null);
        setVisible(true);
    }

    // method to get review
    private String getReview(String vendorId, String orderId) {
        ReviewDTO review = ReviewService.readVendorReview(vendorId, orderId);
        if (review == null) {
            return "No review yet";
        }
        ratings = String.valueOf(review.getRating()) + "/5";
        return review.getComments();
    }

    // method to process item list
    private String processItemList(String vendorId, HashMap<String, Integer> items) {
        try {
            CompletableFuture<String> itemFuture = ItemProcessor.processItemListAsync(vendorId, items);
            return itemFuture.join();
        } catch (Exception e) {
            System.err.println("Error processing item list: " + e.getMessage());
            return "Error";
        }
    }
}

