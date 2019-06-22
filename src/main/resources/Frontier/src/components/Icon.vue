<template>
    <img :src="src"/>
</template>

<script>
    export default {
        name: "Icon",
        props: {
            src: {
                type: String,
                required: true
            }
        }

    }
</script>

<style scoped lang="scss">
    img {
        margin-right: .625em;
        width: .875em;
        height: .875em;
        object-fit: contain;
    }
</style>