package ui.dashboard;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import dto.RunnerDTO;
import enumeration.ButtonMode;
import enumeration.OrderStatus;
import enumeration.TaskStatus;
import enumeration.VendorType;
import service.general.SessionControlService;
import service.task.TaskService;
import ui.complaint.CustomerComplaintPage;
import ui.form.RunnerCurrentTaskForm;
import ui.login.LoginInterface;
import ui.notification.NotificationPage;
import ui.order.CustomerOrderPage;
import ui.profile.RunnerProfilePage;
import ui.revenue.RunnerRevenuePage;
import ui.review.RunnerReviewPage;
import ui.task.RunnerTaskHistoryPage;
import ui.transaction.CustomerTransactionPage;
import ui.utils.ButtonEditor;
import ui.utils.ButtonRenderer;
import ui.utils.MultiLineRenderer;
import ui.vendor.CustomerVendorPage;

public class RunnerDashboard extends JFrame {
	private RunnerDTO runner;

	public RunnerDashboard(RunnerDTO runner) {
		this.runner = runner;
		initComponents();
	}

	private void initComponents() {
		setTitle("Runner Dashboard");
		setSize(800, 600);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		getContentPane().setLayout(new BorderLayout());

		// ======= Menu Bar =======
		JMenuBar menuBar = new JMenuBar();

		// Home Menu
		JMenu homeMenu = new JMenu("Home");
		JMenuItem dashboardItem = new JMenuItem("Dashboard");
		dashboardItem.addActionListener(e -> showDashboard());
		homeMenu.add(dashboardItem);
		menuBar.add(homeMenu);

		// Vendor Menu
		JMenu taskMenu = new JMenu("Task");
		JMenuItem viewCurrentTask =  new JMenuItem("Current Task");
		JMenuItem viewTaskHistory = new JMenuItem("Task History");
		viewCurrentTask.addActionListener(e -> openRunnerCurrentTaskPage());
		viewTaskHistory.addActionListener(e -> openRunnerTaskHistoryPage());
		taskMenu.add(viewCurrentTask);
		taskMenu.add(viewTaskHistory);
		menuBar.add(taskMenu);

		// Order Menu
		JMenu orderMenu = new JMenu("Review");
		JMenuItem orderManagementItem = new JMenuItem("Review Management");
		orderManagementItem.addActionListener(e -> openRunnerReviewPage());
		orderMenu.add(orderManagementItem);
		menuBar.add(orderMenu);

		// Revenue Menu
		JMenu revenueMenu = new JMenu("Revenue");
		JMenuItem revenueItem = new JMenuItem("Revenue Management");
		revenueItem.addActionListener(e -> openRunnerRevenuePage());
		revenueMenu.add(revenueItem);
		menuBar.add(revenueMenu);
		
		// Notification Menu
		JMenu notificationMenu = new JMenu("Notification");
		JMenuItem notificationItem = new JMenuItem("View Notifications");
		notificationItem.addActionListener(e -> openNotificationPage());
		notificationMenu.add(notificationItem);
		menuBar.add(notificationMenu);
		
		// Profile Menu
		JMenu profileMenu = new JMenu("Profile");
		JMenuItem profileItem = new JMenuItem("Profile Management");
		profileItem.addActionListener(e -> openProfilePage());
		profileMenu.add(profileItem);
		menuBar.add(profileMenu);
		
		setJMenuBar(menuBar);

		// ======= Header Panel (Welcome & Logout) =======
		JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		JLabel welcomeLabel = new JLabel("Welcome, " + runner.getName());
		JButton logoutButton = new JButton("Logout");
		logoutButton.setFocusable(false);

		logoutButton.addActionListener(e -> {
			new LoginInterface().setVisible(true);
			this.dispose();
		});

		headerPanel.add(welcomeLabel);
		headerPanel.add(logoutButton);
		getContentPane().add(headerPanel, BorderLayout.NORTH);

		// ======= Pending Orders Table =======
		JPanel pendingOrdersPanel = new JPanel(new BorderLayout());
		JLabel pendingOrdersLabel = new JLabel("Pending Orders");

		String[] columnNames = {"Task ID", "Vendor Name", "Vendor Address", "Placement Time", "Status", "Actions"};
		DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0) {
			@Override
			public boolean isCellEditable(int row, int column) {
				return column == 5; // Make only the "Action" column editable
			}
		};
		JTable pendingOrdersTable = new JTable(tableModel);
		pendingOrdersTable.setRowHeight(35);

		pendingOrdersTable.getColumn("Task ID").setPreferredWidth(30);
		pendingOrdersTable.getColumn("Vendor Name").setPreferredWidth(125);
		pendingOrdersTable.getColumn("Vendor Address").setPreferredWidth(200);
		pendingOrdersTable.getColumn("Actions").setPreferredWidth(40);
		
		// Render vendor address cell multi line
		pendingOrdersTable.getColumnModel().getColumn(2).setCellRenderer(new MultiLineRenderer());

		// Adding sample data
		Object[][] sampleData = {
				{"TAS00001", "Vendor A", "78, Jalan Pisang, Bandar Durian, 57890 Kuala Lumpur", "2025-02-13 12:30", TaskStatus.PENDING, "View"},
				{"TAS00002", "Vendor B", "ENDAH REGAL Taman Sri Endah, Bandar Baru Sri Petaling, 57000 Kuala Lumpur 57000 Kuala Lumpur", "2025-02-13 12:30", TaskStatus.PENDING, "View"},
				{"TAS00003", "Vendor C", "78, Jalan Pisang, Bandar Durian, 57890 Sri Petaling", "2025-02-13 12:30", TaskStatus.PENDING, "View"}
		};
		for (Object[] row : sampleData) {
			tableModel.addRow(row);
		}

		// Rendering buttons on Actions column
		pendingOrdersTable.getColumn("Actions").setCellRenderer(new ButtonRenderer(ButtonMode.VIEW));
		pendingOrdersTable.getColumn("Actions").setCellEditor(new ButtonEditor(pendingOrdersTable, ButtonMode.VIEW));

		JScrollPane tableScrollPane = new JScrollPane(pendingOrdersTable);
		pendingOrdersPanel.add(pendingOrdersLabel, BorderLayout.NORTH);
		pendingOrdersPanel.add(tableScrollPane, BorderLayout.CENTER);
		getContentPane().add(pendingOrdersPanel, BorderLayout.CENTER);
	}

	private void showDashboard() {
		this.dispose();
		new RunnerDashboard((RunnerDTO) SessionControlService.getUser()).setVisible(true);
	}

	private void openRunnerTaskHistoryPage() {
		new RunnerTaskHistoryPage().setVisible(true);
	}

	private void openRunnerReviewPage() {
		new RunnerReviewPage(SessionControlService.getId()).setVisible(true);
	}

	private void openRunnerRevenuePage() {
		new RunnerRevenuePage().setVisible(true);
	}
	
	private void openRunnerCurrentTaskPage() {
		new RunnerCurrentTaskForm(TaskService.readTask(SessionControlService.getCurrentTask())).setVisible(true);
	}
	
	private void openNotificationPage() {
		new NotificationPage().setVisible(true);
	}

	private void openProfilePage() {
		new RunnerProfilePage((RunnerDTO) SessionControlService.getUser()).setVisible(true);
	}
	
	public static void main(String[] args) {
		RunnerDTO runner =  new RunnerDTO();
		runner.setName("Alex"); // Example runner name
		new RunnerDashboard(runner).setVisible(true);
	}
}
