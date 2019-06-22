<template>


        <div class="home">
            <my-header/>
            <MyGroupTable ref="table" style="margin-bottom: 50px"></MyGroupTable>
            <!--<div id="allmap"></div>-->
            <div class="footer">
                <el-button @click="ChuangJianGroup">
                    <span class="letter_footer">创</span>
                    <span class="letter_footer">建</span>
                    <span class="letter_footer">群</span>
                    <span class="letter_footer">体</span>
                </el-button>

            </div>
        </div>



</template>

<script>
    import MyGroupTable from "./component/MyGroupTable"
    import MyHeader from "../../../../components/MyHeader"
    import {createGroup} from "../../../../resource/group"
    import {logout} from "../../../../resource/authorization"
    import {getUserInfo, updateUserInfo} from "../../../../resource/user"

    export default {
        name: 'home',
        components: {MyHeader, MyGroupTable},

        computed: {
            // editPerInfoFormData() {
            //     this.submitUserInfo.name = this.UserInfo.name;
            //     if (this.userHeadPhoto!=null){
            //         alert("有图片");
            //         this.submitUserInfo.profile=this.userHeadPhoto;
            //     }
            //     let formData = new FormData()
            //     console.log(this.submitUserInfo)
            //     Object.keys(this.submitUserInfo).forEach(key => {
            //         formData.append(key, this.submitUserInfo[key])
            //     })
            //     return formData
            // },

        },

        data() {
            return {
                BASE_URL: process.env.BASE_URL,
                headPhoto:null,
                // note: {
                //     backgroundImage: "url(" + require("../../../../image/head3.png") + ")",
                //     backgroundRepeat: "no-repeat",
                //     height: '60px',
                //     width: "100%",
                //     backgroundSize: '100% 100%',
                //     position: "relative",
                // },
                //
                //
                // dialogFormVisible:false,
                //
                // formLabelWidth: '120px',


            }
        },

        methods:{

            update(){

                this.$refs.table.update()

            },

            ChuangJianGroup() {
                this.$prompt('请输入群体名称', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',

                }).then(({ value }) => {
                    console.log(value)
                    createGroup(value).then(()=>{
                        console.log(value)
                        this.$message({
                            type: 'success',
                            message: '成功创建群体：: ' + value
                        });
                        this.update();
                    })
                    // alert(value);

                }).catch(() => {
                    this.$message({
                        type: 'info',
                        message: '取消输入'
                    });
                });
            }
        }
    }
</script>

<style scoped lang="scss">

    .footer .el-button {
        width: 100%;
        height: 100%;
        background-color: #409EFF;
    }

    .footer .letter_footer{
        padding: 0 10px;
        font-size: 20px;
        font-weight: inherit;
        color: white;
    }
</style>
<style lang="scss">
    .footer{
        position: fixed;
        bottom: 0;
        /*background-color: beige;*/
        width: 100%;
        padding: 0;
        margin: 0;
        z-index: 100;
        height: 50px;
    }
</style>