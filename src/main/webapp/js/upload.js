
function uploadInit(){
			 var container = $(".extInCtn");
			 var ss = container.attr("id");
			 for(var i=0; i<container.length; i++)
				 {
				 var id = $(container.get(i)).attr("id");
				 var max = $(container.get(i)).attr("data-maxCount");
				 ItemCountChecker.init(id,max);
				 }
			 $('#fileToUpload').change(fileSelected);
			 var progres = $("progress");
			 progres.hide();
			 $('#upload_button').click(function(){
				    var Filename = $("#fileName").text();
				    var Description = $("#description").find(".tag").text();
				    var Category = $("#category").find(".tag").text();
				    var Tags = [];
				    var Index = $('#words').find(".tag").text();
				    var i=0;
				    $("#tags").find(".tag").each(function(){
				    	Tags[i++] = $(this).text();
				    });
				    var form = $('#form1')[0];
				    var formJ = $(form);
				    formJ.find("#UserID").val(mUser.getId().toString());
				    formJ.find("#Index").val(Index);
				    var formData = new FormData(form);
//				    formData.set('UserID',mUser.getId().toString());
//				    formData.set('Index',Index);
				    $.ajax({
				        url: 'http://pdfarchive-wfiisaw.rhcloud.com/UploadServlet',  //Server script to process data
				        type:'POST',
				        xhr: function() {  // Custom XMLHttpRequest
				            var myXhr = $.ajaxSettings.xhr();
				            if(myXhr.upload){ // Check if upload property exists
				                myXhr.upload.addEventListener('progress',uploadProgress, false); // For handling the progress of the upload
				            }
				            return myXhr;
				        },
				        beforeSend: function (request)
			            {
			                request.setRequestHeader("Filename",Filename);
			                request.setRequestHeader("Description",Description);
			                request.setRequestHeader("Category",Category);
			                request.setRequestHeader("Tags",Tags.join(":"));
			            },
				        //Ajax events
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
			 

			 $(".itemInput_accept").click(function(){
				 var id = $(this).closest(".extInCtn").attr("id");
				 if(!ItemCountChecker.available(id)) return false;
				 var value;
				 var select = $(this).parent().find("input");
				 if(select.length!=0) 
					 {
					 value = select.val();
					 }
				 else
					 {
					 select=$(this).parent().find("select");
					 value = select.val();
					 }
				 var closest = $(this).closest(".extInCtn");
				 var content="<div class=\"tag\">"+value
				 +"<img src=\"images/remove2.png\" data-toggle=\"tooltip\" title=\"remove this tag\" class=\"tag_inside\">"
				 +"</div>"
				 var tag = $(content);
				 var img = tag.find(".tag_inside");
				 img.click(function(){
					 var id = $($(this).closest(".extInCtn")).attr("id");
					 $(this).parent().remove(); ItemCountChecker.subtract(id)
					 });
				 img.hover(function(){$(this).attr("src","images/remove_red.png")},
			 				function(){$(this).attr("src","images/remove2.png")});
				 closest.find(".itemValue").append(tag);
				 ItemCountChecker.add(id);
				 });
			 	var categories = $("#category").find("select");
			 	loadCategories(categories);			 
}

function loadCategories(select){
	$.ajax({
        url: 'http://pdfarchive-wfiisaw.rhcloud.com/CategoriesServlet',  //Server script to process data
        type: 'POST',
        data: mUser.makeUserJson(),
        //Ajax events
        success: function(xml){
    		select.empty();

        	var root = $(xml).find('category')
        	root.each(function(){
        		var val = $(this).text();
        		if(val.trim())
        		select.append("<option>"+val+"</option>");
        	 });
        },
        error: function(xhr,errorStatus,errorThrown){window.alert(errorStatus+"::"+errorThrown);},
        //Options to tell jQuery not to process data or worry about content-type.
        cache: false
    });
	
}
 var ItemValidator =  {
		 items: new Object(),
		 images: new Object(),
		 valid: function(id)
		 {
			 this.items[id]=true;
 			 this.images[id].attr("src","images/ok.png");
		 },
 		 invalid: function(id)
 		 {
 			 this.items[id]=false;
 			 this.images[id].attr("src","images/error.png");
 		 },
		 init: function(id)
		 {
			this.items[id]=false;
			this.images[id]=$($("#"+id).find(".validationImage"));
		 }
 		 
 }
 var ItemCountChecker = {
		 count: new Object(),
		 max: new Object(),
		
 		init: function(id,maxa)
 		{
 			this.max[id] = parseInt(maxa);
 			this.count[id]=0;
 			ItemValidator.init(id);
 			ItemValidator.invalid(id)
 		},
		add: function(id)
		{
			this.count[id]++;
			if(this.count[id]>0) ItemValidator.valid(id);
		},
 		subtract: function(id)
 		{
 			if(this.count[id]>0)
 			 this.count[id]--;
 			if(this.count[id]==0) ItemValidator.invalid(id);
 		},
 		available: function(id)
 		{
 			if(this.max[id]==this.count[id])
 				return false;
 			else return true;
 		}	 
 }
 function checkCount(id){
	 
 }
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

       $("#fileName").html(file.name);
       $("#fileSize").html(fileSize);
       $("#fileType").html(file.type);

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
 function uploadComplete(xhr, ajaxOptions, thrownError) {
     /* This event is raised when the server send back a response */
     alert("Poprawnie dodano plik do archiwum");
     $("progress").hide();
     $("#progressNumber").html("");
   }

   function uploadFailed(xhr, ajaxOptions, thrownError) {
	   if(xhr.status==515)
		   alert("Dokument o podanej nazwie już istnieje w archiwum");
	   else if(xhr.status==516)
		   alert("Wystąpił błąd podczas dodawania dokumentu. Upewnij się czy dodawany plik jest w formacie PDF");
	   else
		   alert("There was an error attempting to upload the file."+xhr.status+" "+thrownError);
   }

   function uploadCanceled(evt) {
     alert("The upload has been canceled by the user or the browser dropped the connection.");
   }
	