package ui.utils;

import java.awt.Component;

import javax.swing.BorderFactory;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.table.TableCellRenderer;

public class MultiLineRenderer extends JTextArea implements TableCellRenderer {
	public MultiLineRenderer() {
        setLineWrap(true);
        setWrapStyleWord(true);
        setOpaque(true); // Essential for background color to work
        setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0)); // Add some padding
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        setText((value == null) ? "" : value.toString());
        setBackground(isSelected ? table.getSelectionBackground() : table.getBackground());
        setForeground(isSelected ? table.getSelectionForeground() : table.getForeground());

        return this;
    }
}
