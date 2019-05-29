package fr.umlv.littlethinker;
import java.awt.GridLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

/**
 * Classe contenants les 5 registres du processeur dans un JPanel
 *
 */
@SuppressWarnings("serial")
public class Registers extends JPanel{
	private final Register[] registerTab;
	public static final int REGISTER_COUNT = 5;
	
	/**
	 * Initialise les 5 registres
	 */
	public Registers() {
		super(new GridLayout(5, 2));
		registerTab = new Register[REGISTER_COUNT];
		for (int i = 0; i < REGISTER_COUNT; i++) {
			registerTab[i] = new Register('G');
			add(new JLabel("Registre #" + i, SwingConstants.RIGHT));
			add(registerTab[i]);
		}
	}
	
	/**
	 * Modifier la valeur d'un registre
	 * @param i L'indice du registre à modifier
	 * @param val La nouvelle valeur à mettre dans le registre
	 * @param tempo La vitesse de clignotement du registre
	 */
	public void setR(int i, int val, int tempo) {
		registerTab[i].setValue(Integer.toString(val), tempo);
	}
	
	/**
	 * Récupérer la valeur d'un registre
	 * @param i L'indice du registre dont on veut récupérer la valeur
	 * @return La valeur du registre sous forme d'entier
	 */
	public int getR(int i) {
		return Integer.parseInt(registerTab[i].getText());
	}
	
	/**
	 * Récupérer la valeur d'un registre
	 * @param i L'indice du registre dont on veut récupérer la valeur
	 * @return La valeur du registre sous forme de String
	 */
	public String getRAsString(int i) {
		if (registerTab[i].getText().equals("")) {
			return "Vide";
		}
		return registerTab[i].getText();
	}
	
	/**
	 * Réinitialise les registres du panel
	 */
	public void reset() {
		for (int i = 0; i < REGISTER_COUNT; i++) {
			registerTab[i].reset();
		}
	}
}