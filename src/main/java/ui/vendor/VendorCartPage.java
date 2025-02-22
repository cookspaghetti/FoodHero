package ui.vendor;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import dto.AddressDTO;
import dto.CustomerDTO;
import dto.ItemDTO;
import dto.OrderDTO;
import dto.VendorDTO;
import enumeration.OrderStatus;
import enumeration.ResponseCode;
import service.address.AddressService;
import service.customer.CustomerService;
import service.distance.DistanceService;
import service.general.SessionControlService;
import service.item.ItemService;
import service.order.OrderService;
import service.vendor.VendorService;

import java.awt.*;
import java.time.LocalDateTime;
import java.util.HashMap;

public class VendorCartPage extends JFrame {
    private JTable cartTable;
    private JButton checkoutButton;
    private DefaultTableModel tableModel;

    public VendorCartPage(String vendorId) {
        setTitle("Cart");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // Header Panel
        JPanel headerPanel = new JPanel(new BorderLayout());
        JLabel titleLabel = new JLabel("Your Cart", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        headerPanel.add(titleLabel, BorderLayout.CENTER);
        add(headerPanel, BorderLayout.NORTH);

        // Table Data
        String[] columnNames = { "Item ID", "Item Name", "Price", "Quantity", "Total Price" };
        loadCartData(vendorId);
        tableModel = new DefaultTableModel(columnNames, 0);
        cartTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(cartTable);
        add(scrollPane, BorderLayout.CENTER);

        // Footer Panel
        JPanel footerPanel = new JPanel();
        checkoutButton = new JButton("Checkout");
        checkoutButton.addActionListener(e -> {
            handleCheckout(vendorId);
        });
        footerPanel.add(checkoutButton);
        add(footerPanel, BorderLayout.SOUTH);

        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void loadCartData(String vendorId) {
        HashMap<String, Integer> cartData = SessionControlService.getCartItems();
        for (String itemId : cartData.keySet()) {
            // Fetch item details from database
            ItemDTO item = ItemService.readItem(vendorId, itemId);
            // Add item details to data array
            addRow(new Object[] { itemId, item.getName(), item.getPrice(), cartData.get(itemId),
                    item.getPrice() * cartData.get(itemId) });
        }
    }

    private void addRow(Object[] rowData) {
        tableModel.addRow(rowData);
    }

    private void handleCheckout(String vendorId) {
        String[] options = { "Dine-In", "Takeaway", "Delivery" };
        String choice = (String) JOptionPane.showInputDialog(
                this,
                "Select an option:",
                "Checkout Option",
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[0]);
    
        if (choice != null) {
            JOptionPane.showMessageDialog(this, "You selected: " + choice);
    
            // Calculate total amount
            double totalAmount = 0;
            HashMap<String, Integer> cartData = SessionControlService.getCartItems();
            for (String itemId : cartData.keySet()) {
                // Fetch item details from database
                ItemDTO item = ItemService.readItem(vendorId, itemId);
                totalAmount += item.getPrice() * cartData.get(itemId);
            }
    
            JOptionPane.showMessageDialog(this, "Total amount: " + totalAmount);
            double balance = SessionControlService.getCredit();
            if (balance < totalAmount) {
                JOptionPane.showMessageDialog(this, "Insufficient balance", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
    
            // Deduct balance
            CustomerDTO customer = (CustomerDTO) SessionControlService.getUser();
            customer.setCredit(balance - totalAmount);
            ResponseCode response = CustomerService.updateCustomer(customer);
            if (response != ResponseCode.SUCCESS) {
                JOptionPane.showMessageDialog(this, "Failed to update balance", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
    
            // Create Order
            OrderDTO order = new OrderDTO();
            order.setCustomerId(customer.getId());
            order.setVendorId(vendorId);
            order.setTotalAmount(totalAmount);
            order.setStatus(OrderStatus.PENDING);
            order.setItems(cartData);
            order.setPlacementTime(LocalDateTime.now());
    
            if (choice.equals("Delivery")) {
                order.setNotes("Delivery");
                order.setDeliveryAddress(customer.getAddressId());
    
                // Get addresses and calculate delivery fee
                VendorDTO vendor = VendorService.readVendor(vendorId);
                AddressDTO vendorAddress = AddressService.getAddressById(vendor.getAddressId());
                AddressDTO customerAddress = AddressService.getAddressById(customer.getAddressId());
                order.setDeliveryFee(DistanceService.getDeliveryFee(vendorAddress, customerAddress));
    
            } else if (choice.equals("Takeaway")) {
                order.setNotes("Takeaway");
                order.setDeliveryAddress(null); // No delivery address needed
                order.setDeliveryFee(0.0); // No delivery fee
    
            } else if (choice.equals("Dine-In")) {
                order.setNotes("Dine-In");
                order.setDeliveryAddress(null); // No delivery address needed
                order.setDeliveryFee(0.0); // No delivery fee
            }
    
            response = OrderService.createOrder(order);
            if (response == ResponseCode.SUCCESS) {
                JOptionPane.showMessageDialog(this, "Order placed successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
                SessionControlService.clearCart();
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to place order", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    

}
