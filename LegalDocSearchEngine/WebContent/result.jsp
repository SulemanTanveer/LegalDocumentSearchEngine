<%@page import="servicePackage.QueryExecutor"%>
<%@page import="wordCloudPackage.WordCloudGenerator"%>
<%@page import="com.kennycason.kumo.WordCloud"%>
<%@page import="com.google.gson.Gson"%>
<%@page import="servicePackage.FileLoader"%>
<%@page import="java.io.BufferedReader"%>
<%@page import="java.io.FileReader"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1" import = "java.util.List"%>
<%@ page import = "servletPackage.Servlet" %>
<%@ page import = "java.io.File" %>
<%@ page import = "org.apache.commons.io.FileUtils, java.util.Collection, java.util.Iterator, java.util.HashMap
,org.mcavallo.opencloud.Cloud, org.mcavallo.opencloud.Tag, java.awt.Font, java.util.Random" %>


<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>

<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<link rel="stylesheet" href="bootstrap-3.3.7/dist/css/bootstrap.min.css" >   
<link rel="stylesheet" href="customFiles/style.css">
<link rel="stylesheet" type="text/css" href="bootstrap-3.3.7/css/jquery.autocomplete.css" />
<link rel="stylesheet"  href="bootstrap-3.3.7//DataTables/jquery.dataTables.min.css">
<link rel="stylesheet" href="font-awesome-4.7.0/css/font-awesome.min.css">
<link rel="stylesheet" href="http://code.jquery.com/ui/1.10.3/themes/smoothness/jquery-ui.css" />
<script src="http://code.jquery.com/jquery-1.9.1.js"></script>
<script src="http://code.jquery.com/ui/1.10.3/jquery-ui.js"></script>
<script type="text/javascript"  src="http://cdn.datatables.net/1.10.2/js/jquery.dataTables.min.js"></script>
<script type="text/javascript"  src="bootstrap-3.3.7/dist/js/bootstrap.min.js"></script>
 <script src="scripts.js"></script>

<title>Result</title>
</head>
<body>

<div id="header"class="navbar  text-center"><h3><strong>Legal Document Search Engine</strong></h3></div>

 		<div class=" col-xs-1" >
			<a href="http://localhost:8081/LegalDocSearchEngine/index.jsp"><img src="customFiles/logo.jpg"  title="Legal Documents" ></a>
			</div>	<br><br>
		<div class=" col-md-offset-1 col-xs-7 " >
	 	  <form action="ServletPath" method="get" id="searchForm" class="input-group">
                    <input type="text" class="form-control"  name="searchbox" id = "searchbox"  value="<%=request.getAttribute("query") %>">
                    <span class="input-group-btn">
                        <button class="btn btn-default" id="submitBtn" type="submit">
                           Search <span class="glyphicon glyphicon-search"></span>
                        </button>
                    </span>
                </form><!-- end form -->   
	 	 </div><br>
	 	<div class="row" style="width:100%">
			<div  class="col-lg-1 "></div>
			<div class=" col-lg-offset-2 " >
				<div class="col-lg-5 " ><br><br>
					
					<% if((request.getAttribute("result1").equals("empty"))){ %>
        				<tr>
               				<td>Your Query - <b><big> <%=request.getAttribute("query") %> </big></b> <br> Did not match any document. <br><br>
                				Suggestions: <br><br>
                				<ul>
                					<li>Make sure that all words are spelled correctly.</li>
                					<li>Try different Keywords. </li>
                					<li>Try more general keywords. </li>
                					<li>Try fewer keywords. </li>
                				</ul>
                			</td>
                
        				</tr>
        				
<%}
else{
	List<String> result = (List<String>) request.getAttribute("result");
	HashMap<String,String> caseClasses = (HashMap<String,String>) request.getAttribute("caseClasses");
 	String fileName, caseClass = "";//directory = "Court Decisions";
	int wordCloudCounter = 0;%>
	<table id="example" class="table  table1 "   >
					<thead>
         		 		<tr>
               		 		<th></th>
           				</tr>
        			</thead>
        			<tfoot>
            			<tr> 
                			<th></th>
           				</tr>
       				</tfoot>
			<tbody>
	
  	<% int resultSize = result.size();
  	boolean flag = false;
  	if(QueryExecutor.isQueryChanged()){
  		String oldQuery = request.getAttribute("query").toString();
  		String correctedQuery = request.getAttribute("correctedQuery").toString();
  	%>
  	 <tr>
                <td><big>Showing results for <font color="#337ab7"><b><%=correctedQuery %></b></font></big> <br>instead of <font color="#337ab7"><i><%=oldQuery %></i></font></td>
        </tr>
  	<%
  	}
  	for(int i=0; i<resultSize; i++){
  		/* if(i>10 && flag == false){
  			wordCloud.generateWordCloud();
  			flag = true;
  		} */
		fileName = result.get(i);
		caseClass = caseClasses.get(fileName);
%>
        <tr>
                <td><a href="search?fileName=<%=fileName %>"  target = "_blank"><%=result.get(i) %></a> <small>[<%=caseClass %>]</small><br>Summarized Document Text </td>
        </tr>
<% }/* if(flag == false)
	wordCloud.generateWordCloud(); */
%>
	
	</tbody>
        </table>
       </div>
	
	<%} if(!(request.getAttribute("result1").equals("empty"))){%>
	
<div id = "imgDiv" class="col-xs-2 text-center" >
	
	<div id = "spacing">
	<br><br><br><br><br><br><br><br></div>
	
	<div id = "loaderId" class = "loader"  style ="margin: 0 auto;"> 

	
	 </div> <br>
	<div id="loadingText"><b> Generating Word Cloud...</b></div>
	
	
	<!-- <div>Generating Word Cloud...</div>  -->
	
	 <!--  <img  id = "img_id" hspace="100" src="customFiles/Loading.gif" >  -->
	 	  

 <% } %>
 <br>
  </div>
  




</div></div> 

<br><br><br><br><br><br><br><br><br><br>
<div  class="footer2">&copy; Copyright 2017 Suleman Tanveer</div>
 
 
 
</body>
</html>
 
   <%

 
 	/* WordCloudGenerator wordCloud = new WordCloudGenerator();
 */		/* wordCloud.generateWordCloud(); */
	
	%> 