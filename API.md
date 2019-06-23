# 接口文档

负责人：刘鹏昊

## 0
* baseurl : /api/v1/
* 所有日期时间如无特殊说明 格式为ISO 8601:
  ```
  YYYY-MM-DDTHH:MM:SSZ
  ```
* 返回形式：
  ```
  {
  "status":200,
  "message":"OK",
  "data":...
  }
  ```
  
  status类型：
  
  |status|description|
  |---|---|
  |200|success|
  |403|权限不足|
  
* 签到端和管理签到端公用一套接口，后台分别给予不同权限
* 权限说明 role  
  共有2种用户类型：
  
  | userType | description | code |
  | -------- | ----------- | ---- |
  | 签到端用户(u) |可以进行签到|0|
  | 管理端用户(m) |对签到端用户管理|1|
  
  **文中将会分别在每个接口后面标志以表示接口权限情况**
  * (u):需要为签到端
  * (m):需要为管理端
  * (u|m):需要为签到端或管理端管理端，即只要登录即可
  * 缺省:不需要任何权限

## 具体请求

### 登录注册

* #### 登陆
  ```
  POST /login
  ```
  ##### input
  |Name|Type|Description|
  |---|---|---|
  |username|string|用户名|  
  |password|string|密码|
  #### response
  ```
   {
  "status":200,
  "message":"success",
  "data":{
    
    "userType": 0 // 0 用户  |   1 管理员 | 2 账户不存在 |3 密码错误
  }
  }
  

  ```
  
* #### 检测是否登陆
  ```
  POST /hasLoggedIn
  ```
  #### response
  ```
  {
  "status":200,
  "message":"success",
  "data":true //true 已登录  | false 未登录
  }
  ```

* #### 注销
  ```
  POST /logout
  ```
  
   #### response
  ```
  {
  "status":200,
  "message":"success",
  "data":true //true 成功注销  | false 失败
  }
  ```

* #### 注册
  ```
  POST /register
  ```
  ##### input
  
  |Name|Type|Description|
  |---|---|---|
  |username|string|用户名|
  |password|string|密码|
  |pay_password|string|支付密码|
  |name|string|姓名|
  |phone|string|电话号码|
  |profile_a|file|本人身份证照片带脸的|
  |profile_b|file|本人身份证照片戴国徽的|
  |id|string|身份证号|
  |gender|number|性别  0是女 1是男 | 
  
### 用户
* #### 获取用户信息
  ```
  GET /user
  ```
  ##### params
  
  |Name|Type|Description|
  |---|---|---|
  |username|string|用户名|
  **id缺省时 返回自己的信息**
  //后台确认用户权限 只有管理员才能看别人的信息
  #### response
  ```
  {
  "status":200,
  "message":"success",
  "data":{
    "userType":0/1,   
    "username":"liupenghao",//用户名
    "name":"刘鹏昊",//姓名
    "gender':0/1 //0女 1男
    "phone":18560125097  //电话号码
    "id":370102199711111111  //身份证号
    "credit":15   //信誉值
  }
  }
  ```
* #### 修改用户信息
  ```
  POST /user
  ```
  ##### input
  要修改的字段
  |Name|Type|Description|
  |---|---|---|

  |password|string|密码|
  |phone|string|电话号码|

  
### 房源
一个房源由用户创建

house： 

|key|type|Description|
|house_id|string|房产证号|
|owner_id|string|拥有者的身份证号|
|owner_name|string|拥有者的姓名|
|owner|string|拥有者的账号|
|state|number|可租用状态, 0 下线状态 1 发布状态 2 删除状态|  //删除状态也就是用户自己选择删除记录
|role|number|调用接口人的身份 1用户 2管理员 0其他|
|low_location|json|简略地址|
|specific_location|string|详细地址 "2号楼3单元1801"|
|floor|number|楼层 eg. 18 |
|elevator|boolean|true 有电梯 false 无电梯 |
|lease|number|租金 5000 |
|lease_inter|number|0 全部 1 500元以下 2 500-1000元   3 1000-1500元 4 1500-2000元 5 2000元以上 |
|lease_type|number| 0 全部 1 整租 2 合租 |
|house_type|number| 0 全部 1 一室 2 二室 3 其他 |
|house_credit|number| 房子信誉值  eg: 16 |
|house_comment|array| 房子评论  |



low_location：

|key|type|Description|
|---|---|---|
|provi|string|省份|
|city|string|市|
|sector|string|区|
|commu_name|string|小区名|


* #### 获取房源详细信息(u|m)
  ```
  GET /house
  ```
  ##### parameters
  |Name|Type|Description|
  |---|---|---|
  |house_id|string|房产证号|
  ##### <a name="get-group-response">response</a>
  **注：**

  ```
  {
  "status":200,
  "message":"success",
  "data":{
      "house_id":"sdfwenk31345",//房产证号
      "owner_id":"37012506546564"，//房主身份证号
      "owner":"quyanso111",//拥有者的账号
      "owner_name":"曲延松",//拥有者的姓名
      "role":1,     //调用这个接口的人是管理者还是用户
      
      
|house_id|string|房产证号|
|owner_id|string|拥有者的身份证号|
|owner_name|string|拥有者的姓名|
|owner|string|拥有者的账号|
|state|number|可租用状态, 0 下线状态 1 发布状态 2 删除状态|  //删除状态也就是用户自己选择删除记录
|role|number|调用接口人的身份 1用户 2管理员 0其他|
|low_location|json|简略地址|
|specific_location|string|详细地址 "2号楼3单元1801"|
|floor|number|楼层 eg. 18 |
|elevator|boolean|true 有电梯 false 无电梯 |
|lease|number|租金 5000 |
|lease_inter|number|0 全部 1 500元以下 2 500-1000元   3 1000-1500元 4 1500-2000元 5 2000元以上 |
|lease_type|number| 0 全部 1 整租 2 合租 |
|house_type|number| 0 全部 1 一室 2 二室 3 其他 |
|house_credit|number| 房子信誉值  eg: 16 |
|house_comment|array| 房子评论  |
      
      //以下为附加信息
      
      //if 管理者||成员 附加state
      "state":true,
      
      //if 管理者 附加members
      "members":[
      {
        "username":"quyansong",
        "name":"曲延松",
        "state":true
      },
      ],
      
      //if 成员 附加checked
      "checked":true，
      
      //if 管理者||成员 附加needLocation
      "needLocation":true,
      
      //if 管理者&&needLocation 附加location
      "location":{
        "lng":116.331398,
        "lat":39.897445,
        "effectiveDistance":50
      },
      
      //if 管理者||成员 附加needFace
      "needFace":true,

  }
  }
  ```
  * #### 搜索
  ```
  POST /search
  ```
  ##### input
  搜索
  |Name|Type|Description|
  |low_location|json|简略地址|
  |lease_inter|number|0 全部 1 500元以下 2 500-1000元   3 1000-1500元 4 1500-2000元 5 2000元以上 |
  |house_type|number| 0 全部 1 一室 2 二室 3 其他 |
  |lease_type|number| 0 全部 1 整租 2 合租 |
   #### response
  ```
  {
  "status":200,
  "message":"success",
  "data":[
        {
          "low_location":"山东省济南市历下区**小区",
          "lease":"5000",
          "house_type":"2",
          "lease_type":"1"
        }，
        ...
    ]
  }
  ```
  
  
* #### 获取已加入的/创建的群体的信息(u|m)
  ```
  GET /group/list
  ```
  ##### response
  response.data为数组 返回方式同上<a href="#get-group-response">↑↑↑↑↑</a>

* #### 创建群体(m)
  ```
  POST /group/add
  ```
  ##### input
  |Name|Type|Description|
  |---|---|---|
  |name|string|群体名称|
  ##### response
  ```
  {
  "status":200,
  "message":"OK",
  "data":"Ojewexcin"//口令即id
  }
  ```
* #### 加入群体(u)
  ```
  POST /group/join
  ```
  ##### input
  |Name|Type|Description|
  |---|---|---|
  |id|string|群体口令|
* #### 退出群体(u)
  ```
  POST /group/quit
  ```
  ##### input
  |Name|Type|Description|
  |---|---|---|
  |id|string|群体口令|
* #### 修改群体信息(m)
  可修改的字段有：**name**,**needLocation**,**needFace**,
  ```
  POST /group/update
  ```
  ##### input
  |Name|Type|Description|
  |---|---|---|
  |id|string|群体口令|
  |...|...|要修改的字段|
  
  要为群体增加位置信息时
   
  |key|value|
  |---|---|
  |id|sdfweccvdfg|
  |needLocation|true|
  |lng|116.331398|
  |lat|39.897445|
  |effectiveDistance|200|
  
  
  
* #### 删除群体(m)
  ```
  POST /group/delete
  ```
  ##### input
  |Name|Type|Description|
  |---|---|---|
  |id|string|群体口令|
  
### 签到
  
* #### 获取当日签到情况(u)
  获取当日签到情况，错过签到,已签到，待签到，暂未开启签到
  ```
  GET /check/status
  ```
  ##### response
  ```
  {
  "status":200,
  "message":"OK",
  "data":{
    //已完成签到
    "done":[
        {
            "groupId":"sdfcvsd",//群体口令
            "groupName":"测试",//群体名称
            "startUpTime":"",//开启时间
        },
        ....
    ],
    //已错过签到
    "missed":[
        //格式同上
    ],
    //当前处于开启状态的签到
    "open":[
        //格式同上
    ],
    //暂未开启的签到
    "future":[
        //格式同上
    ],
  }
  }
  ```

* #### 签到(u)
  ```
  POST /check/check
  ```
  ##### input
  |Name|Type|Description|
  |---|---|---|
  |id|string|群体口令|
  
  对于需要地理位置的签到，附加以下字段
  
  |Name|Type|Description|
  |---|---|---|
  |lng|number|经度|
  |lat|number|纬度|
  
  对于需要人脸验证的签到，附加以下字段
  
  |Name|Type|Description|
  |---|---|---|
  |face|file|实时人像图片|
  
* #### 开启签到(m)
  即刻签到
  ```
  POST /check/enable
  ```
  ##### input
  |Name|Type|Description|
  |---|---|---|
  |id|string|群体口令|
  
* #### 结束签到(m)
  ```
  POST /check/disable
  ```
  ##### input
  |Name|Type|Description|
  |---|---|---|
  |id|string|群体口令|


### 签到计划

除即刻签到外的另一种签到方式，类似手机闹钟，可添加多个计划

选择开启时间和持续时间  可选项：一次有效或重复

暂定最多添加5个(不重要的上限常数 可以只在前端加限制 后端可以不限制)

字段：

|key|type|Description|
|---|---|---|
|scheduleId|string|计划ID|
|startUpTime|string|开启时间|
|duration|number|持续时间，单位：分钟，默认：20|
|repeat|string|在周几重复 用，隔开 如果为空 则不重复,如'1,2,5'表示在周一二五开启|
|enable|boolean|是否开启，逻辑同手机闹钟的开启关闭|

**注意  这里的startUpTime不是日期时间 而是单纯的时间，格式为HH:mm**

* #### 获取所有签到计划(m)
  ```
  GET /schedule
  ```
  ##### parameters
  |Name|Type|Description|
  |---|---|---|
  |id|string|群体口令|
  ##### response
  ```
  {
  "status":200,
  "message":"OK",
  "data":[
    {
        scheduleId:'0dsfwefsd',
        startUpTime:'03:15',
        duration:20,
        enable:true,
        repeat:'1,3,5',
    }
  ]
  }
  ```
  
* #### 添加签到计划(m)
  ```
  POST /schedule/add
  ```
  ##### input
  |Name|Type|Description|
  |---|---|---|
  |id|string|群体口令|
  |stratUpTime|string|开启时间|
  |duration|number|持续时间|
  |repeat|string|重复|
  |enable|boolean|是否启用|
  
  ##### response
  ```
  {
  "status":200,
  "message":"OK",
  "data":'dsf515dsf'//scheduleId
  }
  ```
  
  
* #### 修改签到计划(m)
  ```
  POST /schedule/update
  ```
  ##### input
  |Name|Type|Description|
  |---|---|---|
  |scheduleId|string|计划ID|
  |...|...|要修改的字段|
  
* #### 移除签到计划(m)
  ```
  POST /schedule/delete
  ```
  ##### input
  |Name|Type|Description|
  |---|---|---|
  |scheduleId|string|计划ID|


### 签到历史记录

格式为Array&lt;Record&gt;  
**Record**为某次签到的一条记录,拥有以下结构：

|Name|Type|Description|
|---|---|---|
|id|string|record id|
|startUpDateTime|string|开启时间|
|duration|number|持续时间，单位：分钟|
|checked|boolean|是否完成签到（只在请求者为群体成员时才附加）|
|leave|boolean|是否请假（只在请求者为群体成员时才附加）|
|done|Array|完成签到的成员的列表|
|missed|Array|错过签到的成员的列表|


* #### 获取某群体的历史记录(u|m)
  允许的请求者：1.该群体的创建者  2.该群体的成员
  ```
  GET /history/{id}
  ```
  例如：
  ```
  GET /history/asdfs
  ```
  ##### parameters
  |Name|Type|Description|
  |---|---|---|
  |id|string|口令|
  
  ##### response
  如果是群体的创建者：
  ```
  {
  "status":200,
  "message":"OK",
  "data":[
    {
        "id":"sdfddscvx"
        "startUpDateTime":'2018-07-07T03:13:03Z',
        "duration":20,
    }
  ]
  }
  ```
  如果是群体的成员：
  ```
  {
  "status":200,
  "message":"OK",
  "data":[
    {
        "id":"sdfddscvx"
         "startUpDateTime":'2018-07-07T03:13:03Z',
        "duration":20,
        "checked":false
        "leave":true
    }
  ]
  }
  ```
  

* #### 获取历史记录中的某条记录的信息(m)
  允许的请求者：1.该群体的创建者
  ```
  GET /record/{id}
  ```
  例如：
  ```
  GET /record/asdfs
  ```
  ##### parameters
  |Name|Type|Description|
  |---|---|---|
  |id|string|record id|
  
  ##### response
  ```
  {
  "status":200,
  "message":"OK",
  "data":[
    "id":"sdfddscvx"
    "startUpDateTime":'2018-07-07T03:13:03Z',
    "duration":20,
    "done":[
        {
          "username":"quyansong",
          "name":"曲延松"
        }，
        ...
    ],
    "missed":[
        {
          "username":"quyansong",
          "name":"曲延松"
        }，
        ...
    ],
    "leave":[
            {
              "username":"quyansong",
              "name":"曲延松"
            }，
            ...
        ],
  ]
  }
  ```
  
* #### 获取群体内某个成员的签到历史记录(m)
  允许的请求者：1.该群体的创建者  
  *逻辑上讲这也可以属于群体成员的信息，但我还是放到了这儿*
  ```
  GET /group/{group_id}/user/{user_name}
  ```
  例如：
  ```
  GET /group/asfdsgh/user/quyasong
  ```
  ##### parameters
  |Name|Type|Description|
  |---|---|---|
  |group_id|string|group id|
  |user_name|string|username|
  
  ##### response
  ```
  {
  "status":200,
  "message":"OK",
  "data":[
     {
         "id":"sdfddscvx"
         "startUpDateTime":'2018-07-07T03:13:03Z',
         "duration":20,
         "checked":false
         "leave":true
     },
     ...
  ]
  ```
  **是的你没看错  和上面那条接口的请求者是成员时的response一致，逻辑上也应如此**

* #### 学生发起请假(s)


允许的请求者：1.群体成员

  ```
  POST /group/leave
  ```

  ##### parmas

  |Name|Type|Description|
  |---|---|---|
  |group_id|string|群体名称|
  |result|string|请假理由|




* #### 老师获取群体请假请求(m)
 ```
  GET /leave
  ```
  ##### params



  |Name|Type|Description|
  |---|---|---|
  |group_id|string|群体名称|

  #### response
  ```
  {
  "status":200,
  "message":"success",
  "data":{
    leaves:[
      {
      "leave_id":"xxoo",
      "username":"xxdd"，
      "name":"曲延松",
      "result":"我生病了",
      },
      {
      "leave_id":"xx00",
      "username":"aabb",
      "name":"刘鹏昊"，
      "result":"我生病了",
      },
    ]
  }
  }
  ```

* #### 请假请求反馈(m)
 ```
  POST /leave/response
  ```
  ##### params

  |Name|Type|Description|
  |---|---|---|
  |leave_id|string|假条id|
  |leave_response|boolean|请求反馈|



  #### response
  ```
  {
  "status":200,
  "message":"OK",
  }
  ```


  * #### 老师获取群体统计信息(m)
```
  GET /group/{group_id}/user/record
  ```

例如：
  ```
  GET /group/asfdsgh/user/record
  ```
  ##### parameters
  |Name|Type|Description|
  |---|---|---|
  |group_id|string|group id|


  ##### response
  ```
  {
  "status":200,
  "message":"OK",
  "data":[
     {
         "username":"quyans"
         "name":"曲延松"
         "missed":15
         "leave":3
         "done":20
         "done_percent":"97%"
     },
     {
          "username":"liupenghao"
          "name":"刘鹏昊"
          "missed":15
          "leave":3
          "done":20
          "done_percent":"97%"
      },
     ...
  ]
  ```
