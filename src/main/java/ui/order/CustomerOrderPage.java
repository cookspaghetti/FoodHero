package ui.order;

import java.awt.BorderLayout;

import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import enumeration.ButtonMode;
import enumeration.OrderStatus;
import ui.utils.ButtonEditor;
import ui.utils.ButtonRenderer;

public class CustomerOrderPage extends JFrame {
	private JComboBox<OrderStatus> filterComboBox;
	private JTable orderTable;
	private DefaultTableModel tableModel;
	private JScrollPane tableScrollPane;

	public CustomerOrderPage() {
		setTitle("Order History");
		setSize(800, 600);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setLocationRelativeTo(null);
		setLayout(new BorderLayout());

		// Filter Panel
		JPanel filterPanel = new JPanel();
		filterPanel.add(new JLabel("Filter by Status:"));
		filterComboBox = new JComboBox<>(OrderStatus.values());
		filterPanel.add(filterComboBox);
		add(filterPanel, BorderLayout.NORTH);

		// Table Setup
		String[] columnNames = {"Order Id", "Placement Time", "Completed Time", "Items", "Amount", "Status", "Actions"};
		tableModel = new DefaultTableModel(columnNames, 0) {
			@Override
			public boolean isCellEditable(int row, int column) {
				return column == 6; // Make only the "Action" column editable
			}
		};
		orderTable = new JTable(tableModel);
		tableScrollPane = new JScrollPane(orderTable);
		add(tableScrollPane, BorderLayout.CENTER);
		orderTable.setRowHeight(40);

		// Set column widths
		orderTable.getColumnModel().getColumn(0).setPreferredWidth(90);  // Id
		orderTable.getColumnModel().getColumn(1).setPreferredWidth(140); // Time
		orderTable.getColumnModel().getColumn(2).setPreferredWidth(140); // Time
		orderTable.getColumnModel().getColumn(3).setPreferredWidth(280); // Items (maximized)
		orderTable.getColumnModel().getColumn(5).setPreferredWidth(200); // Status
		orderTable.getColumnModel().getColumn(6).setPreferredWidth(100); // Actions (fit one button)

		// Apply renderer and editor to the table
		orderTable.getColumnModel().getColumn(6).setCellRenderer(new ButtonRenderer(ButtonMode.VIEWORDERHISTORY));
		orderTable.getColumnModel().getColumn(6).setCellEditor(new ButtonEditor(orderTable, ButtonMode.VIEWORDERHISTORY));

		addRow(new Object[]{"ORD00001", "2025-02-13 12:30", "2025-02-13 12:30", "Burger, Fries", "RM25.50", OrderStatus.READY_FOR_TAKEAWAY, "Delete"});

		setVisible(true);
	}

	public void addRow(Object[] rowData) {
		tableModel.addRow(rowData);
	}

	public static void main(String[] args) {
		new CustomerOrderPage();
	}
}
