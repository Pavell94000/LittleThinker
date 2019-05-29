package fr.umlv.littlethinker;

import java.awt.Color;

import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

/**
 * Classe héritant de JTextPane et permettant d'afficher des messages dans la fenêtre
 * @author pavell
 *
 */
@SuppressWarnings("serial")
public class Terminal extends JTextPane {
	private final SimpleAttributeSet out;
	private final SimpleAttributeSet success;
	private final SimpleAttributeSet err;
	private SimpleAttributeSet style;
	public static final String ERR_REGISTER = "Erreur: Registre incorrect";
	public static final String ERR_SYNTAX = "Erreur: Syntax";
	public static final String ERR_MEMORY = "Erreur: Adresse incorrecte";
	public static final String ERR_PARSE_INT = "Erreur: Impossible de ParseInteger";
	public static final String ERR_FILE = "Erreur: fichier incorrect";
	public static final String ERR_STACK = "Erreur: pile vide";
	public static final String ERR_INSTRUCTION = "Erreur: instruction non valide";
	
	/**
	 * Initialisation du Terminal
	 */
	public Terminal() {
        super();
        out = new SimpleAttributeSet();
        err = new SimpleAttributeSet();
        success = new SimpleAttributeSet();

        StyleConstants.setForeground(out, Color.WHITE);
        StyleConstants.setFontFamily(out, "Lucida Console");
        StyleConstants.setFontSize(out, 15);
        err.addAttributes(out);
        StyleConstants.setForeground(err, Color.RED);
        StyleConstants.setBold(err, true);
        success.addAttributes(err);
        StyleConstants.setForeground(success, Color.GREEN);
        setBackground(Color.BLACK);
        
        style = success;
        appendText("LittleThinker v1.0");

        setEditable(false);
    }
	
	/**
	 * Ajouter du texte dans le Terminal
	 * @param str Texte à ajouter
	 */
	private void appendText(String str) {
		StyledDocument doc = getStyledDocument();
        try {
        	doc.insertString(doc.getLength(), str, style);
        }
        catch(BadLocationException e) {return;}
        // On fait scroll le terminal vers le bas
        selectAll();
        select(getSelectionEnd(), getSelectionEnd());
	}
	
	/**
	 * Afficher un message d'information
	 * @param str Message à afficher
	 */
	public void println(String str) {
        style = out;
        appendText("\n" + str);
	}
	
	/**
	 * Afficher un message d'erreur
	 * @param str Message à afficher
	 */
	public void printlnErr(String str) {
        style = err;
        appendText("\n" + str);
		
	}
	
	/**
	 * Afficher un message de succès
	 * @param str Message à afficher
	 */
	public void printlnSuccess(String str) {
        style = success;
        appendText("\n" + str);
		
	}
}