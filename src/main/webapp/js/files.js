function initFilesManager() {
    ContentManager.initContentManager($("#files_content_inner"));
    ContentManager.initContentMenu($("#files_content_menu"));
    ContentManager.initContentOuter($("#files_content_outer"));


    $("#filesDocuments").click(function () {
        NavManager.HLight(this);
        ContentManager.showAllFiles();
    });

    $("#filesCategories").click(function () {
        NavManager.HLight(this);
        ContentManager.showAllCategories();
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
    menuDiv: undefined,
    outerDiv: undefined,
    allFiles: undefined,
    allFilesMenu: undefined,
    allFilesCreated: false,
    lastAdded: undefined,
    searchByTag: undefined,
    searchByCategory: undefined,
    currentItem: undefined,
    currentMenu: undefined,
    menu: undefined,
    categories: undefined,
    categoriesAdd: undefined,
    categoriesAddAttached: undefined,
    isMenuAttached: false,
    initContentManager: function (content) {
        this.mainDiv = content;
        this.categoriesAdd = $('<div class="item" style="order: -1;">' +
            '<img src="images/folder_add.png" alt="pdf">' +
            '<span class="item_title">add category</span>' +
            '</div>');

    },
    initContentMenu: function (content) {
        this.menuDiv = content;
        this.allFilesMenu = $("<div id='files_content_menu_inner'>" +
                "<div><span class='files_content_menu_item_title'> searchByTag</span></div>"+
                "<div><span class='files_content_menu_item_title'> searchByTitle</span></div>"+
                "<div><span class='files_content_menu_item_title'> searchByCategory</span></div>"+
                "<div><span class='files_content_menu_item_title'>Szukaj</span>"+
                    "<img src='images/ok.png' alt='pdf'>"+
                "</div>"+
            "</div>");
    },
    initContentOuter: function (content) {
        this.outerDiv = content;
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
                    '<img src="images/PDF_icon.png" alt="pdf">' +
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
                '<img src="images/PDF_icon.png" alt="pdf">' +
                '<span>' + 'error' + '</span>' +
                '</div>';
            var container_end = '</div>';
            var result = container_start + content.join("\n") + container_end;
            ref.allFiles = $(result);
            ref.allFilesCreated = true;
        }).always(function () {
            ref.outerDiv.fadeOut(200);
            var cm = ref;
            ref.outerDiv.promise().done(function () {
                if (cm.currentItem) cm.currentItem.detach();
                if(cm.categoriesAddAttached) cm.categoriesAdd.detach();
                cm.categoriesAddAttached =false;
                cm.allFiles.appendTo(cm.mainDiv);
                if(!cm.isMenuAttached)
                cm.allFilesMenu.appendTo(cm.menuDiv);

                cm.outerDiv.fadeIn(200);
                cm.currentItem = cm.allFiles;
                cm.currentMenu = cm.menu;
            })
        });
    },
    showLastAdded: function () {

    },
    showSearchByTag: function () {

    },
    showSearchByCategory: function () {

    },
    showAllCategories: function () {
        var ref = this;
        $.ajax({
            url: 'http://pdfarchive-wfiisaw.rhcloud.com/CategoriesServlet',  //Server script to process data
            type: 'GET',
            //Options to tell jQuery not to process data or worry about content-type.
            cache: false,
            processData: false
        }).done(function (xml) {
            var container_start = '<div class="container">';
            var content = [];
            var i = 0;
            var root = $(xml).find('category');
            root.each(function () {
                var val = $(this).text();
                content[i++] = '<div class="item">' +
                    '<img src="images/folder.png" alt="pdf">' +
                    '<span class="item_title">' + val + '</span>' +
                    '</div>';
            });
            var container_end = '</div>';
            var result = container_start + content.join("\n") + container_end;
            ref.categories = $(result);
            //ref.allFilesCreated = true;
        }).fail(function () {
            var container_start = '<div class="container">';
            var content = [];
            content[0] = '<div class="item">' +
                '<img src="images/folder.png" alt="pdf">' +
                '<span>' + 'error' + '</span>' +
                '</div>';
            var container_end = '</div>';
            var result = container_start + content.join("\n") + container_end;
            ref.categories = $(result);
        }).always(function () {
            ref.outerDiv.fadeOut(200);
            var cm = ref;
            ref.outerDiv.promise().done(function () {
                if (cm.currentItem) cm.currentItem.detach();
                if(cm.menuDiv) cm.allFilesMenu.detach();    // usuń menu
                cm.isMenuAttached=false;
                if(!cm.categoriesAddAttached)
                cm.categoriesAdd.appendTo(cm.categories);
                cm.categoriesAddAttached=true;
                cm.categories.appendTo(cm.mainDiv);
                cm.outerDiv.fadeIn(200);
                cm.currentItem = cm.categories;
            })
        });
    }
};
