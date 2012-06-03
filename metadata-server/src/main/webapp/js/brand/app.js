var start = 0;
var pageSize = 20;
Ext.onReady(function(){
    Ext.QuickTips.init();
    var proxy = new Ext.data.DWRProxy(BrandService.getResults, true);
    var recordType = new Ext.data.Record.create(brandRecordType);
    var store=new Ext.data.Store({
        proxy : proxy,
        reader : new Ext.data.ListRangeReader(
        {
            id : 'id',
            totalProperty : 'totalSize'
        }, recordType
        ),
        remoteSort: false

    });
    proxy.on('beforeload', function(thiz, params) {
        params.match = Ext.getCmp('MATCH').getValue();
        params.siteId = Ext.getCmp('SITEID').getValue();
        params.sort = 'createTime desc';
    });

    // the column model has information about grid columns
    // dataIndex maps the column to the specific data field in
    // the data store
    // var nm = new Ext.grid.RowNumberer();
    var fm = Ext.form;
    var sm = new Ext.grid.CheckboxSelectionModel();  // add checkbox column
    var cm = new Ext.grid.ColumnModel([
        //nm,
        sm,
        {
            header: 'cid',
            dataIndex: 'c_id',
            width: 50,
            editor: new fm.TextField({
                allowBlank: false
            })
        },
        {
            header: '品牌',
            dataIndex: 'name',
            width: 100,
            editor: new fm.TextField({
                allowBlank: false
            })
        },
        {
            header: '同义词',
            dataIndex: 'keywords',
            width: 150,
            editor: new fm.TextField({
                allowBlank: false
            })
        },
        {
            header: '型号',
            dataIndex: 'model',
            width: 300,
            editor: new fm.TextField({
                allowBlank: false
            })
        },
        {
            header: '创建时间',
            dataIndex: 'createTime',
            width: 200,
            hidden : true,
            editor: new fm.DateField({
                allowBlank: false,
                readOnly : true,
                format: 'Y-m-d H:i:s'
            })
        },
        {
            header: '创建人',
            dataIndex: 'createUser',
            width: 100,
            hidden : true,
            editor: new fm.TextField({
                allowBlank: false
            })
        },
        {
            header: '修改时间',
            dataIndex: 'updateTime',
            width: 130,
            editor: new fm.DateField({
                allowBlank: false,
                readOnly : true,
                format: 'Y-m-d H:i:s'
            })
        },
        {
            header: '修改人',
            dataIndex: 'lastUpdateUser',
            width: 100,
            editor: new fm.TextField({
                allowBlank: false
            })
        }
        ]);

    // by default columns are sortable
    cm.defaultSortable = false;

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
            text: '提交',
            tooltip: '提交修改记录',
            iconCls: 'icon-edit',
            handler: handleEdit
        }, '-',
        {
            text: '删除',
            tooltip: '删除记录',
            iconCls: 'icon-delete',
            handler: handleDelete
        }, '->',
        ' ', '网站ID', ' ',
        {
            xtype: 'textfield',
            id: 'SITEID',
            initEvents : function(){
                var keyPressed = function(e) {
                    if(e.getKey()==e.ENTER){
                        handleQuery();
                    }
                };
                this.el.on("keypress", keyPressed, this);
            }
        }, ' ',
        ' ', '栏目名称', ' ',
        {
            xtype: 'textfield',
            id: 'MATCH',
            initEvents : function(){
                var keyPressed = function(e) {
                    if(e.getKey()==e.ENTER){
                        handleQuery();
                    }
                };
                this.el.on("keypress", keyPressed, this);
            }
        }, ' ',
        {
            text: '检索',
            iconCls: 'icon-query',
            handler: handleQuery
        }
        ],
        bbar: new Ext.PagingToolbar({
            pageSize: pageSize,
            store: store,
            displayInfo: true
        })
    });


    // render it
    grid.render();

    // trigger the data store load
    store.load(
    {
        params :
        {
            start : start,
            limit : pageSize
        }
    });

    function handleAdd(){
        var p = new recordType();
        grid.stopEditing();
//        p.set("channel","channel name");
//        p.set("siteId","site id");
        store.insert(0, p);
        grid.startEditing(0, 0);
        BrandService.insert(p.data, function(){
            store.reload();
        });
    }


    function handleEdit(){
        var mr = store.getModifiedRecords();
        for(var i=0;i<mr.length;i++){
            if(i == mr.length - 1) {
                BrandService.update(mr[i].data, function(){
                    store.reload();
                });
            } else {
                BrandService.update(mr[i].data);
            }
        }
    }

    //删除数据
    function handleDelete(){
        var selections = grid.selModel.getSelections();
        for (var i = 0,len = selections.length; i < len; i++) {
            if(i == len - 1) {
                BrandService.deleteRecord(selections[i].data, function(){
                    store.reload();
                });
            } else {
                BrandService.deleteRecord(selections[i].data);
            }
        }
    }

    function handleQuery(){
        store.reload();
    }
});