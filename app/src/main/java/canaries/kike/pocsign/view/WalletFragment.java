package canaries.kike.pocsign.view;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;



import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import canaries.kike.pocsign.R;


/**
 * Created by Antonio Vanegas @hpsaturn on 6/30/18.
 */
public class WalletFragment extends Fragment {

    public static String TAG = WalletFragment.class.getSimpleName();


    @BindView(R.id.tv_chart_name)
    TextView chart_name;

    @BindView(R.id.tv_chart_date)
    TextView chart_date;

    @BindView(R.id.rl_separator)
    RelativeLayout rl_separator;


    private int i;
    private long referenceTimestamp;
    private boolean loadingData = true;

    public static final String KEY_RECORD_ID = "key_record_id";
    private String recordId;

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

        View view = inflater.inflate(R.layout.fragment_chart, container, false);
        ButterKnife.bind(this, view);

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

}
