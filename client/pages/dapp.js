import React from 'react'
import fetch from 'isomorphic-unfetch'
import { Form,  Input, Button, Card, Col, Row } from 'antd'
const FormItem = Form.Item

export default class Dapp extends React.Component {
  state = {
    balance: undefined,
    ethBalance: undefined,
    eprice:undefined,
    inputC:undefined,
    inputF:undefined,
    inputfield:undefined,
    contractAddress:undefined
  };
  
  async componentDidMount () {
      this.getEthRates()
      this.getValue()
      this.getEthBalance()
      this.getVaultAddress()
  }


  depositValue = async ( usd ) => {
    const { accounts, contract } = this.props
    const weiDeposit = (usd / this.state.eprice ) * 1e18
    console.log(weiDeposit) 
    await contract.methods.depositEth().send( {value:  weiDeposit , from: accounts[0] })
  }; 
  
  reporterReward = async () => {
    const { accounts, contract } = this.props
    await contract.methods.reporterReward("0x185234ba42d395e1d7fa04e273005d54c8a690c0").send({ from: accounts[0] })
  };
 
  addReporter = async () => {
    const { accounts, contract } = this.props
    await contract.methods.addReporter(this.state.inputfield).send({ from: accounts[0] })
    alert('Stored 5 into account')
  };
  
  getVaultAddress = async () => {
    const { accounts, contract } = this.props
    const response =  await contract.methods.getVault().call({ from: accounts[0] })
    this.setState({ contractAddress: response })
  };

  getEthRates = async () => {
    const eth = await fetch(`https://api.coinmarketcap.com/v1/ticker/ethereum/`).then( r => r.json()) 
    console.log(eth[0].price_usd)
    this.setState({ eprice: parseFloat(eth[0].price_usd) })
  };

  getValue = async () => {
    const { accounts, contract } = this.props
    const response = await contract.methods.getBalance().call({ from: accounts[0] })
    this.setState({ balance: response / 1e18 })
  };

  getEthBalance = async () => {
    const { web3, accounts } = this.props
    const balanceInWei = await web3.eth.getBalance(accounts[0])
    this.setState({ ethBalance: balanceInWei / 1e18 * 1.0 })
  };

  updateInputValue = async (evt) => {
    //console.log("input field updated with "+evt.target.value);
    this.setState({inputfield: evt.target.value})   
  };

  render () {
    const { balance = 'N/A', ethBalance = 'N/A',eprice = 0, inputC ='N/A', contractAddress = '0x' } = this.state
    return (
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
          label=' Perks '
          labelCol={{ span: 8 }}
          wrapperCol={{ span: 8 }}
        >
		<Button onClick={ ()=> this.depositValue(10)} type="primary" size='large'>Deposit 10</Button>
		<Button onClick={()=> this.depositValue(40)} type="primary" size='large'>Deposit 40 </Button>
		<Button onClick={()=> this.depositValue(100)} type="primary" size='large'>Deposit 100</Button>
        </FormItem>

	 <FormItem
	  label='Air Quality Vault Balance'
          labelCol={{ span: 8 }}
          wrapperCol={{ span: 8 }} 
	 >
		<Card title="Air Quality Vault Address" >{contractAddress} </Card>
		<Card title="Air Quality Vault Balance" >{balance} ETH </Card>
		<Card title="USD ETH Price " >Ether USD: {eprice}</Card>

	</FormItem>
      </Form>
    </div>
    )
  }
}


