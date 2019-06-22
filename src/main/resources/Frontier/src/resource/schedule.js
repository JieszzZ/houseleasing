import {handleResponse, server} from "../utils/http";
import qs from 'qs'

export function getAllSchedules(id) {
    return server.get('schedule', {params: {id}}).then(handleResponse)
}

export function addSchedule(id, bundle) {
    return server.post('schedule/add', qs.stringify(Object.assign({id}, bundle))).then(handleResponse)
}

export function updateSchedule(scheduleId, bundle) {
    return server.post('schedule/update', qs.stringify(Object.assign({scheduleId}, bundle))).then(handleResponse)
}

export function deleteSchedule(scheduleId) {
    return server.post('schedule/delete', qs.stringify({scheduleId})).then(handleResponse)
}

