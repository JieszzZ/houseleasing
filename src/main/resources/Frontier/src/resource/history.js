import {handleResponse, server} from "../utils/http";
import qs from 'qs'



//获取群体内某个成员的签到历史记录
export function getUserHistory(group_id,username) {

    return server.get('group/'+group_id+'/user/'+username).then(handleResponse)
};


//获取某群体的历史记录
export function getGroupHistory(group_id) {

    return server.get('history/'+group_id).then(handleResponse)
}

//获取某群体的历史每人签到数据统计信息
export function getEveryoneHistory(groupid) {
    // return [
    //     {
    //         "username":"username",
    //         "name":"曲延松",
    //         "missed":15,
    //         "leave":3,
    //         "done":20,
    //         "done_percent":"98%"
    //     },
    //     {
    //         "username":"wenyanan",
    //         "name":"温雅楠",
    //         "missed":15,
    //         "leave":3,
    //         "done":20,
    //         "done_percent":"90%"
    //     },];
    return server.get('group/'+groupid+"/user/record").then(handleResponse)
}


//获取历史记录中的某条记录的信息
export function getRecord(recordId) {
    // return server.get('group', {params: {groupname,username}}).then(handleResponse)
    return server.get('record/'+recordId).then(handleResponse)
}


