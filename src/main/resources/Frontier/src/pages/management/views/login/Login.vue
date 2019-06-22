<template>
    <div :style="note" id="test" style="height: 100%" >
        <div style="height: 100% ;background-color: rgba(0,0,0,0.47);color: white">

            <div style="width: 80% ;margin: 0 auto; padding-top: 25%">

                <h2 style="margin: 0;margin-bottom: 20%;" >亲爱的管理者，<br><h1 style="margin: 0">welcome!</h1></h2>

                <el-card style=" margin:0 auto;color: white ">

                    <login-form type="management"/>
                    <p style=" float: left">
                        <a :href="`${BASE_URL}index.html/#/login`"
                        >新用户注册</a>
                    </p>
                    <p style="float: right"><a :href="`${BASE_URL}user.html/#/login`">切换至用户端页面</a></p>
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

        data() {
            return {
                note:{
                    backgroundImage: "url(" + require("../../../../image/login_back.png") + ")",
                    backgroundRepeat: "no-repeat",
                    height:'100%',
                    backgroundSize:'100% 100%'
                    },
                BASE_URL: process.env.BASE_URL
            }
        }
    }
</script>

<style scoped lang="scss">


    #test{
        height: 100%;
    }


</style>
<style>
    html{
        height: 100%;
    }
    body{
        height:100% ;
    }
    #test .el-card{
        background-color: #fff0;
        border: none;
    }
    #test .el-form-item__label{
        color: aliceblue;
        font-size: medium;
    }
    #test .el-card__body{
        padding: 0;
        /*max-width: 300px;*/
    }
    #app{
        height: 100%;
    }
</style>