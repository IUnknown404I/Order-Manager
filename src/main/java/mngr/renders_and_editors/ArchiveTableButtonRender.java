/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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
