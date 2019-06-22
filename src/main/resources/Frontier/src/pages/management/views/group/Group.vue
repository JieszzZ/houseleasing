<template>
    <div style="width: 100%;">
        <el-container id="GroupContainer">
            <el-header :style="note">
                <AppBar>
                    <template>{{this.name}}</template>
                </AppBar>
            </el-header>
            <el-main>
                <!--<el-button-->
                        <!--plain-->
                        <!--@click="open12">-->
                    <!--使用 HTML 片段-->
                <!--</el-button>-->

                <el-tabs v-model="activeName" @tab-click="handleListClick">
                    <el-tab-pane label="群体成员" name="first">
                        <p style="color: #606266;margin: 5px 0;">考勤次数:{{kaoqin_all}} 平均出勤率：{{avg_done_persent}}</p>

                        <el-table
                                :data="members"
                                style="width: 100%"
                                border
                        >
                            <el-table-column
                                    prop="name"
                                    label="姓名"
                            >
                            </el-table-column>

                            <el-table-column
                                    prop="done_percent"
                                    label="出勤率"
                            >
                            </el-table-column>
                            <el-table-column
                                    prop="missed"
                                    label="缺勤"
                            >
                            </el-table-column>
                            <el-table-column
                                    prop="leave"
                                    label="请假"
                            >
                            </el-table-column>
                            <el-table-column
                                    fixed="right"
                                    label="操作"
                                    width="60">
                                <template slot-scope="scope">
                                    <el-button @click="stu_message(scope.$index)" type="text" size="small">查看
                                    </el-button>
                                </template>
                            </el-table-column>
                        </el-table>
                        <div style="text-align: right;margin-top: 10px">
                            <downExcel :json-data="members" ></downExcel>
                        </div>
                        <el-dialog id="detailRecord" class="recordDiv" style="margin-top: 0" title="详细签到情况"
                                   :visible.sync="perRecordDiv">
                            <el-table :data="perRecordData" :height="350" :row-class-name="perRecordState">
                                <el-table-column property="day" label="日期"></el-table-column>
                                <el-table-column property="time" label="开始时间"></el-table-column>
                                <el-table-column property="checked" fixed="right" width="50"
                                                 label="情况"></el-table-column>
                            </el-table>
                        </el-dialog>
                    </el-tab-pane>

                    <el-tab-pane label="计划列表" name="second">

                        <el-table
                                :row-class-name="tableRowClassName"
                                :data="mySchList"
                                border
                                style="width: 100%  ;   margin-bottom: 40px;">
                            <el-table-column
                                    prop="startUpTime"
                                    label="起始时间"
                            >
                            </el-table-column>
                            <el-table-column
                                    prop="duration"
                                    label="持续时间"
                            >
                            </el-table-column>
                            <el-table-column
                                    prop="repeat"
                                    label="重复"
                            >
                            </el-table-column>


                            <el-table-column
                                    fixed="right"
                                    label="操作"
                                    width="100">
                                <template slot-scope="scope">
                                    <el-button @click="deleteScheduleClick(scope.$index)" type="text" size="small">删除
                                    </el-button>
                                    <el-button type="text" size="small"
                                               @click.native.prevent="change_Schedule(scope.$index)">编辑
                                    </el-button>
                                </template>
                            </el-table-column>
                        </el-table>
                    </el-tab-pane>

                    <el-tab-pane label="历史签到" name="third">
                        <el-table
                                :data="historyScheduleList"
                                style="width: 100% ;    margin-bottom: 40px;"
                                border
                        >
                            <el-table-column
                                    prop="day"
                                    label="日期"
                            >
                            </el-table-column>
                            <el-table-column
                                    prop="time"
                                    label="时间"
                            >
                            </el-table-column>
                            <el-table-column
                                    fixed="right"
                                    label="操作"
                                    width="60">
                                <template slot-scope="scope">
                                    <el-button @click="recordMess(scope.$index)" type="text" size="small">查看</el-button>
                                </template>
                            </el-table-column>
                        </el-table>

                        <el-dialog class="recordDiv" title="详细签到情况" :visible.sync="RecordDiv">

                            <el-collapse v-model="Record_activeName">
                                <Pie_graph  style="margin: 0 auto;"  :data_people="data_people"></Pie_graph>

                                <el-collapse-item title="出勤人员" name="1">
                                    <el-table :data="RecordData.done" :row-class-name="recordBack_done">
                                        <el-table-column property="username" label="用户名"></el-table-column>
                                        <el-table-column property="name" label="姓名"></el-table-column>
                                        <!--<el-table-column property="checked" fixed="right" width="50" label="情况" ></el-table-column>-->
                                    </el-table>
                                </el-collapse-item>

                                <el-collapse-item title="缺勤人员" name="2">
                                    <el-table :data="RecordData.missed" :row-class-name="recordBack_missed">
                                        <el-table-column property="username" label="用户名"></el-table-column>
                                        <el-table-column property="name" label="姓名"></el-table-column>
                                        <!--<el-table-column property="checked" fixed="right" width="50" label="情况" ></el-table-column>-->
                                    </el-table>
                                </el-collapse-item>
                                <el-collapse-item title="请假人员" name="3">
                                    <el-table :data="RecordData.leave" :row-class-name="recordBack_leave">
                                        <el-table-column property="username" label="用户名"></el-table-column>
                                        <el-table-column property="name" label="姓名"></el-table-column>
                                        <!--<el-table-column property="checked" fixed="right" width="50" label="情况" ></el-table-column>-->
                                    </el-table>
                                </el-collapse-item>
                            </el-collapse>


                        </el-dialog>
                    </el-tab-pane>
                </el-tabs>


                <!--//以下为弹出框 新建签到计划-->
                <el-dialog title="设置签到计划" :visible.sync="dialogFormVisible">
                    <el-form :model="schedule_form">
                        <el-form-item label="开启时间" :label-width="formLabelWidth">
                            <el-time-picker type="fixed-time" format="HH:mm" value-format="HH:mm" placeholder="选择时间"
                                            v-model="schedule_form.startUpTime" style="width: 100%;"></el-time-picker>
                        </el-form-item>
                        <el-form-item label="持续时间" :label-width="formLabelWidth">
                            <el-col :span="8">
                                <el-input v-model="schedule_form.duration"></el-input>
                            </el-col>

                            <el-col :span="2">
                                分
                            </el-col>
                        </el-form-item>

                        <el-form-item label="重复" :label-width="formLabelWidth">
                            <el-select v-model="rep" multiple placeholder="请选择">
                                <el-option
                                        v-for="item in options"
                                        :key="item.value"
                                        :label="item.label"
                                        :value="item.value"
                                >
                                </el-option>
                                <!--{{rep}}-->
                            </el-select>
                        </el-form-item>
                        <el-form-item label="是否启用" :label-width="formLabelWidth">
                            <el-switch
                                    v-model="schedule_form.enable"
                                    active-color="#13ce66"
                                    inactive-color="#ff4949">
                            </el-switch>
                        </el-form-item>
                    </el-form>
                    <div slot="footer" class="dialog-footer">
                        <el-button @click="dialogFormVisible = false">取 消</el-button>
                        <el-button type="primary" @click="submit_schedule">确 定</el-button>
                    </div>
                </el-dialog>


                <!--弹出框  修改签到计划-->
                <el-dialog title="修改签到计划" :visible.sync="dialogChangeFormVisible">

                    <el-form :model="schedule_change_form">
                        <el-form-item label="开启时间" :label-width="formLabelWidth">
                            <el-time-picker type="fixed-time" format="HH:mm" value-format="HH:mm" placeholder="选择时间"
                                            v-model="schedule_change_form.startUpTime"
                                            style="width: 100%;"></el-time-picker>
                        </el-form-item>
                        <el-form-item label="持续时间" :label-width="formLabelWidth">
                            <el-col :span="8">
                                <el-input v-model="schedule_change_form.duration"></el-input>
                            </el-col>

                            <el-col :span="2">
                                分
                            </el-col>
                        </el-form-item>

                        <el-form-item label="重复" :label-width="formLabelWidth">
                            <el-select v-model="change_arr_repeat" multiple placeholder="请选择">
                                <el-option
                                        v-for="item in options"
                                        :key="item.value"
                                        :label="item.label"
                                        :value="item.value"
                                >
                                </el-option>
                                <!--{{change_arr_repeat}}-->
                            </el-select>
                        </el-form-item>
                        <el-form-item label="是否启用" :label-width="formLabelWidth">
                            <el-switch
                                    v-model="schedule_change_form.enable"
                                    active-color="#13ce66"
                                    inactive-color="#ff4949">
                            </el-switch>
                        </el-form-item>
                    </el-form>
                    <div slot="footer" class="dialog-footer">
                        <el-button @click="dialogChangeFormVisible = false">取 消</el-button>
                        <el-button type="primary" @click="submit_Changeschedule">确 定</el-button>
                    </div>
                </el-dialog>


                <!--弹出框 编辑群体-->
                <el-dialog title="编辑群体" :visible.sync="dialogEditGroup">

                    <el-form ref="group_Editform" :model="group_Editform"
                             :rules="{positionCorrect: [{required: true, message: '请在图中标记中心点',trigger:'change'}]}">
                        <el-form-item label="群体名称" :label-width="formLabelWidth">
                            <el-input v-model="group_Editform.name"></el-input>
                        </el-form-item>


                        <el-form-item label="人脸识别" :label-width="formLabelWidth">
                            <el-switch
                                    v-model="group_Editform.needFace"
                                    active-color="#13ce66"
                                    inactive-color="#ff4949"

                            >
                            </el-switch>
                        </el-form-item>
                        <el-form-item label="地理位置" :label-width="formLabelWidth">
                            <el-switch
                                    v-model="group_Editform.needLocation"
                                    active-color="#13ce66"
                                    inactive-color="#ff4949"

                            >
                            </el-switch>
                        </el-form-item>
                        <SetPosition
                                :lat-prop="son_prop.lat" :lng-prop="son_prop.lng"

                                v-bind:class="[this.group_Editform.needLocation?'Show':'NotShow']"
                                v-on:listenToChildLocationEvent="changeLoca"
                        ></SetPosition>


                        <!--:lat-prop="this.son_prop.lat" :lng-prop="this.son_prop.lng"-->

                        <el-form-item
                                v-bind:class="[this.group_Editform.needLocation?'Show':'NotShow']"
                                label="有效距离" :label-width="formLabelWidth">
                            <el-col :span="12">
                                <el-input
                                        v-model="group_Editform.effectiveDistance">
                                </el-input>
                            </el-col>
                            米
                        </el-form-item>
                        <el-form-item prop="positionCorrect">
                        </el-form-item>
                    </el-form>
                    <div slot="footer" class="dialog-footer">
                        <el-button @click="deleGroup_open" type="danger" style="width: 100%;margin: 20px 0">
                            删除群体
                        </el-button>
                        <el-button @click="dialogEditGroup = false">取 消</el-button>
                        <el-button type="primary" @click="submit_EditGroup">确 定</el-button>
                    </div>
                </el-dialog>
                <!--弹出框 显示签到信息-->
                <el-dialog title="请假请求" :visible.sync="dialogLeaveList">
                    <el-table :data="leave_list">
                        <el-table-column property="name" label="姓名" width="70"></el-table-column>
                        <el-table-column property="result" label="原因" width="100"></el-table-column>
                        <el-table-column
                                fixed="right"
                                label="操作"
                                width="100">
                            <template slot-scope="scope">
                                <el-button @click="submit_leaveAnswer(scope.$index,false)" type="text" size="small">拒绝</el-button>
                                <el-button @click="submit_leaveAnswer(scope.$index,true)" type="text" size="small">同意</el-button>

                            </template>
                        </el-table-column>
                    </el-table>
                    <!--<span slot="footer" class="dialog-footer">-->

                            <!--<el-button type="primary" @click="submit_leaveAnswer">确 定</el-button>-->
                        <!--</span>-->
                </el-dialog>
            </el-main>
        </el-container>

        <div class="footer">
            <el-button-group>
                <el-button @click='imme_sign' :type="!state?'primary':'danger'">
                    {{!state?'开始签到':'结束签到'}}
                </el-button>
                <!--<el-button @click="plan_sign" type="primary">计划签到</el-button>-->
                <el-button @click="plan_sign">添加计划</el-button>
                <el-button @click="editGroup">编辑群体</el-button>
            </el-button-group>
        </div>

    </div>
</template>


<script>




    import AppBar from "../../../../components/AppBar";
    import {getGroupInfo} from "../../../../resource/group";
    import {deleteGroup, updateGroupInfo} from "../../../../resource/group";
    import {enableCheck, disableCheck} from "../../../../resource/check";
    import {addSchedule, updateSchedule, getAllSchedules, deleteSchedule} from "../../../../resource/schedule";
    import SetPosition from "../../../../components/SetPosition"
    import {getUserHistory, getGroupHistory, getRecord,getEveryoneHistory} from "../../../../resource/history"
    import {getLeaveRequest,responseToLeave}from "../../../../resource/leave"
    import Pie_graph from "./components/pie_graph";
    import downExcel from "./components/downExcel";

    export default {
        name: "Group",
        components: {downExcel, Pie_graph, AppBar, SetPosition},


        data() {
            return {
                // test_list:[
                //     {
                //         leave_id:"abc",
                //     name:"曲延松",
                //         result:"上厕所"
                //     },
                //     {leave_id:"abc",name:"郭松岳",
                //         result:"上厕所"
                //     },
                //     {leave_id:"abc",name:"郭松岳",
                //         result:"上厕所"
                //     },
                // ],
                //当前群组名称
                name: '',
                owner: '',
                //当前群体是否正在签到  true 在签到中  false不在
                state: false,
                // id:this.$route.params.id,
                members: [],
                //本群体的id
                id: null,

                dialogFormVisible: false,
                formLabelWidth: '100px',
                dialogChangeFormVisible: false,
                dialogEditGroup: false,

                options: [
                    {
                        value: '1',
                        label: '周一'
                    }, {
                        value: '2',
                        label: '周二'
                    }, {
                        value: '3',
                        label: '周三'
                    }, {
                        value: '4',
                        label: '周四'
                    }, {
                        value: '5',
                        label: '周五'
                    }, {
                        value: '6',
                        label: '周六'
                    }, {
                        value: '7',
                        label: '周日'
                    }
                ],
                rep: [],
                schedule_form: {
                    //关联弹出创建签到计划表格
                    startUpTime: '',
                    duration: '',
                    repeat: '',
                    enable: true,
                },
                schedule_change_form: {
                    //关联弹出修改签到计划表格
                    startUpTime: '',
                    duration: null,
                    repeat: '',
                    enable: true,
                },
                schedule_final: {
                    //上传的最终计划
                    startUpTime: '',
                    duration: null,
                    repeat: '',
                    enable: false,
                },
                changeSchedule_final: {
                    //上传的  修改计划的数据
                    startUpTime: '',
                    duration: null,
                    repeat: '',
                    enable: false,
                },
                //标签栏的绑定元素
                activeName: 'second',
                Record_activeName: 1,
                mySchList: [],
                intervalindex: null,
                group_Editform: {
                    name: '操作系统',
                    // state:false,
                    needLocation: false,
                    needFace: false,
                    lng: null,
                    lat: null,
                    effectiveDistance: 500,

                    // 根据son_prop计算出
                    // positionCorrect: ''
                },
                //经度纬度

                stateOfList: false,
                //临时存放对应修改计划的数据
                change_arr_repeat: [],


                //给地图组件的属性的动态绑定对象
                son_prop: {
                    lat: null,
                    lng: null
                },
                perRecordDiv: false,
                perRecordData: [],

                historyScheduleList: [],

                RecordDiv: false,
                RecordData: {
                    id: null,
                    startUpDateTime: null,
                    duration: null,
                    done: [],
                    missed: [],
                    leave:[]
                },
                note: {
                    backgroundImage: "url(" + require("../../../../image/head3.png") + ")",
                    backgroundRepeat: "no-repeat",
                    height: '60px',
                    width: "100%",
                    backgroundSize: '100% 100%',
                    position: "relative",
                },
                kaoqin_all:0,
                data_people:{
                    come:0,
                    notcome:5,
                    leavea:0,
                },
                avg_done_persent:"",

                //轮询的interval_index
                group_mes_index:0,

                //获取请假请求
                leave_list:[],
                //回复请假请求
                request_list:[],
                dialogLeaveList:false,
                //用来让饼图重新渲染
                pie_show:true,
                downloadData:[],
                //通知
                notifynote:{}
            }
        },

        created() {
            this.id = this.$route.params.id;
            console.log(this.id)
            this.update();

            /**
             * 测试
             */

            // var temp=[]
            //
            // for (var i =0;i<this.test_list.length;i++){
            //     temp[i] = {}
            //     temp[i]["leave_id"]=this.test_list[i]["leave_id"];
            //     temp[i]["leave_response"] = false
            // }
            // this.request_list = temp
            // console.log(this.request_list)


            if (this.state==true){
                this.ReturnTheLeaveMessage()
            }

            //
        },
        beforeDestroy(){
            console.log("我要关闭了")
            clearInterval(this.group_mes_index)
        },
        watch: {
            '$route'(to, from) {
                // 对路由变化作出响应...
                // console.log("路由变化"+this.id)
                this.id = this.$route.params.id;
                // console.log("路由变化"+this.id)
                this.update();
            },
            'group_Editform.lng': {
                immediate: true,
                handler(v) {
                    let form = this.group_Editform
                    this.group_Editform.positionCorrect = (!form.needLocation || v) ? 'true' : ''
                }
            },
            'group_Editform.needLocation': {
                immediate: true,
                handler(v) {
                    this.group_Editform.positionCorrect = (!v || this.group_Editform.lng) ? 'true' : ''
                }
            },
            state:function (newval,oldval) {
                // console.log("新状态是" + newval);
                // console.log("旧状态是"+oldval);
                if (newval==true&&oldval==false){
                //    开启轮询
                    console.log("开启轮询了")
                    this.ReturnTheLeaveMessage()

                }else if (newval==false&&oldval==true){
                    clearInterval(this.group_mes_index);
                    console.log("关闭轮询了")
                    this.group_mes_index = 0
                    // console.log(this.$notify)

                    this.notifynote.close()

//                     var a=this.$notify({title:'一则想要关闭的通知'})
// //1s后关闭
//                     setTimeout(a.close,1000)
                }
            }
                
            


        },
        methods: {

            //对群体初始化
            update() {
                getGroupInfo(this.id).then(res => {
                    var that = this
                    if (!res) {
                        // group不存在
                        this.$alert('该群组不存在！', '服务器消息', {
                            confirmButtonText: '确定'
                        }).then(() => {
                            this.$router.go(-1)
                        })
                        return
                    }


                    this.name = res.name;
                    this.owner = res.owner;
                    this.state = res.state;

                    var that = this
                    //在这里获取 历史计划列表
                    getGroupHistory(this.id).then(res => {
                        var temp = res
                        for (var i = 0; i < temp.length; i++) {
                            temp[i].day = temp[i].startUpDateTime.slice(0, 10);
                            temp[i].time = temp[i].startUpDateTime.slice(11, 16);
                            if (temp[i].checked == true) {
                                temp[i].checked = '签到'
                            } else {
                                temp[i].checked = '已签'
                            }
                        }
                        this.historyScheduleList = temp
                        this.kaoqin_all = temp.length
                    })
                    //统计总共开启了几次考勤


                    //在这里对每个成员进行统计
                    getEveryoneHistory(this.id).then(respon => {
                        this.members = respon

                        var that = this;
                        var avg_donepercent = 0;
                        for (var i in that.members){
                            var tem = parseFloat(that.members[i]["done_percent"]);
                            avg_donepercent = avg_donepercent+tem
                        }
                        avg_donepercent = avg_donepercent/that.members.length;
                        this.avg_done_persent = avg_donepercent.toFixed(2)+"%";
                    })
                    // this.members=getEveryoneHistory(this.id)

                    // var temp = res.members
                    // // console.log(temp)
                    // for (var i = 0; i < temp.length; i++) {
                    //     console.log(i + " 这是一开始")
                    //     var allRecord = 0, misRecord = 0, index = 0;
                    //     // console.log(that.id+'sdfasdf')
                    //     getUserHistory(that.id, temp[i].username).then(response => {
                    //         console.log("正在加载")
                    //         for (var j = 0; j < response.length; j++) {
                    //             allRecord++;
                    //             if (response[j].checked == false) {
                    //                 misRecord++;
                    //             }
                    //         }
                    //         temp[index].allRecord = allRecord;
                    //         temp[index].misRecord = misRecord;
                    //         allRecord = 0;
                    //         misRecord = 0;
                    //         index++;
                    //
                    //         console.log(that)
                    //         if (index == temp.length) {
                    //             // console.log(that.stateOfList)
                    //             that.stateOfList = true
                    //         }
                    //     })
                    //     // this.members = ["你好"]
                    //     allRecord = 0, misRecord = 0, index = 0;
                    // }
                    //
                    // // console.log(this)
                    //
                    // this.intervalindex = setInterval(function () {
                    //     // console.log(that)
                    //     if (that.stateOfList == true) {
                    //         that.members = temp;
                    //
                    //         clearInterval(that.intervalindex)
                    //         console.log(that.members)
                    //     }
                    // }, 1000)


                    this.group_Editform.name = res.name;
                    this.group_Editform.needLocation = res.needLocation;
                    this.group_Editform.needFace = res.needFace;

                    if (res.needLocation) {
                        this.group_Editform.lng = res.location.lng;
                        this.group_Editform.lat = res.location.lat;
                        this.son_prop.lng = res.location.lng;
                        this.son_prop.lat = res.location.lat;
                        this.group_Editform.effectiveDistance = res.location.effectiveDistance;
                    } else {
                        this.son_prop.lng = null;
                        this.son_prop.lat = null;
                        this.group_Editform.effectiveDistance = 500;
                    }
                    console.log("正在更新群组信息" + this.state)
                    // console.log(this.id+'asdf')
                    getAllSchedules(this.id).then(res => {
                        console.log("成功获取");
                        this.mySchList = res;
                    });      //获取当前群体计划列表
                })
            },


            //计划签到方法
            plan_sign() {
                // console.log(123);
                this.dialogFormVisible = true
            },

            //即刻签到方法
            imme_sign() {
                // console.log(123);
                if (this.state === true) {
                    //本群体正在签到状态 应该停止签到
                    disableCheck(this.id).then(() => {
                        this.$message({

                            message: "已关闭签到",
                            center: true,
                            type: "warning"
                        });
                        this.update();
                        center: true
                    });
                } else {
                    //本群体没有签到  应该开启签到模式
                    enableCheck(this.id).then(() => {
                        this.$message({
                            message: "开启签到成功",
                            type: "success",
                            center: true
                        });
                        this.update();
                        center: true
                    });
                }

            },


            //删除群体
            deleGroup_open() {
                this.$confirm('此操作将永久删除' + this.name + ', 是否继续?', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                    console.log("删除")
                    deleteGroup(this.id).then(() => {
                            this.$message({
                                message: "删除成功，正在跳转到主页面",
                                type: "success",
                                center: true

                            });
                            setTimeout(() => {
                                this.$router.go(-1);
                            }, 2000)
                            //后退一步
                        }
                        , () => {
                            console.log(this.id)
                        });
                }).catch(() => {
                    this.$message({
                        type: 'info',
                        message: '已取消删除',
                        center: true

                    });
                });
            },


            //提交签到计划
            submit_schedule() {
                this.schedule_final.duration = this.schedule_form.duration;
                console.log("持续时间为" + this.schedule_final.duration);
                this.schedule_final.enable = this.schedule_form.enable;
                this.schedule_final.repeat = '';

                if (this.rep.length != 0) {
                    this.schedule_final.repeat = this.rep[0];

                    for (var a = 1; a < this.rep.length; a++) {
                        this.schedule_final.repeat = this.schedule_final.repeat + ',' + this.rep[a];
                    }
                }

                // console.log(this.schedule_final.repeat);
                this.schedule_final.startUpTime = this.schedule_form.startUpTime;

                addSchedule(this.id, this.schedule_final).then(() => {
                    this.$message({
                        message: "创建计划成功",
                        type: "success",
                        center: true

                    })
                });

                this.dialogFormVisible = false

                this.update();

            },

            //编辑签到计划
            change_Schedule(index) {

                // index是用户要编辑的计划数组下标
                this.dialogChangeFormVisible = true;
                this.schedule_change_form = this.mySchList[index];

                var tempstr = this.schedule_change_form.repeat;
                // change_schedule_temp.scheduleId = temp
                this.change_arr_repeat = tempstr.split(',');
            },


            //提交修改后的签到计划
            submit_Changeschedule() {

                var sched_Id = this.schedule_change_form.scheduleId;
                this.changeSchedule_final.startUpTime = this.schedule_change_form.startUpTime;
                this.changeSchedule_final.duration = this.schedule_change_form.duration;
                this.changeSchedule_final.enable = this.schedule_change_form.enable;
                // startUpTime: '',
                //     duration: null,
                //     repeat: '',
                //     enable: false,
                this.changeSchedule_final.repeat = '';
                if (this.change_arr_repeat.length != 0) {
                    this.changeSchedule_final.repeat = this.change_arr_repeat[0];
                    for (var a = 1; a < this.change_arr_repeat.length; a++) {
                        this.changeSchedule_final.repeat = this.changeSchedule_final.repeat + ',' + this.change_arr_repeat[a];
                    }
                }

                updateSchedule(sched_Id, this.changeSchedule_final).then(() => {
                    this.$message({
                        message: "修改成功",
                        type: "success",
                        center: true

                    })
                    this.update();
                    this.dialogChangeFormVisible = false;
                })


            },


            handleListClick(tab, event) {
                // console.log(tab, event);
            },


            //删除签到计划
            deleteScheduleClick(index) {
                // console.log(row);
                this.$confirm('此操作将永久删除该计划, 是否继续?', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                    deleteSchedule(this.mySchList[index].scheduleId).then(() => {
                        this.$message({
                            type: "success",
                            message: "成功删除计划",
                            center: true
                        })
                        this.update();
                    })
                }).catch(() => {
                    this.$message({
                        type: 'info',
                        message: '已取消删除',
                        center: true

                    });
                });
            },

            editGroup() {
                this.dialogEditGroup = true;
            },
            submit_EditGroup() {

                this.$refs.group_Editform.validate().then(() => updateGroupInfo(this.id, this.group_Editform))
                    .then(() => {
                        this.$message({
                            message: "成功修改",
                            type: "success",
                            center: true

                        })
                        this.dialogEditGroup = false;
                        this.update();
                    })

            },


            //根据state显示颜色
            tableRowClassName({row, rowIndex}) {
                if (this.mySchList[rowIndex].enable === false) {
                    return 'warning_row';
                } else if (this.mySchList[rowIndex].enable === true) {

                    return 'success_row';
                }
                else return 'success_row';
            },

            recordBack_done({row, rowIndex}) {

                return 'success_row';
            },
            recordBack_missed({row, rowIndex}) {

                return 'warning_row';
            },
            recordBack_leave({row, rowIndex}) {

                return 'warning_row';
            },
            perRecordState({row, rowIndex}) {

                if (this.perRecordData[rowIndex].checked === '未签') {

                    return 'warning_row';
                } else if (this.perRecordData[rowIndex].checked === '已签') {

                    return 'success_row';
                }
                // else return 'success_row';
            },

            //响应地图组件的事件 修改 经纬度
            changeLoca(lng, lat) {
                this.group_Editform.lng = lng;
                this.group_Editform.lat = lat;
                console.log("来自父组件打印" + this.group_Editform.lng + ',' + this.group_Editform.lat)
            },


            //弹出表单 展示该成员表现情况
            stu_message(index) {
                // console.log(index)
                this.perRecordDiv = true
                var username = this.members[index].username;
                getUserHistory(this.id, username).then(res => {
                    // console.log(res)
                    var temp = res;
                    for (var i = 0; i < temp.length; i++) {
                        temp[i].day = temp[i].startUpDateTime.slice(0, 10);
                        temp[i].time = temp[i].startUpDateTime.slice(11, 16);
                        if (temp[i].checked == true) {
                            temp[i].checked = '已签'
                        } else if (temp[i].checked == false&&temp[i].leave==true) {
                            temp[i].checked = '请假'
                        }else {
                            temp[i].checked = '未签'
                        }
                    }
                    this.perRecordData = temp
                })
            },
            //轮询群体看有没有请假请求
            ReturnTheLeaveMessage(){
                var that = this
                // console.log()
                console.log("开始轮询了"+that.id)
                this.group_mes_index = setInterval(function () {
                    getLeaveRequest(that.id).then(res =>{
                        if (res.leaves.length>0){
                            that.open12()
                            that.leave_list = res.leaves;
                            var temp=[]
                            for (var i =0;i<that.leave_list.length;i++){
                                temp[i] = {}
                                temp[i]["leave_id"]=that.leave_list[i]["leave_id"];
                                temp[i]["leave_response"] = false
                            }
                            that.request_list = temp
                        }
                    })
                },1000)
            },

            recordMess(index) {
                this.RecordDiv = true
                console.log(index)

                var schId = this.historyScheduleList[index].id;
                console.log(schId)
                getRecord(schId).then(res => {
                    this.RecordData.done = res.done;
                    this.RecordData.missed = res.missed;

                    this.RecordData.leave = res.leave;
                    var temp={}
                    temp.come = this.RecordData.done.length;
                    temp.notcome = this.RecordData.missed.length;
                    temp.leavea = this.RecordData.leave.length;
                    this.data_people = temp
                    // console.log("应该修改了")
                })
            },
            //开启通知
            open12() {
                var that = this;
                clearInterval(this.group_mes_index)
                this.notifynote=this.$notify({
                    title: '通知',
                    dangerouslyUseHTMLString: true,
                    duration: 0,
                    showClose: false,
                    message: '<strong>学生请假</strong>',
                    onClick:function () {
                        // console.log(that.$notify.close())
                        that.dialogLeaveList=true
                        this.close();
                    }
                });
            },
            //提交leave请求的反馈
            submit_leaveAnswer(index,state){
                // this.dialogLeaveList=false
                console.log(state)
                var obj = {}
                obj.leave_id=this.leave_list[index].leave_id;
                obj.leave_response = state;
                obj.leave_msg = state
                this.leave_list.splice(index,1)
                responseToLeave(obj);
                // this.ReturnTheLeaveMessage()

            }

        },


    }
    // document.getElementById("testbutton").onclick=function(){alert(123456)};
</script>

<style scoped>

    .recordDiv .el-dialog {
        width: 100%;

    }

    .recordDiv .el-dialog__body {
        max-height: 50vh;
        padding: 0;
    }

    .footer .el-button-group {
        display: block;
        width: 300px;
        margin: 0 auto;
    }

    .footer .el-button {
        width: 100px;
    }

    #GroupContainer .app-bar {
        background-color: unset;
        /*height: 60%;*/
        /*position: absolute;*/
        top: 50%;
        transform: translate(0px, -50%);

        color: white;

        font-weight: inherit;
        letter-spacing: 3px;
    }

    #GroupContainer .app-bar .title h1 {

    }

</style>
<style>


    .el-table .warning_row {
        background: oldlace;
    }

    .el-table .success_row {
        background: rgba(162, 252, 152, 0.31);
    }

    .Show {
        display: block;
    }

    .NotShow {
        display: none;
    }

    #detailRecord .el-dialog__body {
        padding: 0;
    }


</style>


}