var PrivateKeyProvider = require("truffle-privatekey-provider");
var config = require("./config").sec
const infura = "" + config.infura;
const privateKey = "" + config.privateKey;
module.exports = {
    networks: {
        rinkeby: {
            provider: new PrivateKeyProvider(privateKey, infura),
            network_id: 4
        },
        development: {
            host: 'localhost',
            port: 8545,
            network_id: '*' // Match any network id
        },

    }
};
