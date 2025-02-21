package ui.dashboard;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
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
		headerPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		welcomeLabel = new JLabel("Welcome, " + runner.getName());
		logoutButton = new JButton("Logout");
		logoutButton.setFocusable(false);

		logoutButton.addActionListener(e -> {
			new LoginInterface().setVisible(true);
			this.dispose();
		});

		headerPanel.add(welcomeLabel);
		headerPanel.add(logoutButton);
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

		// Adding sample data
		Object[][] data = getPendingTasks();
		for (Object[] row : data) {
			tableModel.addRow(row);
		}

		// Rendering buttons on Actions column
		pendingOrdersTable.getColumn("Actions").setCellRenderer(new ButtonRenderer(ButtonMode.VIEW));
		pendingOrdersTable.getColumn("Actions").setCellEditor(new ButtonEditor(pendingOrdersTable, ButtonMode.VIEW));

		tableScrollPane = new JScrollPane(pendingOrdersTable);
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

	// Populate the pending orders table
	private Object[][] getPendingTasks() {
		List<TaskDTO> tasks = TaskService.readAllTask();
		List<Object[]> data = new ArrayList<>();

		for (TaskDTO task : tasks) {
			if (task.getStatus() == TaskStatus.PENDING && task.getRunnerId() == null) {
				// Read order, ensuring it's not null
				OrderDTO order = OrderService.readOrder(task.getOrderId());
				if (order == null) {
					System.err.println("Order not found for task ID: " + task.getId());
					continue;
				}

				// Read vendor, ensuring it's not null
				VendorDTO vendor = VendorService.readVendor(order.getVendorId());
				if (vendor == null) {
					System.err.println("Vendor not found for order ID: " + order.getId());
					continue;
				}

				// Read address, ensuring it's not null
				AddressDTO address = AddressService.getAddressById(vendor.getAddressId());
				String vendorAddress = (address != null) ? AddressService.concatAddress(address) : "N/A";

				// Add row to data
				Object[] row = {
						task.getId(),
						vendor.getName(),
						vendorAddress,
						order.getPlacementTime(),
						task.getStatus(),
						"View"
				};
				data.add(row);
			}
		}
		
		pendingOrdersLabel.setText("Pending Orders (" + data.size() + ")");
		return data.toArray(new Object[0][6]);
	}

}
