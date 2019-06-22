// 监听正在签到的课程的变动

import {getCheckInfoToday} from "../../resource/check";

console.log('loaded')
let openCheckList = []

// 请求间隔 /s
const REQUEST_DURATION_SECONDS = 2

async function update() {

    let hasJustOpenedCheckList = []
    try {
        let res = null
        res = await getCheckInfoToday()
        let latestData =  res.open

        // 比对latestData 与 openCheckList的信息
        latestData.forEach(item => {

            if (!openCheckList.find(v => v.groupId === item.groupId)) {
                hasJustOpenedCheckList.push(item)
            }

        })
        openCheckList = latestData
    } catch (e) {
        console.log(e)
    }


    if (hasJustOpenedCheckList.length > 0) {
        console.log('new data')
        // 对新增的签到信息发起通知
        postMessage(hasJustOpenedCheckList)
    }else {
        console.log('no new data')
    }

    setTimeout(update, REQUEST_DURATION_SECONDS * 1000)

}

getCheckInfoToday().then(data => {

    openCheckList = data.open

    setTimeout(update, REQUEST_DURATION_SECONDS)

})