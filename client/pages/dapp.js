import Layout from '../index.js'
import React from 'react'
import Link from 'next/link'
import Web3Container from '../lib/Web3Container'
import fetch from 'isomorphic-unfetch'
import { Form,  Input, Button, Card, Col, Row } from 'antd'
const FormItem = Form.Item

class Dapp extends React.Component {
  state = {
    balance: undefined,
    ethBalance: undefined,
    eprice:0,
    inputC:undefined
  };
  inputChange = (e) => {
	  console.log(e)
  }

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

  getEthRates = async () => {
    const eth = await fetch(`https://api.coinmarketcap.com/v1/ticker/ethereum/`).then( r => r.json()) 
    this.setState({ eprice: parseInt(eth.price_usd) })
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
    const { balance = 'N/A', ethBalance = 'N/A',eprice = 0, inputC ='N/A' } = this.state
    return (
 <Layout>
    <div style={{ marginTop: 100 }}>
      <Form layout='horizontal'>
        <FormItem
          label=''
          labelCol={{ span: 8 }}
          wrapperCol={{ span: 8 }}
        >
		<Input onChange={this.inputChange} style={{ width: '50%' }} defaultValue="input content" />
        </FormItem>
	
	<FormItem
          label=''
          labelCol={{ span: 8 }}
          wrapperCol={{ span: 8 }}
        >
		<Button onClick={this.depositValue} type="primary" size='large'>Primary</Button>
		<Button onClick={this.getValue} type="primary" size='large'>Primary</Button>
		<Button onClick={this.addReporter} type="primary" size='large'>Dashed</Button>
		<Button onClick={this.getReportBalance} type="primary" size='large'>Normal</Button>
        </FormItem>

	 <FormItem
	  label=''
          labelCol={{ span: 8 }}
          wrapperCol={{ span: 8 }} 
	 >
		<Card title="Account" >Account Balance: {balance}</Card>
		<Card title="Vault" >Ether Balance: {ethBalance}</Card>
		<Card title="Eth usd" >Ether USD: {eprice}{inputC}</Card>

	</FormItem>
      </Form>
    </div>
  </Layout>
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
