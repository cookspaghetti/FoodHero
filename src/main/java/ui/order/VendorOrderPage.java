package ui.order;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import dto.OrderDTO;
import enumeration.ButtonMode;
import enumeration.OrderStatus;
import service.general.SessionControlService;
import service.order.OrderService;
import service.processor.ItemProcessor;
import ui.utils.ButtonEditor;
import ui.utils.ButtonRenderer;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CompletableFuture;

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
		filterComboBox.addActionListener(this::filterOrders);
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
		orderTable.getColumnModel().getColumn(2).setPreferredWidth(200); // Items
		orderTable.getColumnModel().getColumn(3).setPreferredWidth(200); // Notes
		orderTable.getColumnModel().getColumn(5).setPreferredWidth(200); // Status
		orderTable.getColumnModel().getColumn(6).setPreferredWidth(100); // Actions

		// Apply renderer and editor to the table
		orderTable.getColumnModel().getColumn(6).setCellRenderer(new ButtonRenderer(ButtonMode.UPDATE));
		orderTable.getColumnModel().getColumn(6).setCellEditor(new ButtonEditor(orderTable, ButtonMode.UPDATE));

		loadOrders();

		setVisible(true);
	}

	public void addRow(Object[] rowData) {
		tableModel.addRow(rowData);
	}

	// Load orders from the database
	private void loadOrders() {
		List<OrderDTO> orders = OrderService.readVendorOrders(SessionControlService.getUser().getId());
		orders.sort((o1, o2) -> o2.getPlacementTime().compareTo(o1.getPlacementTime()));
		for (OrderDTO order : orders) {
			Object[] rowData = {
				order.getId(),
				order.getPlacementTime(),
				order.getCompletionTime(),
				processItemList(order.getVendorId(), order.getItems()),
				order.getTotalAmount(),
				order.getStatus(),
				"View History"
			};
			addRow(rowData);
		}
	}

	// Filter orders based on the selected status
	private void filterOrders(ActionEvent e) {
		OrderStatus selectedStatus = (OrderStatus) filterComboBox.getSelectedItem();
		tableModel.setRowCount(0);
		loadOrders();
		for (int i = 0; i < tableModel.getRowCount(); i++) {
			if (!tableModel.getValueAt(i, 5).equals(selectedStatus)) {
				tableModel.removeRow(i);
				i--;
			}
		}
	}

	// Process item list to display in the table
	private String processItemList(String vendorId, HashMap<String, Integer> items) {
		try{
			CompletableFuture<String> itemFuture = ItemProcessor.processItemListAsync(vendorId, items);
			return itemFuture.join();
		} catch (Exception e) {
			System.err.println("Error processing item list: " + e.getMessage());
			return "Error";
		}
	}

}

