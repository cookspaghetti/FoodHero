package ui.utils;

import java.awt.Component;
import java.awt.FlowLayout;

import javax.swing.AbstractCellEditor;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;

public class ButtonEditor extends AbstractCellEditor implements TableCellEditor {
	private final JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 6));
    private final JButton editButton = new JButton("Edit");
    private final JButton deleteButton = new JButton("Delete");
    private String adminId;

    public ButtonEditor(JTable table) {
        editButton.addActionListener(e -> {
            System.out.println("Edit clicked for Admin ID: " + adminId);
            // Call edit logic here (e.g., open an edit dialog)
        });

        deleteButton.addActionListener(e -> {
            System.out.println("Delete clicked for Admin ID: " + adminId);
            // Call delete logic here (e.g., remove from the database & table)
        });

        panel.add(editButton);
        panel.add(deleteButton);
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        adminId = table.getValueAt(row, 0).toString(); // Get ID from the first column
        return panel;
    }

    @Override
    public Object getCellEditorValue() {
        return null;
    }
}
