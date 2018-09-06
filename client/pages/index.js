import Layout from '../index.js'
import React from 'react'
import Link from 'next/link'


export default () =>
<Layout>
 <div>
    <h1>Home</h1>
    <p>Note that Web3 is not loaded for this page.</p>
    <div><Link href='/dapp'><a>My Dapp</a></Link></div>
    <div><Link href='/accounts'><a>My Accounts</a></Link></div>
  </div>
</Layout>

 


