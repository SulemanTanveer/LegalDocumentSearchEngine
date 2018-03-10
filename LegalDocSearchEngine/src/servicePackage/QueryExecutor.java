package servicePackage;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.stream.Collectors;

import autocorrectPacakge.AutoCorrect;

public class QueryExecutor {
	private HashMap<String, HashMap<Integer, BigDecimal>> map;
	private String weightingScheme;
	public static List<String> stopwords;
	private static boolean queryChanged;
	private static String correctedQuery;


	public QueryExecutor() {
		weightingScheme = "tfidf";
		queryChanged = false;
		
		// ** These methods should be called if new inverted index has to be
		// created **//

		/*
		 * Parser parser = new Parser(); parser.creatInvertedIndex();
		 * parser.createTF_IDF(); parser.createBM25(0.75, 1.0);
		 */

		// ** These methods should be called if to change weighting scheme i.e.,
		// tfidf or bm25 **//

		/*
		 * parser.normalizeDocuments(weightingScheme);
		 * parser.lengthNormalization(weightingScheme);
		 */
		stopwords = FileLoader.loadStopwords();
		map = FileLoader.loadNormalizedWeight(weightingScheme); // Loading
																// weights
	}

	public List<Integer> executeQuery(String qquery) {

		/*
		 * Parser parser = new Parser(); //parser.creatInvertedIndex();
		 * //parser.createTF_IDF(); //parser.createBM25(0.75, 1.0);
		 * parser.normalizeDocuments(weightingScheme);
		 * parser.lengthNormalization(weightingScheme); //System.exit(0);
		 */
		if (qquery.equals(""))
			return null;
		StringBuilder correctionTemp = new StringBuilder();
		String[] temp = qquery.split(" ");
		/*
		 * System.out.println("Query before Correction: " + qquery); for(int
		 * i=0; i<temp.length; i++){ temp[i] =
		 * AutoCorrect.correctPlease(temp[i].split(" "));
		 * //System.out.println("query tokens " + temp[i]);
		 * correctionTemp.append(temp[i] + " "); }
		 * System.out.println("Query After correction is: " +
		 * correctionTemp.toString());
		 */
		StringBuilder query = new StringBuilder();
		StringBuilder unCorrectedQuery = new StringBuilder();
		
		for (int i = 0; i < temp.length; i++) {
			if (stopwords.contains(temp[i]))
				continue;
			unCorrectedQuery.append(temp[i]+" ");
			AutoCorrect autoCorrect = ObjectIntializer.getAutoCorrect();
			temp[i] = autoCorrect.correctPlease(temp[i].split(" "));
			query.append(temp[i] + " ");
			// query.append(" ");
		}
		
		queryChanged = false;
		if(!query.toString().equals(unCorrectedQuery.toString())){ //checking if word corrector corrected
			queryChanged = true;									// some words or not, if corrected, tell user
			correctedQuery = query.toString();
		}
		
		System.out.print(query);
		HashMap<Integer, BigDecimal> postings = new HashMap<Integer, BigDecimal>();
		HashMap<Integer, BigDecimal> ranking = new HashMap<Integer, BigDecimal>();

		//postings = map.get(query.toString());
		String[] queryTokens = query.toString().split(" ");
		String word;
		// we are supposing that each word in a query occurs once only
		int tf_query = 1, docId, df_query;
		double idf_query, tfidf_query, normtfidf_query = 0.0;
		List<Double> queryList = new ArrayList<Double>();

		// ** Normalizing Query vector **//
		int postingFoundCount = 0; // using counter if in query atleast one word occured
						// have posting, dont say no record fount, instead
						// rank
		for (int i = 0; i < queryTokens.length; i++) { // according to that
			df_query = 0;											// particular word which
														// appeard.
			/*if (!map.containsKey(queryTokens[i])){}
				return null;*/
			if (!map.containsKey(queryTokens[i])){
				continue;
			}
				
			postings = map.get(queryTokens[i]);
			df_query = postings.size(); // document frequency query
			idf_query =  Math.log10((double) 479 / df_query); // idf of query
			tfidf_query = tf_query * idf_query; // tfidf of query
			normtfidf_query = normtfidf_query * normtfidf_query; // normalize
			if(normtfidf_query == Double.POSITIVE_INFINITY)// tfidf of
				break;
			//System.out.println(normtfidf_query);												// query
			normtfidf_query = BigDecimal.valueOf(normtfidf_query).add(BigDecimal.valueOf(tfidf_query)).doubleValue(); // adding
																														// to
																														// previous
			queryList.add(tfidf_query);
			postingFoundCount++;
		}
		if(postingFoundCount == 0)
			return null;
		double listData;
		normtfidf_query = Math.sqrt(normtfidf_query); // taking square root of
														// square sum of tfidf
														// of query
		for (int i = 0; i < queryList.size(); i++) {
			listData = queryList.get(i); // saving the list value
			queryList.remove(i); // removing the value which have been saved on
									// previous line : just to remove redundancy
			queryList.add(listData / normtfidf_query); // dividing each weight
														// of query term by its
														// normalized value
		}
		
		// *********************************** //

		// ** Ranking Documents **//
		BigDecimal NmDocWeight, NmQueryWeight, weight;
		for (int i = 0; i < postingFoundCount; i++) {
			word = queryTokens[i];
			postings = map.get(word);

			for (Map.Entry<Integer, BigDecimal> docOccurence : postings.entrySet()) {
				docId = docOccurence.getKey();
				NmDocWeight = docOccurence.getValue(); // normalize doc vector
				NmQueryWeight = BigDecimal.valueOf(queryList.get(i)); // normalized
																		// query
																		// vector
				weight = NmDocWeight.multiply(NmQueryWeight); // taking product
																// of query
																// vector and
																// document
																// vector
				if (ranking.containsKey(docId)) { // Adding weight of same
													// document
					weight = weight.add(ranking.get(docId));
					ranking.put(docId, weight);
				} else {
					ranking.put(docId, weight);
				}
			}

		}
		ranking = FileLoader.sortMap(ranking); // Sorts a Hashmap by value
		// return ranking;
		List<Integer> rankedDocuments = new ArrayList<Integer>();
		for (Map.Entry<Integer, BigDecimal> entry : ranking.entrySet()) {
			// System.out.println("docId: " + entry.getKey() + " Value: " +
			// entry.getValue());
			rankedDocuments.add(entry.getKey());
		}
	
		return rankedDocuments;

	}

	public static boolean isQueryChanged(){
		return queryChanged;
		
	}
	public static String getCorrectedQuery(){
		return correctedQuery;
	}
}
