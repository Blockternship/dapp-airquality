import Layout from '../index.js'
import React from 'react'
import Dapp from './dapp'
import Web3Container from '../lib/Web3Container'
import fetch from 'isomorphic-unfetch'
import { Form,  Input, Button, Card, Col, Row } from 'antd'
const FormItem = Form.Item
export default () => (
<Layout>
  <div>
    <p>AIR Quality DAPP</p>
     <Web3Container
    renderLoading={() => <div>Loading Dapp Page...</div>}
    render={({ web3, accounts, contract }) => (
      <Dapp accounts={accounts} contract={contract} web3={web3} />
    )}
  />
  </div>
  </Layout>
)


