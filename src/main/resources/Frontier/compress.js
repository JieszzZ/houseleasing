// 压缩dist
var exec = require('child_process').exec;
var cmdStr = '"C:\\Program Files\\WinRAR\\rar" a ../dist.rar ./dist';
exec(cmdStr, function(err,stdout,stderr){
    if(err) {
        console.log(err);
    }else {
        console.log('success compressed in ../')
    }
});