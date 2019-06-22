<template>
    <div class="have-not-joined__wrapper">
        <el-button :disabled="loading" type="success" @click="joinGroup" class="join-button">
            <i v-if="loading" class="el-icon-loading"/>{{loading?'':'加入'}}
        </el-button>
    </div>
</template>

<script>
    import {joinGroup} from "../../../../../resource/group";

    export default {
        name: "HaveNotJoined",
        props: {
            id: {
                required: true,
                type: String
            }
        },
        data() {
            return {
                loading: false
            }
        },
        methods: {
            joinGroup() {

                let onFinallyFunc = () => {
                    this.loading = false
                }
                this.loading = true

                joinGroup(this.id).then(
                    ()=>{
                        this.$emit('hasJoined')
                    },
                    e => {
                        this.$message.error(`发生错误：${e.message}`);
                    }
                ).then(onFinallyFunc, onFinallyFunc)
            }
        }
    }
</script>

<style scoped lang="scss">
    .have-not-joined__wrapper {
        position: absolute;
        left: 0;
        right: 0;
        top: 0;
        bottom: 0;
        background-color: rgb(200, 200, 200);

        .join-button {
            position: absolute;
            left: 50%;
            top: 50%;
            transform: translate(-50%, -50%);
        }
    }
</style>