package servletPackage;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import servicePackage.FileLoader;
import servicePackage.ObjectIntializer;
import wordCloudPackage.WordCloudGenerator;

/**
 * Servlet implementation class WordCloudServlet
 */
@WebServlet("/WordCloudServlet")
public class WordCloudServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static WordCloudGenerator wordCloudGenerator;
	
   
    
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		/*Path path = FileSystems.getDefault().getPath("WordCloud", "Cloud.png");
		Files.deleteIfExists(path);*/
		boolean success = (new File("D:\\Java Neon\\workspace\\LegalDocSearchEngine\\WebContent\\WordCloud\\Cloud.png")).delete();
		//System.out.println("deletion " + success);
		//if(success){
		wordCloudGenerator = new WordCloudGenerator();
		wordCloudGenerator.generateWordCloud();
		//}
		System.out.println("Hellooo from Cloud Servlet");
	}


}
