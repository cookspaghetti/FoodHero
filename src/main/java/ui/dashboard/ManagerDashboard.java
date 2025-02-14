package ui.dashboard;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;

import dto.AdminDTO;
import dto.ManagerDTO;
import enumeration.Role;
import service.general.SessionControlService;
import ui.complaint.ComplaintPage;
import ui.item.ItemPage;
import ui.login.LoginInterface;
import ui.performance.PerformancePage;
import ui.revenue.RevenuePage;
import ui.topup.TopUpPage;
import ui.user.AdminPage;
import ui.user.CustomerPage;
import ui.user.ManagerPage;
import ui.user.RunnerPage;
import ui.user.VendorPage;

public class ManagerDashboard extends JFrame {
	private ManagerDTO manager;

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

		// Revenue Menu
		JMenu performanceMenu = new JMenu("Performance");
		JMenuItem performanceItem = new JMenuItem("Performance Management");
		performanceItem.addActionListener(e -> openPerformancePage());
		performanceMenu.add(performanceItem);

		// Complaint Menu
		JMenu complaintMenu = new JMenu("Complaint");
		JMenuItem complaintItem = new JMenuItem("Complaint Management");
		complaintItem.addActionListener(e -> openComplaintPage());
		complaintMenu.add(complaintItem);

		// Add menus to the menu bar
		menuBar.add(homeMenu);
		menuBar.add(itemMenu);
		menuBar.add(revenueMenu);
		menuBar.add(performanceMenu);
		menuBar.add(complaintMenu);

		setJMenuBar(menuBar);

		// ======= Header Panel (Welcome & Logout) =======
		JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		JLabel welcomeLabel = new JLabel("Welcome, " + manager.getName());
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

		JLabel vendorsLabel = new JLabel("Active Vendors (" +  10 + ")");
		vendorsLabel.setFont(new Font("Tahoma", Font.BOLD, 16));
		vendorsLabel.setHorizontalAlignment(SwingConstants.CENTER);
		JList<String> vendorsList = new JList<>(new String[]{"Vendor A", "Vendor B"}); // Example data

		JLabel runnersLabel = new JLabel("Active Runners:");
		runnersLabel.setFont(new Font("Tahoma", Font.BOLD, 16));
		runnersLabel.setHorizontalAlignment(SwingConstants.CENTER);
		JList<String> runnersList = new JList<>(new String[]{"Runner X", "Runner Y"});

		JLabel ordersLabel = new JLabel("Active Orders:");
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
		new ManagerDashboard((ManagerDTO) SessionControlService.getUser()).setVisible(true);
	}

	private void openItemPage() {
		new ItemPage().setVisible(true);
	}
	
	private void openRevenuePage() {
//		new RevenuePage().setVisible(true);
	}
	
	private void openPerformancePage() {
//		new PerformancePage().setVisible(true);
	}
	
	private void openComplaintPage() {
//		new ComplaintPage().setVisible(true);
	}

	public static void main(String[] args) {
		ManagerDTO manager =  new ManagerDTO();
		manager.setName("Alex"); // Example admin name
		new ManagerDashboard(manager).setVisible(true);
	}
}
