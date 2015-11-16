function initFilesManager() {
    ContentManager.initContentManager($("#files_content_inner"));

    $(".files_nav_option").click(function () {
        NavManager.HLight(this);
        ContentManager.showAllFiles();
    });
};

//object managing left action bar in your_files page
var NavManager = {
    HLighted: undefined,
    HLight: function (element) {
        if (this.HLighted !== element) {
            this.UnHLightAll();
            var il = $(element);
            il.css("background-image", "linear-gradient(#dd0000,#990000)");
            this.HLighted = element;
        }

    },
    UnHLightAll: function () {
        $(".files_nav_option").css("background-image", "linear-gradient(#dddddd,#dddddd,#dddddd)");
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
    initContentManager: function (content) {
        this.mainDiv = content;
        //this.initAllFiles();
    },
    initAllFiles: function () {
        var ref = this;
        $.ajax({
            url: 'http://pdfarchive-wfiisaw.rhcloud.com/AllFilesServlet',  //Server script to process data
            type: 'GET',
            //Options to tell jQuery not to process data or worry about content-type.
            cache: false,
            processData: false
        }).done(function (xml) {
            var container_start = '<div class="container">';
            var content = [];
            var i = 0;
            var root = $(xml).find('title');
            root.each(function () {
                var val = $(this).text();
                content[i++] = '<div class="item">' +
                    '<img src="images/pdf.png" alt="pdf">' +
                    '<span class="item_title">' + val + '</span>' +
                    '</div>';
            })
            var container_end = '</div>';
            var result = container_start + content.join("\n") + container_end;
            ref.allFiles = $(result);
            ref.allFilesCreated = true;
        }).fail(function () {
            var container_start = '<div class="container">';
            var content = [];
            content[0] = '<div class="item">' +
                '<img src="images/pdf.png" alt="pdf">' +
                '<span>' + 'error' + '</span>' +
                '</div>';
            var container_end = '</div>';
            var result = container_start + content.join("\n") + container_end;
            ref.allFiles = $(result);
            ref.allFilesCreated = true;
        });

    },
    initLastAdded: function () {
        this.lastAdded = $("<div></div>");
        this.lastAdded.load("html/contents/AllFiles.html");
    },
    initSearchByTag: function () {
        this.searchByTag = $("<div></div>");
        this.searchByTag.load("html/contents/AllFiles.html");
    },
    initSearchByCategory: function () {
        this.searchByCategory = $("<div></div>");
        this.searchByCategory.load("html/contents/AllFiles.html");
    },
    showAllFiles: function () {
        //this.initAllFiles();
        var ref = this;
        $.ajax({
            url: 'http://pdfarchive-wfiisaw.rhcloud.com/AllFilesServlet',  //Server script to process data
            type: 'GET',
            //Options to tell jQuery not to process data or worry about content-type.
            cache: false,
            processData: false
        }).done(function (xml) {
            var container_start = '<div class="container">';
            var content = [];
            var i = 0;
            var root = $(xml).find('Title');
            root.each(function () {
                var val = $(this).text();
                content[i++] = '<div class="item">' +
                    '<img src="images/pdf.png" alt="pdf">' +
                    '<span class="item_title">' + val + '</span>' +
                    '</div>';
            });
            var container_end = '</div>';
            var result = container_start + content.join("\n") + container_end;
            ref.allFiles = $(result);
            ref.allFilesCreated = true;
        }).fail(function () {
            var container_start = '<div class="container">';
            var content = [];
            content[0] = '<div class="item">' +
                '<img src="images/pdf.png" alt="pdf">' +
                '<span>' + 'error' + '</span>' +
                '</div>';
            var container_end = '</div>';
            var result = container_start + content.join("\n") + container_end;
            ref.allFiles = $(result);
            ref.allFilesCreated = true;
        }).always(function () {
            ref.mainDiv.fadeOut(200);
            var cm = ref;
            ref.mainDiv.promise().done(function () {
                if (cm.currentItem) cm.currentItem.detach();
                //if(cm.allFiles==undefined)
                //cm.initAllFiles();
                cm.allFiles.appendTo(cm.mainDiv);
                cm.mainDiv.fadeIn(200);
                cm.currentItem = cm.allFiles;
            })
        });
    },
    showLastAdded: function () {

    },
    showSearchByTag: function () {

    },
    showSearchByCategory: function () {

    }
};
