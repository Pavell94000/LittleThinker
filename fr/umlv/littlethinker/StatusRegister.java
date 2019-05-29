package fr.umlv.littlethinker;

/**
 * Classe héritant de Register, permet de définir un registre d'état
 *
 */
@SuppressWarnings("serial")
public class StatusRegister extends Register {
	private int status;
	static final int NONE_STATUS = 0;
	static final int Z_STATUS = 1;
	static final int N_STATUS = 2;
	static final int D_STATUS = 3;
	
	/**
	 * Initialisation du registre d'état
	 */
	public StatusRegister() {
		super('R');
	}
	
	/**
	 * Récuperer le statut de ce registre d'état
	 * @return Le statut de ce registre d'état sous forme d'entier
	 */
	public int getStatus() {
		return status;
	}
	
	/**
	 * Modifie la statut de ce registre d'état
	 * @param status Le nouveau statut du registre
	 * @param tempo La vitesse de clignotement du registre
	 */
	public void setStatus(int status, int tempo) {
		this.status = status;
		switch (status) {
		case NONE_STATUS: setText(""); break;
		case Z_STATUS: setValue("Z", tempo); break;
		case N_STATUS: setValue("N", tempo); break;
		case D_STATUS: setValue("D", tempo); break;
		default: throw new IllegalArgumentException("Incorrect RFlag State");
		}
	}
}