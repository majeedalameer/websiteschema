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