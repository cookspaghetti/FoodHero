package ui.vendor;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import dto.AddressDTO;
import dto.CustomerDTO;
import dto.DeductTransactionDTO;
import dto.ItemDTO;
import dto.OrderDTO;
import dto.VendorDTO;
import enumeration.ButtonMode;
import enumeration.OrderStatus;
import enumeration.ResponseCode;
import enumeration.ServiceType;
import service.address.AddressService;
import service.customer.CustomerService;
import service.distance.DistanceService;
import service.general.SessionControlService;
import service.item.ItemService;
import service.order.OrderService;
import service.transaction.TransactionService;
import service.utils.IdGenerationUtils;
import service.vendor.VendorService;
import ui.utils.ButtonEditor;
import ui.utils.ButtonRenderer;

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
        String[] columnNames = { "Item ID", "Item Name", "Price", "Quantity", "Total Price", "Actions" };
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
		    public boolean isCellEditable(int row, int column) {
                return column == 5; // Make only the "Action" column editable
		    }
		};
        cartTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(cartTable);
        add(scrollPane, BorderLayout.CENTER);
        cartTable.setRowHeight(40);
        
        // Apply renderer and editor to the table
		cartTable.getColumnModel().getColumn(5).setCellRenderer(new ButtonRenderer(ButtonMode.CARTEDITDELETE));
		cartTable.getColumnModel().getColumn(5).setCellEditor(new ButtonEditor(cartTable, ButtonMode.CARTEDITDELETE));

        cartTable.getColumn("Actions").setPreferredWidth(140);   
        cartTable.getColumn("Item Name").setPreferredWidth(150);

        
        // Footer Panel
        JPanel footerPanel = new JPanel();
        checkoutButton = new JButton("Checkout");
        checkoutButton.addActionListener(e -> {
            handleCheckout(vendorId);
        });
        footerPanel.add(checkoutButton);
        add(footerPanel, BorderLayout.SOUTH);

        setLocationRelativeTo(null);
        loadCartData(vendorId);
        setVisible(true);
    }

    private void loadCartData(String vendorId) {
        HashMap<String, Integer> cartData = new HashMap<>();
        cartData = SessionControlService.getCartItems();
        if (cartData == null || cartData.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Cart is empty", "Empty Cart", JOptionPane.INFORMATION_MESSAGE);
            dispose();
            return;
        }

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
    
        if (choice == null) {
            return;
        }
    
        JOptionPane.showMessageDialog(this, "You selected: " + choice);
    
        // Calculate total amount
        double totalAmount = 0;
        HashMap<String, Integer> cartData = SessionControlService.getCartItems();
    
        for (String itemId : cartData.keySet()) {
            ItemDTO item = ItemService.readItem(vendorId, itemId);
            if (item == null) {
                JOptionPane.showMessageDialog(this, "Item not found: " + itemId, "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            totalAmount += item.getPrice() * cartData.get(itemId);
        }
    
        JOptionPane.showMessageDialog(this, "Total amount: " + totalAmount);
        double balance = SessionControlService.getCredit();
        double oldBalance = balance;
    
        CustomerDTO customer = (CustomerDTO) SessionControlService.getUser();
    
        // Get vendor and customer addresses
        VendorDTO vendor = VendorService.readVendor(vendorId);
        AddressDTO vendorAddress = vendor != null ? AddressService.getAddressById(vendor.getAddressId()) : null;
        AddressDTO customerAddress = AddressService.getAddressById(customer.getAddressId());
    
        if (vendorAddress == null || customerAddress == null) {
            JOptionPane.showMessageDialog(this, "Address information is missing.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
    
        // Calculate delivery fee if needed
        double deliveryFee = (choice.equals("Delivery")) ? DistanceService.getDeliveryFee(vendorAddress, customerAddress) : 0.0;
        double finalAmount = totalAmount + deliveryFee;
    
        if (balance < finalAmount) {
            JOptionPane.showMessageDialog(this, "Insufficient balance (Total: RM" + finalAmount + ")", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
    
        // Deduct balance
        customer.setCredit(balance - finalAmount);
        ResponseCode response = CustomerService.updateCustomer(customer);
        if (response != ResponseCode.SUCCESS) {
            JOptionPane.showMessageDialog(this, "Failed to update balance", "Error", JOptionPane.ERROR_MESSAGE);
            customer.setCredit(oldBalance); // Restore balance
            return;
        }
        
        // Get latest order id
        String currentOrderId = IdGenerationUtils.getNextId(ServiceType.ORDER, null, null);

        // Create order
        OrderDTO order = new OrderDTO();
        order.setCustomerId(customer.getId());
        order.setVendorId(vendorId);
        order.setTotalAmount(totalAmount);
        order.setStatus(OrderStatus.PENDING);
        order.setItems(cartData);
        order.setPlacementTime(LocalDateTime.now());
        order.setCompletionTime(null);
        order.setDeliveryFee(deliveryFee);
        order.setRunnerId(null);
    
        order.setNotes(choice);
        order.setDeliveryAddress(choice.equals("Delivery") ? SessionControlService.getCurrentSelectedAddress() : null);
    
        response = OrderService.createOrder(order);
        System.out.println("Order created: " + response);
        if (response == ResponseCode.RUNNER_NOT_FOUND){
            JOptionPane.showMessageDialog(this, "No runner available for delivery", "Error", JOptionPane.ERROR_MESSAGE);
            customer.setCredit(oldBalance);
            CustomerService.updateCustomer(customer);
            return;
        } else if (response != ResponseCode.SUCCESS) {
            JOptionPane.showMessageDialog(this, "Failed to place order", "Error", JOptionPane.ERROR_MESSAGE);
            customer.setCredit(oldBalance);
            CustomerService.updateCustomer(customer);
            return;
        }
        
        // Create transaction
        DeductTransactionDTO transaction = new DeductTransactionDTO();
        transaction.setCustomerId(customer.getId());
        transaction.setAmount(finalAmount);
        transaction.setDate(LocalDateTime.now());
        transaction.setOrderId(currentOrderId);
        transaction.setDescription("Order from " + vendor.getVendorName());
    
        response = TransactionService.createDeductTransaction(transaction);
        if (response != ResponseCode.SUCCESS) {
            JOptionPane.showMessageDialog(this, "Failed to process transaction", "Error", JOptionPane.ERROR_MESSAGE);
            customer.setCredit(oldBalance);
            CustomerService.updateCustomer(customer);
            OrderService.deleteOrder(order.getId()); // Rollback order
            return;
        }
    
        JOptionPane.showMessageDialog(this, "Order placed successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
        SessionControlService.clearCart();
        dispose();
    }

}
