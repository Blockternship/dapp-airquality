package canaries.kike.pocsign;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.crypto.CipherException;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.ECKeyPair;
import org.web3j.crypto.RawTransaction;
import org.web3j.crypto.TransactionEncoder;
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
import java.math.BigDecimal;
import java.math.BigInteger;
import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.Arrays;
import java.util.Collections;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;


public class MainActivity extends AppCompatActivity {

    static {
        System.loadLibrary("signer");
    }

    private Web3j web3;
    private File walletPathFile;
    private Credentials credentials;
    private File account;
    private File key;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        synchronized (this) {
            checkPermissions();
        }

        //synchronized (this) {
            //walletPathFile= Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
            walletPathFile = new File(this.getApplicationInfo().dataDir);

            //createBIP39Wallet(this);

            //connectEth();
            //loadCredentialsBEP39();
            //transferFunds();

        //}

        TextView tv = (TextView) findViewById(R.id.sample_text);
        String seed = randomPhrase(11);
        tv.setText(ethkeyBrainwalletSecret(seed));

    }

    public void createOfflineTx(){
        String set_contract_Address = BuildConfig.contract;
        EthSendTransaction transactionResponse = null;
        try {
            Function function = new Function(
                    "reporterReward",
                    Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(BuildConfig.reporter)),
                    Collections.<TypeReference<?>>emptyList());

            Future<EthGetTransactionCount> ethGetTransactionCount = web3.ethGetTransactionCount(
                    credentials.getAddress(), DefaultBlockParameterName.LATEST)
                    .sendAsync();
            BigInteger count =ethGetTransactionCount.get().getTransactionCount();

            Log.e("ETH",count.toString());

            String encodedFunction = FunctionEncoder.encode(function);
            RawTransaction rawTransaction = RawTransaction.createTransaction(
                    count, ManagedTransaction.GAS_PRICE, Contract.GAS_LIMIT, set_contract_Address, BigInteger.ZERO,  encodedFunction);

            byte[] signedMessage = TransactionEncoder.signMessage(rawTransaction, credentials);
            String hexValue = Numeric.toHexString(signedMessage);

            transactionResponse = web3.ethSendRawTransaction(hexValue).sendAsync().get();
            Log.i("ETH", ("Transaction complete, view it at https://rinkeby.etherscan.io/tx/"
                    + transactionResponse.getTransactionHash()));

        } catch (InterruptedException e) {
            Log.e("ERROR ETH INT",e.getMessage());
        } catch (ExecutionException e) {
            Log.e("ERROR ETH EXEC",e.getMessage() + Arrays.deepToString(e.getStackTrace()));
        }



    }

    public void loadCredentialsBEP39() {
        try {
            credentials =
                    WalletUtils.loadCredentials(
                            "12345678",
                            new File(walletPathFile, "heyapple"));
            Log.v("ETH", ("Credentials loaded Address is::: "+ credentials.getAddress()));
            createOfflineTx();
        } catch (IOException e) {
            Log.e("ETH",e.getMessage());
        } catch (CipherException e) {
            Log.e("ETH",e.getMessage());
        }
    }





    private void checkPermissions(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                String[] permissions = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE};
                requestPermissions(permissions, 101);
            }
        }
    }

    /* Should work on pc, android crash */
    public void createBIP39Wallet(Context ctx) {
        try {
            WalletUtils.generateBip39Wallet("12345678", walletPathFile);
        } catch (CipherException e) {
            Log.e("ETH",e.getMessage());
        } catch (IOException e) {
            Log.e("ETH",e.getMessage());
        }

    }


    /* Should work on pc, android crash */
    public void createWallet(Context ctx) {
        try {
            BigInteger key = new BigInteger("", 16);
            ECKeyPair keypair = ECKeyPair.create(key);
            account = new File(walletPathFile, "");
            WalletUtils.generateWalletFile("", keypair, account , true);
        } catch (CipherException e) {
            Log.e("ETH",e.getMessage());
        } catch (IOException e) {
            Log.e("ETH",e.getMessage());
        }

    }

    public void connectEth() {
        web3 = Web3jFactory.build(new HttpService(BuildConfig.infura));  // defaults to http://localhost:8545/
        Web3ClientVersion web3ClientVersion = null;
        try {
            web3ClientVersion = web3.web3ClientVersion().sendAsync().get();
            String clientVersion = web3ClientVersion.getWeb3ClientVersion();
            Log.i("ETH", ("Connected to Ethereum client version: "
                    + clientVersion));
        } catch (InterruptedException e) {
            Log.e("ERROR ETH INT",e.getMessage());
        } catch (ExecutionException e) {
            Log.e("ERROR ETH EXEC",e.getMessage() + Arrays.deepToString(e.getStackTrace()));
        }

    }

    public void loadCredentials() {
        try {
            credentials =
                    WalletUtils.loadCredentials(
                            BuildConfig.pass,
                            new File(walletPathFile, BuildConfig.file));
            Log.v("ETH", ("Credentials loaded Address is::: "+ credentials.getAddress()));
            createOfflineTx();
        } catch (IOException e) {
            Log.e("ETH",e.getMessage());
        } catch (CipherException e) {
            Log.e("ETH",e.getMessage());
        }
    }

    public void transferFunds() {
        Future<TransactionReceipt> transactionReceipt = null;
        try {
            Log.i("ETH", ("Sending 1 Wei ("
                    + Convert.fromWei("1", Convert.Unit.ETHER).toPlainString() + " Ether)"));

            transactionReceipt = Transfer.sendFunds(
                    web3, credentials, "0xbd344f93e0b10f1317855f1b77e2238f1531f4a4",
                    BigDecimal.valueOf(0.5), Convert.Unit.ETHER)
                    .sendAsync();
            Log.i("ETH", ("Transaction complete, view it at https://rinkeby.etherscan.io/tx/"
                    + transactionReceipt.get().getBlockHash()));
        } catch (Exception e) {
            Log.e("ETH",""+ e.getMessage() + "" + e.getLocalizedMessage() + "" + e.toString());
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


    public void encryptData(String data, String password) {
        ethkeyEncryptData(data, password);
    }


    public void decryptData(String data, String password) {
        try {
            ethkeyDecryptData(data, password);
        } catch (Exception e) {

        }
    }

    private static native String ethkeyBrainwalletAddress(String seed);

    private static native String ethkeyBrainwalletSecret(String seed);

    private static native String ethkeyBrainwalletSign(String seed, String message);

    private static native String ethkeyRlpItem(String data, int position);

    private static native String ethkeyKeccak(String data);

    private static native String ethkeyEthSign(String data);

    private static native String ethkeyBlockiesIcon(String seed);

    private static native String ethkeyRandomPhrase(int words);

    private static native String ethkeyEncryptData(String data, String password);

    private static native String ethkeyDecryptData(String data, String password);


}
