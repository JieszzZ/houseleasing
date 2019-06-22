import {handleResponse, server} from "../utils/http";
import qs from 'qs'

/***
 * 获取当日签到情况
 * @return {PromiseLike<T> | Promise<T>}
 */
export function getCheckInfoToday() {
    return server.get('check/status').then(handleResponse)
}

/***
 * 签到
 * @param id  要签到的群体的口令
 * @param bundle 附加信息  比如位置信息
 * @return {Promise<T>}
 */
export function check(id, form) {
    form.append('id', id)
    return server.post('check/check', form).then(handleResponse)
}

// 开启、关闭签到
export function enableCheck(id) {
    return server.post('check/enable', qs.stringify({id})).then(handleResponse)
}

export function disableCheck(id) {
    return server.post('check/disable', qs.stringify({id})).then(handleResponse)
}