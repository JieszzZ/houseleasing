<template>
    <div class="QR-code-scanner">
        <video ref="video"></video>
        <canvas :class="[showCanvas?'':'hidden']" ref="canvas"></canvas>
        <a href="javascript:void(0)" @click="$emit('close')" class="button-close"><i class="el-icon-close"/></a>
    </div>
</template>

<script>
    import jsQR from "jsqr"

    let scannerTickId = null

    export default {
        name: "QRCodeScanner",

        data() {
            return {
                video: null,
                canvas: null,

                showCanvas: false,

            }
        },

        async mounted() {

            let video = this.video = this.$refs.video,
                canvas = this.canvas = this.$refs.canvas

            let stream = await navigator.mediaDevices.getUserMedia({video: {facingMode: "environment"}})

            video.srcObject = stream
            await new Promise(resolve => {
                video.onloadedmetadata = () => {
                    resolve()
                    console.log(stream)
                    video.play()
                    canvas.width = video.videoWidth
                    canvas.height = video.videoHeight
                }
            })

            scannerTickId = requestAnimationFrame(this.scanner)


        },

        beforeDestroy() {
            cancelAnimationFrame(scannerTickId)
        },

        methods: {
            highlightArea: function () {

                let delayTimeoutId = null

                return function (x, y, width, height) {


                    let canvas = this.canvas,
                        cc = this.canvas.getContext('2d')

                    cc.clearRect(0, 0, canvas.width, canvas.height)
                    cc.strokeStyle = '#FF3B58'
                    cc.lineWidth = 10
                    cc.strokeRect(x, y, width, height)

                    this.showCanvas = true

                    clearTimeout(delayTimeoutId)
                    setTimeout(() => {
                        this.showCanvas = false
                    }, 500)

                }
            }(),
            scanner() {

                let video = this.video,
                    canvas = this.canvas,
                    cc = canvas.getContext('2d')

                cc.drawImage(video, 0, 0, canvas.width, canvas.height);
                let imageData = cc.getImageData(0, 0, canvas.width, canvas.height);
                let code = jsQR(imageData.data, imageData.width, imageData.height);
                if (code) {
                    let {x, y} = code.location.topLeftCorner,
                        width = code.location.topRightCorner.x - code.location.topLeftCorner.x,
                        height = code.location.bottomLeftCorner.y - code.location.topLeftCorner.y

                    this.highlightArea(x, y, width, height)
                    this.$emit('detected', code.data)
                }
                scannerTickId = requestAnimationFrame(this.scanner);

            }
        }
    }
</script>

<style scoped lang="scss">

    .QR-code-scanner {

        width: 100%;

        position: fixed;

        left: 50%;
        top: 50%;
        transform: translate(-50%, -50%);
        max-width: 500px;

        canvas {
            position: absolute;
            left: 0;
            top: 0;
        }
        video {
            position: relative;
        }
        video, canvas {
            width: 100%;
            height: 100%;
            object-fit: contain;
        }

        canvas {
            transition-duration: 2s;
            transition-property: opacity;
            opacity: 1;

            &.hidden {
                opacity: 0;
            }
        }

        .button-close {
            display: inline-block;
            font-size: 30px;
            background-color: rgba(50, 50, 50, .5);
            position: absolute;
            top: 100%;
            right: 0;
        }
    }

</style>