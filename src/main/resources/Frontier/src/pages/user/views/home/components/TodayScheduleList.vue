<template>
    <div class="today-schedule-table">
        <my-title class="title" style="color: white;">今日签到</my-title>
        <el-collapse v-model="activeNames" style="overflow: hidden">
            <el-collapse-item title="错过签到" name="missed">
                <ul v-if="missedList.length!==0">
                    <li v-for="item in missedList" :key="Math.random()">
                        <schedule type="missed" v-bind="item"/>
                    </li>
                </ul>
                <div v-else class="no-data">暂无数据</div>
            </el-collapse-item>
            <el-collapse-item title="成功签到" name="done">
                <ul v-if="doneList.length!==0">
                    <li v-for="item in doneList" :key="Math.random()">
                        <schedule type="done" v-bind="item"/>
                    </li>
                </ul>
                <div v-else class="no-data">暂无数据</div>
            </el-collapse-item>
            <el-collapse-item title="正在签到" name="open">
                <ul v-if="openList.length!==0">
                    <li v-for="item in openList" :key="Math.random()">
                        <schedule type="open" v-bind="item"/>
                    </li>
                </ul>
                <div v-else class="no-data">暂无数据</div>
            </el-collapse-item>
            <el-collapse-item title="即将开启" name="future">
                <ul v-if="futureList.length!==0">
                    <li v-for="item in futureList" :key="Math.random()">
                        <schedule type="future" v-bind="item"/>
                    </li>
                </ul>
                <div v-else class="no-data">暂无数据</div>
            </el-collapse-item>
        </el-collapse>
    </div>
</template>

<script>
    import {getCheckInfoToday} from "../../../../../resource/check";
    import Schedule from "./Schedule";
    import MyTitle from "./MyTitle";

    export default {
        name: "TodayScheduleList",
        components: {MyTitle, Schedule},
        created() {
            getCheckInfoToday().then(res => {
                this.doneList = res.done
                this.missedList = res.missed
                this.openList = res.open
                this.futureList = res.future
            })
        },
        data() {
            return {
                doneList: [],
                missedList: [],
                openList: [],
                futureList: [],

                activeNames: ['open']
            }
        }
    }
</script>

<style scoped lang="scss">
    .today-schedule-table {
        .no-data {
            text-align: center;
            color: gray;
        }
    }
</style>