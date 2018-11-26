
# dapp-airquality (work in progress)

Our initial **goal** was:

Create a public health air-quality Dapp where users can report air pollution with sensors and receive compensations from ETH. This system works through donations that are addressed to a reward system, which later are evenly distributed as ETH donations to reporters.

This is part of our contributions to CanAirIO which is a citizen science initiative for air quality tracking, visualization and dissemination


To Achieve our we stated building 3 core components, and this is the description of our progress:  


## Android 
![](https://github.com/Blockternship/dapp-airquality/raw/dapp/canario_android.jpg)
* Create eth wallet. 
* “Frictionless” Subscription github api integration. 
* Air quality reports
* Current production version integration branch:rewards.crypto missing code reviews. 
* https://github.com/kike-canaries/android-hpma115s0/tree/rewards.crypto
* Integrate parity signer to create wallets (not used)
 



## Dapp 
* Donors Vault. used next(react based) frontend framework. 
![](https://github.com/Blockternship/dapp-airquality/raw/dapp/vault%20doners.png)
* Reward system initial logic (Solidity smart contract)
* “Frictionless” Subscription with webhoook and lambda:  trigger a lambda function for initial funds transfer, after reporters validation from a github issue been closed. 
![](https://github.com/Blockternship/dapp-airquality/raw/dapp/issues.png)
* demo website: https://out-eywlrffuet.now.sh/


## Esp32
we tested and integrated part of the contracts call on ESP32, thich is the microncontroller we are using. 



this is our website (at this moment is still in spanish)
http://canair.io/

our  repository: 
https://github.com/kike-canaries

and some of our tutorials tutorials:
https://www.hackster.io/114723/canairio-red-ciudadana-para-monitoreo-de-calidad-del-aire-96f79a






