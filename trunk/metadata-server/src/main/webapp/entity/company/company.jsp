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
                    idProperty: 'OID',
                    // turn on remote sorting
                    remoteSort: false,
                    fields: [
                        {name: 'ORG_NAME', type: 'string'},
                        {name: 'ORG_OLD_NAME', type: 'string'},
                        {name: 'SHORT_NAME', type: 'string'},
                        {name: 'SHORT_NAMES', type: 'string'},
                        {name: 'SEC_CODE', type: 'string'},
                        {name: 'STOCK_NAME', type: 'string'},
                        {name: 'STOCK_TYPE', type: 'string'},
                        {name: 'PROVINCE', type: 'string'},
                        {name: 'CITY', type: 'string'},
                        {name: 'PLACE', type: 'string'},
                        {name: 'COMMENTS', type: 'string'},
                        {name: 'LOGS', type: 'string'}
                    ],
                    // load using script tags for cross domain, if the data in on the same domain as
                    // this page, an HttpProxy would be better
                    proxy: new Ext.data.HttpProxy({
                        url: '/company/doGetList.action?mapId=company'
                    })
                });
                //store.setDefaultSort('title', 'desc');
                store.on('beforeload', function(thiz, options) {
                    Ext.apply(thiz.baseParams, {
                        json: Ext.util.JSON.encode({
                            "SHORT_NAME": Ext.getCmp('QUERY_SHORT').getValue(),
                            "SEC_CODE": Ext.getCmp('QUERY_SEC_CODE').getValue(),
                            "STOCK_TYPE": Ext.getCmp('QUERY_TYPE').getValue(),
                            "SORT_NAME" : Ext.getCmp('SORT_NAME').getValue(),
                            "FLAG" : Ext.getCmp('QUERY_FLAG').getValue()
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
                        header: '上市公司全称',
                        dataIndex: 'ORG_NAME',
                        width: 100
                    },
                    {
                        header: '上市公司旧称',
                        dataIndex: 'ORG_OLD_NAME',
                        width: 40
                    },
                    {
                        header: '上市公司简称',
                        dataIndex: 'SHORT_NAME',
                        width: 40
                    },
                    {
                        header: '上市公司简称(多个)',
                        dataIndex: 'SHORT_NAMES',
                        width: 80
                    },
                    {
                        header: '股票代码',
                        dataIndex: 'SEC_CODE',
                        width: 40
                    },
                    {
                        header: '股票名称',
                        dataIndex: 'STOCK_NAME',
                        width: 40
                    },
                    {
                        header: '股票类型',
                        dataIndex: 'STOCK_TYPE',
                        width: 28
                    },
                    {
                        header: '注册地(省)',
                        dataIndex: 'PROVINCE',
                        width: 30
                    },
                    {
                        header: '注册地(市)',
                        dataIndex: 'CITY',
                        width: 30
                    },
                    {
                        header: '公司注册地',
                        dataIndex: 'PLACE',
                        width: 60
                    },
                    {
                        header: '知识备注',
                        dataIndex: 'COMMENTS',
                        width: 60
                    },
                    {
                        header: '维护备注',
                        dataIndex: 'LOGS',
                        width: 60
                    }
                ]);

                // by default columns are sortable
                cm.defaultSortable = true;

                handleQuery = function(){
                    Ext.getCmp('SORT_NAME').setValue("");
                    Ext.getCmp('QUERY_FLAG').setValue("0");
                    store.removeAll();
                    store.load();
                }
                handleClear = function(){
                    Ext.getCmp('QUERY_SHORT').setValue("");
                    Ext.getCmp('QUERY_TYPE').setValue("");
                    Ext.getCmp('SORT_NAME').setValue("");
                    Ext.getCmp('QUERY_FLAG').setValue("0");
                    store.removeAll();
                    store.load();
                }
                handleSort = function(){
                    Ext.getCmp('QUERY_SHORT').setValue("");
                    Ext.getCmp('QUERY_TYPE').setValue("");
                    Ext.getCmp('QUERY_FLAG').setValue("0");
                    store.removeAll();
                    store.load();
                }
                handleQueryFlag = function(){
                    Ext.getCmp('QUERY_SHORT').setValue("");
                    Ext.getCmp('QUERY_TYPE').setValue("");             
                    Ext.getCmp('QUERY_FLAG').setValue("1");
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
                        },'-','排序',' ',
                        {
                            fieldLabel: '排序',
                            id: 'SORT_NAME',
                            width: 150,
                            height: 20,
                            xtype: 'combo',
                            allowBlank: true,
                            valueField: 'value',
                            displayField: 'display',
                            store: new Ext.data.SimpleStore({
                                data: [["nlssort(org_name,'NLS_SORT=SCHINESE_PINYIN_M')","上市公司全称"],["nlssort(short_name,'NLS_SORT=SCHINESE_PINYIN_M')","上市公司简称"],["nlssort(sec_code,'NLS_SORT=SCHINESE_PINYIN_M')","股票代码"]],
                                fields: ['value','display']
                            }),
                            selectOnFocus: true,
                            emptyText: '请选择排序字段',
                            editable: false,
                            mode: 'local',
                            forceSelection: true,
                            typeAhead: true,
                            triggerAction: 'all',
                            listeners: {
                                select: function() {
                                    handleSort();
                                }
                            }

                        },' ',{
                            text: '查询更新',
                            tooltip: '缺省检索',
                            iconCls: 'icon-query',
                            handler: handleQueryFlag
                        },
                        ' ',{
                            xtype: 'hidden',
                            id: 'QUERY_FLAG',
                            value: '0'
                        },
                        '->',' ', {
                            text: '缺省检索',
                            tooltip: '缺省检索',
                            iconCls: 'icon-query',
                            handler: handleClear
                        },
                        
                        ' ', '股票代码', ' ',
                        {
                            xtype: 'textfield',
                            id: 'QUERY_SEC_CODE',
                            width: 80,
                            height: 20,
                            initEvents : function(){
                                var keyPress = function(e) {
                                    if(e.getKey()==e.ENTER){
                                        handleQuery();
                                    }
                                };
                                this.el.on("keypress", keyPress, this);
                            }
                        },' ', '上市公司简称', ' ',
                        {
                            xtype: 'textfield',
                            id: 'QUERY_SHORT',
                            initEvents : function(){
                                var keyPress = function(e) {
                                    if(e.getKey()==e.ENTER){
                                        handleQuery();
                                    }
                                };
                                this.el.on("keypress", keyPress, this);
                            }
                        }, ' ', '股票类型', ' ',
                        {
                            xtype: 'textfield',
                            id: 'QUERY_TYPE',
                            width: 80,
                            height: 20,
                            initEvents : function(){
                                var keyPress = function(e) {
                                    if(e.getKey()==e.ENTER){
                                        handleQuery();
                                    }
                                };
                                this.el.on("keypress", keyPress, this);
                            }
                        },' ',
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
                                name: 'OID',
                                xtype: 'hidden'
                            }, {
                                fieldLabel: '上市公司全称',
                                name: 'ORG_NAME',
                                width: 200,
                                height: 20,
                                xtype: 'textfield',
                                allowBlank: true
                            }, {
                                fieldLabel: '上市公司旧称',
                                name: 'ORG_OLD_NAME',
                                width: 200,
                                height: 20,
                                xtype: 'textfield',
                                allowBlank: true
                            }, {
                                fieldLabel: '上市公司简称',
                                name: 'SHORT_NAME',
                                width: 200,
                                height: 20,
                                xtype: 'textfield',
                                allowBlank: true
                            }, {
                                fieldLabel: '上市公司简称(多个)',
                                name: 'SHORT_NAMES',
                                width: 200,
                                height: 100,
                                xtype: 'textarea',
                                allowBlank: true
                            },
                            {
                                fieldLabel: '股票代码',
                                name: 'SEC_CODE',
                                width: 200,
                                height: 20,
                                xtype: 'textfield',
                                allowBlank: true
                            },
                            {
                                fieldLabel: '股票名称',
                                name: 'STOCK_NAME',
                                width: 200,
                                height: 20,
                                xtype: 'textfield',
                                allowBlank: true
                            },
                            {
                                fieldLabel: '股票类型',
                                name: 'STOCK_TYPE',
                                width: 200,
                                height: 20,
                                xtype: 'textfield',
                                allowBlank: true
                            },
                            {
                                fieldLabel: '注册地(省)',
                                name: 'PROVINCE',
                                width: 200,
                                height: 20,
                                xtype: 'textfield',
                                allowBlank: true
                            },
                            {
                                fieldLabel: '注册地(市)',
                                name: 'CITY',
                                width: 200,
                                height: 20,
                                xtype: 'textfield',
                                allowBlank: true
                            },
                            {
                                fieldLabel: '公司注册地',
                                name: 'PLACE',
                                width: 200,
                                height: 20,
                                xtype: 'textfield',
                                allowBlank: true
                            },
                            {
                                fieldLabel: '知识备注',
                                name: 'COMMENTS',
                                width: 200,
                                height: 20,
                                xtype: 'textfield',
                                allowBlank: true
                            },
                            {
                                fieldLabel: '维护备注',
                                name: 'LOGS',
                                width: 200,
                                height: 20,
                                xtype: 'textfield',
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
                            mainPanel.loadTabIframePage('/company/detail.jsp?id='+selectedKeys[i], 'kb.company.show.'+selectedKeys[i]+'', '查看公司', 'cls');
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
                        url: '/company/doGetOne.action?mapId=oneCompany', //url to server side script
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
                            url: '/company/'+action, //url to server side script
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
                        url: '/company/doDelete.action?mapId=delete', //url to server side script
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
                            btn.disabled = false;
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
