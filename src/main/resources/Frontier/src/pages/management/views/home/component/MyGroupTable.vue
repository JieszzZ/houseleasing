<template>
    <!--<el-collapse v-model="activeNames" @change="handleChange">-->
        <!--<el-collapse-item title="我创建的群体" name="1">-->
            <div id="groupTable">
                <el-dialog style="" :title="groupname" :visible.sync="QRcodeDialogVisible">
                    <p style="text-align: center;margin: 0"><img style="width: 250px;height: 250px" :src="imgUrl"/></p>
                </el-dialog>
                <el-table
                        :data="myGroupList"
                        stripe
                        style="width: 100%">
                    <el-table-column
                            prop="id"
                            label="群口令"

                    >
                    </el-table-column>
                    <el-table-column
                            prop="name"
                            label="群体名称"
                    >
                    </el-table-column>

                    <el-table-column
                            fixed="right"
                            label="操作"
                            width="130"
                    >
                        <template slot-scope="scope">
                            <el-button @click.native.prevent="showQR(scope.$index) "
                                       type="text"
                                       size="small"
                            >
                                二维码
                            </el-button>
                            <el-button @click.native.prevent="JumpToGroup(scope.$index) "
                                       type="text"
                                       size="small"

                            >
                                进入群体
                            </el-button>
                        </template>
                    </el-table-column>

                </el-table>
            </div>
        <!--</el-collapse-item>-->

    <!--</el-collapse>-->



</template>

<script>

    import {getGroupInfoList} from "../../../../../resource/group";
    import QRCode from 'qrcode'

    export default {
        name: "MyGroupTable",

        created(){
            this.update();
        },
        data(){

            return{
                baseurl:'https://www.wecheck.work/user.html#/group/',
                tableData: [],
                activeNames: ['1'],
                myGroupList:[],
                dialogFormVisible:false,
                formLabelWidth: '100px',
                imgUrl:null,
                groupname:null,
                QRcodeDialogVisible:false
            }
        },
        methods:{
            update(){
                getGroupInfoList().then(res =>{
                    this.myGroupList = res
                    // console.log(res)
                })
            },
            handleChange(val){
                // console.log(val);
            },
            JumpToGroup(index){
                //传入的是数组的下标
                // console.log(this.myGroupList[index].id);
                var id   = this.myGroupList[index].id
                this.$router.push('group/'+id)
            },
            showQR(index){
                this.QRcodeDialogVisible = true
                var id   = this.myGroupList[index].id;
                this.groupname = this.myGroupList[index].name
                var comUrl = this.baseurl+id;
                QRCode.toDataURL(comUrl, { errorCorrectionLevel: 'H' }, (err, url) =>{
                    this.imgUrl = url
                })
            }



        }
    }
</script>

<style lang="scss" >
    #groupTable .el-dialog{
        width: 250px;
        /*background-color: black;*/
    }
    #groupTable .el-dialog__body{
        padding: 0;
    }
</style>