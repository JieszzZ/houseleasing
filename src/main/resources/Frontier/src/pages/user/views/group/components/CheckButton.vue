<template>
    <div @click="$emit('click')" class="check-button">签到
        <div class="flow flow-1"></div>
        <div class="flow flow-2"></div>
        <div class="flow flow-3"></div>
    </div>
</template>

<script>
    export default {
        name: "CheckButton"
    }
</script>

<style scoped lang="scss">

    .check-button {

        display: inline-block;
        font-size: 25px;

        background-color: #409EFF;
        color: white;
        width: 5em;
        height: 5em;
        border-radius: 50%;
        text-align: center;
        line-height: 5em;

        position: relative;
        box-shadow: 0px 0px 1em transparent;
        transition-property: box-shadow;
        transition-duration: .2s;

        &:hover {
            cursor: pointer;
            box-shadow: 5px 5px 20px rgba(50, 50, 50, .5);
        }

        .flow {

            border-radius: 50%;
            position: absolute;
            border: 1px solid transparent;
            animation-name: spark;
            animation-duration: 1s;
            animation-iteration-count: infinite;
        }

        @for $i from 1 through 3 {
            .flow-#{$i} {

                width: #{100%+$i*10%};
                height: #{100%+$i*10%};
                left: #{-$i*5%};
                top: #{-$i*5%};

                animation-delay: #{$i*0.2s-0.2s};
            }
        }
    }


    @keyframes spark {
        0% {
            border-color: #409EFF;
        }
        30% {
            border-color: transparent;
        }
        70% {
            border-color: transparent;
        }
        100% {
            border-color: #409EFF;
        }
    }
</style>