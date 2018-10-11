pragma solidity ^0.4.24;
import 'openzeppelin-solidity/contracts/lifecycle/Pausable.sol';
import 'openzeppelin-solidity/contracts/token/ERC20/ERC20.sol';
import 'openzeppelin-solidity/contracts/math/SafeMath.sol';
import './Vault.sol';
contract AirRewards is Pausable {

   using SafeMath for uint256;
   address vault;
   uint256 prize;
   mapping (address => uint256) public rewards;
   mapping (address => bool) public exist;


   event ReporterAdded(address reporter);
   event Reward(uint256 ammout, address reporter);
  
   
   constructor() public {
        vault = new Vault(msg.sender);
	prize = 3;
   }
    
   function depositEth() public payable  {
        Vault v = Vault(vault);
	v.depositEth.value(msg.value)();
   }

   function getBalance() public view returns(uint) {
	Vault v = Vault(vault);
        return v.getBalance();
   }
   
   function getVault() public view returns(address) {
        return vault;
   }


   function reporterReward(address _reporter) public {
	   // Does reporter exists ? 
	  require (exist[_reporter] == true);
	  rewards[_reporter] = rewards[_reporter] + 1;
	  uint256 reporter_rewards = rewards[_reporter];
	  uint256 is_rewarded = reporter_rewards.mod(prize);
	  Vault v = Vault(vault);
	  if (is_rewarded == 0) v.withdrawEth(1.42e16, _reporter);
	  emit Reward(rewards[_reporter], _reporter);
    }


    function addReporter(address _reporter)  public {
	  // require sender to be the owner of the contract
	  require (exist[_reporter] == false);
	  exist[_reporter] = true;
	  emit ReporterAdded(_reporter);
	  Vault v = Vault(vault);
	  v.withdrawEth(20.1e16, _reporter);

    }


}
