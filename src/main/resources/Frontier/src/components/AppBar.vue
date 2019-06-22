<template>
    <el-row class="app-bar" type="flex" align="middle">
        <el-col :span="6">
            <router-link to="/">
                <el-button style="vertical-align: middle;width: 3em" type="text">
                    <icon :src="backIcon"/>
                </el-button>
            </router-link>
        </el-col>
        <el-col :span="12" class="title">
            <h1>
                <slot></slot>
            </h1>
        </el-col>
        <el-col :span="6" style="text-align: right" class="right">
            <slot name="right"></slot>
        </el-col>
    </el-row>
</template>

<script>
    import Icon from "./Icon";
    import backIcon from '../assets/icon_back.png'

    export default {
        name: "AppBar",
        components: {Icon},
        data() {
            return {
                backIcon
            }
        },
        methods: {
        }
    }
</script>

<style scoped lang="scss">
    .app-bar {
        background-color: #fafafa;
        width: 100%;
        height: 2.75em;

        /*触发BFC*/
        overflow: hidden;

        .title {
            text-align: center;

            h1 {
                font-size: 1.2em;
                font-weight: lighter;
            }
        }

    }
</style>