// init navigation 
function nav(){

var mainDiv = $("#main");
mainDiv.css("display","none");

var uploadDiv = $("<div></div>");
uploadDiv.load("html/UploadFile.html");

var filesDiv = $("<div></div>");
filesDiv.load("html/FileManager.html");

var loginDiv = $("<div></div>");
loginDiv.load("html/Login.html");

loginDiv.appendTo(mainDiv);
mainDiv.fadeIn("slow");
mainDiv.promise().done(loginInit);

var currentDiv = loginDiv;
var isInitialized = false;
var isUploadInitialized = false;

$("#navYourFiles").click(function(){
	mainDiv.fadeOut(200);
	mainDiv.promise().done(function(){
		currentDiv.detach();
		filesDiv.appendTo(mainDiv);
		currentDiv = filesDiv;
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
		currentDiv.detach();
		uploadDiv.appendTo(mainDiv);
		currentDiv = uploadDiv;
		if(isUploadInitialized==false)
		{
			uploadInit();
			isUploadInitialized = true;
		}
		mainDiv.fadeIn(200);
	})
});
};