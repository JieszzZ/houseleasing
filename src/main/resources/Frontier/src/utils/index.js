// 工具类

export const toFirstUpperCase = s => s.slice(0, 1).toUpperCase().concat(s.slice(1))
export const toFirstLowerCase = s => s.slice(0, 1).toLowerCase().concat(s.slice(1))

// 按指定格式解析date
export function parseDate(str, fmt) {
    fmt = fmt || 'yyyy-MM-dd';
    var obj = {y: 0, M: 1, d: 0, H: 0, h: 0, m: 0, s: 0, S: 0};
    fmt.replace(/([^yMdHmsS]*?)(([yMdHmsS])\3*)([^yMdHmsS]*?)/g, function (m, $1, $2, $3, $4, idx, old) {
        str = str.replace(new RegExp($1 + '(\\d{' + $2.length + '})' + $4), function (_m, _$1) {
            obj[$3] = parseInt(_$1);
            return '';
        });
        return '';
    });
    obj.M--; // 月份是从0开始的，所以要减去1
    var date = new Date(obj.y, obj.M, obj.d, obj.H, obj.m, obj.s);
    if (obj.S !== 0) date.setMilliseconds(obj.S); // 如果设置了毫秒
    return date;
}

// time ms之后resolve的Promise
export function timeout(data, time) {
    return new Promise(resolve => {
        setTimeout(() => {
            resolve(data)
        }, time)
    })
}

export function wait(ms) {
    return new Promise(resolve => {
        setTimeout(() => {
            resolve()
        }, ms)
    })
}

export function getCurrentPosition() {
    return new Promise((resolve, reject) => {

        AMap.plugin('AMap.Geolocation', function () {
            var geolocation = new AMap.Geolocation({
                // 是否使用高精度定位，默认：true
                enableHighAccuracy: true,
                GeoLocationFirst: true
            })

            geolocation.getCurrentPosition()
            let firstInvokingTime = Date.now(),//首次调用时间
                maxWaitingSeconds = 5//最长等待时间

            // 低精度的也会保存  但会重复多次以获得更好的结果
            let result = null

            AMap.event.addListener(geolocation, 'complete', onComplete)
            AMap.event.addListener(geolocation, 'error', onError)

            function onComplete(data) {

                if (!result || data.accuracy < result.accuracy) result = data

                if (Date.now() - firstInvokingTime < maxWaitingSeconds * 1000) {
                    geolocation.getCurrentPosition()
                } else {
                    resolve({lng: result.position.lng, lat: result.position.lat})
                }
            }

            function onError(data) {

                // 定位出错
                console.log(`定位失败  ${Date.now()} ${data}`)

                reject(data)

            }
        })
    })
}

export function compressImage(image, boxConstraints = {width: 500, height: 500}) {


    let width = image.width,
        height = image.height
    let rate = width/height,
        cRate = boxConstraints.width/boxConstraints.height

    let rWidth = rate>cRate?boxConstraints.width:boxConstraints.height*rate,
        rHeight = rate<cRate?boxConstraints.height:boxConstraints.width/rate

    let canvas = document.createElement('canvas'),
        cc = canvas.getContext('2d')

    canvas.width = rWidth
    canvas.height = rHeight

    cc.drawImage(image, 0, 0, rWidth,rHeight)


    return new Promise(resolve => {

        canvas.toBlob(function (blob) {
            let file = new File([blob], `${Date.now()}.jpg`)
            resolve(file)
        }, "image/jpeg")
    })

}

export async function captureImageFromVideo(video, options = {}) {
    let canvas = document.createElement("canvas");

    let boxConstraint = options.boxConstraint
    if (boxConstraint) {

        if (typeof boxConstraint === "boolean" || boxConstraint instanceof Boolean)
            boxConstraint = {
                width: 200,
                height: 200
            }
        else
            boxConstraint = {
                width: boxConstraint.width,
                height: boxConstraint.height
            }

    } else {
        boxConstraint = {width: video.videoWidth, height: video.videoHeight}
    }

    if (video.videoHeight / video.videoWidth > boxConstraint.height / boxConstraint.width) {
        // 较高
        canvas.height = boxConstraint.height
        canvas.width = canvas.height * video.videoWidth / video.videoHeight
    } else {
        canvas.width = boxConstraint.width
        canvas.height = canvas.width * video.videoHeight / video.videoWidth
    }

    canvas.getContext('2d').drawImage(video, 0, 0, canvas.width, canvas.height);
    let blob = await new Promise(resolve => canvas.toBlob(blob => {
        resolve(blob)
    }, "image/jpeg"))
    return new File([blob], `video_capture_${Date.now()}`, {lastModified: Date.now()})
};