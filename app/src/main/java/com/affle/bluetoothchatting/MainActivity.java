package com.affle.bluetoothchatting;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.tbruyelle.rxpermissions2.RxPermissions;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

import java.util.ArrayList;
import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends RxAppCompatActivity implements View.OnClickListener {

    @BindView(R.id.btn_search_devices)
    Button btnSearchDevices;
    @BindView(R.id.rv_bluetooth_devices)
    RecyclerView rvBluetoothDevices;
    BluetoothAdapter bluetoothAdapter;
    ArrayList arrayList = new ArrayList();
    BluetoothDevicesAdapter bluetoothDevicesAdapter;
    private boolean bluetoothSupported = false;
    private int dummy=10000;


    public BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            switch (intent.getAction()) {
                case BluetoothAdapter.ACTION_DISCOVERY_STARTED:
                    Toast.makeText(MainActivity.this, "Discovery Started", Toast.LENGTH_SHORT).show();
                    break;
                case BluetoothAdapter.ACTION_DISCOVERY_FINISHED:
                    bluetoothDevicesAdapter = new BluetoothDevicesAdapter(arrayList);
                    if (arrayList.size() > 0) {
                        rvBluetoothDevices.setAdapter(bluetoothDevicesAdapter);
                        bluetoothDevicesAdapter.val().subscribe(new Observer<String>() {
                            @Override
                            public void onSubscribe(@NonNull Disposable d) {
                                Toast.makeText(MainActivity.this, "Clicked on $it", Toast.LENGTH_LONG).show();
                            }

                            @Override
                            public void onNext(@NonNull String s) {

                            }

                            @Override
                            public void onError(@NonNull Throwable e) {

                            }

                            @Override
                            public void onComplete() {

                            }
                        });
                    } else {
                        Toast.makeText(MainActivity.this, "No Device Found", Toast.LENGTH_SHORT).show();
                    }
                    Toast.makeText(MainActivity.this, "Discovery Finished", Toast.LENGTH_SHORT).show();
                    bluetoothAdapter.cancelDiscovery();
                    break;
                case BluetoothDevice.ACTION_FOUND:
                    BluetoothDevice device3 = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                    BlueToothDevices bluetoothDevices = new BlueToothDevices();
                    bluetoothDevices.setAddress(device3.getAddress());
                    bluetoothDevices.setClassName(device3.getBluetoothClass().toString());
                    bluetoothDevices.setName(device3.getName());
                    arrayList.add(bluetoothDevices);
                    Toast.makeText(MainActivity.this, "Device Found " + bluetoothDevices.getAddress(), Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        btnSearchDevices.setOnClickListener(this);

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        bluetoothDevicesAdapter = new BluetoothDevicesAdapter(arrayList);
        rvBluetoothDevices.setLayoutManager(new LinearLayoutManager(MainActivity.this));
        rvBluetoothDevices.setAdapter(bluetoothDevicesAdapter);
        if (savedInstanceState != null) {
            Toast.makeText(MainActivity.this, savedInstanceState.getString("Message", ""), Toast.LENGTH_SHORT).show();
            arrayList = ((ArrayList) savedInstanceState.getSerializable("BluetoothDevices"));
            rvBluetoothDevices.setAdapter(new BluetoothDevicesAdapter(arrayList));

        } else {
            RxPermissions rxPermissions = new RxPermissions(this);
            if (bluetoothAdapter != null) {
                rxPermissions
                        .request(Manifest.permission.BLUETOOTH, Manifest.permission.BLUETOOTH_ADMIN, Manifest.permission.ACCESS_COARSE_LOCATION)
                        .subscribeOn(Schedulers.io())
                        .compose(this.<Boolean>bindToLifecycle())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Observer<Boolean>() {
                            @Override
                            public void onSubscribe(Disposable d) {
                                Toast.makeText(MainActivity.this, "test onSubscribe", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onNext(Boolean value) {
                                if (!bluetoothAdapter.isEnabled()) {
                                    Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                                    startActivityForResult(enableBtIntent, 1001);
                                }


                            }

                            @Override
                            public void onError(Throwable e) {
                                Toast.makeText(MainActivity.this, "test onError", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onComplete() {
                                Toast.makeText(MainActivity.this, "test onComplete", Toast.LENGTH_SHORT).show();
                            }
                        });
            } else {
                Toast.makeText(MainActivity.this, "Bluetooth not supported", Toast.LENGTH_SHORT).show();
            }
        }


    }


    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        intentFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
        intentFilter.addAction(BluetoothDevice.ACTION_FOUND);

        registerReceiver(mReceiver, intentFilter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(mReceiver);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1001 && resultCode == RESULT_OK) {
            Toast.makeText(MainActivity.this, "Bluetooth enabled", Toast.LENGTH_SHORT).show();
            bluetoothSupported = true;
        } else {
            Toast.makeText(MainActivity.this, "Please enable Bluetooth to continue", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_search_devices:
                if (bluetoothSupported) {
                    // If we're already discovering, stop it
                    if (bluetoothAdapter.isDiscovering()) {
                        bluetoothAdapter.cancelDiscovery();
                    } else
                        Toast.makeText(MainActivity.this, "Bluetooth not supported", Toast.LENGTH_SHORT).show();

                    bluetoothAdapter.startDiscovery();
                } else {
                    Toast.makeText(MainActivity.this, "Bluetooth not supported", Toast.LENGTH_SHORT).show();


                    BlueToothDevices bluetoothDevices = new BlueToothDevices();
                    bluetoothDevices.setAddress(""+dummy);
                    bluetoothDevices.setClassName(""+dummy);
                    bluetoothDevices.setName(""+dummy);
                    arrayList.add(bluetoothDevices);
                    bluetoothDevicesAdapter.notifyDataSetChanged();
                    dummy++;
                }
                break;
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e("onDestroy", "Activity destroyed");
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
//        outState.putString(GAME_STATE_KEY, mGameState);
//        outState.putString(TEXT_VIEW_KEY, mTextView.getText());
        outState.putString("Message", "Hello,Screen Configuration changed.");
        outState.putSerializable("BluetoothDevices", arrayList);
        // call superclass to save any view hierarchy
        super.onSaveInstanceState(outState);
    }


}
