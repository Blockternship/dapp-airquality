const AirRewards = artifacts.require('./AirRewards.sol')

module.exports = function (deployer) {
  deployer.deploy(AirRewards)
}

