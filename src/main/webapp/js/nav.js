// init navigation 
function nav(){

var mainDiv = $("#main");
mainDiv.css("display","none");

var uploadDiv = $("<div></div>");
uploadDiv.load("html/UploadFile.html");
uploadDiv.appendTo(mainDiv);
mainDiv.fadeIn("slow");
mainDiv.promise().done(uploadInit);

var filesDiv = $("<div></div>");
filesDiv.load("html/FileManager.html");

var isInitialized = false;

$("#navYourFiles").click(function(){
	mainDiv.fadeOut(200);
	mainDiv.promise().done(function(){
		uploadDiv.detach();
		filesDiv.appendTo(mainDiv);
		if(isInitialized==false)
			{
			initFilesManager();
			isInitialized = true;
			}
		mainDiv.fadeIn(200);
	})
	
});
$("#navUploadFile").click(function(){
	mainDiv.fadeOut(200);
	mainDiv.promise().done(function(){
		filesDiv.detach();
		uploadDiv.appendTo(mainDiv);
		mainDiv.fadeIn(200);
	})
});
};