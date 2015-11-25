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
        this.categoriesAdd = $('<div id="categoryAddButton" class="item" style="order: -1;">' +
            '<img src="images/folder_add.png" alt="pdf">' +
            '<span class="item_title">add category</span>' +
            '</div>');
        $( "#progressbar" ).progressbar({
            value: false,
            disabled:true
        });
        $("#serverReplyDialog").dialog({
           autoOpen: false,
           buttons: [
               {
                   text: "OK",
                   icons: {
                       primary: "ui-icon-check"
                   },
                   click: function() {
                       $( this ).dialog( "close" );
                   }
               }
           ]
        });
        $( "#categoryDialog" ).dialog({
            autoOpen: false,
            buttons: [
                {
                    text: "Add",
                    icons: {
                        primary: "ui-icon-check"
                    },
                    click: function() {
                        var Category = $("#categoryAddInput").val();
                        $( ".selector" ).progressbar( "option", "disabled", false );
                        $.ajax({
                            url: 'http://pdfarchive-wfiisaw.rhcloud.com/UploadCategory',  //Server script to process data
                            type: 'GET',
                            //Options to tell jQuery not to process data or worry about content-type.
                            cache: false,
                            processData: false,
                            beforeSend: function (request)
                            {
                                request.setRequestHeader("Category",Category);
                            }
                        }).done(function(reply) {
                            $("#serverReplyDialog").text(reply).promise().done(
                                function() {
                                    $( ".selector" ).progressbar( "option", "disabled", true );
                                    $("#serverReplyDialog").dialog( "open" );
                                }
                            );
                        }).always();
                    }
                },
                {
                    text: "Close",
                    icons: {
                        primary: "ui-icon-close"
                    },
                    click: function() {
                        $( this ).dialog( "close" );
                    }
                }
            ]
        });
        this.categoriesAdd.click(function (){
            $( "#categoryDialog" ).dialog( "open" );
        });

    },
    initContentMenu: function (content) {
        var categories = [];
        var tags= [];
        var titles = [];
        var ci = 0;
        var tagi = 0;
        var titlei = 0;
        var cm = this;
        this.menuDiv = content;
        this.allFilesMenu = $("<div id='files_content_menu_inner'>" +
                "<div><span class='files_content_menu_item_title'> searchByTag</span>+" +
                    "<input type='text' id='searchInputTag'>"+
                "</div>"+
                "<div><span class='files_content_menu_item_title'> searchByTitle</span>" +
                    "<input type='text' id='searchInputTitle'>"+
                "</div>"+
                "<div><span class='files_content_menu_item_title'> searchByCategory</span>" +
                    "<input type='text' id='searchInputCategory'>"+
                "</div>"+
                "<div id='lookup'><span class='files_content_menu_item_title' >Szukaj</span>"+
                    "<img src='images/ok.png' alt='pdf'>"+
                "</div>"+
            "</div>");
        this.allFilesMenu.find('#lookup').click(function(){
        	cm.showAllFiles();
        });
        $.ajax({
            url: 'http://pdfarchive-wfiisaw.rhcloud.com/CategoriesServlet',  //Server script to process data
            type: 'GET',
            //Ajax events
            cache: false,
            processData: false
        }).done(function(xml) {
            var root = $(xml).find('category')
            root.each(function () {
                var val = $(this).text();
                if (val.trim())
                categories[ci++] = val;
            });
            cm.allFilesMenu.find("#searchInputCategory").autocomplete({
                source: categories
            });
        });
        $.ajax({
            url: 'http://pdfarchive-wfiisaw.rhcloud.com/TagsServlet',  //Server script to process data
            type: 'GET',
            //Ajax events
            cache: false,
            processData: false
        }).done(function(xml) {
            var root = $(xml).find('tag')
            root.each(function () {
                var val = $(this).text();
                if (val.trim())
                    tags[tagi++] = val;
            });
            cm.allFilesMenu.find("#searchInputTag").autocomplete({
                source: tags
            });
        });
        $.ajax({
            url: 'http://pdfarchive-wfiisaw.rhcloud.com/TitlesServlet',  //Server script to process data
            type: 'GET',
            //Ajax events
            cache: false,
            processData: false
        }).done(function(xml) {
            var root = $(xml).find('title')
            root.each(function () {
                var val = $(this).text();
                if (val.trim())
                    titles[titlei++] = val;
            });
            cm.allFilesMenu.find("#searchInputTitle").autocomplete({
                source: titles
            });
        });

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
                //if(!cm.categoriesAddAttached)
                cm.categoriesAdd.appendTo(cm.categories);
                cm.categoriesAddAttached=true;
                cm.categories.appendTo(cm.mainDiv);
                cm.outerDiv.fadeIn(200);
                cm.currentItem = cm.categories;
            })
        });
    }
};
