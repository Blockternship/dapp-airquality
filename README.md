
# Blockternship submission air quality dApp CanAirIO 

Our initial **goal** was:

Create a public health air-quality Dapp where users can report air pollution with sensors and receive compensations from ETH. This system works through donations that are addressed to a reward system, which later are evenly distributed as ETH donations to reporters.

This is part of our contributions to CanAirIO which is a citizen science initiative for air quality tracking, visualization and dissemination


To achieve our goal we started building 3 core components(Android, Frontend and ESP32), and this is the current state of our progress on them:  


## Android 
![](https://github.com/Blockternship/dapp-airquality/raw/dapp/canario_android.jpg)
* Create Eth wallet in android
* “Frictionless” Subscription github api integration. 
* Air quality reports
* Current production version integration [branch: rewards.crypto](https://github.com/kike-canaries/android-hpma115s0/tree/rewards.crypto) (missing code reviews)
* Integrate parity signer to create wallets (not used for simplicity, instead java web3 library)
 



## Dapp 
* Donors Vault. used next(react based) frontend framework. 
![](https://github.com/Blockternship/dapp-airquality/raw/dapp/vault%20doners.png)
* Reward system initial logic (Solidity smart contract)
* “Frictionless” Subscription with webhook and lambda:  trigger a lambda function for initial funds transfer, after reporters validation from a github issue being closed. 
![](https://github.com/Blockternship/dapp-airquality/raw/dapp/issues.png)
* demo website: https://out-eywlrffuet.now.sh/


## Esp32
we tested and integrated part of the contracts call on ESP32, thich is the microcontroller we are using. 



# CanAirIO
Some important info about our project

* This is our website (at this moment is still in spanish)
http://canair.io/

* Our  repository: 
https://github.com/kike-canaries

* And some of our tutorials 
https://www.hackster.io/114723/canairio-red-ciudadana-para-monitoreo-de-calidad-del-aire-96f79a
