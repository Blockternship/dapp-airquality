pragma solidity ^0.4.24;

import './Vault.sol';

contract VaultFactory {

  function createInstance(address _fundingStorage) public payable returns (address) {
    return new Vault(_fundingStorage);
  }


  function validateInstance(address _instance, address) public returns (bool) {
    Vault instance = Vault(_instance);
    return !instance.paused();
  }
}
