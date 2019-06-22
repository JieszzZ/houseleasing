<template>
    <el-form  :model="form" :rules="rules" ref="form">
        <el-form-item label="账号" prop="username">
            <el-input type="text" v-model="form.username"/>
        </el-form-item>
        <el-form-item label="密码" prop="password">
            <el-input type="password" v-model="form.password"/>
        </el-form-item>
        <el-form-item>
            <el-button type="primary" :disabled="submitting" @click="submit" style="width: 100%">
                <i :class="[submitting?'el-icon-loading':'']"/>{{submitting?'':'登录'}}
            </el-button>
        </el-form-item>
    </el-form>
</template>

<script>
    import {login} from "../resource/authorization";
    import {wait} from "../utils";

    // 管理端、用户端通用登陆组件


    export default {
        name: "LoginForm",
        props: {
            redirect: {
                type: String
            }
        },
        data() {
            return {
                form: {
                    username: '',
                    password: '',
                },
                rules: {
                    username: [{required: true, message: '请输入用户名'}],
                    password: [{required: true, message: '请输入密码'}]
                },
                submitting: false
            }
        },
        methods: {
            async submit() {
                this.submitting = true

                try {
                    let data = await login(this.form.username, this.form.password)
                    this.$message({
                        message: '登陆成功  正在跳转',
                        type: 'success'
                    });
                    // 2s后跳转到相应页面
                    await wait(2000)

                    if (this.redirect) {
                        window.location.href = this.redirect
                    } else {
                        let type = data.userType === 0? 'user':'management'
                        window.location.href = `/${type}.html`
                    }
                }
                catch (e) {
                    this.$message.error(`出错：${e.message}`);
                }
                finally {
                    this.submitting = false
                }
            }
        }
    }
</script>

<style scoped lang="scss">

</style>