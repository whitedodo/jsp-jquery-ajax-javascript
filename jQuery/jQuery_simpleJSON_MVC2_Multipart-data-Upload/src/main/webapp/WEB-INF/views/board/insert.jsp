<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
	<meta charset="utf-8">
	<title>jQuery.post demo</title>
	<script src="../js/jquery-3.5.1.js"></script>
<script>
$(document).ready(function(){

	// 1. Ajax - Post 결과 출력
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
	    	$( "#result1" ).html( content );
	    	
		});
	// Attach a submit handler to the form
	});
	
	// 2. Ajax 버튼(수작업 쿼리 - 보내기)
	$('#check').click(function(){
		
   		alert('특정 파라메터 보내기\n------------');
		var query = {username:'한글', password:1234, enabled:1};
   		
   		$.ajax({
   			url : "list_json.do",
   			type : "POST",
   			dataType : "json",
   			data : jQuery.param(query),
   			success : function(data) {
   				$.each(data, function(key, value) { //  { logList:[{}], command:{} } 이런구조임

   			   		//alert('성공');
   					if (key == "list") {
   	   					
   						for (var i = 0; i < value.length; i++) {
   	   						
   							// alert(value[i].username);
   							
   						}
   						
   					} else if (key == "command") {
   	   					
   						$('#result2').html(value);
   						
   					}else if(key=="paramUser"){

						// ArrayList로 받아서 처리함.
   						alert(value[0].username + "/" + value.username);

						// 인식 여부 (ArrayList로 안 하면, 알수없는 객체로 인식함)
						/*
   						for (var i = 0; i < value.length; i++) {
   							alert(i + "/" + value[i].username);
   						}
   						*/
   	   				} // end of if
   					
   				});
   				
   			},
   			error : function(msg) {
   				alert("error" + msg);
   			}
   		});
   		
	});

	// 4. 다중 업로드 기능
	$(function(){
 
	    $('#uploadBtn').on('click', function(){
	        uploadFile();
	    });
	 
	});
 
	function uploadFile(){
	    
	    var form = $('#uploadForm')[0];
	    var formData = new FormData(form);
	 
	    $.ajax({
	        url : 'upload.do',
	        type : 'POST',
	        data : formData,
	        contentType : false,
	        processData : false,
	    	success : function(data) {
				alert('성공');
				alert(data);
	    	},
   			error : function(msg) {
   				alert("error" + msg);
   			}
    		    
	    }).done(function(data){
	        callback(data);
	    });
	}
	
});
	
</script>
</head>
<body>
 
 <h3>Ajax - 전송</h3>
<form action="write_result.do" id="searchForm">
  <input type="text" name="s" placeholder="Search...">
  <input type="submit" value="Search">
</form>
<div id="result1"></div>

<h3>JSON 전송 확인(jQuery 미적용)</h3>
<form action="list_json.do" method="POST">
  <input type="text" name="keyword" placeholder="Search...">
  <input type="submit" value="Search">
</form>

<h3>JSON 가져오기(jQuery 적용)</h3>
<label for="userid">USER ID</label>
<input type="text" id="userid">
<button id="check">버튼 누르기</button>
<p id="result2"></p>

<h3>Ajax 기반 - 다중 업로드</h3>
<form id="uploadForm" enctype="multipart/form-data">
	<input type="hidden" name="token" value="2">
	<input type="text" name="usrID" size="10">
	<input type="file" name="uploadFile" multiple>
    <button type="button" id="uploadBtn">Save</button>
</form>	
 
</body>
</html>