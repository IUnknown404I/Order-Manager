package mngr.renders_and_editors;

import java.awt.Color;

/**
 *
 * @author MrUnknown404
 */
public class ArchiveTableButtonRender extends ButtonRenderer {
    @Override
    public void setDefaultBackColor(boolean isSelected){
        if(isSelected)
            setBackground(new Color(179,119,0));
        else
            setBackground(Color.orange);
    }
}
