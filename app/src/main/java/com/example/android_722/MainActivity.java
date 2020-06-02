package com.example.android_722;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class MainActivity extends AppCompatActivity {
    private static final int MY_PERMISSIONS_REQUEST_CALL_PHONE = 11;
    private static final int MY_PERMISSIONS_REQUEST_SEND_SMS = 12;
    private EditText phoneNumber;
    private EditText smsText;
    private Button btnCall;
    private Button btnMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        phoneNumber = findViewById(R.id.edTxt_phone_number);
        smsText = findViewById(R.id.edTxt_sms_text);
        btnCall = findViewById(R.id.btn_call);
        btnMessage = findViewById(R.id.btn_message);

        btnCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callByNumber();
            }
        });

        btnMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendByNumber();
            }
        });
    }

    private void callByNumber() {
        if (isEdTxtPhoneEmpty()) {
            return;
        }

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CALL_PHONE},
                    MY_PERMISSIONS_REQUEST_CALL_PHONE);
        } else {
            String tel = phoneNumber.getText().toString();
            Uri uri = Uri.parse("tel:" + tel);
            Intent dialIntent = new Intent(Intent.ACTION_CALL, uri);
            startActivity(dialIntent);
        }
    }

    private void sendByNumber() {
        if (isEdTxtSmsEmpty()) {
            return;
        }

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.SEND_SMS},
                    MY_PERMISSIONS_REQUEST_SEND_SMS);
        } else {
            String address = phoneNumber.getText().toString();
            String text = smsText.getText().toString();

            SmsManager smgr = SmsManager.getDefault();
            smgr.sendTextMessage(address, null, text, null, null);
        }
    }

    public boolean isEdTxtPhoneEmpty() {
        if (phoneNumber.getText().toString().equals("")) {
            Toast.makeText(this, "Enter a number", Toast.LENGTH_SHORT).show();
            return true;
        }
        return false;
    }

    public boolean isEdTxtSmsEmpty() {
        if (smsText.getText().toString().equals("") || phoneNumber.getText().toString().equals("")) {
            Toast.makeText(this, "Enter a number and message text", Toast.LENGTH_SHORT).show();
            return true;
        }
        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_CALL_PHONE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    callByNumber();
                } else {
                    Toast.makeText(this, R.string.toast_no_permission, Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;
            }
            case MY_PERMISSIONS_REQUEST_SEND_SMS: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    sendByNumber();
                } else {
                    Toast.makeText(this, R.string.toast_no_permission, Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;
            }
        }
    }
}