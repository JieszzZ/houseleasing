<template>
    <div class="group">
        <app-bar :style="note">
            <template><span style="color: white;font-size: 1.3em;letter-spacing: 7.5px">{{name}}</span></template>
            <template slot="right">
                <el-button type="text" @click="quitGroup" style="margin-right: 1em;color:white">退出群组</el-button>
            </template>
        </app-bar>
        <section class="content">
            <div class="owner-tag" v-if="ownerName">
                <div class="owner-tag__key" style="color: white">创建者：</div>
                <div class="owner-tag__value">{{ownerName}}</div>
            </div>
            <div class="group-operation" v-if="!loading">

                <!--签到-->
                <el-alert
                        v-if="!state"
                        title="暂未开启签到"
                        type="warning">
                </el-alert>
                <el-button v-else-if="checked" type="success" disabled>已完成签到</el-button>
                <template v-else>
                    <!--包含签到按钮和请假按钮（逻辑上两个按钮共存亡  所以放在一起-->
                    <div class="check-button-wrapper">
                        <check-button @click="check"/>
                    </div>
                    <div style="text-align: right">
                        <el-button type="danger" class="absence-button" plain @click="reportAbsence">请假</el-button>
                    </div>
                </template>
                <have-not-joined v-if="!hasJoined" :id="id" @hasJoined="hasJoinedHandler"/>
            </div>
            <checking-history v-if="!loading && hasJoined" :id="id"/>
            <i class="el-icon-loading" v-else/>
        </section>
        <check-validator ref="checkValidator" :group-id="id"/>
    </div>
</template>

<script>
    import {getGroupInfo, quitGroup} from "../../../../resource/group";
    import AppBar from "../../../../components/AppBar";
    import HaveNotJoined from "./components/HaveNotJoined";
    import {getUserInfo} from "../../../../resource/user";
    import Icon from "../../../../components/Icon";
    import ButtonMore from "../../../../components/ButtonMore";
    import CheckValidator from "./components/CheckValidator";
    import CheckButton from "./components/CheckButton";
    import CheckingHistory from "@/pages/user/views/group/components/CheckingHistory";
    import {getAbsenceRequestFeedback, reportAbsence} from "@/resource/leave";

    let test = false

    export default {
        name: "Group",
        components: {
            CheckingHistory,
            CheckButton, CheckValidator, ButtonMore, Icon, HaveNotJoined, AppBar,
        },
        props: {
            id: {
                required: true,
                type: String
            }
        },
        created() {
            this.update()
            
            // 绑定请假回复定时器
            this.absenceTimer = setInterval(()=>{
                this.absenceIdList.map(id=>getAbsenceRequestFeedback(id)).forEach(respPromise=>{
                    respPromise.then(resp=>{
                        
                        const states = {
                            unhandled:0,
                            fullfilled:1,
                            rejected:2
                        }
                        
                        switch (resp.leave_status) {
                            case states.unhandled:
                                return
                                break
                            case states.fullfilled:
                                this.$message.success('请假成功')
                                this.absenceIdList.splice(this.absenceIdList.findIndex(v=>v===resp.leave_id))
                                break
                            case states.rejected:
                                this.$message.error(`老师拒绝了您的请求`)
                                this.absenceIdList.splice(this.absenceIdList.findIndex(v=>v===resp.leave_id))
                                break
                        }
                        
                    })
                })
            },2000)
        },
        beforeDestroy(){
            clearTimeout(this.absenceTimer)
        },
        data() {
            return {
                ownerId: undefined,
                ownerName: undefined,

                // 群组名称
                name: '',

                hasJoined: false,
                // 是否开启签到
                state: false,
                // 是否完成签到
                checked: false,

                loading: false,

                // 人脸、地理
                needFace: false,
                needLocation: false,
                note: {
                    backgroundImage: "url(" + require("../../../../image/head3.png") + ")",
                    backgroundRepeat: "no-repeat",
                    height: '60px',
                    width: "100%",
                    backgroundSize: '100% 100%',
                    position: "relative",
                },
                
                
                // 请假
                absenceIdList:[],
                absenceTimer:null
            }
        },
        watch: {
            id() {
                this.update()
            },
            ownerId() {
                getUserInfo(this.ownerId).then(info => {
                    this.ownerName = info.name
                })
            }
        },
        methods: {
            check() {
                this.$refs.checkValidator.check(this.needLocation, this.needFace)
                    .then(() => {
                        this.update()
                    })
            },
            reportAbsence() {


                this.$prompt('请输入请假理由', '请假', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消'
                }).then(({value}) => {
                    reportAbsence(this.id, value).then((data) => {
                        this.$message.success('请求成功，请等待老师回复')
                        this.update()
                        
                        this.absenceIdList.push(data.leaveID)
                        
                    }, e => {
                        this.$message.error(`请假失败：${e.message}`)
                    })
                },()=>{})

            },
            update() {

                let onSucceedFunc = () => {
                    this.loading = false
                }

                this.loading = true

                getGroupInfo(this.id).then(res => {

                        this.name = res.name
                        this.ownerId = res.owner
                        this.hasJoined = res.role === 1
                        this.state = res.state
                        this.checked = res.checked

                        this.needLocation = res.needLocation
                        this.needFace = res.needFace
                    },
                    e => {
                        return null
                        this.$alert('该群组不存在！', '服务器消息', {
                            confirmButtonText: '确定'
                        }).then(() => {
                            this.$router.go(-1)
                        })
                    })
                    .then(onSucceedFunc, onSucceedFunc)
            },
            hasJoinedHandler() {
                this.update()
            },
            async quitGroup() {

                try {
                    await this.$confirm('即将退出该群组, 是否继续?', '提示', {
                        confirmButtonText: '确定',
                        cancelButtonText: '取消',
                        type: 'warning'
                    })
                    await quitGroup(this.id)
                    this.$message('退出成功');
                    this.$router.push({name: 'home'});
                } catch (e) {
                    this.$message.error(e.message)
                }

            },
            test() {
                alert(1)
            }
        }
    }
</script>

<style scoped lang="scss">
    @import "../../style/variables";

    .group {
        & > .content {
            position: relative;
            overflow: hidden;
            min-height: calc(75vh - #{$top-bar-height});
            margin-top: 5vh;
        }

        .group-operation {
            position: relative;
            padding: 1em .5em;

            .check-button-wrapper {
                margin-top: 10vh;
                margin-bottom: 50px;
                text-align: center;
            }
        }

        .owner-tag {
            border-bottom: 1px solid #409EFF;
            position: relative;
            height: 2em;
            line-height: 2;

            .owner-tag__key {

                display: inline-block;
                position: relative;
                background-color: #409EFF;
                padding-left: .5em;
                margin-right: 2em;

                &:after {
                    content: "";
                    display: inline-block;
                    width: 0px;
                    height: 0px;
                    position: absolute;
                    left: 100%;
                    bottom: 0px;
                    border: 2em solid transparent;
                    border-right-color: #409EFF;
                    transform: translateX(-50%) rotateZ(90deg);
                    z-index: -2;
                }
            }
            .owner-tag__value {
                display: inline-block;
                position: relative;
            }
        }
    }

</style>