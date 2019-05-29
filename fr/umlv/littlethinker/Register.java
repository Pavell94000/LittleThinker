package fr.umlv.littlethinker;
import java.awt.Color;

import javax.swing.JTextField;

/**
 * Classe héritant de JTextField et définissant un registre
 * 
 */
@SuppressWarnings("serial")
public class Register extends JTextField {
	private final char color;
	
	/**
	 * Constructeur initialisant un registre
	 * @param color la couleur de clignotement du registre lorsqu'on utilise la methode setValue
	 */
	public Register(char color) {
		super();
		this.color = color;
	}
	
	/**
	 * Réinitialise le registre
	 */
	public void reset() {
		setText("");
	}
	
	/**
	 * Modifier la valeur d'un registre
	 * @param s La nouvelle valeur du registre sous forme de String
	 * @param tempo La vitesse de clignotement du registre
	 */
	public void setValue(String s, int tempo) {
		setText(s);
		for(int i = 256/(tempo+1); i < 256; i += 256/(tempo + 1)) {
			try {
				Thread.sleep(16);
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
				setBackground(Color.WHITE);
				return;
			}
			switch(color) { 
			case 'R' :
				setBackground(new Color(255,i,i));
				break;
			case 'G' :
				setBackground(new Color(i,255,i));
				break;
			case 'B' :
				setBackground(new Color(i,i,255));
				break;
			default :
				setBackground(new Color(i,i,i));
				break;
			}
			revalidate(); // force la fenetre à afficher la couleur
		}
		setBackground(Color.WHITE);
		revalidate();
	}
	
	@Override
	public boolean isEditable() {
		return false;
	}
}