存放页面（user、management）间的公共组件，譬如登陆组件

组件规约：

* 大驼峰式命名(eg:LoginForm.vue)
* 对于复杂组件(可能包含子组件)，先创建小驼峰命名的文件夹，再创建组件
  
  eg:图片上传组件   
  &gt;imageUploader  
  &nbsp;&nbsp;&gt;components  
  &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;SubComponent1.vue  
  &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;SubComponent2.vue  
  &nbsp;&nbsp;ImageUploader.vue  
  
**注意：非公共组件务必放在各自view下的components（page/views/view1/componnets）文件夹中**