#include <Arduino.h>
#include <WiFiClientSecure.h>
#include <WiFi.h>
#include <Web3.h>
#include "Conf.h"
#include <Contract.h>

#define USE_SERIAL Serial

string host = INFURA_HOST;
string path = INFURA_PATH;

string account = AIRQUALITY_ADDRESS;
string contractaddress = CONTRACT_ADDRESS;

Web3 web3(&host, &path);

void air_quality_report();

void setup() {
    USE_SERIAL.begin(115200);
    WiFi.begin(ENV_SSID, ENV_WIFI_KEY);
    // attempt to connect to Wifi network:
    while (WiFi.status() != WL_CONNECTED) {
        Serial.print(".");
        // wait 1 second for re-trying
        delay(1000);
    }
    USE_SERIAL.println("Connected");
    air_quality_report();
}

void loop() {
	//TODO: 
}

void air_quality_report() {
    Contract contract(&web3, &contractaddress);
    char tmp[32];
    contract.SetPrivateKey(PRIVATE_KEY);
    
    int txcount = web3.EthGetTransactionCount(&account) ;
    USE_SERIAL.println("eth_getTransactionCount");
    memset(tmp, 0, 32);
    sprintf(tmp, "%d", txcount);
    USE_SERIAL.println(tmp);
   
    uint32_t gasPriceVal = 201006540;
    uint32_t  gasLimitVal = 5000000;

    string valueStr = "0x00";
    string toStr = "0x0000000000000000000000000000000000000000";
    string func = "reporterReward(address)";
    string data = "0x185234bA42d395e1D7Fa04E273005d54c8a690C0";
    string p = contract.SetupContractData(&func, &data);
    USE_SERIAL.println(p.c_str());
    string result = contract.SendTransaction(txcount, gasPriceVal, gasLimitVal, &contractaddress, &valueStr, &p);

}

