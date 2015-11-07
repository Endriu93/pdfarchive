function initFilesManager()
{	
	ContentManager.initContentManager($("#files_content_inner"));
	
	$(".files_nav_option").click(function(){
		NavManager.HLight(this);
		ContentManager.showAllFiles();
	});
};

//object managing left action bar in your_files page
var NavManager = {
	HLighted: undefined,
	HLight: function(element){
		if(this.HLighted!==element)
			{
			this.UnHLightAll();
			var il = $(element);
			il.css("background-image","linear-gradient(#dd0000,#990000)");
			this.HLighted = element;
			}
		
	},
	UnHLightAll: function(){
		$(".files_nav_option").css("background-image","linear-gradient(#dddddd,#dddddd,#dddddd)");
		this.HLighted = undefined;
	} 
};

//object managing right content area 
var ContentManager = {
		mainDiv: undefined,
		allFiles: undefined,
		allFilesCreated: false,
		lastAdded: undefined,
		searchByTag: undefined,
		searchByCategory: undefined,
		currentItem: undefined,
		initContentManager: function(content){
			this.mainDiv = content;
			//this.initAllFiles();
		},
		initAllFiles: function(){
			var container_start = '<div class="container">';
			var content = [];
			for(var i=0; i<3; i++)
				{
				content[i] = '<div class="item">'+
					'<img src="images/pdf.png" alt="pdf">'+ 
					'<span>something</span>'+
					'</div>';
				}
			var container_end = '</div>';
			var result = container_start+content.join("\n")+container_end;
			this.allFiles = $(result);
			this.allFilesCreated = true;
		},
		initLastAdded: function(){
			this.lastAdded = $("<div></div>");
			this.lastAdded.load("html/contents/AllFiles.html");
		},
		initSearchByTag: function(){
			this.searchByTag = $("<div></div>");
			this.searchByTag.load("html/contents/AllFiles.html");
		},
		initSearchByCategory: function(){
			this.searchByCategory = $("<div></div>");
			this.searchByCategory.load("html/contents/AllFiles.html");
		},
		showAllFiles: function(){
			this.initAllFiles();
			this.mainDiv.fadeOut(200);
			var cm = this;
			this.mainDiv.promise().done(function(){
				if(cm.currentItem) cm.currentItem.detach();
				//if(cm.allFiles==undefined)
				//cm.initAllFiles();
				cm.allFiles.appendTo(cm.mainDiv);
				cm.mainDiv.fadeIn(200);
				cm.currentItem = cm.allFiles; 
			});
		},
		showLastAdded: function(){
			
		},
		showSearchByTag: function() {
			
		},
		showSearchByCategory: function(){
			
		}
};
