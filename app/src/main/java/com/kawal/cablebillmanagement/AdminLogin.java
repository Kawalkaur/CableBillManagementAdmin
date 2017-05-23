package com.kawal.cablebillmanagement;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import android.content.Intent;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class AdminLogin extends AppCompatActivity implements View.OnClickListener {
//    private static final String TAG = "LoginActivity";
//    private static final int REQUEST_SIGNUP = 0;

    EditText emailText;
    EditText passwordText;
    Button loginButton;
    TextView signupLink;
    UserBean userBean;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    ProgressDialog progressDialog;
    RequestQueue requestQueue;
    String login_phone,login_password;




    void views() {
        emailText = (EditText) findViewById(R.id.input_email);
        passwordText = (EditText) findViewById(R.id.input_password);
        loginButton = (Button) findViewById(R.id.btn_login);
        loginButton.setOnClickListener(this);
        signupLink = (TextView) findViewById(R.id.link_signup);
        signupLink.setOnClickListener(this);
        requestQueue = Volley.newRequestQueue(this);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Signing in....");
        progressDialog.setCancelable(false);
        userBean = new UserBean();
        preferences = getSharedPreferences(Util.PREFS_NAME, MODE_PRIVATE);
        editor = preferences.edit();

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_login);
        views();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.link_signup) {
            Intent i = new Intent(this, AdminRegistration.class);
            startActivity(i);
            finish();
        } else if (id == R.id.btn_login) {
            login_phone = emailText.getText().toString().trim();
            login_password = passwordText.getText().toString().trim();

            login();

        }
    }
    void login(){
//        final String token= FirebaseInstanceId.getInstance().getToken();
//        Log.e("token ",token);
        progressDialog.show();
        StringRequest stringRequest=new StringRequest(Request.Method.POST, Util.LOGIN_PHP, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try{
                    JSONObject object=new JSONObject(response);
                    JSONArray jsonArray = object.getJSONArray("user");
                    int id = 0, u = 0;
                    String n = "", m = "", e = "", p = "", a = "";
                    for(int j=0;j<jsonArray.length();j++) {
                        JSONObject jObj = jsonArray.getJSONObject(j);
                        id = jObj.getInt("ID");
//These are coloumn name in database table
                        id = jObj.getInt("id");
                        n = jObj.getString("uName");
                        m = jObj.getString("uPhone");
                        e = jObj.getString("uEmail");
                        p = jObj.getString("uPassword");
                        a = jObj.getString("uAddress");
                        u = jObj.getInt("UserType");
                    }

                    userBean = new UserBean( id, n, m, e, p, a,u);

                    String mess=object.getString("message");
                    if(mess.contains("Login Sucessful")) {
                        editor.putString(Util.KEY_PHONE, userBean.getuPhone());
                        editor.putString("name",userBean.getuName());
                       // editor.putString("phone",userBean.getuPhone());
                        editor.putString("email",userBean.getuEmail());
                        editor.putString("password",userBean.getuPassword());
                        editor.putString("address",userBean.getuAddress());
                      //  editor.putString("token",token);


                        editor.commit();
                        Intent i = new Intent(AdminLogin.this, AdminHomeActivity.class);
                        startActivity(i);
                        finish();
                        progressDialog.dismiss();
                        Toast.makeText(AdminLogin.this, mess, Toast.LENGTH_SHORT).show();
                    }else{
                        progressDialog.dismiss();
                        Toast.makeText(AdminLogin.this, mess, Toast.LENGTH_SHORT).show();
                    }
                }catch (Exception e){
                    e.printStackTrace();
                    progressDialog.dismiss();
                    Toast.makeText(AdminLogin.this, "Exception", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(AdminLogin.this, "Error "+error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map=new HashMap<>();
                map.put("phone",login_phone);
                map.put("password",login_password);
               // map.put("token",token);
                return map;
            }
        };
        requestQueue.add(stringRequest);
    }

        /*  retrieveFromcloud();
        _loginButton.setOnClickListener(this);
        _signupLink.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_login){
            login();
        }
        _signupLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),AdminRegistration.class);
                startActivityForResult(intent,REQUEST_SIGNUP);
                finish();
                overridePendingTransition(R.anim.push_left_in,R.anim.push_left_out);
            }
        });
    }


     /* _loginButton.setOnClickListener(new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            login();
        }
    });

        _signupLink.setOnClickListener(new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            // Start the Signup activity
            Intent intent = new Intent(getApplicationContext(), AdminRegistration.class);
            startActivityForResult(intent, REQUEST_SIGNUP);
            finish();
            overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
        }
    });
}

    public void login() {
        Log.d(TAG, "Login");

        if (!validate()) {
            onLoginFailed();
            return;
        }

        _loginButton.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(AdminLogin.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Authenticating...");
        progressDialog.show();

        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();

        // TODO: Implement your own authentication logic here.

        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        // On complete call either onLoginSuccess or onLoginFailed
                        onLoginSuccess();
                        // onLoginFailed();
                        progressDialog.dismiss();
                    }
                }, 3000);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_SIGNUP) {
            if (resultCode == RESULT_OK) {

                // TODO: Implement successful signup logic here
                // By default we just finish the Activity and log them in automatically
                this.finish();
            }
        }
    }

    @Override
    public void onBackPressed() {
        // Disable going back to the MainActivity
        moveTaskToBack(true);
    }

    public void onLoginSuccess() {
        _loginButton.setEnabled(true);
        finish();
    }

    public void onLoginFailed() {
        Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();

        _loginButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _emailText.setError("enter a valid email address");
            valid = false;
        } else {
            _emailText.setError(null);
        }
        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            _passwordText.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            _passwordText.setError(null);
        }

        return valid;
    }



    @Override
    public void onClick(View v) {
        Intent i = new Intent(AdminLogin.this, AdminRegistration.class);
        startActivity(i);
        finish();

    }*/


}