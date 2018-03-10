package servicePackage;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class FileLoader {

	private static int avg_dl;

	public static HashMap<String, HashMap<Integer, BigDecimal>> loadWeightModel(String weightingScheme) {
		HashMap<String, HashMap<Integer, BigDecimal>> weight;
		weight = new HashMap<String, HashMap<Integer, BigDecimal>>();
		BufferedReader br = null;
		FileReader fr = null;
		String fileName;
		if (weightingScheme.equals("tfidf"))
			fileName = "Preprocessing/tf-IDF.txt";
		else
			fileName = "Preprocessing/BM25.txt";
		int flag = 0, docId = 0;
		String word = null;
		try {
			fr = new FileReader(fileName);
			br = new BufferedReader(fr);
			String x, temp2;
			String[] tokens, occurences;
			// for(int j=0; j<7969; j++)
			// br.readLine();
			while ((x = br.readLine()) != null) {
				flag++;
				tokens = x.split(": ");
				word = tokens[0];
				temp2 = tokens[1];
				occurences = temp2.split(" ");
				weight.put(word, new HashMap<Integer, BigDecimal>());
				for (int i = 0; i < occurences.length; i++) {
					String temp = occurences[i];
					String[] tempToken = temp.split(":");
					docId = Integer.parseInt(tempToken[0]);
					double weigh = Double.parseDouble(tempToken[1]);
					// BigDecimal weight = BigDecimal.va
					weight.get(word).put(docId, BigDecimal.valueOf(weigh));
				}
			}

			fr.close();
			br.close();

		} catch (Exception e) {
			System.out.println(flag);
			System.out.println(word);
			System.out.println("Exception in loadTF-IDF Methd" + e.getMessage());
			e.printStackTrace();
		}
		return weight;
	}

	public static HashMap<Integer, String> loadDocuments() {
		HashMap<Integer, String> documents;
		BufferedReader br = null;
		FileReader fr = null;
		documents = new HashMap<Integer, String>();
		String x, temp, documentName = null, fileName = "Preprocessing/DocumentIds.txt";
		;
		int docId = 0, avgDocLength = 0;
		String tokens[];
		try {
			fr = new FileReader(fileName);
			br = new BufferedReader(fr);

			while ((x = br.readLine()) != null) {
				tokens = x.split(":");
				docId = Integer.parseInt(tokens[0]);
				documentName = tokens[1];
				if(documentName.equals("Panam Case Final Verdict Summary.pdf"))
					documentName = "Panama Case Final Verdict Summary.pdf";
				avgDocLength += Integer.parseInt(tokens[2]);
				documents.put(docId, documentName + ":" + tokens[2]);
			}
			int N = Parser.getNumberofDocs().intValue(); // getting Total number
															// of documents
			setavg_dl(avgDocLength / N);
		} catch (Exception e) {
			System.out.println(docId + " " + documentName);
			e.printStackTrace();
			System.out.println(e.getMessage());
		}
		return documents;
	}

	public static List<String> loadStopwords() { // loading stop words from file
		ArrayList<String> stopwords;
		stopwords = new ArrayList<String>();
		try {
			BufferedReader br = new BufferedReader(new FileReader("D:/Java Neon/workspace/LegalDocSearchEngine/Preprocessing/stopwords.txt"));
			String x;
			while ((x = br.readLine()) != null) {
				stopwords.add(x);
			}
		} catch (Exception e) {
			System.out.println("Error in loadStopword method" + e.getMessage());
			e.printStackTrace();
		}

		return stopwords;
	}

	public static HashMap<String, HashMap<Integer, Integer>> loadDictionary() {
		HashMap<String, HashMap<Integer, Integer>> dictionary;
		dictionary = new HashMap<String, HashMap<Integer, Integer>>();
		BufferedReader br = null;
		FileReader fr = null;
		String FILENAME = "Preprocessing/Dictionary.txt"; // inverted index
		try {
			fr = new FileReader(FILENAME);
			br = new BufferedReader(fr);
			String line;
			String word, occurences = null, x;
			String[] tokens, temp1, temp2;
			br = new BufferedReader(new FileReader(FILENAME));
			Double freq, tfidf;
			int docNumber, tf;

			while ((line = br.readLine()) != null) {
				tokens = line.split(": ");
				word = tokens[0];
				occurences = tokens[1];
				temp1 = occurences.split(" ");
				dictionary.put(word, new HashMap<Integer, Integer>());
				for (int i = 0; i < temp1.length; i++) {
					x = temp1[i];
					temp2 = x.split("-");
					docNumber = Integer.parseInt(temp2[0]);
					tf = Integer.parseInt(temp2[1]);
					dictionary.get(word).put(docNumber, tf);
				}
			}
			// return dictionary;

		} catch (Exception e) {
			System.out.println("Error in LoadFiles.java" + e.getMessage());
			e.printStackTrace();
		}
		return dictionary;
	}

	public static HashMap<Integer, BigDecimal> loadNormalizedDocs(String weightingScheme) {
		HashMap<Integer, BigDecimal> map = new HashMap<Integer, BigDecimal>();
		String x = "";
		String[] temp;
		int docId;
		double weight;
		String fileName = "Normalized" + weightingScheme + "DocumentsWeight.txt";
		try {
			BufferedReader br = new BufferedReader(new FileReader("Preprocessing/" + fileName));
			while ((x = br.readLine()) != null) {
				temp = x.split("-:");
				docId = Integer.parseInt(temp[0]);
				weight = Double.parseDouble(temp[1]);
				map.put(docId, BigDecimal.valueOf(weight));
			}

		} catch (FileNotFoundException e) {
			System.out.println("Exception in loadNormalizeDocs: " + e.getMessage());
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("Exception in loadNormalizeDocs: " + e.getMessage());
			e.printStackTrace();
		}

		return map;
	}

	public static HashMap<String, HashMap<Integer, BigDecimal>> loadNormalizedWeight(String weightModel) {
		HashMap<String, HashMap<Integer, BigDecimal>> N_weight;
		N_weight = new HashMap<String, HashMap<Integer, BigDecimal>>();
		BufferedReader br = null;
		FileReader fr = null;
		String fileName="";
		System.out.println(weightModel);
		if(weightModel.equals("bm25")){
			fileName = "D:/Java Neon/workspace/LegalDocSearchEngine/Preprocessing/Normalized_bm25_Weight.txt";
		}else if(weightModel.equals("tfidf")){
			fileName = "D:/Java Neon/workspace/LegalDocSearchEngine/Preprocessing/Normalized_tfidf_Weight.txt";
		}
		//fileName = "D:/Java Neon/workspace/LegalDocSearchEngine/Preprocessing/Normalized_tfif_Weight.txt";
		//String fileName = "Preprocessing/Normalized_" + weightModel + "_Weight.txt";
		
		int flag = 0, docId = 0;
		String word = null;
		try {
			
			fr = new FileReader(fileName);
			br = new BufferedReader(fr);
			String x, temp2;
			String[] tokens, occurences;
			// for(int j=0; j<7969; j++)
			// br.readLine();
			while ((x = br.readLine()) != null) {
				flag++;
				tokens = x.split(": ");
				word = tokens[0];
				temp2 = tokens[1];
				occurences = temp2.split(" ");
				N_weight.put(word, new HashMap<Integer, BigDecimal>());
				for (int i = 0; i < occurences.length; i++) {
					String temp = occurences[i];
					String[] tempToken = temp.split(":");
					docId = Integer.parseInt(tempToken[0]);
					double weight = Double.parseDouble(tempToken[1]);
					// BigDecimal weight = BigDecimal.va
					N_weight.get(word).put(docId, BigDecimal.valueOf(weight));
				}
			}

			fr.close();
			br.close();

		} catch (Exception e) {
			System.out.println(flag);
			System.out.println(word);
			System.out.println("Exception in loadNormalizedWeights Method " + e.getMessage());
			e.printStackTrace();
		}
		return N_weight;
	}

	@SuppressWarnings("unchecked")
	public static HashMap sortMap(HashMap map) {
		List list = new LinkedList(map.entrySet());
		// Defined Custom Comparator here
		Collections.sort(list, new Comparator() {
			@SuppressWarnings("unchecked")
			public int compare(Object o1, Object o2) {
				return ((Comparable) ((Map.Entry) (o1)).getValue()).compareTo(((Map.Entry) (o2)).getValue());
			}
		});

		// Here I am copying the sorted list in HashMap
		// using LinkedHashMap to preserve the insertion order
		HashMap sortedHashMap = new LinkedHashMap();
		for (Iterator it = list.iterator(); it.hasNext();) {
			Map.Entry entry = (Map.Entry) it.next();
			sortedHashMap.put(entry.getKey(), entry.getValue());
		}
		return sortedHashMap;

	}

	public static ArrayList<String> loadVocabulary() {
		ArrayList<String> vocab = new ArrayList<String>();
		try {
			String x = "";
			BufferedReader br = new BufferedReader(new FileReader("D:/Java Neon/workspace/LegalDocSearchEngine/Preprocessing/Vocabulary.txt"));
			while ((x = br.readLine()) != null)
				vocab.add(x);
			return vocab;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static ArrayList<String> loadVocabularyForSuggestions() {
		ArrayList<String> vocab = new ArrayList<String>();
		try {
			String x = "";
			BufferedReader br = new BufferedReader(new FileReader("D:/Java Neon/workspace/LegalDocSearchEngine/Preprocessing/VocabularyForSuggestions.txt"));
			while ((x = br.readLine()) != null)
				vocab.add(x);
			return vocab;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	

	public static HashMap<String, String> loadCaseClasses() {
		HashMap<String, String> caseClasses = new HashMap<String, String>();
		BufferedReader br;
		try {
			br = new BufferedReader(new FileReader("D:/Java Neon/workspace/LegalDocSearchEngine/Preprocessing/caseClasses.txt"));
			String x;
			
int count=0;
			while ((x = br.readLine()) != null) {
				
				String[] caseClassesTokens = x.split(":");
				// System.out.println(x);
				String fileName = caseClassesTokens[0];
				String caseClass = caseClassesTokens[1];
				//System.out.println(x);
				if(fileName.equals("Panam Case Final Verdict Summary.pdf"))
					fileName = "Panama Case Final Verdict Summary.pdf";
				caseClasses.put(fileName, caseClass);
				
				
				
			}
		//	System.out.println(caseClasses.get("Panama Case Final Verdict Summary.pdf"));
		} catch (FileNotFoundException e) {
			System.out.println("File Not Found" + e.getMessage());
			e.printStackTrace();
		} catch (IOException e1) {
			System.out.println("IOException" + e1.getMessage());
			e1.printStackTrace();
		}

		return caseClasses;
	}

	public static int getavg_dl() {
		return avg_dl;
	}

	public static void setavg_dl(int avgDocLength) {
		avg_dl = avgDocLength;
	}
}
