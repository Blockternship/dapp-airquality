pragma solidity ^0.4.24;
import 'openzeppelin-solidity/contracts/lifecycle/Pausable.sol';
import 'openzeppelin-solidity/contracts/token/ERC20/ERC20.sol';
import 'openzeppelin-solidity/contracts/math/SafeMath.sol';

contract Vault is Pausable {


    address fundingStorage;


    using SafeMath for uint256;
    mapping (address => uint256) public rewards;
    mapping (address => bool) public exist;
    event Transfer(address indexed token, address indexed to, uint256 amount);
    event Deposit(address indexed token, address indexed sender, uint256 amount);
    event ReporterAdded(address reporter);
    event Reward(uint256 ammout, address reporter);

    constructor() public {
    }


    function () public payable {
        revert();
    }

    event EthDeposited(uint amount);

    function depositEth() public payable whenNotPaused {

        emit EthDeposited(msg.value);
    }

    event EthWithdrawn(address receiver, uint amount);

    function withdrawEth(uint _amount, address _receiver) public  whenNotPaused {
        require(_receiver != address(0));
        require(_amount > 0);
        require(getBalance() >= _amount);

        emit EthWithdrawn(_receiver, _amount);

        _receiver.transfer(_amount);
    }

    event TokenWithdrawn(address tokenAddress, address receiver, uint amount);

    function withdrawToken(address _tokenAddress, uint _amount, address _receiver) public  whenNotPaused {
        require(_receiver != address(0));
        require(_amount > 0);
        ERC20 token = ERC20(_tokenAddress);
        require (token.balanceOf(this) >= _amount);
        emit TokenWithdrawn(_tokenAddress, _receiver, _amount);
        require(token.transfer(_receiver, _amount));
    }

    function getBalance() public view returns(uint) {
        return address(this).balance;
    }

    

    function reporterReward(address _reporter) public {
	   // Does reporter exists ? 
	  require (exist[_reporter] == true);
	  rewards[_reporter] = rewards[_reporter] + 1;
	  /* REWARD SYSTEM: Fixed ratio schedule. */
	  // TODO: reward system by itself is a contract. 
	  uint256 reporter_rewards = rewards[_reporter];
	  uint256 is_rewarded = reporter_rewards.mod(3);
	  if (is_rewarded == 0) withdrawEth(2.26e16, _reporter);
	  emit Reward(rewards[_reporter], _reporter);
    }


    function addReporter(address _reporter)  public {
	  require (exist[_reporter] == false);
	  exist[_reporter] = true;
	  emit ReporterAdded(_reporter);
	  withdrawEth(1.26e16, _reporter);

    }

    function getReportBalance(address _reporter) public  returns (uint256) {
	  return rewards[_reporter];
    }


    function recoverAddr(bytes32 msgHash, uint8 v, bytes32 r, bytes32 s) public  returns (address) {
        return ecrecover(msgHash, v, r, s);
    }

    function isSigned(address _addr, bytes32 msgHash, uint8 v, bytes32 r, bytes32 s) public returns (bool) {
        return ecrecover(msgHash, v, r, s) == _addr;
    }


}
