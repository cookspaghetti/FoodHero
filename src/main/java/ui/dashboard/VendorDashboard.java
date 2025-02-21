package ui.dashboard;

import javax.swing.*;

import dto.OrderDTO;
import dto.VendorDTO;
import enumeration.OrderStatus;
import service.general.SessionControlService;
import service.order.OrderService;
import ui.item.VendorItemPage;
import ui.login.LoginInterface;
import ui.notification.NotificationPage;
import ui.order.OrderHistoryPage;
import ui.order.VendorOrderPage;
import ui.profile.VendorProfilePage;
import ui.revenue.VendorRevenuePage;
import ui.review.VendorReviewPage;
import java.awt.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class VendorDashboard extends JFrame {
	private VendorDTO vendor;

	private String[] activeOrders;
	private String[] topSales;
	private String[] earningsToday;
	private int numActiveOrders = 0;

	public VendorDashboard(VendorDTO vendor) {
		this.vendor = vendor;
		initComponents();
	}

	private void initComponents() {
		setTitle("Vendor Dashboard");
		setSize(800, 600);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null); // Center the window
		getContentPane().setLayout(new BorderLayout()); // Define layout for the JFrame

		// ======= Menu Bar =======
		JMenuBar menuBar = new JMenuBar();

		// Home Menu
		JMenu homeMenu = new JMenu("Home");
		JMenuItem dashboardItem = new JMenuItem("Dashboard");
		dashboardItem.addActionListener(e -> showDashboard());
		homeMenu.add(dashboardItem);
		
		// Order Menu
		JMenu orderMenu = new JMenu("Order");
		JMenuItem orderItem = new JMenuItem("All Orders");
		JMenuItem orderHistoryItem = new JMenuItem("Order History");
		orderItem.addActionListener(e -> openOrderPage());
		orderHistoryItem.addActionListener(e -> openOrderHistoryPage());
		orderMenu.add(orderItem);
		orderMenu.add(orderHistoryItem);
		
		// Item Menu
        JMenu itemMenu = new JMenu("Item");
        JMenuItem itemMenuItem = new JMenuItem("Item Management");
		itemMenuItem.addActionListener(e -> openItemPage());
        itemMenu.add(itemMenuItem);
        
        // Review Menu
        JMenu reviewMenu = new JMenu("Review");
        JMenuItem reviewItem = new JMenuItem("Reviews");
        reviewItem.addActionListener(e -> openReviewPage());
        reviewMenu.add(reviewItem);
        
        // Revenue Menu
        JMenu revenueMenu = new JMenu("Revenue");
		JMenuItem revenueItem = new JMenuItem("Revenue Management");
		revenueItem.addActionListener(e -> openRevenuePage());
		revenueMenu.add(revenueItem);

		// Notification Menu
		JMenu notificationMenu = new JMenu("Notification");
		JMenuItem notificationItem = new JMenuItem("View Notifications");
		notificationItem.addActionListener(e -> openNotificationPage());
		notificationMenu.add(notificationItem);

		// Profile Menu
		JMenu profileMenu = new JMenu("Profile");
		JMenuItem profileItem = new JMenuItem("Profile Management");
		profileItem.addActionListener(e -> openProfilePage());
		profileMenu.add(profileItem);
        
        menuBar.add(homeMenu);
        menuBar.add(orderMenu);
        menuBar.add(itemMenu);
        menuBar.add(reviewMenu);
        menuBar.add(revenueMenu);
        setJMenuBar(menuBar);

		// ======= Header Panel (Welcome & Logout) =======
		JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		JLabel welcomeLabel = new JLabel("Welcome, " + vendor.getName());
		JButton logoutButton = new JButton("Logout");
		logoutButton.setFocusable(false);

		// Logout Button Action
		logoutButton.addActionListener(e -> {
			new LoginInterface().setVisible(true);
			this.dispose();
		});

		headerPanel.add(welcomeLabel);
		headerPanel.add(logoutButton);

		getContentPane().add(headerPanel, BorderLayout.NORTH);

		// ======= Data Panel =======
		JPanel dataPanel = new JPanel(new GridLayout(3, 2, 15, 10)); // 3 rows, 2 columns with gaps

		// Get data to populate the dashboard
		activeOrders = getActiveOrders();
		topSales = getTopSales();
		earningsToday = getEarningsToday();

		JLabel vendorsLabel = new JLabel("Active Orders (" + numActiveOrders + "):");
		vendorsLabel.setFont(new Font("Tahoma", Font.BOLD, 16));
		vendorsLabel.setHorizontalAlignment(SwingConstants.CENTER);
		JList<String> vendorsList = new JList<>(activeOrders);

		JLabel runnersLabel = new JLabel("Top Sales:");
		runnersLabel.setFont(new Font("Tahoma", Font.BOLD, 16));
		runnersLabel.setHorizontalAlignment(SwingConstants.CENTER);
		JList<String> runnersList = new JList<>(topSales);

		JLabel ordersLabel = new JLabel("Earnings Today:");
		ordersLabel.setFont(new Font("Tahoma", Font.BOLD, 16));
		ordersLabel.setHorizontalAlignment(SwingConstants.CENTER);
		JList<String> ordersList = new JList<>(earningsToday);

		// Add components to the data panel
		dataPanel.add(vendorsLabel);
		dataPanel.add(new JScrollPane(vendorsList));
		dataPanel.add(runnersLabel);
		dataPanel.add(new JScrollPane(runnersList));
		dataPanel.add(ordersLabel);
		dataPanel.add(new JScrollPane(ordersList));

		getContentPane().add(dataPanel, BorderLayout.CENTER);
	}

	// ======= Menu Action Methods =======
	private void showDashboard() {
		this.dispose();
		new VendorDashboard((VendorDTO) SessionControlService.getUser()).setVisible(true);
	}
	
	private void openOrderPage() {
		new VendorOrderPage().setVisible(true);
	}
	
	private void openOrderHistoryPage() {
		new OrderHistoryPage().setVisible(true);
	}
	
	private void openItemPage() {
		new VendorItemPage().setVisible(true);
	}
	
	private void openReviewPage() {
		new VendorReviewPage((VendorDTO) SessionControlService.getUser()).setVisible(true);
	}

	private void openRevenuePage() {
		new VendorRevenuePage().setVisible(true);
	}

	private void openNotificationPage() {
		new NotificationPage().setVisible(true);
	}

	private void openProfilePage() {
		new VendorProfilePage((VendorDTO) SessionControlService.getUser()).setVisible(true);
	}
	
	// ======= Data Methods =======
	private String[] getActiveOrders() {
		List<String> activeOrders = new ArrayList<>();
		List<OrderDTO> orders = OrderService.readVendorOrders(vendor.getId());
		for (OrderDTO order : orders) {
			if (order.getStatus().equals(OrderStatus.PENDING) || order.getStatus().equals(OrderStatus.PROCESSING) || order.getStatus().equals(OrderStatus.ON_THE_WAY) || 
				order.getStatus().equals(OrderStatus.READY_FOR_PICKUP)) {
				activeOrders.add(order.getId());
			}
		}
		numActiveOrders = activeOrders.size();
		return activeOrders.toArray(new String[0]);
	}

	private String[] getTopSales() {
		List<OrderDTO> orders = OrderService.readVendorOrders(vendor.getId());
		HashMap<String, Integer> salesByItems = new HashMap<>();
		List<String> topSales = new ArrayList<>();
		LocalDate currentDate = LocalDate.now();

		for (OrderDTO order : orders) {
			LocalDate orderDate = order.getPlacementTime().toLocalDate();
			if (orderDate.equals(currentDate) && order.getStatus().equals(OrderStatus.DELIVERED)) {
				HashMap<String, Integer> items = order.getItems();
				for (String item : items.keySet()) {
					if (salesByItems.containsKey(item)) {
						salesByItems.put(item, salesByItems.get(item) + items.get(item));
					} else {
						salesByItems.put(item, items.get(item));
					}
				}
			}
		}

		// Get the top 10 sales
		salesByItems.entrySet().stream()
			.sorted((item1, item2) -> item2.getValue().compareTo(item1.getValue()))
			.limit(10)
			.forEach(item -> topSales.add(item.getKey() + " - " + item.getValue()));
		
		return topSales.toArray(new String[0]);
	}

	private String[] getEarningsToday() {
		List<OrderDTO> orders = OrderService.readVendorOrders(vendor.getId());
		double earnings = 0.0;
		LocalDate currentDate = LocalDate.now();

		for (OrderDTO order : orders) {
			LocalDate orderDate = order.getPlacementTime().toLocalDate();
			if (orderDate.equals(currentDate) && order.getStatus().equals(OrderStatus.DELIVERED)) {
				earnings += order.getTotalAmount();
			}
		}
		earnings = earnings * 0.9; // 10% commission fee
		return new String[]{String.format("RM %.2f", earnings)};
	}
}
