pragma solidity > 0.5.0 < 0.6.0;
/*
*��д�ˣ�Ԭ��
*����ʱ�䣺2019/06/26
*����˵��������ǩ���ĺ�ͬ�������Ϊ��Լ�����Ա��������ܺ�Լ����
*			ʱ������������ɣ���������Ϊuint��������
*			ÿ����̫���˻���Ӧ��һ�����飬���ڴ洢���ǵĺ�Լ
*    		��Ӧ�����ݽṹҲ��֮�����˸���
*			���ں�ͬ��Ч����ͬ���֮��Ҫ���еĲ�����δ���
*			����ԱӦ�����˻��˻���̫�ң���������ط���δ�����
*
*
*����ʱ�䣺2019/06/27
*����˵���������һ���洢�����˻��洢�ں�Լ�˻��ϵ����ں�Լ��ɺ��˿�����������
*		   �˿�ķ����Ѿ����
*
*����ʱ�䣺2019/06/28
*����˵�������û���Ϣ���������ܺ�Լ��
*			�ѹ���Ա�����ű�ӽ������ܺ�Լ
*
*����ʱ�䣺2019/06/29
*����˵���������һ������Աר�õĶ�ָ����ַ��ָ���������˿����
*			��Money���ö�������������һ��deduction�۳�״̬��
*			�����û������Ա������������ܣ�ò���ں�Լ��û����Ҫ���Ĳ�������û�м�
*
*����ʱ�䣺2019/07/01
*����˵�����������Ա�ı��޸Ĺ���Ա�ı��޸��û���Ϣ�ķ���������
*			����һ������û��ķ�����id��nameֻ����������ӣ�����ط��޷��޸�
*			
*����ʱ�䣺2019/07/02
*����˵��: ����˲����û�������Ϣ�Ĺ��ܣ�������̫����ַ��ȡ�û��Ķ������飬���ݶ��������λ��
*			����ĳһ�������ľ�����Ϣ��������̫����ַ����û����������������ϲ�������������Ա����û�
*			��������Ϣ��
*
*			������£�
*			�����������������һ����������Ϣ
*			���û����Ա����Լ��뵽��Լ��
*			�Ѳ�ѯ��Ч�У���ɵȺ�Լ�ķ������ó���public,��Լ���Ե����ˣ��ӿ���Ϣ��ӵ�������ӿ�˵������
*			
*����ʱ�䣺2019/07/03
*����˵�������������ӵ�����ֵ������
*
*			������£������һ�����ӹ�ϣ����֮��صĽӿڣ�function submitOrder(address payable _aimer,string memory house_hash,uint32 coins) 
*        public payable balanceEnough(coins) onlyOnce(msg.sender,_aimer);��������Ҫ���house_hash
*					 function findOrder_2(address _user,uint256 location)public view returns( uint256 sub_time,
*        uint256 effect_time,
*        uint256 finish_time,
*        Role role,
*        State state,
*        Money money��
*		 string house_hash)������ֵ����������һ�����ӹ�ϣ��
*		ɾ���˲��ִ�����ĵ�˵����
*
*����ʱ�䣺2019/07/04
*����˵�����ش���£�����Ϊ��Ӧ��Լ�ӵ��Ĳ��ܳ���15K��С��ֵ���趨��ȫ�����Ĵ��룬�޸Ĵ���������£�
*			ɾ���˼�������modifier�������жϣ�����ִ��ǰ�Ƿ��ܹ�������������Ӧ�ò��ж�
*			�ϲ��˻�Ӧ��Լ�ĺ͵ڶ���ǩ���ķ������ܾ��ͽ��ܶ���һ��������޸��˷�������������ע�⣡���������һ������bool�����鿴�ӿ�
*			�ϲ����˿���������ۺ�Լ��ô������һ�������˿�������ڵ���������������ֵ�����鿴�ӿ�
*			���õ�ʱ����һ��һ��ע�ⷽ����������
*			���ע��̫�ർ�²���ʧ�ܣ���ɾ��ע�ͺ����ԣ�
*
*����ʱ�䣺2019/07/09
*����˵������ö�����͸ĳ���uint
*			���BUG��
*
*ʹ��ʱ�������������⣺
*			1.���Լ�˻�ת��,�Ҳ����������Լ�˻�ת�ˣ����ϵ����ϲ��岻���������ҰѲ����ַ�����Ǻ�Լ�ĵ�ַ�����Լת��
*			��̫���ҵ�ʵ�ֶ���ת�����ַ�ģ�����������������ϵ
*			2.msg.value���������Ӱ�������Ҳ�̫������Ƿ�������̫������������ֵ�ģ�����ǵĻ������ҿ��Խ��ܶ�ֵ����
*			msg.value����ʾ������������ж����˵Ļ����������һ��;
*			3.³�����д���ߣ��ڶ����ٽ����
*
*
*/

/*
*		���ܺ�Լ���ܣ�
*		
*
*
*		
*		������	
*				
*			ǩ����0Ϊδǩ����1Ϊͬ�⣬2Ϊ�ܾ�
*			��ɫ��0Ϊ���ͣ�1Ϊ����
*			��Լ״̬��0Ϊ���ύ��1Ϊ��Ч�У�2Ϊ����ɣ�3Ϊ�Ѿܾ���4Ϊ��ʧ��
*			Ѻ��״̬��0Ϊδ�ύ��1Ϊ���ύ��2Ϊ���˻���3Ϊ�ѿ�Ѻ
*
*				address payable owner;									�����Լ�ĵ�ַ
*   			mapping (address => contractInformation[]) contracts;   ���ڴ洢�û��ĺ�Լ
*				struct  contractInformation								���׺�Լ
*			    {
*			        address payable submiter;							�׷���ַ
*			        address payable responder;							�ҷ���ַ
*			
*			        uint256 submiterEthCoin;								�׷�Ѻ��
*			        uint256 aimerEthCoin;								�ҷ�Ѻ��
*			        uint32 subFirstSign;									�׷���һ��ǩ��
*			        uint32 resFirstSign;									�ҷ���һ��ǩ��
*			        uint32 subSecondSign;									�׷��ڶ���ǩ��
*			        uint32 resSecondSign;									�ҷ��ڶ���ǩ��
*			        uint256 sub_time;									�ύ��Լ��ʱ��
*			        uint256 effect_time;								��Լ��Ч��ʱ��
*			        uint256 finish_time;								��Լ��ɵ�ʱ��
*			        uint32 role;											��Լӵ���߰��ݵĽ�ɫ�����ͣ�������
*			        uint32 state;										��Լ��״̬
*			        uint32 money;										Ѻ�����
*			  		string house_hash;									���ӹ�ϣ
*			     }
*				struct userInformation									�û���Ϣ
*				{
*	    			string name;										����
*	    			string id;											���֤��
*	    			string pay_password;								֧������
*	    			string IPFS_hash;									IPFS�Ĺ�ϣֵ
*	    			uint32 gender;										�Ա�
*	  				uint32 credit;										����ֵ
*				}
*	
*				struct adminTable										����Ա��������ű�
*				{
*	    			string user_account;								�û��˺�����̫���˺Ŷ�Ӧ��
*	    			string account_house_online;						��̫���˺������߷��ӵı�
*	    			string account_house_downline;						��̫���˺������߷��ӵı�
*	    		}
*				    
*	
*	������
*
*    function submitOrder(address payable _aimer,string memory house_hash,uint32 coins) 
*        public payable 
*	�û��ύ��Լ���������Ϊ�������ĵ�ַ�����ӹ�ϣ��Ѻ��
*	
*
*	function respondOrder(address payable _submiter,bool res,uint32 coins)
*        public payable 
*	������Ӧ��Լ��������submitOrder֮���������Ϊ�����͵ĵ�ַ���Ƿ���ܣ�Ѻ��
*	
*	 function resSecond(address _submiter,address _aimer,bool res) 
*        public 
*	�ڶ���ǩ�����������Ϊ�����͵�ַ�������ĵ�ַ����Ӧ�������
*	˫������ɵڶ���ǩ���󣬶���״̬����finished������ִ����Ѻ�����
*
*	 function withdraw(address payable _submiter,address payable _aimer,address _appealer)
*		public payable onlyOwner() onlyExistNoReturnOrder(_submiter,_aimer) returns(bool)
*	��Ѻ���������Ϊ�����͵ĵ�ַ�������ĵ�ַ�������ߵĵ�ַ����ֻ��ӵ�ж�����Ѻ��״̬Ϊsubed�Ķ���������Ǯ���ɹ�����true,ʧ�ܷ���false
*		ע�⣡���������������������Ϊ����Ա�ĵ�ַ������˫����ɺ�Լ����Ǯ�����Ϊ��Լ˫������һ�˵ĵ�ַ�����Ǯȫ���˸����ˣ�
*	
*	function findUser(string memory name,string memory id,string memory IPFS_hash,string memory phone,uint256 gender,uint256 credit)
*	Ѱ���û���Ϣ��������̫���˺ŵ�ַ��ѯ�����أ����������֤�ţ�IPFS�Ĺ�ϣֵ���绰���룬�Ա�����ֵ)
*
*	function findAdminTable()
*        public view returns(string memory user_account,string memory account_house_online,string memory account_house_downline)
*	���ҹ���Ա�����ű��κ��˶����Ե��ã��������ű��IPFS��ϣֵ
*	
*	function addUser(address _who,string memory name,string memory id,string memory IPFS_hash,string memory phone,uint256 gender,uint256 credit)public returns(bool)
*	���һ���û�����Ϣ������Ϊ�û��ģ���̫����ַ�����������֤�ţ�IPFS�Ĺ�ϣֵ���绰����,�Ա�����ֵ������һ��true��ʾִ�гɹ�
*
*	function postUserPhone(address _who,string memory phone)public returns(bool)
*	�޸��û��ĵ绰����      
*
*   function findUser_account_table()public view returns(string memory user_account)
*	�����û������˺ű����ظñ�Ĺ�ϣֵ 
*
*   function findAccount_house_online()public view returns(string memory account_house_online)
*	�������ߵķ��ݱ�����һ����ϣֵ   
*
*   function findAccount_house_downline()public view returns(string memory account_house_downline)
*	�������ߵķ��ݱ�����һ����ϣֵ   
*   
*   function postUser_account(string memory user_account)public returns (bool)
*	�޸��û�-�˺ű�����һ��true��ʾִ�гɹ�    
*
*   function postAccount_house_online(string memory account_house_online)public returns(bool)
*	�޸����߷��ݱ�����һ��true��ʾִ�гɹ�    
*
*   function postAccount_house_downline(string memory account_house_downline)public returns(bool)
*   �޸����߷��ݱ�����һ��true��ʾִ�гɹ�
*
*	function findOrdersNum(address _user)public returns(uint256)
*	�����û�����̫����ַ�����û��Ķ�������
*
*	function findOrder_1(address _user,uint256 location)public view returns(address submiter,
*        address responder,
*        uint256 submiterEthCoin,
*        uint256 aimerEthCoin,
*        uint256 subFirstSign,
*        uint256 resFirstSign,
*        uint256 subSecondSign,
*        uint256 resSecondSign) 
*	������̫����ַ��ȡ�û���locationλ�õĶ�����Ϣ������ֵ�ֱ�Ϊ���׷���ַ���ҷ���ַ���׷�Ѻ���ҷ�Ѻ�𣬼׷���һ��ǩ�����ҷ���һ��ǩ�����׷��ڶ���ǩ�����ҷ��ڶ���ǩ����
*
*	function findOrder_2(address _user,uint256 location)public view returns( uint256 sub_time,
*        uint256 effect_time,
*        uint256 finish_time,
*        uint256 role,
*        uint256 state,
*        uint256 money��
*		 string house_hash)
*	������̫����ַ��ȡ�û���locationλ�õĶ�����Ϣ������ֵ�ֱ�Ϊ���ύʱ�䣬��Чʱ�䣬��Լӵ���߰��ݵĽ�ɫ~���ͷ����ȣ���Լ��״̬����ԼѺ��״̬,���ӹ�ϣ��
*
*	
*	
*	//Ѱ����Ч�ĺ�Լ��_submiter�Ǽ׷��ĵ�ַ��_aimer���ҷ��ĵ�ַ��_from�Ǵ洢��Լ�ĵ�ַ
*	//�����ڷ���������effcting�ĺ�Լ���򷵻ظú�Լ�������е�λ��
*	//��û�У��򷵻�-1
*    function findEff_order(address _submiter,address _aimer,address _from)public view returns(int256)
*
*	//Ѱ���ύ�ĺ�Լ��_submiter�Ǽ׷��ĵ�ַ��_aimer���ҷ��ĵ�ַ��_from�Ǵ洢��Լ�ĵ�ַ
*	//�����ڷ���������submiting�ĺ�Լ���򷵻ظú�Լ�������е�λ��
*	//��û�У��򷵻�-1;
*   function findSub_order(address _submiter,address _aimer,address _from)public view returns(int256)
*
*	function findEnd_noReturned_oreder(address _submiter,address _aimer,address _from)public view returns(int256)
*	Ѱ����ɵ���û����Ǯ�Ķ�����_submiter�Ǽ׷��ĵ�ַ��_aimer���ҷ��ĵ�ַ��_from�Ǵ洢��Լ�ĵ�ַ
*	�����ڷ���������subed&&finished�ĺ�Լ���򷵻ظú�Լ�������е�λ��
*	��û�У��򷵻�-1
*
*	function findFailed_noReturned_orderer(address _submiter,address _aimer,address _from)public view returns(int256)
*	Ѱ��ʧ����û����Ǯ�Ķ�����_submiter�Ǽ׷��ĵ�ַ��_aimer���ҷ��ĵ�ַ��_from�Ǵ洢��Լ�ĵ�ַ
*	�����ڷ���������subed&&failed�ĺ�Լ���򷵻ظú�Լ�������е�λ��
*	��û�У��򷵻�-1
*
*
*
*	�������������ⲿ���ã�������
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
        cir.submiterEthCoin = coins;
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
        if(_appealer == msg.sender)
        {
             k = uint256(findEnd_noReturned_oreder(_submiter,_aimer,_submiter));
             a = uint256(findEnd_noReturned_oreder(_submiter,_aimer,_aimer));
             
        }
        else if(_appealer != msg.sender)
        {
             k = uint256(findFailed_noReturned_orderer(_submiter,_aimer,_submiter));
             a = uint256(findFailed_noReturned_orderer(_submiter,_aimer,_aimer));
        }
        km = contracts[_submiter][k].submiterEthCoin;
        am = contracts[_aimer][a].aimerEthCoin;
        uint256 om = km+am;
        if(_appealer == msg.sender)
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
         else if(_appealer != msg.sender)
        {
                if(_appealer == _submiter)
                {
                    contracts[_submiter][k].money = 2;
                    contracts[_aimer][a].money = 3;
                     if(!_submiter.send(om))
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
                     if(!_aimer.send(om))
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
