package fr.umlv.littlethinker;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;

/**
 * Classe qui créé la fenetre principale du programme
 */
@SuppressWarnings("serial")
public class LittleThinker extends JFrame{
	private final Processor proc;
	private final Memory mem;
	private final Terminal term;
	static final int MEMORY_LENGTH = 51;
	
	/**
	 * Initialise le processeur, la mémoire et le terminal dans deux JSplitPane
	 */
	public LittleThinker(){
        super("LittleThinker");

        mem = new Memory(MEMORY_LENGTH);
        term = new Terminal();
        proc = new Processor(mem, term);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 600);

        JSplitPane splitV = new JSplitPane(JSplitPane.VERTICAL_SPLIT, proc, new JScrollPane(term));
        add(splitV);
        
        JSplitPane splitH = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, splitV, new JScrollPane(mem));
        add(splitH);

        setVisible(true);
        splitV.setDividerLocation(0.5);
        splitH.setDividerLocation(0.75);
    }
}