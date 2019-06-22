<template>
    <el-card style="; margin: auto ">
        <h2 style="font-weight: initial">管理者注册</h2>
        <register-form/>
        <p style="text-align: right"><a :href="`${BASE_URL}user.html/#/register`">切换至用户端注册</a></p>
    </el-card>
</template>

<script>
    import RegisterForm from "../../../../components/RegisterForm";
    export default {
        name: "Register",
        components: {RegisterForm},

        data(){
            return{
                BASE_URL: process.env.BASE_URL
            }
        }
    }
</script>

<style scoped lang="scss">

</style>