# houseleasing<br>
###——基于区块链和IPFS的房屋租赁系统
###——写于2019暑期实训

---
>开发参与人员
>>[@MokeLock](https://github.com/MokeLock)<br>
>>[@Accelerator131415](https://github.com/Accelerator131415)<br>
>>[@Lijiayu51578](https://github.com/Lijiayu51578)<br>
>>[@IsaacDiane](https://github.com/IsaacDiane)<br>
>>[@LXT0](https://github.com/LXT0)<br>
>>[@fkiki789](https://github.com/fkiki789)<br>

---
###写于2019：07：12
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;该项目到现在也进行了大概四周的时间，大家从无到有的在区块链和IPFS上尝试进行开发，
期间也走了不少弯路，磕磕绊绊到现在也算是勉强完成了既定的目标，基本功能均已实现。<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;项目答辩时和老师交流了许多，发现我们仍有很多内容需要完善，尤其是关于用户使用体验
方面，在这里把一些可能的功能性问题罗列下来，以便后续完善：
* 热评，如评论采纳频次、优秀评论推荐等
* 交易过程中房主可查看申请人交易历史，以便判断申请人可信程度
* 采用多节点部署，这部分是我在设计过程中未想到的部分，虽然采用了8个节点的区块链和
IPFS服务节点，但是应用服务器与节点的连接依然是对单节点的连接，这违背了分布式的开发
思想，后续应添加节点池的设定，应用服务器与多个节点维护连接状态并进行维护，查询数据
则采用少数服从多数的原则，避免某个连接的失效带来的影响