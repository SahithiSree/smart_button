package com.vaagdevi.smartbutton;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.telephony.SmsManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class ContactaAdapter extends RecyclerView.Adapter<ContactaAdapter.HomeComViewHolder> {
    Context context;
    ArrayList<Getset>homeComs;
    private List<Getset> homeComList;
    private AdapterView.OnItemClickListener listener;

   ContactaAdapter(){}

    public ContactaAdapter(List<Getset> hList, AdapterView.OnItemClickListener listener) {
        this.homeComList = hList;
        this.listener = listener;
    }

    public ContactaAdapter(Context c, ArrayList<Getset> h) {
        context = c;
        homeComs = h;
    }

    @NonNull
    @Override
    public ContactaAdapter.HomeComViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ContactaAdapter.HomeComViewHolder(LayoutInflater.from(context).inflate(R.layout.contactlist,
                parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ContactaAdapter.HomeComViewHolder holder, int position) {

        holder.bind(homeComs.get(position), listener);
        holder.cname.setText(homeComs.get(position).getCname());
        holder.cnumber.setText(homeComs.get(position).getCnumber());
        holder.latitude=homeComs.get(position).getLatitude();
        holder.longitude=homeComs.get(position).getLongitude();
        holder.Cuid=homeComs.get(position).getCnumber();
      //Toast.makeText(Context.this,holder.latitude,Toast.LENGTH_SHORT).show();




   }

    @Override
    public int getItemCount() {
        return homeComs.size();
    }


    public class HomeComViewHolder extends RecyclerView.ViewHolder {

        TextView cname,cnumber;
        String latitude,longitude,Cuid;
        DatabaseReference dref;

        ImageButton delete;


        public HomeComViewHolder(View itemView) {

            super(itemView);


            cname = itemView.findViewById(R.id.cname);
            cnumber = itemView.findViewById(R.id.cnumber);
            delete=itemView.findViewById(R.id.delete);
          //  latitude=itemView.findViewById(R.id.latitude);

            ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.SEND_SMS, Manifest.permission.READ_SMS}, PackageManager.PERMISSION_GRANTED);

            dref= FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        }

        public void bind(Getset item, AdapterView.OnItemClickListener listener) {
            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    dref.child("contacts").child(Cuid).removeValue();



        }

    });
}
    }
}
