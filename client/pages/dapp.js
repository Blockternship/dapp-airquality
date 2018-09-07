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
    eprice:undefined,
    inputC:undefined,
    inputF:undefined,
    inputfield:undefined
  };
  
  async componentDidMount () {
      this.getEthRates()
      this.getValue()
      this.getEthBalance()
  }


  depositValue = async ( usd ) => {
    const { accounts, contract } = this.props
    const weiDeposit = (usd / this.state.eprice ) * 1e18
    console.log(weiDeposit) 
    await contract.methods.depositEth().send( {value:  weiDeposit , from: accounts[0] })
    alert('Stored 5 into account')
  }; 
  
  reporterReward = async () => {
    const { accounts, contract } = this.props
    await contract.methods.reporterReward("0x185234ba42d395e1d7fa04e273005d54c8a690c0").send({ from: accounts[0] })
    alert('Stored 5 into account')
  };
 
  addReporter = async () => {
    const { accounts, contract } = this.props
    await contract.methods.addReporter(this.state.inputfield).send({ from: accounts[0] })
    alert('Stored 5 into account')
  };
  
  getReportBalance = async () => {
    const { accounts, contract } = this.props
    const response =  await contract.methods.getReportBalance("0x185234ba42d395e1d7fa04e273005d54c8a690c0").call({ from: accounts[0] })
    this.setState({ balance: response })
  };

  getEthRates = async () => {
    const eth = await fetch(`https://api.coinmarketcap.com/v1/ticker/ethereum/`).then( r => r.json()) 
    console.log(eth[0].price_usd)
    this.setState({ eprice: parseFloat(eth[0].price_usd) })
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

  updateInputValue = async (evt) => {
    //console.log("input field updated with "+evt.target.value);
    this.setState({inputfield: evt.target.value})   
  };

  render () {
    const { balance = 'N/A', ethBalance = 'N/A',eprice = 0, inputC ='N/A' } = this.state
    return (
 <Layout>
    <div style={{ marginTop: 100 }}>
      <Form layout='horizontal'>
	 <FormItem
	  label=' Your Doner Account'
          labelCol={{ span: 8 }}
          wrapperCol={{ span: 8 }} 
	 >
		<Card title= {this.props.accounts} >Account Balance: { ethBalance} </Card>

	</FormItem>

        <FormItem
          label='Input Address of reporter '
          labelCol={{ span: 8 }}
          wrapperCol={{ span: 8 }}
        >
		<Input onChange={this.updateInputValue} style={{ width: '50%' }} defaultValue="input content" />

		<Button onClick={this.addReporter} type="primary" size='large'>Add reporter</Button>
        </FormItem>
	
	<FormItem
          label=' Perks '
          labelCol={{ span: 8 }}
          wrapperCol={{ span: 8 }}
        >
		<Button onClick={ ()=> this.depositValue(10)} type="primary" size='large'>Deposit 10</Button>
		<Button onClick={()=> this.depositValue(20)} type="primary" size='large'>Deposit 20 </Button>
		<Button onClick={()=> this.depositValue(50)} type="primary" size='large'>Deposit 40</Button>
        </FormItem>

	 <FormItem
	  label='Air Quality Vault Balance'
          labelCol={{ span: 8 }}
          wrapperCol={{ span: 8 }} 
	 >
		<Card title="Air Quality Vaul" >{balance}</Card>
		<Card title="Eth usd" >Ether USD: {eprice}</Card>

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
