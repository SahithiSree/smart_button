package com.vaagdevi.smartbutton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Activity;
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
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.Settings;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class Contacts extends AppCompatActivity implements LocationListener{
    Button contactnum;
    RecyclerView Rview;
    private static final int MY_PERMISSION_SEND_SMS=0;
    ArrayList<Getset> display;
    DatabaseReference dref;
    ContactaAdapter adapter;
    private TextView showLocation;
    private LocationManager locationManager;
    private LocationListener listener;
    Button btLocation;
    String latitude, longitude,Cuid;
    Activity dialogView;
    Button delete;
  private String cnumber;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getApplicationContext(),
                android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION}, 101);
        }
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS, Manifest.permission.READ_SMS}, PackageManager.PERMISSION_GRANTED);
        showLocation = (TextView) findViewById(R.id.showLocation);
        btLocation = (Button) findViewById(R.id.bt_location);

        contactnum = findViewById(R.id.contactnum);
        Rview = findViewById(R.id.displaycntcs);
     //   delete=findViewById(R.id.delete);

        //     final Button buttonDelete = (Button) dialogView.findViewById(R.id.buttonDeleteContact);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationEnabled();
        getLocation();

        btLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    //OnGPS();
                } else {
                    locationEnabled();
                    getLocation();
                }
            }
        });

        dref = FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        display = new ArrayList<Getset>();
        dref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

              //   try {
                      display.clear();
                      String clink=snapshot.child("link").getValue().toString();
if(snapshot.child("contacts").exists()) {
    for (DataSnapshot d1 : snapshot.child("contacts").getChildren()) {
        String scontact = d1.child("cnumber").getValue().toString();
        Getset h = d1.getValue(Getset.class);
        display.add(h);
        if(!clink.equals("")) {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(scontact, null, clink, null, null);
        }
    }

}

                //} catch (Exception e) {
              // e.printStackTrace();
             //   }
                adapter = new ContactaAdapter(Contacts.this, display);
                Rview.setAdapter(adapter);
            }




            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        // Button buttonPickContact = (Button)findViewById(R.id.pickcontact);
        contactnum.setOnClickListener(new Button.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub


                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE);
                startActivityForResult(intent, 1);


            }
        });
        Rview.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        Rview.setLayoutManager(linearLayoutManager);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        Rview.setNestedScrollingEnabled(false);




    }


    //    @Override
//    public void onActivityResult(int RequestCode, int ResultCode, Intent ResultIntent) {
//
//        super.onActivityResult(RequestCode, ResultCode, ResultIntent);
//
//        switch (RequestCode) {
//
//            case (7):
//                if (ResultCode == Activity.RESULT_OK) {
//
//                    Uri uri;
//                    Cursor cursor1, cursor2;
//                    String TempNameHolder, TempNumberHolder, TempContactID, IDresult = "";
//                    int IDresultHolder;
//
//                    uri = ResultIntent.getData();
//
//                    cursor1 = getContentResolver().query(uri, null, null, null, null);
//
//                    if (cursor1.moveToFirst()) {
//
//                        TempNameHolder = cursor1.getString(cursor1.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
//
//                        TempContactID = cursor1.getString(cursor1.getColumnIndex(ContactsContract.Contacts._ID));
//
//                        IDresult = cursor1.getString(cursor1.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));
//
//                        IDresultHolder = Integer.valueOf(IDresult);
//
//                        if (IDresultHolder == 1) {
//
//                            cursor2 = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + TempContactID, null, null);
//
//                            while (cursor2.moveToNext()) {
//
//                                TempNumberHolder = cursor2.getString(cursor2.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
//                                Calendar c = Calendar.getInstance();
//                                SimpleDateFormat dateformat = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss aa");
//                                String datetime = dateformat.format(c.getTime());
////                                HashMap<String, Object> Usermap = new HashMap<>();
////                                Usermap.put("cname",TempNameHolder);
////                                Usermap.put("cnumber",TempNumberHolder);
////                                dref.child(datetime).updateChildren(Usermap).addOnCompleteListener(new OnCompleteListener<Void>() {
////                                    @Override
////                                    public void onComplete(@NonNull Task<Void> task) {
//                                        Toast.makeText(Contacts.this, TempNumberHolder, Toast.LENGTH_SHORT).show();
//
//
//                            //}
//
//
//                               // });
//                            }
//                        }
//
//
//                break;
//        }
//    }
//}
//        }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                Uri contactData = data.getData();
                Cursor cursor = managedQuery(contactData, null, null, null, null);
                cursor.moveToFirst();

                final String number = cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.NUMBER));
                String name = cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                // String number = cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.NUMBER));
               // Calendar c = Calendar.getInstance();
                //SimpleDateFormat dateformat = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss aa");
              //  final String datetime = dateformat.format(c.getTime());

                final HashMap<String, Object> Usermap = new HashMap<>();
                Usermap.put("cname", name);
                Usermap.put("cnumber", number);

                String scontact=number;
               //Usermap.put("Cuid",datetime);
                final Task<Void> Contacts = dref.child("contacts").child(number).updateChildren(Usermap).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        Toast.makeText(Contacts.this, number, Toast.LENGTH_SHORT).show();
                    }

                });


//
//                dref.addValueEventListener(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot snapshot) {
//                        for (DataSnapshot d1 : snapshot.child("contacts").getChildren()) {
//                            String scontact = d1.child("contacts").getValue().toString();
//                            if (number.equals(scontact)) {
//                                Toast.makeText(Contacts.this, "number is already saved ", Toast.LENGTH_SHORT).show();
//                            } else {
//                                dref.child("contacts").child(datetime).updateChildren(Usermap).addOnCompleteListener(new OnCompleteListener<Void>() {
//                                    @Override
//                                    public void onComplete(@NonNull Task<Void> task) {
//                                        Toast.makeText(Contacts.this, number, Toast.LENGTH_SHORT).show();
//                                    }
//                                });
//
//                            }
//                        }
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError error) {
//
//                    }
//                });
            }
        }
    }
    private void locationEnabled() {
        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        boolean gps_enabled = false;
        boolean network_enabled = false;
        try {
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (!gps_enabled && !network_enabled) {
            new AlertDialog.Builder(Contacts.this)
                    .setTitle("Enable GPS Service")
                    .setMessage("We need your GPS location to show Near Places around you.")
                    .setCancelable(false)
                    .setPositiveButton("Enable", new
                            DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                                    startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                                }
                            })
                    .setNegativeButton("Cancel", null)
                    .show();
        }
    }

    void getLocation() {
        try {
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 500, 5, (LocationListener) this);
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        try {
            Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            double lat = location.getLatitude();
            double longi = location.getLongitude();
            latitude = String.valueOf(lat);
            longitude = String.valueOf(longi);
            String link = "http://www.google.com/maps/place/" + latitude + "," + longitude;
            showLocation.setText("Your Location: " + "\n" + "Latitude: " + latitude + "\n" + "Longitude: " + longitude + link);
            String message = link;
            HashMap<String, Object> Usermap = new HashMap<>();
           Usermap.put("latitude",latitude);
            Usermap.put("longitude",latitude);
            Usermap.put("link",link);

            dref.updateChildren(Usermap).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    Toast.makeText(Contacts.this,latitude, Toast.LENGTH_SHORT).show();
                }
            });



      //    String number="scontact";
            //    DatabaseReference number = FirebaseDatabase.getInstance().getReference("contacts").child(FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber());


            //   String number = editTextNumber.getText().toString().trim();


//            tvCity.setText(addresses.get(0).getLocality());
//            tvState.setText(addresses.get(0).getAdminArea());
//            tvCountry.setText(addresses.get(0).getCountryName());
//            tvPin.setText(addresses.get(0).getPostalCode());
//            tvLocality.setText(addresses.get(0).getAddressLine(0));

        } catch (Exception e) {
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    public void Delete(View view) {
        dref.child("contacts").removeValue();
    }
}











