package com.affle.bluetoothchatting;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;

/**
 * Created by akash on 13/7/17.
 */

public class BluetoothDevicesAdapter extends RecyclerView.Adapter<BluetoothDevicesAdapter.ViewHolder>{
    ArrayList<BlueToothDevices> arrayList = new ArrayList();
    private PublishSubject<String> clickSubject =PublishSubject.create();


    public BluetoothDevicesAdapter(ArrayList<BlueToothDevices> arrayList) {
        this.arrayList=arrayList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.row_bluetooth_devices,null);
        return new ViewHolder(view);
    }

    public PublishSubject<String> val()
    {
        return clickSubject;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder,int position){
        holder.tvDevice.setText(arrayList.get(position).getAddress());
    }

    @Override
    public int getItemCount(){
        return arrayList.size();
    }

class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    @BindView(R.id.tv_device_name)
    TextView tvDevice;


    public ViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        itemView.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        clickSubject.onNext(this.getLayoutPosition()+"");
    }
}
}


