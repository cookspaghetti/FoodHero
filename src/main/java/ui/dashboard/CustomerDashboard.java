package ui.dashboard;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import java.awt.*;

import dto.CustomerDTO;
import dto.ManagerDTO;
import enumeration.ButtonMode;
import enumeration.OrderStatus;
import service.general.SessionControlService;
import ui.item.ManagerItemPage;
import ui.login.LoginInterface;
import ui.utils.ButtonEditor;
import ui.utils.ButtonRenderer;
import ui.vendor.CustomerVendorPage;

public class CustomerDashboard extends JFrame {
	private CustomerDTO customer;

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
		JMenuBar menuBar = new JMenuBar();

		// Home Menu
		JMenu homeMenu = new JMenu("Home");
		JMenuItem dashboardItem = new JMenuItem("Dashboard");
		dashboardItem.addActionListener(e -> showDashboard());
		homeMenu.add(dashboardItem);
		menuBar.add(homeMenu);

		// Vendor Menu
		JMenu vendorMenu = new JMenu("Vendor");
		JMenuItem viewVendorsItem = new JMenuItem("View Vendors");
		viewVendorsItem.addActionListener(e -> openVendorPage());
		vendorMenu.add(viewVendorsItem);
		menuBar.add(vendorMenu);

		// Order Menu
		JMenu orderMenu = new JMenu("Order");
		JMenuItem orderManagementItem = new JMenuItem("Order Management");
		orderManagementItem.addActionListener(e -> openOrderPage());
		orderMenu.add(orderManagementItem);
		menuBar.add(orderMenu);

		// Transaction Menu
		JMenu transactionMenu = new JMenu("Transaction");
		JMenuItem transactionManagementItem = new JMenuItem("Transaction Management");
		transactionManagementItem.addActionListener(e -> openTransactionPage());
		transactionMenu.add(transactionManagementItem);
		menuBar.add(transactionMenu);

		// Complaint Menu
		JMenu complaintMenu = new JMenu("Complaint");
		JMenuItem complaintManagementItem = new JMenuItem("Complaint Management");
		complaintManagementItem.addActionListener(e -> openComplaintPage());
		complaintMenu.add(complaintManagementItem);
		menuBar.add(complaintMenu);

		setJMenuBar(menuBar);

		// ======= Header Panel (Welcome & Logout) =======
		JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		JLabel welcomeLabel = new JLabel("Welcome, " + customer.getName());
		JButton logoutButton = new JButton("Logout");
		logoutButton.setFocusable(false);

		logoutButton.addActionListener(e -> {
			new LoginInterface().setVisible(true);
			this.dispose();
		});

		headerPanel.add(welcomeLabel);
		headerPanel.add(logoutButton);
		getContentPane().add(headerPanel, BorderLayout.NORTH);

		// ======= Active Orders Table =======
		JPanel activeOrdersPanel = new JPanel(new BorderLayout());
		JLabel activeOrdersLabel = new JLabel("Active Orders");

		String[] columnNames = {"Order ID", "Vendor Name", "Item", "Progress", "Status", "Actions"};
		DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0) {
			@Override
		    public boolean isCellEditable(int row, int column) {
		        return column == 5; // Make only the "Action" column editable
		    }
		};
		JTable activeOrdersTable = new JTable(tableModel);
		activeOrdersTable.setRowHeight(40);
		
		activeOrdersTable.getColumnModel().getColumn(0).setPreferredWidth(50);
		activeOrdersTable.getColumnModel().getColumn(2).setPreferredWidth(250);
		
        // Adding sample data
        Object[][] sampleData = {
            {"ORD00001", "Vendor A", "Burger", 50, OrderStatus.ON_THE_WAY, "Cancel"},
            {"ORD00002", "Vendor B", "Pizza", 75, OrderStatus.ON_THE_WAY, "Cancel"},
            {"ORD00003", "Vendor C", "Pasta", 30, OrderStatus.ON_THE_WAY, "Cancel"}
        };
        for (Object[] row : sampleData) {
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
		activeOrdersTable.getColumnModel().getColumn(5).setCellRenderer(new ButtonRenderer(ButtonMode.VIEW));
		activeOrdersTable.getColumnModel().getColumn(5).setCellEditor(new ButtonEditor(activeOrdersTable, ButtonMode.VIEW));

		JScrollPane tableScrollPane = new JScrollPane(activeOrdersTable);
		activeOrdersPanel.add(activeOrdersLabel, BorderLayout.NORTH);
		activeOrdersPanel.add(tableScrollPane, BorderLayout.CENTER);
		getContentPane().add(activeOrdersPanel, BorderLayout.CENTER);
	}

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

	public static void main(String[] args) {
		CustomerDTO customer =  new CustomerDTO();
		customer.setName("Alex"); // Example customer name
		new CustomerDashboard(customer).setVisible(true);
	}
}
