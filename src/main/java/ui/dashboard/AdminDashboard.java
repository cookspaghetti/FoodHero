package ui.dashboard;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;
import dto.AdminDTO;
import dto.OrderDTO;
import dto.RunnerDTO;
import dto.VendorDTO;
import enumeration.OrderStatus;
import enumeration.Role;
import service.general.SessionControlService;
import service.order.OrderService;
import service.runner.RunnerService;
import service.vendor.VendorService;
import ui.login.LoginInterface;
import ui.notification.NotificationPage;
import ui.profile.AdminProfilePage;
import ui.topup.AdminTopUpPage;
import ui.user.AdminPage;
import ui.user.CustomerPage;
import ui.user.ManagerPage;
import ui.user.RunnerPage;
import ui.user.VendorPage;

import java.awt.Font;

public class AdminDashboard extends JFrame {
	private AdminDTO admin;

	private String[] activeVendors;
	private String[] activeRunners;
	private String[] activeOrders;
	private int numActiveVendors = 0;
	private int numActiveRunners = 0;
	private int numActiveOrders = 0;

	public AdminDashboard(AdminDTO admin) {
		this.admin = admin;
		initComponents();
	}

	private void initComponents() {
		setTitle("Admin Dashboard");
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

		// User Menu
		JMenu userMenu = new JMenu("User");

		JMenuItem adminItem = new JMenuItem("Admin");
		adminItem.addActionListener(e -> openUserPage(Role.ADMIN));

		JMenuItem managerItem = new JMenuItem("Manager");
		managerItem.addActionListener(e -> openUserPage(Role.MANAGER));

		JMenuItem customerItem = new JMenuItem("Customer");
		customerItem.addActionListener(e -> openUserPage(Role.CUSTOMER));

		JMenuItem vendorItem = new JMenuItem("Vendor");
		vendorItem.addActionListener(e -> openUserPage(Role.VENDOR));

		JMenuItem runnerItem = new JMenuItem("Runner");
		runnerItem.addActionListener(e -> openUserPage(Role.RUNNER));

		userMenu.add(adminItem);
		userMenu.add(managerItem);
		userMenu.add(customerItem);
		userMenu.add(vendorItem);
		userMenu.add(runnerItem);

		// Top Up Menu
		JMenu topUpMenu = new JMenu("Top Up");
		JMenuItem topUpItem = new JMenuItem("Add Balance");
		topUpItem.addActionListener(e -> openTopUpPage());
		topUpMenu.add(topUpItem);

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

		// Add menus to the menu bar
		menuBar.add(homeMenu);
		menuBar.add(userMenu);
		menuBar.add(topUpMenu);
		menuBar.add(notificationMenu);
		menuBar.add(profileMenu);

		setJMenuBar(menuBar);

		// ======= Header Panel (Welcome & Logout) =======
		JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		JLabel welcomeLabel = new JLabel("Welcome, " + admin.getName());
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

		// Get active vendors, runners, and orders
		activeVendors = getActiveVendors();
		activeRunners = getActiveRunners();
		activeOrders = getActiveOrders();

		JLabel vendorsLabel = new JLabel("Active Vendors (" + numActiveVendors + ")");
		vendorsLabel.setFont(new Font("Tahoma", Font.BOLD, 16));
		vendorsLabel.setHorizontalAlignment(SwingConstants.CENTER);
		JList<String> vendorsList = new JList<>(activeVendors);

		JLabel runnersLabel = new JLabel("Active Runners (" + numActiveRunners + ")");
		runnersLabel.setFont(new Font("Tahoma", Font.BOLD, 16));
		runnersLabel.setHorizontalAlignment(SwingConstants.CENTER);
		JList<String> runnersList = new JList<>(activeRunners);

		JLabel ordersLabel = new JLabel("Active Orders (" + numActiveOrders + ")");
		ordersLabel.setFont(new Font("Tahoma", Font.BOLD, 16));
		ordersLabel.setHorizontalAlignment(SwingConstants.CENTER);
		JList<String> ordersList = new JList<>(activeOrders);

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
		new AdminDashboard((AdminDTO) SessionControlService.getUser()).setVisible(true);
	}

	private void openUserPage(Role role) {
		
		switch (role) {
		case ADMIN:
			new AdminPage().setVisible(true);
			break;
		case MANAGER:
			new ManagerPage().setVisible(true);
			break;
		case CUSTOMER:
			new CustomerPage().setVisible(true);
			break;
		case VENDOR:
			new VendorPage().setVisible(true);
			break;
		case RUNNER:
			new RunnerPage().setVisible(true);
			break;
		default:
			JOptionPane.showMessageDialog(this, "Unknown error occured. Please try again. ");
			break;
		}
	}

	// Menu Action Methods
	private void openTopUpPage() {
		new AdminTopUpPage().setVisible(true);
	}

	private void openNotificationPage() {
		new NotificationPage().setVisible(true);
	}

	private void openProfilePage() {
		new AdminProfilePage((AdminDTO) SessionControlService.getUser()).setVisible(true);
	}

	// Populate the active vendors list
	private String[] getActiveVendors() {
		List<VendorDTO> vendors = VendorService.readAllVendor();
		List<String> activeVendors = new ArrayList<>();
		for (VendorDTO vendor : vendors) {
			if (vendor.getOpen()) {
				activeVendors.add(vendor.getName());
			}
		}
		numActiveVendors = activeVendors.size();
		return activeVendors.toArray(new String[0]);
	}

	// Populate the active runners list
	private String[] getActiveRunners() {
		List<String> activeRunners = new ArrayList<>();
		List<RunnerDTO> runners = RunnerService.readAllRunner();
		for (RunnerDTO runner : runners) {
			if (runner.isAvailable()) {
				activeRunners.add(runner.getName());
			}
		}
		numActiveRunners = activeRunners.size();
		return activeRunners.toArray(new String[0]);
	}

	// Populate the active orders list
	private String[] getActiveOrders() {
		List<String> activeOrders = new ArrayList<>();
		List<OrderDTO> orders = OrderService.readAllOrder();
		for (OrderDTO order : orders) {
			if (order.getStatus().equals(OrderStatus.PENDING) || order.getStatus().equals(OrderStatus.PROCESSING) || order.getStatus().equals(OrderStatus.ON_THE_WAY) || 
				order.getStatus().equals(OrderStatus.READY_FOR_PICKUP)) {
				activeOrders.add(order.getId());
			}
		}
		numActiveOrders = activeOrders.size();
		return activeOrders.toArray(new String[0]);
	}
	
}
