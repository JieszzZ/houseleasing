<template>
    <el-button @click="down"  type="primary"  plain>导出表格</el-button>
</template>

<script>
    export default {
        name: "downExcel",
        props:[
            "jsonData"
        ],
        data(){
            return{
                // jsonData : [
                //     {
                //         name:'路人甲',
                //         phone:'123456789',
                //         email:'000@123456.com'
                //     },
                //     {
                //         name:'炮灰乙',
                //         phone:'123456789',
                //         email:'000@123456.com'
                //     },
                //     {
                //         name:'土匪丙',
                //         phone:'123456789',
                //         email:'000@123456.com'
                //     },
                //     {
                //         name:'流氓丁',
                //         phone:'123456789',
                //         email:'000@123456.com'
                //     },
                // ]
            }
        },
        methods:{
            down(){
                console.log("123")
                let str = `用户名,姓名,缺勤,请假,出勤,出勤率\n`;
                //增加\t为了不让表格显示科学计数法或者其他格式
                for(let i = 0 ; i < this.jsonData.length ; i++ ){

                    str =str+`${this.jsonData[i]["username"] + '\t'},`+`${this.jsonData[i]["name"] + '\t'},`+`${this.jsonData[i]["missed"] + '\t'},`+
                        `${this.jsonData[i]["leave"] + '\t'},`+`${this.jsonData[i]["done"] + '\t'},`+`${this.jsonData[i]["done_percent"] + '\t'},`
                    str+='\n';
                }
                //encodeURIComponent解决中文乱码
                let uri = 'data:text/csv;charset=utf-8,\ufeff' + encodeURIComponent(str);
                //通过创建a标签实现
                var link = document.createElement("a");
                link.href = uri;
                //对下载的文件命名..
                link.download =  "考勤记录.csv";
                document.body.appendChild(link);
                link.click();
                document.body.removeChild(link);

            }
        }
    }
</script>

<style scoped>

</style>