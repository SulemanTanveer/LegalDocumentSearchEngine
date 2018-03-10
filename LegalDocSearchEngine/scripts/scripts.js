
	$(document).ready(function() {
    $('#example').dataTable({
    	"ordering": false
    });
    $('label').hide();
	$('border-top').hide();
	$('th').hide();
	
	$.get('WordCloudServlet', function(data) {
        var items = data;
    });
	
	$(".imgDiv").delay(1000).fadeIn(2000);
	
} );
	
	   $(function() { 
		   $.get("vocabulary.jsp", function (response) {
			    var items = response;
			}, "json");
		  	
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
	
	
