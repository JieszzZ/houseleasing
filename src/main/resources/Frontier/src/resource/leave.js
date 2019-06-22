//有关请假的一切
import {handleResponse, server} from "../utils/http";
import qs from 'qs'

//获取群体内的请假请求
export function getLeaveRequest(group_id) {
    return server.get('leave', {params: {group_id}}).then(handleResponse)
}

//获取请假请求回馈
export function getAbsenceRequestFeedback(absenceId) {
    return server.get(`leave/${absenceId}/feedback`).then(handleResponse)
}

//学生发起请假
export function reportAbsence(group_id, result) {
    var form = new FormData()
    form.set('result',result)
    form.set('group_id', group_id)
    return server.post('group/leave', form).then(handleResponse)
}

//老师回复请假请求
export function responseToLeave(bundle) {
    return server.post('leave/response', qs.stringify(Object.assign(bundle))).then(handleResponse)
}

