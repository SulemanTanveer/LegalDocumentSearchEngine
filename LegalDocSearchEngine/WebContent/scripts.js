var items;

function alpha(e) {
    var k;
    document.all ? k = e.keyCode : k = e.which;
    return ((k > 64 && k < 91) || (k > 96 && k < 123)  || k==95|| k==34 || k == 8 || (k>= 44 && k<=46) 
    		|| k == 63 || k == 32 || (k >= 48 && k <= 57));
}

function removeSpecialChar(searchValue) {
    return searchValue.replace(/[&\/\\#,+()$~%.'":*?<>{}]/g,'');
}

/*function myFunction() {
	console.log(document.getElementById("searchbox").value);
	var x = document.getElementById("searchbox").value;
    document.getElementById("searchbox").value = removeSpecialChar(x);
    console.log(document.getElementById("searchbox").value);
}*/

	$(document).ready(function() {
    $('#example').dataTable({
    	"ordering": false
    	
    });
    
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
    
    $('label').hide();
	$('border-top').hide();
	$('th').hide();

	$.get('WordCloudServlet', function(data) {
		
		console.log("Making PNG",data);
		var start = Date.now();
        var  now = start;
        var ms = 5000;
    	
    /*while (now - start <= ms) {
    	var time = now-start;
    	console.log(time);
    	if(time+100 >= ms)
    	{ 
    		break;
    	}
    	now = Date.now();
    }*/
    
    setTimeout(function () {
    	 $('#loaderId').hide();
    	 $('#loadingText').hide();
    	 $('#spacing').hide();
    	    $('#imgDiv').append('<img id="theImg" src="WordCloud/Cloud.png"  />');
    }, 5000);
    
   
		
		/* $("#imgCloud").attr('src','WordCloud/Cloud.png?time='+d.getTime());*/

		/*$("#imgDiv").load(location.href + " #imgDiv");*/
    	});
	var d = new Date();
	/*$("#imgCloud").attr('src','WordCloud/Cloud.png?time='+d.getTime());*/
	/* $('#imgDiv').prepend('<img id="theImg" src="WordCloud/Cloud.png"  />');*/
	
	/*$(".imgDiv").delay(1000).fadeIn(2000);*/
	});
	

	$(function() { 
		  
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
	
	