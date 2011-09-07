<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" errorPage="../../include/error.jsp"%>
<!------- CMS IMPORTS BEGIN ------------>

<!------- CMS IMPORTS END ------------>
<!--- 页面状态设定、登录校验、参数获取，都放在public_server.jsp中 --->
<%@include file="../../include/public_server.jsp"%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
        <title>宏爵财经资讯（北京）有限公司</title>

        <link rel="stylesheet" type="text/css" href="/resources/css/Ext.ux.form.LovCombo.css">
        <style type="text/css"></style>

        <script type="text/javascript" src="/js/packages.js"></script>
        <script type="text/javascript" src="/js/Ext.ux.form.LovCombo.js"></script>
        <script type="text/javascript" src="/js/Ext.ux.ThemeCombo.js"></script>
    </head>

    <body>

        <div id="gridpanel"></div>

        <script type="text/javascript">

            Ext.onReady(function(){

                // create the Data Store
                var store = new Ext.data.JsonStore({
                    totalProperty: 'total',
                    root: 'results',
                    idProperty: 'ID',
                    // turn on remote sorting
                    remoteSort: false,
                    fields: [
                        {name: 'L1', type: 'string'},
                        {name: 'L1K', type: 'string'},
                        {name: 'L1COMMENT', type: 'string'},
                        {name: 'L1LOG', type: 'string'},
                        {name: 'L2', type: 'string'},
                        {name: 'L2K', type: 'string'},
                        {name: 'L2COMMENT', type: 'string'},
                        {name: 'L2LOG', type: 'string'},
                        {name: 'L3', type: 'string'},
                        {name: 'L3K', type: 'string'},
                        {name: 'L3COMMENT', type: 'string'},
                        {name: 'L3LOG', type: 'string'}
                    ],
                    // load using script tags for cross domain, if the data in on the same domain as
                    // this page, an HttpProxy would be better
                    proxy: new Ext.data.HttpProxy({
                        url: '/business/doGetList.action?mapId=business'
                    })
                });
                //store.setDefaultSort('title', 'desc');
                store.on('beforeload', function(thiz, options) {
                    Ext.apply(thiz.baseParams, {
                        json: Ext.util.JSON.encode({
                            "L1": Ext.getCmp('QUERY_L1').getValue(),
                            "L2": Ext.getCmp('QUERY_L2').getValue(),
                            "L3": Ext.getCmp('QUERY_L3').getValue()
                        })
                    });
                });


                // the column model has information about grid columns
                // dataIndex maps the column to the specific data field in
                // the data store
                //var nm = new Ext.grid.RowNumberer();
                var sm = new Ext.grid.CheckboxSelectionModel();  // add checkbox column
                var cm = new Ext.grid.ColumnModel([
                    //nm,
                    sm,
                    {
                        header: '一级行业',
                        dataIndex: 'L1',
                        width: 100
                    },
                    {
                        header: '一级行业关键词库',
                        dataIndex: 'L1K',
                        width: 100
                    },
                    {
                        header: '一级行业知识备注',
                        dataIndex: 'L1COMMENT',
                        width: 50,
                        hidden: true
                    },
                    {
                        header: '一级行业维护备注',
                        dataIndex: 'L1LOG',
                        width: 50,
                        hidden: true
                    },
                    {
                        header: '二级行业',
                        dataIndex: 'L2',
                        width: 100
                    },
                    {
                        header: '二级行业关键词库',
                        dataIndex: 'L2K',
                        width: 50
                    },
                    {
                        header: '二级行业知识备注',
                        dataIndex: 'L2COMMENT',
                        width: 50,
                        hidden: true
                    },
                    {
                        header: '二级行业维护备注',
                        dataIndex: 'L2LOG',
                        width: 50,
                        hidden: true
                    },
                    {
                        header: '三级行业',
                        dataIndex: 'L3',
                        width: 80
                    },
                    {
                        header: '三级行业关键词库',
                        dataIndex: 'L3K',
                        width: 80
                    },
                    {
                        header: '三级行业知识备注',
                        dataIndex: 'L2COMMENT',
                        width: 50,
                        hidden: true
                    },
                    {
                        header: '三级行业维护备注',
                        dataIndex: 'L2LOG',
                        width: 50,
                        hidden: true
                    }
                ]);

                // by default columns are sortable
                cm.defaultSortable = true;

                handleClear = function(){
                    Ext.getCmp('QUERY_L1').setValue("");
                    Ext.getCmp('QUERY_L2').setValue("");
                    Ext.getCmp('QUERY_L3').setValue("");
                    store.removeAll();
                    store.load();
                }
                
                handleQuery = function(){
                    store.removeAll();
                    store.load();
                }

                var grid = new Ext.grid.GridPanel({
                    //el:'topic-grid',
                    renderTo: 'gridpanel',
                    width: '100%',
                    height: 530,
                    autoScroll: true,
                    //title: '分页和排序列表',
                    store: store,
                    trackMouseOver: false,
                    loadMask: true,
                    sm: sm,
                    cm: cm,
                    viewConfig: {
                        forceFit:true,
                        enableRowBody:true,
                        showPreview:true,
                        getRowClass : function(record, rowIndex, p, store){
                            return 'x-grid3-row-collapsed';
                        }
                    },
                    // inline toolbars
                    tbar: [{
                            text: '新建',
                            tooltip: '新建记录',
                            iconCls: 'icon-add',
                            handler: handleAdd
                        }, '-', {
                            text: '修改',
                            tooltip: '修改记录',
                            iconCls: 'icon-edit',
                            handler: handleEdit
                        }, '-', {
                            text: '删除',
                            tooltip: '删除记录',
                            iconCls: 'icon-delete',
                            handler: handleDelete
                        }, '->',
                        ' ', {
                            text: '缺省检索',
                            tooltip: '缺省检索',
                            iconCls: 'icon-query',
                            handler: handleClear
                        },
                        ' ', '一级行业', ' ',
                        {
                            xtype: 'textfield',
                            id: 'QUERY_L1',
                            initEvents : function(){
                                var keyPress = function(e) {
                                    if(e.getKey()==e.ENTER){
                                        handleQuery();
                                    }
                                };
                                this.el.on("keypress", keyPress, this);
                            }
                        },' ', '二级行业', ' ',
                        {
                            xtype: 'textfield',
                            id: 'QUERY_L2',
                            initEvents : function(){
                                var keyPress = function(e) {
                                    if(e.getKey()==e.ENTER){
                                        handleQuery();
                                    }
                                };
                                this.el.on("keypress", keyPress, this);
                            }
                        },' ', '三级行业', ' ',
                        {
                            xtype: 'textfield',
                            id: 'QUERY_L3',
                            initEvents : function(){
                                var keyPress = function(e) {
                                    if(e.getKey()==e.ENTER){
                                        handleQuery();
                                    }
                                };
                                this.el.on("keypress", keyPress, this);
                            }
                        }, ' ',
                        {
                            text: '检索',
                            iconCls: 'icon-query',
                            handler: handleQuery
                        }, ' '
                    ],
                    bbar: new Ext.PagingToolbar({
                        pageSize: 20,
                        store: store,
                        displayInfo: true
                    })
                });

                // render it
                grid.render();

                // trigger the data store load
                store.load({params:{start: 0, limit: 20}});

                var level1 = new Ext.data.Store({
                    reader: new Ext.data.JsonReader({
                        totalProperty: 'total',
                        root: 'results',
                        fields: [
                            {name: 'value', mapping: 'ID1', type: 'string'},
                            {name: 'display', mapping: 'L1', type: 'string'}
                        ]}
                ),
                    autoLoad: true,
                    proxy: new Ext.data.HttpProxy({
                        url: '/business/doGetList.action?mapId=getLevel1'
                    })
                });

                var level2 = new Ext.data.Store({
                    reader: new Ext.data.JsonReader({
                        totalProperty: 'total',
                        root: 'results',
                        fields: [
                            {name: 'value', mapping: 'ID2', type: 'string'},
                            {name: 'display', mapping: 'L2', type: 'string'}
                        ]}
                ),
                    autoLoad: false,
                    proxy: new Ext.data.HttpProxy({
                        url: '/business/doGetList.action?mapId=getLevel2'
                    })
                });

                var level3 = new Ext.data.Store({
                    reader: new Ext.data.JsonReader({
                        totalProperty: 'total',
                        root: 'results',
                        fields: [
                            {name: 'value', mapping: 'ID3', type: 'string'},
                            {name: 'display', mapping: 'L3', type: 'string'}
                        ]}
                ),
                    autoLoad: false,
                    proxy: new Ext.data.HttpProxy({
                        url: '/business/doGetList.action?mapId=getLevel3'
                    })
                });

               

                //数据表单
                DataFormPanel = function(){
                    return new Ext.FormPanel({
                        frame: true,
                        labelAlign: 'left',
                        labelWidth: 120,
                        width: 400,
                        height: 500,
                        autoScroll: true,
                        items: [
                            {
                                name: 'ID',
                                xtype: 'hidden'
                            },
                            {
                                name: 'ID1',
                                xtype: 'hidden'
                            },
                            {
                                fieldLabel: '一级行业',
                                hiddenName: 'L1',
                                width: 200,
                                height: 20,
                                xtype: 'combo',
                                allowBlank: false,
                                valueField: 'value',
                                displayField: 'display',
                                store: level1,
                                selectOnFocus: false,
                                emptyText: '请选择一级行业',
                                editable: true,
                                mode: 'local',
                                forceSelection: false,
                                typeAhead: true,
                                triggerAction: 'all',
                                listeners : {
                                    'select' : function() {
                                     try{
                                            var level2Combo=Ext.getCmp('level2Combo');
                                            //level2Combo.setValue("");
                                            level2Combo.store.reload({params:{json: Ext.util.JSON.encode(
                                                    {
                                                        "L_CODE":this.value
                                                    }
                                            )
                                           }
                                     });

                                        }
                                        catch(ex)
                                        {
                                            Ext.MessageBox.alert(ex);
                                        }

                                    }
                                }
                            }, {
                                fieldLabel: '一级行业关键词库',
                                name: 'L1K',
                                width: 200,
                                height: 50,
                                xtype: 'textarea',
                                allowBlank: true
                            },
                            {
                                fieldLabel: '一级行业知识备注',
                                name: 'L1COMMENT',
                                width: 200,
                                height: 50,
                                xtype: 'textarea',
                                allowBlank: true
                            },
                            {
                                fieldLabel: '一级行业维护备注',
                                name: 'L1LOG',
                                width: 200,
                                height: 50,
                                xtype: 'textarea',
                                allowBlank: true
                            },
                            {
                                name: 'ID2',
                                xtype: 'hidden'
                            },{
                                id : 'level2Combo',
                                fieldLabel: '二级行业',
                                hiddenName: 'L2',
                                width: 200,
                                height: 20,
                                xtype: 'combo',
                                allowBlank: true,
                                valueField: 'value',
                                displayField: 'display',
                                store: level2,
                                selectOnFocus: false,
                                emptyText: '请选择二级行业',
                                editable: true,
                                mode: 'local',
                                forceSelection: false,
                                typeAhead: true,
                                triggerAction: 'all',
                                listeners : {
                                    'select' : function() {
                                        try{
                                            var level3Combo=Ext.getCmp('level3Combo');
                                            level3Combo.store.reload({params:{json: Ext.util.JSON.encode(
                                                    {
                                                        "L_CODE":this.value
                                                    }
                                                )
                                                }
                                            });
                                        }
                                        catch(ex)
                                        {
                                            Ext.MessageBox.alert(ex);
                                        }

                                    }
                                }
                            }, {
                                fieldLabel: '二级行业关键词库',
                                name: 'L2K',
                                width: 200,
                                height: 50,
                                xtype: 'textarea',
                                allowBlank: true
                            },
                            {
                                fieldLabel: '二级行业知识备注',
                                name: 'L2COMMENT',
                                width: 200,
                                height: 50,
                                xtype: 'textarea',
                                allowBlank: true
                            },
                            {
                                fieldLabel: '二级行业维护备注',
                                name: 'L2LOG',
                                width: 200,
                                height: 50,
                                xtype: 'textarea',
                                allowBlank: true
                            },
                            {
                                name: 'ID3',
                                xtype: 'hidden'
                            },{
                                id : 'level3Combo',
                                fieldLabel: '三级行业',
                                hiddenName: 'L3',
                                width: 200,
                                height: 20,
                                xtype: 'combo',
                                allowBlank: true,
                                valueField: 'value',
                                displayField: 'display',
                                store: level3,
                                selectOnFocus: false,
                                emptyText: '请选择三级行业',
                                editable: true,
                                mode: 'local',
                                forceSelection: false,
                                typeAhead: true,
                                triggerAction: 'all'
                            }, {
                                fieldLabel: '三级行业关键词库',
                                name: 'L3K',
                                width: 200,
                                height: 50,
                                xtype: 'textarea',
                                allowBlank: true
                            },
                            {
                                fieldLabel: '三级行业知识备注',
                                name: 'L3COMMENT',
                                width: 200,
                                height: 50,
                                xtype: 'textarea',
                                allowBlank: true
                            },
                            {
                                fieldLabel: '三级行业维护备注',
                                name: 'L3LOG',
                                width: 200,
                                height: 50,
                                xtype: 'textarea',
                                allowBlank: true
                            }
                        ]
                    });
                };

                function handleAdd(){
                    var AddPanel = new DataFormPanel();
                    var AddWin = new Ext.Window({
                        title: '新建记录',
                        layout: 'fit',
                        width: 400,
                        height: 500,
                        plain: true,
                        items: AddPanel,
                        buttons: [{
                                text: '保存',
                                handler: function(btn){
                                    rSave(btn, AddWin, AddPanel, 'doInsert.action?mapId=insert');
                                }
                            }, {
                                text: '取消',
                                handler: function(){
                                    AddWin.close();
                                }
                            }]
                    });
                    AddWin.show(this);
                }

                function handleShow(){
                    if(true)
                        return;
                    var selectedKeys = grid.selModel.selections.keys; //returns array of selected rows ids only
                    if(selectedKeys.length != 1){
                        Ext.MessageBox.alert('提示','请选择一条记录！');
                    }else{
                        for(var i=0; i<selectedKeys.length; i++) {
                            mainPanel.loadTabIframePage('/fundCompany/detail.jsp?id='+selectedKeys[i], 'kb.company.show.'+selectedKeys[i]+'', '查看公司', 'cls');
                        }
                    }
                }

                function handleEdit(){
                    var selectedKeys = grid.selModel.selections.keys; //returns array of selected rows ids only
                    if(selectedKeys.length != 1){
                        Ext.MessageBox.alert('提示','请选择一条记录！');
                        return;
                    }
                    var EditPanel = new DataFormPanel();
                    var EditWin = new Ext.Window({
                        title: '修改记录',
                        layout: 'fit',
                        width: 400,
                        height: 500,
                        plain: true,
                        items: EditPanel,
                        buttons: [{
                                text:'保存',
                                handler:  function(btn) {
                                    rSave(btn, EditWin, EditPanel, 'doUpdate.action?mapId=update');
                                }
                            }, {
                                text: '取消',
                                handler: function(){
                                    EditWin.close();
                                }
                            }]
                    });
                    EditWin.show(this);
                    var request = {json: Ext.util.JSON.encode(selectedKeys)};
                    Ext.MessageBox.show({
                        msg: '正在获取数据, 请稍侯',
                        progressText: '正在请求数据',
                        width: 300,
                        wait: true,
                        waitConfig: {interval: 100}
                    });
                    Ext.Ajax.request({
                        url: '/business/doGetOne.action?mapId=oneBusiness', //url to server side script
                        method: 'POST',
                        params: request,//the unique id(s)Ext.util.JSON.encode(request)
                        callback: function (options, success, response) {
                            if(success){ //success will be true if the request succeeded
                                Ext.MessageBox.hide();
                                EditPanel.getForm().setValues(Ext.util.JSON.decode(response.responseText));
                            }else{
                                Ext.MessageBox.hide();
                                Ext.MessageBox.alert('失败，请重试', response.responseText);
                            }
                        },
                        //the function to be called upon failure of the request (server script, 404, or 403 errors)
                        failure:function(response,options){
                            Ext.MessageBox.hide();
                            Ext.MessageBox.alert('警告','出现异常错误！请联系管理员！');
                        },
                        success:function(response,options){
                            Ext.MessageBox.hide();
                        }
                    })// end Ajax request
                }

                function rSave(btn, win, panel, action){
                    if(panel.form.isValid()){
                        btn.disabled = true;
                        Ext.MessageBox.show({
                            msg: '正在请求数据, 请稍侯',
                            progressText: '正在请求数据',
                            width: 300,
                            wait: true,
                            waitConfig: {interval: 100}
                        });
                        var request = {json: Ext.util.JSON.encode(panel.form.getValues())};
                        //Ext.MessageBox.alert('提示', EditPanel.form.getValues());
                        Ext.Ajax.request({
                            url: '/business/'+action, //url to server side script
                            method: 'POST',
                            params: request,//the unique id(s)
                            callback: function (options, success, response){
                                if(success){ //success will be true if the request succeeded
                                    Ext.MessageBox.hide();
                                    win.close();
                                    if(action == 'doInsert'){
                                        store.load({params:{start: 0, limit: 20}});
                                    }else{
                                        store.reload();
                                    }
                                    //Ext.MessageBox.alert('成功', response.responseText);
                                }else{
                                    Ext.MessageBox.hide();
                                    Ext.MessageBox.alert('操作失败，请重试！', response.responseText);
                                }
                            },
                            //the function to be called upon failure of the request (server script, 404, or 403 errors)
                            failure:function(response,options){
                                Ext.MessageBox.hide();
                                Ext.MessageBox.alert('警告', '出现异常错误！请联系管理员！');
                            },
                            success:function(response,options){
                                Ext.MessageBox.hide();
                            }
                        })// end Ajax request
                    }
                }

                function handleDelete(){
                    var selectedKeys = grid.selModel.selections.keys; //returns array of selected rows ids only
                    if(selectedKeys.length > 0){
                        Ext.MessageBox.confirm('提示','您确实要删除选定的记录吗？', rDelete);
                    }else{
                        Ext.MessageBox.alert('提示','请至少选择一条记录！');
                    }//end
                }

                function rDelete(btn){
                    if(btn=='no')
                        return;
                    //var selectedRows = grid.selModel.selections.items;//returns record objects for selected rows (all info for row)
                    var selectedKeys = grid.selModel.selections.keys;
                    Ext.MessageBox.show({
                        msg: '正在删除数据, 请稍侯',
                        progressText: '正在请求数据',
                        width: 300,
                        wait: true,
                        waitConfig: {interval: 100}
                    });
                    var request = {json: Ext.util.JSON.encode(selectedKeys)};
                    Ext.Ajax.request({
                        url: '/business/doDelete.action?mapId=delete', //url to server side script
                        method: 'POST',
                        params: request,//the unique id(s)
                        callback: function (options, success, response){
                            if(success){ //success will be true if the request succeeded
                                Ext.MessageBox.hide();
                                store.load();
                                //Ext.MessageBox.alert('成功',response.responseText);
                            }else{
                                Ext.MessageBox.hide();
                                Ext.MessageBox.alert('操作失败，请重试！', response.responseText);
                            }
                        },
                        //the function to be called upon failure of the request (server script, 404, or 403 errors)
                        failure: function(response,options){
                            Ext.MessageBox.hide();
                            Ext.MessageBox.alert('警告', '出现异常错误！请联系管理员！');
                        },
                        success:function(response,options){
                            Ext.MessageBox.hide();
                        }
                    })// end Ajax request
                } // end deleteRecord

            });
        </script>

    </body>
</html>
