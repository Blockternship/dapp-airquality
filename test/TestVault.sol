pragma solidity ^0.4.18;

import "truffle/Assert.sol";
import "truffle/DeployedAddresses.sol";
import "../contracts/Vault.sol";

contract TestVault {

  function testItStoresAValue() public {
    Vault simpleStorage = Vault(DeployedAddresses.Vault());

    simpleStorage.set(89);

    uint expected = 89;

    Assert.equal(simpleStorage.get(), expected, "It should store the value 89.");
  }

}
