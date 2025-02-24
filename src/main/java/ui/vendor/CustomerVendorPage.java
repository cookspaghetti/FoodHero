package ui.vendor;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import dto.AddressDTO;
import dto.CustomerDTO;
import dto.VendorDTO;
import enumeration.ButtonMode;
import enumeration.VendorType;
import service.address.AddressService;
import service.distance.DistanceService;
import service.general.SessionControlService;
import service.vendor.VendorService;
import ui.dashboard.CustomerDashboard;
import ui.utils.ButtonEditor;
import ui.utils.ButtonRenderer;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class CustomerVendorPage extends JFrame {
    private JTable vendorTable;
    private DefaultTableModel tableModel;
    private JComboBox<VendorType> vendorTypeFilter;
    private JTextField searchField;
    private JButton sortButton, searchButton;
    private List<VendorDTO> vendorList = new ArrayList<>();
    private String selectedAddress;

    public CustomerVendorPage() {

        if (!promptForAddress(SessionControlService.getDeliveryAddresses())) {
            JOptionPane.showMessageDialog(null, "You must select an address to proceed.", "Address Required",
                    JOptionPane.WARNING_MESSAGE);
            this.dispose();
            new CustomerDashboard((CustomerDTO) SessionControlService.getUser()).setVisible(true);
            return;
        }

        // Get a list of vendors that has the same state as customer
        getVendorsByState(selectedAddress);

        setTitle("Vendors");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setLayout(new BorderLayout());

        // Filter Panel
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        filterPanel.add(new JLabel("Search:"));
        searchField = new JTextField(20);
        filterPanel.add(searchField);
        searchButton = new JButton("Search");
        searchButton.addActionListener(e -> searchVendors());
        filterPanel.add(searchButton);
        filterPanel.add(new JLabel("Filter by Vendor Type:"));
        vendorTypeFilter = new JComboBox<>(VendorType.values());
        vendorTypeFilter.addActionListener(e -> filterVendors());
        filterPanel.add(vendorTypeFilter);

        sortButton = new JButton("Sort by Delivery Fee");
        sortButton.addActionListener(e -> sortVendors());
        filterPanel.add(sortButton);

        getContentPane().add(filterPanel, BorderLayout.NORTH);

        // Vendor Table
        String[] columnNames = { "Vendor ID", "Vendor Name", "Vendor Type", "Delivery Fee", "Actions" };
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

        vendorTable.getColumn("Vendor ID").setPreferredWidth(40);
        vendorTable.getColumn("Vendor Name").setPreferredWidth(300);
        vendorTable.getColumn("Delivery Fee").setPreferredWidth(20);

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
        int result = JOptionPane.showConfirmDialog(null, addressComboBox, "Select Delivery Address",
                JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION) {
            selectedAddress = (String) addressComboBox.getSelectedItem();
            SessionControlService.setCurrentSelectedAddress(selectedAddress);
            return true;
        }
        return false;
    }

    private void loadVendorData(List<VendorDTO> vendors) {
        tableModel.setRowCount(0);
        if (vendors.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No vendors found in your area.", "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }
        for (VendorDTO vendor : vendors) {
            tableModel.addRow(new Object[] {
                    vendor.getId(),
                    vendor.getVendorName(),
                    vendor.getVendorType(),
                    getDeliveryFee(vendor.getAddressId()),
                    "View"
            });
        }
    }

    private void searchVendors() {
        String searchQuery = searchField.getText();
        List<VendorDTO> filteredVendors = new ArrayList<>();
        for (VendorDTO vendor : vendorList) {
            if (vendor.getVendorName().toLowerCase().contains(searchQuery.toLowerCase())) {
                filteredVendors.add(vendor);
            }
        }
        loadVendorData(filteredVendors);
    }

    private void filterVendors() {
        VendorType selectedType = (VendorType) vendorTypeFilter.getSelectedItem();
        List<VendorDTO> filteredVendors = new ArrayList<>();
        for (VendorDTO vendor : vendorList) {
            if (vendor.getVendorType().equals(selectedType)) {
                filteredVendors.add(vendor);
            }
        }
        loadVendorData(filteredVendors);
    }

    private void getVendorsByState(String id) {
        AddressDTO address = AddressService.getAddressById(id);

        if (address == null) {
            JOptionPane.showMessageDialog(null, "Failed to retrieve address information.", "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        String userState = address.getState();
        String userPostalCode = address.getPostalCode();

        if (userPostalCode == null || userPostalCode.length() < 2) {
            JOptionPane.showMessageDialog(null, "Invalid postal code.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        List<VendorDTO> vendors = VendorService.readAllVendor();

        List<VendorDTO> filteredVendors = vendors.stream()
                .map(vendor -> AddressService.getAddressById(vendor.getAddressId()))
                .filter(vendorAddress -> vendorAddress != null && vendorAddress.getState().equals(userState))                                                                                                            
                .filter(vendorAddress -> isPostalCodeNearby(userPostalCode, vendorAddress.getPostalCode()))                                                                                  
                .map(vendorAddress -> vendors.stream()
                        .filter(v -> v.getAddressId().equals(vendorAddress.getId()))
                        .findFirst().orElse(null))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
        System.out.println(filteredVendors);
        vendorList.addAll(filteredVendors);
    }

    private boolean isPostalCodeNearby(String userPostal, String vendorPostal) {
        if (vendorPostal == null || vendorPostal.length() < 2) {
            return false; // Invalid postal code
        }
    
        // Extract first 2 digits (state-based check)
        String userPrefix = userPostal.substring(0, 2);
        String vendorPrefix = vendorPostal.substring(0, 2);
    
        if (!userPrefix.equals(vendorPrefix)) {
            return false; // Different states
        }
    
        try {
            int userPostalInt = Integer.parseInt(userPostal);
            int vendorPostalInt = Integer.parseInt(vendorPostal);
    
            // Check if vendor's postal code is within ±500 range
            return Math.abs(userPostalInt - vendorPostalInt) <= 500;
        } catch (NumberFormatException e) {
            return false; // Handle invalid numeric postal codes
        }
    }

    private double getDeliveryFee(String addressId) {
        AddressDTO address = AddressService.getAddressById(addressId);
        AddressDTO customerAddress = AddressService.getAddressById(selectedAddress);
        if (address == null) {
            return 0;
        }
        return DistanceService.getDeliveryFee(address, customerAddress);
    }

    private void sortVendors() {
        int rowCount = tableModel.getRowCount();
        boolean swapped;

        // Bubble sort implementation
        for (int i = 0; i < rowCount - 1; i++) {
            swapped = false;
            for (int j = 0; j < rowCount - i - 1; j++) {
                // Get delivery fees from the table (column index 3)
                double fee1 = Double.parseDouble(tableModel.getValueAt(j, 3).toString());
                double fee2 = Double.parseDouble(tableModel.getValueAt(j + 1, 3).toString());

                // Compare and swap if necessary
                if (fee1 > fee2) {
                    // Swap all columns
                    for (int col = 0; col < tableModel.getColumnCount(); col++) {
                        Object temp = tableModel.getValueAt(j, col);
                        tableModel.setValueAt(tableModel.getValueAt(j + 1, col), j, col);
                        tableModel.setValueAt(temp, j + 1, col);
                    }
                    swapped = true;
                }
            }
            // If no swapping occurred, array is sorted
            if (!swapped)
                break;
        }

        // Refresh the table
        vendorTable.repaint();
    }
}
