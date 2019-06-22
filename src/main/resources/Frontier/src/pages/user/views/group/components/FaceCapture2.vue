<template>
    <div class="face-capture container">
        <video width="320" height="140" ref="videoDisplayer"></video>
        <canvas width="320" height="240" ref="canvasDisplayer"></canvas>
    </div>
</template>

<style scoped lang="scss">

    .face-capture {
    }

    .container {

        video, canvas {
            width: 100%;
            height: 100%;
            object-fit: contain;
            position: absolute;
            left: 0;
            top: 0;
        }

        /*position: relative;*/
        /*width: 320px;*/
        /*height: 240px;*/
        position: fixed;
        left: 0;
        width: 100%;
        height: 50%;
        top: 25%;
    }
</style>

<script>
    // 使用googleFaceCapture
    //!!!暂时不可用

    import {FaceDetector} from "@/utils/FaceDetector";

    let initPromise = null

    export default {
        name: "FaceCapture2",

        created() {
            if (!FaceDetector.support()) {
                throw new Error('当前浏览器不支持google face detector api')
            }
        },
        data() {
            return {
                faceDetector: null,
                detectorTimeoutId: null,
                lastDetectingTime: 0,
                videoArea: {
                    width: 320,
                    height: 240
                }
            }
        },
        mounted() {
            initPromise = new Promise(resolve => {
                this.faceDetector = new FaceDetector(this.$refs.videoDisplayer)
                resolve()
            })
        },
        beforeDestroy(){
            this.$refs.videoDisplayer.srcObject.getTracks().forEach(track=>track.stop())
            this.$refs.videoDisplayer.srcObject = null
        },
        methods: {

            async getNormalFrame() {

                await initPromise

                const stream = await navigator.mediaDevices.getUserMedia({video: true})
                const video = this.$refs.videoDisplayer,
                    canvas = this.$refs.canvasDisplayer,
                    ctx = canvas.getContext('2d')


                video.srcObject = stream

                await new Promise(resolve => {
                    video.onloadedmetadata = () => resolve()
                })

                video.play()

                return new Promise(resolve => {

                    let detectorTimeFunction = async () => {
                        try {
                            let faces = await this.faceDetector.detect()

                            ctx.clearRect(0, 0, canvas.width, canvas.height)

                            ctx.lineWidth = 2;
                            ctx.strokeStyle = 'red';
                            
                            for (let face in faces) {
                                let boundingBox = face
                                
                                ctx.strokeRect(Math.floor(boundingBox.x * this.videoArea.width),
                                    Math.floor(boundingBox.y * this.videoArea.height),
                                    Math.floor(boundingBox.width * this.videoArea.width),
                                    Math.floor(boundingBox.height * this.videoArea.height));
                                ctx.stroke();
                            }

                            if (faces.length > 0) {
                                resolve(this.faceDetector.currentDetectingImageFile)
                            } else {

                            let now = this.lastDetectingTime = Date.now()
                            this.detectorTimeoutId = setTimeout(detectorTimeFunction, Math.max(0, 300 - now + this.lastDetectingTime))

                            }
                        } catch (e) {
                            throw e
                        }

                    }

                    detectorTimeFunction()


                })


            },

        }
    }
</script>