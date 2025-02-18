package ui.dashboard;

import javax.swing.*;

import dto.VendorDTO;
import service.general.SessionControlService;
import ui.item.VendorItemPage;
import ui.login.LoginInterface;
import ui.notification.NotificationPage;
import ui.order.OrderHistoryPage;
import ui.order.VendorOrderPage;
import ui.profile.VendorProfilePage;
import ui.revenue.VendorRevenuePage;
import ui.review.VendorReviewPage;
import java.awt.*;

public class VendorDashboard extends JFrame {
	private VendorDTO vendor;

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

		JLabel vendorsLabel = new JLabel("Active Orders (" +  10 + ")");
		vendorsLabel.setFont(new Font("Tahoma", Font.BOLD, 16));
		vendorsLabel.setHorizontalAlignment(SwingConstants.CENTER);
		JList<String> vendorsList = new JList<>(new String[]{"Vendor A", "Vendor B"}); // Example data

		JLabel runnersLabel = new JLabel("Top Sales:");
		runnersLabel.setFont(new Font("Tahoma", Font.BOLD, 16));
		runnersLabel.setHorizontalAlignment(SwingConstants.CENTER);
		JList<String> runnersList = new JList<>(new String[]{"Runner X", "Runner Y"});

		JLabel ordersLabel = new JLabel("Earnings Today:");
		ordersLabel.setFont(new Font("Tahoma", Font.BOLD, 16));
		ordersLabel.setHorizontalAlignment(SwingConstants.CENTER);
		JList<String> ordersList = new JList<>(new String[]{"Order #123", "Order #456"});

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
	
	public static void main(String[] args) {
		VendorDTO vendor = new VendorDTO();
		vendor.setName("Alex"); // Example admin name
		new VendorDashboard(vendor).setVisible(true);
	}
}
