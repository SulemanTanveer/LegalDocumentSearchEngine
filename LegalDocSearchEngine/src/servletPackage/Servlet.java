package servletPackage;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import autocorrectPacakge.AutoCorrect;
import servicePackage.Lemmatizer;
import servicePackage.ObjectIntializer;
import servicePackage.Parser;
import servicePackage.FileLoader;
import servicePackage.QueryExecutor;
import wordCloudPackage.WordCloudGenerator;

/**
 * Servlet implementation class Servlet
 */
@WebServlet("/ServletPath")
public class Servlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Lemmatizer lem;
	private AutoCorrect autoCorrect;
	private QueryExecutor queryExecutor;
	private HashMap<Integer, String> docIds;
	private HashMap<String, String> caseClasses;
	private List<String> result;
	private static List<String> vocab;
	private WordCloudGenerator wc;
	private static String textForCloud;

	@Override
	public void init() throws ServletException {
		lem = ObjectIntializer.getLem();
		queryExecutor = ObjectIntializer.getQueryExecutor();
		docIds = FileLoader.loadDocuments();
		caseClasses = FileLoader.loadCaseClasses();
		autoCorrect = ObjectIntializer.getAutoCorrect();
		
		vocab = FileLoader.loadVocabulary();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String query = request.getParameter("searchbox");
		String tquery = query;
		request.setAttribute("query", tquery);
		query = query.toLowerCase().replaceAll("[^\\w\\s]", ""); // replace line or anything that is not word with nothing i.e ""
																
		query = query.replaceAll("( )+", " "); // deleting white spaces
		query = query.replaceAll("\n", ""); // deleting new line char
		query = query.replaceAll("[^a-zA-Z0-9\\s]", ""); // removing special character from query
		query = lem.lemmatize(query);
		request.setAttribute("result1", "NotEmpty");
		List<Integer> rankedDocumets = queryExecutor.executeQuery(query);
		if(QueryExecutor.isQueryChanged()){
			String correctedQuery = QueryExecutor.getCorrectedQuery();
			request.setAttribute("correctedQuery", correctedQuery);
		}
		
		result = new ArrayList<String>();
		if (rankedDocumets == null) {
			System.out.println("Sorry, word not found");
			String result1 = "empty";
			request.setAttribute("result1", result1);

			request.setAttribute("caseClasses", caseClasses);
			RequestDispatcher dispatcher = request.getRequestDispatcher("result.jsp");
			dispatcher.forward(request, response);
		} else {
				
			List<String> docClassification = new ArrayList<String>();
			for (int i = rankedDocumets.size() - 1; i >= 0; i--) { // since sortMap func return sort in asc order, we are iterating it in reverse order
				String docPlusSize = docIds.get(rankedDocumets.get(i));
				String[] tokens = docPlusSize.split(":");
				result.add(tokens[0]); // token[0] contains document name
			}

			request.setAttribute("result", result);
			// Code Block for WordCloud
			int resultSize = result.size();
			int cloudDocsSize = 10;
			if (!(resultSize > 10))
				cloudDocsSize = resultSize;

			String dir = "D:/Java Neon/workspace/LegalDocSearchEngine/Court Decisions/";
			String fileName = "";
			StringBuilder text = new StringBuilder();
			
			StringBuilder docText = new StringBuilder();
			List<String> stopwords = FileLoader.loadStopwords();
			
			for (int i = 0; i < cloudDocsSize; i++) {
				fileName = result.get(i);
				//System.out.println("fileName is: " + fileName);
				String clasSS = caseClasses.get(fileName);
				
				
				String filePath = dir + caseClasses.get(fileName) + "/" + fileName;
				String parsedText = Parser.pdftoText(filePath);
				docText.append(parsedText);
			}
			String tempDocText = docText.toString(); // since lematizer takes string not stringBulder, so convert Stringbuilder to string
			tempDocText = tempDocText.toLowerCase().replaceAll("[^\\w\\s]", ""); // replace line or anything that is not word with nothing i.e ""
			tempDocText = tempDocText.replaceAll("( )+", " "); // deleting white spaces
			tempDocText = tempDocText.replaceAll("\n", ""); // deleting new line char
			String[] tempText = tempDocText.split(" ");
			int count = 0;

			for (int j = 0; j < tempText.length; j++) {
				if (stopwords.contains(tempText[j]))
					continue;
				text.append(tempText[j] + " ");
				if (count == 100) {
					text.append("\n");
					count = 0;
				}
				count++;
			}
	
			//textForCloud = text.toString();
			String cloudText = text.toString();
			WordCloudGenerator.setCloudText(cloudText);
			
			request.setAttribute("caseClasses", caseClasses);
			RequestDispatcher dispatcher = request.getRequestDispatcher("result.jsp");
			dispatcher.forward(request, response);

		}
	}

	

}
