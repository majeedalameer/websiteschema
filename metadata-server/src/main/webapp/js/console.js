Ext.BLANK_IMAGE_URL = 'resources/s.gif';

Console = {};

Console.actionData = [
       {
           text:"数据管理系统",id:"dh",title:"数据管理系统",cls:"cls",singleClickExpand:true,
           children:[
//                        {
//                            text:"网站管理",id:"dh.fetch",title:"网站管理",cls:"cls",singleClickExpand:true,
//                            children:[
//                            {
//                                text:"抓取配置",id:"dh.crawler",title:"抓取配置",cls:"cls",singleClickExpand:true,
//                                children:[
//                                    {href:"manager/channel.jsp",text:"栏目配置",id:"channel",title:"栏目配置",cls:"cls",iconCls:"icon-cls",leaf:true},
//                                    {href:"manager/url.jsp",text:"URL配置",id:"url",title:"URL配置",cls:"cls",iconCls:"icon-cls",leaf:true}
//                                ]
//                            }]
//                        },
//                        {
//                            text:"采集管理",id:"meta-maintain.task",title:"采集管理",cls:"cls",singleClickExpand:true,
//                            children:[
//                                {href:"metadata/ipoxg.jsp",text:"IPO新股",id:"ipoxg",title:"IPO新股 - 投资逻辑和主要产品",cls:"cls",iconCls:"icon-cls",leaf:true}
//                            ]
//                        },
                        {
                            text:"用户管理",id:"user-admin.task",title:"用户管理",cls:"cls",singleClickExpand:true,
                            children:[
                                {href:"views/metadata/user",text:"用户管理",id:"yhgl",title:"用户管理",cls:"cls",iconCls:"icon-cls",leaf:true}
                            ]
                        },
                        {
                            href:"views/personInfo", text:"个人信息",id:"personal-admin.task",title:"个人信息",cls:"cls",leaf:true
                        }
                    ]
       }
];

LeftPanel = function() {
    LeftPanel.superclass.constructor.call(this, {
        id:'main-tree',
        region:'west',
        split:true,
        header: false,
        width: 200,
        minSize: 175,
        maxSize: 500,
        collapsible: true,
        margins:'0 0 5 5',
        cmargins:'0 0 0 0',
        rootVisible:false,
        lines:false,
        autoScroll:true,
        animCollapse:false,
        animate: false,
        collapseMode:'mini',

        useArrows: true,
        enableDD: true,
        containerScroll: true,
        border: false,

        loader: new Ext.tree.TreeLoader({
        preloadChildren: true,
            clearOnLoad: false
        }),
        root: new Ext.tree.AsyncTreeNode({
            text:'Ext JS',
            id:'root',
            expanded:true,
            draggable: false,
            children:Console.actionData
         }),
/*
        listeners: {
            click: function(n) {
                Ext.Msg.alert('Navigation Tree Click', 'You clicked: "' + n.getPath() + '"');
            }
        },
*/
        collapseFirst:false
    });
    // no longer needed!
    //new Ext.tree.TreeSorter(this, {folderSort:true,leafAttr:'isClass'});

    this.getSelectionModel().on('beforeselect', function(sm, node){
        return !Ext.isEmpty(node.attributes.href);//node.isLeaf();
    });
};

Ext.extend(LeftPanel, Ext.tree.TreePanel, {
    initComponent: function(){
        Ext.apply(this, {
            tbar:[ ' ',
            //'系统导航',' ',' ',' ',' ',
            {
                iconCls: 'icon-expand-all',
                tooltip: '展开全部',
                handler: function(){ this.root.expand(true); },
                scope: this
            }, '-', {
                iconCls: 'icon-collapse-all',
                tooltip: '关闭全部',
                handler: function(){ this.root.collapse(true); },
                scope: this
            }]
        })
        LeftPanel.superclass.initComponent.call(this);
    },
    autoSelectPath : function(cls){
        if(cls){
            if(cls.indexOf("tab-")==0)
                cls = cls.substring(4);
            var parts = cls.split('.');
            var res = [];
            var pkg = [];
            for(var i = 0; i < parts.length-1; i++){
                pkg.push(parts[i]);
                res.push(pkg.join('.'));
            }
            res.push(cls);
            this.selectPath('/root/'+res.join('/'));
        }
    }
});

MainPanel = function(){

    MainPanel.superclass.constructor.call(this, {
        id:'main-body',
        region:'center',
        margins:'0 5 5 0',
        resizeTabs: true,
        minTabWidth: 135,
        tabWidth: 135,
        enableTabScroll: true,
        activeTab: 0,

        items: {
            id: 'tab-welcome',
            title: '帮助',
            cls: 'cls',
            autoLoad: {url: '/views/main_page', scope: this, script: true},
            iconCls: 'icon-docs',
            closable: false,
            autoScroll: true
        }
    });
};

Ext.extend(MainPanel, Ext.TabPanel, {

    initEvents : function(){
        MainPanel.superclass.initEvents.call(this);
        this.body.on('click', this.onClick, this);
    },

    onClick: function(e, target){

    },

    loadTabPage : function(href, id, title, cls, iconCls){
        var tabid = 'tab-' + id;
        var tab = this.getComponent(tabid);
        if(tab){
            this.setActiveTab(tab);
        }else{
            var p = this.add({
                closable: true,
                autoScroll: true,
                id: tabid,
                title: title,
                autoLoad: {url: href, scripts: true},
                cls: cls,
                iconCls: iconCls
            });
            this.setActiveTab(p);
        }
    },

    loadTabIframePage : function(href, id, title, cls, iconCls){
        var tabid = 'tab-' + id;
        var tab = this.getComponent(tabid);
        if(tab){
            this.setActiveTab(tab);
        }else{
            var p = this.add({
                closable: true,
                autoScroll: true,
                id: tabid,
                title: title,
                html: '<iframe id="'+id+'-iframe" src="'+href+'" frameborder="0" scrolling="auto" style="border:0px none; height:100%; width:100%;"></iframe>',
                cls : cls,
                iconCls: iconCls
            });
            this.setActiveTab(p);
        }
    }
});

//定义为全局变量便于其他页面操作
var leftPanel;
var mainPanel;

Ext.onReady(function(){

    Ext.QuickTips.init();

    leftPanel = new LeftPanel();
    mainPanel = new MainPanel();

    leftPanel.on('click', function(node, e){
         //if(node.isLeaf()){
         //修正非叶子节点无法点击
         if(!Ext.isEmpty(node.attributes.href)){
            e.stopEvent();
            mainPanel.loadTabIframePage(node.attributes.href, node.id, node.attributes.title, node.attributes.cls, node.attributes.iconCls);
         }
    });

    mainPanel.on('tabchange', function(tp, tab){
        leftPanel.autoSelectPath(tab.id); 
    });

    var viewport = new Ext.Viewport({
        layout: 'border',
        items:[ {
            cls: 'top',
//            height: 38,
            region:'north',
            xtype:'box',
            el:'top',
            border:false,
            margins: '0 0 0 0'
        }, leftPanel, mainPanel, {
            //cls: 'bottom',
            height: 31,
            region:'south',
            xtype:'box',
            el:'bottom',
            border:false,
            margins: '0 0 0 0'
        }]
    });

    leftPanel.expandPath('/root/dh');

    viewport.doLayout();

    setTimeout(function(){
        Ext.get('loading').remove();
        Ext.get('loading-mask').fadeOut({remove:true});
    }, 250);
});

Ext.Ajax.on('requestcomplete', function(ajax, xhr, o){
    if(typeof urchinTracker == 'function' && o && o.url){
        urchinTracker(o.url);
    }
});
