<template>
    <div style="">
        <div id='container' style="height: 250px"></div>
        <!--<div id="tip" style="height: 100px"></div>-->
        <!--<el-button>确定</el-button>-->
    </div>
</template>

<script>
    export default {
        name: "SetPosition",
        props:["lngProp","latProp"],

        data(){
            return  {
                jing:null,
                wei:null,
                intervalindex:null,
                marker:null,
                map:null,
                counter:0,
                change_jw_counter:0,

                lngCenter:null,
                latCenter:null,
                zoom:null
            }
        },
        created(){
          if (this.latProp==null||this.lngProp==null){

              this.lngCenter = 117.138302;
              this.latCenter = 36.666758;
              this.zoom=15
          }else {

              this.lngCenter = this.lngProp;
              this.latCenter = this.latProp;
              this.zoom=15
          }
        },
        mounted(){
          this.update()
        },
        watch:{
            // counter:function(oldval,newval) {
            //     if (newval>=3){
            //         clearInterval(this.intervalindex)
            //     }
            // }
        },
        methods:{

             update(){
                var that =this

                var geolocation;
                //加载地图，调用浏览器定位服务
                that.marker,that.map = new AMap.Map('container', {
                    resizeEnable: true,

                    center: [that.lngCenter,that.latCenter],
                    zoom:that.zoom
                });

                //点击事件
                 //为地图注册click事件获取鼠标点击出的经纬度坐标
                 var clickEventListener = that.map.on('click', function(e) {


                    that.jing=e.lnglat.getLng()
                    that.wei = e.lnglat.getLat()

                     that.clearmarker()
                     that.addMarker()
                     //触发事件 向父组件传送经纬度
                     that.sendLocationParent()
                     // console.log(that.jing+','+that.wei)
                 });
                 //

                that.map.plugin('AMap.Geolocation', function() {
                    geolocation = new AMap.Geolocation({
                        enableHighAccuracy: true,//是否使用高精度定位，默认:true
                        timeout: 10000,          //超过10秒后停止定位，默认：无穷大
                        buttonOffset: new AMap.Pixel(10, 20),//定位按钮与设置的停靠位置的偏移量，默认：Pixel(10, 20)
                        zoomToAccuracy: true,      //定位成功后调整地图视野范围使定位位置及精度范围视野内可见，默认：false
                        buttonPosition:'RB',
                        GeoLocationFirst:true
                    });


                        that.map.addControl(geolocation);
                    // that.intervalindex = setInterval(()=>{
                    //     geolocation.getCurrentPosition()
                    //     that.counter = that.counter+1;
                    //     console.log("在县城里")
                    // },1000)
                    if (that.lngProp==null&&that.latProp==null){
                        // console.log("abc")
                        geolocation.getCurrentPosition()
                    }else if (that.lngProp==undefined&&that.latProp==undefined){
                        // console.log(that.lngProp)
                        console.log("没收到prop")
                    }else {
                        //传过来了经纬度
                        // console.log(that.lngProp)
                        // console.log(that.latProp)
                        that.jing = that.lngProp
                        that.wei = that.latProp
                        that.clearmarker()
                        that.addMarker()
                        // console.log("画图")
                        that.sendLocationParent()
                    }

                    console.log("已经开始加载地图了")
                    AMap.event.addListener(geolocation, 'complete',that.onComplete);//返回定位信息
                    AMap.event.addListener(geolocation, 'error',that.onError);      //返回定位出错信息

                });
            },
            onComplete(data) {
                var str=['定位成功'];
                str.push('经度：' + data.position.getLng());
                str.push('纬度：' + data.position.getLat());
                // if (this.intervalindex!=0){
                //     clearInterval(this.intervalindex)
                //     this.intervalindex=0

                    this.jing = data.position.getLng()
                    this.wei = data.position.getLat()
                    this.clearmarker()
                    this.addMarker()
                    this.sendLocationParent()
                // }
                if(data.accuracy){
                    str.push('精度：' + data.accuracy + ' 米');
                }//如为IP精确定位结果则没有精度信息
                str.push('是否经过偏移：' + (data.isConverted ? '是' : '否'));
                // document.getElementById('tip').innerHTML = str.join('<br>');
            },
    //解析定位错误信息
            onError(data) {
                 console.log(JSON.stringify(data));
                // document.getElementById('tip').innerHTML = '定位失败';
            },
            addMarker() {
                 this.marker = new AMap.Marker({
                    icon: "https://webapi.amap.com/theme/v1.3/markers/n/mark_b.png",
                    position: [this.jing, this.wei]
                });
                this.marker.setMap(this.map);
            },
            clearmarker(){
                if (this.marker) {
                    this.marker.setMap(null);
                    this.marker = null;
                }
            },

            sendLocationParent(){
                 this.$emit("listenToChildLocationEvent",this.jing,this.wei)
            }

        }
    }

</script>

<style scoped>

    .marker {
        color: #ff6600;
        padding: 4px 10px;
        border: 1px solid #fff;
        white-space: nowrap;
        font-size: 12px;
        font-family: "";
        background-color: #0066ff;
    }

    /*#container {*/
        /*position: absolute;*/
        /*top: 0;*/
        /*left: 0;*/
        /*right: 0;*/
        /*bottom: 0;*/
        /*width: 100%;*/
        /*height: 100%;*/
    /*}*/
    #tip {
        background-color: #fff;
        /*padding-left: 10px;*/
        /*padding-right: 10px;*/
        /*position: relative;*/
        /*font-size: 12px;*/
        /*right: 10px;*/
        /*top: 20px;*/
        /*border-radius: 3px;*/
        /*border: 1px solid #ccc;*/
        /*line-height: 30px;*/
    }


</style>