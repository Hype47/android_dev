package com.example.eventby3;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpResponse;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

public class UserSignInActivity extends AppCompatActivity {

    // Variable declaration
    public static String jwtToken;
    public static String loginName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_sign_in);

        // Get Access to the fields
        final EditText editEmail = findViewById(R.id.editSignInEmail);
        final EditText editPassword = findViewById(R.id.editSignInPassword);

        // Cancel Button
        Button buttonCancel = findViewById(R.id.buttonCancelSignIn);
        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        // To User registration
        Button buttonToRegistration = findViewById(R.id.buttonToRegistration);
        buttonToRegistration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Context context = view.getContext();
                Intent intent = new Intent(context, UserRegistrationActivity.class);
                context.startActivity(intent);
            }
        });

        // Login Button
        Button buttonLogIn = findViewById(R.id.buttonLogIn);
        buttonLogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {

                try {
                    RequestQueue requestQueue = Volley.newRequestQueue(UserSignInActivity.this);
                    String URL = "https://news-geocode.herokuapp.com/auth";
                    JSONObject jsonBody = new JSONObject();
                    jsonBody.put("username", editEmail.getText().toString());
                    jsonBody.put("password", editPassword.getText().toString());
                    final String requestBody = jsonBody.toString();

                    StringRequest stringRequest = new StringRequest(com.android.volley.Request.Method.POST,URL, new com.android.volley.Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.i("VOLLEY", response);
                            try {
                                JSONObject jsonObject = new JSONObject(response);

                                // Storing credentials
                                jwtToken = jsonObject.getString("access_token");
                                loginName = editEmail.getText().toString();

                                // System messages
                                Toast.makeText(UserSignInActivity.this,"Log in successful...",Toast.LENGTH_LONG).show();
                                Log.i("Log in Successful", loginName + "_" + jwtToken );

                                Context context = view.getContext();
                                Intent intent = new Intent(context, MainActivity.class);
                                context.startActivity(intent);

                            } catch (Exception e){
                                e.printStackTrace();
                            }
                        }
                    }, new com.android.volley.Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.e("VOLLEY", error.toString());
                            Toast.makeText(UserSignInActivity.this, "Invalid username/password...",Toast.LENGTH_LONG).show();
                        }
                    }) {
                        @Override
                        public String getBodyContentType() {
                            return "application/json; charset=utf-8";
                        }

                        @Override
                        public byte[] getBody() throws AuthFailureError {
                            try {
                                return requestBody == null ? null : requestBody.getBytes("utf-8");
                            } catch (UnsupportedEncodingException uee) {
                                VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", requestBody, "utf-8");
                                return null;
                            }
                        }

//                        @Override
//                        protected Response<String> parseNetworkResponse(NetworkResponse response) {
//                            String responseString = "";
//                            if (response != null) {
//                                responseString = String.valueOf(response.statusCode);
//                                // can get more details such as response.headers
//                            }
//                            return Response.success(responseString, HttpHeaderParser.parseCacheHeaders(response));
//                        }
                    };

                    requestQueue.add(stringRequest);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });

    }

}
