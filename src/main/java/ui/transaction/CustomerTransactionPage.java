package ui.transaction;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import com.toedter.calendar.JDateChooser;

import dto.AddTransactionDTO;
import dto.CustomerDTO;
import dto.DeductTransactionDTO;
import dto.TransactionDTO;
import enumeration.PaymentMethod;
import service.general.SessionControlService;
import service.transaction.TransactionService;

import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class CustomerTransactionPage extends JFrame {
    private JTable transactionTable;
    private JDateChooser datePicker;
    private JButton filterButton;
    private DefaultTableModel tableModel;

    private CustomerDTO customer = (CustomerDTO) SessionControlService.getUser();

    public CustomerTransactionPage() {
        setTitle("Customer Transactions");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        datePicker = new JDateChooser();
        datePicker.setPreferredSize(new Dimension(150, 25));
        filterButton = new JButton("Find");
        
        topPanel.add(new JLabel("Select Date: "));
        topPanel.add(datePicker);
        topPanel.add(filterButton);

        filterButton.addActionListener(e -> filterTransactionByDate());

        String[] columnNames = {"Transaction ID", "Date", "Amount", "Description", "Payment Method"};
        tableModel = new DefaultTableModel(columnNames, 0);
        transactionTable = new JTable(tableModel);
        transactionTable.setRowHeight(40);
        
        transactionTable.getColumn("Transaction ID").setPreferredWidth(40);
        transactionTable.getColumn("Date").setPreferredWidth(140);
        transactionTable.getColumn("Description").setPreferredWidth(300);

        JScrollPane scrollPane = new JScrollPane(transactionTable);

        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(topPanel, BorderLayout.NORTH);
        getContentPane().add(scrollPane, BorderLayout.CENTER);

        loadTransactions();
        
        setVisible(true);
    }

    private void filterTransactionByDate() {

        List<TransactionDTO> transactions = new ArrayList<>();

        tableModel.setRowCount(0);

        LocalDate selectedDate = datePicker.getDate().toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDate();

        List<AddTransactionDTO> addTransactions = TransactionService.readAllAddTransaction(customer.getId());
        List<DeductTransactionDTO> deductTransactions = TransactionService.readAllDeductTransaction(customer.getId());

        for (AddTransactionDTO transaction : addTransactions) {
            if (transaction.getDate().toLocalDate().equals(selectedDate)) {
                transactions.add(transaction);
            }
        }

        for (DeductTransactionDTO transaction : deductTransactions) {
            if (transaction.getDate().toLocalDate().equals(selectedDate)) {
                transactions.add(transaction);
            }
        }

        transactions.sort((t1, t2) -> t2.getDate().compareTo(t1.getDate()));

        for (TransactionDTO transaction : transactions) {
            tableModel.addRow(new Object[] {
                    transaction.getId(),
                    transaction.getDate(),
                    transaction.getAmount(),
                    transaction.getDescription(),
                    getPaymentMethod(transaction.getId())
            });
        }
    }

    // Get payment method of a transaction
    private PaymentMethod getPaymentMethod(String transactionId) {
        // Determine the type of transaction
        if (transactionId.startsWith("A")) {
            AddTransactionDTO addTransaction = TransactionService.readAddTransaction(transactionId);
            return addTransaction.getPaymentMethod();
        } else {
            return PaymentMethod.WALLET;
        }
    }

    // Load all transactions of the customer
    private void loadTransactions() {
        List<AddTransactionDTO> addTransactions = TransactionService.readAllAddTransaction(customer.getId());
        List<DeductTransactionDTO> deductTransactions = TransactionService.readAllDeductTransaction(customer.getId());

        List<TransactionDTO> transactions = new ArrayList<>();
        transactions.addAll(addTransactions);
        transactions.addAll(deductTransactions);

        transactions.sort((t1, t2) -> t2.getDate().compareTo(t1.getDate()));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        for (TransactionDTO transaction : transactions) {
            tableModel.addRow(new Object[] {
                    transaction.getId(),
                    transaction.getDate().format(formatter),
                    transaction.getAmount(),
                    transaction.getDescription(),
                    getPaymentMethod(transaction.getId())
            });
        }
    }
}
