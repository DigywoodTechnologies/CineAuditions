package com.digywood.cineauditions.AsyncTasks;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.digywood.cineauditions.IBagroundListener;
import com.digywood.cineauditions.URLClass;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;


public class AdvtBagroundTask extends AsyncTask<Void, String, String> {
    private ProgressDialog dialog;
    String urlAddress;
    HashMap<String, String> hmap;
    HttpPost httpPost;
    HttpResponse response = null;
    String status,userid,path;
    String resultString=null,fileName="",fileUrl="";
    IBagroundListener listener;


    public AdvtBagroundTask(String url,HashMap<String, String> hmap1,String userId,String path,String fileName,Context activity, IBagroundListener iListener) {
        dialog = new ProgressDialog(activity);
        dialog.setCanceledOnTouchOutside(false);
        this.urlAddress=url;
        this.hmap=hmap1;
        this.path=path;
        this.userid=userId;
        this.fileName=fileName;
        this.listener=iListener;

    }

    @Override
    protected void onPreExecute() {
        dialog.setMessage("Loading, please wait.");
        dialog.show();
        dialog.setCancelable(false);
    }

    @Override
    protected void onPostExecute(String result) {

        if ((dialog !=null) && (dialog.isShowing())) {
            dialog.dismiss();
            try {
                listener.bagroundData(result);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected String doInBackground(Void... params) {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        status = "Connecting to server..";
        publishProgress(status);
        
        try {

            if(path!=null){
                int upload_res=uploadFile(path,userid,hmap);
                if(upload_res==200){
                    Log.e("AdvtBackgroundTask---","image uploaded");
                }else{
                    Log.e("AdvtBackgroundTask---","image not uploaded");
                }

            }else {

            }

            HttpClient httpClient = new DefaultHttpClient();
            httpPost = new HttpPost(urlAddress);
            List<? super NameValuePair> nvps = new ArrayList<>();

		/* Adding Arguments to List Of Name value Pairs  */
            Set<Map.Entry<String, String>> set = hmap.entrySet();
            Iterator<Map.Entry<String, String>> iterator = set.iterator();
            while (iterator.hasNext()) {
                @SuppressWarnings("rawtypes")
                Map.Entry mentry = (Map.Entry) iterator.next();
                nvps.add(new BasicNameValuePair(mentry.getKey().toString(),mentry.getValue().toString()));
                Log.d("JSON_SERVICE", "result-nvps..."+nvps);
            }

            try {
                httpPost.setEntity(new UrlEncodedFormEntity((List<? extends NameValuePair>) nvps));
                response = httpClient.execute(httpPost);
                int responCode = response.getStatusLine().getStatusCode();
                status = getHttpStatusDescription(responCode);
                publishProgress(status);

                HttpEntity entity = response.getEntity();
                InputStream is = entity.getContent();
                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(is, "UTF-8"), 8);
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {

                    sb.append(line);

                    status = "Loading the data..";
                    publishProgress(status);
                }

                System.out.println("JSONArray--" + sb.toString());
                is.close();
                resultString = sb.toString();

                if(!resultString.equalsIgnoreCase("Not Inserted")){
                    moveFile(path,URLClass.myadspath);
                }

            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();

            } catch (ClientProtocolException e) {
                e.printStackTrace();

            } catch (IOException e) {
                e.printStackTrace();

            }
            finally {
                HttpEntity entity  = response.getEntity();
                try {
                    if (entity != null) {
                        httpPost.abort();
                    }
                }
                catch(NullPointerException e){
                    e.printStackTrace();
                }
            }

        }  catch (Exception e) {
            e.printStackTrace();

        }

        return resultString;
    }

    @Override
    protected void onProgressUpdate(String... values) {
        super.onProgressUpdate(values);
        dialog.setMessage(values[0]);

    }

    public static String getHttpStatusDescription(int statusCode) {
        switch (statusCode) {
            case HttpStatus.SC_ACCEPTED:
                return "Accepted";
            case HttpStatus.SC_BAD_GATEWAY:
                return "Bad Gateway";
            case HttpStatus.SC_BAD_REQUEST:
                return "Bad Request";
            case HttpStatus.SC_CONFLICT:
                return "Conflict";
            case HttpStatus.SC_CONTINUE:
                return "Continue";
            case HttpStatus.SC_CREATED:
                return "Created";
            case HttpStatus.SC_EXPECTATION_FAILED:
                return "Expectation failed";
            case HttpStatus.SC_FAILED_DEPENDENCY:
                return "Failed dependency";
            case HttpStatus.SC_FORBIDDEN:
                return "Forbidden";
            case HttpStatus.SC_GATEWAY_TIMEOUT:
                return "Gateway timeout";
            case HttpStatus.SC_GONE:
                return "Gone";
            case HttpStatus.SC_HTTP_VERSION_NOT_SUPPORTED:
                return "HTTP version not supported";
            case HttpStatus.SC_INSUFFICIENT_SPACE_ON_RESOURCE:
                return "Insufficient space on resource";
            case HttpStatus.SC_INSUFFICIENT_STORAGE:
                return "Insufficient storage";
            case HttpStatus.SC_INTERNAL_SERVER_ERROR:
                return "Internal server error";
            case HttpStatus.SC_LENGTH_REQUIRED:
                return "Length required";
            case HttpStatus.SC_LOCKED:
                return "Locked";
            case HttpStatus.SC_METHOD_FAILURE:
                return "Method failure";
            case HttpStatus.SC_METHOD_NOT_ALLOWED:
                return "Method not allowed";
            case HttpStatus.SC_MOVED_PERMANENTLY:
                return "Moved permanently";
            case HttpStatus.SC_MOVED_TEMPORARILY:
                return "Moved temporarily";
            case HttpStatus.SC_MULTI_STATUS:
                return "Multi status";
            case HttpStatus.SC_MULTIPLE_CHOICES:
                return "Multiple choices";
            case HttpStatus.SC_NO_CONTENT:
                return "No content";
            case HttpStatus.SC_NON_AUTHORITATIVE_INFORMATION:
                return "Non authoritive information";
            case HttpStatus.SC_NOT_ACCEPTABLE:
                return "Not acceptable";
            case HttpStatus.SC_NOT_FOUND:
                return "Not found";
            case HttpStatus.SC_NOT_IMPLEMENTED:
                return "Not implemented";
            case HttpStatus.SC_NOT_MODIFIED:
                return "Not modified";
            case HttpStatus.SC_OK:
                return "Connected";
            case HttpStatus.SC_PARTIAL_CONTENT:
                return "Partial content";
            case HttpStatus.SC_PAYMENT_REQUIRED:
                return "Payment required";
            case HttpStatus.SC_PRECONDITION_FAILED:
                return "Precondition failed";
            case HttpStatus.SC_PROCESSING:
                return "Procecssing";
            case HttpStatus.SC_PROXY_AUTHENTICATION_REQUIRED:
                return "Proxy authentication required";
            case HttpStatus.SC_REQUEST_TIMEOUT:
                return "Request timeout";
            case HttpStatus.SC_REQUEST_TOO_LONG:
                return "Request too long";
            case HttpStatus.SC_REQUEST_URI_TOO_LONG:
                return "Request URI too long";
            case HttpStatus.SC_REQUESTED_RANGE_NOT_SATISFIABLE:
                return "Requested range not satisfiable";
            case HttpStatus.SC_RESET_CONTENT:
                return "Reset content";
            case HttpStatus.SC_SEE_OTHER:
                return "See other";
            case HttpStatus.SC_SERVICE_UNAVAILABLE:
                return "Service unavailable";
            case HttpStatus.SC_SWITCHING_PROTOCOLS:
                return "Switching protocols";
            case HttpStatus.SC_TEMPORARY_REDIRECT:
                return "Temporary redirect";
            case HttpStatus.SC_UNAUTHORIZED:
                return "Unauthorized";
            case HttpStatus.SC_UNPROCESSABLE_ENTITY:
                return "Unprocessable entity";
            case HttpStatus.SC_UNSUPPORTED_MEDIA_TYPE:
                return "Unsupported media type";
            case HttpStatus.SC_USE_PROXY:
                return "Use proxy";
            default:
                return "[status code: " + statusCode + "]";
        }
    }

    public int uploadFile(String selectedFilePath,String userId,HashMap<String,String> hmap) throws JSONException {

        int serverResponseCode = 0;

        HttpURLConnection connection;
        DataOutputStream dataOutputStream;
        String lineEnd = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";

        int bytesRead,bytesAvailable,bufferSize;
        byte[] buffer;
        int maxBufferSize = 500 * 1024 * 1024;
        File selectedFile = new File(selectedFilePath);

//        String[] parts = selectedFilePath.getAbsolutePath().split("/");
//        final String fileName = parts[parts.length-1];

        try{
            FileInputStream fileInputStream = new FileInputStream(selectedFile);
            URL url = new URL(URLClass.hosturl+"uploadfile.php");
            connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);//Allow Inputs
            connection.setDoOutput(true);//Allow Outputs
            connection.setUseCaches(false);//Don't use a cached Copy
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Connection", "Keep-Alive");
            connection.setRequestProperty("ENCTYPE", "multipart/form-data");
            connection.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
            connection.setRequestProperty("uploaded_file",selectedFilePath);
//            connection.setRequestProperty("orgId",hmap.get("orgId"));
//            connection.setRequestProperty("userId",hmap.get("userId"));
//            connection.setRequestProperty("caption",hmap.get("caption"));
//            connection.setRequestProperty("description",hmap.get("description"));
//            connection.setRequestProperty("fileType",hmap.get("fileType"));
//            connection.setRequestProperty("fileName",hmap.get("fileName"));
//            connection.setRequestProperty("filePath",hmap.get("filePath"));
//            connection.setRequestProperty("startDate",hmap.get("startDate"));
//            connection.setRequestProperty("endDate",hmap.get("endDate"));
//            connection.setRequestProperty("contactName",hmap.get("contactName"));
//            connection.setRequestProperty("contactNumber",hmap.get("contactNumber"));
//            connection.setRequestProperty("emailId",hmap.get("emailId"));
//            connection.setRequestProperty("createdTime",hmap.get("createdTime"));
//            connection.setRequestProperty("status","created");

//            fileName=userId+"_"+currentDateandTime +hmap.get("fileType");
//            fileUrl=URLClass.imageurl+userId+"_"+currentDateandTime +hmap.get("fileType");
//            connection.setRequestProperty("fileName",userId+"_"+currentDateandTime +hmap.get("fileType"));
//            connection.setRequestProperty("filePath","http://www.jcbpoint.com/cinesooruAuditions/post_resources/"+userId+"_"+currentDateandTime +hmap.get("fileType"));
            //connection.setRequestProperty("uploaded_file_name",deviceId+"_log_"+currentDateandTime);

            //creating new dataoutputstream
            dataOutputStream = new DataOutputStream(connection.getOutputStream());

            //writing bytes to data outputstream

            dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);
            dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"devId\""+lineEnd);
            dataOutputStream.writeBytes(lineEnd);
            dataOutputStream.writeBytes(lineEnd);


            dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);
            dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"uploaded_file\";filename=\""
                    +fileName+ "\"" + lineEnd);

            dataOutputStream.writeBytes(lineEnd);

            //returns no. of bytes present in fileInputStream
            bytesAvailable = fileInputStream.available();
            //selecting the buffer size as minimum of available bytes or 500 MB
            bufferSize = Math.min(bytesAvailable,maxBufferSize);
            //setting the buffer as byte array of size of bufferSize
            buffer = new byte[bufferSize];

            //reads bytes from FileInputStream(from 0th index of buffer to buffersize)
            bytesRead = fileInputStream.read(buffer,0,bufferSize);

            //loop repeats till bytesRead = -1, i.e., no bytes are left to read
            while (bytesRead > 0){
                //write the bytes read from inputstream
                dataOutputStream.write(buffer,0,bufferSize);
                bytesAvailable = fileInputStream.available();
                bufferSize = Math.min(bytesAvailable,maxBufferSize);
                bytesRead = fileInputStream.read(buffer,0,bufferSize);
            }

            dataOutputStream.writeBytes(lineEnd);
            dataOutputStream.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

            serverResponseCode = connection.getResponseCode();
            InputStream io=(InputStream) connection.getContent();
            String serverResponseMessage = connection.getResponseMessage();
            Log.e("AdvtBagroundTask","Server Response is: " + serverResponseMessage + "; Responce_Code:" + serverResponseCode);
            //response code of 200 indicates the server status OK
            if(serverResponseCode == 200){
                Log.e("AdvtBagroundTask","upload success...."+"; upload file path :");
                Log.e("AdvtBagroundTask","upload success...."+io.toString());
            }

            //closing the input and output streams
            fileInputStream.close();
            dataOutputStream.flush();
            dataOutputStream.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Log.e("ImageUpload---",e.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
            Log.e("ImageUpload---",e.toString());
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("ImageUpload---",e.toString());
        }

        return serverResponseCode;
    }

    private void moveFile(String inputPath,String outputPath) {

        InputStream in = null;
        OutputStream out = null;
        try {
            //create output directory if it doesn't exist
            File dir = new File(outputPath);
            if (!dir.exists()) {
                dir.mkdirs();
            }

            in = new FileInputStream(inputPath);
            out = new FileOutputStream(outputPath+fileName);

            byte[] buffer = new byte[1024 * 1024];
            int read;
            long total = 0;
            while ((read = in.read(buffer)) != -1) {
                total += read;
                out.write(buffer, 0, read);
            }
            in.close();
            in = null;

            // write the output file
            out.flush();
            out.close();
            out = null;

        } catch (FileNotFoundException fnfe1) {
            Log.e("AdvtInfoScreen---",fnfe1.toString());

        } catch (Exception e) {
            Log.e("AdvtInfoScreen---",e.toString());
        } finally {
            try {
                if (out != null)
                    out.close();
                if (in != null)
                    in.close();
            } catch (IOException e) {
                Log.e("AdvtInfoScreen---",e.toString());
            }
        }

    }

}
