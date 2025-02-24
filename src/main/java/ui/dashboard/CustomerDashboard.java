package ui.dashboard;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import dto.CustomerDTO;
import dto.OrderDTO;
import dto.VendorDTO;
import enumeration.ButtonMode;
import enumeration.OrderStatus;
import service.general.SessionControlService;
import service.order.OrderService;
import service.processor.ItemProcessor;
import service.vendor.VendorService;
import ui.complaint.CustomerComplaintPage;
import ui.login.LoginInterface;
import ui.notification.NotificationPage;
import ui.order.CustomerOrderPage;
import ui.profile.CustomerProfilePage;
import ui.transaction.CustomerTransactionPage;
import ui.utils.ButtonEditor;
import ui.utils.ButtonRenderer;
import ui.vendor.CustomerVendorPage;

public class CustomerDashboard extends JFrame {
    private CustomerDTO customer;

    // UI Components
    private JMenuBar menuBar;
    private JMenu homeMenu, vendorMenu, orderMenu, transactionMenu, complaintMenu, notificationMenu, profileMenu;
    private JMenuItem dashboardItem, viewVendorsItem, orderManagementItem, transactionManagementItem,
            complaintManagementItem, notificationItem, editProfileItem;
    private JPanel headerPanel, activeOrdersPanel;
    private JLabel balanceLabel, welcomeLabel, activeOrdersLabel;
    private JButton logoutButton;
    private JTable activeOrdersTable;
    private DefaultTableModel tableModel;
    private JScrollPane tableScrollPane;

    public CustomerDashboard(CustomerDTO customer) {
        this.customer = customer;
        initComponents();
    }

    private void initComponents() {
        setTitle("Customer Dashboard");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setLayout(new BorderLayout());

        // ======= Menu Bar =======
        menuBar = new JMenuBar();

        // Home Menu
        homeMenu = new JMenu("Home");
        dashboardItem = new JMenuItem("Dashboard");
        dashboardItem.addActionListener(e -> showDashboard());
        homeMenu.add(dashboardItem);
        menuBar.add(homeMenu);

        // Vendor Menu
        vendorMenu = new JMenu("Vendor");
        viewVendorsItem = new JMenuItem("View Vendors");
        viewVendorsItem.addActionListener(e -> openVendorPage());
        vendorMenu.add(viewVendorsItem);
        menuBar.add(vendorMenu);

        // Order Menu
        orderMenu = new JMenu("Order");
        orderManagementItem = new JMenuItem("Order Management");
        orderManagementItem.addActionListener(e -> openOrderPage());
        orderMenu.add(orderManagementItem);
        menuBar.add(orderMenu);

        // Transaction Menu
        transactionMenu = new JMenu("Transaction");
        transactionManagementItem = new JMenuItem("Transaction Management");
        transactionManagementItem.addActionListener(e -> openTransactionPage());
        transactionMenu.add(transactionManagementItem);
        menuBar.add(transactionMenu);

        // Complaint Menu
        complaintMenu = new JMenu("Complaint");
        complaintManagementItem = new JMenuItem("Complaint Management");
        complaintManagementItem.addActionListener(e -> openComplaintPage());
        complaintMenu.add(complaintManagementItem);
        menuBar.add(complaintMenu);

        // Notification Menu
        notificationMenu = new JMenu("Notification");
        notificationItem = new JMenuItem("View Notifications");
        notificationItem.addActionListener(e -> openNotificationPage());
        notificationMenu.add(notificationItem);
        menuBar.add(notificationMenu);

        // Profile Menu
        profileMenu = new JMenu("Profile");
        editProfileItem = new JMenuItem("Profile Management");
        editProfileItem.addActionListener(e -> openProfilePage());
        profileMenu.add(editProfileItem);
        menuBar.add(profileMenu);

        setJMenuBar(menuBar);

        // ======= Header Panel (Welcome & Logout) =======
        headerPanel = new JPanel(new BorderLayout());
        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        // Style components
        balanceLabel = new JLabel(String.format("Balance: RM%.2f", customer.getCredit()));
        welcomeLabel = new JLabel("Welcome, " + customer.getName());
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 12));
        logoutButton = new JButton("Logout");
        logoutButton.setFocusable(false);

        // Set fonts and colors
        Font labelFont = new Font("Arial", Font.PLAIN, 12);
        balanceLabel.setFont(labelFont);
        logoutButton.setFont(labelFont);

        // Add logout action
        logoutButton.addActionListener(e -> {
            SessionControlService.clearSession(); // Clear session if you have this method
            new LoginInterface().setVisible(true);
            this.dispose();
        });

        // Add components to panels
        leftPanel.add(welcomeLabel);
        rightPanel.add(balanceLabel);
        rightPanel.add(logoutButton);

        // Add panels to header
        headerPanel.add(leftPanel, BorderLayout.WEST);
        headerPanel.add(rightPanel, BorderLayout.EAST);

        // Add header to frame
        getContentPane().add(headerPanel, BorderLayout.NORTH);

        // ======= Active Orders Table =======
        activeOrdersPanel = new JPanel(new BorderLayout());
        activeOrdersLabel = new JLabel("Active Orders");

        String[] columnNames = { "Order ID", "Vendor Name", "Vendor Type", "Item", "Progress", "Status", "Actions" };
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 6; // Make only the "Action" column editable
            }
        };
        activeOrdersTable = new JTable(tableModel);
        activeOrdersTable.setRowHeight(40);

        activeOrdersTable.getColumn("Order ID").setPreferredWidth(50);
        activeOrdersTable.getColumn("Vendor Name").setPreferredWidth(200);

        // Adding sample data
        Object[][] data = getActiveOrders();

        for (Object[] row : data) {
            tableModel.addRow(row);
        }

        // Adding a progress bar to the table
        activeOrdersTable.getColumn("Progress").setCellRenderer((table, value, isSelected, hasFocus, row, column) -> {
            JProgressBar progressBar = new JProgressBar(0, 100);
            progressBar.setValue((int) value);
            progressBar.setStringPainted(true);
            return progressBar;
        });

        // Rendering buttons on Actions column
        activeOrdersTable.getColumn("Actions").setCellRenderer(new ButtonRenderer(ButtonMode.VIEWORDER));
        activeOrdersTable.getColumn("Actions").setCellEditor(new ButtonEditor(activeOrdersTable, ButtonMode.VIEWORDER));

        tableScrollPane = new JScrollPane(activeOrdersTable);
        activeOrdersPanel.add(activeOrdersLabel, BorderLayout.NORTH);
        activeOrdersPanel.add(tableScrollPane, BorderLayout.CENTER);
        getContentPane().add(activeOrdersPanel, BorderLayout.CENTER);
    }

    // Menu actions
    private void showDashboard() {
        this.dispose();
        new CustomerDashboard((CustomerDTO) SessionControlService.getUser()).setVisible(true);
    }

    private void openVendorPage() {
        new CustomerVendorPage().setVisible(true);
    }

    private void openOrderPage() {
        new CustomerOrderPage().setVisible(true);
    }

    private void openTransactionPage() {
        new CustomerTransactionPage().setVisible(true);
    }

    private void openComplaintPage() {
        new CustomerComplaintPage().setVisible(true);
    }

    private void openNotificationPage() {
        new NotificationPage().setVisible(true);
    }

    private void openProfilePage() {
        new CustomerProfilePage((CustomerDTO) SessionControlService.getUser()).setVisible(true);
    }

    // Populating the table
    private Object[][] getActiveOrders() {
        List<OrderDTO> orders = OrderService.readCustomerOrders(customer.getId());
        // First, filter out delivered orders and null orders
        List<OrderDTO> activeOrders = orders.stream()
                .filter(order -> order != null && order.getStatus() != OrderStatus.DELIVERED)
                .filter(order -> order != null && order.getStatus() != OrderStatus.DINE_IN)
                .filter(order -> order != null && order.getStatus() != OrderStatus.CANCELLED)
                .filter(order -> order != null && order.getStatus() != OrderStatus.FAILED)
                .toList();

        Object[][] data = new Object[activeOrders.size()][7];
        int index = 0; // Use separate index since we're skipping some orders

        for (OrderDTO order : activeOrders) {
            try {
                VendorDTO vendor = VendorService.readVendor(order.getVendorId());
                if (vendor == null) {
                    continue; // Skip if vendor not found
                }

                data[index][0] = order.getId(); // Order ID
                data[index][1] = vendor.getVendorName(); // Vendor Name
                data[index][2] = vendor.getVendorType(); // Vendor Type
                data[index][3] = processItemList(order.getVendorId(), order.getItems()); // Item
                data[index][4] = getProgress(order.getStatus()); // Progress
                data[index][5] = order.getStatus(); // Status
                data[index][6] = "View"; // Actions
                index++;
            } catch (Exception e) {
                System.err.println("Error processing order " + order.getId() + ": " + e.getMessage());
            }
        }

        // Trim array if needed due to skipped orders
        if (index < data.length) {
            Object[][] trimmedData = new Object[index][7];
            System.arraycopy(data, 0, trimmedData, 0, index);
            data = trimmedData;
        }

        activeOrdersLabel.setText("Active Orders (" + index + ")");
        return data;
    }

    // Process item list to display in the table
    private String processItemList(String vendorId, HashMap<String, Integer> items) {
        try {
            CompletableFuture<String> itemFuture = ItemProcessor.processItemListAsync(vendorId, items);
            return itemFuture.join();
        } catch (Exception e) {
            System.err.println("Error processing item list: " + e.getMessage());
            return "Error";
        }
    }

    // Get progress based on order status
    private int getProgress(OrderStatus status) {
        switch (status) {
            case PENDING:
                return 0;
            case RUNNER_ASSIGNED:
                return 10;
            case PROCESSING:
                return 25;
            case READY_FOR_PICKUP:
                return 50;
            case ON_THE_WAY:
                return 75;
            case READY_FOR_TAKEAWAY:
                return 75;
            case DELIVERED:
                return 100;
            case DINE_IN:
                return 100;
            default:
                return 0;
        }
    }

}
