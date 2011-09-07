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
                        {name: 'SHORT', type: 'string'},
                        {name: 'SHORTS', type: 'string'},
                        {name: 'SCALE', type: 'string'},
                        {name: 'PLACE', type: 'string'},
                        {name: 'ORG_COMMENT', type: 'string'},
                        {name: 'COM_TYPE' , type: 'string'}
                    ],
                    // load using script tags for cross domain, if the data in on the same domain as
                    // this page, an HttpProxy would be better
                    proxy: new Ext.data.HttpProxy({
                        url: '/fundCompany/doGetList.action?mapId=fundCompany'
                    })
                });
                //store.setDefaultSort('title', 'desc');
                store.on('beforeload', function(thiz, options) {
                    Ext.apply(thiz.baseParams, {
                        json: Ext.util.JSON.encode({
                            "SHORT": Ext.getCmp('QUERY_SHORT').getValue(),
                            "SORT_NAME" : Ext.getCmp('SORT_NAME').getValue()
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
                        header: '基金公司全称',
                        dataIndex: 'ORG_NAME',
                        width: 100
                    },
                    {
                        header: '基金公司旧称',
                        dataIndex: 'ORG_OLD_NAME',
                        width: 100
                    },
                    {
                        header: '基金公司简称',
                        dataIndex: 'SHORT',
                        width: 100
                    },
                    {
                        header: '基金公司简称(多个)',
                        dataIndex: 'SHORTS',
                        width: 200
                    },
                    {
                        header: '基金公司分类',
                        dataIndex: 'COM_TYPE',
                        width: 80
                    },
                    {
                        header: '基金公司规模',
                        dataIndex: 'SCALE',
                        width: 80
                    },
                    {
                        header: '总部办公地区',
                        dataIndex: 'PLACE',
                        width: 60
                    },
                    {
                        header: '备注',
                        dataIndex: 'ORG_COMMENT',
                        width: 60
                    }
                ]);

                // by default columns are sortable
                cm.defaultSortable = true;

                handleQuery = function(){
                    Ext.getCmp('SORT_NAME').setValue("");
                    store.removeAll();
                    store.load();
                }

                handleClear = function(){
                    Ext.getCmp('QUERY_SHORT').setValue("");
                    Ext.getCmp('SORT_NAME').setValue("");
                    store.removeAll();
                    store.load();
                }

                handleSort = function(){
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
                            width: 200,
                            height: 20,
                            xtype: 'combo',
                            allowBlank: true,
                            valueField: 'value',
                            displayField: 'display',
                            store: new Ext.data.SimpleStore({
                                data: [["nlssort(short,'NLS_SORT=SCHINESE_PINYIN_M')","基金公司简称"]],
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

                        },
                        '->',
                        ' ', {
                            text: '缺省检索',
                            tooltip: '缺省检索',
                            iconCls: 'icon-query',
                            handler: handleClear
                        },
                        ' ', '基金公司简称', ' ',
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

                var com_type = new Ext.data.Store({
                    reader: new Ext.data.JsonReader({
                        totalProperty: 'total',
                        root: 'results',
                        fields: [
                            {name: 'value', mapping: 'TYPE_ID', type: 'string'},
                            {name: 'display', mapping: 'COM_TYPE', type: 'string'}
                        ]}
                ),
                    autoLoad: true,
                    proxy: new Ext.data.HttpProxy({
                        url: '/fundCompany/doGetList.action?mapId=ComType'
                    })
                }
            );

                //数据表单
                DataFormPanel = function(){
                    return new Ext.FormPanel({
                        frame: true,
                        labelAlign: 'left',
                        labelWidth: 130,
                        width: 400,
                        height: 300,
                        autoScroll: true,
                        items: [
                            {
                                name: 'OID',
                                xtype: 'hidden'
                            }, {
                                fieldLabel: '基金公司全称',
                                name: 'ORG_NAME',
                                width: 200,
                                height: 20,
                                xtype: 'textfield',
                                allowBlank: false
                            }, {
                                fieldLabel: '基金公司旧称',
                                name: 'ORG_OLD_NAME',
                                width: 200,
                                height: 20,
                                xtype: 'textfield',
                                allowBlank: true
                            }, {
                                fieldLabel: '基金公司简称',
                                name: 'SHORT',
                                width: 200,
                                height: 20,
                                xtype: 'textfield',
                                allowBlank: false
                            },
                            {
                                fieldLabel: '基金公司类型',
                                hiddenName: 'TYPE_ID',
                                width: 200,
                                height: 20,
                                xtype: 'lovcombo',
                                allowBlank: false,
                                valueField: 'value',
                                displayField: 'display',
                                store: com_type,
                                hideOnSelect: false,
                                selectOnFocus: true,
                                emptyText: '请选择基金公司类型',
                                editable: false,
                                mode: 'local',
                                forceSelection: true,
                                typeAhead: true,
                                triggerAction: 'all'
                            },
                            {
                                fieldLabel: '基金公司规模',
                                hiddenName: 'SCALE',
                                width: 200,
                                height: 20,
                                xtype: 'combo',
                                allowBlank: false,
                                valueField: 'value',
                                displayField: 'value',
                                store: new Ext.data.SimpleStore({
                                    data: [['小型（300亿以下）'],['中型（300-1000亿）'], ['大型（1000亿以上）']],
                                    fields: ['value']
                                }),
                                selectOnFocus: true,
                                emptyText: '请选择基金规模',
                                editable: false,
                                mode: 'local',
                                forceSelection: true,
                                typeAhead: true,
                                triggerAction: 'all'
                            }, {
                                fieldLabel: '总部办公地区',
                                name: 'PLACE',
                                width: 200,
                                height: 20,
                                xtype: 'textfield',
                                allowBlank: false
                            }, {
                                fieldLabel: '备注',
                                name: 'ORG_COMMENT',
                                width: 200,
                                height: 20,
                                xtype: 'textfield',
                                allowBlank: true
                            }, {
                                fieldLabel: '基金公司简称(多个)',
                                name: 'SHORTS',
                                width: 200,
                                height: 100,
                                xtype: 'textarea',
                                allowBlank: false
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
                        url: '/fundCompany/doGetOne.action?mapId=oneFundCompany', //url to server side script
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
                            url: '/fundCompany/'+action, //url to server side script
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
                        url: '/fundCompany/doDelete.action?mapId=delete', //url to server side script
                        method: 'POST',
                        params: request,//the unique id(s)
                        callback: function (options, success, response){
                            if(success){ //success will be true if the request succeeded
                                Ext.MessageBox.hide();
                                store.reload();
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
