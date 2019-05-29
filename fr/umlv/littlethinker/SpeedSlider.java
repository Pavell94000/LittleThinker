package fr.umlv.littlethinker;

import java.util.Dictionary;
import java.util.Hashtable;

import javax.swing.JLabel;
import javax.swing.JSlider;

/**
 * Classe permettant de choisir la vitesse de clignotement (tempo) pour les registres
 *
 */
@SuppressWarnings("serial")
public class SpeedSlider extends JSlider{
	private static final int DELAY_MIN = 1;
	private static final int DELAY_MAX = 40;
	private static final int DELAY_INIT = 15;
	
	/**
	 * Initialise le slider
	 */
	public SpeedSlider() {
		super(JSlider.HORIZONTAL, DELAY_MIN, DELAY_MAX, DELAY_INIT);
		
		setMajorTickSpacing(10);
		setMinorTickSpacing(2);
		setPaintTicks(true);
		setPaintLabels(true);
		
		Dictionary<Integer, JLabel> labelTable = new Hashtable<>();
		labelTable.put(Integer.valueOf(DELAY_MIN), new JLabel("Fast"));
		labelTable.put(Integer.valueOf(DELAY_INIT), new JLabel("Init"));
		labelTable.put(Integer.valueOf(DELAY_MAX), new JLabel("Slow"));
		
		setLabelTable(labelTable);
	}
}
