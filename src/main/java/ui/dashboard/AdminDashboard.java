package ui.dashboard;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;

import javax.swing.*;
import dto.AdminDTO; // Assuming you have AdminDTO for user details
import enumeration.Role;
import service.general.SessionControlService;
import ui.login.LoginInterface;
import ui.topup.AdminTopUpPage;
import ui.user.AdminPage;
import ui.user.CustomerPage;
import ui.user.ManagerPage;
import ui.user.RunnerPage;
import ui.user.VendorPage;

import java.awt.Font;

public class AdminDashboard extends JFrame {
	private AdminDTO admin;

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

		// Add menus to the menu bar
		menuBar.add(homeMenu);
		menuBar.add(userMenu);
		menuBar.add(topUpMenu);

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

	private void openTopUpPage() {
		new AdminTopUpPage().setVisible(true);
	}
	
	public static void main(String[] args) {
		AdminDTO admin = new AdminDTO();
		admin.setName("Alex"); // Example admin name
		new AdminDashboard(admin).setVisible(true);
	}
}
