$(document).ready(
		 function(){
			 window.console.log("line 10");
			 $('#fileToUpload').change(fileSelected);
			 window.console.log("line 17");
			 $("progress").hide();
			 $('#upload_button').click(function(){
				    var formData = new FormData($('form1')[0]);
				    $.ajax({
				        url: 'http://pdfarchive-wfiisaw.rhcloud.com/UploadServlet',  //Server script to process data
				        type: 'POST',
				        xhr: function() {  // Custom XMLHttpRequest
				            var myXhr = $.ajaxSettings.xhr();
				            if(myXhr.upload){ // Check if upload property exists
				                myXhr.upload.addEventListener('progress',uploadProgress, false); // For handling the progress of the upload
				            }
				            return myXhr;
				        },
				        //Ajax events
				       // beforeSend: beforeSendHandler,
				        success: uploadComplete,
				        error: uploadFailed,
				        // Form data
				        data: formData,
				        //Options to tell jQuery not to process data or worry about content-type.
				        cache: false,
				        contentType: false,
				        processData: false
				    });
				});                                                                                  
			 });
		 
 function progressHandlingFunction(e){
	    if(e.lengthComputable){
	        $('progress').attr({value:e.loaded,max:e.total});
	    }
 }
 function completeHandler(received,status) {alert("status: "+status+" received: "+received); $("#progress").hide();};
 function errorHandler() {alert("error Occured");$("#progress").hide();}
 function fileSelected() {
     var file = document.getElementById('fileToUpload').files[0];
     if (file) {
       var fileSize = 0;
       if (file.size > 1024 * 1024)
         fileSize = (Math.round(file.size * 100 / (1024 * 1024)) / 100).toString() + 'MB';
       else
         fileSize = (Math.round(file.size * 100 / 1024) / 100).toString() + 'KB';

       $("#fileName").html('Name: ' + file.name);
       $("#fileSize").html('Size: ' + fileSize);
       $("#fileType").html('Type: ' + file.type);

     }
   }
 function uploadProgress(evt) {
     if (evt.lengthComputable) {
       var percentComplete = Math.round(evt.loaded * 100 / evt.total);
       document.getElementById('progressNumber').innerHTML = percentComplete.toString() + '%';
       $('progress').show().attr({value:evt.loaded,max:evt.total});

     }
     else {
       document.getElementById('progressNumber').innerHTML = 'unable to compute';
     }
   }
 function uploadComplete(evt) {
     /* This event is raised when the server send back a response */
     alert(evt.target.responseText);
   }

   function uploadFailed(evt) {
     alert("There was an error attempting to upload the file.");
   }

   function uploadCanceled(evt) {
     alert("The upload has been canceled by the user or the browser dropped the connection.");
   }
	