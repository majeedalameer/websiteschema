<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
            String path = request.getContextPath();
            String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>
<html>
    <head>
        <base href="<%=basePath%>">
        <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
        <title>Websiteschema Scheduler Management</title>

        <link rel="stylesheet" type="text/css" href="resources/css/Ext.ux.form.LovCombo.css">
        <script type="text/javascript" src="js/packages.js"></script>
        <script type="text/javascript" src="js/Ext.ux.form.LovCombo.js"></script>
        <script type="text/javascript" src="js/Ext.ux.ThemeCombo.js"></script>
        <script type="text/javascript" src="js/dwrproxy.js"></script>
        <script type="text/javascript" src="dwr/engine.js"></script>
        <script type="text/javascript" src="dwr/interface/ScheduleService.js"></script>
        <script type="text/javascript" src="dwr/interface/JobService.js"></script>
        <script type="text/javascript" src="dwr/interface/WrapperService.js"></script>
        <script type="text/javascript" src="js/wrapper/JobEditorPanel.js"></script>
    </head>

    <body>

        <div id="gridpanel"></div>

        <div id="info-panel"></div>

        <script type="text/javascript">
            var start = 0;
            var pageSize = 20;
            Ext.onReady(function(){

                var proxy = new Ext.data.DWRProxy(ScheduleService.getResults, true);
                var recordType = new Ext.data.Record.create(scheduleRecordType);
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
                    params.sort = 'createTime desc';
                });
                
                var type_store = new Ext.data.SimpleStore(
                {
                    fields :['name','value'],
                    data:[
                        ['开始停止次数',1],
                        ['CRONTAB',0],
                        ['无效类型',-1]
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
                        header: 'ID',
                        dataIndex: 'id',
                        width: 50
                    },
                    {
                        header: '起始URLId',
                        dataIndex: 'startURLId',
                        width: 300,
                        hidden: true,
                        editor: new fm.TextField({
                            allowBlank: false,
                            readOnly : true
                        })
                    },
                    {
                        header: '起始URL',
                        dataIndex: 'startURL',
                        width: 300,
                        editor: new fm.TextField({
                            allowBlank: false,
                            readOnly : true
                        })
                    },
                    {
                        header: '调度信息',
                        dataIndex: 'schedule',
                        width: 100,
                        editor: new fm.TextField({
                            allowBlank: false
                        })
                    },
                    {
                        header: '类型',
                        dataIndex: 'scheduleType',
                        width: 100,
                        hidden : false,
                        editor: new fm.ComboBox({
                            store : type_store,
                            triggerAction: 'all',
                            allowBlank: false,
                            forceSelection: true,
                            mode: 'local',
                            displayField:'name',
                            valueField:'value'

                        }),
                        renderer: function(value,metadata,record){
                            var index = type_store.find('value',value);
                            if(index!=-1){
                                return type_store.getAt(index).data.name;
                            }
                            return value;
                        }
                    },
                    {
                        header: '任务配置',
                        dataIndex: 'jobId',
                        width: 60,
                        editor: new fm.TextField({
                            allowBlank: false,
                            readOnly : true
                        })
                    },
                    {
                        header: '编辑任务配置',
                        width: 80,
                        xtype: 'actioncolumn',
                        items: [
                            {
                                icon   : 'resources/accept.gif',  // Use a URL in the icon config
                                tooltip: '编辑任务配置',
                                handler: editJob
                            }
                        ]
                    },
                    {
                        header: '创建时间',
                        dataIndex: 'createTime',
                        width: 200,
                        editor: new fm.DateField({
                            allowBlank: false,
                            readOnly : true,
                            format: 'Y-m-d H:i:s'
                        })
                    }
                ]);

                // by default columns are sortable
                cm.defaultSortable = false;

                var grid = new Ext.grid.EditorGridPanel({
                    //el:'topic-grid',
                    renderTo: 'gridpanel',
                    width: '100%',
                    autoHeight: true,
                    clicksToEdit:1,
                    autoScroll: true,
                    //title: '分页和排序列表',
                    store: store,
                    trackMouseOver: false,
                    loadMask: true,
                    enableHdMenu: true,
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
                        }, '-',
                        {
                            text: '启动调度器',
                            tooltip: '启动调度器',
                            iconCls: 'icon-delete',
                            handler: handleLaunchScheduler
                        }, '-',
                        {
                            text: '关闭调度器',
                            tooltip: '关闭调度器',
                            iconCls: 'icon-delete',
                            handler: handleShutdownScheduler
                        }, '->',
                        ' ', '网站ID', ' ',
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
                        height: '22',
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

                var p = '<p>CRONTAB的配置类似Linux系统的CRONTAB配置，但其实是<a target="_blank" href="http://www.quartz-scheduler.org/documentation/quartz-2.x/tutorials/crontrigger">Quartz</a>的配置:秒分时日月周。\n\
                而且需要注意的是：第一位秒是不能自己配置的，websiteschema认为用户没有必要指定精确的秒钟；另外不能同时指定周和月中的特定天，所以默认参数的周是？，这里的周不是一年54周的意思。</p>'
                

                new Ext.Panel({
                    title: 'CRONTAB的帮助提示',
                    preventBodyReset: true,
                    renderTo: 'info-panel',
                    width: '100%',
                    html: p
                });

                function handleAdd(){
                    var p = new recordType();
                    grid.stopEditing();
                    p.set("jobId","0");
                    p.set("startURLId","0");
                    p.set("schedule",defaultSchedule)
                    p.set("scheduleType","0");
                    store.insert(0, p);
                    grid.startEditing(0, 0);
                    ScheduleService.insert(p.data, function(){
                        store.reload();
                    });
                }

                function handleEdit(){
                    var mr = store.getModifiedRecords();
                    for(var i=0;i<mr.length;i++){
                        if(i == mr.length - 1) {
                            ScheduleService.update(mr[i].data, function(){
                                store.reload();
                            });
                        } else {
                            ScheduleService.update(mr[i].data);
                        }
                    }
                }

                //删除数据
                function handleDelete(){
                    var selections = grid.selModel.getSelections();
                    for (var i = 0,len = selections.length; i < len; i++) {
                        if(i == len - 1) {
                            ScheduleService.deleteRecord(selections[i].data, function(){
                                store.reload();
                            });
                        } else {
                            ScheduleService.deleteRecord(selections[i].data);
                        }
                    }
                }

                //启动调度器
                function handleLaunchScheduler(){
                    ScheduleService.launchScheduler(function(data){
                        if(data) {
                            alert("启动调度器成功！");
                        } else {
                            alert("启动调度器失败！");
                        }
                    });
                }

                //关闭调度器
                function handleShutdownScheduler(){
                    ScheduleService.shutdownScheduler(function(data){
                        if(data) {
                            alert("关闭调度器成功！");
                        } else {
                            alert("关闭调度器失败！");
                        }
                    });
                }

                function handleQuery(){
                    store.reload();
                }

                function editJob(grid, rowIndex, colIndex) {
                    var job= grid.getStore().getAt(rowIndex);
                    if(null != job) {
                        var id = job.data.jobId;
                        JobService.getById(id, function(data){
                            var editPanel = new JobEditorFormPanel();
                            Ext.getCmp('fp_id').setValue(data.id);
                            Ext.getCmp('fp_job').setValue(data.configure);
                            Ext.getCmp('fp_jobType').setValue(data.jobType);
                            Ext.getCmp('fp_wrapperType').setValue(data.wrapperId);
                            var AddWin = new Ext.Window({
                                title: '新建记录',
                                width: 600,
                                height: 350,
                                plain: true,
                                items: editPanel,
                                buttons: [{
                                        text: '保存',
                                        handler: function(){
                                            data.configure = editPanel.getComponent('fp_job').getValue();
                                            data.jobType = editPanel.getComponent('fp_jobType').getValue();
                                            data.jobType = editPanel.getComponent('fp_jobType').getValue();
                                            JobService.update(data, function(){
                                                store.reload();
                                            });
                                            AddWin.close();
                                        }
                                    }, {
                                        text: '取消',
                                        handler: function(){
                                            AddWin.close();
                                        }
                                    }]
                            });
                            AddWin.show(this);
                        });
                    }
                }
            });
        </script>

    </body>
</html>
