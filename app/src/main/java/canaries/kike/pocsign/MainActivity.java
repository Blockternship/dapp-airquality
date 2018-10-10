package canaries.kike.pocsign;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import org.web3j.crypto.Credentials;

import org.web3j.protocol.Web3j;


import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;


import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;

import butterknife.ButterKnife;
import canaries.kike.pocsign.view.WalletFragment;


public class MainActivity extends BaseActivity {

    static {
        System.loadLibrary("signer");
    }

    private Web3j web3;
    private File walletPathFile;
    private Credentials credentials;
    private File account;
    private File key;
    private WalletFragment walletFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        synchronized (this) {

            checkPermissions();
            ButterKnife.bind(this);
            addWalletFragment();

        }
    }

/*
    TODO: create lets encrypt cert for this.
    private  SSLContext getSSLConfig(Context context) {
        Certificate ca;
        try {
            // Loading CAs from an InputStream
            CertificateFactory cf = null;
            cf = CertificateFactory.getInstance("X.509");
            //InputStream cert = context.getResources().openRawResource(R.raw.);
            InputStream cert = context.getResources().openRawResource(1);
            ca = cf.generateCertificate(cert);
            // Creating a KeyStore containing our trusted CAs
            String keyStoreType = KeyStore.getDefaultType();
            KeyStore keyStore = KeyStore.getInstance(keyStoreType);
            keyStore.load(null, null);
            keyStore.setCertificateEntry("ca", ca);

            // Creating a TrustManager that trusts the CAs in our KeyStore.
            String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
            TrustManagerFactory tmf = TrustManagerFactory.getInstance(tmfAlgorithm);
            tmf.init(keyStore);

            // Creating an SSLSocketFactory that uses our TrustManager
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, tmf.getTrustManagers(), null);

            return sslContext;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (CertificateException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyStoreException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }


    }
*/
    private void addWalletFragment() {
        if (walletFragment == null) walletFragment = walletFragment.newInstance();
        addFragment(walletFragment, walletFragment.TAG, false);
    }


    private void setupUI() {
            addWalletFragment();
            //showFragment(scanFragment);

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


    @Override
    void onPermissionGranted() {

    }
}
