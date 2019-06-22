<template>
    <div>
        <div id="headOut" :style="note">
            <div style="height: 45px;width: 45px;background-color: #00ffff00;
                    display: inline-block;
                    position: absolute;top: 50%;transform: translate(0, -50%);
                    border-radius: 25px;
                    margin-left: 5%;
                    "
                 @click="showEditPerInfo()"
                 id="headphoto"
            >
            </div>
            <div id="headIn" style="right: 0; height: 60%; position: absolute;top: 50%;transform: translate(0, -50%);
                                            margin-right: 5%;">
                <el-button type="text" @click="logoutFunction" style="color: white">注销账户</el-button>
            </div>
        </div>


        <!--点击头像弹出框 修改个人信息-->
        <el-dialog title="个人信息" :visible.sync="dialogFormVisible">
            <el-form :model="UserInfo" ref="UserInfo">
                <el-form-item label="用户名" :label-width="formLabelWidth">
                    <el-input v-model="UserInfo.username" :disabled="true"></el-input>
                </el-form-item>
                <el-form-item label="姓名" :label-width="formLabelWidth">
                    <el-input v-model="UserInfo.name"></el-input>
                </el-form-item>
                <el-form-item label="人脸识别头像" :label-width="formLabelWidth">
                    <div id="perEditHead" style="width: 120px;height: 120px; ">

                    </div>
                    <el-upload
                            ref="profile"
                            prop="profile"
                            class="upload-demo"
                            action=""
                            :on-preview="handlePreview"
                            :on-remove="handleRemove"
                            :before-remove="beforeRemove"
                            :on-change="getFile"
                            multiple
                            :limit="1"
                            :on-exceed="handleExceed"
                            :auto-upload="false">
                        <el-button size="small" type="primary">更换头像</el-button>
                    </el-upload>
                </el-form-item>


            </el-form>
            <div slot="footer" class="dialog-footer">
                <el-button @click="dialogFormVisible = false">取 消</el-button>
                <el-button type="primary" @click="editPerInfo()">确 定</el-button>
            </div>
        </el-dialog>
    </div>
</template>

<script>
    import {logout} from "../resource/authorization"
    import {getUserInfo, updateUserInfo} from "../resource/user"
    import {compressImage} from "@/utils";

    export default {
        name: "MyHeader",
        created() {
            // this.getposition()
            this.getPhoto()
        },
        data() {
            return {
                BASE_URL: process.env.BASE_URL,
                note: {
                    backgroundImage: "url(" + require("../image/head3.png") + ")",
                    backgroundRepeat: "no-repeat",
                    height: '60px',
                    width: "100%",
                    backgroundSize: '100% 100%',
                    position: "relative",
                },

                UserInfo: {
                    username: '',
                    name: '',
                    profile: null,
                },
                userHeadPhoto: null,
                submitUserInfo: {
                    name: "",
                },
                dialogFormVisible: false,

                formLabelWidth: '100px',
            }
        },
        computed: {
            editPerInfoFormData() {
                this.submitUserInfo.name = this.UserInfo.name;
                if (this.userHeadPhoto != null) {
                    this.submitUserInfo.profile = this.userHeadPhoto;
                }
                let formData = new FormData()
                console.log(this.submitUserInfo)
                Object.keys(this.submitUserInfo).forEach(key => {
                    formData.set(key, this.submitUserInfo[key])
                })
                return formData
            },

        },
        methods: {

            getFile(file, filelist) {
                // console.log(file)
                this.userHeadPhoto = file.raw;
            },
            handleRemove(file, fileList) {
                console.log(file, fileList);
                this.fileList = [];
            },
            handlePreview(file) {
                console.log(file);
            },
            handleExceed(files, fileList) {

                this.$message.warning('当前限制选择 1 个文件');
            },
            beforeRemove(file, fileList) {
                return this.$confirm(`确定移除 ${ file.name }？`);
            },

            logoutFunction() {
                logout().then(() => {
                    this.$message({
                        message: '账户注销成功  正在跳转',
                        type: 'success'
                    })
                    setTimeout(() => {
                        window.location.href = this.BASE_URL;
                    }, 2000)
                })
            },

            //设置头像
            getPhoto() {
                getUserInfo().then(res => {
                    // console.log(res)
                    document.getElementById("headphoto").style.backgroundImage = 'url(' + res.profile + '?t=' + Date.now() + ')';
                    document.getElementById("headphoto").style.backgroundSize = '45px 45px'
                    this.UserInfo = res;
                    // console.log(res.name + " 123");

                })
            },


            editPerInfo() {
                // 如果有图片  压缩它

                new Promise(resolve => {
                    // 图片压缩
                    let formData = this.editPerInfoFormData
                    let profile = formData.get('profile')
                    // 无需压缩
                    if (!profile) {
                        resolve(formData)
                        return
                    }
                    let img = new Image()
                    img.src = URL.createObjectURL(profile)
                    img.onload = () => {
                        compressImage(img).then(file => {
                            formData.set('profile', file)
                            resolve(formData)
                        })
                    }
                }).then(formData => updateUserInfo(formData))
                    .then(() => {
                        this.dialogFormVisible = false
                        this.$message("修改成功")
                        setTimeout(this.getPhoto(), 1000)
                        this.$refs.profile.clearFiles()
                        //初始化
                        this.userHeadPhoto = null
                        // console.log(this.submitUserInfo.profile)
                    })
            },

            showEditPerInfo() {
                this.dialogFormVisible = true;
                getUserInfo().then(res => {
                    // console.log(res)
                    document.getElementById("perEditHead").style.backgroundImage = 'url(' + res.profile + '?t=' + Date.now() + ')';
                    document.getElementById("perEditHead").style.backgroundSize = '120px 120px'
                })
            },

        }
    }

</script>
<style scoped>

</style>