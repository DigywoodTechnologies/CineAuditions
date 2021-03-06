package com.digywood.cineauditions;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.media.Image;
import android.media.ImageWriter;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
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
import com.digywood.cineauditions.AsyncTasks.AdvtBagroundTask;
import com.digywood.cineauditions.AsyncTasks.AsyncCheckInternet;
import com.digywood.cineauditions.AsyncTasks.BagroundTask;
import com.digywood.cineauditions.DBHelper.DBHelper;
import com.digywood.cineauditions.Pojo.SingleAdvt;
import com.digywood.cineauditions.Pojo.SingleCategory;
import com.digywood.cineauditions.Pojo.SingleSubCategory;
import com.digywood.cineauditions.Pojo.SingleSubcat;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.TimeZone;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;


public class AdvtInfoScreen extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    ArrayList<SingleCategory> CategoryList = new ArrayList<>();
    ArrayList<SingleSubCategory> SubCategoryList = new ArrayList<>();
    final ArrayList<String> CategoryNamesList = new ArrayList<>();
    final ArrayList<String> SubCategoryNamesList = new ArrayList<>();
    ArrayList<SingleAdvt> AdvtList = new ArrayList<>();
    ArrayList<CategoryCheck> CategoryCheckedList = new ArrayList();

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
    int count=0;
    String path="",fileType=null;
    String myfile;
    ArrayList<String> myList=new ArrayList<>();
    CustomGrid adapter;
    byte[] imagebyte=null;
    Bitmap bitmap=null;
    String timeStamp;
    Calendar cal1,cal2;
    HashMap<String, String> hmap = new HashMap<>();
    private AwesomeValidation awesomeValidation;
    EditText captionEt,descEt,contactnameEt,phnoEt,emailIdEt;
    Typeface myTypeface1,myTypeface2,myTypeface3,myTypeface4;
    String fileName=null,fileUrl=null,enddate="";
    String categoryId,MobileNo,captionSt,descSt,startdateSt,endDateSt,contactnameSt,phnoSt,emailIdSt,status,url,url1,downloadDate,orgIdSt,encodedImage=null;

    final int REQUEST_CODE_GALLERY = 999;
    public static final int RequestPermissionCode = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advt_info_screen);

        awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);
        awesomeValidation.addValidation(this, R.id.contactnameET, "^[a-zA-Z0-9_ ]*$",R.string.nameerror);
        awesomeValidation.addValidation(this, R.id.phnoET, "^([0-9]{1})([0-9]{9})$", R.string.mobileerror);
        awesomeValidation.addValidation(this, R.id.emailET, Patterns.EMAIL_ADDRESS,R.string.emailerror);

        title_newAdvt = findViewById(R.id.title_newAdvt);
//        start_date = (Button)findViewById(R.id.btn_startDate);
        end_date = (Button)findViewById(R.id.btn_endDate);
        btn_submit = (Button)findViewById(R.id.submit_Info);
        btn_browse = (Button)findViewById(R.id.upload_Image);
        imageView = (ImageView)findViewById(R.id.imgView);
        startdateEt = (TextView)findViewById(R.id.startDateEt);
        endDateEt = (TextView)findViewById(R.id.endDateEt);
        captionEt = (EditText)findViewById(R.id.captionET);
        descEt = (EditText)findViewById(R.id.descET);
        contactnameEt = (EditText)findViewById(R.id.contactnameET);
        phnoEt = (EditText)findViewById(R.id.phnoET);
        emailIdEt = (EditText)findViewById(R.id.emailET);
        s1 = (Spinner)findViewById(R.id.services);
        grid=findViewById(R.id.grid);
        s1.setOnItemSelectedListener(this);
        dbHelper = new DBHelper(this);

        grid.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                v.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }

        });

        myTypeface1 = Typeface.createFromAsset(getAssets(),"fonts/Raleway-Medium.ttf");

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

        title_newAdvt.setText(Html.fromHtml("<u>Advertisement Details</u> "));

        title_newAdvt.setTypeface(myTypeface1);
        btn_browse.setTypeface(myTypeface1);
        btn_submit.setTypeface(myTypeface1);

        ArrayAdapter<String> dataAdapter2 = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item,CategoryNamesList);
        dataAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dataAdapter2.notifyDataSetChanged();
        s1.setAdapter(dataAdapter2);

        timeStamp= new SimpleDateFormat("dd-MM-yyyy").format(Calendar.getInstance(TimeZone.getDefault()).getTime());

        startdateEt.setText(timeStamp);

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
                                enddate=""+dayOfMonth + "-" + (monthOfYear + 1) + "-" + year;

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });


        btn_browse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checkPermission()){
                    Intent intent = new Intent(Intent.ACTION_PICK);
                    intent.setType("image/*");
                    startActivityForResult(intent, REQUEST_CODE_GALLERY);
                }else{
                    requestPermission();
                }
            }
        });

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {
                    Date date1 = new SimpleDateFormat("dd-MM-yyyy").parse(timeStamp);
                    Date date2 = new SimpleDateFormat("dd-MM-yyyy").parse(enddate);
                    cal1 = Calendar.getInstance();
                    cal2 = Calendar.getInstance();
                    cal1.setTime(date1);
                    cal2.setTime(date2);
                }catch (Exception e){
                    e.printStackTrace();
                    Log.e("AdvtInfoScreen---",e.toString());
                }

                if (awesomeValidation.validate() ) {

                    if(!enddate.equalsIgnoreCase("") && cal1.before(cal2)){

                        new AsyncCheckInternet(AdvtInfoScreen.this, new INetStatus() {
                            @Override
                            public void inetSatus(Boolean netStatus) {

                                if(netStatus){

                                    try {
                                        myList = adapter.getChkList();
                                    }catch (Exception e){
                                        e.printStackTrace();
                                        Log.e("AdvtInfoScreen---",e.toString());
                                    }

                                    if(myList.size()!=0){
                                        orgIdSt ="ORG001";
                                        captionSt = captionEt.getText().toString();
                                        descSt = descEt.getText().toString();
                                        startdateSt = startdateEt.getText().toString();
                                        endDateSt = endDateEt.getText().toString();
                                        contactnameSt = contactnameEt.getText().toString();
                                        phnoSt = phnoEt.getText().toString();
                                        emailIdSt = emailIdEt.getText().toString();
                                        status ="created";

                                        Calendar c1 = Calendar.getInstance(TimeZone.getDefault());
                                        SimpleDateFormat sdf1 = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                                        downloadDate = sdf1.format(c1.getTime());

                                        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
                                        String currentDateandTime = sdf.format(new Date());

                                        if(fileType!=null){

                                            fileName=MobileNo+"_"+currentDateandTime +fileType;
                                            fileUrl=URLClass.imageurl+MobileNo+"_"+currentDateandTime +fileType;

                                        }else{

                                            fileType="";
                                            fileName="";
                                            fileUrl="";

                                        }

                                        url = URLClass.hosturl+"insertAdvtInfo.php";
                                        hmap.clear();
                                        hmap.put("orgId",orgIdSt);
                                        hmap.put("userId",MobileNo);
                                        hmap.put("caption",captionSt);
                                        hmap.put("description",descSt);
                                        hmap.put("fileType",fileType);
                                        hmap.put("fileName",fileName);
                                        hmap.put("filePath",fileUrl);
                                        hmap.put("startDate",startdateSt);
                                        hmap.put("endDate",endDateSt);
                                        hmap.put("contactName",contactnameSt);
                                        hmap.put("contactNumber",phnoSt);
                                        hmap.put("emailId",emailIdSt);
                                        hmap.put("createdTime",downloadDate);
                                        hmap.put("status",status);

                                        final String str = "";

                                        try {
                                            //inserting advertisement into server
                                            new AdvtBagroundTask(url,hmap,MobileNo,path,fileName,AdvtInfoScreen.this,new IBagroundListener() {
                                                @Override
                                                public void bagroundData(String json) {
                                                    Log.e("ja", "comes:" + json);

                                                    if (!json.equalsIgnoreCase("Not Inserted")) {
                                                        advtId = Integer.parseInt(json);
                                                        Toast.makeText(AdvtInfoScreen.this, "Advt Inserted ", Toast.LENGTH_LONG).show();
                                                        //inserting advertisement into local advertisement list
                                                        long insertFlag=dbHelper.insertNewAdvt(advtId,orgIdSt,MobileNo,captionSt,descSt,fileType,fileName,fileUrl,startdateSt,endDateSt,contactnameSt,phnoSt,emailIdSt,downloadDate,status);
                                                        if(insertFlag>0){
                                                            Toast.makeText(getApplicationContext(),"Inserted",Toast.LENGTH_SHORT).show();
                                                            insertCatSubcat(advtId);
                                                        }else{
                                                            Toast.makeText(getApplicationContext(),"Advt Insertion failed in Local",Toast.LENGTH_SHORT).show();
                                                        }

                                                    } else {
                                                        Toast.makeText(AdvtInfoScreen.this, "Insertion failed in Server", Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            }).execute();
                                            Log.v("jo", str);

                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }else{
                                        Toast.makeText(AdvtInfoScreen.this,"Please Choose Any Services", Toast.LENGTH_LONG).show();
                                    }

                                }else{
                                    Toast.makeText(getApplicationContext(),"No Internet,Please Check Your Connection",Toast.LENGTH_SHORT).show();
                                }

                            }
                        }).execute();
                    }else{
                        showAlert("End Date Should be greater than Current Date, Please Choose Valid One");
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

        Log.e("SpinItem---",sp1);
        String CatId=dbHelper.getCatId(sp1);
        Log.e("CatItem---",CatId);
        ArrayList<String> subcatList=dbHelper.getSubCategoriesByCat(CatId);

        if(count>0){
            adapter.updateGrid(subcatList);
            adapter.notifyDataSetChanged();
            grid.setAdapter(adapter);
        }else{
            count++;
            adapter = new CustomGrid(AdvtInfoScreen.this,subcatList);
            Log.d("selected_list", "comes:" + SubCategoryNamesList);
            grid.setAdapter(adapter);
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }


    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if(requestCode == RequestPermissionCode){
            if(grantResults.length >0 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED){
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
                String path = uri.getPath();
                File f=null;
                try {
                    f = new File(path);
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

                path=getPath(AdvtInfoScreen.this,uri);
                Log.e("AdvtInfoScreen",path);
                fileType=path.substring(path.lastIndexOf("."));
                myfile=path;

                try {
                    InputStream inputStream = getContentResolver().openInputStream(uri);
                    bitmap = BitmapFactory.decodeStream(inputStream);
                    imageView.setImageBitmap(bitmap);
                    imagebyte=convertImageToByte(uri,imgSize);
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

    public void insertCatSubcat(int advtId){

        try {
            myList= adapter.getChkList();
        }catch (Exception e){
            e.printStackTrace();
            Log.e("AdvtInfoScreen---",e.toString());
        }

        Log.e("AdvtInfoScreen---",""+myList.size());

        if(myList.size()!=0){

            JSONObject myjo=JSONEncode(advtId,myList);
            hmap.clear();
            url= URLClass.hosturl+"insertAdvtCategoryInfo.php";
            hmap.put("CatSubcatData",myjo.toString());
            try {
                new BagroundTask(url,hmap,AdvtInfoScreen.this,new IBagroundListener() {
                    @Override
                    public void bagroundData(String json) {
                        Log.d("ja", "comes:" + json);
                        if (json.equals("Inserted")) {
                            Intent intent = new Intent(AdvtInfoScreen.this, LandingActivity.class);
                            intent.putExtra("mobileNo", MobileNo);
                            intent.putExtra("key", "F2");
                            overridePendingTransition(0, 0);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                            finish();
                            startActivity(intent);
                            Toast.makeText(AdvtInfoScreen.this, "Advt CatSubcat Inserted successfully", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(AdvtInfoScreen.this, "Advt CatSubcat Insertion failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                }).execute();

            } catch (Exception e) {
                e.printStackTrace();
            }

        }else{
            Intent intent = new Intent(AdvtInfoScreen.this, LandingActivity.class);
            intent.putExtra("mobileNo", MobileNo);
            intent.putExtra("key", "F2");
            overridePendingTransition(0, 0);
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            finish();
            startActivity(intent);
            Log.e("AdvtInfoScreen---","Empty Cat and Subcat");
        }

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

    public static String getPath(final Context context,final Uri uri) {
        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];
                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {
                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));
                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];
                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }
                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{split[1]};
                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {
            // Return the remote address
            if (isGooglePhotosUri(uri))
                return uri.getLastPathSegment();
            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }
        return null;
    }

    public static String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {
        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {column};
        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }

    public JSONObject JSONEncode(int advtId,ArrayList<String> finalList){
        JSONObject job=new JSONObject();
        JSONArray UserCatSubcat = new JSONArray();
        try{
            JSONObject CatSubcat;
            for(int i=0;i<finalList.size();i++){
                CatSubcat = new JSONObject();
                CatSubcat.put("orgId","ORG001");
                CatSubcat.put("advtId",advtId);
                ArrayList<SingleSubcat> mysubcatList=dbHelper.getCatIdbySubcatid(finalList.get(i));
                SingleSubcat mysubcat=mysubcatList.get(0);
                CatSubcat.put("category",mysubcat.getCatid());
                CatSubcat.put("subCategory",mysubcat.getSubcatid());
                String catname=dbHelper.getCatName(mysubcat.getCatid());
                CatSubcat.put("catName",catname);
                CatSubcat.put("subCatName",finalList.get(i));

//                Log.e("Values: "," Catid: "+mysubcat.getCatid()+" Subcatid: "+mysubcat.getSubcatid()+" catName: "+catname+" SubcatName: "+finalList.get(i));

                UserCatSubcat.put(CatSubcat);
            }
            job.put("CatSubcatData",UserCatSubcat);
        }catch (Exception e){
            e.printStackTrace();
        }

        return job;
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(AdvtInfoScreen.this, new
                String[]{WRITE_EXTERNAL_STORAGE,READ_EXTERNAL_STORAGE},RequestPermissionCode);
    }


    public boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(getApplicationContext(),
                WRITE_EXTERNAL_STORAGE);
        int result1 = ContextCompat.checkSelfPermission(getApplicationContext(),
                READ_EXTERNAL_STORAGE);
        return result == PackageManager.PERMISSION_GRANTED && result1==PackageManager.PERMISSION_GRANTED;
    }

}

