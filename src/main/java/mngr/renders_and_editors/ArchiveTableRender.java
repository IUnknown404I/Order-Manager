package mngr.renders_and_editors;

import java.awt.Color;
import java.awt.Component;
import javax.swing.JTable;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import static javax.swing.SwingConstants.CENTER;

/**
 *
 * @author MrUnknown404
 */
public class ArchiveTableRender extends DefaultTableCellRenderer {
    private final Border IS_FOCUSED = new BevelBorder(1);
    private final Border OUT_OF_FOCUS = new EmptyBorder(1, 1, 1, 1);

    @Override
    @SuppressWarnings("empty-statement")
    public Component getTableCellRendererComponent(JTable table,
            Object value,
            boolean isSelected,
            boolean hasFocus,
            int row,
            int column) {
        
        setHorizontalAlignment(CENTER);
        
        // setting up colors according data difference
        if (isSelected) {
            setBackground(new Color(255,212,128));
        } else {
            setBackground(Color.WHITE);
        }
        
        // setting up a border when the cell is selected
        if (hasFocus && isSelected) {
            setBorder(IS_FOCUSED);
        } else
            setBorder(OUT_OF_FOCUS);
        
        // setting the out and in of description column cells
        if(column==2) {
            setText((value == null) ? "" : getFormattedText(value.toString()));
        } else
            setText((value == null) ? "" : value.toString());
        
        return this;
    }
    
    private String getFormattedText(String text){
        String formattedText = text;
        return formattedText.replaceAll("\n", " \n");
    }
    
}
