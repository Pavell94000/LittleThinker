package fr.umlv.littlethinker;
import java.io.File;
import java.io.IOException;
import java.util.StringTokenizer;
import java.util.function.BooleanSupplier;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 * Classe héritant de JPanel et contenant le processeur.
 * Il sait lire une mémoire et modifier ses registres selon les instructions.
 * Il affiche aussi des message dans un terminal.
 */
@SuppressWarnings("serial")
public class Processor extends JPanel{
	private final Registers registers;
	private final Controls controls;
	private final Memory mem;
	private transient Thread t;
	private final JButton startButton;
	private final JButton stopButton;
	private final JButton nextButton;
	private final JButton saveMemButton;
	private final JButton loadMemButton;
	private final SpeedSlider slider;
	private final Terminal term;
	private final JCheckBox checkBox;
	
	/**
	 * Initialise un processeur
	 * @param mem Memoire qui sera lue par le processeur
	 * @param term Terminal dans lequel le processeur écrira des messages
	 */
	public Processor(Memory mem, Terminal term){
		super();
		
		this.mem = mem;
		this.term = term;
		registers = new Registers();
		controls = new Controls();
		
		JPanel textFields = new JPanel();
		textFields.setLayout(new BoxLayout(textFields, BoxLayout.X_AXIS));
		textFields.add(new JPanel());
		textFields.add(registers);
		textFields.add(controls);
		textFields.add(new JPanel());
		
		JPanel buttons = new JPanel();
		buttons.setLayout(new BoxLayout(buttons, BoxLayout.X_AXIS));
        // Affichage du bouton Start 
		startButton = new JButton("Start !");
        startButton.addActionListener(e -> this.start());
        buttons.add(startButton);
        // Affichage du bouton Stop 
        stopButton = new JButton("Stop");
        stopButton.addActionListener(e -> this.stop());
		stopButton.setEnabled(false);
		buttons.add(stopButton);
        // Affichage du bouton Next 
        nextButton = new JButton("Next");
        nextButton.addActionListener(e -> this.next());
        buttons.add(nextButton);
        // Affichage du bouton Info 
        JButton infoButton = new JButton("Infos");
        infoButton.addActionListener(e -> 
		JOptionPane.showMessageDialog(this,
				"LittleThinker Version 1.0\nSous licence "+
				"GNU General Public Licence.\n\nPetit "+
				"simulateur de microprocesseur très basic, "+
				"qui permet de\nvisualiser le fonctionnement"+
				" théorique d'un micro-processeur.\n\n\n"+
				"Auteurs : Severin Gosset et Christophe Callé, Novembre 2017.\n"+
				"Projet réalisé sous le tutorat de Sylvain Cherrier.\n\n\n"+
				"Convention d'écriture : On préfixe par\n"+
				"# pour un registre,\na pour l'accumulateur,\n"+
				"$ pour une case mémoire,\nrien pour une"+
				" valeur.\n\nNOTA : On peut utiliser l'accès"+
				" mémoire indirect avec $# (aller à\n"+
				"l'adresse indiquée dans le registre).\n\n"+
				"Commandes implémentées :"+
				"\nld-st-mv\nadd-sub-mul-div-mod-dec-inc-cmp\n"+
				"bne-beq-bde-bnd-bmi-bpl-brn\nps-pp\n\n"+
				"st peut stocker le ProgramCounter (pc) et le StackPointer (sp)\n\n"+
				"On peut cocher la case \"Trace\" pour sauvegarder\n"+
				"l'état du processeur après chaque instruction lue dans le\n"+
				"fichier \"trace.txt\".\n"+
				"\n\nPour plus d'infos, voir http://sylvain.cherrier.free.fr",
				"LittleThinker",
				JOptionPane.INFORMATION_MESSAGE)
        );
        buttons.add(infoButton);
        //Affichage du slider
        JPanel sliderPanel = new JPanel();
        sliderPanel.setLayout(new BoxLayout(sliderPanel, BoxLayout.X_AXIS));
        slider = new SpeedSlider();
        sliderPanel.add(new JPanel());
        sliderPanel.add(new JLabel("Régler la vitesse:", SwingConstants.RIGHT));
        sliderPanel.add(slider);
        sliderPanel.add(new JPanel());
        //Affichage du bouton de sauvegarde
        saveMemButton = new JButton("Sauver");
        saveMemButton.addActionListener(e -> this.saveMem());
        buttons.add(saveMemButton);
        //Affichage du bouton de chargement
        loadMemButton = new JButton("Charger");
        loadMemButton.addActionListener(e -> this.loadMem());
        buttons.add(loadMemButton);
        //Checkbox de sauvegarde de trace
		JPanel checkBoxPanel = new JPanel();
		checkBoxPanel.setLayout(new BoxLayout(checkBoxPanel, BoxLayout.X_AXIS));
        checkBox = new JCheckBox("Trace");
        checkBoxPanel.add(checkBox);
        
        
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        add(new JPanel());
        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.X_AXIS));
        titlePanel.add(new JLabel("LittleThinker"));
        add(titlePanel);
        add(new JPanel());
        add(textFields);
        add(new JPanel());
        add(buttons);
        add(new JPanel());
        add(checkBoxPanel);
        add(new JPanel());
        add(sliderPanel);
        add(new JPanel());
	}
	
	/**
	 * Récupérer les controles du processeur
	 * @return Les controles du processeur (de type Controls)
	 */
	public Controls getControls() {
		return this.controls;
	}
	
	/**
	 * Récupérer les registres du processeur
	 * @return Les registres du processeur (de type Registers)
	 */
	public Registers getRegisters() {
		return this.registers;
	}
	
	/**
	 * Récupérer le terminal du processeur
	 * @return Le terminal du processeur
	 */
	public Terminal getTerm() {
		return this.term;
	}
	
	/**
	 * Reinitialise le fichier trace.txt
	 */
	private void resetTrace() {
		try {
			SaveLoad.resetTrace();
		} catch (IOException e) {
			term.printlnErr("Impossible de réinitialiser trace.txt");
		}
	}
	
	/**
	 * Désactive les boutons du processeur, et active le bouton Stop
	 */
	private void disableButtons() {
		// desactivation des boutons lorsqu'on "start" ou saveMemory"next"
		startButton.setEnabled(false);
		nextButton.setEnabled(false);
		slider.setEnabled(false);
		stopButton.setEnabled(true);
		mem.setEditable(false);
        saveMemButton.setEnabled(false);
        loadMemButton.setEnabled(false);
        checkBox.setEnabled(false);
	}
	
	/**
	 * Active les boutons du processeur, et désactive le bouton Stop
	 */
	private void enableButtons() {
		// Reactivation des boutons a la fin d'une action
		mem.setEditable(true);
		startButton.setEnabled(true);
		nextButton.setEnabled(true);
		slider.setEnabled(true);
		stopButton.setEnabled(false);
        saveMemButton.setEnabled(true);
        loadMemButton.setEnabled(true);
        checkBox.setEnabled(true);
	}
	
	/**
	 * Méthode appelée lorsqu'on clique sur le bouton "Sauver"
	 * Permet de choisir un fichier dans lequel la mémoire sera sauvegarder
	 * Force l'ajoute d'une extension .lt si l'utilisateur ne la met pas de lui meme
	 */
	private void saveMem() {
		mem.setEditable(false);
		JFileChooser chooser = new JFileChooser();
		FileNameExtensionFilter filter = new FileNameExtensionFilter("LittleThinker Files \".lt\"", "lt");
		chooser.setFileFilter(filter);
		chooser.setAcceptAllFileFilterUsed(false);
		chooser.setDialogTitle("Sauvegarder la mémoire");
		chooser.setApproveButtonText("Sauver");
	    if(chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
	    	File f = chooser.getSelectedFile();
	    	int i = f.getName().lastIndexOf('.');
	    	String extension = f.getName().substring(i+1);
	    	if (i == 0 || !extension.equalsIgnoreCase("lt")) {
	    		f = new File(f.toString()+".lt");
	    	}
			try {
				SaveLoad.saveMemory(f.toPath(), mem);
				term.printlnSuccess("Sauvegarde réussie dans " + f.getAbsolutePath());
			} catch (IOException e) {
				term.printlnErr(Terminal.ERR_FILE);
			}
	    }
		mem.setEditable(true);
	}
	
	/**
	 * Méthode appelée lorsqu'on clique sur le bouton "Charger"
	 * Permet de choisir un fichier dans lequel on lira une mémoire
	 * Le fichier doit obligatoirement avoir une extension .lt
	 */
	private void loadMem() {
		mem.setEditable(false);
		JFileChooser chooser = new JFileChooser();
		FileNameExtensionFilter filter = new FileNameExtensionFilter("LittleThinker Files \".lt\"", "lt");
		
		chooser.setFileFilter(filter);
		chooser.setAcceptAllFileFilterUsed(false);
		chooser.setDialogTitle("Charger une mémoire");
		chooser.setApproveButtonText("Charger");
		
	    if(chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
			try {
				SaveLoad.loadMemory(chooser.getSelectedFile().toPath(), mem);
				term.printlnSuccess("Chargement de " + chooser.getSelectedFile().getAbsolutePath()+ " réussi");
			} catch (IOException e) {
				term.printlnErr(Terminal.ERR_FILE);
			} catch (ArrayIndexOutOfBoundsException e) {
				term.printlnErr("Fichier trop long, tout n'a pas pu être chargé");
			}
	    }
		mem.setEditable(true);
	}
	
	/**
	 * Méthode utilisée dans un thread lorsqu'on appuie sur le bouton start.
	 * Cette méthode parcours la mémoire afin de la lire
	 */
	private synchronized void runStart() {
		// Code du Thread pour le bouton start
		reset();
		controls.setI(0, slider.getValue());
		int i = 1;
		
		if (checkBox.isSelected()) {
			term.printlnSuccess("La trace du processeur sera sauvegardé après chaque instruction dans \"trace.txt\"");
			resetTrace();
		}
	
		while (parse(controls.getI(), i)) {
			if (checkBox.isSelected()) {
				SaveLoad.saveTrace(this);
			}
			if (Thread.interrupted()) {
				enableButtons();
				return;
			}
			i += 1;
		}
		
		if (checkBox.isSelected()) {
			SaveLoad.saveTrace(this);
		}
		enableButtons();
	}	
	
	/**
	 * Méthode appelée lorsqu'on appuie sur le bouton start
	 * On créé un nouveau thread dans lequel on appelle la méthode qui lira la mémoire
	 */
	private void start() {
		// code du bouton start
		t = new Thread(this::runStart);
		disableButtons();
		t.start();
	}
	
	/**
	 * Méthode utilisée dans un thread lorsqu'on appuie sur le bouton next.
	 * Cette méthode lis une seule ligne de la mémoire
	 */
	private synchronized void runNext() {	
		// Code du Thread pour le bouton next
		if (!parse(controls.getI(), -1))
			reset();
		if (checkBox.isSelected()) {
			SaveLoad.saveTrace(this);
			term.printlnSuccess("Trace du processeur sauvegardé dans \"trace.txt\"");
		}
		enableButtons();
	}

	/**
	 * Méthode appelée lorsqu'on appuie sur le bouton next
	 * On créé un nouveau thread dans lequel on appelle la méthode qui lira une ligne de la mémoire
	 */
	private void next() {
		// code du bouton next
		t = new Thread(this::runNext);
		disableButtons();
		t.start();
	}
	
	/**
	 * Méthode appelée lorsqu'on appuie sur le bouton Stop
	 * Interromp le Thread qui lit la mémoire
	 */
	private void stop() {
		// code du bouton stop
		t.interrupt();
		try {
			t.join(); //pour etre sur que le run s'arrete avant de reset.
		} catch (InterruptedException e) {Thread.currentThread().interrupt();}
		term.printlnErr("Interruption de la lecture de la mémoire, réinitialisation du processeur");
		reset();
	}
	
	/**
	 * Reinitialise le processeur (ses controles et ses registres)
	 * et ses sauvegardes de trace
	 */
	private void reset() {
		registers.reset();
		controls.reset();
	}
	
	/**
	 * Parse une ligne de la mémoire
	 * @param instructionIndex adresse à lire dans la mémoire
	 * @param index Numero d'éxécution à afficher dans le terminal. -1 si on ne veut pas l'afficher
	 * @return true si on a réussi a lire une ligne, false sinon
	 */
	private boolean parse(int instructionIndex, int index) {
		String instruction;
		
		try {
			if (!(mem.getValueAt(instructionIndex, 1) instanceof String)) {
				term.printlnErr(Terminal.ERR_INSTRUCTION);
				return false;
			}
			instruction = (String) mem.getValueAt(instructionIndex, 1);
		} catch (ArrayIndexOutOfBoundsException e) {
			term.printlnErr(Terminal.ERR_MEMORY);
			return false;
		}
		
		StringTokenizer st = new StringTokenizer(instruction, " ");
		int nbToken = st.countTokens();
		String[] cmd = new String[nbToken];
		for (int i = 0; i < nbToken; i++) {
			cmd[i] = st.nextToken();
		}
		
		// On regarde d'abord si l'instruction et vide, et on affiche ce que l'on va faire
		if (nbToken == 0) { 
			if (index != -1)
				term.println("Execution n°" + index +" (case n°"+instructionIndex + "): STOP");
			else 
				term.println("STOP");
			controls.setD("STOP", slider.getValue());
			return false;
		}			
		if (index != -1)
			term.println("Execution n°" + index +" (case n°"+instructionIndex + "): " + instruction);
		else
			term.println(instruction);
		controls.setD(instruction, slider.getValue());
			
        try {
        	// gestion des ruptures
        	if (cmd[0].equals("beq"))
	            return rupture(() -> controls.getStatus() == StatusRegister.Z_STATUS, instructionIndex, nbToken, cmd);
			if (cmd[0].equals("bnz"))
	            return rupture(() -> controls.getStatus() != StatusRegister.Z_STATUS, instructionIndex, nbToken, cmd);
	        if (cmd[0].equals("bmi"))
	            return rupture(() -> controls.getStatus() == StatusRegister.N_STATUS, instructionIndex, nbToken, cmd);
	        if (cmd[0].equals("bpl"))
	            return rupture(() -> controls.getStatus() != StatusRegister.N_STATUS, instructionIndex, nbToken, cmd);
	        if (cmd[0].equals("bde"))
	            return rupture(() -> controls.getStatus() == StatusRegister.D_STATUS, instructionIndex, nbToken, cmd);
	        if (cmd[0].equals("bnd"))
	            return rupture(() -> controls.getStatus() != StatusRegister.D_STATUS, instructionIndex, nbToken, cmd);
	        if (cmd[0].equals("brn"))
	            return rupture(() -> true, instructionIndex, nbToken, cmd);
        
	        // reinitialisation du registre d'etat
	        controls.setStatus(StatusRegister.NONE_STATUS, slider.getValue());
		
	        // gestion des autres operateurs
			if (cmd[0].equals("ld")) {
				// ld(cible, valeur)
				if (nbToken == 3) {
					return load(cmd[1], cmd[2], instructionIndex);
				}
				term.printlnErr(Terminal.ERR_SYNTAX);
				return false;
			}
			
			else if (cmd[0].equals("st")) {
				// st(valeur, cible)
				if (nbToken == 3) {
					return store(cmd[1], cmd[2], instructionIndex);
				}
				term.printlnErr(Terminal.ERR_SYNTAX);
				return false;
			}
			
			else if(cmd[0].equals("mv")) {
				// mv(valeur, cible) [ici la valeur est le registre d'origine]
				if (nbToken == 3) {
					return move(cmd[1], cmd[2], instructionIndex);
				}
				term.printlnErr(Terminal.ERR_SYNTAX);
				return false;
			}
			
			else if (cmd[0].equals("inc") || cmd[0].equals("dec")) {
				// inc(cible)
				// dec(cible)
				if (nbToken == 2) {
					return incDec(cmd[0], cmd[1], instructionIndex);
				}
				term.printlnErr(Terminal.ERR_SYNTAX);
				return false;
			}
			
			else if (cmd[0].equals("ps")) {
				//ps(valeur) Ajoute la valeur dans la pile
				if (nbToken != 2) {
					term.printlnErr(Terminal.ERR_SYNTAX);
					return false;
				}
				controls.setP(controls.getP()-1, slider.getValue());
				return store(cmd[1], "$"+(controls.getP()+1), instructionIndex);
			}
			else if (cmd[0].equals("pp")) {
				//pp(cible) Récupère une valeur de la pile et la place dans le registre cible
				if (nbToken != 2) {
					term.printlnErr(Terminal.ERR_SYNTAX);
					return false;
				}
				controls.setP(controls.getP()+1, slider.getValue());
				if (controls.getP() >= mem.getRowCount()) {
					term.printlnErr(Terminal.ERR_STACK);
					return false;
				}
				return load(cmd[1], "$"+controls.getP(), instructionIndex);
			}
			
			else {
				// une operation mathematique (add, sub, ...)
				if (nbToken != 2) {
					term.printlnErr(Terminal.ERR_SYNTAX);
					return false;
				}
				return op(cmd[0], cmd[1], instructionIndex);
			}
		} catch (NumberFormatException e){
			term.printlnErr(Terminal.ERR_PARSE_INT);
			return false;
			
		} catch (ArrayIndexOutOfBoundsException e) {
			term.printlnErr(Terminal.ERR_MEMORY);
			return false;
		}
	}
	
	/**
	 * Correspond a la commande mv
	 * @param cmd1 Registre source sous forme de String
	 * @param cmd2 Registre cible sous forme de String
	 * @param instructionIndex Indice de l'instruction lue
	 * @return Si la commande a été réalisée avec succès ou non
	 */
	private boolean move(String cmd1, String cmd2, int instructionIndex) {
		int value;
		int target;
		if (cmd1.length() == 1 && cmd1.charAt(0) == 'a') {
			value = controls.getA();
		}
		else if (cmd1.charAt(0) == '#') {
			value = Integer.parseInt(cmd1.substring(1));
			if (value < 0 || value > Registers.REGISTER_COUNT) {
				term.printlnErr(Terminal.ERR_REGISTER);
				return false;
			}
			value = registers.getR(value);
		}
		else {
			term.printlnErr(Terminal.ERR_SYNTAX);
			return false;
		}
		if (cmd2.length() == 1 && cmd2.charAt(0) == 'a') {
			target = -1;
		}
		else if (cmd2.charAt(0) == '#') {
			target = Integer.parseInt(cmd2.substring(1));
			if (target < 0 || target > Registers.REGISTER_COUNT) {
				term.printlnErr(Terminal.ERR_REGISTER);
				return false;
			}
		}
		else {
			term.printlnErr(Terminal.ERR_SYNTAX);
			return false;
		}

		if (target == -1) {
			controls.setA(updateFlag(value, slider.getValue()), slider.getValue());
		}
		else 
			registers.setR(target, updateFlag(value, slider.getValue()), slider.getValue());
		
		controls.setI(instructionIndex + 1, slider.getValue());
		return true;
	}
	
	/**
	 * Correspond aux commandes inc et dec
	 * @param cmd0 inc ou dec
	 * @param cmd1 registre cible
	 * @param instructionIndex Indice de l'instruction lue
	 * @return Si la commande a été réalisée avec succes ou non
	 */
	private boolean incDec(String cmd0, String cmd1, int instructionIndex) {
		int target;
		int value;
		if (cmd1.charAt(0) == '#') {
			target = Integer.parseInt(cmd1.substring(1));
			if (target < 0 || target > Registers.REGISTER_COUNT) {
				term.printlnErr(Terminal.ERR_REGISTER);
				return false;
			}
			value = registers.getR(target);
			if (cmd0.equals("inc"))
				value++;
			else
				value--;
			registers.setR(target, value, slider.getValue());
			updateFlag(value, slider.getValue());
		}
		else {
			term.printlnErr(Terminal.ERR_SYNTAX);
			return false;
		}
		controls.setI(instructionIndex + 1, slider.getValue());
		return true;
	}
	
	/**
	 * Correspond à la commande st
	 * @param cmd1 registre source
	 * @param cmd2 cible
	 * @param instructionIndex Indice de l'instruction lue
	 * @return Si la commande a été réalisée avec succes ou non
	 */
	private boolean store(String cmd1, String cmd2, int instructionIndex) {
		int target;
		int value;		
		if (cmd1.equals("a")) {
			value = controls.getA();
		}
		else if (cmd1.equals("sp")) {
			value = controls.getP();
		}
		else if (cmd1.equals("pc")) {
			value = controls.getI();
		}
		else if (cmd1.charAt(0) == '#') {
			value = Integer.parseInt(cmd1.substring(1));
			if (value < 0 || value > Registers.REGISTER_COUNT) {
				term.printlnErr(Terminal.ERR_REGISTER);
				return false;
			}
			value = registers.getR(value);
		}
		else {
			value = Integer.parseInt(cmd1);
		}
		if (cmd2.charAt(0) != '$') {
			term.printlnErr(Terminal.ERR_SYNTAX);
			return false;
		}
		target = getAdress(cmd2);
		
		mem.setValue(target, updateFlag(value, slider.getValue()), slider.getValue());
		controls.setI(instructionIndex + 1, slider.getValue());
		return true;
	}
	
	/**
	 * Correspond à la commande ld
	 * @param cmd1 cible
	 * @param cmd2 source
	 * @param instructionIndex Indice de l'instruction lue 
	 * @return Si la commande a été réalisée avec succès ou non
	 */
	private boolean load(String cmd1, String cmd2, int instructionIndex) {
		int target;
		int value;
		if (cmd1.length() == 1 && cmd1.charAt(0) == 'a') {
			target = -1;
		}
		else if (cmd1.charAt(0) == '#') {
			target = Integer.parseInt(cmd1.substring(1));
			if (target < 0 || target > Registers.REGISTER_COUNT) {
				term.printlnErr(Terminal.ERR_REGISTER);
				return false;
			}
		}
		else {
			term.printlnErr(Terminal.ERR_SYNTAX);
			return false;
		}
		if (cmd2.charAt(0) == '$')
			value = mem.getValue(getAdress(cmd2), slider.getValue());
		else
			value = Integer.parseInt(cmd2);
		
		if (target == -1)
			controls.setA(updateFlag(value, slider.getValue()), slider.getValue());
		else 
			registers.setR(target, updateFlag(value, slider.getValue()), slider.getValue());
		
		controls.setI(instructionIndex + 1, slider.getValue());
		return true;
	}
	
	/**
	 * Correspond aux commandes arithmétiques (sub, add, etc...)
	 * @param cmd0 commande
	 * @param cmd1 cible
	 * @param instructionIndex Indice de l'instruction lue 
	 * @return Si la commande a été réalisée avec succès ou non
	 */
	private boolean op(String cmd0, String cmd1, int instructionIndex) {
		int value;
		try {
			if (cmd1.charAt(0) == '$') {
				value = mem.getValue(getAdress(cmd1), slider.getValue());
			}
			else if (cmd1.charAt(0) == '#') {
				value = Integer.parseInt(cmd1.substring(1));
				if (value < 0 || value > Registers.REGISTER_COUNT) {
					term.printlnErr(Terminal.ERR_REGISTER);
					return false;
				}
				value = registers.getR(value);
			}
			else 
				value = Integer.parseInt(cmd1);
			if (cmd0.equals("cmp")) {
				updateFlag(controls.getA() - value, slider.getValue());
				controls.setI(instructionIndex + 1, slider.getValue());
				return true;
			}
			if (cmd0.equals("div") && (controls.getA() % value) != 0) {
				controls.setStatus(StatusRegister.D_STATUS, slider.getValue());
			}
			switch(cmd0) {
				case "add": value = controls.getA() + value; break;
				case "sub": value = controls.getA() - value; break;
				case "mul": value = controls.getA() * value; break;
				case "div": value = controls.getA() / value; break;
				case "mod": value = controls.getA() % value; break;
				default :	term.printlnErr(Terminal.ERR_SYNTAX); return false;
			}
			if (controls.getStatus() == StatusRegister.D_STATUS)
				controls.setA(value, slider.getValue());
			else
				controls.setA(updateFlag(value, slider.getValue()), slider.getValue());
			controls.setI(instructionIndex + 1, slider.getValue());
			return true;
		} catch (ArithmeticException e) {
			term.printlnErr("Erreur: Division par zero");
			return false;
		}
	}
	
	/**
	 * Met à jour le registre d'état selon la valeur
	 * @param val Valeur permettant de définir le statut du registre d'état
	 * @param tempo Vitesse de clignotement du registre
	 * @return val
	 */
	private int updateFlag(int val, int tempo) {
		if (val < 0)
			controls.setStatus(StatusRegister.N_STATUS, tempo);
		if (val == 0)
			controls.setStatus(StatusRegister.Z_STATUS, tempo);
		return val;
	}
	
	/**
	 * Factorise le code pour lire une rupture
	 * @param b Interface Fonctionnelle permettant de savoir si on doit effectuer la rupture ou non.
	 * @param instructionIndex Adresse de l'instruction en cours de lecture
	 * @param nbToken nombre de tokens dans l'instruction
	 * @param cmd Tableau dans lequel l'instruction est découpée
	 * @return true si on a réussi a lire la rupture, false sinon
	 */
	private boolean rupture(BooleanSupplier b, int instructionIndex, int nbToken, String[] cmd) {
	    // pour factoriser le code de gestion des ruptures
	    int value;
	    if (nbToken == 2) {
	        if (b.getAsBoolean()) {
                value = Integer.parseInt(cmd[1]);
                controls.setI(value, slider.getValue());
                return true;
	        }
	        controls.setI(instructionIndex + 1, slider.getValue());
	        return true;
	    }
        term.printlnErr(Terminal.ERR_SYNTAX);
        return false;
    }
	
	/**
	 * Renvoie une adresse lue dans un token d'une instruction. Permet de lire l'adressage indirect
	 * @param str Le token à parse
	 * @return L'adresse lue dans le token
	 */
	private int getAdress(String str) {
		int res;
		String tmp = str.substring(1);
		if (tmp.charAt(0) == '#') {
			res = registers.getR(Integer.parseInt(tmp.substring(1)));
		}
		else {
			res = Integer.parseInt(tmp);
		}
		return res;
	}
}