package ui.transaction;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import com.toedter.calendar.JDateChooser;

import enumeration.PaymentMethod;
import enumeration.TransactionType;

import java.awt.*;
import java.util.Date;

public class CustomerTransactionPage extends JFrame {
    private JTable transactionTable;
    private JDateChooser datePicker;

    public CustomerTransactionPage() {
        setTitle("Customer Transactions");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        datePicker = new JDateChooser();
        datePicker.setPreferredSize(new Dimension(150, 25));
        JButton filterButton = new JButton("Find"); 
        
        JComboBox<TransactionType> transactionTypeComboBox = new JComboBox<>(TransactionType.values());
        
        topPanel.add(new JLabel("Select Date: "));
        topPanel.add(datePicker);
        topPanel.add(filterButton);
        topPanel.add(new JLabel("Filter by Transaction Type:"));
        topPanel.add(transactionTypeComboBox);

        String[] columnNames = {"Transaction ID", "Transaction Type", "Date", "Amount", "Description", "Payment Method"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);
        transactionTable = new JTable(model);
        transactionTable.setRowHeight(40);
        
        transactionTable.getColumn("Transaction ID").setPreferredWidth(60);
        transactionTable.getColumn("Date").setPreferredWidth(140);

        JScrollPane scrollPane = new JScrollPane(transactionTable);

        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(topPanel, BorderLayout.NORTH);
        getContentPane().add(scrollPane, BorderLayout.CENTER);

        loadSampleData(model);
        
        setVisible(true);
    }

    private void loadSampleData(DefaultTableModel model) {
        model.addRow(new Object[]{"TXN001", TransactionType.TOPUP, new Date(), "$50", "Wallet recharge", PaymentMethod.CASH});
        model.addRow(new Object[]{"TXN002", TransactionType.PAYMENT, new Date(), "$20", "Food order", PaymentMethod.WALLET});
    }

    public static void main(String[] args) {
        new CustomerTransactionPage();
    }
}
