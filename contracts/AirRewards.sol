pragma solidity 0.4.24;

import "@aragon/os/contracts/apps/AragonApp.sol";

import "@aragon/os/contracts/lib/zeppelin/token/ERC20.sol";

import "@aragon/os/contracts/lib/misc/Migrations.sol";

contract AirRewards is AragonApp {
    constructor () {

    }

    bytes32 constant public TRANSFER_ROLE = keccak256("TRANSFER_ROLE");

    event Transfer(address indexed token, address indexed to, uint256 amount);
    event Deposit(address indexed token, address indexed sender, uint256 amount);
    event ReporterAdded(address reporter);
    mapping (address => int) rewards;
    /**
    * @dev On a normal send() or transfer() this fallback is never executed as it will be
    * intercepted by the Proxy (see aragonOS#281)
    */
    function () payable external {
        require(msg.data.length == 0);
        deposit(ETH, msg.sender, msg.value);
    }

    function reporterReward(address _reporter, bytes32 msgHash, uint8 v, bytes32 r, bytes32 s)  {
	  if ( rewards[_reporter] !=-1 && isSigned(_reporter, msgHash, v,r,s))
	      rewards[_reporter] ++;
    }

    function addReporter(address _reporter)  public {
	  rewards[_reporter] = -1;
	  emit ReporterAdded(_reporter);
    }

    function getReportBalance(address _reporter) returns (int) {
	  return rewards[_reporter];
    }


    /**
    * @notice Deposit `value` `token` to the vault
    * @param token Address of the token being transferred
    * @param from Entity that currently owns the tokens
    * @param value Amount of tokens being transferred
    */
    function deposit(address token, address from, uint256 value) payable public {
        require(value > 0);
        require(msg.sender == from);

        if (token == ETH) {
            // Deposit is implicit in this case
            require(msg.value == value);
        } else {
            require(ERC20(token).transferFrom(from, this, value));
        }

        emit Deposit(token, from, value);
    }

    /*
    TODO: Function could be brought back when https://github.com/ethereum/solidity/issues/526 is fixed
    * @notice Deposit `value` `token` to the vault
    * @param token Address of the token being transferred
    * @param from Entity that currently owns the tokens
    * @param value Amount of tokens being transferred
    * @param data Extra data associated with the deposit (currently unused)
    function deposit(address token, address from, uint256 value, bytes data) payable external {
        deposit(token, from, value);
    }
    */

    /**
    * @notice Transfer `value` `token` from the Vault to `to`
    * @param token Address of the token being transferred
    * @param to Address of the recipient of tokens
    * @param value Amount of tokens being transferred
    */
    function transfer(address token, address to, uint256 value)
        authP(TRANSFER_ROLE, arr(address(token), to, value))
        external
    {
        transfer(token, to, value, new bytes(0));
    }

    /**
    * @notice Transfer `value` `token` from the Vault to `to`
    * @param token Address of the token being transferred
    * @param to Address of the recipient of tokens
    * @param value Amount of tokens being transferred
    * @param data Extra data associated with the transfer (only used for ETH)
    */
    function transfer(address token, address to, uint256 value, bytes data)
        authP(TRANSFER_ROLE, arr(address(token), to, value))
        public
    {
        require(value > 0);

        if (token == ETH) {
            require(to.call.value(value)(data));
        } else {
            require(ERC20(token).transfer(to, value));
        }

        emit Transfer(token, to, value);
    }

    function balance(address token) public view returns (uint256) {
        if (token == ETH) {
            return address(this).balance;
        } else {
            return ERC20(token).balanceOf(this);
        }
    }

    /**
    * @dev Disable recovery escape hatch, as it could be used
    *      maliciously to transfer funds away from the vault
    */
    function allowRecoverability(address token) public view returns (bool) {
        return false;
    }


    function recoverAddr(bytes32 msgHash, uint8 v, bytes32 r, bytes32 s) returns (address) {
        return ecrecover(msgHash, v, r, s);
    }

    function isSigned(address _addr, bytes32 msgHash, uint8 v, bytes32 r, bytes32 s) returns (bool) {
        return ecrecover(msgHash, v, r, s) == _addr;
    }

}
