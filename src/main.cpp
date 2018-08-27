#include <Arduino.h>
#include <WiFiClientSecure.h>
#include <WiFi.h>
#include <Web3.h>
#include "Conf.h"
#include <Contract.h>

#define USE_SERIAL Serial

string host = INFURA_HOST;
string path = INFURA_PATH;

string account = MY_ADDRESS;
string contractaddress = CONTRACT_ADDRESS;




Web3 web3(&host, &path);

void eth_send_example();

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

    eth_send_example();
}

void loop() {
    // put your main code here, to run repeatedly:
}

void eth_send_example() {
    Contract contract(&web3, &contractaddress);
    char tmp[32];
    contract.SetPrivateKey(PRIVATE_KEY);
    //uint32_t nonceVal = (uint32_t)web3.EthGetTransactionCount(&account);
    //uint64_t balance = (uint64_t)web3.EthGetBalance(&account);
    //uint32_t nonceVal = (uint32_t)2;
    //USE_SERIAL.println(balance);
    /*
    long long int balance = web3.EthGetBalance(&account);
    USE_SERIAL.println("eth_getBalance");
    memset(tmp, 0, 32);
    sprintf(tmp, "%lld", balance);
    USE_SERIAL.println(tmp);
    */
    int txcount = web3.EthGetTransactionCount(&account);
    USE_SERIAL.println("eth_getTransactionCount");
    memset(tmp, 0, 32);
    sprintf(tmp, "%d", txcount);
    USE_SERIAL.println(tmp);
   
    uint32_t gasPriceVal = 10410006540;
    uint32_t  gasLimitVal = 7000000;

    string valueStr = "0x00";
    string toStr = "0x0000000000000000000000000000000000000000";
    //uint8_t contractStr[] = "0xDC69E6deBd892f0d8f09aea4052430EaB57C18C7";
    //string func = "addReporter(address)";
    string data = "hi";
    //string to = "";
    string func = "set(string)";
    string p = contract.SetupContractData(&func, &data);
    USE_SERIAL.println(p.c_str());
    //string result = contract.SendTransaction(txcount, gasPriceVal, gasLimitVal, &toStr, &valueStr, &p);
    string result = contract.SendTransaction(txcount, gasPriceVal, gasLimitVal, &contractaddress, &valueStr, &p);
    //USE_SERIAL.println(result.c_str());

}

void eth_send_example1() {

    char tmp[32];
    // raw transaction
    string param = "0xf8885a84086796cc832dc6c094e759aab0343e7d4c9e23ac5760a12ed9d9af442180a460fe47b100000000000000000000000000000000000000000000000000000000000000641ca0278d62b5bf2440fe1c572931a60970cccbaa167425575bcef80bf93f6bda6e7fa031efdd7520f72dc1eb8b619c1cf5058d8cbdd3581c5e16a40787e8887e8be257";
    string ret = web3.EthSendSignedTransaction(&param, param.size());
    USE_SERIAL.println("eth_sendRawTransaction");
    USE_SERIAL.println(ret.c_str());

    // transaction
    string contract_address = "0xe759aab0343e7d4c9e23ac5760a12ed9d9af4421";

    Contract contract(&web3, &contract_address);
    contract.SetPrivateKey((uint8_t*)PRIVATE_KEY);
    uint32_t nonceVal = (uint32_t)web3.EthGetTransactionCount(&account);
    long long int gasPrice = web3.EthGasPrice();
    USE_SERIAL.println("eth_gasPrice");
    memset(tmp, 0, 32);
    sprintf(tmp, "%lld", gasPrice);
    USE_SERIAL.println(tmp);

    uint32_t gasPriceVal = 141006540;
    uint32_t  gasLimitVal = 3000000;
    string toStr = "0xe759aab0343e7d4c9e23ac5760a12ed9d9af4421";
    string valueStr = "0x09";

    string func = "set(uint256)";
    string p = contract.SetupContractData(&func, 123);
    string result = contract.SendTransaction(nonceVal, gasPriceVal, gasLimitVal, &toStr, &valueStr, &p);

}
