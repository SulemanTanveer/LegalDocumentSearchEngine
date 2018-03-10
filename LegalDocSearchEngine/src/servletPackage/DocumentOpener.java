package servletPackage;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.Iterator;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;

/**
 * Servlet implementation class Search
 */
@WebServlet("/search")
public class DocumentOpener extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();

		// String fileName = session.getAttribute("docName").toString();
		String fileName = request.getParameter("fileName");
		String filePath = "";
		//System.out.println("fileName in servlet is: " + fileName);
		File root = new File("D:/Java Neon/workspace/LegalDocSearchEngine/Court Decisions");
		try {
			boolean recursive = true;
			Collection files = FileUtils.listFiles(root, null, recursive);

			for (Iterator iterator = files.iterator(); iterator.hasNext();) {
				File file = (File) iterator.next();
				if (file.getName().equals(fileName)) // tokens[0] contains
														// document name
					filePath = file.getAbsolutePath(); // absolute path return
														// complete path of a
														// particular file
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("Collecion is workng " + filePath);

		// String filePath = "D:/Java Neon/workspace/courtDecision/";
		response.setContentType("APPLICATION/PDF");
		response.setHeader("Content-Disposition", "inline; filename=\"" + fileName + "\"");
		// System.out.println(filePath + fileName);
		FileInputStream fi = new FileInputStream(filePath);
		int i;
		while ((i = fi.read()) != -1)
			out.write(i);
		out.close();
		fi.close();
	}

}
