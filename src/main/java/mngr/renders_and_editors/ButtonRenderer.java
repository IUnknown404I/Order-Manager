package mngr.renders_and_editors;

import mngr.TableMethods;

import java.awt.Color;
import java.awt.Component;
import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.table.TableCellRenderer;

/**
 * The Render class for Active orders' buttons
 * @author MrUnknown404
 */
public class ButtonRenderer extends JButton implements TableCellRenderer {
    private static final Border IS_FOCUSED = new BevelBorder(1);
    private static final Border OUT_OF_FOCUS = new EmptyBorder(1, 1, 1, 1);
    
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value,
                        boolean isSelected, boolean hasFocus, int row, int column){
        
        if (isSelected) {
            setDefaultBackColor(isSelected);
        } else if (table.getName().equals("Archieve")) {
            setBackground(Color.ORANGE);
        } else {
            int daysDif = TableMethods.getDaysDiff((String) table.getValueAt(row,0));
                if (daysDif < 0 && daysDif > -6) {
                    setBackground(Color.GRAY); //серый   128, 128, 128
                } else if (daysDif <= -6) {
                    setBackground(Color.orange); //банан   255, 255, 153 new Color(250, 231, 182)
                } else if (daysDif <= 3) {
                    setBackground(Color.red); //красный new Color(255, 51, 51)
                } else if (daysDif <= 7) {
                    setBackground(new Color(255, 255, 102)); //жёлтый
                } else {
                    setDefaultBackColor(isSelected);
                }
        }
        
        if (hasFocus && isSelected) {
            setBorder(IS_FOCUSED);
        } else
            setBorder(OUT_OF_FOCUS);
        
        setText((value == null) ? "" : value.toString());
        
        return this;
    }
    
    /**
     * Back color setter
     * @param isSelected boolean is thr row selected 
     */
    public void setDefaultBackColor(boolean isSelected){
        if(isSelected)
            setBackground(Color.cyan);
        else
            setBackground(Color.WHITE);
    }
}
