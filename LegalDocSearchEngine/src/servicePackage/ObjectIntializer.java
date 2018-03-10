package servicePackage;

import java.util.List;

import autocorrectPacakge.AutoCorrect;
import wordCloudPackage.WordCloudGenerator;

public class ObjectIntializer {
	private static Lemmatizer lem;
	private static AutoCorrect autoCorrect;
	private static QueryExecutor queryExecutor;

	private static List<String> vocab;

	public ObjectIntializer() {
		lem = new Lemmatizer();
		queryExecutor = new QueryExecutor();
		autoCorrect = new AutoCorrect();
	
		vocab = FileLoader.loadVocabulary();
	}

	public static List<String> getVocab(){
		return vocab;
	}
	public static Lemmatizer getLem() {
		return lem;
	}

	public static AutoCorrect getAutoCorrect() {
		return autoCorrect;
	}

	public static QueryExecutor getQueryExecutor() {
		return queryExecutor;
	}

	

}
