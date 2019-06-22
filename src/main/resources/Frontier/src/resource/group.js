import {handleResponse, server} from "../utils/http";
import qs from 'qs'

export function getGroupInfo(id) {
    return server.get('group', {params: {id}}).then(handleResponse)
}

export function getGroupInfoList() {
    return server.get('group/list').then(handleResponse)
}

export function createGroup(name) {
    return server.post('group/add', qs.stringify({name})).then(handleResponse)
}



export function joinGroup(id) {
    return server.post('group/join', qs.stringify({id})).then(handleResponse)
}

export function quitGroup(id) {
    return server.post('group/quit', qs.stringify({id})).then(handleResponse)
}

export function updateGroupInfo(id, bundle) {
    return server.post('group/update', qs.stringify(Object.assign({id}, bundle))).then(handleResponse)
}

export function deleteGroup(id) {
    return server.post('group/delete', qs.stringify({id})).then(handleResponse)
}
