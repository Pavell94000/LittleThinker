package fr.umlv.littlethinker;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Classe contenant les fonction de lecture et d'écriture dans un fichier
 * Ne peut pas être instanciée
 */
public final class SaveLoad {
	private SaveLoad() {}
	
	/**
	 * Sauvegarde une mémoire dans un fichier
	 * @param p Path vers le fichier dans lequel on veut écrire la mémoire
	 * @param mem Mémoire a écrire dans le fichier
	 * @throws IOException Si il y a une erreur lors de l'écriture dans le fichier
	 */
	public static void saveMemory(Path p, Memory mem) throws IOException {
		try (BufferedWriter writer = Files.newBufferedWriter(p)){
			for (int i = 0; i < mem.getRowCount(); i++) {
				writer.write(mem.getValueAsString(i));
				writer.newLine();
			}
		}
	}
	
	/**
	 * Lis une mémoire à partir d'un fichier
	 * @param p Path vers le fichier qui doit être lu
	 * @param mem Memoire dans laquelle on va écrire le fichier
	 * @throws IOException Si il y a une erreur lors de la lecture du fichier
	 */
	public static void loadMemory(Path p, Memory mem) throws IOException {
		String line;
		try (BufferedReader reader = Files.newBufferedReader(p)){
			for (int i = 0; (line = reader.readLine()) != null; i++) {
				mem.setValue(i, line);
			}
		}
	}
	
	/**
	 * Enregistre l'état du processeur dans le fichier "trace.txt"
	 * @param proc Processeur dont on veut faire la photo
	 */
	public static void saveTrace(Processor proc){
		try (BufferedWriter writer = new BufferedWriter(new FileWriter("trace.txt", true))){
			writer.write("C: ");
			writer.write(proc.getControls().getIAsString());
			writer.write(" | ");
			writer.write("D: ");
			writer.write(proc.getControls().getD());
			writer.write(" | ");
			writer.write("A: ");
			writer.write(proc.getControls().getAAsString());
			writer.write(" | ");
			writer.write("F: ");
			writer.write(proc.getControls().getStatusAsString());
			writer.write(" | ");
			writer.write("R: ");
			for (int i = 0; i < Registers.REGISTER_COUNT; i++) {
				writer.write(proc.getRegisters().getRAsString(i));
				writer.write(" ");
			}
			writer.newLine();
		} catch (IOException e) {
			proc.getTerm().printlnErr("Impossible de sauvegarder la trace");
		}
	}
	
	/**
	 * Créer ou écrase le fichier trace.txt
	 * @throws IOException Si il y a une erreur lors de l'écriture dans le fichier
	 */
	public static void resetTrace() throws IOException {
		Path p = Paths.get("trace.txt");
		try (BufferedWriter writer = Files.newBufferedWriter(p)) {
			writer.write("");
		}
	}
}
