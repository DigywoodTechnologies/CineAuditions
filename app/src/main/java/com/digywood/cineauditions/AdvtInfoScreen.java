package com.digywood.cineauditions;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Base64;
import android.util.Log;
import android.util.Patterns;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.digywood.cineauditions.Adapters.CustomGrid;
import com.digywood.cineauditions.AsyncTasks.BagroundTask;
import com.digywood.cineauditions.DBHelper.DBHelper;
import com.digywood.cineauditions.Pojo.SingleAdvt;
import com.digywood.cineauditions.Pojo.SingleAdvtCategory;
import com.digywood.cineauditions.Pojo.SingleCategory;
import com.digywood.cineauditions.Pojo.SingleSubCategory;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class AdvtInfoScreen extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    ArrayList<SingleCategory> CategoryList = new ArrayList<>();
    ArrayList<SingleSubCategory> SubCategoryList = new ArrayList<>();
    final ArrayList<String> CategoryNamesList = new ArrayList<>();
    final ArrayList<String> SubCategoryNamesList = new ArrayList<>();
    ArrayList<SingleAdvt> AdvtList = new ArrayList<>();
    ArrayList<CategoryCheck> CategoryCheckedList = new ArrayList<>();
    public CategoryCheck checkcat =new CategoryCheck();
    CustomGrid adapter ;
    String[] subcatlist;
    int[] _intSubCat,_intCat;
    TextView title_newAdvt,phno,startdateEt,endDateEt;
    Button start_date,end_date,btn_submit,btn_browse;
    private int mYear, mMonth, mDay;
    ImageView imageView;
    DBHelper dbHelper;
    Spinner s1;
    Boolean check;
    GridView grid;
    private ProgressDialog dialog;
    BagroundTask task1;
    int advtId;
    byte[] imagebyte=null;
    static Bitmap bitmap=null;
    HashMap<String, String> hmap = new HashMap<>();
    private AwesomeValidation awesomeValidation;
    EditText captionEt,descEt,contactnameEt,phnoEt,emailIdEt;
    Typeface myTypeface1;
    File mypath;
    FileOutputStream fos = null;
    String path = android.os.Environment.getExternalStorageDirectory().toString()+ "/AuditionsPlus/PostedAds";
    String categoryId,MobileNo,captionSt,descSt,startdateSt,endDateSt, contactnameSt,phnoSt,emailIdSt,status,url,url1
            ,downloadDate,orgIdSt,encodedImage=null;


    final int REQUEST_CODE_GALLERY = 999;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advt_info_screen);

        awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);
        awesomeValidation.addValidation(this, R.id.contactnameET, "^[a-zA-Z0-9_ ]*$", R.string.nameerror);
        awesomeValidation.addValidation(this, R.id.phnoET, "^([7-9]{1})([0-9]{9})$", R.string.mobileerror);
        awesomeValidation.addValidation(this, R.id.emailET, Patterns.EMAIL_ADDRESS, R.string.emailerror);

        title_newAdvt = findViewById(R.id.title_newAdvt);
        start_date = findViewById(R.id.btn_startDate);
        end_date = findViewById(R.id.btn_endDate);
        btn_submit = findViewById(R.id.submit_Info);
        btn_browse = findViewById(R.id.upload_Image);
        imageView =  findViewById(R.id.imgView);
        startdateEt = findViewById(R.id.startDateEt);
        endDateEt =  findViewById(R.id.endDateEt);
        captionEt =  findViewById(R.id.captionET);
        descEt = findViewById(R.id.descET);
        contactnameEt = findViewById(R.id.contactnameET);
        phnoEt = findViewById(R.id.phnoET);
        emailIdEt = findViewById(R.id.emailET);
        s1 = findViewById(R.id.services);
        grid=findViewById(R.id.grid);
        s1.setOnItemSelectedListener(this);
        dbHelper = new DBHelper(this);
        adapter = new CustomGrid(AdvtInfoScreen.this, SubCategoryNamesList);
        File file = new File(path);

        if(!file.exists())
        {
            file.mkdirs();
        }


        grid.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                v.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }

        });

        myTypeface1 = Typeface.createFromAsset(getAssets(), "fonts/Raleway-Medium.ttf");

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            MobileNo = bundle.getString("mobileNo");
        }

        if (Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        dialog = new ProgressDialog(AdvtInfoScreen.this);
        CategoryList = dbHelper.getAllCategories();
        SubCategoryList = dbHelper.getAllSubCategories();
        _intSubCat= new int[SubCategoryList.size()];
        _intCat= new int[CategoryList.size()];

        for(int i = 0;i < CategoryList.size();i++){
            CategoryNamesList.add(CategoryList.get(i).getLongName());
            Log.d("CategoryNamesList", "comes:" + CategoryNamesList);
        }
//        for(int i = 0;i < SubCategoryList.size();i++){
//            SubCategoryNamesList.add(SubCategoryList.get(i).getLongName());
//        }

        title_newAdvt.setText(Html.fromHtml("<u>Advertisement Details</u> "));

        title_newAdvt.setTypeface(myTypeface1);
        btn_browse.setTypeface(myTypeface1);
        btn_submit.setTypeface(myTypeface1);

        ArrayAdapter<String> dataAdapter2 = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, CategoryNamesList);
        dataAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dataAdapter2.notifyDataSetChanged();
        s1.setAdapter(dataAdapter2);

        start_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(AdvtInfoScreen.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                startdateEt.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });

        end_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(AdvtInfoScreen.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                endDateEt.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });


        btn_browse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityCompat.requestPermissions(
                        AdvtInfoScreen.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        REQUEST_CODE_GALLERY
                );
            }
        });

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (awesomeValidation.validate()) {

                    if (isInternetConnected()) {

                        orgIdSt ="ORG001";
                        captionSt = captionEt.getText().toString();
                        descSt = descEt.getText().toString();
                        startdateSt = startdateEt.getText().toString();
                        endDateSt = endDateEt.getText().toString();
                        contactnameSt = contactnameEt.getText().toString();
                        phnoSt = phnoEt.getText().toString();
                        emailIdSt = emailIdEt.getText().toString();
                        status = "created";

                        Calendar c1 = Calendar.getInstance();
                        SimpleDateFormat sdf1 = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                        downloadDate = sdf1.format(c1.getTime());

                        try {
                            if(imagebyte!=null){
                                encodedImage = Base64.encodeToString(imagebyte, Base64.DEFAULT);
                            }
                            else{
                                encodedImage=null;
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                            Log.e("AdvtInfoScreen---",e.toString());
                        }
                        url = URLClass.hosturl+"insertAdvtInfo.php";
                        hmap.clear();
                        hmap.put("orgId",orgIdSt);
                        hmap.put("userId",MobileNo);
                        hmap.put("caption",captionSt);
                        hmap.put("description",descSt);
                        hmap.put("image",encodedImage);
                        hmap.put("startDate",startdateSt);
                        hmap.put("endDate",endDateSt);//
                        hmap.put("contactName",contactnameSt);
                        hmap.put("contactNumber",phnoSt);
                        hmap.put("emailId",emailIdSt);
                        hmap.put("createdTime",downloadDate);
                        hmap.put("status",status);

                        final String str = "";

                        try {
                            if(encodedImage != null) {
                                //inserting advertisement into server
                                new BagroundTask(url, hmap, AdvtInfoScreen.this, new IBagroundListener() {
                                    @Override
                                    public void bagroundData(String json) {
                                        Log.e("ja", "comes:" + json);

                                        if (!json.equalsIgnoreCase("Not Inserted")) {
                                            advtId = Integer.parseInt(json);
                                            Toast.makeText(AdvtInfoScreen.this, "Advt Inserted ", Toast.LENGTH_LONG).show();
                                            //inserting advertisement into local advertisement list
                                            long insertFlag = dbHelper.insertNewAdvt(advtId, orgIdSt, MobileNo, captionSt, descSt, imagebyte, startdateSt, endDateSt, contactnameSt, phnoSt, emailIdSt, downloadDate, status);
                                            if (insertFlag > 0) {
                                                Toast.makeText(getApplicationContext(), "Inserted", Toast.LENGTH_SHORT).show();
//                                                insertCatSubcat(advtId);
                                                Intent intent = new Intent(AdvtInfoScreen.this, LandingActivity.class);
                                                intent.putExtra("mobileNo", MobileNo);
                                                intent.putExtra("key", "F2");
                                                overridePendingTransition(0, 0);
                                                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                                finish();
                                                startActivity(intent);
                                            } else {
                                                Toast.makeText(getApplicationContext(), "Advt Insertion failed in Local", Toast.LENGTH_SHORT).show();
                                            }

                                        } else {
                                            Toast.makeText(AdvtInfoScreen.this, "Insertion failed in Server", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }).execute();
                                Log.v("jo", str);
                            }
                            else{
                                Toast.makeText(AdvtInfoScreen.this,"Please Insert Image",Toast.LENGTH_LONG);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }finally {
                            try {
                                fos.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }

                    } else {
                        Toast.makeText(AdvtInfoScreen.this, "Please, connect to internet and try again.", Toast.LENGTH_LONG).show();
                    }
                }

            }
        });


    }


    @Override
    public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
                               long arg3) {
        // TODO Auto-generated method stub
        String sp1= String.valueOf(s1.getSelectedItem());
        //String[] subcatlist = new String[SubCategoryList.size()];

        SubCategoryNamesList.clear();
        for(int i = 0;i < CategoryList.size();i++) {
            if (sp1.equals(CategoryList.get(i).getLongName())) {
                categoryId = CategoryList.get(i).getCategoryId();
                Log.d("catlistid", CategoryList.get(i).getLongName());
                for(int j = 0;j < SubCategoryList.size();j++){
                    if(categoryId.equals(SubCategoryList.get(j).getCategoryId())){
                        //SubCategoryNamesList.clear();
                        SubCategoryNamesList.add(SubCategoryList.get(j).getLongName());
                        //subcatlist.add
                        //viewHolder.category.setText(CategoryList.get(i).getLongName());
                        //String[] list = {"Art-Deparment","Casting","Choreographer","Costume-Designer","Lighting-Technician","Media-Production","Photography","Property-Manager"};
                    }
                }
            }
        }
        Log.d("selected_category", "contains:" + SubCategoryNamesList);
        grid.setAdapter(adapter);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    public boolean isInternetConnected() {
        boolean iNetFlag = false;
        try {
            URL url = new URL("https://www.google.com");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(10000);
            connection.connect();
            iNetFlag = (connection.getResponseCode() == 200);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return iNetFlag;
    }


    @Override
    protected void onPause() {
        super.onPause();
    }

    public static byte[] imageViewToByte(ImageView image) {
        Bitmap bitmap = ((BitmapDrawable)image.getDrawable()).getBitmap();

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        long lengthbmp = byteArray.length/1024;

        return byteArray;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if(requestCode == REQUEST_CODE_GALLERY){
            if(grantResults.length >0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, REQUEST_CODE_GALLERY);
            }
            else {
                Toast.makeText(AdvtInfoScreen.this, "You don't have permission to access file location!", Toast.LENGTH_SHORT).show();
            }
            return;
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(requestCode == REQUEST_CODE_GALLERY && resultCode == RESULT_OK && data != null){
            Uri uri = data.getData();
            int dataSize=0;
            long imgSize=0;

            String scheme = uri.getScheme();
            System.out.println("Scheme type " + scheme);
            if(scheme.equals(ContentResolver.SCHEME_CONTENT))
            {
                try {
                    InputStream fileInputStream=getApplicationContext().getContentResolver().openInputStream(uri);
                    dataSize = fileInputStream.available();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                //File Size in KB
                long fileSizeInKB = dataSize / 1024;
                //File Size in MB
                float fileSizeInMB = fileSizeInKB / 1024;
                imgSize=fileSizeInKB;
                Log.e("AdvtInfoScreen---","File size1: "+fileSizeInKB);

            }
            else if(scheme.equals(ContentResolver.SCHEME_FILE))
            {
                String pathvar = uri.getPath();
                File f=null;
                try {
                    f = new File(pathvar);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                //File Size in KB
                long fileSizeInKB = f.length() / 1024;
                //File Size in MB
                float fileSizeInMB = fileSizeInKB / 1024;
                imgSize=fileSizeInKB;
                Log.e("AdvtInfoScreen---","File size2: "+fileSizeInMB);
            }

            if(imgSize>2048){

                showAlert("Selected Image Size Should be less than 2MB");

            }else{

                try {
                    InputStream inputStream = getContentResolver().openInputStream(uri);
                    bitmap = BitmapFactory.decodeStream(inputStream);
                    imageView.setImageBitmap(bitmap);
                    imagebyte=convertImageToByte(uri,imgSize);
                    mypath=new File(path,"IMG_"+advtId+".png");
                    fos = new FileOutputStream(mypath);
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e("AdvtInfoScreen---",e.toString());
                }

            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    public  void showAlert(String messege){
        AlertDialog.Builder builder = new AlertDialog.Builder(AdvtInfoScreen.this);
        builder.setMessage(messege)
                .setCancelable(false)
                .setPositiveButton("Ok",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        //Setting the title manually
        alert.setIcon(R.drawable.info);
        alert.setTitle("Info.");
        alert.show();
    }


    public byte[] convertImageToByte(Uri uri,long imgsize){
        byte[] data = null;
        try {
            ContentResolver cr = getBaseContext().getContentResolver();
            InputStream inputStream = cr.openInputStream(uri);
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            if(imgsize>1024 && imgsize<2048){
                bitmap.compress(Bitmap.CompressFormat.WEBP,30,baos);
            }else if(imgsize>700 && imgsize<1024){
                bitmap.compress(Bitmap.CompressFormat.WEBP,40,baos);
            }else if(imgsize>500 && imgsize<700){
                bitmap.compress(Bitmap.CompressFormat.WEBP,50,baos);
            }else{
                bitmap.compress(Bitmap.CompressFormat.WEBP,70,baos);
            }
            data = baos.toByteArray();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return data;
    }
/*    private void loadImageFromStorage(String path)
    {

        try {
            File f=new File(path, "profile.jpg");
            Bitmap b = BitmapFactory.decodeStream(new FileInputStream(f));
            ImageView img=(ImageView)findViewById(R.id.imgPicker);
            img.setImageBitmap(b);
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }

    }*/

}

