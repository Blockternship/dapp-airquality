package canaries.kike.pocsign.view;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;


import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;

import butterknife.BindView;
import butterknife.ButterKnife;
import canaries.kike.pocsign.R;
import canaries.kike.pocsign.api.GitHubApi;
import canaries.kike.pocsign.api.ResponseCallback;
import canaries.kike.pocsign.crypto.CryptoUtils;
import okhttp3.ResponseBody;


/**
 * Created by Antonio Vanegas @hpsaturn on 6/30/18.
 */
public class WalletFragment extends Fragment  implements View.OnClickListener{

    private static final String GITHUB_USER = "hi";

    public static String TAG = WalletFragment.class.getSimpleName();
    private static Context context;


    @BindView(R.id.tv_address)
    TextView tv_address;

    @BindView(R.id.tv_balance)
    TextView tv_balance;

    @BindView(R.id.et_pwd)
    EditText et_pwd;

    @BindView(R.id.bt_check_balance)
    Button bt_balance;

    @BindView(R.id.bt_create_wallet)
    Button bt_wallet;

    @BindView(R.id.bt_send_tx)
    Button bt_tx;

    @BindView(R.id.bt_create_issue)
    Button bt_issue;


    @BindView(R.id.progress_bar)
    ProgressBar pb_loader;



    private int i;
    private long referenceTimestamp;
    private boolean loadingData = true;

    public static final String KEY_RECORD_ID = "key_record_id";
    private String recordId;
    private String walletFile;
    private static Web3j web3;
    private boolean networkCallIsInProgress;
    private String address;

    public static WalletFragment newInstance() {
        WalletFragment fragment = new WalletFragment();
        return fragment;
    }

    public static WalletFragment newInstance(String recordId) {
        WalletFragment fragment = new WalletFragment();
        Bundle args = new Bundle();
        args.putString(KEY_RECORD_ID,recordId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        context= getActivity();
        View view = inflater.inflate(R.layout.fragment_wallet, container, false);
        ButterKnife.bind(this, view);
        bt_balance.setOnClickListener(this);
        bt_tx.setOnClickListener(this);
        bt_wallet.setOnClickListener(this);
        bt_issue.setOnClickListener(this);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    public void touchWallet(View view) {
        walletFile = CryptoUtils.genWallet(context, et_pwd.getText().toString());
        Credentials credentials = CryptoUtils.loadCredentials(context, walletFile, et_pwd.getText().toString());
        address = credentials.getAddress();
        web3 = CryptoUtils.connectEth();
        String balance = CryptoUtils.getAccountBalance(web3,address);
        tv_address.setText(address);
        tv_balance.setText(balance);
    }

    public void sendTx(View view) {
        //web3 = CryptoUtils.connectEth();
        //String address = credentials.getAddress();
        String balance = CryptoUtils.getAccountBalance(web3, address);
        tv_balance.setText(balance);

    }

    public void updateBalance(View view) {
        Credentials credentials = CryptoUtils.loadCredentials(context, walletFile, et_pwd.getText().toString());
        String tx_reporter = CryptoUtils.createOfflineContracReporterstTx(web3, "reporterReward", credentials);
        CryptoUtils.sendRawSignedTx(web3, tx_reporter, credentials);
    }

    private void syncUI() {
        if (networkCallIsInProgress) {
            pb_loader.setVisibility(View.VISIBLE);
        } else {
            pb_loader.setVisibility(View.INVISIBLE);
        }
    }

    private void setNetworkCallInProgress(final boolean inProgress) {
        networkCallIsInProgress = inProgress;
        syncUI();
    }

    private void makeNetworkCall() {
       // setNetworkCallInProgress(true);
        //String address = credentials.getAddress();
        String yamlBody = String.format(
                "name: AIRQ bot testing reports\n" +
                "motivation: im a bot testing airquality reports\n" +
                "address: '%s'", address);
        final GitHubApi gitHubApi = GitHubApi.getSharedInstance();

        ResponseCallback<ResponseBody> cbGithub = new ResponseCallback<ResponseBody>() {
            @Override
            public void onSuccess(@NonNull ResponseBody result) {

            }

            @Override
            public void onError() {

            }

            @Override
            public void onFailure() {

            }
        };
        gitHubApi.createIssue(cbGithub, yamlBody);
    }



    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bt_check_balance:
                updateBalance(v);
                break;
            case R.id.bt_create_wallet:
                touchWallet(v);
                break;
            case R.id.bt_send_tx:
                sendTx(v);
                break;
	   case R.id.bt_create_issue:
                makeNetworkCall();
                break;


        }

    }
}
