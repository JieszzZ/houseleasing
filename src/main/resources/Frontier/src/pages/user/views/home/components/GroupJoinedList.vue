<template>
    <div class="group-joined-list">
        <my-title>已加入的群组</my-title>
        <ul>
            <li v-for="item in groupInfoList"><group-item :id="item.id"/></li>
        </ul>
    </div>
</template>

<script>
    import GroupItem from "./GroupItem";
    import {getGroupInfoList} from "../../../../../resource/group";
    import MyTitle from "./MyTitle";

    export default {
        name: "GroupJoinedList",
        components: {MyTitle, GroupItem},
        created(){
            this.update()
        },
        data(){
            return{
                groupInfoList:[]
            }
        },
        methods:{
            update(){
                getGroupInfoList().then(list=>{
                    this.groupInfoList = list
                })
            }
        }
    }
</script>

<style scoped lang="scss">

</style>