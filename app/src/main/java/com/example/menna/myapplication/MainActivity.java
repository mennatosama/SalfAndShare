package com.example.menna.myapplication;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;

public class MainActivity extends AppCompatActivity {


    ImageView postImg;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        postImg = (ImageView) findViewById(R.id.postImg);





    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    public void ShowPostDetails(View view) {
        RequestParams params = new RequestParams();

        String postId = "6";

        if (Utility.isNotNull(postId)) {

            // Put Http parameter post id with value of post Edit View control
            params.put("postId", postId);
            // Invoke RESTful Web Service with Http parameters
            invokeWS(params);

        } else {
            Toast.makeText(getApplicationContext(), "Please fill the form, don't leave any field blank", Toast.LENGTH_LONG).show();
        }
    }


    public void invokeWS(RequestParams params) {
        // Show Progress Dialog
        // prgDialog.show();
        // Make RESTful webservice call using AsyncHttpClient object
        AsyncHttpClient client = new AsyncHttpClient();
        //172.16.2.26 port 8087
        client.get("http://172.16.1.181:8084/SlafAndShare/rest/postDetails/showPostDetails", params, new AsyncHttpResponseHandler() {
            // When the response returned by REST has Http response code '200'
            @Override
            public void onSuccess(String response) {
                // Hide Progress Dialog
                //  prgDialog.hide();
                try {
                    // JSON Object
                    JSONObject obj = new JSONObject(response);

                    // When the JSON response has status boolean value assigned with true
                    Log.i("testJson", obj.toString());
                    if (obj.length() != 0) {

                        Toast.makeText(getApplicationContext(), "Done!", Toast.LENGTH_LONG).show();

                        JSONObject dataObj = obj.getJSONObject("postDetails");

                        int id = dataObj.getInt("id");
                        String title = dataObj.getString("title");
                        String desc = dataObj.getString("desc");
                        String region = dataObj.getString("region");
                        String city = dataObj.getString("city");
                        String userName = dataObj.getString("userName");
                        String userEmail = dataObj.getString("userEmail");
                        String phone = dataObj.getString("userPhone");
                        String PostDate = dataObj.get("date").toString();
                        String catg = dataObj.getString("category");
                        String type = dataObj.getString("type");
                        Double price = dataObj.getDouble("price");
                        Double guranteeFees = dataObj.getDouble("guaranteeFees");

                        JSONArray imgArr = dataObj.getJSONArray("images");
                        String [] imgUrl = new String[5];
                        for (int i = 0 ; i < imgArr.length() ; i++)
                        {
                            imgUrl[i] = imgArr.getString(i);
                        }




                        //  Intent i = new  Intent(getContext(),report.class);
                        // startActivity(i);
                        // Navigate to Home screen
                        // navigatetoHomeActivity();
                    }
                    // Else display error message
                    else {

                        Toast.makeText(getApplicationContext(), "NO Data Found!", Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    // TODO Auto-generated catch block

                    Toast.makeText(getApplicationContext(), "Server's JSON response might be invalid!", Toast.LENGTH_LONG).show();
                    e.printStackTrace();

                }
            }

            // When the response returned by REST has Http response code other than '200'
            @Override
            public void onFailure(int statusCode, Throwable error,
                                  String content) {
                // Hide Progress Dialog
                   //prgDialog.hide();
                // When Http response code is '404'
                if (statusCode == 404) {
                    Toast.makeText(getApplicationContext(), "Requested resource not found", Toast.LENGTH_LONG).show();
                }
                // When Http response code is '500'
                else if (statusCode == 500) {
                    Toast.makeText(getApplicationContext(), "Something went wrong at server", Toast.LENGTH_LONG).show();
                }
                // When Http response code other than 404, 500
                else {
                    Toast.makeText(getApplicationContext(), "Device might not be connected to Internet", Toast.LENGTH_LONG).show();
                }
            }
        });

    }


    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }

}