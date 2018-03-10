# LegalDocSearchEngine

Legal Document Search Engine is a web base search engine which is used for searching legal contents and legislation.
It is developed in Java using JSPs and Servlet.
User provides a query, by applying IR techniques, a set of results is displayed to the user based upon there relevance.
Two models of Information Retireival(IR) techniques are used, BM25 and TF-IDF.
For user's persuation; Auto-correct, Suggestions and word cloud has been made.

# Installation

1. Instal IDE, Ecplise or Netbeans
2. make sure that you have all the dependencies that a web project might need. Like Tomcat server etc.
3. don't forget to place all the JAR files provided in the repository (WEB-INF -> lib).
4. make sure that you have changed the path of folder named "Court Decision" that contains .pdf files. These files are acting as a database. All of the queries are mapped on those files and after processing all of the IR techniques, best match document is shown to the user.
5. make sure you are connected to the internet and disabled the firewall. As enabling firewall may cause dependencies issues.
