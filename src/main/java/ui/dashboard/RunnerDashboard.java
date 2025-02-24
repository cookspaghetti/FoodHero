package ui.dashboard;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import dto.AddressDTO;
import dto.OrderDTO;
import dto.RunnerDTO;
import dto.TaskDTO;
import dto.VendorDTO;
import enumeration.ButtonMode;
import enumeration.TaskStatus;
import service.address.AddressService;
import service.general.SessionControlService;
import service.order.OrderService;
import service.task.TaskService;
import service.vendor.VendorService;
import ui.form.RunnerCurrentTaskForm;
import ui.login.LoginInterface;
import ui.notification.NotificationPage;
import ui.profile.RunnerProfilePage;
import ui.revenue.RunnerRevenuePage;
import ui.review.RunnerReviewPage;
import ui.task.RunnerTaskHistoryPage;
import ui.utils.ButtonEditor;
import ui.utils.ButtonRenderer;
import ui.utils.MultiLineRenderer;

public class RunnerDashboard extends JFrame {
	private RunnerDTO runner;

	// Menu Bar Components
	private JMenuBar menuBar;
	private JMenu homeMenu, taskMenu, orderMenu, revenueMenu, notificationMenu, profileMenu;
	private JMenuItem dashboardItem, viewCurrentTask, viewTaskHistory, orderManagementItem,
			revenueItem, notificationItem, profileItem;

	// Header Panel Components
	private JPanel headerPanel;
	private JLabel welcomeLabel;
	private JButton logoutButton;

	// Pending Orders Panel Components
	private JPanel pendingOrdersPanel;
	private JLabel pendingOrdersLabel;
	private JTable pendingOrdersTable;
	private DefaultTableModel tableModel;
	private JScrollPane tableScrollPane;
	private String[] columnNames = { "Task ID", "Vendor Name", "Vendor Address", "Placement Time", "Status",
			"Actions" };

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
		menuBar = new JMenuBar();

		// Home Menu
		homeMenu = new JMenu("Home");
		dashboardItem = new JMenuItem("Dashboard");
		dashboardItem.addActionListener(e -> showDashboard());
		homeMenu.add(dashboardItem);
		menuBar.add(homeMenu);

		// Task Menu
		taskMenu = new JMenu("Task");
		viewCurrentTask = new JMenuItem("Current Task");
		viewTaskHistory = new JMenuItem("Task History");
		viewCurrentTask.addActionListener(e -> openRunnerCurrentTaskPage());
		viewTaskHistory.addActionListener(e -> openRunnerTaskHistoryPage());
		taskMenu.add(viewCurrentTask);
		taskMenu.add(viewTaskHistory);
		menuBar.add(taskMenu);

		// Order Menu
		orderMenu = new JMenu("Review");
		orderManagementItem = new JMenuItem("Review Management");
		orderManagementItem.addActionListener(e -> openRunnerReviewPage());
		orderMenu.add(orderManagementItem);
		menuBar.add(orderMenu);

		// Revenue Menu
		revenueMenu = new JMenu("Revenue");
		revenueItem = new JMenuItem("Revenue Management");
		revenueItem.addActionListener(e -> openRunnerRevenuePage());
		revenueMenu.add(revenueItem);
		menuBar.add(revenueMenu);

		// Notification Menu
		notificationMenu = new JMenu("Notification");
		notificationItem = new JMenuItem("View Notifications");
		notificationItem.addActionListener(e -> openNotificationPage());
		notificationMenu.add(notificationItem);
		menuBar.add(notificationMenu);

		// Profile Menu
		profileMenu = new JMenu("Profile");
		profileItem = new JMenuItem("Profile Management");
		profileItem.addActionListener(e -> openProfilePage());
		profileMenu.add(profileItem);
		menuBar.add(profileMenu);

		setJMenuBar(menuBar);

		// ======= Header Panel (Welcome & Logout) =======
		headerPanel = new JPanel(new BorderLayout());
		JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

		// Style components
		welcomeLabel = new JLabel("Welcome, " + runner.getName());
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

		// ======= Pending Orders Table =======
		pendingOrdersPanel = new JPanel(new BorderLayout());
		pendingOrdersLabel = new JLabel("Pending Orders");

		tableModel = new DefaultTableModel(columnNames, 0) {
			@Override
			public boolean isCellEditable(int row, int column) {
				return column == 5; // Make only the "Action" column editable
			}
		};
		pendingOrdersTable = new JTable(tableModel);
		pendingOrdersTable.setRowHeight(35);

		pendingOrdersTable.getColumn("Task ID").setPreferredWidth(30);
		pendingOrdersTable.getColumn("Vendor Name").setPreferredWidth(125);
		pendingOrdersTable.getColumn("Vendor Address").setPreferredWidth(200);
		pendingOrdersTable.getColumn("Actions").setPreferredWidth(40);

		// Render vendor address cell multi line
		pendingOrdersTable.getColumnModel().getColumn(2).setCellRenderer(new MultiLineRenderer());

		// Rendering buttons on Actions column
		pendingOrdersTable.getColumn("Actions").setCellRenderer(new ButtonRenderer(ButtonMode.VIEW));
		pendingOrdersTable.getColumn("Actions").setCellEditor(new ButtonEditor(pendingOrdersTable, ButtonMode.VIEW));

		tableScrollPane = new JScrollPane(pendingOrdersTable);
		pendingOrdersPanel.add(pendingOrdersLabel, BorderLayout.NORTH);
		pendingOrdersPanel.add(tableScrollPane, BorderLayout.CENTER);
		getContentPane().add(pendingOrdersPanel, BorderLayout.CENTER);

		// Adding data
		Object[][] data = getPendingTasks();
		for (Object[] row : data) {
			tableModel.addRow(row);
		}
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
		TaskDTO task = TaskService.readTask(SessionControlService.getCurrentTask());
		if (task == null) {
			JOptionPane.showMessageDialog(this, "No current task found", "Error", JOptionPane.ERROR_MESSAGE);
			return;
		}
		new RunnerCurrentTaskForm(TaskService.readTask(SessionControlService.getCurrentTask())).setVisible(true);
	}

	private void openNotificationPage() {
		new NotificationPage().setVisible(true);
	}

	private void openProfilePage() {
		new RunnerProfilePage((RunnerDTO) SessionControlService.getUser()).setVisible(true);
	}

	// Populate the pending orders table
	private Object[][] getPendingTasks() {
		List<TaskDTO> tasks = TaskService.readAllTask();
		List<Object[]> data = new ArrayList<>();

		if (tasks == null || tasks.isEmpty()) {
			System.out.println("No tasks found");
			pendingOrdersLabel.setText("Pending Orders (0)");
			return new Object[0][6];
		}

		for (TaskDTO task : tasks) {
			// Check if task is pending and belongs to current runner
			if (task != null && task.getStatus() == TaskStatus.PENDING
					&& task.getRunnerId().equals(runner.getId())) {

				try {
					// Read order
					OrderDTO order = OrderService.readOrder(task.getOrderId());
					if (order == null) {
						System.err.println("Order not found for task: " + task.getId());
						continue;
					}

					// Read vendor
					VendorDTO vendor = VendorService.readVendor(order.getVendorId());
					if (vendor == null) {
						System.err.println("Vendor not found for order: " + order.getId());
						continue;
					}

					// Read vendor address
					AddressDTO address = AddressService.getAddressById(vendor.getAddressId());
					String vendorAddress = address != null ? AddressService.concatAddress(address)
							: "Address not found";

					// Create row data
					Object[] row = new Object[] {
							task.getId(), // Task ID
							vendor.getVendorName(), // Vendor Name
							vendorAddress, // Vendor Address
							order.getPlacementTime(), // Placement Time
							task.getStatus().toString(), // Status
							"View" // Actions
					};
					data.add(row);

				} catch (Exception e) {
					System.err.println("Error processing task " + task.getId() + ": " + e.getMessage());
					e.printStackTrace();
				}
			}
		}

		// Update label with count
		pendingOrdersLabel.setText("Pending Orders (" + data.size() + ")");

		// Convert list to array
		return data.toArray(new Object[data.size()][6]);
	}

}
