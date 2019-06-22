<template>
    <router-link :to="`/group/${id}`" class="group-item">
        <i v-if="loading" class="el-icon-loading"/>{{loading?'':name}}
        <i class="group-item__arrow el-icon-arrow-right"></i>
    </router-link>
</template>

<script>
    import {getGroupInfo} from "../../../../../resource/group";

    export default {
        name: "GroupItem",
        props: {
            id: {
                type: String,
                required: true,
            }
        },
        created() {
            this.update()
        },
        data() {
            return {
                name: '',
                loading: true
            }
        },
        methods: {
            update() {
                
                this.loading = true
                
                getGroupInfo(this.id).then(info => {
                    this.loading = false

                    this.name = info.name
                })
            }
        }
    }
</script>

<style scoped lang="scss">
    .group-item {
        display: block;
        padding: 1em .5em;
        color: #303133;
        cursor: pointer;
        border-bottom: 1px solid #ebeef5;
        font-weight: 500;
        
        .group-item__arrow{
            float: right;
            line-height: 20px;
        }
    }
</style>