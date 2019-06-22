<template>
    <div class="home">
        <my-header/>
        <el-form>
            <el-form-item>
                <el-input type="text" v-model="searchId" placeholder="输入群组口令">
                    <el-button slot="append" icon="el-icon-search" @click="searchButtonHandler"></el-button>
                    <el-button slot="append" @click="handleIconScanClick">
                        <img class="icon_scan" src="@/assets/icon_scan.png">
                    </el-button>
                </el-input>
            </el-form-item>
        </el-form>
        <section class="main">
            <today-schedule-table/>
            <group-joined-list/>
        </section>
        <q-r-code-scanner @close="isScanningQRCode=false" @detected="handleDetectedQRCode" v-if="isScanningQRCode"/>
    </div>
</template>

<script>
    import TodayScheduleTable from "./components/TodayScheduleList";
    import GroupJoinedList from "./components/GroupJoinedList";
    import MyHeader from "../../../../components/MyHeader";
    import Icon from "@/components/Icon";
    import QRCodeScanner from "@/components/QRCodeScanner";
    import {getGroupInfo} from "@/resource/group";

    export default {
        name: 'home',
        components: {
            QRCodeScanner,
            Icon,
            MyHeader,
            GroupJoinedList,
            TodayScheduleTable
        },
        created() {
        },
        data() {
            return {
                BASE_URL: process.env.BASE_URL,
                searchId: '',
                isScanningQRCode: false
            }
        },
        methods: {
            searchButtonHandler() {
                if (!this.searchId || this.searchId && this.searchId.length === 0) {
                    this.$alert('群组ID不可为空')
                    return
                }
                getGroupInfo(this.searchId).then(
                    () => {
                        this.$message.success(`正在跳转`)
                        this.$router.push(`/group/${this.searchId}`)
                    },
                    e => {
                        if (e.status === 202) {
                            this.$message.error(`群组：${this.searchId} 不存在`)
                        } else {
                            this.$message.error(`未知错误：${e.message}`)
                        }
                    }
                )
            },
            handleIconScanClick() {
                this.isScanningQRCode = true
            },
            handleDetectedQRCode(content) {
                if (!this.isScanningQRCode) {
                    return
                }
                this.isScanningQRCode = false
                // 判断content是否属于我们的
                if (!content.match(/\/group\/[a-zA-Z0-9]+/i)) {
                    this.$message.error(`检测到的内容：${content} 中不含群组信息`)
                } else {
                    this.$message.success(`检测到：${content}即将跳转到新的页面`)
                    setTimeout(() => {
                        location.href = content
                    }, 2000)
                }

            }
        }
    }
</script>
<style lang="scss" scoped>
    .home {
        section.main {
            padding: 0 1em;
        }

        .icon_scan {
            width: 14px;
            height: 14px;
            vertical-align: bottom;
        }
    }
</style>
<style lang="scss">
    .el-dialog {
        width: 100% !important;
        max-width: 450px;
    }
</style>