package fr.umlv.littlethinker;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

/**
 * Classe pour le renderer de la mémoire, permet de faire clignoter des adresse
 */

@SuppressWarnings("serial")
public class MemoryRenderer extends DefaultTableCellRenderer{
	private int row = -1;
	private int i;
	private char color;
	
	/**
	 * Change la valeur de l'adresse à faire clignoter
	 * @param row Nouvel indice de la colonne a faire clignoter
	 */
	public void setRow(int row) {
		this.row = row;
	}
	
	
	/**
	 * Change l'intensité de la coloration de la cellule row
	 * @param i Nouvelle intensité
	 */
	public void setI(int i) {
		this.i = i;
	}
	
	/**
	 * Change la couleur de la prochaine case mémoire qui clignote
	 * @param color Nouvelle couleur choisie ('R', 'G' ou 'B');
	 */
	public void setColor(char color) {
		this.color = color;
	}
	
	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int col) {
		JLabel l = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, col);
		if (row == this.row) {	
			if (color == 'R')
				l.setBackground(new Color(255, i, i));
			else if (color == 'G')
				l.setBackground(new Color(i, 255, i));
			else if (color == 'B')
				l.setBackground(new Color(i, i, 255));
		}
		else {
			l.setBackground(Color.WHITE);
		}
	    
		return l;
	}
}
