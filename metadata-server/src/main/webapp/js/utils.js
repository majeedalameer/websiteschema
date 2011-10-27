/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


function getCookie(name)//取cookies函数
{
    var arr = document.cookie.match(new RegExp("(^| )"+name+"=([^;]*)(;|$)"));
    if(arr != null) return unescape(arr[2]);
    return null;
}