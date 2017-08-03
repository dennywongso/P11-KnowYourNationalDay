package com.example.a15017096.p11_knowyournationalday;

import android.Manifest;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.PermissionChecker;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private ListView lvFact;
    private ArrayList<String> alFact;
    private ArrayAdapter<String> aaFact;
    LayoutInflater inflater;
    String text = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lvFact = (ListView)findViewById(R.id.lvFact);
        alFact = new ArrayList<>();
        aaFact = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, alFact);
        lvFact.setAdapter(aaFact);
        alFact.add("Singapore National Day is on 9 Aug");
        alFact.add("Singapore is 52 years old");
        alFact.add("Theme is '#OneNationTogether'");
        aaFact.notifyDataSetChanged();

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.send) {
            for(int s=0;s<alFact.size();s++){
                text += alFact.get(s) +", ";
            }
            String [] list = new String[] { "Email", "Sms"};
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Select the way to enrich your friend")
                    // Set the list of items easily by just supplying an
                    //  array of the items
                    .setItems(list, new DialogInterface.OnClickListener() {
                        // The parameter "which" is the item index
                        // clicked, starting from 0
                        public void onClick(DialogInterface dialog, int which) {
                            if (which == 0) {
                                    Intent email = new Intent(Intent.ACTION_SEND);
                                    // Put essentials like email address, subject & body text
                                    email.putExtra(Intent.EXTRA_EMAIL,
                                            new String[]{"low.shun.cheong2@gmail.com"});
                                    email.putExtra(Intent.EXTRA_SUBJECT,
                                            "Test Email from C347");
                                    email.putExtra(Intent.EXTRA_TEXT,
                                            text);
                                    // This MIME type indicates email
                                    email.setType("message/rfc822");
                                    // createChooser shows user a list of app that can handle
                                    // this MIME type, which is, email
                                    startActivity(Intent.createChooser(email,
                                            "Choose an Email client :"));


                            }  else {

                                int permissionCheck = PermissionChecker.checkSelfPermission
                                        (MainActivity.this, Manifest.permission.SEND_SMS);

                                if (permissionCheck != PermissionChecker.PERMISSION_GRANTED){
                                    ActivityCompat.requestPermissions(MainActivity.this,
                                            new String[]{Manifest.permission.SEND_SMS}, 0);
                                    return;
                                }

                                SmsManager smsManager = SmsManager.getDefault();
                                smsManager.sendTextMessage("5554", null, text, null, null);
                                Toast.makeText(MainActivity.this, "Send successfully",Toast.LENGTH_SHORT).show();

                            }
                        }
                    });
            AlertDialog alertDialog = builder.create();
            alertDialog.show();


        } else if (item.getItemId() == R.id.quiz) {
            inflater = (LayoutInflater)
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final LinearLayout passPhrase =
                    (LinearLayout) inflater.inflate(R.layout.quiz, null);
            final RadioGroup rg1 = (RadioGroup)passPhrase.findViewById(R.id.rg1);
            final RadioGroup rg2 = (RadioGroup)passPhrase.findViewById(R.id.rg2);
            final RadioGroup rg3 = (RadioGroup)passPhrase.findViewById(R.id.rg3);


            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Test Yourself!")
                    .setView(passPhrase)
                    .setNegativeButton("DON'T KNOW LAH", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(MainActivity.this,"Your score: 0", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {
                            int score = 0;
                            int selectedButtonId1 = rg1.getCheckedRadioButtonId();
                            int selectedButtonId2 = rg2.getCheckedRadioButtonId();
                            int selectedButtonId3 = rg3.getCheckedRadioButtonId();
                            RadioButton rb1 = (RadioButton)passPhrase.findViewById(selectedButtonId1);
                            RadioButton rb2 = (RadioButton)passPhrase.findViewById(selectedButtonId2);
                            RadioButton rb3 = (RadioButton)passPhrase.findViewById(selectedButtonId3);
                            if(rb1.getText().toString().equals("No")){
                                score++;
                            }
                            if(rb2.getText().toString().equals("Yes")){
                                score++;
                            }
                            if(rb3.getText().toString().equals("Yes")){
                                score++;
                            }
                            Toast.makeText(MainActivity.this,"Your score: "+score,Toast.LENGTH_SHORT).show();

                        }
                    });
            AlertDialog alertDialog = builder.create();
            alertDialog.show();

        }
         else if (item.getItemId() == R.id.quit) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Are you sure want to log out?")
                    // Set text for the positive button and the corresponding
                    //  OnClickListener when it is clicked
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            SharedPreferences pref = getSharedPreferences("myPref",MainActivity.MODE_PRIVATE);
                            SharedPreferences.Editor editor = pref.edit();
                            editor.putString("pass","no");
                            editor.commit();
                            finish();
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                        }
                    });
            // Create the AlertDialog object and return it
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 0: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    SmsManager smsManager = SmsManager.getDefault();
                    smsManager.sendTextMessage("5554", null, text, null, null);
                    Toast.makeText(MainActivity.this, "Send successfully",Toast.LENGTH_SHORT).show();
                } else {
                    // permission denied... notify user
                    Toast.makeText(MainActivity.this, "Permission not granted",
                            Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences pref = getSharedPreferences("myPref",MainActivity.MODE_PRIVATE);
        String pass = pref.getString("pass","no");
        if(pass.equals("no")){
            inflater = (LayoutInflater)
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            LinearLayout passPhrase =
                    (LinearLayout) inflater.inflate(R.layout.login, null);
            final EditText etPass = (EditText) passPhrase
                    .findViewById(R.id.etPass);
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Please login")
                    .setView(passPhrase)
                    .setNegativeButton("NO ACCESS CODE", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(MainActivity.this, "Please call 12345678 to get the access code", Toast.LENGTH_LONG).show();
                            finish();
                        }
                    })
                    .setCancelable(false)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {
                            String value = etPass.getText().toString();
                            if(!value.equals(null) ) {
                                if (Integer.parseInt(value) == 111) {
                                    SharedPreferences pref = getSharedPreferences("myPref",MainActivity.MODE_PRIVATE);
                                    SharedPreferences.Editor editor = pref.edit();
                                    editor.putString("pass","yes");
                                    editor.commit();
                                } else {
                                    Toast.makeText(MainActivity.this, "Wrong Pass Code", Toast.LENGTH_SHORT).show();
                                    finish();
                                }
                            }
                        }
                    });
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        }
    }
}
