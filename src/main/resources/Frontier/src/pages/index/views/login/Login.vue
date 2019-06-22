<template>
    
    <div :style="pcOrPhone?this.notePc:this.note" id="test" style="height: 100%;min-height: 550px">
        <div style="height: 100% ;background-color: rgba(0,0,0,0.47);color: white">

            <div style="width: 80% ;margin: 0 auto; padding-top: 10vh">

                <h2 style="margin: 0;margin-bottom: 10vh;">Hello，<br>
                    <h2 style="margin: 0">欢迎回来!</h2></h2>

                <el-card style=" margin:0 auto;color: white ">

                    <login-form :redirect="redirect"/>
                    <p style=" float: left">
                        <router-link to="/register" style="color: white;
                                            margin: 0;text-decoration: none;">
                            新用户注册</router-link>
                    </p>
                </el-card>
            </div>


        </div>

    </div>
</template>

<script>
    import LoginForm from "../../../../components/LoginForm";

    export default {
        name: "Login",
        components: {LoginForm},
        props: {
            // type: {
            //     type: String,
            //     validator: val => Object.keys(userType).map(k => userType[k]).indexOf(val) !== -1
            // },
            redirect:{
                type:String
            }
        },
        created(){

            if(/Android|webOS|iPhone|iPod|BlackBerry/i.test(navigator.userAgent)) {
                this.pcOrPhone = false
            } else {
                this.pcOrPhone = true
            }
        },
        data() {
            return {
                note: {
                    backgroundImage: "url(" + require("../../../../image/login_back.png") + ")",
                    backgroundRepeat: "no-repeat",
                    height: '100%',
                    backgroundSize: '100% 100%'
                },
                notePc:{
                    backgroundImage: "url(" + require("../../../../image/pcBackground.jpg") + ")",
                    backgroundRepeat: "no-repeat",
                    height: '100%',
                    backgroundSize: '100% 100%'
                },
                BASE_URL: process.env.BASE_URL,
                pcOrPhone:true,
            }
        },
        computed: {}
    }
</script>
<style scoped lang="scss">
    #test {
        height: 100%;
    }

</style>
<style  lang="scss">

    html {
        height: 100%;
    }

    body {
        height: 100%;
        margin: 0;
    }

    #test .el-card {
        background-color: rgba(7, 6, 8, 0.03);
        border: none;
    }

    #test .el-form-item__label {
        color: aliceblue;
        font-size: medium;
    }

    #test .el-card__body {
        padding: 0;
        /*max-width: 300px;*/
    }

    #app {
        height: 100%;
    }
</style>