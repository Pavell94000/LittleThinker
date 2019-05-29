package fr.umlv.littlethinker;
import java.awt.Color;

import javax.swing.CellEditor;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;

/**
 * Classe héritant de JTable, une mémoire, avec une colonne d'adresse (0)
 * et une colonne de valeur (1)
 */
@SuppressWarnings("serial")
public class Memory extends JTable {
	private boolean editable;
	private final MemoryRenderer renderer;
	
	/**
	 * Initialise la mémoire
	 * @param rowCount le nombre d'adresses dans la mémoire
	 * 
	 */
	public Memory(int rowCount){
		super(rowCount, 2);
		editable = true;
		
		// on place les titres de colonnes
		getColumnModel().getColumn(0).setHeaderValue("Adresse");
		getColumnModel().getColumn(1).setHeaderValue("Contenu");
		// on réduit la taille de la colonne des adresses
		getColumnModel().getColumn(0).setPreferredWidth(24);
		// on empeche l'utilisateur de changer l'ordre des colonnes
		getTableHeader().setReorderingAllowed(false);
		
		//On change le renderer pour faire clignoter la mémoire
		renderer = new MemoryRenderer();
		this.getColumnModel().getColumn(1).setCellRenderer(renderer);
		
		for (int i = 0; i < rowCount; i++) {
			setValueAt(Integer.valueOf(i), i, 0);
			setValueAt("", i, 1);
		}
	}
	
	/**
	 * Fait clignoter la case mémoire voulue
	 * @param row L'indice de la case à faire clignoter
	 * @param tempo Vitesse de clignotement
	 * @param color couleur de clignotement de la case ('R', 'G', ou 'B')
	 */
	private void memoryBlink(int row, int tempo, char color) {
		AbstractTableModel model = (AbstractTableModel) this.getModel();
		renderer.setRow(row);
		renderer.setColor(color);
		for(int i = 256/(tempo+1); i < 256; i += 256/(tempo + 1)) {
			try {
				Thread.sleep(16);
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
				setBackground(Color.WHITE);
				return;
			}
			renderer.setI(i);
			model.fireTableCellUpdated(row, 1);
			revalidate();
		}
		renderer.setRow(-1);
		model.fireTableCellUpdated(row, 1);
		revalidate();
		
	}
	
	/**
	 * Placer un entier dans la mémoire
	 * @param row L'adresse dans laquelle value est placée
	 * @param value La valeur placée à l'adresse row
	 * @param tempo vitesse de clignotement de la cellule
	 */
	public void setValue(int row, int value, int tempo) {
		setValueAt(value, row, 1);
		memoryBlink(row, tempo, 'R');
	}
	
	/**
	 * Placer une String dans la mémoire
	 * @param row L'adresse dans laquelle value est placée
	 * @param value La valeur placée à l'adresse row
	 */
	public void setValue(int 	row, String value) {
		setValueAt(value, row, 1);
	}
	
	/**
	 * Récupérer une valeur dans la mémoire
	 * @param row L'adresse de la valeur à récupérer
	 * @param tempo vitesse de clignotement de la cellule lue
	 * @return La valeur récupérée sous forme d'entier
	 */
	public int getValue(int row, int tempo) {
		memoryBlink(row, tempo, 'G');
		return Integer.parseInt(getValueAt(row, 1).toString());
	}
	
	/**
	 * Récupérer une valeur dans la mémoire sous forme de String
	 * @param row L'adresse de la valeur à récupérer
	 * @return La valeur récupérée sous forme de String
	 */
	public String getValueAsString(int row) {
		Object o = getValueAt(row, 1);
		if (o != null) {
			if (o instanceof String) {
				return (String)o;
			}
			if (o instanceof Integer) {
				return ((Integer)o).toString();
			}
		}
		return "";
	}
	
	/**
	 * Modifie la capacité à modifier la colonne des valeurs de la mémoire
	 * @param b modifier la mémoire
	 */
	public void setEditable(boolean b) {
		if (!b) {
			CellEditor c = getCellEditor();
			if (c != null) c.stopCellEditing();
		}
		editable = b;
	}
	
	@Override
	public boolean isCellEditable(int row, int column) {
		return column == 1 && editable;
	}
}