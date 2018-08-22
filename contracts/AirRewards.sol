pragma solidity ^0.4.4;

import "@aragon/os/contracts/apps/AragonApp.sol";
import "@aragon/apps-vault/contracts/IVaultConnector.sol";
import "@aragon/os/contracts/lib/misc/Migrations.sol";

import "@aragon/os/contracts/lib/zeppelin/math/SafeMath.sol";
import "@aragon/os/contracts/lib/zeppelin/math/SafeMath64.sol";



contract AirRewards is AragonApp {
    using SafeMath for uint256;
    using SafeMath64 for uint64;


    address constant public ETH = address(0);


    // State
    IVaultConnector public vault;
    mapping (address => uint256) rewards;

    
    // Roles
    bytes32 constant public REPORTER_ROLE = keccak256("REPORTER_ROLE");
    bytes32 constant public DONOR_ROLE = keccak256("DONOR_ROLE");
    
    /**
     * @notice Increment rewarder by 1
     */
    function reporterRewarded(address _rewarder) auth(REPORTER_ROLE) external {
	  rewards[_rewarder] ++;
    }



     /**
     * @dev Sends ETH to Vault. Sends all the available balance.
     * @notice Allows to send ETH from this contract to Vault, to avoid locking them in contract forever.
     */
    function () external payable isInitialized {
        vault.deposit.value(this.balance)(ETH, msg.sender, this.balance, new bytes(0));
    }

    /**
    * @notice Initialize Finance app for Vault at `_vault` 
    * @param _vault Address of the vault Finance will rely on (non changeable)
    */
    function initialize(IVaultConnector _vault) external onlyInit {
        initialized();
        vault = _vault;
    }


}

