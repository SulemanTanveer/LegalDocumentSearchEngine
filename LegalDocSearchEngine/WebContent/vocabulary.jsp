<%@page import="java.util.List"%>
<%@page import="servicePackage.FileLoader"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%
List<String> vocab = FileLoader.loadVocabulary();
%>

<%=vocab %>