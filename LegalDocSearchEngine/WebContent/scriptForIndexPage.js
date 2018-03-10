function removeSpecialChar(searchValue) {
    return searchValue.replace(/[&\/\\#,+()$~%.'":*?<>{}]/g,'');
}


$(function() { 
	
	  $('#searchbox').keypress(function(e){
		  if(e.keyCode==13){
	        	console.log(document.getElementById("searchbox").value);
	        	var searchValue = document.getElementById("searchbox").value;
	        	document.getElementById("searchbox").value = removeSpecialChar(searchValue); 
	        	console.log(searchValue);
	        	$(".ui-menu-item").hide();
	        	$(".ui-autocomplete").hide();
	        	$('#submitBtn').click();
	        }
	      });
		  
		 $.get("VocabServlet", function (response) {
		     items = response;
		     //alert(items);
		    function split( val ) {
		      return val.split( /\s/ );
		    }
		    function extractLast( term ) {
		      return split( term ).pop();
		    }
		 
		    $( "#searchbox" )
		      .autocomplete({
		        minLength: 3,
		        source: function( request, response ) {
		        	var results = $.ui.autocomplete.filter(
		                    items, extractLast( request.term ) );
		         response(results.slice(0, 50));
		        },
		        
		        focus: function() {
		          return false;
		        },
		        select: function( event, ui ) {
		          var terms = split( this.value );
		          // remove the current input
		          var lastTerm = terms.pop();
		          // add the selected item
		          terms.push( ui.item.value );
		          // add placeholder to get the comma-and-space at the end
		          terms.push( "" );
		          this.value = terms.join( " " );
		          return false;
		        }
		      });
		  });
	
	

	
} );
	
	