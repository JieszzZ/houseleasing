接口文档
# 接口文档

负责人：曲延松

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
  
* 签到端和管理端公用一套接口，后台分别给予不同权限
* 权限说明 role  
  共有2种用户类型：
  
  | userType | description | code |
  | -------- | ----------- | ---- |
  | 签到端用户(u) |可以进行日常操作|0|
  | 管理端用户(m) |对用户进行管理|1|
  
  **文中将会分别在每个接口后面标志以表示接口权限情况**
  * (u):需要为签到端
  * (m):需要为管理端
  * (u|m):需要为签到端或管理端管理端，即只要登录即可
  * 缺省:不需要任何权限

## 具体请求

### 登录注册

* #### 登陆
  ```
  POST /user/login
  ```
  ##### input
  Name|Type  |Description
  ----|--|----  
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
  POST /user/hasLoggedIn
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
  POST /user/logout
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
  POST /user/register
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
  GET /user/user
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
    "myhouse":[
        {
            "commu_name":"奥龙官邸"，
            "house_hash"："sdfaafadsfasd"
        },
        
        {
            "commu_name":"茗筑美嘉"，
            "house_hash"："sdfsdafsdfadf"
        },
        。。。
    ]
  }
  }
  ```


* #### 获取用户余额
  ```
  GET /user/balance
  ```
  ##### params
  
  |Name|Type|Description|
  |---|---|---|
  |username|string|用户名|
  
  **username缺省时 返回自己的信息**
  //后台确认用户权限 只有管理员才能看别人的信息
  
  #### response
  ```
  {
  "status":200,
  "message":"success",
  "data":{
    "username":"liupenghao",//用户名
    "name":"刘鹏昊",//姓名
    "balance":85500  // 余额
  }
  }
  ```

* #### 充值（u）
  ```
  POST /user/account
  ```
  ##### input
  
  |Name|Type|Description|
  |---|---|---|
  |money|number|充值金额 如 5000|

  #### response
  ```
  {
  "status":200,
  "message":"success",
  "data":{
    "username":"liupenghao",//用户名
    "name":"刘鹏昊",//姓名
    "balance":85500  // 充值后余额
  }
  }
  ```

* #### 获取交易记录 (u/m)
  ```
  GET /user/trans_record
  ```
  ##### params
  
  |Name|Type|Description|
  |---|---|---|
  |username|string|用户名|
  
  **id缺省时 返回自己的交易记录**
  //后台确认用户权限 只有管理员才能看别人的交易记录
  #### response
  ```
  {
  "status":200,
  "message":"success",
  "data":{
    "username":"liupenghao",//用户名
    "name":"刘鹏昊",//姓名
    “record”:[
        {
        "time": 2019-1-13    //交易时间
        "gas": 500  //花费的手续费
        “low_location”：{
                “provi”:"山东省"，
                “city”："济南市"，
                “sector”:"历下区",
                "commu_name":"奥龙官邸"
        }，
       "specific_location":"2号楼3单元1801",
        “record”:[
          {"time": 2019-1-13    //交易时间
            "gas": 500  //花费的手续费
            “low_location”：{
                “provi”:"山东省"，
                “city”："济南市"，
                “sector”:"历下区",
                "commu_name":"奥龙官邸"
            }，
          "specific_location":"2号楼3单元1801",
          }，
        ...
       ]，
     }
  }
  ```

* #### 获取我的房子 (u)


  ```
  GET /user/myhouse
  ```
   ##### params
  
  |Name|Type|Description|
  |---|---|---|
  |house_hash|string|房子哈希|

 #### response
 ```
 
  {
      "status":200,
      "message":"success",
      "data":{
    
        "house_pic":[
            "sdfadfasfasfasdfa" ，   //图片一的哈希值
            “sdfadsfadsfasf”,
            "sadfadsfasfsa"
        ]
      "house_hash":"sdfwenk31345",//房产证号
      "owner_id":"37012506546564"，//房主身份证号
      "verify":"true"   //经过验证
      "owner":"quyanso111",//拥有者的账号
      "owner_name":"曲延松",//拥有者的姓名
      "role":1,     //调用这个接口的人是管理者还是用户
      "state":0,    //可租用状态
      "low_location":{
          "provi":"山东省",
          "city":"济南市",
          "sector":"历下区",
          "commu_name":"奥龙官邸",
      },
      "specific_location":"2号楼3单元1801",
      "floor":18
      "elevator":true
      "lease":3800
      "house_type":1  // 1 一室 2 二室 3 其他
      "house_credit":16,
      "house_comment":[
        {
          "user_id":"quyans111",   //评论人账号名
          "comment":"这个房子挺不错适合居住",   //评论内容
          "comment_pic":[
                "sdfadfasfasfasdfa" ，   //图片一的哈希值
                “sdfadsfadsfasf”,
                "sadfadsfasfsa"
          ],
          ......
        }
      ]
  }
}
  ```


* #### 修改我的房子 (u)
  ```
  POST /user/myhouse
  ```
 
   ##### input
  
  |Name|Type|Description|
  |---|---|---|
  |house_hash|string|要修改的房子的哈希|
  |state|number|可租用状态 0 不可租用 1可租用|
  |elevator|boolean|有无电梯|
  |lease|3800|租金|
  |phone|string|电话号码|
  
  
  //不可添加图片
  




* #### 修改用户信息
  ```
  POST /user/info
  ```
  ##### input
  
  
  |Name|Type|Description|
  |---|---|---|
  |password|string|密码|
  |phone|string|电话号码|

  
### 房源
一个房源由用户创建

house： 

|key|type|Description|
|---|---|---|
|house_id|string|房产证号|
|house_hash|string|房产证号hash|
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
|verify|boolean| 是否经过验证，true 通过了 false没通过  |



low_location：

|key|type|Description|
|---|---|---|
|provi|string|省份|
|city|string|市|
|sector|string|区|
|commu_name|string|小区名|


* #### 获取房源详细信息(u|m)
  ```
  GET /house/speinfo
  ```
  ##### parameters
  |Name|Type|Description|
  |---|---|---|
  |house_hash|string|房产证号|
  ##### <a name="get-group-response">response</a>
  **注：**

  ```
  {
  "status":200,
  "message":"success",
  "data":{
       "house_pic":[
            "sdfadfasfasfasdfa" ，   //图片一的哈希值
            “sdfadsfadsfasf”,
            "sadfadsfasfsa"
        ]
      "house_hash":"sdfwenk31345",//房产证号
      "owner_id":"37012506546564"，//房主身份证号
      "verify":"true"   //经过验证
      "owner":"quyanso111",//拥有者的账号
      "owner_name":"曲延松",//拥有者的姓名
      "role":1,     //调用这个接口的人是管理者还是用户
      "state":0,    //可租用状态
      "low_location":{
          "provi":"山东省",
          "city":"济南市",
          "sector":"历下区",
          "commu_name":"奥龙官邸",
      },
      "specific_location":"2号楼3单元1801",
      "floor":18
      "elevator":true
      "lease":3800
      "house_type":1  // 1 一室 2 二室 3 其他
      "house_credit":16,
      "house_comment":[
        {
          "user_id":"quyans111",   //评论人账号名
          "comment":"这个房子挺不错适合居住",   //评论内容
          "comment_pic":[
                "sdfadfasfasfasdfa" ，   //图片一的哈希值
                “sdfadsfadsfasf”,
                "sadfadsfasfsa"
          ],
          ......
        }
      ]
  }
  }
  ```


   * #### 搜索
  ```
  POST /house/search
  ```
  ##### input
  |Name|Type|Description|
  |---|---|---|
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
          "photo":"sadfadsfadf"   //一张图片的hash
          "low_location":"山东省济南市历下区**小区",
          "lease":"5000",
          "house_type":"2",
          "lease_type":"1",
        }，
        ...
    ]
  }
  ```
  
   * #### 联系房主
  ```
  POST /user/contact_owner
  ```
  ##### input
  |Name|Type|Description|
  |---|---|---|
  |house_hash|string|房子房产证hash|
   #### response
  ```
  {
  "status":200,
  "message":"success",
  "data": {
          "phone":"12345678910"
        }
  }
  ```

 * #### 评价房子
 
  ```
  POST /house/valuation
  ```
  ##### input
  |Name|Type|Description|
  |---|---|---|
  |house_hash|string|房子房产证hash|
  |house_level|number|1~5 五个级别|
  |comment_word|string|文字评价|
  |comment_pic|file|图片评价|
   #### response
  ```
  {
  "status":200,
  "message":"success",
  }
  ```
  
  
  * #### 获取所有用户信息(m)
  ```
  GET /user/all_info
  ```

  ```
  {
  "status":200,
  "message":"success",
  "data":{
    "users":[
     {
      "username":"liupenghao",//用户名
      "name":"曲延松",         //姓名
      "gender':0            //0女 1男
      "phone":18560125097  //电话号码
      "id":370102199711111111  //身份证号
      "credit":15         //信誉值
    }，
    
    {
      "username":"liupenghao",//用户名
      "name":"曲延松",         //姓名
      "gender':1            //0女 1男
      "phone":18560125097  //电话号码
      "id":370102199711111111  //身份证号
      "credit":15         //信誉值
    },
    
     {
      "username":"liupenghao",//用户名
      "name":"张三",         //姓名
      "gender':0            //0女 1男
      "phone":18560125097  //电话号码
      "id":370102199711111111  //身份证号
      "credit":15         //信誉值
    },
  }
  }
  ```
  
  * #### 修改用户信息(m)
 
  ```
  POST /user/changeinfo
  ```
  ##### input
  |Name|Type|Description|
  |---|---|---|
  |username|string|用户名|
  |credit|number|eg:35  信誉值|
  
   #### response
  ```
  {
  "status":200,
  "message":"success",
  }
  ```
  
    * #### 获取房屋列表(m)
    
  ```
  GET /house/allinfo (m)
  ```


  ```
  {
  "status":200,
  "message":"success",
  "data":{
      "verified":[            //通过验证的房子
          {
          "photo":"sadfadsfadf"   //一张图片的hash
          "low_location":"山东省济南市历下区**小区",
          "lease":"5000",
          "house_type":"2",
          "lease_type":"1",
         }，
         
         {
          "photo":"sadfadsfadf"   //一张图片的hash
          "low_location":"山东省济南市历下区**小区",
          "lease":"5000",
          "house_type":"2",
          "lease_type":"1",
         }，
         
         {
          "photo":"sadfadsfadf"   //一张图片的hash
          "low_location":"山东省济南市历下区**小区",
          "lease":"5000",
          "house_type":"2",
          "lease_type":"1",
         }，
         
        ],
        
       
       "non_verified":[            //通过验证的房子
          {
          "photo":"sadfadsfadf"   //一张图片的hash
          "low_location":"山东省济南市历下区**小区",
          "lease":"5000",
          "house_type":"2",
          "lease_type":"1",
         }，
         
         {
          "photo":"sadfadsfadf"   //一张图片的hash
          "low_location":"山东省济南市历下区**小区",
          "lease":"5000",
          "house_type":"2",
          "lease_type":"1",
         }，
         
         {
          "photo":"sadfadsfadf"   //一张图片的hash
          "low_location":"山东省济南市历下区**小区",
          "lease":"5000",
          "house_type":"2",
          "lease_type":"1",
         }，
        ],
  }
  }
  ```
  
  
  
  * #### 租客发起签约请求(u)
允许的请求者：1.用户

  ```
  POST /tract/set
  ```

  ##### parmas

  |Name|Type|Description|
  |---|---|---|
  |house_hash|string|房产证的哈希|
  |owner|string|房主账号|
  
  ##### response
  ```
  {
   "status":200,
   "message":"success",
   "data":{
           "requestID":"xxoo",
           }
   }
  ```
  

* #### 房主获取所有与他有关的签约请求(m)
 ```
  GET /tract/get
  ```


  #### response
  ```
  {
  "status":200,
  "message":"success",
  "data":{
    leaves:[
      {
      "requestID":"xxoo",
      "username":"xxdd"，   //请求的人的用户名
      "name":"曲延松",       //请求的人的姓名
      "house_hash":"adfafd",    //请求的房子hash
      "commu_name":"茗筑美嘉"，  //房子小区名
      },
      {
      "requestID":"xsxoo",
      "username":"xxdd"，   //请求的人的用户名
      "name":"曲延松",       //请求的人的姓名
      "house_hash":"adfafd",    //请求的房子hash
      "commu_name":"奥龙官邸"，  //房子小区名
      },
    ]
  }
  }
  ```

* #### 请假请求反馈(m)
 ```
  POST /tract/response
  ```
  ##### params

  |Name|Type|Description|
  |---|---|---|
  |requestID|string|请求id|
  |request_response|boolean|请求反馈 true 同意签约  false 不同意签约|
  |pay_password|string|请求反馈 true 同意签约  false 不同意签约|

  #### response
  ```
  {
  "status":200,
  "message":"OK",
  }
  ```
  
  * #### 请假反馈获取(u)
 ```
  GET /leave/{requestID}/feedback
  ```
  ```
  例如：
  GET /leave/XIDJGE/feedback  
  ```

  #### response
  ```
  {
  "status":200,
  "message":"success",
  "data":{
        "checkID":"aadfif",
        "groupID":"xxoo",
        "request_status":0,                 # 0：未处理  1：同意   2：拒绝
   },
  }
  ```
  
  
  

  

