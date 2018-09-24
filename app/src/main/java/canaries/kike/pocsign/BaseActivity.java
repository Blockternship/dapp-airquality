package canaries.kike.pocsign;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.intentfilter.androidpermissions.PermissionManager;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

import java.util.ArrayList;
import java.util.Collection;

import static java.util.Collections.singleton;

/**
 * Created by Antonio Vanegas @hpsaturn on 7/1/18.
 */
public abstract class BaseActivity extends RxAppCompatActivity {

    public static String TAG = BaseActivity.class.getSimpleName();


    public void checkForPermissions() {
        PermissionManager permissionManager = PermissionManager.getInstance(this);
        ArrayList<String> permissions = new ArrayList<>();
        permissions.add(Manifest.permission.ACCESS_COARSE_LOCATION);
        permissions.add(Manifest.permission.INTERNET);
        permissions.add(Manifest.permission.ACCESS_NETWORK_STATE);
        permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        permissions.add(Manifest.permission.ACCESS_COARSE_LOCATION);
        permissions.add(Manifest.permission.ACCESS_FINE_LOCATION);
        permissions.add(Manifest.permission.ACCESS_WIFI_STATE);
        permissionManager.checkPermissions(permissions, new PermissionManager.PermissionRequestListener() {
            @Override
            public void onPermissionGranted() {
            }

            @Override
            public void onPermissionDenied() {
            }
        });
    }

    abstract void onPermissionGranted();


    public void showFragment(Fragment fragment){
        if(fragment!=null) {
            try {
                Log.d(TAG,"showFragment: "+fragment.getTag());
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.show(fragment).commitAllowingStateLoss();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void hideFragment(Fragment fragment){
        if(fragment!=null) {
            try {
                Log.d(TAG,"hideFragment: "+fragment.getTag());
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.hide(fragment).commitAllowingStateLoss();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void addFragment(Fragment fragment, String fragmentTag, boolean toStack) {
        try {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.add(R.id.content_default, fragment, fragmentTag);
            if (toStack) ft.addToBackStack(fragmentTag);
            ft.commitAllowingStateLoss();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    public void replaceFragment(Fragment fragment, String fragmentTag, boolean toStack) {
        try {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_default, fragment, fragmentTag);
            if (toStack) ft.addToBackStack(fragmentTag);
            ft.commitAllowingStateLoss();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void replaceFragment(Fragment fragment, String fragmentTag, boolean toStack, int content) {

        try {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(content, fragment, fragmentTag);
            if (toStack) ft.addToBackStack(fragmentTag);
            ft.commitAllowingStateLoss();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void showFragmentFull(Fragment fragment, String fragmentTag, boolean toStack) {

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.content_default, fragment, fragmentTag);
        if (toStack) ft.addToBackStack(fragmentTag);
        ft.commitAllowingStateLoss();

    }

    public void showDialog(Fragment fragment, String fragmentTag) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.add(fragment, fragmentTag);
        ft.show(fragment);
        ft.commitAllowingStateLoss();
    }

    public void popBackStackSecure(String TAG) {
        try {
            Log.d(TAG, "popBackStackSecure to: " + TAG);
            getSupportFragmentManager().popBackStack(TAG, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void popBackLastFragment() {
        if (getSupportFragmentManager().getBackStackEntryCount() != 0) {
            Log.d(TAG, "onBackPressed popBackStack for:" + getLastFragmentName());
            getSupportFragmentManager().popBackStack();
        }
    }


    public void removeFragment(Fragment fragment) {
        if(fragment!=null) {
            try {
                Log.w(TAG, "removing fragment: " + fragment.getClass().getSimpleName());
                FragmentManager fm = getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.remove(fragment).commitAllowingStateLoss();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public String getLastFragmentName() {
        try {
            if (getSupportFragmentManager().getBackStackEntryCount() == 0) return "";
            FragmentManager fm = getSupportFragmentManager();
            return fm.getBackStackEntryAt(fm.getBackStackEntryCount() - 1).getName();
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public boolean isFragmentInStack(String tag) {
        try {
            if (getSupportFragmentManager().getBackStackEntryCount() == 0) return false;
            FragmentManager fm = getSupportFragmentManager();
            Fragment match = fm.findFragmentByTag(tag);
            if (match != null) return true;
            else return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }



}
