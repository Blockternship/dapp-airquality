const AirRewards = artifacts.require('AirRewards.sol')
const Vault = artifacts.require('Vault')
const IVaultConnector = artifacts.require('IVaultConnector')
const ETHConnector = artifacts.require('ETHConnector')
const ERC20Connector = artifacts.require('ERC20Connector')

contract('AirRewards', (accounts) => {
	let app, vault, token1, token2, executionTarget, etherToken = {}

    const n = '0x00'
    const withdrawAddr = '0x0000000000000000000000000000000000001234'

    const ETH = '0x0000000000000000000000000000000000000000'

    beforeEach(async () => {
        vault = await Vault.new()
        const ethConnector = await ETHConnector.new()
        const erc20Connector = await ERC20Connector.new()
        await vault.initialize(erc20Connector.address, ethConnector.address)


        await ETHConnector.at(vault.address).deposit(ETH, accounts[0], 400, [0], { value: 400 });

        app = await AirRewards.new()

        await app.initialize(vault.address)
    })

    it('records ETH deposits', async () => {
        const sentWei = 10
        const receipt = await app.send(sentWei, { gas: 3e5 })
        const transactionId = receipt.logs.filter(log => log.event == 'NewTransaction')[0].args.transactionId


        // vault has 400 wei initially
        assert.equal(await ETHConnector.at(vault.address).balance(ETH), 400 + 10, 'deposited ETH must be in vault')
    })
})
