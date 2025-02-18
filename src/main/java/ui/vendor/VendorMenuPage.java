package ui.vendor;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import enumeration.ButtonMode;
import ui.utils.ButtonEditor;
import ui.utils.ButtonRenderer;

import java.awt.*;

public class VendorMenuPage extends JFrame {

	DefaultTableModel tableModel;

	public VendorMenuPage(String vendorId) {
		setTitle("Vendor Menu");
		setSize(800, 600);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setLayout(new BorderLayout());

		// Header Panel
		JPanel headerPanel = new JPanel(new BorderLayout());
		JLabel titleLabel = new JLabel("Menu", SwingConstants.LEFT);
		titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
		JButton cartButton = new JButton("Cart");
		cartButton.addActionListener(e -> {
			new VendorCartPage(vendorId);
		});

		headerPanel.add(titleLabel, BorderLayout.WEST);
		headerPanel.add(cartButton, BorderLayout.EAST);
		add(headerPanel, BorderLayout.NORTH);

		// Table Model
		String[] columnNames = {"Item ID", "Item Name", "Price", "Description", "Actions"};
		tableModel = new DefaultTableModel(columnNames, 0) {
			@Override
			public boolean isCellEditable(int row, int column) {
				return column == 4; // Make only the "Action" column editable
			}
		};
		JTable menuTable = new JTable(tableModel);
		menuTable.setRowHeight(40);

		// Apply renderer and editor to the table
		menuTable.getColumnModel().getColumn(4).setCellRenderer(new ButtonRenderer(ButtonMode.ADDTOCART));
		menuTable.getColumnModel().getColumn(4).setCellEditor(new ButtonEditor(menuTable, ButtonMode.ADDTOCART));
		
		// Sample Data (Replace with real vendor data)
		Object[][] sampleData = {
				{"I001", "Burger", "$5.00", "Delicious beef burger", "Add to Cart"},
				{"I002", "Pizza", "$8.00", "Cheesy pepperoni pizza", "Add to Cart"},
				{"I003", "Pasta", "$7.00", "Creamy Alfredo pasta", "Add to Cart"}
		};

		for (Object[] row : sampleData) {
			tableModel.addRow(row);
		}

		JScrollPane scrollPane = new JScrollPane(menuTable);
		add(scrollPane, BorderLayout.CENTER);

		setLocationRelativeTo(null);
		setVisible(true);
	}

	public static void main(String[] args) {
		new VendorMenuPage("VEN00001");
	}
}

