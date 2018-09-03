const Vault = artifacts.require('./Vault.sol')

contract('Vault', (accounts) => {
  it('...should store the value 89.', async () => {
    const simpleStorageInstance = await Vault.deployed()

    // Set value of 89
    await simpleStorageInstance.set(89, {from: accounts[0]})

    // Get stored value
    const storedData = await simpleStorageInstance.get.call()

    assert.equal(storedData, 89, 'The value 89 was not stored.')
  })
})
