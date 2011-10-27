/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


var schedulerRecordType = [
{
    name : 'id',
    type : 'long'
},
{
    name : 'startURLId',
    type : 'string'
},
{
    name : 'jobId',
    type : 'string'
},
{
    name : 'schedule',
    type : 'string'
},
{
    name : 'scheduleType',
    type : 'int'
},
{
    name : 'createTime',
    type : 'date'
}
];

var startURLRecordType = [
{
    name : 'id',
    type : 'long'
},
{
    name : 'siteId',
    type : 'string'
},
{
    name : 'startURL',
    type : 'string'
},
{
    name : 'jobname',
    type : 'string'
},
{
    name : 'status',
    type : 'long'
},
{
    name : 'createTime',
    type : 'date'
},
{
    name : 'createUser',
    type : 'string'
},
{
    name : 'updateTime',
    type : 'date'
},
{
    name : 'lastUpdateUser',
    type : 'string'
}
];

var jobRecordType =[
{
    name : 'id',
    type : 'long'
},
{
    name : 'jobType',
    type : 'string'
},
{
    name : 'configure',
    type : 'string'
},
{
    name : 'wrapperId',
    type : 'long'
},
{
    name : 'createTime',
    type : 'date'
},
{
    name : 'createUser',
    type : 'string'
},
{
    name : 'updateTime',
    type : 'date'
},
{
    name : 'lastUpdateUser',
    type : 'string'
}
];

var wrapperRecordType = [
{
    name : 'id',
    type : 'long'
},
{
    name : 'application',
    type : 'string'
},
{
    name : 'name',
    type : 'string'
},
{
    name : 'wrapperType',
    type : 'string'
},
{
    name : 'visualConfig',
    type : 'string'
},
{
    name : 'createTime',
    type : 'date'
},
{
    name : 'createUser',
    type : 'string'
},
{
    name : 'updateTime',
    type : 'date'
},
{
    name : 'lastUpdateUser',
    type : 'string'
}
];


var siteRecordType = [
{
    name : 'id',
    type : 'long'
},
{
    name : 'siteId',
    type : 'string'
},
{
    name : 'siteName',
    type : 'string'
},
{
    name : 'siteDomain',
    type : 'string'
},
{
    name : 'siteType',
    type : 'string'
},
{
    name : 'parentId',
    type : 'long'
},
{
    name : 'url',
    type : 'string'
},
{
    name : 'createTime',
    type : 'date'
},
{
    name : 'createUser',
    type : 'string'
},
{
    name : 'updateTime',
    type : 'date'
},
{
    name : 'lastUpdateUser',
    type : 'string'
}
];

var keywordRecordType = [
{
    name : 'id',
    type : 'long'
},
{
    name : 'keywords',
    type : 'string'
},
{
    name : 'referrer',
    type : 'string'
},
{
    name : 'status',
    type : 'int'
},
{
    name : 'createTime',
    type : 'date'
},
{
    name : 'createUser',
    type : 'string'
},
{
    name : 'updateTime',
    type : 'date'
},
{
    name : 'lastUpdateUser',
    type : 'string'
}
];


var weiboRecordType = [
{
    name : 'id',
    type : 'long'
},
{
    name : 'userId',
    type : 'string'
},
{
    name : 'siteId',
    type : 'string'
},
{
    name : 'passwd',
    type : 'string'
},
{
    name : 'status',
    type : 'int'
},
{
    name : 'createTime',
    type : 'date'
},
{
    name : 'createUser',
    type : 'string'
},
{
    name : 'updateTime',
    type : 'date'
},
{
    name : 'lastUpdateUser',
    type : 'string'
}
];


var concernedWeiboRecordType = [
{
    name : 'id',
    type : 'long'
},
{
    name : 'name',
    type : 'string'
},
{
    name : 'objectType',
    type : 'int'
},
{
    name : 'title',
    type : 'string'
},
{
    name : 'siteId',
    type : 'string'
},
{
    name : 'weiboURL',
    type : 'string'
},
{
    name : 'org',
    type : 'string'
},
{
    name : 'fans',
    type : 'int'
},
{
    name : 'follow',
    type : 'int'
},
{
    name : 'weibo',
    type : 'int'
},
{
    name : 'notes',
    type : 'string'
},
{
    name : 'certification',
    type : 'string'
},
{
    name : 'createTime',
    type : 'date'
},
{
    name : 'createUser',
    type : 'string'
},
{
    name : 'updateTime',
    type : 'date'
},
{
    name : 'lastUpdateUser',
    type : 'string'
}
];

var followRecordType = [
{
    name : 'id',
    type : 'long'
},
{
    name : 'wid',
    type : 'long'
},
{
    name : 'cwid',
    type : 'long'
},
{
    name : 'weibo',
    type : 'string'
},
{
    name : 'concernedWeibo',
    type : 'string'
},
{
    name : 'status',
    type : 'int'
},
{
    name : 'createTime',
    type : 'date'
}
];