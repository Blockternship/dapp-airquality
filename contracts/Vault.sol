pragma solidity ^0.4.24;
import 'openzeppelin-solidity/contracts/lifecycle/Pausable.sol';
import 'openzeppelin-solidity/contracts/token/ERC20/ERC20.sol';

contract Vault is Pausable {


    address fundingStorage;


    event Transfer(address indexed token, address indexed to, uint256 amount);
    event Deposit(address indexed token, address indexed sender, uint256 amount);
    
    constructor(address _fundingStorage) public {
	fundingStorage = _fundingStorage;
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

    


}
