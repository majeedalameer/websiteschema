<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" isErrorPage="true" %>
<!------- CMS IMPORTS BEGIN ------------>
<%@ page import="com.apc.basic.util.CMyException" %>
<%@ page import="com.apc.basic.util.CMyString" %>
<%@ page import="com.apc.basic.util.ExceptionNumber" %>
<%@ page import="com.apc.surface.locale.LocaleServer" %>
<!------- CMS IMPORTS END ------------>

<%
	response.setHeader("Cache-Control","no-cache"); //HTTP 1.1
	response.setHeader("Pragma","no-cache"); //HTTP 1.0
	response.setDateHeader ("Expires", -1);
	//prevents caching at the proxy server
	response.setDateHeader("max-age", 0);
%>

<%
	//[1]增加站点根目录配置  [2]增加不折行处理

	int		nErrNo; //错误编号
	String	sErrType, sErrMsg, sErrDetail;  //错误类型，错误信息，详细信息
	String  sRootPath = "/cms";  //站点根目录 //需要从系统配置中读取

	if( exception instanceof CMyException ){
		CMyException myException = (CMyException)exception;
		nErrNo		= myException.getErrNo();
		sErrType	= myException.getErrNoMsg();
		sErrMsg		= myException.getMyMessage();
		sErrDetail	= CMyString.transDisplay( myException.getStackTraceText() );
    }
    else{
		nErrNo		= ExceptionNumber.ERR_UNKNOWN;
		sErrType	= "未知";
		sErrMsg		= "未知错误！";
		sErrDetail	= exception.getMessage();
	}

	//处理不同异常的表现方式
	String sUrl = null;
	if(nErrNo == ExceptionNumber.ERR_USER_NOTLOGIN){//处理用户登录超时的问题
		sUrl = "./include/not_login.htm";
		//request.getRequestDispatcher(sUrl).forward(request,response);
	}
	
%>

<HTML>
<HEAD>
<META http-equiv="Content-Type" content="text/html; charset=UTF-8">
<TITLE><%=LocaleServer.getString("platform.lable.version", "")%> 系统提示信息</TITLE>
<LINK href="./style/style.css" rel="stylesheet" type="text/css">
<SCRIPT src="./js/CNLPAction.js"></SCRIPT>
<SCRIPT src="./js/CNLPDialogHead.js"></SCRIPT>
<script language="javascript">
	function copyToClipboard(){
		var sDetailMsg = document.all("msgForCopy").innerText;
		window.clipboardData.setData("Text", sDetailMsg);
		CNLPAction_alert("已经复制到剪切板中！");
	}

	function showDetail( ){
		var objSpan = document.getElementById("id_spanErrDetail");
		if( objSpan==null ) return;

		var bCurrShowed = ( objSpan.style.display=="inline" );
		var sDisplay, sTitle;
		if( bCurrShowed ){
			sDisplay = "none";
			sTitle = "显示";
		}
		else{
			sDisplay = "inline";
			sTitle = "隐藏"
		}

		objSpan.style.display = sDisplay;  //显示或隐藏

		//更改链接标题
		var objLink = document.getElementById("id_linkShowDetail");
		if( objLink!=null ) {
			objLink.innerText = sTitle;
		}
	}

	function doDispatch(_sUrl){
		window.top.location.href = _sUrl;
	}
		
	<%
		if(sUrl != null){
			out.println("doDispatch(\""+sUrl+"\");");
		}
	%>
</script>
</HEAD>

<BODY>
    <TABLE width="100%" height="100%" border="0" cellpadding="0" cellspacing="0">
    <tr>
<TD height="25">
	<SCRIPT src="../js/CNLPDialogHead.js"></SCRIPT>
	<SCRIPT LANGUAGE="JavaScript">
		NLPDialogHead.draw("系统信息",true);
	</SCRIPT>
</TD>
</tr>
    <TR>
	<TD align="center" valign="top" class="tanchu_content_td">
	    <TABLE width="100%" border="0" cellspacing="0" cellpadding="0">
	    <TR>
		<TD>
		    <TABLE width="100%" border="0" cellpadding="5" cellspacing="1" bgcolor="a6a6a6">
		    <TR>
			<TD height="350" valign="top" bgcolor="#FFFFFF">
			    <TABLE width="400" border="0" align="center" cellpadding="3" cellspacing="0">
			    <TR>
				<TD colspan="2">
				    <TABLE width="100%" border="0" cellspacing="5" cellpadding="0">
				    <TR>
					    <TD width="60"><IMG src="./images/error.gif" width="60" height="60"></TD>
					<TD>
					<SPAN class="font_redbold style7">
					系统提示信息:&nbsp;&nbsp;<%=sErrMsg%>
					</SPAN>
					</TD>
				    </TR>
				    </TABLE>
				</TD>
			    </TR>
			    <TR>
				<TD colspan="2" align="left" valign="top">&nbsp;</TD>
			    </TR>
			    <!--TR>
				<TD width="100" align="left" valign="top"> 请尝试以下操作：</TD> 
				<TD width="300" align="left" valign="top">
				<UL>
				    <LI><A href="#" onclick="javascript:document.execCommand('refresh');"> 重试一次</A>
				    <LI><A href="#" onclick="javascript:history.back();">后退</A>
				</UL>
				</TD>
			    </TR-->
			    <TR>
				    <TD colspan="2" align="left" valign="top" > </TD>
			    </TR>
			    <TR>
				<TD colspan="2" align="left" valign="top"><A id="id_linkShowDetail" href="#" class="navigation_page_link" onclick="showDetail();return false;">查看详细错误信息</A>
				</TD>
			    </TR>
				<TR>
                	<TD ALIGN="LEFT" colspan="2" NOWRAP>
						<SPAN id="id_spanErrDetail" STYLE="display:none">
						<UL>
							<li><font color="#000000"><b>错误编号：</b></font><%=nErrNo%></li>
							<li><font color="#000000"><b>错误类型：</b></font><%=sErrType%></li>
							<li><font color="#000000"><b>详细信息：</b></font><br>
								<font style='font-family:Arial'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<span id="msgForCopy" name="msgForCopy"><%=sErrDetail%></span></font></li>
						</UL>
						</SPAN>
					</TD>
                </TR>
			    </TABLE>
			</TD>
		    </TR>
		    </TABLE>
		</TD>
	    </TR>
	    <TR>
		<TD>
		    <TABLE width="200" border="0" align="center" cellpadding="0" cellspacing="8">
		    <TR>
			<TD>
			<DIV align="center">
			<INPUT type="button" name="btnCopy" onclick="copyToClipboard()" value="复制到剪切板" class="inputbutton">&nbsp;&nbsp;&nbsp;
			<INPUT type="button" name="btnClose" onclick="window.close()" value="关闭窗口" class="inputbutton">
			</DIV>
			</TD>
		    </TR>
		    </TABLE>
		</TD>
	    </TR>
	    </TABLE>
	</TD>
    </TR>
    </TABLE>
</BODY>
</HTML>