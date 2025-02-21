package ui.complaint;

import java.awt.BorderLayout;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import dto.ComplaintDTO;
import enumeration.ButtonMode;
import enumeration.ComplaintStatus;
import service.complaint.ComplaintService;
import service.general.SessionControlService;
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
		searchButton.addActionListener(e -> {
			String keyword = searchField.getText();
			if (keyword.isEmpty()) {
				return;
			}
			// Search by keyword
			searchByKeyword(keyword);
		});

		statusFilter = new JComboBox<>(ComplaintStatus.values());
		statusFilter.addActionListener(e -> {
			filterByStatus((ComplaintStatus) statusFilter.getSelectedItem());
		});

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

		Object[][] data = getComplaintData();

		for (Object[] row : data) {
			tableModel.addRow(row);
		}

		setVisible(true);
	}

	// Method to get complaint data from database
	private Object[][] getComplaintData() {
		List<ComplaintDTO> complaints = ComplaintService.readCustomerComplaints(SessionControlService.getUser().getId());
		Object[][] data = new Object[complaints.size()][6];
		return data;
	}

	// Method to search complaints by keyword
	private void searchByKeyword(String keyword) {
		List<ComplaintDTO> complaints = ComplaintService.readCustomerComplaints(SessionControlService.getUser().getId());
		tableModel.setRowCount(0);
		for (ComplaintDTO complaint : complaints) {
			if (complaint.getDescription().toLowerCase().contains(keyword.toLowerCase())) {
				tableModel.addRow(new Object[] {complaint.getId(), complaint.getCustomerId(), complaint.getOrderId(), complaint.getDescription(), complaint.getStatus(), "View"});
			}
		}
	}

	// Method to filter complaints by status
	private void filterByStatus(ComplaintStatus status) {
		List<ComplaintDTO> complaints = ComplaintService.readCustomerComplaints(SessionControlService.getUser().getId());
		tableModel.setRowCount(0);
		for (ComplaintDTO complaint : complaints) {
			if (complaint.getStatus() == status) {
				tableModel.addRow(new Object[] {complaint.getId(), complaint.getCustomerId(), complaint.getOrderId(), complaint.getDescription(), complaint.getStatus(), "View"});
			}
		}
	}
}
