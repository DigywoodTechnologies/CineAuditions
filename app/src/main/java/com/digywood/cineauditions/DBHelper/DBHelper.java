package com.digywood.cineauditions.DBHelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.digywood.cineauditions.CategoryCheck;
import com.digywood.cineauditions.Pojo.SingleAd;
import com.digywood.cineauditions.Pojo.SingleInterest;
import com.digywood.cineauditions.Pojo.SinglePreference;
import com.digywood.cineauditions.Pojo.SingleAdvt;
import com.digywood.cineauditions.Pojo.SingleAdvtCategory;
import com.digywood.cineauditions.Pojo.SingleCategory;
import com.digywood.cineauditions.Pojo.SingleProducer;
import com.digywood.cineauditions.Pojo.SingleSubCategory;
import com.digywood.cineauditions.Pojo.SingleSubcat;
import java.util.ArrayList;

public class DBHelper extends SQLiteOpenHelper {

    Context context;
    SQLiteDatabase db;
    private static final int DATABASE_VERSION = 2;

    public DBHelper(Context c)
    {
        super(c,"digywoodAdsDB",null,DATABASE_VERSION);
        this.context=c;
        db=getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String tblProducerReg_Status="CREATE TABLE IF NOT EXISTS producer_table(producer_id INTEGER PRIMARY KEY AUTOINCREMENT, name text, address text, city text, state text, contact_Person text, phno text, emailId text, otp text, regDate text, status text)";
        db.execSQL(tblProducerReg_Status);

        String tblItemTable="CREATE TABLE IF NOT EXISTS advt_info_producer(keyId INTEGER PRIMARY KEY,advtRefNo INTEGER, orgId text,producer_id text, caption text, description text," +
                "  fileType text,fileName text,filePath text,startDate text, endDate text, contactName text, contactNumber text, emailId text, createdTime text, status text)";
        db.execSQL(tblItemTable);

        String tblAdvtPrefTable="CREATE TABLE IF NOT EXISTS advt_interest_producer(keyId INTEGER PRIMARY KEY,advtRefNo INTEGER, orgId text,producer_id text,currentuserid text,caption text, description text," +
                "  fileType text,fileName text,filePath text,startDate text, endDate text, contactName text, contactNumber text, emailId text, createdTime text, status text)";
        db.execSQL(tblAdvtPrefTable);

        String tblPreferences="CREATE TABLE IF NOT EXISTS preferences_table(keyId INTEGER PRIMARY KEY AUTOINCREMENT, orgId text, userId text, " +
                "category text, subCategory text, createdBy text, createdDate text, modifiedBy text, modifiedDate text, status text)";
        db.execSQL(tblPreferences);

        String tblCategory="CREATE TABLE IF NOT EXISTS category_table(keyId INTEGER PRIMARY KEY AUTOINCREMENT,orgId text, categoryId text, longName text, shortName text, createdBy text, createdDate text, modifiedBy text, modifiedDate text, status text)";
        db.execSQL(tblCategory);

        String tblSubCategory="CREATE TABLE IF NOT EXISTS sub_category_table(keyId INTEGER PRIMARY KEY AUTOINCREMENT, orgId text, categoryId text, subCategoryId text, longName text, shortName text, createdBy text, createdDate text, modifiedBy text, modifiedDate text, status text)";
        db.execSQL(tblSubCategory);

        String tbladvtCategory="CREATE TABLE IF NOT EXISTS advt_category_table(keyId INTEGER PRIMARY KEY AUTOINCREMENT, orgId text, advtId text, category text, subCategory text)";
        db.execSQL(tbladvtCategory);

        String tblCategoryCheck="CREATE TABLE IF NOT EXISTS category_check_table(keyId INTEGER PRIMARY KEY AUTOINCREMENT, category text, subCategory text, status text)";
        db.execSQL(tblCategoryCheck);

        String tblUserIntrests="CREATE TABLE IF NOT EXISTS user_interests(seqId INTEGER PRIMARY KEY,userId text,advtId integer(10),description text,status text)";
        db.execSQL(tblUserIntrests);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        switch (oldVersion)
        {
            case 1:
                // upgrade logic from version 1 to 2
                String tblAdvtPrefTable="CREATE TABLE IF NOT EXISTS advt_pref_producer(advtId INTEGER PRIMARY KEY AUTOINCREMENT, orgId text, producer_id text, caption text, description text," +
                        " image BLOB, startDate text, endDate text, contactName text, contactNumber text, emailId text, createdTime text, status text)";
                db.execSQL(tblAdvtPrefTable);

                String tblAdvtInterestTable2="ALTER TABLE advt_interest_producer ADD COLUMN `currentuserid` text";
                db.execSQL(tblAdvtInterestTable2);

            case 2:
                // upgrade logic from version 2 to 3

                String copytable31="CREATE TABLE my_table_copy1(keyId INTEGER PRIMARY KEY,advtRefNo INTEGER, orgId text,producer_id text, caption text, description text," +
                        "  fileType text,fileName text,filePath text,startDate text, endDate text, contactName text, contactNumber text, emailId text, createdTime text, status text)";
                db.execSQL(copytable31);
                String copydata31="INSERT INTO my_table_copy1 (advtRefNo,orgId,producer_id,caption,description," +
                        "  fileType,fileName,filePath,startDate,endDate,contactName,contactNumber,emailId,createdTime,status) SELECT advtRefNo,orgId,producer_id,caption,description," +
                        "  fileType,fileName,filePath,startDate,endDate,contactName,contactNumber,emailId,createdTime,status FROM advt_info_producer";
                db.execSQL(copydata31);
                String dropexist31="DROP TABLE advt_info_producer";
                db.execSQL(dropexist31);
                String renametable31="ALTER TABLE my_table_copy1 RENAME TO advt_info_producer";
                db.execSQL(renametable31);

                String copytable32="CREATE TABLE my_table_copy2(keyId INTEGER PRIMARY KEY,advtRefNo INTEGER, orgId text,producer_id text,currentuserid text,caption text, description text," +
                        "  fileType text,fileName text,filePath text,startDate text, endDate text, contactName text, contactNumber text, emailId text, createdTime text, status text)";
                db.execSQL(copytable32);
                String copydata32="INSERT INTO my_table_copy2 (advtRefNo,orgId,producer_id,currentuserid,caption,description," +
                        "  fileType,fileName,filePath,startDate,endDate,contactName,contactNumber,emailId,createdTime,status) SELECT advtRefNo,orgId,producer_id,currentuserid,caption,description," +
                        "  fileType,fileName,filePath,startDate,endDate,contactName,contactNumber,emailId,createdTime,status FROM advt_interest_producer";
                db.execSQL(copydata32);
                String dropexist32="DROP TABLE advt_interest_producer";
                db.execSQL(dropexist32);
                String renametable32="ALTER TABLE my_table_copy2 RENAME TO advt_interest_producer";
                db.execSQL(renametable32);
                break;
            default:
                throw new IllegalStateException("onUpgrade() with unknown old version "+oldVersion);
        }
    }

    public long insertNewProducer(String name, String address, String city, String state, String contact_Person, String phno, String emailId, String otp, String regDate, String status){
        long insertFlag=0;
        ContentValues cv = new ContentValues();
        cv.put("name", name);
        cv.put("address", address);
        cv.put("city", city);
        cv.put("state", state);
        cv.put("contact_Person", contact_Person);
        cv.put("phno", phno);
        cv.put("emailId", emailId);
        cv.put("otp", otp);
        cv.put("regDate", regDate);
        cv.put("status", status);
        insertFlag = db.insert("producer_table",null, cv);
        return insertFlag;
    }

    public ArrayList<SingleSubcat> getCatIdbySubcatid(String subctname){
        ArrayList<SingleSubcat> myList=new ArrayList<>();

        Cursor c =db.query("sub_category_table", new String[] {"keyId","categoryId","subCategoryId"},"longName='"+subctname+"'", null, null, null,null);
        while (c.moveToNext()) {

            myList.add(new SingleSubcat(c.getString(c.getColumnIndex("categoryId")),c.getString(c.getColumnIndex("subCategoryId"))));
        }
        return myList;
    }

    public ArrayList<String> getSubCategoriesByCat(String cat_id){
        ArrayList<String> subcatList =new ArrayList<>();

        Cursor c =db.query("sub_category_table", new String[] {"keyId","categoryId","subCategoryId","longName"},"categoryId='"+cat_id+"'", null, null, null,null);
        while (c.moveToNext()) {

            subcatList.add(c.getString(c.getColumnIndex("longName")));
        }
        return subcatList;
    }

    public String getCatId(String catName){
        String cat_name=null;

        Cursor c =db.query("category_table", new String[] {"keyId","categoryId"},"longName='"+catName+"'", null, null, null,null);
        while (c.moveToNext()) {

            cat_name=c.getString(c.getColumnIndex("categoryId"));
        }
        return cat_name;
    }

    public String getCatName(String catId){
        String cat_id=null;

        Cursor c =db.query("category_table", new String[] {"keyId","longName"},"categoryId='"+catId+"'", null, null, null,null);
        while (c.moveToNext()) {

            cat_id=c.getString(c.getColumnIndex("longName"));
        }
        return cat_id;
    }

    public ArrayList<String> getCatNames(ArrayList<String> catIds){
        ArrayList<String> catNames=new ArrayList<>();
        String catName=null;
        String fullname=null;

        if(catIds.size()!=0){
            for(int i=0;i<catIds.size();i++){
                fullname=fullname+catIds.get(i)+",";
            }
        }

        Cursor c =db.query("category_table", new String[] {"longName"},"categoryId IN ("+fullname+")", null, null, null,null);
        while (c.moveToNext()) {
            catName=c.getString(c.getColumnIndex("longName"));
            catNames.add(catName);
        }
        return catNames;
    }

    public ArrayList<String> getSubCatNames(ArrayList<String> subCatIds){
        ArrayList<String> subCatNames=new ArrayList<>();
        String subcatName=null;
        String fullname="";

        if(subCatIds.size()!=0){
            for(int i=0;i<subCatIds.size();i++){
                if(i==0){
                    fullname="'"+subCatIds.get(i)+"'";
                }else{
                    fullname=fullname+","+"'"+subCatIds.get(i)+"'";
                }
            }
        }

        Cursor c =db.query("sub_category_table", new String[] {"longName"},"subCategoryId IN ("+fullname+")", null, null, null,null);
        while (c.moveToNext()) {
            subcatName=c.getString(c.getColumnIndex("longName"));
            subCatNames.add(subcatName);
        }
        return subCatNames;
    }

    public long updateProducer(String phno, String otp, String status, String regDate){
        long updateFlag=0;
        ContentValues cv = new ContentValues();
        cv.put("phno", phno);
        cv.put("otp", otp);
        cv.put("status", status);
        cv.put("regDate", regDate);

        updateFlag=db.update("producer_table", cv, "phno='"+phno+"'", null);
        return  updateFlag;
    }
    public long updateProducerStatus(String devId, String locationCityName)
    {
        long updateFlag=0;
        ContentValues cv=new ContentValues();
        cv.put("locationCityName", locationCityName);
        updateFlag=db.update("producer_table", cv, "devId='"+devId+"'", null);
        return  updateFlag;
    }

    public  int checkProducerExists(String phno) {
        int checkFlag=0;
        String countQuery = "select * from producer_table where phno='"+phno+"'";
        Cursor c = db.rawQuery(countQuery, null);
        checkFlag=c.getCount();
        return checkFlag;
    }
    public int deleteProducer(String phno){
        int checkFlag=0;
        checkFlag = db.delete("producer_table","phno = "+phno ,null);
        return checkFlag;
    }

    public String getOTPStatus(String phno){
        String status="";
        String countQuery = "select status from producer_table where phno='"+phno+"'";
        Cursor c = db.rawQuery(countQuery, null);
        if(c.getCount() > 0) {
            c.moveToFirst();
            status = c.getString(c.getColumnIndex("status"));
        }
        return status;

    }

//    public ArrayList<Address> getAllUserAccounts() {
//        ArrayList<Address> AddressList = new ArrayList<Address>();
//
//        Cursor c =db.query("AddressStatus", new String[] {"addressId","flatno","area","city","state","pincode","name"}, null, null, null, null, "addressId ASC");
//        while (c.moveToNext()) {
//
//            AddressList.add(new Address(c.getString(c.getColumnIndex("addressId")),c.getString(c.getColumnIndex("flatno")),c.getString(c.getColumnIndex("area")),
//                    c.getString(c.getColumnIndex("city")),c.getString(c.getColumnIndex("state")),
//                    c.getString(c.getColumnIndex("pincode")),c.getString(c.getColumnIndex("name"))
//            ));
//        }
//
//        return AddressList;
//    }

    public long insertNewAdvt( int keyId,String orgId,String producer_id,String caption, String description,String fileType,String fileName,String filePath,String startDate, String endDate,String contactName, String contactNumber, String emailId,String createdTime, String status){
        long insertFlag=0;
        ContentValues cv = new ContentValues();
        cv.put("advtRefNo", keyId);
        cv.put("orgId", orgId);
        cv.put("producer_id", producer_id);
        cv.put("caption", caption);
        cv.put("description", description);
        cv.put("fileType", fileType);
        cv.put("fileName", fileName);
        cv.put("filePath", filePath);
        cv.put("startDate", startDate);
        cv.put("endDate", endDate);
        cv.put("contactName", contactName);
        cv.put("contactNumber", contactNumber);
        cv.put("emailId", emailId);
        cv.put("createdTime", createdTime);
        cv.put("status", status);
        insertFlag = db.insert("advt_info_producer",null, cv);
        return insertFlag;
    }

    public long insertInterestedAdvt(int keyId,String orgId,String producer_id,String currentuser,String caption, String description,String fileType,String fileName,String filePath,String startDate, String endDate,String contactName, String contactNumber, String emailId,String createdTime, String status){
        long insertFlag=0;
        ContentValues cv = new ContentValues();
        cv.put("advtRefNo",keyId);
        cv.put("orgId", orgId);
        cv.put("producer_id", producer_id);
        cv.put("currentuserid", currentuser);
        cv.put("caption", caption);
        cv.put("description", description);
        cv.put("fileType", fileType);
        cv.put("fileName", fileName);
        cv.put("filePath", filePath);
        cv.put("startDate", startDate);
        cv.put("endDate", endDate);
        cv.put("contactName", contactName);
        cv.put("contactNumber", contactNumber);
        cv.put("emailId", emailId);
        cv.put("createdTime", createdTime);
        cv.put("status", status);
        insertFlag = db.insert("advt_interest_producer",null, cv);
        return insertFlag;
    }


    public long updateItem(String orgId, String producer_id,String caption, String description, byte[] image, String startDate, String endDate,String contactName, String contactNumber, String emailId,String createdTime, String status){
        long updateFlag=0;
        ContentValues cv = new ContentValues();
        cv.put("orgId", orgId);
        cv.put("producer_id", producer_id);
        cv.put("caption", caption);
        cv.put("description", description);
        cv.put("image", image);
        cv.put("startDate", startDate);
        cv.put("endDate", endDate);
        cv.put("contactName", contactName);
        cv.put("contactNumber", contactNumber);
        cv.put("emailId", emailId);
        cv.put("createdTime", createdTime);
        cv.put("status", status);
        updateFlag=db.update("advt_info_producer", cv, "producer_id='"+producer_id+"'", null);
        return  updateFlag;
    }

    public long deleteAllAdvts(String userid){
        long deleteFlag=0;
        deleteFlag=db.delete("advt_info_producer","producer_id='"+userid+"'", null);
        //db.delete("SQLITE_SEQUENCE","NAME = ?",new String[]{"ItemTable"});
        return  deleteFlag;
    }

    public long deleteAllInterestedAdvts(String userid){
        long deleteFlag=0;
        deleteFlag=db.delete("advt_interest_producer","currentuserid='"+userid+"'", null);
        //db.delete("SQLITE_SEQUENCE","NAME = ?",new String[]{"ItemTable"});
        return  deleteFlag;
    }

    public Cursor getData(String sql){
        SQLiteDatabase database = getReadableDatabase();
        return database.rawQuery(sql, null);
    }

//    public ArrayList<SingleAdvt> getAllAdvtProducer(int producer_id){
//        ArrayList<SingleAdvt> AdvtList = new ArrayList<SingleAdvt>();
//
//        Cursor c =db.query("advt_info_producer", new String[] {"advtRefNo","orgId","producer_id","caption","description","image","startDate","endDate","contactName","contactNumber","emailId","createdTime","status"}, "advtRefNo='"+producer_id+"'", null, null, null, "createdTime DESC");
//        while (c.moveToNext()) {
//
//            AdvtList.add(new SingleAdvt(c.getInt(c.getColumnIndex("advtRefNo")),c.getString(c.getColumnIndex("orgId")),
//                    c.getString(c.getColumnIndex("producer_id")),c.getString(c.getColumnIndex("caption")),
//                    c.getString(c.getColumnIndex("description")),c.getBlob(c.getColumnIndex("image")),
//                    c.getString(c.getColumnIndex("startDate")),c.getString(c.getColumnIndex("endDate")),
//                    c.getString(c.getColumnIndex("contactName")), c.getString(c.getColumnIndex("contactNumber")),
//                    c.getString(c.getColumnIndex("emailId")), c.getString(c.getColumnIndex("createdTime")),
//                    c.getString(c.getColumnIndex("status"))
//            ));
//        }
//        return AdvtList;
//    }

    public SingleAdvt getLocalAd(int producer_id){
        SingleAdvt myad =null;

        Cursor c =db.query("advt_info_producer", new String[] {"advtRefNo","orgId","producer_id","caption","description","fileType","fileName","filePath","startDate","endDate","contactName","contactNumber","emailId","createdTime","status"}, "advtRefNo='"+producer_id+"'", null, null, null, "createdTime DESC");
        while (c.moveToNext()) {

            myad=new SingleAdvt(c.getInt(c.getColumnIndex("advtRefNo")),c.getString(c.getColumnIndex("orgId")),
                    c.getString(c.getColumnIndex("producer_id")),c.getString(c.getColumnIndex("caption")),
                    c.getString(c.getColumnIndex("description")),c.getString(c.getColumnIndex("fileType")),
                    c.getString(c.getColumnIndex("fileName")),c.getString(c.getColumnIndex("filePath")),
                    c.getString(c.getColumnIndex("startDate")),c.getString(c.getColumnIndex("endDate")),
                    c.getString(c.getColumnIndex("contactName")), c.getString(c.getColumnIndex("contactNumber")),
                    c.getString(c.getColumnIndex("emailId")), c.getString(c.getColumnIndex("createdTime")),
                    c.getString(c.getColumnIndex("status")));

        }
        return myad;
    }

    public SingleAdvt getLocalInterestedAd(int advtid){
        SingleAdvt myad =null;

        Cursor c =db.query("advt_interest_producer", new String[] {"advtRefNo","orgId","producer_id","caption","description","fileType","fileName","filePath","startDate","endDate","contactName","contactNumber","emailId","createdTime","status"}, "advtRefNo='"+advtid+"'", null, null, null, null);
        while (c.moveToNext()) {

            myad=new SingleAdvt(c.getInt(c.getColumnIndex("advtRefNo")),c.getString(c.getColumnIndex("orgId")),
                    c.getString(c.getColumnIndex("producer_id")),c.getString(c.getColumnIndex("caption")),
                    c.getString(c.getColumnIndex("description")),c.getString(c.getColumnIndex("fileType")),
                    c.getString(c.getColumnIndex("fileName")),c.getString(c.getColumnIndex("filePath")),
                    c.getString(c.getColumnIndex("startDate")),c.getString(c.getColumnIndex("endDate")),
                    c.getString(c.getColumnIndex("contactName")), c.getString(c.getColumnIndex("contactNumber")),
                    c.getString(c.getColumnIndex("emailId")), c.getString(c.getColumnIndex("createdTime")),
                    c.getString(c.getColumnIndex("status")));

        }
        return myad;
    }

//    public ArrayList<SingleAdvt> getAllAdvts() {
//        ArrayList<SingleAdvt> AdvtList = new ArrayList<SingleAdvt>();
//
//        Cursor c =db.query("advt_info_producer", new String[] {"advtRefNo","orgId","producer_id","caption","description","image","startDate","endDate","contactName","contactNumber","emailId","createdTime","status"}, null, null, null, null, "createdTime DESC");
//        if(c.moveToFirst()){
//
//            while (c.moveToNext()) {
//
//                AdvtList.add(new SingleAdvt(c.getInt(c.getColumnIndex("advtRefNo")),c.getString(c.getColumnIndex("orgId")),
//                        c.getString(c.getColumnIndex("producer_id")),c.getString(c.getColumnIndex("caption")),
//                        c.getString(c.getColumnIndex("description")),c.getBlob(c.getColumnIndex("image")),
//                        c.getString(c.getColumnIndex("startDate")),c.getString(c.getColumnIndex("endDate")),
//                        c.getString(c.getColumnIndex("contactName")), c.getString(c.getColumnIndex("contactNumber")),
//                        c.getString(c.getColumnIndex("emailId")), c.getString(c.getColumnIndex("createdTime")),
//                        c.getString(c.getColumnIndex("status"))
//                ));
//            }
//
//        }
//        return AdvtList;
//    }

    public ArrayList<SingleAd> getAllAdvts(String phno) {
        ArrayList<SingleAd> AdvtList = new ArrayList<>();

        Cursor c =db.query("advt_info_producer", new String[] {"advtRefNo","caption","startDate","endDate","createdTime","status"},"producer_id='"+phno+"'", null, null, null, "createdTime DESC");
        while (c.moveToNext()) {

            AdvtList.add(new SingleAd(c.getInt(c.getColumnIndex("advtRefNo")),c.getString(c.getColumnIndex("createdTime")),
                    c.getString(c.getColumnIndex("caption")),c.getString(c.getColumnIndex("startDate")),c.getString(c.getColumnIndex("endDate"))

            ));
        }
        return AdvtList;
    }

    public ArrayList<SingleAd> getInterestedAdvts(String phno) {
        ArrayList<SingleAd> AdvtList = new ArrayList<>();

        Cursor c =db.query("advt_interest_producer", new String[] {"advtRefNo","caption","startDate","endDate","createdTime","status"},"currentuserid='"+phno+"'", null, null, null, "createdTime DESC");
        while (c.moveToNext()) {

            AdvtList.add(new SingleAd(c.getInt(c.getColumnIndex("advtRefNo")),c.getString(c.getColumnIndex("createdTime")),
                    c.getString(c.getColumnIndex("caption")),c.getString(c.getColumnIndex("startDate")),c.getString(c.getColumnIndex("endDate"))

            ));
        }
        return AdvtList;
    }

    public int getInterestedAdCount(String phno) {
        int count=0;

        Cursor c =db.query("advt_interest_producer", new String[] {"advtRefNo","caption","startDate","endDate","createdTime","status"},"producer_id='"+phno+"'", null, null, null, "createdTime DESC");
        count=c.getCount();
        return count;
    }

    public int getAdvtCount(String phno) {
        int count=0;

        Cursor c =db.query("advt_info_producer", new String[] {"advtRefNo","caption","startDate","endDate","createdTime","status"},"producer_id='"+phno+"'" , null, null, null, "createdTime DESC");
        count=c.getCount();
        return count;
    }

    public int getInterestedAdvtCount(String phno) {
        int count=0;

        Cursor c =db.query("advt_interest_producer", new String[] {"advtRefNo","caption","startDate","endDate","createdTime","status"}, "producer_id='"+phno+"'", null, null, null, "createdTime DESC");
        count=c.getCount();
        return count;
    }

    public ArrayList<SingleAdvt> getAllPrefAdvts() {
        ArrayList<SingleAdvt> AdvtList = new ArrayList<SingleAdvt>();

        Cursor c =db.query("advt_pref_producer", new String[] {"advtRefNo","orgId","producer_id","caption","description","image","startDate","endDate","contactName","contactNumber","emailId","createdTime","status"}, null, null, null, null, "createdTime DESC");
        while (c.moveToNext()) {

            AdvtList.add(new SingleAdvt(c.getInt(c.getColumnIndex("advtRefNo")),c.getString(c.getColumnIndex("orgId")),
                    c.getString(c.getColumnIndex("producer_id")),c.getString(c.getColumnIndex("caption")),
                    c.getString(c.getColumnIndex("description")),c.getString(c.getColumnIndex("fileType")),
                    c.getString(c.getColumnIndex("fileName")),c.getString(c.getColumnIndex("filePath")),
                    c.getString(c.getColumnIndex("startDate")),c.getString(c.getColumnIndex("endDate")),
                    c.getString(c.getColumnIndex("contactName")), c.getString(c.getColumnIndex("contactNumber")),
                    c.getString(c.getColumnIndex("emailId")), c.getString(c.getColumnIndex("createdTime")),
                    c.getString(c.getColumnIndex("status"))
            ));
        }
        return AdvtList;
    }

//    public ArrayList<SingleInterest> getAllInterests() {
//        ArrayList<SingleInterest> AdvtList = new ArrayList<SingleInterest>();
//
//        Cursor c =db.query("user_interests", new String[] {"userId","advtId","description","status"}, null, null, null, null, "createdTime DESC");
//        while (c.moveToNext()) {
//
//            AdvtList.add(new SingleInterest(c.getString(c.getColumnIndex("advtId")),c.getString(c.getColumnIndex("userId")),
//                    c.getString(c.getColumnIndex("description")),c.getString(c.getColumnIndex("status"))
//            ));
//        }
//        return AdvtList;
//    }

    public ArrayList<Integer> getAllInterests(String mobileno) {
        ArrayList<Integer> AdvtList = new ArrayList<>();

        Cursor c =db.query("user_interests", new String[] {"userId","advtId","description","status"},"userId='"+mobileno+"'", null, null, null, null);
        while (c.moveToNext()) {

            AdvtList.add(c.getInt(c.getColumnIndex("advtId")));
        }
        return AdvtList;
    }

    public long insertInterest(int seqId,String userId,int advtId , String description , String status ){
        long insertFlag=0;
        ContentValues cv = new ContentValues();
        cv.put("seqId", seqId);
        cv.put("userId", userId);
        cv.put("advtId", advtId);
        cv.put("description", description);
        cv.put("status", status);
        insertFlag = db.insert("user_interests",null, cv);
        return insertFlag;
    }

    public long deleteAllInterests(String userid){
        long deleteFlag=0;
        deleteFlag=db.delete("user_interests","userId='"+userid+"'" , null);
        //db.delete("SQLITE_SEQUENCE","NAME = ?",new String[]{"ItemTable"});
        return  deleteFlag;
    }

    public long insertPrefAdvt( int keyId, String orgId, String producer_id,String caption, String description, byte[] image, String startDate, String endDate,String contactName, String contactNumber, String emailId,String createdTime, String status){
        long insertFlag=0;
        ContentValues cv = new ContentValues();
        cv.put("advtRefNo", keyId);
        cv.put("orgId", orgId);
        cv.put("producer_id", producer_id);
        cv.put("caption", caption);
        cv.put("description", description);
        cv.put("image", image);
        cv.put("startDate", startDate);
        cv.put("endDate", endDate);
        cv.put("contactName", contactName);
        cv.put("contactNumber", contactNumber);
        cv.put("emailId", emailId);
        cv.put("createdTime", createdTime);
        cv.put("status", status);

        insertFlag = db.insert("advt_pref_producer",null, cv);
        return insertFlag;
    }


    public long updatePrefAdvt(String orgId, String producer_id,String caption, String description, byte[] image, String startDate, String endDate,String contactName, String contactNumber, String emailId,String createdTime, String status){
        long updateFlag=0;
        ContentValues cv = new ContentValues();
        cv.put("orgId", orgId);
        cv.put("producer_id", producer_id);
        cv.put("caption", caption);
        cv.put("description", description);
        cv.put("image", image);
        cv.put("startDate", startDate);
        cv.put("endDate", endDate);
        cv.put("contactName", contactName);
        cv.put("contactNumber", contactNumber);
        cv.put("emailId", emailId);
        cv.put("createdTime", createdTime);
        cv.put("status", status);
        updateFlag=db.update("advt_pref_producer", cv, "producer_id='"+producer_id+"'", null);
        return  updateFlag;
    }

    public long deleteAllPrefAdvts(){
        long deleteFlag=0;
        deleteFlag=db.delete("advt_pref_producer", null, null);
        //db.delete("SQLITE_SEQUENCE","NAME = ?",new String[]{"ItemTable"});
        return  deleteFlag;
    }

    public ArrayList<SingleAdvt> getPrefAdvtProducer(){
        ArrayList<SingleAdvt> PrefAdvtList = new ArrayList<SingleAdvt>();

        Cursor c =db.query("advt_pref_producer", new String[] {"advtId","orgId","producer_id","caption","description","image","startDate","endDate","contactName","contactNumber","emailId","createdTime","status"}, null, null, null, null, "createdTime DESC");
        while (c.moveToNext()) {

            PrefAdvtList.add(new SingleAdvt(c.getInt(c.getColumnIndex("advtId")),c.getString(c.getColumnIndex("orgId")),
                    c.getString(c.getColumnIndex("producer_id")),c.getString(c.getColumnIndex("caption")),
                    c.getString(c.getColumnIndex("description")),c.getString(c.getColumnIndex("fileType")),
                    c.getString(c.getColumnIndex("fileName")),c.getString(c.getColumnIndex("filePath")),
                    c.getString(c.getColumnIndex("startDate")),c.getString(c.getColumnIndex("endDate")),
                    c.getString(c.getColumnIndex("contactName")), c.getString(c.getColumnIndex("contactNumber")),
                    c.getString(c.getColumnIndex("emailId")), c.getString(c.getColumnIndex("createdTime")),
                    c.getString(c.getColumnIndex("status"))
            ));
        }
        return PrefAdvtList;
    }

    public long insertNewPreference(String orgId, String userId,String category, String subCategory, String createdBy, String createdDate,
                                    String modifiedBy,String modifiedDate, String status){
        long insertFlag=0;
        ContentValues cv = new ContentValues();
        cv.put("orgId", orgId);
        cv.put("userId", userId);
        cv.put("category", category);
        cv.put("subCategory", subCategory);
        cv.put("createdBy", createdBy);
        cv.put("createdDate", createdDate);
        cv.put("modifiedBy", modifiedBy);
        cv.put("modifiedDate", modifiedDate);
        cv.put("status", status);
        insertFlag = db.insert("preferences_table",null, cv);
        return insertFlag;
    }

    public boolean checkUserExists(String phno){
        boolean checkFlag = false;
        String countQuery = "select * from preferences_table where userId ='"+phno+"'";
        Cursor c = db.rawQuery(countQuery, null);
        if(c.getCount() == 0)
            checkFlag = false;
        else
            checkFlag = true;
        return checkFlag;
    }


    public long updatePreference(String orgId, String userId,String category, String subCategory, String createdBy, String createdDate,
                                 String modifiedBy,String modifiedDate, String status){
        long updateFlag=0;
        ContentValues cv = new ContentValues();
        cv.put("orgId", orgId);
        cv.put("userId", userId);
        cv.put("category", category);
        cv.put("subCategory", subCategory);
        cv.put("createdBy", createdBy);
        cv.put("createdDate", createdDate);
        cv.put("modifiedBy", modifiedBy);
        cv.put("modifiedDate", modifiedDate);
        cv.put("status", status);
        updateFlag=db.update("preferences_table", cv, "userId='"+userId+"'", null);
        return  updateFlag;
    }

    public int checkPreferencesExist(String userId) {
        int checkFlag=0;
        String countQuery = "select * from preferences_table where userId='"+userId+"'";
        Cursor c = db.rawQuery(countQuery, null);
        checkFlag=c.getCount();
        return checkFlag;
    }

    public  int checkPreferencesSelected(String subcatagory ) {
        int checkFlag=0;
        String countQuery = "select * from preferences_table where subCategory='"+subcatagory+"'";
        Cursor c = db.rawQuery(countQuery, null);
        checkFlag=c.getCount();
        return checkFlag;
    }

    public long deleteAllPreferences(String userid){
        long deleteFlag=0;
        deleteFlag=db.delete("preferences_table","userId='"+userid+"'", null);
        return  deleteFlag;
    }

    public ArrayList<SinglePreference> getAllPreferencesUser(String userId){
        ArrayList<SinglePreference> AdvtprefList = new ArrayList<SinglePreference>();

        Cursor c =db.query("preferences_table", new String[] {"keyId","orgId","userId","category","subCategory", "createdBy","createdDate","modifiedBy","modifiedDate","status"}, "userId='"+userId+"'", null, null, null, "keyId ASC");
        while (c.moveToNext()) {

            AdvtprefList.add(new SinglePreference(c.getInt(c.getColumnIndex("keyId")),c.getString(c.getColumnIndex("orgId")),
                    c.getString(c.getColumnIndex("userId")),c.getString(c.getColumnIndex("category")),
                    c.getString(c.getColumnIndex("subCategory")),c.getString(c.getColumnIndex("createdBy")),
                    c.getString(c.getColumnIndex("createdDate")),c.getString(c.getColumnIndex("modifiedBy")),
                    c.getString(c.getColumnIndex("modifiedDate")), c.getString(c.getColumnIndex("status"))
            ));
        }
        return AdvtprefList;
    }

    public ArrayList<SinglePreference> getAllPreferences() {
        ArrayList<SinglePreference> AdvtprefList = new ArrayList<SinglePreference>();

        Cursor c =db.query("preferences_table", new String[] {"keyId","orgId","userId","category","subCategory", "createdBy","createdDate","modifiedBy","modifiedDate","status"}, null, null, null, null, "keyId ASC");
        while (c.moveToNext()) {

            AdvtprefList.add(new SinglePreference(c.getInt(c.getColumnIndex("keyId")),c.getString(c.getColumnIndex("orgId")),
                    c.getString(c.getColumnIndex("userId")),c.getString(c.getColumnIndex("category")),
                    c.getString(c.getColumnIndex("subCategory")),c.getString(c.getColumnIndex("createdBy")),
                    c.getString(c.getColumnIndex("createdDate")),c.getString(c.getColumnIndex("modifiedBy")),
                    c.getString(c.getColumnIndex("modifiedDate")), c.getString(c.getColumnIndex("status"))
            ));
        }
        return AdvtprefList;
    }

    public long insertNewCategory(String orgId, String categoryId, String longName, String shortName, String createdBy, String createdDate, String modifiedBy, String modifiedDate, String status){
        long insertFlag=0;
        ContentValues cv = new ContentValues();
        cv.put("orgId", orgId);
        cv.put("categoryId", categoryId);
        cv.put("longName", longName);
        cv.put("shortName", shortName);
        cv.put("createdBy", createdBy);
        cv.put("createdDate", createdDate);
        cv.put("modifiedBy", modifiedBy);
        cv.put("modifiedDate", modifiedDate);
        cv.put("status", status);
        insertFlag = db.insert("category_table",null, cv);
        return insertFlag;
    }
    public long updateCategory(String phno, String otp, String status, String regDate){
        long updateFlag=0;
        ContentValues cv = new ContentValues();
        cv.put("phno", phno);
        cv.put("otp", otp);
        cv.put("status", status);
        cv.put("regDate", regDate);

        updateFlag=db.update("category_table", cv, "phno='"+phno+"'", null);
        return  updateFlag;
    }
    public long updateCategoryStatus(String devId, String locationCityName)
    {
        long updateFlag=0;
        ContentValues cv=new ContentValues();
        cv.put("locationCityName", locationCityName);
        updateFlag=db.update("category_table", cv, "devId='"+devId+"'", null);
        return  updateFlag;
    }

    public  int checkCategoryExists() {
        int checkFlag=0;
        String countQuery = "select * from category_table";
        Cursor c = db.rawQuery(countQuery, null);
        checkFlag=c.getCount();
        return checkFlag;
    }

    public long deleteAllCategories(){
        long deleteFlag=0;
        deleteFlag=db.delete("category_table", null, null);
        return  deleteFlag;
    }


    public ArrayList<SingleCategory> getAllCategories() {
        ArrayList<SingleCategory> CategoryList = new ArrayList<>();

        Cursor c =db.query("category_table", new String[] {"keyId","orgId","categoryId","longName","shortName","createdBy","createdDate","modifiedBy","modifiedDate","status"}, null, null, null, null, null);
        while (c.moveToNext()) {

            CategoryList.add(new SingleCategory(c.getInt(c.getColumnIndex("keyId")),c.getString(c.getColumnIndex("orgId")),
                    c.getString(c.getColumnIndex("categoryId")), c.getString(c.getColumnIndex("longName")),
                    c.getString(c.getColumnIndex("shortName")), c.getString(c.getColumnIndex("createdBy")),
                    c.getString(c.getColumnIndex("createdDate")), c.getString(c.getColumnIndex("modifiedBy")),
                    c.getString(c.getColumnIndex("modifiedDate")), c.getString(c.getColumnIndex("status"))
            ));
        }

        return CategoryList;
    }

    public long insertNewSubCategory(String orgId, String categoryId, String subCategoryId, String longName, String shortName, String createdBy, String createdDate, String modifiedBy, String modifiedDate, String status){
        long insertFlag=0;
        ContentValues cv = new ContentValues();
        cv.put("orgId", orgId);
        cv.put("categoryId", categoryId);
        cv.put("subCategoryId", subCategoryId);
        cv.put("longName", longName);
        cv.put("shortName", shortName);
        cv.put("createdBy", createdBy);
        cv.put("createdDate", createdDate);
        cv.put("modifiedBy", modifiedBy);
        cv.put("modifiedDate", modifiedDate);
        cv.put("status", status);
        insertFlag = db.insert("sub_category_table",null, cv);
        return insertFlag;
    }
    public long updateSubCategory(String phno, String otp, String status, String regDate){
        long updateFlag=0;
        ContentValues cv = new ContentValues();
        cv.put("phno", phno);
        cv.put("otp", otp);
        cv.put("status", status);
        cv.put("regDate", regDate);

        updateFlag=db.update("sub_category_table", cv, "phno='"+phno+"'", null);
        return  updateFlag;
    }
    public long updateSubCategoryStatus(String devId, String locationCityName)
    {
        long updateFlag=0;
        ContentValues cv=new ContentValues();
        cv.put("locationCityName", locationCityName);
        updateFlag=db.update("sub_category_table", cv, "devId='"+devId+"'", null);
        return  updateFlag;
    }

    public  int checkSubCategoryExists(String phno) {
        int checkFlag=0;
        String countQuery = "select * from sub_category_table where phno='"+phno+"'";
        Cursor c = db.rawQuery(countQuery, null);
        checkFlag=c.getCount();
        return checkFlag;
    }

    public long deleteAllSubCategories(){
        long deleteFlag=0;
        deleteFlag=db.delete("sub_category_table", null, null);
        return  deleteFlag;
    }


    public ArrayList<SingleSubCategory> getAllSubCategories() {
        ArrayList<SingleSubCategory> CategoryList = new ArrayList<>();

        Cursor c =db.query("sub_category_table", new String[] {"keyId","orgId","categoryId","subCategoryId","longName","shortName","createdBy","createdDate","modifiedBy","modifiedDate","status"}, null, null, null, null, "categoryId ASC,longName ASC");
        while (c.moveToNext()) {

            CategoryList.add(new SingleSubCategory(c.getInt(c.getColumnIndex("keyId")),c.getString(c.getColumnIndex("orgId")),
                    c.getString(c.getColumnIndex("categoryId")), c.getString(c.getColumnIndex("subCategoryId")),
                    c.getString(c.getColumnIndex("longName")),c.getString(c.getColumnIndex("shortName")), c.getString(c.getColumnIndex("createdBy")),
                    c.getString(c.getColumnIndex("createdDate")), c.getString(c.getColumnIndex("modifiedBy")),
                    c.getString(c.getColumnIndex("modifiedDate")), c.getString(c.getColumnIndex("status")),null,false
            ));
        }
//        c.getString(c.getColumnIndex("longName")), c.getString(c.getColumnIndex("shortName"))
        return CategoryList;
    }

    public long insertCategoryCheck(String category, String subCategory, String status){
        long insertFlag=0;
        ContentValues cv = new ContentValues();
        cv.put("category", category);
        cv.put("subCategory", subCategory);
        cv.put("status", status);
        insertFlag = db.insert("category_check_table",null, cv);
        return insertFlag;
    }
    public long updateCategoryCheck(String status1, String status2){
        long updateFlag=0;
        ContentValues cv = new ContentValues();
        cv.put("status", status1);
        updateFlag=db.update("category_check_table", cv, "status='"+status2+"'", null);
        return  updateFlag;
    }

    public long deleteCategoryCheck(String subCategory){
        long deleteFlag=0;
        deleteFlag=db.delete("category_check_table", "subCategory='"+subCategory+"'", null);
        return  deleteFlag;
    }


    public ArrayList<CategoryCheck> getCategoriesChecked(String status) {
        ArrayList<CategoryCheck> CategoryCheckedList = new ArrayList<CategoryCheck>();

        Cursor c =db.query("category_check_table", new String[] {"keyId","category","subCategory","status"}, "status='"+status+"'", null, null, null, "keyId ASC");
        while (c.moveToNext()) {

            CategoryCheckedList.add(new CategoryCheck(c.getInt(c.getColumnIndex("keyId")), c.getString(c.getColumnIndex("category")),
                    c.getString(c.getColumnIndex("subCategory")),c.getString(c.getColumnIndex("status"))
            ));
        }

        return CategoryCheckedList;
    }


    public long insertAdvtCategory(String orgId, String advtId, String category, String subCategory){
        long insertFlag=0;
        ContentValues cv = new ContentValues();
        cv.put("orgId", orgId);
        cv.put("advtId", advtId);
        cv.put("category", category);
        cv.put("subCategory", subCategory);
        insertFlag = db.insert("advt_category_table",null, cv);
        return insertFlag;
    }
    public long updateAdvtCategory(String orgId, String advtId, String category, String subCategory){
        long updateFlag=0;
        ContentValues cv = new ContentValues();
        cv.put("orgId", orgId);
        cv.put("advtId", advtId);
        cv.put("category", category);
        cv.put("subCategory", subCategory);

        updateFlag=db.update("advt_category_table", cv, "subCategory='"+subCategory+"'", null);
        return  updateFlag;
    }
//    public long updateCategoryStatus(String devId, String locationCityName)
//    {
//        long updateFlag=0;
//        ContentValues cv=new ContentValues();
//        cv.put("locationCityName", locationCityName);
//        updateFlag=db.update("category_table", cv, "devId='"+devId+"'", null);
//        return  updateFlag;
//    }

//    public  int checkCategoryExists(String phno) {
//        int checkFlag=0;
//        String countQuery = "select * from category_table where phno='"+phno+"'";
//        Cursor c = db.rawQuery(countQuery, null);
//        checkFlag=c.getCount();
//        return checkFlag;
//    }

    public long deleteAdvtCategory(String subCategory){
        long deleteFlag=0;
        deleteFlag=db.delete("advt_category_table", "subCategory='"+subCategory+"'", null);
        return  deleteFlag;
    }


    public ArrayList<SingleAdvtCategory> getAllAdvtCategories() {
        ArrayList<SingleAdvtCategory> AdvtCategoryList = new ArrayList<SingleAdvtCategory>();

        Cursor c =db.query("advt_category_table", new String[] {"keyId","orgId","advtId","category","subCategory"}, null, null, null, null, "keyId ASC");
        while (c.moveToNext()) {

            AdvtCategoryList.add(new SingleAdvtCategory(c.getInt(c.getColumnIndex("keyId")),c.getString(c.getColumnIndex("orgId")),
                    c.getString(c.getColumnIndex("advtId")), c.getString(c.getColumnIndex("category")),
                    c.getString(c.getColumnIndex("subCategory"))
            ));
        }

        return AdvtCategoryList;
    }
    public ArrayList<String> getActivePref(String status,String mobileno){
        ArrayList<String> AdvtprefList = new ArrayList<>();

        Cursor c =db.query("preferences_table", new String[] {"subCategory"},"status='"+status+"' and userId='"+mobileno+"'", null, null, null,null);
        while (c.moveToNext()) {

            AdvtprefList.add(c.getString(c.getColumnIndex("subCategory")));
        }
        return AdvtprefList;
    }


    public long getActivePrefCount(String status,String mobileno){
        long count=0;
        String countQuery = "select * from preferences_table where userId='"+mobileno+"' and status='"+status+"'";
        Cursor c = db.rawQuery(countQuery, null);
        count=c.getCount();
        return count;
    }

    public ArrayList<String> getAllPref(String mobileno){
        ArrayList<String> AdvtprefList = new ArrayList<>();

        Cursor c =db.query("preferences_table", new String[] {"subCategory"},"userId='"+mobileno+"'", null, null, null,null);
        while (c.moveToNext()) {

            AdvtprefList.add(c.getString(c.getColumnIndex("subCategory")));
        }
        return AdvtprefList;
    }

    public SingleProducer getProducer(String MobileNo){
        SingleProducer user =null;

        Cursor c =db.query("producer_table", new String[] { "name", "address" , "city" , "state" , "contact_Person" , "phno" , "emailId" ,
                "otp" , "regDate", "status" }, "phno='"+MobileNo+"'", null, null, null, "regDate DESC");
        while (c.moveToNext()) {

            user=new SingleProducer(c.getString(c.getColumnIndex("name")),c.getString(c.getColumnIndex("address")),
                    c.getString(c.getColumnIndex("city")),c.getString(c.getColumnIndex("state")),
                    c.getString(c.getColumnIndex("contact_Person")),c.getString(c.getColumnIndex("phno")),
                    c.getString(c.getColumnIndex("emailId")),c.getString(c.getColumnIndex("otp")),
                    c.getString(c.getColumnIndex("regDate")), c.getString(c.getColumnIndex("status")));
        }
        return user;
    }


    public long insertPref(String orgId, String userId,String category,String subcategory,String status){
        long insertFlag=0;
        ContentValues cv = new ContentValues();
        cv.put("orgId", orgId);
        cv.put("userId",userId);
        cv.put("category",category);
        cv.put("subCategory",subcategory);
        cv.put("status", status);
        insertFlag = db.insert("preferences_table",null, cv);
        return insertFlag;
    }

    public long updatePref(String userId,String subcategory,String status){
        long updateFlag=0;
        ContentValues cv=new ContentValues();
        cv.put("status",status);
        updateFlag=db.update("preferences_table",cv,"userId='"+userId+"' and subCategory='"+subcategory+"'", null);
        return  updateFlag;
    }
}
