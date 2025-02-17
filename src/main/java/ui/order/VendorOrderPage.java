package ui.order;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import enumeration.ButtonMode;
import enumeration.OrderStatus;
import ui.utils.ButtonEditor;
import ui.utils.ButtonRenderer;

import java.awt.*;

public class VendorOrderPage extends JFrame {
	private JComboBox<OrderStatus> filterComboBox;
	private JTable orderTable;
	private DefaultTableModel tableModel;
	private JScrollPane tableScrollPane;

	public VendorOrderPage() {
		setTitle("Vendor Orders");
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
		String[] columnNames = {"Order Id", "Time", "Items", "Notes", "Amount", "Status", "Actions"};
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
		orderTable.getColumnModel().getColumn(1).setPreferredWidth(130); // Time
		orderTable.getColumnModel().getColumn(2).setPreferredWidth(200); // Items (maximized)
		orderTable.getColumnModel().getColumn(3).setPreferredWidth(200); // Notes (maximized)
		orderTable.getColumnModel().getColumn(5).setPreferredWidth(200); // Status
		orderTable.getColumnModel().getColumn(6).setPreferredWidth(100); // Actions (fit one button)

		// Apply renderer and editor to the table
		orderTable.getColumnModel().getColumn(6).setCellRenderer(new ButtonRenderer(ButtonMode.UPDATE));
		orderTable.getColumnModel().getColumn(6).setCellEditor(new ButtonEditor(orderTable, ButtonMode.UPDATE));

		addRow(new Object[]{"ORD00001", "2025-02-13 12:30", "Burger, Fries", "Extra ketchup", "RM25.50", OrderStatus.READY_FOR_TAKEAWAY, "Delete"});

		setVisible(true);
	}

	public void addRow(Object[] rowData) {
		tableModel.addRow(rowData);
	}

	public static void main(String[] args) {
		new VendorOrderPage();
	}
}

