package fr.umlv.littlethinker;

import java.awt.GridLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

/**
 * Classe héritant d'un JPanel et contenant les controles d'un processeur:
 * - un compteur ordinal
 * - un décodeur
 * - un accumulateur
 * - un registre d'état
 */
@SuppressWarnings("serial")
public class Controls extends JPanel{
	private final Register instructionPointer;
	private final Register decoder;
	private final Register accumulator;
	private final Register stackPointer;
	private final StatusRegister statusRegister;
	
	/**
	 * Initialise le JPanel des controles
	 */
	public Controls() {
		super(new GridLayout(5, 2));
		accumulator = new Register('R');
		decoder = new Register('B');
		instructionPointer = new Register('B');
		stackPointer = new Register('B');
		statusRegister = new StatusRegister();
		setI(0, 0);
		setP(LittleThinker.MEMORY_LENGTH - 1, 0);
		add(new JLabel("Program Counter", SwingConstants.RIGHT));
		add(instructionPointer);
		add(new JLabel("Stack Pointer", SwingConstants.RIGHT));
		add(stackPointer);
		add(new JLabel("Décodeur", SwingConstants.RIGHT));
		add(decoder);
		add(new JLabel("Accumulateur", SwingConstants.RIGHT));
		add(accumulator);
		add(new JLabel("Flag", SwingConstants.RIGHT));
		add(statusRegister);
	}
	
	/**
	 * Récupérer l'instruction stockée dans le décodeur
	 * @return L'instruction stockée dans le décodeur
	 */
	public String getD() {
		return decoder.getText();
	}
	
	/**
	 * Mettre une instruction dans le décodeur
	 * @param instruction Instruction à afficher dans le décodeur
	 * @param tempo La vitesse de clignotement du décodeur
	 */
	public void setD(String instruction, int tempo) {
		decoder.setValue(instruction, tempo);
	}
	
	/**
	 * Récupérer la valeur du compteur ordinal
	 * @return La valeur du compteur ordinal sous forme d'entier
	 */
	public int getI() {
		return Integer.parseInt(instructionPointer.getText());
	}
	
	/**
	 * Récupérer la valeur du compteur ordinal
	 * @return La valeur du compteur ordinal sous forme de String
	 */
	public String getIAsString() {
		return instructionPointer.getText();
	}
	
	/**
	 * Modifier la valeur du compteur ordinal
	 * @param val La valeur à mettre dans le compteur ordinal
	 * @param tempo La vitesse de clignotement du compteur ordinal
	 */
	public void setI(int val, int tempo) {
		instructionPointer.setValue(Integer.toString(val), tempo);
	}
	
	/**
	 * Récupérer la valeur de l'accumulateur
	 * @return La valeur de l'accumulateur sous forme d'entier
	 */
	public int getA() {
		return Integer.parseInt(accumulator.getText());
	}
	
	/**
	 * Récupérer la valeur de l'accumulateur
	 * @return La valeur de l'accumulateur sous forme de String
	 */
	public String getAAsString() {
		return accumulator.getText();
	}
	
	/**
	 * Modifier la valeur de l'accumulateur
	 * @param val La valeur à mettre dans l'accumulateur
	 * @param tempo La vitesse de clignotement de l'accumulateur
	 */
	public void setA(int val, int tempo) {
		accumulator.setValue(Integer.toString(val), tempo);
	}
	
	/**
	 * Récupérer le statut du registre d'état
	 * @return Le statut du registre d'état sous forme d'entier
	 */
	public int getStatus() {
		return statusRegister.getStatus();
	}
	
	/**
	 * Récupérer le statut du registre d'état
	 * @return Le statut du registre d'état sous forme de String
	 */
	public String getStatusAsString() {
		return statusRegister.getText();
	}
	
	/**
	 * Modifier le statut du registre d'état
	 * @param status Le nouveau statut du registre d'état
	 * @param tempo La vitesse de clignotement du registre d'état
	 */
	public void setStatus(int status, int tempo){
		statusRegister.setStatus(status, tempo);
	}
	
	/**
	 * Modifier la valeur du pointeur de pile
	 * @param val La valeur à mettre dans le pointeur
	 * @param tempo La vitesse de clignotement du pointeur
	 */
	public void setP(int val, int tempo) {
		stackPointer.setValue(Integer.toString(val), tempo);
	}
	
	/**
	 * Récupérer la valeur du pointeur de pile
	 * @return La valeur du pointeur de pile sous forme d'entier
	 */
	public int getP() {
		return Integer.parseInt(stackPointer.getText());
	}
	
	/**
	 * Réinitialise tout les controles
	 */
	public void reset() {
		accumulator.reset();
		decoder.reset();
		instructionPointer.setText("0");
		stackPointer.setText(Integer.toString(LittleThinker.MEMORY_LENGTH - 1));
		statusRegister.reset();
	}
}
