package ui.vendor;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import dto.CustomerDTO;
import dto.VendorDTO;
import enumeration.ButtonMode;
import enumeration.VendorType;
import service.general.SessionControlService;
import ui.dashboard.CustomerDashboard;
import ui.utils.ButtonEditor;
import ui.utils.ButtonRenderer;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class CustomerVendorPage extends JFrame {
    private JTable vendorTable;
    private DefaultTableModel tableModel;
    private JComboBox<VendorType> vendorTypeFilter;
    private JButton sortButton;
    private List<VendorDTO> vendorList;
    private String selectedAddress;

    public CustomerVendorPage() {
    	
        if (!promptForAddress(SessionControlService.getDeliveryAddresses())) {
            JOptionPane.showMessageDialog(null, "You must select an address to proceed.", "Address Required", JOptionPane.WARNING_MESSAGE);
            this.dispose();
    		new CustomerDashboard((CustomerDTO) SessionControlService.getUser()).setVisible(true);
            return;
        }
        
        // Compare postcode
        
        this.vendorList = new ArrayList<>();
        setTitle("Vendors");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setLayout(new BorderLayout());

        // Filter Panel
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        filterPanel.add(new JLabel("Filter by Vendor Type:"));
        vendorTypeFilter = new JComboBox<>(VendorType.values());
        vendorTypeFilter.addActionListener(e -> filterVendors());
        filterPanel.add(vendorTypeFilter);

        sortButton = new JButton("Sort by Delivery Fee");
        sortButton.addActionListener(e -> sortVendors());
        filterPanel.add(sortButton);

        getContentPane().add(filterPanel, BorderLayout.NORTH);

        // Vendor Table
        String[] columnNames = {"Vendor ID", "Vendor Name", "Vendor Type", "Delivery Fee", "Actions"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 4; // Make only the "Action" column editable
            }
        };

        vendorTable = new JTable(tableModel);
        vendorTable.setRowHeight(40);
        JScrollPane scrollPane = new JScrollPane(vendorTable);
        getContentPane().add(scrollPane, BorderLayout.CENTER);

        // Apply renderer and editor to the table
        vendorTable.getColumnModel().getColumn(4).setCellRenderer(new ButtonRenderer(ButtonMode.VIEW));
        vendorTable.getColumnModel().getColumn(4).setCellEditor(new ButtonEditor(vendorTable, ButtonMode.VIEW));

        loadVendorData(vendorList);
    }

    private boolean promptForAddress(List<String> addresses) {
        if (addresses == null || addresses.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No available addresses.", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        JComboBox<String> addressComboBox = new JComboBox<>(addresses.toArray(new String[0]));
        int result = JOptionPane.showConfirmDialog(null, addressComboBox, "Select Delivery Address", JOptionPane.OK_CANCEL_OPTION);
        
        if (result == JOptionPane.OK_OPTION) {
            selectedAddress = (String) addressComboBox.getSelectedItem();
            return true;
        }
        return false;
    }

    private void loadVendorData(List<VendorDTO> vendors) {
        tableModel.setRowCount(0);
        for (VendorDTO vendor : vendors) {
            tableModel.addRow(new Object[]{
                    vendor.getId(),
                    vendor.getName(),
                    "View"
            });
        }
    }

    private void filterVendors() {
        String selectedType = (String) vendorTypeFilter.getSelectedItem();
        List<VendorDTO> filteredVendors = vendorList;
        loadVendorData(filteredVendors);
    }

    private void sortVendors() {
        filterVendors();
    }
}
