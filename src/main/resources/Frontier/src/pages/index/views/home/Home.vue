<template>
    <div :style="note">
        <div style="height: 100% ;background-color: rgba(0,0,0,0.47);color: white">
            <el-card>
                <router-link :to="{path:'/login'}">登陆</router-link><br/>
                <router-link :to="{path:'/register'}">注册</router-link>
            </el-card>
        </div>
    </div>
</template>

<script>
    export default {
        name: "Home",
        data(){
            return{
                note: {
                    backgroundImage: "url(" + require("../../../../image/index.png") + ")",
                    backgroundRepeat: "no-repeat",
                    height:"100%",
                    backgroundSize: '100% 100%',

                },
            }
        }
    }
</script>

<style scoped lang="scss">


</style>