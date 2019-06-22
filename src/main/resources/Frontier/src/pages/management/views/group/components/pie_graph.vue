<template>
    <div id="pie" style="height: 200px;width: 200px">
    </div>
</template>

<script>
    var $ = require('jquery');


    export default {
        name: "pie_graph",
        props:[
            "data_people"
        ],
        mounted(){
            this.update();
        },
        watch:{
            data_people:function (val,oldval) {
                // console.log("修改了修改了修改了")
                this.update()
            }
        },
        methods:{
            update(){
                console.log("123")
                var that = this;
                var
                    d1 = [[0, that.data_people.come]],
                    d2 = [[0, that.data_people.leavea]],
                    d3 = [[0, that.data_people.notcome]],

                    graph;
                graph = Flotr.draw(
                    document.getElementById("pie"),
                    [
                        { data : d1, label : '出勤' },
                        { data : d2, label : '请假'
                        },
                        { data : d3, label : '缺勤'},
                    ]
                    , {
                        HtmlText: false,
                        grid: {
                            verticalLines: false,
                            horizontalLines: false,
                            outlineWidth: 0, //去除Grid的边框
                        },
                        xaxis: {showLabels: false},
                        yaxis: {showLabels: false},
                        pie: {
                            show: true,
                            explode: 6
                        },
                        mouse: {track: true},
                        legend: {
                            position: 'se',
                            backgroundColor: '#D2E8FF'
                        }
                    }

                )
            }
        }
    }
</script>

<style scoped>

</style>