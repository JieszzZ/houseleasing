<template>
    <div class="face-capture container">
        <video width="320" height="240" ref="videoDisplayer" autoplay></video>
        <canvas width="320" height="240" ref="canvasDisplayer"></canvas>
        <el-button @click="$emit('close')" type="danger" icon="el-icon-close" circle class="button-close"></el-button>
    </div>
</template>

<style scoped lang="scss">


    /*.container {

        video, canvas {
            object-fit: contain;
            position: absolute;
            left: 50%;
            transform: translateX(-50%);
            top: 0;
        }

        position: fixed;
        left: 0;
        width: 100%;
        height: 240px;
        top: 25%;
    }*/
    .container {

        video {
            position: relative;
        }

        canvas{
            position: absolute;
            left: 50%;
            transform: translateX(-50%);
            top: 0;
        }
        
        .button-close{
            position: absolute;
            right: 0;
            top: 0;
        }
        
        position: fixed;
        left: 50%;
        top: 25%;
        transform: translateX(-50%);
    }
</style>

<script>
    import {captureImageFromVideo} from "../../../../../utils";
    import '@/lib/tracking.js'
    import '@/lib/data/face.js'

    let mounted = null


    // 利用clmtrackr和百度ai人脸识别来识别一张高质量的照片
    export default {
        name: "FaceCapture",
        data() {

            return {

                tracker: null,
                trackerTask: null
            }

        },
        beforeDestroy() {
            this.stopTracking()
        },
        methods: {


            async getNormalFrame() {

                let video = this.$refs.videoDisplayer


                let tracker = this.tracker = new tracking.ObjectTracker('face')
                tracker.setInitialScale(4);
                tracker.setStepSize(2);
                tracker.setEdgesDensity(0.1);

                let trackerTask = this.trackerTask = tracking.track(video, tracker, {camera: true});
                this.tracker.on('track', this.paintingFunc)
                trackerTask.run()
                return await new Promise(resolve => {
                    // 获取一张图像

                    let hasPicture = false

                    let handler = async event => {

                        if (event.data.length !== 0 && !hasPicture) {
                            hasPicture = true
                            let img = await captureImageFromVideo(this.$refs.videoDisplayer, {boxConstraint: true})
                            this.stopTracking()
                            this.$emit('close')
                            resolve(img)
                        }
                    }

                    this.tracker.on('track', handler)
                })
            },

            stopTracking() {

                this.tracker && this.tracker.removeAllListeners();
                this.trackerTask && this.trackerTask.stop()
                let video = this.$refs.videoDisplayer
                if (video.srcObject !== null) {
                    video.srcObject.getTracks().forEach(t => t.stop())
                    video.srcObject = null
                }
            },


            paintingFunc(event) {
                let canvas = this.$refs.canvasDisplayer,
                    cc = canvas.getContext('2d');
                cc.clearRect(0, 0, canvas.width, canvas.height);

                // let video = this.$refs.videoDisplayer


                // let normalize = rect => ({
                //     x: rect.x / video.videoWidth,
                //     y: rect.y / video.videoHeight,
                //     width: rect.width / video.videoWidth,
                //     height: rect.height / video.videoHeight,
                // })
                // let boundingBox = {
                //     width: 320,
                //     height: 240
                // }

                // cc.fillRect(boundingBox.width - 20, boundingBox.height - 20, 20, 20)


                event.data.forEach(rect => {
                    console.log(rect)
                    cc.strokeStyle = '#a64ceb';
                    cc.strokeRect(rect.x, rect.y, rect.width, rect.height)
                })
            },

        }
    }
</script>