package com.kawal.cablebillmanagement;

import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.app.ProgressDialog;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;


public class AdminRegistration extends AppCompatActivity implements View.OnClickListener {

    @InjectView(R.id.input_name)
    EditText _nameText;
    @InjectView(R.id.input_address)
    EditText _addressText;
    @InjectView(R.id.input_email)
    EditText _emailText;
    @InjectView(R.id.input_mobile)
    EditText _mobileText;
    @InjectView(R.id.input_password)
    EditText _passwordText;

    @InjectView(R.id.btn_signup)
    Button _signupButton;
    @InjectView(R.id.link_login)
    TextView _loginLink;
    UserBean uBean, rcvUser;
    RequestQueue requestQueue;
    ProgressDialog progressDialog;
    boolean updateMode;
    ConnectivityManager connectivityManager;
    NetworkInfo networkInfo;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_registration);
        ButterKnife.inject(this);
        preferences = getSharedPreferences(Util.PREFS_NAME, MODE_PRIVATE);
        editor = preferences.edit();

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait....");
        progressDialog.setCancelable(false);

        _signupButton.setOnClickListener(this);
        _loginLink.setOnClickListener(this);
        uBean = new UserBean();
        //String name="admin";

        Intent rcv = getIntent();
        updateMode = rcv.hasExtra("keyUser");

       /* _signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminRegistration.this, AdminHomeActivity.class);
                startActivity(intent);

            }
        });*/
        requestQueue = Volley.newRequestQueue(this);


        _loginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), AdminLogin.class);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            }
        });
        
        if (updateMode) {
            rcvUser = (UserBean) rcv.getSerializableExtra("keyUser");
            _nameText.setText(rcvUser.getuName());
            _mobileText.setText(rcvUser.getuPhone());
            _emailText.setText(rcvUser.getuEmail());
            _passwordText.setText(rcvUser.getuPassword());
            _addressText.setText(rcvUser.getuAddress());
            _signupButton.setText("Update");

        }
    }
//    boolean isNetWorkConnected(){
//
//        connectivityManager = (ConnectivityManager)getSystemService(CONNECTIVITY_SERVICE);
//        networkInfo = connectivityManager.getActiveNetworkInfo();
//
//
//        return (networkInfo!=null && networkInfo.isConnected());
//
//    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_signup) {

            insertAdmin();

            if (validateFields()){

              //  if (isNetWorkConnected()){

                    insertIntoCloud();
                }
//                else
//                    Toast.makeText(this, "Please connect to Internet", Toast.LENGTH_LONG).show();
            }else {
                Toast.makeText(this, "Please insert correct Input", Toast.LENGTH_LONG).show();
            }
        }



    void insertAdmin() {
        uBean.setUserType(0);
        uBean.setuName(_nameText.getText().toString().trim());
        uBean.setuPhone(_mobileText.getText().toString().trim());
        uBean.setuEmail(_emailText.getText().toString().trim());
        uBean.setuPassword(_passwordText.getText().toString().trim());
        uBean.setuAddress(_addressText.getText().toString().trim());
       // insertIntoCloud();

        }

    public void insertIntoCloud() {

        Log.i("TEST", uBean.toString());

        String url = "";
        if (!updateMode) {
            url = Util.INSERT_USER_PHP;

        } else {
            Log.e("user", rcvUser.toString());
            url = Util.UPDATE_USER_PHP;
        }
        progressDialog.show();
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("TEST", response);
                progressDialog.show();

//                Toast.makeText(AdminRegistration.this, "Success", Toast.LENGTH_LONG).show();
//                Intent intent = new Intent(AdminRegistration.this, AdminHomeActivity.class);
//                startActivity(intent);
//

                try {
                    JSONObject jsonObject = new JSONObject(response);

                    int success = jsonObject.getInt("success");
                    String message = jsonObject.getString("message");

                    if (success == 1) {
                        Toast.makeText(AdminRegistration.this, message, Toast.LENGTH_SHORT).show();

                        if (!updateMode) {
                            editor.putString(Util.KEY_NAME, uBean.getuName());
                            editor.putString(Util.KEY_PHONE, uBean.getuPhone());
                            editor.putString(Util.KEY_EMAIL, uBean.getuEmail());
                            editor.putString(Util.KEY_PASSWORD, uBean.getuPassword());
                            editor.putString(Util.KEY_ADDRESS, uBean.getuAddress());

                            editor.commit();

                            Intent home = new Intent(AdminRegistration.this, AdminHomeActivity.class);
                            startActivity(home);
                            finish();
                        }
                        if (updateMode)
                            finish();
                    } else {
                        Toast.makeText(AdminRegistration.this, message, Toast.LENGTH_SHORT).show();
                    }
                    progressDialog.dismiss();
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(AdminRegistration.this, "Some Exception", Toast.LENGTH_SHORT).show();
                }
            }


        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                      progressDialog.dismiss();
                Toast.makeText(AdminRegistration.this, "Some Error" + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();

                if (updateMode)
                    map.put("id", String.valueOf(rcvUser.getId()));
                map.put("uName", uBean.getuName());
                map.put("uPhone", uBean.getuPhone());
                map.put("uEmail", uBean.getuEmail());
                map.put("uPassword", uBean.getuPassword());
                map.put("uAddress", uBean.getuAddress());
                map.put("userType", String.valueOf(uBean.getUserType()));

                return map;
            }
        };
        requestQueue.add(request);
//        clearFields();
    }
    boolean validateFields() {
        boolean flag = true;


        if (uBean.getuName().isEmpty()) {
            _nameText.setError("Please Enter Name");
            flag = false;
        }
        if (uBean.getuPhone().isEmpty()) {


            _mobileText.setError("Please Enter Phone");
            flag = false;
        }
            if (uBean.getuPhone().length() < 10) {


                _mobileText.setError("Please Enter 10 digits phone number");
                flag = false;
            }


        if(uBean.getuEmail().isEmpty()) {

            _emailText.setError("Please Enter Email");
            flag = false;
        }
            if (!uBean.getuEmail().contains("@") && uBean.getuEmail().contains(".")) {


                _emailText.setError("Please Enter Correct Email");
                flag = false;

        }
        if(uBean.getuPassword().isEmpty()) {

            _passwordText.setError("Please Enter Password");
            flag = false;
        }


        if(uBean.getuAddress().isEmpty()) {

            _addressText.setError("Please Enter Address");
            flag = false;
        }

        return flag;
    }
    void clearFields() {
        _nameText.setText("");
        _mobileText.setText("");
        _emailText.setText("");
        _passwordText.setText("");
        _addressText.setText("");

    }


    }
