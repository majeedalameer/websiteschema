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

        <link rel="stylesheet" type="text/css" href="/resources/css/Ext.ux.form.LovCombo.css">
        <style type="text/css"></style>
        <script type="text/javascript" src="/js/packages.js"></script>
        <script type="text/javascript" src="/js/Ext.ux.form.LovCombo.js"></script>
        <script type="text/javascript" src="/js/Ext.ux.ThemeCombo.js"></script>
        <script type="text/javascript" src="/js/dwrproxy.js"></script>
        <script type="text/javascript" src="/dwr/engine.js"></script>
        <script type="text/javascript" src="/dwr/interface/DataChannelService.js"></script>
    </head>

    <body>

        <div id="gridpanel"></div>

        <script type="text/javascript">
            var start = 0;
            var pageSize = 10;
            Ext.onReady(function(){

                var proxy = new Ext.data.DWRProxy(DataChannelService.getChannels, true);

                var recordType = new Ext.data.Record.create([
                                    {
                                        name : 'chl_id',
                                        type : 'long'
                                    },
                                    {
                                        name : 'name',
                                        type : 'string'
                                    },
                                    {
                                        name : 'notes',
                                        type : 'string'
                                    },
                                    {
                                        name : 'url',
                                        type : 'string'
                                    },
                                    {
                                        name : 'task_type',
                                        type : 'string'
                                    },
                                    {
                                        name : 's_type',
                                        type : 'string'
                                    },
                                    {
                                        name : 'source_info',
                                        type : 'string'
                                    },
                                    {
                                        name : 'schedule_info',
                                        type : 'string'
                                    },
                                    {
                                        name : 'config_info',
                                        type : 'string'
                                    }
                                ]);
                var store=new Ext.data.Store({
                    proxy : proxy,
                    reader : new Ext.data.ListRangeReader(
                                {
                                    id : 'chl_id',
                                    totalProperty : 'totalSize'
                                }, recordType
                             )

                });

                //alert(store);


                //store.setDefaultSort('title', 'desc');
//                store.on('beforeload', function(thiz, options) {
//                    Ext.apply(thiz.baseParams, {
//                        json: Ext.util.JSON.encode({
//                            "SEC_CODE": Ext.getCmp('QUERY_SEC_CODE').getValue(),
//                            "SHORT": Ext.getCmp('QUERY_SHORT_NAME').getValue(),
//                            "SORT_NAME" : Ext.getCmp('SORT_NAME').getValue()
//                        })
//                    });
//                });


                // the column model has information about grid columns
                // dataIndex maps the column to the specific data field in
                // the data store
                //var nm = new Ext.grid.RowNumberer();
                var taskTypeData = new Ext.data.SimpleStore(
                                    {
                                        fields :['name','value'],
                                        data:[
                                            ['仅采集',1],
                                            ['仅抽取',2],
                                            ['既采集又抽取',3]
                                        ]
                                });

                var slaveTypeData = new Ext.data.SimpleStore(
                                    {
                                        fields :['name','value'],
                                        data:[
                                            ['CRAWLER',1],
                                            ['WRAPPER',2]
                                        ]
                                });


                var fm = Ext.form;
                var sm = new Ext.grid.CheckboxSelectionModel();  // add checkbox column
                var cm = new Ext.grid.ColumnModel([
                    //nm,
                    sm,
                    {
                        header: '栏目ID',
                        dataIndex: 'chl_id',
                        width: 100
                    },
                    {
                        header: '栏目名称',
                        dataIndex: 'name',
                        width: 200,
                        editor: new fm.TextField({
                                allowBlank: false
                                })
                    },
                    {
                        header: '栏目信息',
                        dataIndex: 'notes',
                        width: 200,
                        editor: new fm.TextField({
                                allowBlank: false
                                })
                    },
                    {
                        header: 'URL',
                        dataIndex: 'url',
                        width: 100,
                        editor: new fm.TextField({
                                allowBlank: false
                                })
                    },
                    {
                        header: '任务类型',
                        dataIndex: 'task_type',
                        width: 100,
                        editor: new fm.ComboBox({
                                    store : taskTypeData,
//                                    typeAhead: true,
                                    triggerAction: 'all',
                                    allowBlank: false,
                                    forceSelection: true,
                                    mode: 'local',
                                    //listClass: 'x-combo-list-small',
                                    displayField:'name',
                                    valueField:'value'
                                    
                                }),
                        renderer: function(value,metadata,record){
                            var index = taskTypeData.find('value',value);
                            if(index!=-1){
                                return taskTypeData.getAt(index).data.name;
                            }
                            return value;
                        }
                    },
                    {
                        header: '从节点类型',
                        dataIndex: 's_type',
                        width: 100,
                        editor: new fm.ComboBox({
                                    store : slaveTypeData,
//                                    typeAhead: true,
                                    triggerAction: 'all',
                                    allowBlank: false,
                                    forceSelection: true,
                                    mode: 'local',
                                    //listClass: 'x-combo-list-small',
                                    displayField:'name',
                                    valueField:'value'

                                }),
                        renderer: function(value,metadata,record){
                            var index = slaveTypeData.find('value',value);
                            if(index!=-1){
                                return slaveTypeData.getAt(index).data.name;
                            }
                            return value;
                        }
                    },
                    {
                        header: 'URL来源',
                        dataIndex: 'source_info',
                        width: 100,
                        editor: new fm.TextField({
                                allowBlank: false
                                })
                    },
                    {
                        header: '周期',
                        dataIndex: 'schedule_info',
                        width: 100,
                        editor: new fm.TextField({
                                allowBlank: false
                                })
                    },
                    {
                        header: '配置信息',
                        dataIndex: 'config_info',
                        width: 100,
                        editor: new fm.TextField({
                                allowBlank: false
                                })
                    },
                    {
                        header: ' 操作',
                        width: 100,
                        xtype: 'actioncolumn',
                        items: [
                            {
                                icon   : 'images/delete.gif',  // Use a URL in the icon config
                                tooltip: 'Sell stock',
                                handler: function(grid, rowIndex, colIndex) {
                                    var deleteV = function(){
                                        var rec = store.getAt(rowIndex);
                                        DataChannelService.deleteDataChannel(rec.get("chl_id"));
                                        store.load({params : {
                                            start : start,
                                            limit : pageSize
                                        }});
                                    }


                                    Ext.MessageBox.confirm('提示','您确实要删除选定的记录吗？', deleteV);
                                    
                                    
                                }
                            },
                            {
                                icon   : 'images/add.gif',  // Use a URL in the icon config
                                tooltip: 'Sell stock',
                                handler: function(grid, rowIndex, colIndex) {
                                    var insertV = function(){
                                        
                                        var p = new recordType();
                                        DataChannelService.insertDataChannel(p.data);
                                        store.load({params : {
                                            start : start,
                                            limit : pageSize
                                        }});
                                        
                                    }

                                    Ext.MessageBox.confirm('提示','您确实要增加记录吗？', insertV);
                                    
                                }
                            },
                            {
                                icon   : 'images/accept.gif',  // Use a URL in the icon config
                                tooltip: 'Sell stock',
                                handler: function(grid, rowIndex, colIndex) {
                                    var updateV = function(){
                                        var rec = store.getAt(rowIndex);
                                        DataChannelService.updateDataChannel(rec.data);
                                        store.load({params : {
                                            start : start,
                                            limit : pageSize
                                        }});
                                    }

                                    Ext.MessageBox.confirm('提示','您确实要修改选定的记录吗？', updateV);
                                    }

                                    
                            }
                        ]
                    }

                ]);

                // by default columns are sortable
                cm.defaultSortable = false;

               // trigger the data store load
                store.load({params : {
					start : start,
					limit : pageSize
				}});


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
                        }, '-',
                        {
                            text: '刷新',
                            tooltip: '刷新记录',
                            iconCls: 'icon-delete',
                            handler: handleUpdate
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

                    DataChannelService.insertDataChannel(p.data);
                    store.load({params : {
                        start : start,
                        limit : pageSize
                    }});
                }


                function handleEdit(){
                    var mr = store.getModifiedRecords();                   
                    for(var i=0;i<mr.length;i++){
                        Ext.MessageBox.alert("是否要更改" + mr[i].data["url"]+ "的配置");


                        DataChannelService.updateDataChannel(mr[i].data);
                    }
                    store.load({params : {
                        start : start,
                        limit : pageSize
                    }});
                    
                }

                //删除数据
                function handleDelete(){
                    var selections = grid.selModel.getSelections();                   
                    for (var i = 0,len = selections.length; i < len; i++) {
                        DataChannelService.deleteDataChannel(selections[i].id);
                    }
                }

                function handleUpdate(){
                    store.load({params : {
                        start : start,
                        limit : pageSize
                    }});
                }

            });
        </script>

    </body>
</html>
