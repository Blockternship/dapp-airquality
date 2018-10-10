var config = require("./config").sec
const Web3 = require('web3');
const fs = require('fs');
const path = require('path');
const EthereumTx = require('ethereumjs-tx')
const privateKey = Buffer.from(config.privateKey, 'hex')
const address_origin = '0x185234bA42d395e1D7Fa04E273005d54c8a690C0';
const infura = config.infura;
const web3 = new Web3(new Web3.providers.HttpProvider(infura));
const ABI = JSON.parse(fs.readFileSync('./abi.json', 'utf8'));
const rewards_contract = '0xB35F89019842050798847D08beE84AE9E8bc1036';
const contract = new web3.eth.Contract(ABI, rewards_contract);
const yaml = require('js-yaml');

exports.handler = function(event, context, callback) {


    console.log(event);
    let gh = event['body-json'];
    let body = yaml.load(gh.issue.body);
    let state = gh.issue.state;
    let action = gh.action;
    var address = body['address'];
    console.log(address, state, action);

    if (!state.includes('closed') || !action.includes('closed')) {
        callback({}, {
            message: 'invalid issue state'
        });
    } else {

        web3.eth.getTransactionCount(address_origin).then((count) =>{
            console.log(count);
            let data = contract.methods.addReporter(address).encodeABI();
            const txParams = {
                nonce: count,
                gasPrice: 22000000000,
                gasLimit: 4300000,
                to: rewards_contract,
                value: '0x00',
                data: data,
                chainId: 4
            };
            const tx = new EthereumTx(txParams)
            tx.sign(privateKey)
            const serializedTx = tx.serialize()
            web3.eth.sendSignedTransaction('0x' + serializedTx.toString('hex'))
                .on('receipt', (receipt) => {
                    callback(receipt, {
                        message: 'tx ok'
                    });

                });



        });
    }


};
