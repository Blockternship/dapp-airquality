var config = require("./config").sec
const Web3 = require('web3');
const fs = require('fs');
const path = require('path');
const EthereumTx = require('ethereumjs-tx')
const privateKey = Buffer.from(config.privateKey, 'hex')
const address = '0x185234bA42d395e1D7Fa04E273005d54c8a690C0';
const infura = config.infura;
const web3 = new Web3(new Web3.providers.HttpProvider(infura));
const ABI = JSON.parse(fs.readFileSync('./abi.json', 'utf8'));
const rewards_contract = '0xbbdd97ba2c28807f0f6954897ce6971ce1ba0e2c';
const contract = new web3.eth.Contract(ABI, rewards_contract);


exports.handler = function(event, context, callback) {

    web3.eth.getTransactionCount(address).then((count) => {

        let data = contract.methods.addReporter(address).encodeABI();
        const txParams = {
            nonce: count,
            gasPrice: 22000000000,
            gasLimit: 4300000,
            to: rewards_contract,
            value: '0x00',
            data: data,
            chainId: 4
        }
        const tx = new EthereumTx(txParams)
        tx.sign(privateKey)
        const serializedTx = tx.serialize()
        web3.eth.sendSignedTransaction('0x' + serializedTx.toString('hex'))
            .on('receipt', (receipt) => {
                console.log(event.issue.state, event.issue.body);
                callback(receipt, {
                    message: 'tx ok'
                });

            });



    });


};
