package autocorrectPacakge;

import java.io.FileNotFoundException;
import java.lang.management.ManagementFactory;
import java.util.ArrayList;
import java.util.List;

import edu.drexel.cs.jah473.args.Arg;
import edu.drexel.cs.jah473.args.ArgParseParams;
import edu.drexel.cs.jah473.args.Args;
import edu.drexel.cs.jah473.autocorrect.Autocorrect;
import edu.drexel.cs.jah473.util.Input;
import edu.drexel.cs.jah473.util.IterableReader;
import servicePackage.FileLoader;
import servicePackage.ObjectIntializer;

import static edu.drexel.cs.jah473.args.Type.*;

public class AutoCorrect {
	static String word;
	static boolean autocomplete = true;
	static int ledDist = 0;
	static boolean whitespace = false;
	static int n = 3;
	private static Autocorrect ac;
	private List<String> corpus;
	
	public AutoCorrect(){
		corpus = FileLoader.loadVocabulary();
		ac = new Autocorrect(corpus,true);
		
	}
	
	// public static void main(String[] args) {
	public String correctPlease(String[] args) {
		if (args.length == 1) {
			word = args[0];
			ledDist = 5;
		}
		// Args.parse(args, params, AutocorrectExample.class);
		/*
		 * IterableReader in; try { in = Input.fromFile("words.txt"); } catch
		 * (FileNotFoundException e) {
		 * System.err.println("Error reading from dictionary"); return null; }
		 */
	

		
		ac.useAutocomplete = autocomplete;
		ac.useLED = ledDist > 0;
		ac.setMaxLED(ledDist);
		ac.useWhitespace = whitespace;
		// ac.suggest(word).stream().limit(n).forEachOrdered(System.out::println);
		String result;
		if (ac.suggest(word).isEmpty())
			return "";
		else
			return ac.suggest(word).get(0);
		// System.out.println(result);

	}
}