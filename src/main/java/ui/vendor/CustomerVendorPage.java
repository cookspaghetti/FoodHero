package ui.vendor;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import dto.VendorDTO;
import enumeration.VendorType;

import java.awt.*;
import java.util.List;

public class CustomerVendorPage extends JFrame {
    private JTable vendorTable;
    private DefaultTableModel tableModel;
    private JComboBox<VendorType> vendorTypeFilter;
    private JButton sortButton;
    private List<VendorDTO> vendorList;

    public CustomerVendorPage() {
        this.vendorList = vendors;
        setTitle("Vendors");
        setSize(600, 400);
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
        tableModel = new DefaultTableModel(columnNames, 0);
        vendorTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(vendorTable);
        getContentPane().add(scrollPane, BorderLayout.CENTER);

        loadVendorData(vendorList);
    }

    private void loadVendorData(List<VendorDTO> vendors) {
    	// Implement logic to compare postcode
    	
        tableModel.setRowCount(0);
        for (VendorDTO vendor : vendors) {
            tableModel.addRow(new Object[]{
                vendor.getId(),
                vendor.getName(),
//                vendor.getType(),
//                getDeliveryFee(),
                "View"
            });
        }
    }

    private void filterVendors() {
        String selectedType = (String) vendorTypeFilter.getSelectedItem();
        List<VendorDTO> filteredVendors = vendorList;
        if (!"All".equals(selectedType)) {
//            filteredVendors = vendorList.stream().filter(v -> v.getType().equals(selectedType)).collect(Collectors.toList());
        }
        loadVendorData(filteredVendors);
    }

    private void sortVendors() {
//        vendorList.sort(Comparator.comparingDouble(VendorDTO::getDeliveryFee));
        filterVendors();
    }
}

