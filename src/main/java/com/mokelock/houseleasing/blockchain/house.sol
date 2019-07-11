pragma solidity > 0.5.0 < 0.6.0;
/*
*编写人：袁虎
*更新时间：2019/06/26
*更新说明：现在签订的合同（下面称为合约）可以保存在智能合约上了
*			时间戳变量已生成，变量类型为uint（秒数）
*			每个以太坊账户对应了一个数组，用于存储他们的合约
*    		相应的数据结构也随之进行了更改
*			关于合同生效，合同完成之后要进行的操作还未解决
*			管理员应该向账户退回以太币，但是这个地方还未解决；
*
*
*更新时间：2019/06/27
*更新说明：添加了一个存储各个账户存储在合约账户上的余额，在合约完成后退款根据余额来退
*		   退款的方法已经完成
*
*更新时间：2019/06/28
*更新说明：把用户信息放在了智能合约里
*			把管理员的三张表加进了智能合约
*
*更新时间：2019/06/29
*更新说明：添加了一个管理员专用的对指定地址的指定订单的退款操作
*			在Money这个枚举类型里添加了一个deduction扣除状态；
*			对于用户向管理员提申请这个功能，貌似在合约里没有需要做的操作，就没有加
*
*更新时间：2019/07/01
*更新说明：将查管理员的表，修改管理员的表，修改用户信息的方法都拆开了
*			加了一个添加用户的方法，id和name只能在这里添加，其余地方无法修改
*			
*更新时间：2019/07/02
*更新说明: 添加了查找用户订单信息的功能，根据以太坊地址获取用户的订单数组，根据订单数组和位置
*			访问某一个订单的具体信息，根据以太坊地址获得用户订单的数量，以上操作结合起来可以遍历用户
*			的所有信息；
*
*			下午更新：
*			添加了两个方法返回一个订单的信息
*			将用户的性别属性加入到合约中
*			把查询生效中，完成等合约的方法设置成了public,合约可以调用了，接口信息添加到了下面接口说明中了
*			
*更新时间：2019/07/03
*更新说明：把昨天忘加的信誉值加上了
*
*			下午更新：添加了一个房子哈希，与之相关的接口：function submitOrder(address payable _aimer,string memory house_hash,uint32 coins) 
*        public payable balanceEnough(coins) onlyOnce(msg.sender,_aimer);参数里需要添加house_hash
*					 function findOrder_2(address _user,uint256 location)public view returns( uint256 sub_time,
*        uint256 effect_time,
*        uint256 finish_time,
*        Role role,
*        State state,
*        Money money，
*		 string house_hash)；返回值在最后添加了一个房子哈希；
*		删除了部分错误的文档说明；
*
*更新时间：2019/07/04
*更新说明：重大更新！！！为适应合约坑爹的不能超过15K大小阈值的设定，全面整改代码，修改代码情况如下：
*			删除了几乎所有modifier的条件判断，所以执行前是否能够满足条件请在应用层判断
*			合并了回应合约的和第二次签名的方法，拒绝和接受都在一个方法里，修改了方法名！！！请注意！！！添加了一个参数bool，详情看接口
*			合并了退款操作，无论合约怎么样都由一个方法退款，差异在于第三个参数的输入值，详情看接口
*			调用的时候请一定一定注意方法名！！！
*			如果注释太多导致部署失败，请删除注释后重试；
*
*更新时间：2019/07/09
*更新说明：把枚举类型改成了uint
*			清除BUG！
*
*使用时可能遇到的问题：
*			1.向合约账户转账,我不清楚如何向合约账户转账，网上的资料不清不楚，所以我把部署地址当做是合约的地址，向合约转的
*			以太币我的实现都是转向部署地址的，如果有问题请和我联系
*			2.msg.value这个变量的影响依据我不太清楚，是否是在以太坊层次设置这个值的？如果是的话，那我可以将很多值都用
*			msg.value来表示，来避免出错，有懂得人的话请和我商量一下;
*			3.鲁棒性有待提高，第二版再解决；
*
*
*/

/*
*		智能合约介绍：
*		
*
*
*		
*		变量：	
*				
*			签名：0为未签名，1为同意，2为拒绝
*			角色：0为房客，1为房主
*			合约状态：0为已提交，1为生效中，2为已完成，3为已拒绝，4为已失败
*			押金状态：0为未提交，1为已提交，2为已退还，3为已扣押
*
*				address payable owner;									部署合约的地址
*   			mapping (address => contractInformation[]) contracts;   用于存储用户的合约
*				struct  contractInformation								交易合约
*			    {
*			        address payable submiter;							甲方地址
*			        address payable responder;							乙方地址
*			
*			        uint256 submiterEthCoin;								甲方押金
*			        uint256 aimerEthCoin;								乙方押金
*			        uint32 subFirstSign;									甲方第一次签名
*			        uint32 resFirstSign;									乙方第一次签名
*			        uint32 subSecondSign;									甲方第二次签名
*			        uint32 resSecondSign;									乙方第二次签名
*			        uint256 sub_time;									提交合约的时间
*			        uint256 effect_time;								合约生效的时间
*			        uint256 finish_time;								合约完成的时间
*			        uint32 role;											合约拥有者扮演的角色，房客，房主等
*			        uint32 state;										合约的状态
*			        uint32 money;										押金情况
*			  		string house_hash;									房子哈希
*			     }
*				struct userInformation									用户信息
*				{
*	    			string name;										姓名
*	    			string id;											身份证号
*	    			string pay_password;								支付口令
*	    			string IPFS_hash;									IPFS的哈希值
*	    			uint32 gender;										性别
*	  				uint32 credit;										信誉值
*				}
*	
*				struct adminTable										管理员保存的三张表
*				{
*	    			string user_account;								用户账号与以太坊账号对应表
*	    			string account_house_online;						以太坊账号与上线房子的表
*	    			string account_house_downline;						以太坊账号与下线房子的表
*	    		}
*				    
*	
*	函数：
*
*    function submitOrder(address payable _aimer,string memory house_hash,uint32 coins) 
*        public payable 
*	用户提交合约，输入参数为（房主的地址，房子哈希，押金）
*	
*
*	function respondOrder(address payable _submiter,bool res,uint32 coins)
*        public payable 
*	房主回应合约，发生在submitOrder之后，输入参数为（房客的地址，是否接受，押金）
*	
*	 function resSecond(address _submiter,address _aimer,bool res) 
*        public 
*	第二次签名，输入参数为（房客地址，房主的地址，回应结果），
*	双方都完成第二次签名后，订单状态会变成finished；可以执行退押金操作
*
*	 function withdraw(address payable _submiter,address payable _aimer,address _appealer)
*		public payable onlyOwner() onlyExistNoReturnOrder(_submiter,_aimer) returns(bool)
*	退押金，输入参数为（房客的地址，房主的地址，申诉者的地址），只有拥有订单且押金状态为subed的订单才能退钱，成功返回true,失败返回false
*		注意！！！第三个参数如果输入为管理员的地址，则按照双方完成合约来退钱，如果为合约双方其中一人的地址，则把钱全部退给此人；
*	
*	function findUser(string memory name,string memory id,string memory IPFS_hash,string memory phone,uint256 gender,uint256 credit)
*	寻找用户信息，根据以太坊账号地址查询，返回（姓名，身份证号，IPFS的哈希值，电话号码，性别，信誉值)
*
*	function findAdminTable()
*        public view returns(string memory user_account,string memory account_house_online,string memory account_house_downline)
*	查找管理员的三张表，任何人都可以调用，返回三张表的IPFS哈希值
*	
*	function addUser(address _who,string memory name,string memory id,string memory IPFS_hash,string memory phone,uint256 gender,uint256 credit)public returns(bool)
*	添加一个用户的信息，参数为用户的（以太坊地址，姓名，身份证号，IPFS的哈希值，电话号码,性别，信誉值）返回一个true表示执行成功
*
*	function postUserPhone(address _who,string memory phone)public returns(bool)
*	修改用户的电话号码      
*
*   function findUser_account_table()public view returns(string memory user_account)
*	查找用户——账号表，返回该表的哈希值 
*
*   function findAccount_house_online()public view returns(string memory account_house_online)
*	查找上线的房屋表，返回一个哈希值   
*
*   function findAccount_house_downline()public view returns(string memory account_house_downline)
*	查找下线的房屋表，返回一个哈希值   
*   
*   function postUser_account(string memory user_account)public returns (bool)
*	修改用户-账号表，返回一个true表示执行成功    
*
*   function postAccount_house_online(string memory account_house_online)public returns(bool)
*	修改上线房屋表，返回一个true表示执行成功    
*
*   function postAccount_house_downline(string memory account_house_downline)public returns(bool)
*   修改下线房屋表，返回一个true表示执行成功
*
*	function findOrdersNum(address _user)public returns(uint256)
*	根据用户的以太坊地址访问用户的订单数量
*
*	function findOrder_1(address _user,uint256 location)public view returns(address submiter,
*        address responder,
*        uint256 submiterEthCoin,
*        uint256 aimerEthCoin,
*        uint256 subFirstSign,
*        uint256 resFirstSign,
*        uint256 subSecondSign,
*        uint256 resSecondSign) 
*	根据以太坊地址获取用户的location位置的订单信息，返回值分别为（甲方地址，乙方地址，甲方押金，乙方押金，甲方第一次签名，乙方第一次签名，甲方第二次签名，乙方第二次签名）
*
*	function findOrder_2(address _user,uint256 location)public view returns( uint256 sub_time,
*        uint256 effect_time,
*        uint256 finish_time,
*        uint256 role,
*        uint256 state,
*        uint256 money，
*		 string house_hash)
*	根据以太坊地址获取用户的location位置的订单信息，返回值分别为（提交时间，生效时间，合约拥有者扮演的角色~房客房主等，合约的状态，合约押金状态,房子哈希）
*
*	
*	
*	//寻找生效的合约，_submiter是甲方的地址，_aimer是乙方的地址，_from是存储合约的地址
*	//若存在符合条件的effcting的合约，则返回该合约在数组中的位置
*	//若没有，则返回-1
*    function findEff_order(address _submiter,address _aimer,address _from)public view returns(int256)
*
*	//寻找提交的合约，_submiter是甲方的地址，_aimer是乙方的地址，_from是存储合约的地址
*	//若存在符合条件的submiting的合约，则返回该合约在数组中的位置
*	//若没有，则返回-1;
*   function findSub_order(address _submiter,address _aimer,address _from)public view returns(int256)
*
*	function findEnd_noReturned_oreder(address _submiter,address _aimer,address _from)public view returns(int256)
*	寻找完成但是没有退钱的订单，_submiter是甲方的地址，_aimer是乙方的地址，_from是存储合约的地址
*	若存在符合条件的subed&&finished的合约，则返回该合约在数组中的位置
*	若没有，则返回-1
*
*	function findFailed_noReturned_orderer(address _submiter,address _aimer,address _from)public view returns(int256)
*	寻找失败且没有退钱的订单，_submiter是甲方的地址，_aimer是乙方的地址，_from是存储合约的地址
*	若存在符合条件的subed&&failed的合约，则返回该合约在数组中的位置
*	若没有，则返回-1
*
*
*
*	其他函数不从外部调用，不介绍
*/
pragma solidity > 0.5.0 < 0.6.0;

contract blockChain
{
    address payable owner;
	
    constructor()public
    {
        owner = msg.sender;
    
    }

    struct  contractInformation
    {
        
        address payable submiter;
        address payable responder;
        
        uint256 submiterEthCoin;
        uint256 aimerEthCoin;
        uint32 subFirstSign;
        uint32 resFirstSign;
        uint32 subSecondSign;
        uint32 resSecondSign;
        uint256 sub_time;
        uint256 effect_time;
        uint256 finish_time;
        uint32 role;
        uint32 state;
        uint32 money;
        
        string house_hash;
    }
	
	struct userInformation
	{
	    string name;
	    string id;
	    string pay_password;
	    string IPFS_hash;
	    string phone;
	    uint32 gender;
	    uint32 credit;
	}
	
	struct adminTable
	{
	    string user_account;
	    string account_house_online;
	    string account_house_downline;
	}
	
    mapping (address => contractInformation[]) contracts;
    mapping(address => userInformation) userList;
    adminTable admintable;


    function submitOrder(address payable _aimer,string memory house_hash,uint256 coins) 
        public payable 
    {
        
        contractInformation memory cis;
        contractInformation memory cir;
       
        cis.submiter = msg.sender;
        cir.submiter = msg.sender;
        cis.responder = _aimer;
        cir.responder = _aimer;
        
        cis.house_hash = house_hash;
        cir.house_hash = house_hash;
        
        
        cis.submiterEthCoin = coins;
        cis.subFirstSign = 1;
        cir.subFirstSign = 1;
        cis.sub_time = now;
        cir.sub_time = now;
        cis.role = 0;
        cir.role = 1;
        cis.state = 0;
        cir.state = 0;
        cis.money = 1;
        cir.money = 0;
        
        contracts[msg.sender].push(cis);
        contracts[_aimer].push(cir);
    
        emit OrderSubmited(msg.sender,_aimer,now);
     
    }
    
    function respondOrder(address payable _submiter,bool res,uint32 coins)
        public payable 
    {   
        
        uint256 k = uint256(findSub_order(_submiter,msg.sender,_submiter));
        uint256 r = uint256(findSub_order(_submiter,msg.sender,msg.sender));
        if(res)
        {
            contracts[_submiter][k].aimerEthCoin = coins;
            contracts[_submiter][k].resFirstSign = 1;
            contracts[_submiter][k].effect_time = now;
            contracts[_submiter][k].state = 1;
            contracts[msg.sender][r].aimerEthCoin = coins;
            contracts[msg.sender][r].resFirstSign = 1;
            contracts[msg.sender][r].effect_time = now;
            contracts[msg.sender][r].state = 1;
            contracts[msg.sender][r].money = 1;
            
            emit OrderEffect(_submiter,msg.sender,now);
        }
        else if(!res)
        {
            contracts[_submiter][k].resFirstSign = 2;
            contracts[_submiter][k].state = 3;
            contracts[msg.sender][r].resFirstSign = 2;
            contracts[msg.sender][r].state = 3;
     
            emit OrderRejected(_submiter,msg.sender,now);
        }
        
     
    }
    
   
    function resSecond(address _submiter,address _aimer,bool res) 
        public 
    {
        uint256 inSub = uint256(findEff_order(_submiter,_aimer,_submiter));
        uint256 inRes = uint256(findEff_order(_submiter,_aimer,_aimer));
        
        if(res)
        {
            if(msg.sender == _submiter )
            {
                contracts[_submiter][inSub].subSecondSign = 1;
                contracts[_aimer][inRes].subSecondSign = 1;
            }
            
            else if(msg.sender == _aimer )
            {
                contracts[_submiter][inSub].resSecondSign = 1;
                contracts[_aimer][inRes].resSecondSign = 1;
            }
            
            if(contracts[_submiter][inSub].subSecondSign == 1 
                && contracts[_submiter][inSub].resSecondSign == 1)
                {
                    contracts[_submiter][inSub].state = 2;
                    contracts[_submiter][inSub].finish_time = now;
                    contracts[_aimer][inRes].state = 2;
                    contracts[_aimer][inRes].finish_time = now;
                }
            if(contracts[_submiter][inSub].state == 2)
                emit OrderFinished(_submiter,_aimer,now);
        }
        else if(!res)
        {
             if(msg.sender == _submiter )
            {
                contracts[_submiter][inSub].subSecondSign = 2;
                contracts[_aimer][inRes].subSecondSign = 2;
            }
            else if(msg.sender == _aimer )
            {
                contracts[_submiter][inSub].resSecondSign = 2;
                contracts[_aimer][inRes].resSecondSign = 2;
            }
            contracts[_submiter][inSub].state = 4;
            contracts[_aimer][inRes].state = 4;
            emit OrderFailed(_submiter,_aimer,now);
        }
        
    }
    
  
    function findSub_order(address _submiter,address _aimer,address _from)public view returns(int256)
    {
        uint l = contracts[_from].length;
        for(uint256 i=0;i<l;i++)
        {
            if(contracts[_from][i].submiter == _submiter && contracts[_from][i].responder == _aimer 
            && contracts[_submiter][i].state == 0)
            {
                return int(i);
            }
        }
        
        return -1;
        
    } 
    
	
    function findEff_order(address _submiter,address _aimer,address _from)public view returns(int256)
    {
        uint256 l = contracts[_from].length;
        for(uint256 i=0;i<l;i++)
        {
            if(contracts[_from][i].submiter == _submiter && contracts[_from][i].responder == _aimer
            && contracts[_from][i].state == 1)
            {
                return int(i);
            }
        }
        
        return -1;
    }
    
    function findEnd_noReturned_oreder(address _submiter,address _aimer,address _from)public view returns(int256)
    {
        uint256 l = contracts[_from].length;
        for(uint256 i=0;i<l;i++)
        {
            if(contracts[_from][i].submiter == _submiter && contracts[_from][i].responder == _aimer
            && contracts[_from][i].state == 2 && contracts[_from][i].money == 1)
            {
                return int(i);
            }
        }
        
        return -1;
    }
    
    function findFailed_noReturned_orderer(address _submiter,address _aimer,address _from)public view returns(int256)
    {
        uint256 l = contracts[_from].length;
        for(uint256 i=0;i<l;i++)
        {
            if(contracts[_from][i].submiter == _submiter && contracts[_from][i].responder == _aimer
            && contracts[_from][i].state == 4 && contracts[_from][i].money == 1)
            {
                return int(i);
            }
        }
    }
	
	modifier onlyExistNoReturnOrder(address _submiter,address _aimer)
	{
	    require(findEnd_noReturned_oreder(_submiter,_aimer,_submiter) >= 0 
	    || findEnd_noReturned_oreder(_submiter,_aimer,_submiter) >= 0
	    ,"You have no contrancts satisfy these:finished and submit money");
	    _;
	    
	}

    modifier isFinished(contractInformation memory _conts)
    {
        require(_conts.subSecondSign == 1 && _conts.resSecondSign == 1,"Contract is not finshed");
        _;
    }

    
    modifier onlyOwner()
    {
        require(msg.sender == owner);
        _;
    }
    event OrderSubmited(address _submiter,address _aimer,uint256 _time);
    event OrderEffect(address _submiter,address _aimer,uint256 _time);
    event OrderRejected(address _submiter,address _aimer,uint256 _time);
    event OrderFinished(address _submiter,address _aimer,uint256 _time);
    event OrderFailed(address _submiter,address _aimer,uint256 _time);
    event DepositWithdraw(address _submiter,address _aimer,uint256 _time);

    function contractsFinished(contractInformation memory _conts)view internal isFinished(_conts)
    {
        _conts.state = 2;
        _conts.finish_time = now;
    }

 
   
    function withdraw(address payable _submiter,address payable _aimer,address  _appealer)public payable onlyOwner() onlyExistNoReturnOrder(_submiter,_aimer) returns(bool)
    {
        uint256 k;
        uint256 a;
        uint256 km;
        uint256 am;
        if(_appealer == owner)
        {
             k = uint256(findEnd_noReturned_oreder(_submiter,_aimer,_submiter));
             a = uint256(findEnd_noReturned_oreder(_submiter,_aimer,_aimer));
             
        }
        else if(_appealer != owner)
        {
             k = uint256(findFailed_noReturned_orderer(_submiter,_aimer,_submiter));
             a = uint256(findFailed_noReturned_orderer(_submiter,_aimer,_aimer));
        }
        km = contracts[_submiter][k].submiterEthCoin;
        am = contracts[_aimer][a].aimerEthCoin;
        if(_appealer == owner)
        {
                contracts[_submiter][k].money = 2;
                contracts[_aimer][a].money = 3;
                
                if(!_submiter.send(km))
                {
                    contracts[_submiter][k].money = 1;
                    return false;
                }
                if(!_aimer.send(am))
                {
                    contracts[_aimer][a].money = 1;
                    return false;
                }
        }
         else if(_appealer != owner)
            {
                if(_appealer == _submiter)
                {
                    contracts[_submiter][k].money = 2;
                    contracts[_aimer][a].money = 3;
                     if(!_submiter.send(km+am))
                    {
                        contracts[_appealer][k].money = 1;
                        contracts[_submiter][a].money = 1;
                        return false;
                     }
                }
                else if(_appealer == _aimer)
                {
                    contracts[_submiter][k].money = 3;
                    contracts[_aimer][a].money = 2;
                     if(!_aimer.send(km+am))
                    {
                        contracts[_appealer][k].money = 1;
                        contracts[_submiter][a].money = 1;
                        return false;
                     }
                }
                
            }
           emit DepositWithdraw(_submiter,_aimer,now);
           return true;
        }

    function findOrdersNum(address _user)public view returns(uint256)
    {
        return contracts[_user].length;
    }
    
    function findOrder_1(address _user,uint256 location)public view returns(address submiter,
        address responder,
        uint256 submiterEthCoin,
        uint256 aimerEthCoin,
        uint256 subFirstSign,
        uint256 resFirstSign,
        uint256 subSecondSign,
        uint256 resSecondSign) 
    {
        submiter = contracts[_user][location].submiter;
        responder = contracts[_user][location].responder;
        submiterEthCoin = contracts[_user][location].submiterEthCoin;
        aimerEthCoin = contracts[_user][location].aimerEthCoin;
        subFirstSign = contracts[_user][location].subFirstSign;
        resFirstSign = contracts[_user][location].resFirstSign;
        subSecondSign = contracts[_user][location].subSecondSign;
        resSecondSign = contracts[_user][location].resSecondSign;
        
    }
    
    function findOrder_2(address _user,uint256 location)public view returns(uint256 sub_time,
        uint256 effect_time,
        uint256 finish_time,
        uint256 role,
        uint256 state,
        uint256 money,
        string memory house_hash)
        {
            sub_time = contracts[_user][location].sub_time;
            effect_time = contracts[_user][location].effect_time;
            finish_time = contracts[_user][location].finish_time;
            role = contracts[_user][location].role;
            state = contracts[_user][location].state;
            money = contracts[_user][location].money;
            house_hash = contracts[_user][location].house_hash;
        }
  
    function addUser(address _who,string memory name,string memory id,string memory IPFS_hash,string memory phone,uint256 gender,uint256 credit)public returns(bool)
    {
        userList[_who].name = name;
        userList[_who].id = id;
        userList[_who].IPFS_hash = IPFS_hash;
        userList[_who].phone = phone;
        userList[_who].gender = uint32(gender);
		userList[_who].credit = uint32(credit);
        return true;
    }
    
    function findUser(address _who)public view returns(string memory name,string memory id,string memory IPFS_hash,string memory phone,uint256 gender,uint256 credit)
    {
        
        name = userList[_who].name;
        id = userList[_who].id;
        IPFS_hash = userList[_who].IPFS_hash;
        phone = userList[_who].phone;
        gender = userList[_who].gender;
        credit = userList[_who].credit;
    }
    
    function postUserPhone(address _who,string memory phone)public returns(bool)
    {
        userList[_who].phone = phone;
    }

    function findUser_account_table()public view returns(string memory user_account)
    {
        user_account = admintable.user_account;
    }
    function findAccount_house_online()public view returns(string memory account_house_online)
    {
        account_house_online = admintable.account_house_online;
    }
    function findAccount_house_downline()public view returns(string memory account_house_downline)
    {
        account_house_downline = admintable.account_house_downline;
    }
    function postUser_account(string memory user_account)public returns (bool)
    {
        admintable.user_account = user_account;
        return true;
    }
    function postAccount_house_online(string memory account_house_online)public returns(bool)
    {
        admintable.account_house_online = account_house_online;
        return true;
    }
    function postAccount_house_downline(string memory account_house_downline)public returns(bool)
    {
        admintable.account_house_downline = account_house_downline;
        return true;
    }
}

