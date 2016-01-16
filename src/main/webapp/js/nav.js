// init navigation 
function nav(val){

	//is called when logout is clicked.
if(val==1)
 	{
//		$("nav").hide(mAnimationSpeed, function() {
//			mainDiv.fadeOut(mAnimationSpeed);
//			mainDiv.promise().done(function() {
//				currentDiv.detach();
//				loginDiv.appendTo(mainDiv);
//				currentDiv = loginDiv;
//				mainDiv.fadeIn(mAnimationSpeed);
//			});
//		});
		location.reload();
	}
else if(val==2){	// is called when Login is valid.
	$("nav").show(mAnimationSpeed,function(){
		$("#navStart").trigger("click");
	});
}
else{

mainDiv = $("#main");
mainDiv.css("display","none");

uploadDiv = $("<div></div>");
uploadDiv.load("html/UploadFile.html");

filesDiv = $("<div class='height100'></div>");
filesDiv.load("html/FileManager.html");

loginDiv = $("<div></div>");
loginDiv.load("html/Login.html");

startDiv = $("<div></div>");
startDiv.load("html/Start.html");

loginDiv.appendTo(mainDiv);
mainDiv.fadeIn("slow");
$("nav").hide();
mainDiv.promise().done(loginInit);

currentDiv = loginDiv;
isInitialized = false;
isUploadInitialized = false;
isStartInitialized = false;


$("#navYourFiles").click(function(){
	mainDiv.fadeOut(mAnimationSpeed);
	mainDiv.promise().done(function(){
		currentDiv.detach();
		filesDiv.appendTo(mainDiv);
		currentDiv = filesDiv;
//		if(isInitialized==false)
			{
			initFilesManager();
			isInitialized = true;
			}
		mainDiv.fadeIn(mAnimationSpeed);
		uploadDiv.empty();
		uploadDiv.load("html/UploadFile.html");
	})
	
});
$("#navUploadFile").click(function(){
	mainDiv.fadeOut(mAnimationSpeed);
	mainDiv.promise().done(function(){
		currentDiv.detach();
		
		uploadDiv.appendTo(mainDiv);
		currentDiv = uploadDiv;
		loadCategories($(uploadDiv.find("#select_category")));
//		if(isUploadInitialized==false)
		{
			uploadInit();
			isUploadInitialized = true;
		}
		mainDiv.fadeIn(mAnimationSpeed);
		filesDiv.empty();
		filesDiv.load("html/FileManager.html");
	})
});

ShowDocumentsAfterUpload = function(){
	mainDiv.fadeOut(mAnimationSpeed);
	mainDiv.promise().done(function(){
		currentDiv.detach();
		filesDiv.appendTo(mainDiv);
		currentDiv = filesDiv;
//		if(isInitialized==false)
			{
			initFilesManager();
			isInitialized = true;
			}
		mainDiv.fadeIn(mAnimationSpeed).promise().done(function(){
			$("#filesDocuments").trigger("click");
			uploadDiv.empty();
			uploadDiv.load("html/UploadFile.html");
		})
		
	});
};
$("#navStart").click(function(){
	mainDiv.fadeOut(mAnimationSpeed);
	mainDiv.promise().done(function(){
		currentDiv.detach();
		startDiv.appendTo(mainDiv);
		currentDiv = startDiv;
		if(isStartInitialized==false)
		{
			startInit();
			isStartInitialized = true;
		}
		mainDiv.fadeIn(mAnimationSpeed);
	})
});
}
};