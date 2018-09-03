import React from 'react'
import Link from 'next/link'
import Web3Container from '../lib/Web3Container'

class Dapp extends React.Component {
  state = {
    balance: undefined,
    ethBalance: undefined
  };

  depositValue = async () => {
    const { accounts, contract } = this.props
    await contract.methods.depositEth().send( {value:"200000", from: accounts[0] })
    alert('Stored 5 into account')
  }; 
  
  reporterReward = async () => {
    const { accounts, contract } = this.props
    await contract.methods.reporterReward("0x185234ba42d395e1d7fa04e273005d54c8a690c0").send({ from: accounts[0] })
    alert('Stored 5 into account')
  };
 
  addReporter = async () => {
    const { accounts, contract } = this.props
    await contract.methods.addReporter("0x185234ba42d395e1d7fa04e273005d54c8a690c0").send({ from: accounts[0] })
    alert('Stored 5 into account')
  };
  
  getReportBalance = async () => {
    const { accounts, contract } = this.props
    const response =  await contract.methods.getReportBalance("0x185234ba42d395e1d7fa04e273005d54c8a690c0").call({ from: accounts[0] })
    this.setState({ balance: response })
  };



  getValue = async () => {
    const { accounts, contract } = this.props
    const response = await contract.methods.getBalance().call({ from: accounts[0] })
    this.setState({ balance: response })
  };

  getEthBalance = async () => {
    const { web3, accounts } = this.props
    const balanceInWei = await web3.eth.getBalance(accounts[0])
    this.setState({ ethBalance: balanceInWei / 1e18 })
  };

  render () {
    const { balance = 'N/A', ethBalance = 'N/A' } = this.state
    return (
      <div>
        <h1>My Dapp</h1>

        <button onClick={this.depositValue}>Store 1 into account balance</button>
        <button onClick={this.getValue}>Get account balance</button>
        <button onClick={this.addReporter}> Add reporter</button>
        <button onClick={this.reporterReward}> reqard reporter</button>
        <button onClick={this.getReportBalance}> get balance</button>
        <div>Account Balance: {balance}</div>
        <div>Ether Balance: {ethBalance}</div>
        <div>
          <Link href='/accounts'>
            <a>My Accounts</a>
          </Link>
        </div>
        <div>
          <Link href='/'>
            <a>Home</a>
          </Link>
        </div>
      </div>
    )
  }
}

export default () => (
  <Web3Container
    renderLoading={() => <div>Loading Dapp Page...</div>}
    render={({ web3, accounts, contract }) => (
      <Dapp accounts={accounts} contract={contract} web3={web3} />
    )}
  />
)
