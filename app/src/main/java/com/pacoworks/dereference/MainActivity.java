package com.pacoworks.dereference;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;

import com.pacoworks.dereference.reactive.ActivityResult;
import com.pacoworks.dereference.reactive.PermissionResult;
import com.pacoworks.dereference.reactive.ReactiveActivity;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static android.content.pm.PackageManager.PERMISSION_DENIED;
import static android.content.pm.PackageManager.PERMISSION_GRANTED;

public class MainActivity extends AppCompatActivity {

    private final ReactiveActivity reactiveActivity = new ReactiveActivity();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        reactiveActivity.onCreate();
    }

    @Override
    protected void onStart() {
        super.onStart();
        reactiveActivity.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        reactiveActivity.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        reactiveActivity.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        reactiveActivity.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        reactiveActivity.onDestroy();
        if (isFinishing()) {
            reactiveActivity.onFinish();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (null != data && null != data.getExtras()) {
                final Bundle extras = data.getExtras();
                reactiveActivity.onActivityResult(new ActivityResult.SuccessWithData(requestCode, bundleToMap(extras)));
            } else {
                reactiveActivity.onActivityResult(new ActivityResult.Success(requestCode));
            }
        } else if (resultCode == RESULT_CANCELED) {
            if (null != data && null != data.getExtras()) {
                final Bundle extras = data.getExtras();
                reactiveActivity.onActivityResult(new ActivityResult.FailureWithData(requestCode, bundleToMap(extras)));
            } else {
                reactiveActivity.onActivityResult(new ActivityResult.Failure(requestCode));
            }
        }
    }

    private Map<String, Object> bundleToMap(Bundle extras) {
        HashMap<String, Object> results = new HashMap<>(extras.size());
        final Set<String> keys = extras.keySet();
        for (String key : keys) {
            results.put(key, extras.get(key));
        }
        return results;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        for (int i = 0; i < permissions.length; i++) {
            if (grantResults[i] == PERMISSION_GRANTED) {
                reactiveActivity.getPermissionResultRelay().call(new PermissionResult.Success(requestCode, permissions[i]));
            } else if (grantResults[i] == PERMISSION_DENIED) {
                reactiveActivity.getPermissionResultRelay().call(new PermissionResult.Failure(requestCode, permissions[i]));
            }
        }
    }
}
