package ui.order;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import dto.OrderDTO;
import enumeration.ButtonMode;
import enumeration.OrderStatus;
import service.general.SessionControlService;
import service.order.OrderService;
import service.processor.ItemProcessor;
import ui.utils.ButtonEditor;
import ui.utils.ButtonRenderer;

public class CustomerOrderPage extends JFrame {
	private JComboBox<OrderStatus> filterComboBox;
	private JTable orderTable;
	private DefaultTableModel tableModel;
	private JScrollPane tableScrollPane;

	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

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
		filterComboBox.addActionListener(this::filterOrders);
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
		orderTable.getColumnModel().getColumn(3).setPreferredWidth(280); // Items
		orderTable.getColumnModel().getColumn(5).setPreferredWidth(200); // Status
		orderTable.getColumnModel().getColumn(6).setPreferredWidth(100); // Actions

		// Apply renderer and editor to the table
		orderTable.getColumnModel().getColumn(6).setCellRenderer(new ButtonRenderer(ButtonMode.VIEWORDERHISTORY));
		orderTable.getColumnModel().getColumn(6).setCellEditor(new ButtonEditor(orderTable, ButtonMode.VIEWORDERHISTORY));

		loadOrders();

		setVisible(true);
	}

	public void addRow(Object[] rowData) {
		tableModel.addRow(rowData);
	}

	// Load orders from the database
	private void loadOrders() {
		List<OrderDTO> orders = OrderService.readCustomerOrders(SessionControlService.getUser().getId());
		orders.sort((o1, o2) -> o2.getPlacementTime().compareTo(o1.getPlacementTime()));
		for (OrderDTO order : orders) {
			Object[] rowData = {
				order.getId(),
				order.getPlacementTime().format(formatter),
				order.getCompletionTime() == null ? "" : order.getCompletionTime().format(formatter),
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
