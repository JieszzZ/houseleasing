// google face detector api的封装
import {captureImageFromVideo} from "@/utils/index";

export class FaceDetector {
    constructor(videoElement) {
        this.video = videoElement
        this.faceDetector = new window.FaceDetector
    }

    async detect() {
        const boxConstraint = {
            width: 320,
            height: 140
        }


        const imageFile = this.currentDetectingImageFile = await captureImageFromVideo(this.video, {
            boxConstraint
        })
        const rate = Math.min(boxConstraint.width / this.video.videoWidth, boxConstraint.height / this.video.videoHeight)
        const realSize = {
            width: this.video.videoWidth * rate,
            height: this.video.videoHeight * rate
        }
        const image = await new Promise(async resolve => {
            let image = new Image()
            image.src = URL.createObjectURL(imageFile)
            image.onload = function () {
                resolve(image)
            }
        })

        try {
            this.currentDetectingImageFile = imageFile
            let faces = await this.faceDetector.detect(image)
            
            // 标准化
            return faces.map(face => ({
                x: face.boundingBox.left / realSize.width,
                y: face.boundingBox.top / realSize.height,
                width: face.boundingBox.width / realSize.width,
                height: face.boundingBox.height / realSize.height
            }))
            
        } catch (e) {
            console.error(e);
        }

    }

    static support() {
        
        //TODO 暂时不可用  所以返回false
        return false
        // return !!window.FaceDetector
    }
}