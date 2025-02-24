package ui.dashboard;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;

import dto.ComplaintDTO;
import dto.ManagerDTO;
import dto.RunnerDTO;
import dto.VendorDTO;
import enumeration.ComplaintStatus;
import service.complaint.ComplaintService;
import service.general.SessionControlService;
import service.runner.RunnerService;
import service.vendor.VendorService;
import ui.complaint.ManagerComplaintPage;
import ui.item.ManagerItemPage;
import ui.login.LoginInterface;
import ui.notification.NotificationPage;
import ui.performance.ManagerPerformancePage;
import ui.profile.ManagerProfilePage;
import ui.revenue.ManagerRevenuePage;

public class ManagerDashboard extends JFrame {
	private ManagerDTO manager;

	private JPanel headerPanel;
	private JLabel welcomeLabel;
	private JButton logoutButton;

	private String[] activeVendors;
	private String[] activeRunners;
	private String[] newComplaints;
	private int numActiveVendors = 0;
	private int numActiveRunners = 0;
	private int numNewComplaints = 0;

	public ManagerDashboard(ManagerDTO manager) {
		this.manager = manager;
		initComponents();
	}

	private void initComponents() {
		setTitle("Manager Dashboard");
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

		// Item Menu
		JMenu itemMenu = new JMenu("Item");
		JMenuItem itemMenuItem = new JMenuItem("Item Management");
		itemMenuItem.addActionListener(e -> openItemPage());
		itemMenu.add(itemMenuItem);

		// Revenue Menu
		JMenu revenueMenu = new JMenu("Revenue");
		JMenuItem revenueItem = new JMenuItem("Revenue Management");
		revenueItem.addActionListener(e -> openRevenuePage());
		revenueMenu.add(revenueItem);

		// Performance Menu
		JMenu performanceMenu = new JMenu("Performance");
		JMenuItem performanceItem = new JMenuItem("Performance Management");
		performanceItem.addActionListener(e -> openPerformancePage());
		performanceMenu.add(performanceItem);

		// Complaint Menu
		JMenu complaintMenu = new JMenu("Complaint");
		JMenuItem complaintItem = new JMenuItem("Complaint Management");
		complaintItem.addActionListener(e -> openComplaintPage());
		complaintMenu.add(complaintItem);

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
		menuBar.add(itemMenu);
		menuBar.add(revenueMenu);
		menuBar.add(performanceMenu);
		menuBar.add(complaintMenu);
		menuBar.add(notificationMenu);
		menuBar.add(profileMenu);

		setJMenuBar(menuBar);

		// ======= Header Panel (Welcome & Logout) =======
		headerPanel = new JPanel(new BorderLayout());
		JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

		// Style components
		welcomeLabel = new JLabel("Welcome, " + manager.getName());
		welcomeLabel.setFont(new Font("Arial", Font.BOLD, 12));
		logoutButton = new JButton("Logout");
		logoutButton.setFocusable(false);

		// Set fonts and colors
		Font labelFont = new Font("Arial", Font.PLAIN, 12);
		welcomeLabel.setFont(labelFont);
		logoutButton.setFont(labelFont);

		// Add logout action
		logoutButton.addActionListener(e -> {
			SessionControlService.clearSession();
			new LoginInterface().setVisible(true);
			this.dispose();
		});

		// Add components to panels
		leftPanel.add(welcomeLabel);
		rightPanel.add(logoutButton);

		// Add panels to header
		headerPanel.add(leftPanel, BorderLayout.WEST);
		headerPanel.add(rightPanel, BorderLayout.EAST);

		// Add header to frame
		getContentPane().add(headerPanel, BorderLayout.NORTH);

		// ======= Data Panel =======
		JPanel dataPanel = new JPanel(new GridLayout(3, 2, 15, 10)); // 3 rows, 2 columns with gaps

		// Get active vendors and runners
		activeVendors = getActiveVendors();
		activeRunners = getActiveRunners();
		newComplaints = getNewComplaints();

		JLabel vendorsLabel = new JLabel("Active Vendors (" + numActiveVendors + ")");
		vendorsLabel.setFont(new Font("Tahoma", Font.BOLD, 16));
		vendorsLabel.setHorizontalAlignment(SwingConstants.CENTER);
		JList<String> vendorsList = new JList<>(activeVendors);

		JLabel runnersLabel = new JLabel("Active Runners: (" + numActiveRunners + ")");
		runnersLabel.setFont(new Font("Tahoma", Font.BOLD, 16));
		runnersLabel.setHorizontalAlignment(SwingConstants.CENTER);
		JList<String> runnersList = new JList<>(activeRunners);

		JLabel ordersLabel = new JLabel("New Complaints: (" + numNewComplaints + ")");
		ordersLabel.setFont(new Font("Tahoma", Font.BOLD, 16));
		ordersLabel.setHorizontalAlignment(SwingConstants.CENTER);
		JList<String> ordersList = new JList<>(newComplaints);

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
		new ManagerDashboard((ManagerDTO) SessionControlService.getUser()).setVisible(true);
	}

	private void openItemPage() {
		new ManagerItemPage().setVisible(true);
	}

	private void openRevenuePage() {
		new ManagerRevenuePage().setVisible(true);
	}

	private void openPerformancePage() {
		new ManagerPerformancePage().setVisible(true);
	}

	private void openComplaintPage() {
		new ManagerComplaintPage().setVisible(true);
	}

	private void openNotificationPage() {
		new NotificationPage().setVisible(true);
	}

	private void openProfilePage() {
		new ManagerProfilePage((ManagerDTO) SessionControlService.getUser()).setVisible(true);
	}

	// Populate the active vendors list
	private String[] getActiveVendors() {
		List<VendorDTO> vendors = VendorService.readAllVendor();
		List<String> activeVendors = new ArrayList<>();
		for (VendorDTO vendor : vendors) {
			if (vendor.getOpen()) {
				activeVendors.add(vendor.getVendorName());
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

	// Populate the new complaints list
	private String[] getNewComplaints() {
		List<String> newComplaints = new ArrayList<>();
		List<ComplaintDTO> complaints = ComplaintService.readAllComplaint();
		for (ComplaintDTO complaint : complaints) {
			if (complaint.getStatus().equals(ComplaintStatus.PENDING)) {
				newComplaints.add(complaint.getId());
			}
		}
		numNewComplaints = newComplaints.size();
		return newComplaints.toArray(new String[0]);
	}
}
