<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
            String path = request.getContextPath();
            String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>
<html>
    <head>
        <base href="<%=basePath%>">
        <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
        <title>宏爵财经资讯（北京）有限公司</title>

        <link rel="stylesheet" type="text/css" href="resources/css/Ext.ux.form.LovCombo.css">
        <style type="text/css"></style>
        <script type="text/javascript" src="js/packages.js"></script>
        <script type="text/javascript" src="js/Ext.ux.form.LovCombo.js"></script>
        <script type="text/javascript" src="js/Ext.ux.ThemeCombo.js"></script>
        <script type="text/javascript" src="js/dwrproxy.js"></script>
        <script type="text/javascript" src="dwr/engine.js"></script>
        <script type="text/javascript" src="dwr/interface/URLService.js"></script>
    </head>

    <body>

        <div id="gridpanel"></div>

        <script type="text/javascript">
            var start = 0;
            var pageSize = 10;
            Ext.onReady(function(){

                var proxy = new Ext.data.DWRProxy(URLService.getURLs, true);
                var recordType = new Ext.data.Record.create([
                                    {
                                        name : 'url_id',
                                        type : 'long'
                                    },
                                    {
                                        name : 'url',
                                        type : 'string'
                                    },
                                    {
                                        name : 'url_status',
                                        type : 'string'
                                    },
                                    {
                                        name : 'url_priority',
                                        type : 'string'
                                    },
                                    {
                                        name : 'url_type',
                                        type : 'string'
                                    },
                                    {
                                        name : 'url_depth',
                                        type : 'string'
                                    },
                                    {
                                        name : 'chl_name',
                                        type : 'string'
                                    },
                                    {
                                        name : 'create_time',
                                        type : 'string'
                                    }

                                ]);
                var store=new Ext.data.Store({
                    proxy : proxy,
                    reader : new Ext.data.ListRangeReader(
                                {
                                    id : 'url_id',
                                    totalProperty : 'totalSize'
                                }, recordType
                             ),
                             remoteSort: false

                });

         
                var url_status_store = new Ext.data.SimpleStore(
                                    {
                                        fields :['name','value'],
                                        data:[
                                            ['初始化',1],
                                            ['已分配',2],
                                            ['已完成',3],
                                            ['有异常',4]
                                        ]
                                });

                var url_priority_store = new Ext.data.SimpleStore(
                                    {
                                        fields :['name','value'],
                                        data:[
                                            ['底',1],
                                            ['中',2],
                                            ['高',3]
                                        ]
                                });

                var url_type_store = new Ext.data.SimpleStore(
                                    {
                                        fields :['name','value'],
                                        data:[
                                            ['栏目链接页',1],
                                            ['资讯内容页',2],
                                            ['数据内容页',3],
                                            ['栏目列表页',4],
                                            ['未识别类型',5]
                                        ]
                                });

                // the column model has information about grid columns
                // dataIndex maps the column to the specific data field in
                // the data store
                //var nm = new Ext.grid.RowNumberer();
                var fm = Ext.form;
                var sm = new Ext.grid.CheckboxSelectionModel();  // add checkbox column
                var cm = new Ext.grid.ColumnModel([
                    //nm,
                    sm,
                    {
                        header: 'URL_ID',
                        dataIndex: 'url_id',
                        width: 100
                    },
                    {
                        header: 'url',
                        dataIndex: 'url',
                        width: 200,
                        editor: new fm.TextField({
                                allowBlank: false
                                })
                    },
                    {
                        header: 'URL状态',
                        dataIndex: 'url_status',
                        width: 100,
                        editor: new fm.ComboBox({
                                    store : url_status_store,
                                    triggerAction: 'all',
                                    allowBlank: false,
                                    forceSelection: true,
                                    mode: 'local',
                                    displayField:'name',
                                    valueField:'value'

                                }),
                        renderer: function(value,metadata,record){
                            var index = url_status_store.find('value',value);
                            if(index!=-1){
                                return url_status_store.getAt(index).data.name;
                            }
                            return value;
                        }
                    },
                    {
                        header: 'URL优先级',
                        dataIndex: 'url_priority',
                        width: 100,
                        editor: new fm.ComboBox({
                                    store : url_priority_store,
                                    triggerAction: 'all',
                                    allowBlank: false,
                                    forceSelection: true,
                                    mode: 'local',
                                    displayField:'name',
                                    valueField:'value'

                                }),
                        renderer: function(value,metadata,record){
                            var index = url_priority_store.find('value',value);
                            if(index!=-1){
                                return url_priority_store.getAt(index).data.name;
                            }
                            return value;
                        }
                    },
                    {
                        header: 'URL类型',
                        dataIndex: 'url_type',
                        width: 100,
                        editor: new fm.ComboBox({
                                    store : url_type_store,
                                    triggerAction: 'all',
                                    allowBlank: false,
                                    forceSelection: true,
                                    mode: 'local',
                                    displayField:'name',
                                    valueField:'value'

                                }),
                        renderer: function(value,metadata,record){
                            var index = url_type_store.find('value',value);
                            if(index!=-1){
                                return url_type_store.getAt(index).data.name;
                            }
                            return value;
                        }
                    },
                    {
                        header: 'URL深度',
                        dataIndex: 'url_depth',
                        width: 100
                    },
                    {
                        header: '栏目',
                        dataIndex: 'chl_name',
                        width: 100
                    },
                    {
                        header: '创建时间',
                        dataIndex: 'create_time',
                        width: 100
                    }
                ]);

                // by default columns are sortable
                cm.defaultSortable = false;

               // trigger the data store load
                store.load({params : {
					start : start,
					limit : pageSize
				},
                            arg : []});

                
                var grid = new Ext.grid.EditorGridPanel({
                    //el:'topic-grid',
                    renderTo: 'gridpanel',
                    width: '100%',
                    height: 530,
                    clicksToEdit:1,
                    autoScroll: true,
                    //title: '分页和排序列表',
                    store: store,
                    trackMouseOver: false,
                    loadMask: true,
                    enableHdMenu: false,
                    sm: sm,
                    cm: cm,
                    
                    // inline toolbars
                    tbar: [ {
                            text: '新建',
                            tooltip: '新建记录',
                            iconCls: 'icon-add',
                            handler: handleAdd
                        }, '-',
                        {
                            text: '修改',
                            tooltip: '修改记录',
                            iconCls: 'icon-edit',
                            handler: handleEdit
                        }, '-',
                        {
                            text: '删除',
                            tooltip: '删除记录',
                            iconCls: 'icon-delete',
                            handler: handleDelete
                        }
                    ],
                    bbar: new Ext.PagingToolbar({
                        pageSize: 20,
                        store: store,
                        displayInfo: true
                    })
                });

                // render it
                grid.render();

                
              
                function handleAdd(){
                    var p = new recordType();
                    grid.stopEditing();
                    store.insert(0, p);
                    grid.startEditing(0, 0);
                    URLService.insertURL(p);
                }


                function handleEdit(){

                    var mr = store.getModifiedRecords();
                    for(var i=0;i<mr.length;i++){
                        Ext.MessageBox.alert("是否要更改" + mr[i].data["url"]+ "的配置");


                        URLService.updateURL(mr[i].data);
                    }
                    
                }

                //删除数据
                function handleDelete(){
//                    var selections = grid.selModel.getSelections();
//                    for (var i = 0,len = selections.length; i < len; i++) {
//                        alert(selections[i].id);
//                    }
                }

            });
        </script>

    </body>
</html>
