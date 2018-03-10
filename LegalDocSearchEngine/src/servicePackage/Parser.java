package servicePackage;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.KeyStore.LoadStoreParameter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.atomic.LongAdder;

import org.apache.commons.io.FileUtils;

import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.PdfTextExtractor;

public class Parser {

	private List<String> stopwords;
	private int docId;
	private Lemmatizer lem;
	private HashMap<String, HashMap<Integer, Integer>> dic;
	private HashMap<String, HashMap<Integer, BigDecimal>> tf_IDF;
	private HashMap<String, HashMap<Integer, BigDecimal>> BM25;
	private HashMap<Integer, Integer> docSize;
	private HashMap<Integer, String> documents;
	private int avg_dl;
	private static Double N;
	private int totalSizeofAllDocs;

	public Parser() {
		docId = 0;

		lem = new Lemmatizer();
		dic = new HashMap<String, HashMap<Integer, Integer>>();
		tf_IDF = new HashMap<String, HashMap<Integer, BigDecimal>>();
		BM25 = new HashMap<String, HashMap<Integer, BigDecimal>>();
		docSize = new HashMap<Integer, Integer>();
		documents = new HashMap<Integer, String>();
		stopwords = new ArrayList<String>();
		avg_dl = 0;
		totalSizeofAllDocs = 0;
		N = (double) 477;

	}

	public static String pdftoText(String location) {
		String text = "";
		try {
			PdfReader reader = new PdfReader(location);
			int pages = reader.getNumberOfPages();
			for (int i = 1; i <= pages; i++)
				text += PdfTextExtractor.getTextFromPage(reader, i);
		} catch (IOException e) {
			System.out.println("Error " + e.getMessage());
		}
		return text;
	}

	public File[] getFolder(String location) {
		File[] directories = new File(location).listFiles(new FileFilter() {

			public boolean accept(File file) {
				// TODO Auto-generated method stub
				return file.isDirectory();
			}
		});

		return directories;
	}

	public void readPdf(File file) {
		File[] files = file.listFiles();
		String[] tokens;
		String text = "";
		String replacedText = null;
		int tf, size;
		String documentName;
		// ** iterating to all the files in the given folder: folder specs are
		// coming in "file" parameter **//
		for (File f : files) {
			documentName = f.getName();

			text = pdftoText(f.getPath());// converting .pdf file to text
			replacedText = text.toLowerCase().replaceAll("[^\\w\\s]", ""); // replace
																			// line
																			// or
																			// anything
																			// that
																			// is
																			// not
																			// word
																			// with
																			// nothing.
																			// i.e
																			// ""
			replacedText = replacedText.replaceAll("( )+", " "); // deleting
																	// white
																	// spaces
			replacedText = replacedText.replaceAll("\n", ""); // deleting new
																// line char
		/*	replacedText = replacedText.replaceAll("\\d",""); //removing digits 
*/			replacedText = lem.lemmatize(replacedText);
			tokens = replacedText.split(" ");

			tf = 0;
			size = tokens.length;
			totalSizeofAllDocs += size;
			docId++;

			documents.put(docId, documentName + ":" + Integer.toString(size));
			System.out.println("docId: " + docId + "  Document Name: " + documentName);
			docSize.put(docId, size);
			try {
				// ** creating HashMap **//
				for (int j = 0; j < size; j++) {
					if (stopwords.contains(tokens[j]))
						continue;
					if (dic.containsKey(tokens[j])) {
						if (dic.get(tokens[j]).get(docId) == null) { // checking
																		// if a
																		// token
																		// does
																		// have
																		// mapping
																		// against
																		// a
																		// particular
																		// document
							dic.get(tokens[j]).put(docId, 1);// if not, creating
																// new mapping
																// against
																// particular
																// document
						} else {
							tf = dic.get(tokens[j]).get(docId); // if mapping
																// exists,
																// increment the
																// tf
							tf++;
							dic.get(tokens[j]).put(docId, tf);
						}
					} else if (!(tokens[j].equals("") || tokens[j].equals(" "))) { // if
																					// dictionary
																					// doesnt
																					// contain
																					// word,
																					// create
																					// mapping
						dic.put(tokens[j], new HashMap<Integer, Integer>());// inserting
																			// word
						dic.get(tokens[j]).put(docId, 1);// inserting document
															// Id and setting
															// its frequency to
															// 1
					}
				}
			} catch (Exception e) {
				System.out.println("Error: " + e.getMessage());
				e.printStackTrace();
			}
		}
		// avg_dl = totalSizeofAllDocs/N.intValue();
		N = (double) docId;

	}

	/* assigning tf-idf weight to each term */
	public void calculateTF_idf() {
		double tf = 0.0, idf = 0.0, df;
		BufferedReader br = null;
		FileReader fr = null;
		String FILENAME = "Preprocessing/Dictionary.txt"; // inverted index
		try {

			fr = new FileReader(FILENAME);
			br = new BufferedReader(fr);
			String line;
			String word, occurences = null, x;
			String[] token, temp1, temp2;
			br = new BufferedReader(new FileReader(FILENAME));
			Double freq, tfidf;
			int docNumber;

			while ((line = br.readLine()) != null) { // reading docId and
														// frequency from
														// dictionary term by
														// term.
				token = line.split(": "); // splitting term and its docId +
											// frequency
				word = token[0]; // term
				occurences = token[1]; // occurences of each word, i.e., docId
										// and frequency of each term
				temp1 = occurences.split(" "); // splitting each single
												// occurences for getting term's
												// tfidf weight. temp1 will
												// contain all the occurences in
												// an array
				tf_IDF.put(word, new HashMap<Integer, BigDecimal>()); // putting
																		// term
																		// in
																		// hashMap
				for (int i = 0; i < temp1.length; i++) {
					x = temp1[i];
					temp2 = x.split("-");// splitting docId and frequency for
											// each occurence.
					docNumber = Integer.parseInt(temp2[0]);// getting docId
					freq = Double.parseDouble(temp2[1]);// getting frequency
					System.out.println("frequency: " + freq);
					System.out.println("Doc Number: " + docNumber);
					System.out.println(docSize.get(docNumber));
					tf = Math.log10(1 + (freq / docSize.get(docNumber).doubleValue())); // calculatin
																						// tf
																						// factor
					df = temp1.length; // df (document frequency)
					idf = Math.log10(N / (double) df); // inverse document
														// frequency
					tfidf = tf * idf;
					tf_IDF.get(word).put(docNumber, BigDecimal.valueOf(tfidf)); // putting
																				// tfidf
																				// weight
																				// factor
																				// of
																				// a
																				// term
																				// for
																				// each
																				// occurence.
				}

			}
			fr.close();
			br.close();

		} catch (IOException e) {
			// System.out.println("l is: " + l);
			System.out.println("Error: " + e.getMessage());
			e.printStackTrace();
		}

	}

	public void creatInvertedIndex() {
		stopwords = FileLoader.loadStopwords();
		FileWriter fw;
		File[] folders = getFolder("Court Decisions"); // getting
																		// all
																		// folders
																		// of
																		// listed
																		// path
		for (int i = 0; i < folders.length; i++)
			readPdf(folders[i]);
		avg_dl = totalSizeofAllDocs / N.intValue();
		ArrayList<String> Vocabulry = new ArrayList<String>();
		try {
			// writting average lenght of all the documents on file
			fw = new FileWriter("Preprocessing/avg_lenghtof All Docs.txt");
			fw.write(Integer.toString(avg_dl));
			fw.flush();
			fw.close();
			/// creating new object to write on another file;
			fw = new FileWriter("Preprocessing/Dictionary.txt", true);
			// FileWriter = new FileWriter("Documents.txt",true);
			// ** Iterating HashMap for printing on file **//
			for (Map.Entry<String, HashMap<Integer, Integer>> word : dic.entrySet()) {
				String wordKey = word.getKey();// getting key part of Parent
												// HashMap (dic).
				fw.append(wordKey + ": ");
				Vocabulry.add(wordKey);// creating Vocabulary
				// System.out.print(wordKey+" : ");
				for (Map.Entry<Integer, Integer> freq : word.getValue().entrySet()) { // Since
																						// value
																						// part
																						// of
																						// dic
																						// HashMap
																						// is
																						// also
					fw.append("" + freq.getKey()); // a HashMap, iterating the
													// Value part of dic.
					fw.append("-" + freq.getValue());
					fw.append(" ");
					// termFreq.add(freq.getValue());
				}

				fw.append("\n");
			}
			fw.flush();
			fw.close();

			HashMap<String, String> docClassification = new HashMap<String, String>();

			fw = new FileWriter("Preprocessing/DocumentIds.txt", true);// re-initialising
																		// filewriter
																		// object
																		// to
																		// write
																		// on
																		// new
																		// file
			// Assigning document Ids to documents
			// String wrd;
			for (Map.Entry<Integer, String> word : documents.entrySet()) {
				int docId = word.getKey();// getting key part
				String docName = word.getValue(); // getting value part
				String[] temp = docName.split(":"); // Since docName have
													// pattern
													// docName:sizeOfDoc, so
													// splitting it to get only
													// docName
				String caseClass = getDocClass(temp[0]);
				docClassification.put(temp[0], caseClass);
				fw.append(docId + ":");
				fw.append(docName);
				fw.append("\n");

			}

			fw.flush();
			fw.close();

			// writing vocabulary to file
			fw = new FileWriter("Preprocessing/Vocabulary.txt", true);
			for (int i = 0; i < Vocabulry.size(); i++)
				fw.write(Vocabulry.get(i) + "\n");
			fw.flush();
			fw.close();

			// writing case of each class to a file
			fw = new FileWriter("Preprocessing/caseClasses.txt", true);
			for (Map.Entry<String, String> word : docClassification.entrySet()) {
				String docName = word.getKey();// getting key part
				String docClass = word.getValue(); // getting value part

				fw.append(docName + ":");
				fw.append(docClass);
				fw.append("\n");

			}
			fw.flush();
			fw.close();
			// System.out.println("Vacablury size is: " + Vocabulry.size());
			// System.out.println("Total # of Documents Read: " + docId);
		} catch (Exception e) {
			System.out.println("Error: " + e.getMessage());
		}
	}

	public void createTF_IDF() {
		calculateTF_idf();
		FileWriter fw;
		try {
			fw = new FileWriter("Preprocessing/tf-IDF.txt", true);

			// ** Iterating HashMap for printing on file **//
			for (Map.Entry<String, HashMap<Integer, BigDecimal>> word : tf_IDF.entrySet()) {
				String wordKey = word.getKey();// getting key part of Parent
												// HashMap (dic).
				fw.append(wordKey + ": ");
				// Vocabulry.add(wordKey);// creating Vocabulary
				// System.out.print(wordKey+" : ");
				for (Map.Entry<Integer, BigDecimal> freq : word.getValue().entrySet()) { // Since
																							// value
																							// part
																							// of
																							// dic
																							// HashMap
																							// is
																							// also
					fw.append("" + freq.getKey()); // a HashMap, iterating the
													// Value part of dic.
					fw.append(":" + freq.getValue());
					fw.append(" ");
					// termFreq.add(freq.getValue());
				}
				fw.append("\n");
			}
			fw.flush();
			fw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void createBM25(double k, double b) {
		BufferedReader br = null;
		FileReader fr = null;
		String FILENAME = "Preprocessing/Dictionary.txt"; // inverted index
		String word, line, occurences = null, x, documentsInfo;
		String[] token, temp1, temp2, docTokens;
		int docId, tf, df, cwd, documentSize;
		Double score;
		try {
			fr = new FileReader(FILENAME);
			br = new BufferedReader(fr);
			br = new BufferedReader(new FileReader(FILENAME));
			HashMap<Integer, BigDecimal> occurencesTF_IDF;
			while ((line = br.readLine()) != null) {
				token = line.split(": ");
				word = token[0]; // word
				occurencesTF_IDF = tf_IDF.get(word);// getting tf-idf posting
													// for token i.e. "word".
				occurences = token[1];
				temp1 = occurences.split(" ");
				BM25.put(word, new HashMap<Integer, BigDecimal>());
				df = temp1.length;
				for (int i = 0; i < temp1.length; i++) {
					x = temp1[i];
					temp2 = x.split("-");
					docId = Integer.parseInt(temp2[0]);

					tf = Integer.parseInt(temp2[1]);
					cwd = tf;
					documentsInfo = documents.get(docId);
					docTokens = documentsInfo.split(":");
					documentSize = Integer.parseInt(docTokens[1]);
					score = (((k + 1) * cwd) / (cwd + k * (1 - b + b * (documentSize / avg_dl))))
							* (Math.log((N + 1)) / df); // idf
					BM25.get(word).put(docId, BigDecimal.valueOf(score));
					System.out.println(score);
				}

			}
			// Writting to file
			FileWriter fw;
			fw = new FileWriter("Preprocessing/BM25.txt", true);

			// ** Iterating HashMap for printing on file **//
			for (Map.Entry<String, HashMap<Integer, BigDecimal>> word1 : BM25.entrySet()) {
				String wordKey = word1.getKey();// getting key part of Parent
												// HashMap (BM25).
				fw.append(wordKey + ": ");
				for (Map.Entry<Integer, BigDecimal> freq : word1.getValue().entrySet()) { // Since
																							// value
																							// part
																							// of
																							// BM25
																							// HashMap
																							// is
																							// also
					fw.append("" + freq.getKey()); // a HashMap, iterating the
													// Value part of it.
					fw.append(":" + freq.getValue());
					fw.append(" ");
				}
				fw.append("\n");
			}
			fw.flush();
			fw.close();

		} catch (Exception e) {
			System.out.println("Error in BM25 method " + e.getMessage());
			e.printStackTrace();
		}
	}

	public void normalizeDocuments(String weightingScheme) {
		HashMap<Integer, BigDecimal> map = new HashMap<Integer, BigDecimal>();
		HashMap<String, HashMap<Integer, BigDecimal>> tempWeightScheme = null;

		tempWeightScheme = FileLoader.loadWeightModel(weightingScheme);
		// else
		// tempWeightScheme = LoadFiles.loadBM25;
		int docId;
		BigDecimal tfidf, weight, newWeight;
		// iterating over hashMap
		for (Entry<String, HashMap<Integer, BigDecimal>> word : tempWeightScheme.entrySet()) {
			String wordKey = word.getKey();// getting key part of Parent HashMap
											// (dic).
			for (Entry<Integer, BigDecimal> freq : word.getValue().entrySet()) { // Since
																					// value
																					// part
																					// of
																					// map
																					// is
																					// also
																					// a
																					// HashMap
				docId = freq.getKey();
				tfidf = freq.getValue();// weight of a term against a particular
										// document
				if (!map.containsKey(docId))
					map.put(docId, tfidf.multiply(tfidf));
				else {// if posting of a document exists then extract that
						// weight and add it with the squared new weight
					weight = map.get(docId);
					tfidf = tfidf.multiply(tfidf);// squaring the weight
					newWeight = weight.add(tfidf);
					map.put(docId, newWeight);
				}
				// termFreq.add(freq.getValue());
			}

		}
		/////////////////////

		// ** writing map to file **//
		int count = 1;
		try {
			FileWriter fw = new FileWriter("Preprocessing/Normalized" + weightingScheme + "DocumentsWeight.txt", true);
			for (Map.Entry<Integer, BigDecimal> data : map.entrySet()) {
				fw.append("" + data.getKey() + "-:" + Math.sqrt(data.getValue().doubleValue()) + "\n");
				System.out.println(count);
				count++;
			}
			fw.flush();
			fw.close();
			System.out.println("Done");

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	// In this method tfidf weight is being normalized. tfidf weight of each
	// term is divided by normalized weight of its document.
	public void lengthNormalization(String weightingScheme) {

		HashMap<String, HashMap<Integer, BigDecimal>> weightModel = new HashMap<String, HashMap<Integer, BigDecimal>>();
		HashMap<Integer, BigDecimal> normWeight = new HashMap<Integer, BigDecimal>();
		weightModel = FileLoader.loadWeightModel(weightingScheme);
		normWeight = FileLoader.loadNormalizedDocs(weightingScheme);
		double tfidf, weight, newWeight;

		int documentId;
		try {
			FileWriter fw = new FileWriter("Preprocessing/Normalized_" + weightingScheme + "_Weight.txt", true);

			for (Entry<String, HashMap<Integer, BigDecimal>> word : weightModel.entrySet()) {
				String wordKey = word.getKey();// getting key part of Parent
												// HashMap (tfidf).
				fw.append(wordKey + ": ");
				for (Entry<Integer, BigDecimal> freq : word.getValue().entrySet()) { // Since
																						// value
																						// part
																						// of
																						// map
																						// is
																						// also
																						// a
																						// HashMap
					documentId = freq.getKey();
					tfidf = freq.getValue().doubleValue();// weight of a term
															// against a
															// particular
															// document
					weight = normWeight.get(documentId).doubleValue();
					newWeight = tfidf / weight; // diviing tfidf by normalized
												// weight of corresponding
												// document.

					fw.append("" + documentId);
					fw.append(":" + BigDecimal.valueOf(newWeight));
					System.out.println(BigDecimal.valueOf(newWeight));
					fw.append(" ");
				}
				fw.append("\n");
			}
			fw.flush();
			fw.close();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public String getDocClass(String fileName) {
		File root = new File("Court Decisions");
		String fileClass = "";
		try {
			boolean recursive = true;
			Collection files = FileUtils.listFiles(root, null, recursive);

			for (Iterator iterator = files.iterator(); iterator.hasNext();) {
				File file = (File) iterator.next();
				if (file.getName().equals(fileName)) { // tokens[0] contains
														// document name
					fileClass = file.getParentFile().getName();
					return fileClass;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return fileClass;
	}

	public static void setNumberofDocs() {
		N = (double) 477;
	}

	public static Double getNumberofDocs() {
		setNumberofDocs();
		return N;
	}
}
