package servicePackage;

import edu.stanford.nlp.ling.CoreAnnotations.LemmaAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.SentencesAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TokensAnnotation;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.util.CoreMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class Lemmatizer {

	private StanfordCoreNLP pepeline;

	public Lemmatizer() {

		Properties props;
		props = new Properties();
		props.put("annotators", "tokenize,ssplit,pos,lemma");
		this.pepeline = new StanfordCoreNLP(props);
	}

	@SuppressWarnings("null")
	public String lemmatize(String doctext) {
		// List<String> lemmas = new ArrayList<>();
		StringBuilder lemmas = new StringBuilder();
		// String lemmas="";
		Annotation document = new Annotation(doctext);
		this.pepeline.annotate(document);

		List<CoreMap> sentences = document.get(SentencesAnnotation.class);

		for (CoreMap sentence : sentences) {
			for (CoreLabel token : sentence.get(TokensAnnotation.class)) {
				// lemmas.add(token.get(LemmaAnnotation.class));
				lemmas.append(token.getString(LemmaAnnotation.class) + " ");
				// lemmas +=token.getString(LemmaAnnotation.class) + " ";
			}
		}
		return lemmas.toString();
	}
}
