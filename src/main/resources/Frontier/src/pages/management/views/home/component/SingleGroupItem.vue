<template>
    <el-card
            :style="'background-color: #909399'"
            >
        <h4>{{groupName}}</h4>
    </el-card>
</template>

<script>
    export default {
        name: "SingleGroupItem",
        props:{
            groupId:{
                type:String,
                required:true
            },
            groupName:{
                type:String,
                required:true
            },
        },
        data(){
            return
            {

            }
        }
    }
</script>

<style scoped>

</style>