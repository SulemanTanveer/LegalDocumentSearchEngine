<%@page import="com.google.gson.Gson"%>
<%@page import="servicePackage.FileLoader"%>
<%@page import="java.util.List"%>
<%@page import="servicePackage.ObjectIntializer"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1" import = "servletPackage.Servlet"%>
<!DOCTYPE html>
<html lang="en">
<head>
  <title>Legal Document Search Engine</title>
  <meta charset="utf-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <link rel="stylesheet" href="bootstrap-3.3.7/dist/css/bootstrap.min.css">
  <script type="text/javascript" src="bootstrap-3.3.7/js/jquery-3.2.1.min.js"></script>
  <link rel="stylesheet" href="customFiles/style.css">
  <link rel="stylesheet" href="http://code.jquery.com/ui/1.10.3/themes/smoothness/jquery-ui.css" />
  <script src="http://code.jquery.com/jquery-1.9.1.js"></script>
  <script src="http://code.jquery.com/ui/1.10.3/jquery-ui.js"></script>
  <script src="scriptForIndexPage.js"></script>
    <%
    	ObjectIntializer objs = new ObjectIntializer();
   	%> 
</head>
<body>

<div id="header"class="navbar text-center"><h3>Legal Document Search Engine</h3></div>

<br><br><br><br><br><br><br>
<div class="container text-center">    

    <img src="customFiles/logo.jpg"  title="Legal Documents" ><br><br>
    <div class="container">
        <div class="row">
            <div class="col-xs-8 col-xs-offset-2">
                <form action="ServletPath" method="get" id="searchForm" class="input-group">
                    
                  
                    <input type="text" class="form-control" name="searchbox" id = "searchbox" placeholder="Search Documents..." value= "" >
					
					  
                    <span class="input-group-btn">
                        <button class="btn btn-default" type="submit">
                           Search <span class="glyphicon glyphicon-search"></span>
                        </button>
                    </span>
                </form><!-- end form -->     
				
             </div>
        </div><!-- end row --> 
		<h4><b>Discover Legal Docs</b></h4>		
    </div><!-- end container -->    

  
	</div><br>


	<div class="footer2"><p>&copy; Copyright 2017 Suleman Tanveer</p></div>

</body>
</html>
