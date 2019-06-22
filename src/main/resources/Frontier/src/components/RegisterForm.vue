<template>
    <el-container id="RfContainer" style="">

        <el-form status-icon :model="ruleForm" :rules="rules" ref="ruleForm" style="width: 80%;margin: 0 auto">

            <el-form-item label="用户名" prop="username">
                <el-input type="text" v-model="ruleForm.username"></el-input>
            </el-form-item>
            <el-form-item label="密码" prop="password">
                <el-input type="password" v-model="ruleForm.password"></el-input>
            </el-form-item>
            <el-form-item label="确认密码" prop="checkpassword">
                <el-input type="password" v-model="ruleForm.checkpassword"></el-input>
            </el-form-item>

            <el-form-item label="用户类型" prop="userType">
                <br>
                <el-select v-model="ruleForm.userType" placeholder="请选择用户类型">
                    <el-option label="签到端用户" :value="0"></el-option>
                    <el-option label="管理端用户" :value="1"></el-option>
                </el-select>
            </el-form-item>

            <el-form-item label="姓名" prop="name">
                <el-input v-model="ruleForm.name"></el-input>
            </el-form-item>


            <el-form-item label="本人照片" prop="profile">
                <el-upload
                        class="upload-demo"
                        drag
                        action=""
                        :on-change="getFile"
                        :on-preview="handlePreview"
                        :on-remove="handleRemove"
                        :before-remove="beforeRemove"
                        :on-exceed="handleExceed"
                        :limit=1
                        :auto-upload="false"
                >
                    <i class="el-icon-upload"></i>
                    <div class="el-upload__text">将文件拖到此处，或<em>点击上传</em></div>
                    <!--<div class="el-upload__tip" slot="tip">只能上传jpg/png文件，且不超过500kb</div>-->
                </el-upload>
            </el-form-item>

            <el-form-item>
                <el-button type="primary" style="width: 100%" @click="submitForm('ruleForm') ">注册</el-button>
                <!--<el-button @click="resetForm('ruleForm')">重置</el-button>-->
            </el-form-item>


        </el-form>

    </el-container>


</template>

<script>

    import {register} from "../resource/authorization";
    import {captureImageFromVideo} from "../utils/index"
    import {compressImage} from "@/utils";

    export default {

        name: 'RegisterForm',

        created() {

            if (/Android|webOS|iPhone|iPod|BlackBerry/i.test(navigator.userAgent)) {
                this.pcOrPhone = false
            } else {
                this.pcOrPhone = true
            }
        },

        mounted() {

        },


        data() {


            var checkusername = (rule, value, callback) => {
                if (!value) {
                    return callback(new Error('账号不能为空'));
                }
                setTimeout(() => {

                    //以下if 条件 将来改成验证是否重复
                    // if (!Number.isInteger(value)) {
                    //     callback(new Error('请输入数字值'));
                    // } else {
                    //     callback();
                    // }

                    callback();
                }, 100);
            };
            var validatePass = (rule, value, callback) => {
                if (value === '') {
                    callback(new Error('请输入密码'));
                } else {
                    if (this.ruleForm.checkpassword !== '') {
                        this.$refs.ruleForm.validateField('checkpassword');
                    }
                    callback();
                }
            };
            var validatecheckpass = (rule, value, callback) => {
                if (value === '') {
                    callback(new Error('请再次输入密码'));
                } else if (value !== this.ruleForm.password) {
                    callback(new Error('两次输入密码不一致!'));
                } else {
                    callback();
                }
            };

            var checkname = (rule, value, callback) => {
                if (!value) {
                    return callback(new Error('姓名不能为空'));
                }
                setTimeout(() => {

                    //以下if 条件 将来改成验证是否重复
                    // if (!Number.isInteger(value)) {
                    //     callback(new Error('请输入数字值'));
                    // } else {
                    //     callback();
                    // }

                    callback();
                }, 100);
            };


            var checkfile = (rule, value, callback) => {
                if (!value) {
                    return callback(new Error('请上传正脸照片'));
                }
                setTimeout(() => {

                    //以下if 条件 将来改成验证是否重复
                    // if (!Number.isInteger(value)) {
                    //     callback(new Error('请输入数字值'));
                    // } else {
                    //     callback();
                    // }

                    callback();
                }, 100);
            };

            var checkface = (rule, value, callback) => {
                if (!value) {
                    return callback(new Error('请拍照'));
                }
                setTimeout(() => {

                    callback();
                }, 100);
            };


            return {

                ruleForm: {
                    username: '',
                    password: '',
                    checkpassword: '',
                    name: '',
                    userType: null,
                    profile: false,
                    pcOrPhone: true,
                    photo_PC: null,

                },


                rules: {
                    username: [
                        {required: true, validator: checkusername, trigger: 'blur'},
                    ],
                    password: [
                        {required: true, validator: validatePass, trigger: 'blur'},
                    ],
                    checkpassword: [
                        {required: true, validator: validatecheckpass, trigger: 'blur'},
                    ],
                    name: [
                        {required: true, message: '请输入姓名', trigger: 'blur'},
                    ],
                    userType: [
                        {required: true, message: '请选择用户类型', trigger: 'blur'},
                    ],
                    profile: [
                        {required: true, validator: checkfile, trigger: 'blur'},
                    ],
                    photo_PC: [
                        {required: true, validator: checkface, trigger: 'blur'},
                    ]

                }
            };
        },


        computed: {
            ruleFormData() {
                let formData = new FormData()
                console.log(this.ruleForm);
                Object.keys(this.ruleForm).forEach(key => {
                    if (key === 'checkpassword') return
                    formData.append(key, this.ruleForm[key])
                })
                console.log(formData)
                return formData
            }
        },
        methods: {


            getFile(file, filelist) {
                this.ruleForm.profile = file.raw;

            },

            submitForm(formName) {
                this.$refs[formName].validate((valid) => {
                    if (valid) {


                        new Promise(resolve => {

                            // 图片压缩
                            let formData = this.ruleFormData
                            let img = new Image()
                            img.src = URL.createObjectURL(formData.get('profile'))
                            img.onload = () => {
                                compressImage(img).then(file => {
                                    formData.append('profile',file)
                                    resolve(formData)
                                })
                            }
                        }).then(formData => register(formData))
                            .then(() => {
                                    this.$message({
                                        message: '注册成功  正在跳转',
                                        type: 'success'
                                    });
                                    // 2s后跳转到登录页面
                                    setTimeout(() => {
                                        this.$router.push('/login')
                                    }, 2000)
                                },
                                e => {
                                    this.$message.error(`出错：${e.message}`);
                                })

                    } else {
                        console.log('error submit!!');
                        return false;
                    }
                })
            },

            resetForm(formName) {
                this.$refs[formName].resetFields();

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


        }

    }


</script>


<style scoped lang="scss">

    .displayNone {
        display: none;
    }

    .show {
        display: block;
    }


</style>
<style lang="scss">
    #RfContainer .el-upload-dragger {
        width: 100%;
    }

    #RfContainer .el-upload {
        width: 100%;
    }
</style>