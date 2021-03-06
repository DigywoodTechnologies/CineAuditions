package com.digywood.cineauditions;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.digywood.cineauditions.AsyncTasks.AsyncCheckInternet;
import com.digywood.cineauditions.AsyncTasks.BagroundTask;
import com.digywood.cineauditions.AsyncTasks.MyBagroundTask;
import com.digywood.cineauditions.DBHelper.DBHelper;

import org.json.JSONException;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Random;

public class RegistrationActivity extends AppCompatActivity {

    TextView RegistrationLbl;
    Typeface myTypeface1,myTypeface2,myTypeface3,myTypeface4;
    Button reg_submit,reg_cancel;
    int otpInt;
    DBHelper dbHelper;
    private AwesomeValidation awesomeValidation;
    EditText nameEt, addressEt , cityEt , stateEt,contact_PersonEt, phnoEt,emailIDEt;
    String nameSt, addressSt , citySt , stateSt, contact_PersonSt, phnoSt, emailIDSt, otp,status,url,regDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        myTypeface1 = Typeface.createFromAsset(getAssets(), "fonts/Raleway-Medium.ttf");

        RegistrationLbl = (TextView)findViewById(R.id.RegistrationLbl);
        reg_submit = (Button)findViewById(R.id.reg_submit);
        reg_cancel = (Button)findViewById(R.id.reg_cancel);
        nameEt = (EditText)findViewById(R.id.userNameET_ForReg);
        addressEt = (EditText)findViewById(R.id.addressET_ForReg);
        cityEt = (EditText)findViewById(R.id.cityET_ForReg);
        stateEt = (EditText)findViewById(R.id.stateET_ForReg);
        phnoEt = (EditText)findViewById(R.id.phnoET_ForReg);
        emailIDEt = (EditText)findViewById(R.id.emailET_ForReg);
        dbHelper = new DBHelper(this);

        awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);
        awesomeValidation.addValidation(this, R.id.userNameET_ForReg, "^[a-zA-Z0-9_ ]*$",R.string.nameerror);
//        awesomeValidation.addValidation(this, R.id.addressET_ForReg, "^[a-zA-Z0-9_# ]*$",R.string.addresserror);
        awesomeValidation.addValidation(this, R.id.cityET_ForReg, "^[a-zA-Z_ ]*$", R.string.cityerror);
        awesomeValidation.addValidation(this, R.id.stateET_ForReg, "^[a-zA-Z_ ]*$", R.string.stateerror);
        awesomeValidation.addValidation(this, R.id.emailET_ForReg, Patterns.EMAIL_ADDRESS, R.string.emailerror);
        awesomeValidation.addValidation(this, R.id.phnoET_ForReg, "^([0-9]{1})([0-9]{9})$",R.string.mobileerror);

        RegistrationLbl.setTypeface(myTypeface1);
        reg_submit.setTypeface(myTypeface1);
        reg_cancel.setTypeface(myTypeface1);

        reg_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (awesomeValidation.validate()) {

                    nameSt = nameEt.getText().toString();
                    addressSt = addressEt.getText().toString();
                    citySt = cityEt.getText().toString();
                    stateSt = stateEt.getText().toString();
                    contact_PersonSt = "";
                    phnoSt = phnoEt.getText().toString();
                    emailIDSt = emailIDEt.getText().toString();
                    status = "created";
                    Calendar c1 = Calendar.getInstance();
                    SimpleDateFormat sdf1 = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                    regDate = sdf1.format(c1.getTime());

                    new AsyncCheckInternet(RegistrationActivity.this,new INetStatus() {
                        @Override
                        public void inetSatus(Boolean netStatus) {
                            if(netStatus){
                                HashMap<String, String> hmap = new HashMap<String, String>();
                                url =URLClass.hosturl+"insertProducerDetails.php";
                                hmap.put("producer_name", nameSt);
                                hmap.put("address", addressSt);
                                hmap.put("city", citySt);
                                hmap.put("state", stateSt);
                                hmap.put("contact_person", contact_PersonSt);
                                hmap.put("phno", phnoSt);
                                hmap.put("emailId", emailIDSt);
                                otp=getOtp();
                                if(otp.isEmpty()){
                                    otp=getOtp();
                                }
                                Log.e("OTP: ",""+otp);
                                hmap.put("otpNo",otp);
                                hmap.put("dateOfRegistration", regDate);
                                hmap.put("status", status);

                                try {

                                    new BagroundTask(url, hmap, RegistrationActivity.this, new IBagroundListener() {
                                        @Override
                                        public void bagroundData(String json) {
                                            Log.e("ja", "comes:" + json);
                                            if (json.equals("User_Exist")) {

                                                Toast.makeText(RegistrationActivity.this,"User Already Exist,Please Login",Toast.LENGTH_SHORT).show();
                                                finish();
                                                Intent intent = new Intent(RegistrationActivity.this,FullscreenActivity.class);
                                                startActivity(intent);

                                            } else {

                                                if(json.equalsIgnoreCase("Inserted")){
                                                    dbHelper.insertNewProducer(nameSt, addressSt, citySt, stateSt, contact_PersonSt, phnoSt, emailIDSt, otp, regDate, status);

                                                    //Toast.makeText(RegistrationActivity.this, "MobileNo"+phnoSt, Toast.LENGTH_SHORT).show();
                                                    new MyBagroundTask("http://www.jcbpoint.com/sms/sms.php", otp, phnoSt, RegistrationActivity.this, new IBagroundListener() {
                                                        @Override
                                                        public void bagroundData(String json) throws JSONException {

                                                            Intent intent = new Intent(RegistrationActivity.this,OTPEntryActivity.class);
                                                            intent.putExtra("mobileNo",phnoSt);
                                                            startActivity(intent);
                                                            finish();
                                                            Toast.makeText(RegistrationActivity.this, "Registration Successfull", Toast.LENGTH_LONG).show();

                                                        }
                                                    }).execute();

                                                }else{
                                                    Toast.makeText(RegistrationActivity.this, "Registration failed", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        }
                                    }).execute();

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }else{
                                Toast.makeText(getApplicationContext(),"No Internet,Please Check Your Connection",Toast.LENGTH_SHORT).show();
                            }

                        }
                        public String getOtp() {
                            Random r = new Random();
                            otpInt = r.nextInt(9999 - 1000) + 1000;
                            otp = String.valueOf(otpInt);
                            return otp;
                        }

                    }).execute();

                }
            }
        });

        reg_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Intent intent = new Intent(RegistrationActivity.this,FullscreenActivity.class);
                startActivity(intent);
            }
        });

    }
}
