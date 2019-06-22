<template>
    <div class="my-title" style="color: white"><div class="inner-wrapper"><slot></slot></div></div>
</template>

<script>
    export default {
        name: "MyTitle"
    }
</script>

<style scoped lang="scss">
    .my-title{
        
        font-size: 1.1rem;
        
        border-bottom: 1px solid #409eff;
        
        >.inner-wrapper{
            display: inline-block;
            background-color: #409effad;;
            /*border-bottom-left-radius: 5px;*/
            border-top-left-radius: 5px;
            border-top-right-radius: 15px;
            
            padding: .5em 1em .5em .5em;
        }
        
    }
</style>