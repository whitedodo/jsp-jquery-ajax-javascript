<!doctype html>
<html lang="en">
<head>
	<meta charset="utf-8">
	<title>jQuery.post demo</title>
	<script src="js/jquery-3.5.1.js"></script>

</head>
<body>
 
<form action="board/insert.do" id="searchForm">
  <input type="text" name="s" placeholder="Search...">
  <input type="submit" value="Search">
</form>
<div id="result"></div>

<script type="text/javascript">
// Attach a submit handler to the form
$( "#searchForm" ).submit(function( event ) {
 
  	// Stop form from submitting normally
  	event.preventDefault();
 
	// Get some values from elements on the page:
  	var $form = $( this ),
    	term = $form.find( "input[name='s']" ).val(),
    	url = $form.attr( "action" );
 
  	// Send the data using post
  	var posting = $.post( url, { s: term } );
 
  	// Put the results in a div
  	posting.done(function( data ) {
  	  	alert( "Data Loaded: " + data );
    	var content = data ;
    	$( "#result" ).html( content );
    	
  	});
  	
});

</script>
<!-- the result of the search will be rendered inside this div --> 
 
</body>
</html>