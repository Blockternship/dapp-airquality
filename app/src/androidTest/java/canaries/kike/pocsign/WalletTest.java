package canaries.kike.pocsign;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.crypto.CipherException;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.ECKeyPair;
import org.web3j.crypto.RawTransaction;
import org.web3j.crypto.TransactionEncoder;
import org.web3j.crypto.Wallet;
import org.web3j.crypto.WalletFile;
import org.web3j.crypto.WalletUtils;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.Web3jFactory;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.request.Transaction;
import org.web3j.protocol.core.methods.response.EthGetTransactionCount;
import org.web3j.protocol.core.methods.response.EthSendTransaction;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.core.methods.response.Web3ClientVersion;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.Contract;
import org.web3j.tx.ManagedTransaction;
import org.web3j.tx.Transfer;
import org.web3j.utils.Convert;
import org.web3j.utils.Numeric;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class WalletTest {

    private static final String TAG = WalletTest.class.getSimpleName() + " ETH ";
    private Credentials credentials;
    private String account;
    private String privateSeed;
    private String seed;
    private Web3j web3;
    private String signed;
    private WalletFile walletFile;
    private File walletPathFile;
    private String tx;

    private static native String ethkeyBrainwalletAddress(String seed);
    private static native String ethkeyRandomSecret();
    private static native String ethkeyBrainwalletSecret(String seed);
    private static native String ethkeyBrainwalletSign(String seed, String message);
    private static native String ethkeyBrainwalletSignSecret(String secret, String message);
    private static native String ethkeyRlpItem(String data, int position);
    private static native String ethkeyKeccak(String data);
    private static native String ethkeyEthSign(String data);
    private static native String ethkeyBlockiesIcon(String seed);
    private static native String ethkeyRandomPhrase(int words);
    private static native String ethkeyEncryptData(String data, String password);
    private static native String ethkeyDecryptData(String data, String password);


    static {
        System.loadLibrary("signer");
    }

    private void genWallet(){
        try {
            walletFile = Wallet.createLight("12345678", ECKeyPair.create(Numeric.toBigInt(privateSeed)));
            ObjectMapper objectMapper = new ObjectMapper();
            Context appContext = InstrumentationRegistry.getTargetContext();
            walletPathFile = new File(appContext.getApplicationInfo().dataDir);
            String fileName = getWalletFileName(walletFile);
            Log.d("ETH",fileName);
            File destination = new File(walletPathFile, fileName);
            try {
                objectMapper.writeValue(destination, walletFile);
            } catch (IOException e) {
                Log.e("IO EXCEPTION ETH",e.getMessage());
            }
        } catch (CipherException e) {
            Log.e("ETH",e.getMessage());
        }
    }



    private String getWalletFileName(WalletFile walletFile) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("'UTC--'yyyy-MM-dd'T'HH-mm-ss.SSS'--'");
        return dateFormat.format(new Date()) + walletFile.getAddress() + ".json";
    }

    public String signTx(RawTransaction rawTransaction){
        byte[] signedMessage = TransactionEncoder.signMessage(rawTransaction, credentials);
        String hexValue = Numeric.toHexString(signedMessage);
        return hexValue;
    }

    public RawTransaction createRawTX( Web3j web3, String address){
        String set_contract_Address = BuildConfig.contract;
        EthSendTransaction transactionResponse = null;
        try {
            Function function = new Function(
                    "reporterReward",
                    Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(BuildConfig.reporter)),
                    Collections.<TypeReference<?>>emptyList());
            Future<EthGetTransactionCount> ethGetTransactionCount = web3.ethGetTransactionCount(
                    address, DefaultBlockParameterName.LATEST)
                    .sendAsync();
            BigInteger count =ethGetTransactionCount.get().getTransactionCount();
            String encodedFunction = FunctionEncoder.encode(function);
            RawTransaction rawTransaction = RawTransaction.createTransaction(
                    count, ManagedTransaction.GAS_PRICE, Contract.GAS_LIMIT, set_contract_Address, BigInteger.ZERO,  encodedFunction);
            return rawTransaction;
        } catch (InterruptedException e) {
            Log.e("ERROR ETH INT",e.getMessage());
        } catch (ExecutionException e) {
            Log.e("ERROR ETH EXEC",e.getMessage() + Arrays.deepToString(e.getStackTrace()));
        }
        return null;
    }

    public Web3j connectEth() {
        Web3j web3 = Web3jFactory.build(new HttpService(BuildConfig.infura));  // defaults to http://localhost:8545/
        return web3;
    }

    public String account(Credentials c) {
        return c.getAddress();
    }

    private String createOfflineTx() {
        RawTransaction rawTX = createRawTX(web3, credentials.getAddress());
        String signedTx= signTx(rawTX);
        return signedTx;
    }


    public Credentials loadCredentials(String fileName) {
        Credentials credentials = null;
        try {
            credentials = WalletUtils.loadCredentials("12345678",new File(walletPathFile, fileName));
            return credentials;
        } catch (IOException e) {
            Log.e("ETH",e.getMessage());
        } catch (CipherException e) {
            Log.e("ETH",e.getMessage());
        }
        return null;
    }
    @Before public void setup(){
        seed = randomPhrase(12);
        web3 = connectEth();
        privateSeed = ethkeyBrainwalletSecret(seed);
        //genWallet();
        Log.d(TAG,  seed+ " " + privateSeed );
    }

    @Test
    public void testSign() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();
        walletPathFile = new File(appContext.getApplicationInfo().dataDir);
        String fileName = "UTC--2018-09-18T10-43-39.158--008ad1fa31cc3697b7036bbda630f75809124d2c.json";
        credentials = loadCredentials(fileName);
        tx = createOfflineTx();
        Log.d(TAG, credentials.getAddress());
        assertTrue(tx.length() > 0);

    }
    @Test
    public void testSendRawSignedTx() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();
        walletPathFile = new File(appContext.getApplicationInfo().dataDir);
        String fileName = "UTC--2018-09-18T10-43-39.158--008ad1fa31cc3697b7036bbda630f75809124d2c.json";
        credentials = loadCredentials(fileName);
        tx = createOfflineTx();
        Log.d(TAG, credentials.getAddress());
        EthSendTransaction transactionResponse = null;
        try {
            transactionResponse = web3.ethSendRawTransaction(tx).sendAsync().get();
            Log.i("ETH", ("Transaction complete, view it at https://rinkeby.etherscan.io/tx/"
                    + transactionResponse.getTransactionHash()));
            assertTrue(transactionResponse.getTransactionHash().length() > 0);
        } catch (InterruptedException e) {
            Log.e(TAG, e.getMessage());
        } catch (ExecutionException e) {
            Log.e(TAG, e.getMessage());
        }


    }



    public void brainWalletAddress(String seed) {
        ethkeyBrainwalletAddress(seed);
    }


    public void brainWalletSecret(String seed) {
        ethkeyBrainwalletSecret(seed);
    }


    public void brainWalletSign(String seed, String message) {
        ethkeyBrainwalletSign(seed, message);
    }

    public String brainWalletSignSecret(String secret, String message) {
        return ethkeyBrainwalletSignSecret(secret, message);
    }


    public void rlpItem(String rlp, int position) {
        try {
            ethkeyRlpItem(rlp, position);
        } catch (Exception e) {

        }
    }


    public void keccak(String data) {
        ethkeyKeccak(data);
    }


    public void ethSign(String data) {
        ethkeyEthSign(data);
    }


    public void blockiesIcon(String seed) {
        ethkeyBlockiesIcon(seed);
    }


    public String randomPhrase(int words) {
        String seed = ethkeyRandomPhrase(words);
        return seed;
    }


    public String encryptData(String data, String password) {
        return ethkeyEncryptData(data, password);
    }


    public void decryptData(String data, String password) {
        try {
            ethkeyDecryptData(data, password);
        } catch (Exception e) {

        }
    }
}
