<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
            String path = request.getContextPath();
            String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
    <head>
        <base href="<%=basePath%>">

        <title>My JSP 'hello.jsp' starting page</title>

        <meta http-equiv="pragma" content="no-cache">
        <meta http-equiv="cache-control" content="no-cache">
        <meta http-equiv="expires" content="0">
        <meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
        <meta http-equiv="description" content="This is my page">

    </head>
    <!-- 记得引入js，测试地址： http://localhost:8083/dwrweb/dwr/ -->
    <script type="text/javascript" src="dwr/engine.js"></script>
    <script type="text/javascript" src="dwr/interface/helloSrv.js"></script>
    <script type="text/javascript" src="dwr/util.js"></script>
    <script type="text/javascript">
        function loadDept(){
            //说明已经加载，不必重新加载
            if($('depts').options.length > 0){
                return;
            }
            DWRUtil.addOptions('depts', [{id:'',name:'正在下载...'}], 'id', 'name');
            dwr.engine._execute("dwr", 'deptSrv', 'getDepts', function(depts){
                DWRUtil.removeAllOptions('depts');
                DWRUtil.addOptions('depts', depts, 'id', 'name');
            });
        }
        function loadDept2(){
            if($('depts2').options.length > 0){
                return;
            }
            DWRUtil.addOptions('depts2', [{id:'',name:'正在下载...'}], 'id', 'name');
            dwr.engine._execute("dwr", 'deptSrv', 'getDeptsForPo', function(depts){
                DWRUtil.removeAllOptions('depts2');
                DWRUtil.addOptions('depts2', depts, 'id', 'name');
            });
        }
        function saveDept(){
            //声明dept对象
            var dept = {
                id:$("deptid").value,
                name:$("deptname").value
            };
            dwr.engine._execute("dwr", 'deptSrv', 'saveDept', [dept], function(){
                alert('保存成功!');
            });

        }
        function find(){
            dwr.engine._execute("dwr", 'deptSrv', 'findDept', {
                callback:function(results){
                    alert('查询成功!');
                },
                errorHandler:function(e){
                    alert("查询失败:" + e);
                }
            });

        }
    </script>
    <body>
        <select id="depts" onclick="loadDept();"></select>
        <select id="depts2" onclick="loadDept2();"></select>
        <hr/>
        ID:<input id="deptid" type="text" size="8"/>
        Name:<input id="deptname" type="text" size="8"/>
        <input value="保存部门" type="button" onclick="saveDept();"/>
        <input value="查找" type="button" onclick="find();"/>
    </body>
</html>
