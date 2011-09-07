<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
            String path = request.getContextPath();
            String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>
<html>
    <head>
        <base href="<%=basePath%>">
        <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
        <title>Websiteschema User Management</title>

        <link rel="stylesheet" type="text/css" href="resources/css/Ext.ux.form.LovCombo.css">
        <style type="text/css"></style>
        <script type="text/javascript" src="js/packages.js"></script>
        <script type="text/javascript" src="js/Ext.ux.form.LovCombo.js"></script>
        <script type="text/javascript" src="js/Ext.ux.ThemeCombo.js"></script>
        <script type="text/javascript" src="js/dwrproxy.js"></script>
        <script type="text/javascript" src="dwr/engine.js"></script>
        <script type="text/javascript" src="dwr/interface/UserService.js"></script>
    </head>

    <body>

        <div id="gridpanel"></div>

        <script type="text/javascript">
            var start = 0;
            var pageSize = 10;
            Ext.onReady(function(){

                var proxy = new Ext.data.DWRProxy(UserService.getUsers, true);
                var recordType = new Ext.data.Record.create([
                    {
                        name : 'userId',
                        type : 'long'
                    },
                    {
                        name : 'name',
                        type : 'string'
                    },
                    {
                        name : 'email',
                        type : 'string'
                    },
                    {
                        name : 'passwd',
                        type : 'string'
                    }
                ]);
                var store=new Ext.data.Store({
                    proxy : proxy,
                    reader : new Ext.data.ListRangeReader(
                    {
                        id : 'userId',
                        totalProperty : 'totalSize'
                    }, recordType
                ),
                    remoteSort: false

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
                        header: 'USER_ID',
                        dataIndex: 'userId',
                        width: 100
                    },
                    {
                        header: 'name',
                        dataIndex: 'name',
                        width: 200,
                        editor: new fm.TextField({
                            allowBlank: false
                        })
                    },
                    {
                        header: 'email',
                        dataIndex: 'email',
                        width: 100,
                        editor: new fm.TextField({
                            allowBlank: false
                        })
                    },
                    {
                        header: 'passwd',
                        dataIndex: 'passwd',
                        width: 100,
                        editor: new fm.TextField({
                            allowBlank: false
                        })
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
                    p.set("name","name");
                    p.set("passwd","passwd");
                    p.set("email","email@gmail.com");
                    store.insert(0, p);
                    grid.startEditing(0, 0);
                    UserService.insert(p.data);
                    store.reload();
                }


                function handleEdit(){

                    var mr = store.getModifiedRecords();
                    Ext.MessageBox.alert("是否要继续更改？");
                    for(var i=0;i<mr.length;i++){
                        UserService.update(mr[i].data);
                    }
                    store.reload();
                }

                //删除数据
                function handleDelete(){
                    var selections = grid.selModel.getSelections();
                    Ext.MessageBox.alert("是否要继续删除？");
                    for (var i = 0,len = selections.length; i < len; i++) {
                        UserService.deleteUser(selections[i].data);
                    }
                    store.reload();
                }

            });
        </script>

    </body>
</html>
