package com.vaagdevi.smartbutton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.Settings;
import android.telephony.SmsManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Set;

public class Home extends AppCompatActivity {//implements LocationListener {
   // private static final String TAG = ;

    private Button b1,b2,b3,b4,button5,btLocation,pickcontact;
    private static final int REQUEST_ENABLE_BT = 0;
    private static final int MY_PERMISSION_SEND_SMS=0;
    private TextView showLocation;
    private LocationManager locationManager;
    private LocationListener listener;
    String latitude, longitude;
    ListView lv;
    private BluetoothAdapter BA;
    private Set<BluetoothDevice> pairedDevices;
    TextView tvCity, tvState, tvCountry, tvPin, tvLocality;
    ListView contactNumber;
    ArrayList<String> contactList;
    ArrayAdapter<String> adapter;
    DatabaseReference dref;
    String link;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
//        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
//                && ActivityCompat.checkSelfPermission(getApplicationContext(),
//                android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION,
//                    android.Manifest.permission.ACCESS_COARSE_LOCATION}, 101);
//        }
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS, Manifest.permission.READ_SMS}, PackageManager.PERMISSION_GRANTED);
//       showLocation = (TextView) findViewById(R.id.showLocation);
//        btLocation = (Button) findViewById(R.id.bt_location);
        b1 = (Button) findViewById(R.id.button2);
        b3 = (Button) findViewById(R.id.button3);
        b4 = (Button) findViewById(R.id.button4);
        BA = BluetoothAdapter.getDefaultAdapter();
        lv = (ListView) findViewById(R.id.listView);
        pickcontact=findViewById(R.id.pickcontact);
        button5=findViewById(R.id.button5);

        contactList = new ArrayList<>();
        adapter = new ArrayAdapter<String>(Home.this, android.R.layout.simple_list_item_1, contactList);

      //  contactNumber = findViewById(R.id.contactnumber);

      //  Button buttonPickContact = (Button)findViewById(R.id.pickcontact);
//        buttonPickContact.setOnClickListener(new Button.OnClickListener(){
//
//            @Override
//            public void onClick(View arg0) {
//                // TODO Auto-generated method stub
//
//
//                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
//                intent.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE);
//                startActivityForResult(intent, 1);
//
//
//            }});


//        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
//        locationEnabled();
//        getLocation();
//        btLocation.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
//                if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
//                    OnGPS();
//                } else {
//                    locationEnabled();
//                    getLocation();
//                }
//            }
//        });
        button5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(Home.this,Login_Activity.class));
            }
        });
        pickcontact.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
           startActivity(new Intent(Home.this,Contacts.class));
//                HashMap<String, Object> Usermap = new HashMap<>();
//               Usermap.put("latitude",latitude);
//               Usermap.put("longitude",longitude);
//                Usermap.put("link",link);
//
//                dref.updateChildren(Usermap).addOnCompleteListener(new OnCompleteListener<Void>() {
//                    @Override
//                    public void onComplete(@NonNull Task<Void> task) {
//                        Toast.makeText(Home.this,link, Toast.LENGTH_SHORT).show();
//                    }
//                });
            }
        });



    }
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        // TODO Auto-generated method stub
//        super.onActivityResult(requestCode, resultCode, data);
//
//        if(requestCode == 1){
//            if(resultCode == RESULT_OK){
//                Uri contactData = data.getData();
//                Cursor cursor =  managedQuery(contactData, null, null, null, null);
//                cursor.moveToFirst();
//
//                String number = cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.NUMBER));
//                String name = cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
//                // String number = cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.NUMBER));
//
//                contactList.add(name);
//                contactList.add(number);
//                contactNumber.setAdapter(adapter);
//                //contactEmail.setText(email);
//                contactNumber.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                    @Override
//                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                        contactList.remove(position);
//                        adapter.notifyDataSetChanged();
//                    }
//                });
//            }
//        }
//    }
    private void OnGPS() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Enable GPS").setCancelable(false).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
            }
        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        final AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
//    private void locationEnabled() {
//        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
//        boolean gps_enabled = false;
//        boolean network_enabled = false;
//        try {
//            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        try {
//            network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        if (!gps_enabled && !network_enabled) {
//            new AlertDialog.Builder(Home.this)
//                    .setTitle("Enable GPS Service")
//                    .setMessage("We need your GPS location to show Near Places around you.")
//                    .setCancelable(false)
//                    .setPositiveButton("Enable", new
//                            DialogInterface.OnClickListener() {
//                                @Override
//                                public void onClick(DialogInterface paramDialogInterface, int paramInt) {
//                                    startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
//                                }
//                            })
//                    .setNegativeButton("Cancel", null)
//                    .show();
//        }
//    }
//
//    void getLocation() {
//        try {
//            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
//            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 500, 5, (LocationListener) this);
//        } catch (SecurityException e) {
//            e.printStackTrace();
//        }
//    }
//
//    @Override
//    public void onLocationChanged(Location location) {
//        try {
//            Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
//            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
//            double lat = location.getLatitude();
//            double longi = location.getLongitude();
//            latitude = String.valueOf(lat);
//            longitude = String.valueOf(longi);
//            String link = "http://www.google.com/maps/place/" + latitude + "," + longitude;
//            showLocation.setText("Your Location: " + "\n" + "Latitude: " + latitude + "\n" + "Longitude: " + longitude + link);
//            String message = link;
//
//
//            String number="";
//        //    DatabaseReference number = FirebaseDatabase.getInstance().getReference("contacts").child(FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber());
//
//            SmsManager smsManager = SmsManager.getDefault();
//
//            //   String number = editTextNumber.getText().toString().trim();
//            smsManager.sendTextMessage(number, null, message, null, null);
//
////            tvCity.setText(addresses.get(0).getLocality());
////            tvState.setText(addresses.get(0).getAdminArea());
////            tvCountry.setText(addresses.get(0).getCountryName());
////            tvPin.setText(addresses.get(0).getPostalCode());
////            tvLocality.setText(addresses.get(0).getAddressLine(0));
//
//        } catch (Exception e) {
//        }
//    }
//
//    @Override
//    public void onStatusChanged(String provider, int status, Bundle extras) {
//
//    }
//
//    @Override
//    public void onProviderEnabled(String provider) {
//
//    }
//
//    @Override
//    public void onProviderDisabled(String provider) {
//
//    }
//    public void sendSMS(View view) {
//        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS);
//        if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
//            mymessage();
//        } else {
//            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS}, MY_PERMISSION_SEND_SMS);
//
//        }
//    }
//    public void mymessage(){
//
//        String message = editTextMessage.getText().toString().trim();
//
//        String number = editTextNumber.getText().toString().trim();
//        if(number==null || number.equals("") || message==null || message.equals("")){
//            Toast.makeText(this,"Fields are empty",Toast.LENGTH_LONG).show();
//        }
//        else{
//            if(TextUtils.isDigitsOnly(number)) {
//                SmsManager mySmsManager = SmsManager.getDefault();
//
//
//                mySmsManager.sendTextMessage(number, null, message, null,null);
//
//                Toast.makeText(this,"message send",Toast.LENGTH_LONG).show();
//            }
//            else{
//                Toast.makeText(this,"pls enter valid number",Toast.LENGTH_LONG).show();
//            }
//        }
//
//    }
    public void On(View v) {
        if (!BA.isEnabled()) {
            Intent turnOn = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(turnOn, 1);
            Toast.makeText(getApplicationContext(), "Turned on", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(getApplicationContext(), "Already on", Toast.LENGTH_LONG).show();
        }
    }

    public void Off(View v) {
        BA.disable();
        Toast.makeText(getApplicationContext(), "Turned off", Toast.LENGTH_LONG).show();
    }


    public void visible(View v) {
        Intent getVisible = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
        startActivityForResult(getVisible, 0);
    }


    public void List(View v) {
        pairedDevices = BA.getBondedDevices();

        ArrayList list = new ArrayList();

        for (BluetoothDevice bt : pairedDevices) list.add(bt.getName());
        Toast.makeText(getApplicationContext(), "Showing Paired Devices", Toast.LENGTH_SHORT).show();

        final ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, list);

        lv.setAdapter(adapter);
    }




//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        switch(requestCode){
//            case REQUEST_ENABLE_BT:
//                if(requestCode==RESULT_OK){
////                    img.setImageResource(R.drawable.ic_action_on);
//                    showToast("bluetooth is on");
//                }
////                else{
////                    showToast("couldn't on bluetooth");
////                }
////                break;
//        }
//        super.onActivityResult(requestCode, resultCode, data);
//    }
//    private void showToast(String msg){
//        Toast.makeText(this,msg,Toast.LENGTH_SHORT).show();
//
//    }

}