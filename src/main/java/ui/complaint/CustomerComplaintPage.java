package ui.complaint;

import java.awt.BorderLayout;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import enumeration.ButtonMode;
import enumeration.ComplaintStatus;
import ui.utils.ButtonEditor;
import ui.utils.ButtonRenderer;
import ui.utils.MultiLineRenderer;

public class CustomerComplaintPage extends JFrame {
	private JTextField searchField;
	private JButton searchButton;
	private JComboBox<ComplaintStatus> statusFilter;
	private JTable complaintTable;
	private JScrollPane tableScrollPane;
	private DefaultTableModel tableModel;

	public CustomerComplaintPage() {
		setTitle("Complaint Management");
		setSize(800, 600);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setLocationRelativeTo(null);
		setLayout(new BorderLayout());

		// Search Panel
		JPanel searchPanel = new JPanel();
		searchField = new JTextField(20);
		searchButton = new JButton("Search");
		statusFilter = new JComboBox<>(ComplaintStatus.values());

		searchPanel.add(new JLabel("Search Complaint:"));
		searchPanel.add(searchField);
		searchPanel.add(searchButton);
		searchPanel.add(new JLabel("Filter by Status:"));
		searchPanel.add(statusFilter);
		add(searchPanel, BorderLayout.NORTH);

		// Table Panel
		String[] columnNames = {"Complaint ID", "Customer ID", "Order ID", "Description", "Status", "Actions"};
		tableModel = new DefaultTableModel(columnNames, 0) {
			@Override
			public boolean isCellEditable(int row, int column) {
				return column == 5; // Make only the "Action" column editable
			}
		};
		complaintTable = new JTable(tableModel);
		tableScrollPane = new JScrollPane(complaintTable);
		add(tableScrollPane, BorderLayout.CENTER);
		complaintTable.setRowHeight(40);

		// Apply renderer and editor to the table
		complaintTable.getColumnModel().getColumn(5).setCellRenderer(new ButtonRenderer(ButtonMode.VIEW));
		complaintTable.getColumnModel().getColumn(5).setCellEditor(new ButtonEditor(complaintTable, ButtonMode.VIEW));

		// Apply multi line renderer to description
		complaintTable.getColumnModel().getColumn(3).setCellRenderer(new MultiLineRenderer());

		// Set column widths
		complaintTable.getColumnModel().getColumn(0).setPreferredWidth(80); // Complaint ID
		complaintTable.getColumnModel().getColumn(1).setPreferredWidth(80); // Customer ID
		complaintTable.getColumnModel().getColumn(2).setPreferredWidth(80); // Order ID
		complaintTable.getColumnModel().getColumn(3).setPreferredWidth(300); // Description
		complaintTable.getColumnModel().getColumn(4).setPreferredWidth(100); // Status
		complaintTable.getColumnModel().getColumn(5).setPreferredWidth(80); // Actions

		Object[][] sampleData = {
				{"C001", "U123", "O567", "Received a defective product. Requesting a replacement.", "Pending", "View"},
				{"C002", "U456", "O789", "Delivery was delayed by 5 days, highly dissatisfied.", "Resolved", "View"},
				{"C003", "U789", "O345", "Wrong item received. Need a refund or exchange.", "In Progress", "View"},
				{"C004", "U234", "O678", "Customer service was unresponsive to my queries.", "Escalated", "View"},
				{"C005", "U567", "O901", "Product description was misleading. Want to return.", "Closed", "View"}
		};

		for (Object[] row : sampleData) {
			tableModel.addRow(row);
		}

		setVisible(true);
	}

	public static void main(String[] args) {
		new CustomerComplaintPage();
	}
}
