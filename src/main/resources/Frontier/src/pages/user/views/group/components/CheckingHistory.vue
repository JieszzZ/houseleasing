<template>
    <el-table
            class="checking-history-table"
            :data="historyData"
            v-loading="loading"
            style="width: 100%"
            :row-class-name="tableRowClassName">
        <el-table-column
                align="center"
                label="时间">
            <template slot-scope="scope">{{ scope.row.startUpDateTime | dateFormat }}</template>
        </el-table-column>
        <el-table-column
                align="center"
                label="状态">
            <template slot-scope="scope">{{ scope.row.checked?'出勤':'缺勤' }}</template>
        </el-table-column>
    </el-table>
</template>

<script>
    import {getGroupHistory} from "@/resource/history";
    import moment from 'moment'

    export default {
        name: "CheckingHistory",
        async created(){
            console.log(moment('2018-11-26T21:22:38Z').locale('zh-cn').format("M/D, HH:mm, dddd"))
            this.loading = true
            try {
                this.historyData = await getGroupHistory(this.id)
            } finally {
                this.loading = false
            }
        },
        props:{
            id:{
                type:String,
                required:true
            }
        },
        data(){
            return{
                historyData:[],
                loading:true
            }
        },
        methods:{
            tableRowClassName({row, rowIndex}) {
                if (row.checked) {
                    return 'record-done'
                } else {
                    return 'record-missed'
                }
            }
        },
        filters: {
            dateFormat: function (value) {
                return moment(value).locale('zh-cn').format("M/D, HH:mm, dddd")
            }
        }
    }
</script>

<style lang="scss">
    .checking-history-table{
        .record-{
            &missed{
                background-color: #fdd4d4;
            }
            &done{
                background-color: rgba(162, 252, 152, 0.31);
            }
        }
    }
</style>