//const HDWalletProvider = require("truffle-hdwallet-provider");
const HDWalletProvider = require("truffle-hdwallet-provider-privkey");
const mnemonic = ["privatekey"];
module.exports = {
    networks: {
        development: {
            host: "localhost",
            port: 8545,
            network_id: "*" // Match any network id
        },
        rinkeby: {
            provider: () => {
                return new HDWalletProvider(mnemonic, "https://rinkeby.infura.io/v3/token")
            },
            network_id: 4
        }
    }
};
