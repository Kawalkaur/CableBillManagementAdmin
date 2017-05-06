package com.kawal.cablebillmanagement;

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
    @InjectView(R.id.input_reEnterPassword)
    EditText _reEnterPasswordText;
    @InjectView(R.id.btn_signup)
    Button _signupButton;
    @InjectView(R.id.link_login)
    TextView _loginLink;

    AdminBean bean;
    RequestQueue requestQueue;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_registration);
        ButterKnife.inject(this);
        _signupButton.setOnClickListener(this);
        bean = new AdminBean();



        requestQueue = Volley.newRequestQueue(this);

     /*   _signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signup();
            }
        });

        _loginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    bean.setaName(_nameText.getText().toString().trim());
                    bean.setaPhone(_mobileText.getText().toString().trim());
                    bean.setaEmail(_emailText.getText().toString().trim());
                    bean.setaPassword(_passwordText.getText().toString().trim());
                    bean.setaReEnterPass(_reEnterPasswordText.getText().toString().trim());
                    bean.setaAddress(_addressText.getText().toString().trim());


                    // Finish the registration screen and return to the Login activity
                    Intent intent = new Intent(getApplicationContext(), AdminLogin.class);
                    startActivity(intent);
                    finish();
                    overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);

                    insertIntoCloud();
                }



            public void insertIntoCloud(){
                StringRequest request = new StringRequest(Request.Method.POST, Util.INSERT_ADMIN_PHP, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(AdminRegistration.this, "Response", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(AdminRegistration.this,AdminHomeActivity.class);
                        startActivity(intent);
                        finish();
                    }


            } ,new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(AdminRegistration.this, "Some Error", Toast.LENGTH_SHORT).show();
                    }
                })
                {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> map =new HashMap();
                        map.put("aName", bean.getaName());
                        map.put("aPhone", bean.getaPhone());
                        map.put("aEmail", bean.getaEmail());
                        map.put("aPassword", bean.getaPassword());
                        map.put("aReEnterPass", bean.getaReEnterPass());
                        map.put("aAddress",bean.getaAddress());

                        return map;
                    }
                };
                requestQueue.add(request);
            }

        });
    }

    public void signup() {
        Log.d(TAG, "Signup");

       if (!validate()) {
            onSignupFailed();
            return;
        }

        _signupButton.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(AdminRegistration.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Creating Account...");
        progressDialog.show();

        String name = _nameText.getText().toString();
        String address = _addressText.getText().toString();
        String email = _emailText.getText().toString();
        String mobile = _mobileText.getText().toString();
        String password = _passwordText.getText().toString();
        String reEnterPassword = _reEnterPasswordText.getText().toString();

        // TODO: Implement your own signup logic here.

        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        // On complete call either onSignupSuccess or onSignupFailed
                        // depending on success
                        onSignupSuccess();
                        // onSignupFailed();
                        progressDialog.dismiss();
                    }
                }, 3000);
    }


    public void onSignupSuccess() {
        _signupButton.setEnabled(true);
        setResult(RESULT_OK, null);
        startActivity(new Intent(AdminRegistration.this, AdminLogin.class));
        finish();
    }

    public void onSignupFailed() {
        Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();

        _signupButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String name = _nameText.getText().toString();
        String address = _addressText.getText().toString();
        String email = _emailText.getText().toString();
        String mobile = _mobileText.getText().toString();
        String password = _passwordText.getText().toString();
        String reEnterPassword = _reEnterPasswordText.getText().toString();

        if (name.isEmpty() || name.length() < 3) {
            _nameText.setError("at least 3 characters");
            valid = false;
        } else {
            _nameText.setError(null);
        }

        if (address.isEmpty()) {
            _addressText.setError("Enter Valid Address");
            valid = false;
        } else {
            _addressText.setError(null);
        }


        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _emailText.setError("enter a valid email address");
            valid = false;
        } else {
            _emailText.setError(null);
        }

        if (mobile.isEmpty() || mobile.length()!=10) {
            _mobileText.setError("Enter Valid Mobile Number");
            valid = false;
        } else {
            _mobileText.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            _passwordText.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            _passwordText.setError(null);
        }

        if (reEnterPassword.isEmpty() || reEnterPassword.length() < 4 || reEnterPassword.length() > 10 || !(reEnterPassword.equals(password))) {
            _reEnterPasswordText.setError("Password Do not match");
            valid = false;
        } else {
            _reEnterPasswordText.setError(null);
        }

        return valid;
    }


    /*public void buttonSubmit(View view) {
        startActivity( new Intent(AdminRegistration.this, AdminHomeActivity.class));
    }*/
    }

    @Override
    public void onClick(View v) {
        if (v.getId()== R.id.btn_signup)
        bean.setaName(_nameText.getText().toString().trim());
        bean.setaPhone(_mobileText.getText().toString().trim());
        bean.setaEmail(_emailText.getText().toString().trim());
        bean.setaPassword(_passwordText.getText().toString().trim());
        bean.setaReEnterPass(_reEnterPasswordText.getText().toString().trim());
        bean.setaAddress(_addressText.getText().toString().trim());

        insertIntoCloud();
    }

    public void insertIntoCloud() {
        StringRequest request = new StringRequest(Request.Method.POST, Util.INSERT_ADMIN_PHP, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(AdminRegistration.this, "Success", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(AdminRegistration.this, AdminHomeActivity.class);
                startActivity(intent);
                finish();
            }


        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(AdminRegistration.this, "Some Error", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap();
                map.put("aName", bean.getaName());
                map.put("aPhone", bean.getaPhone());
                map.put("aEmail", bean.getaEmail());
                map.put("aPassword", bean.getaPassword());
                map.put("aReEnterPass", bean.getaReEnterPass());
                map.put("aAddress", bean.getaAddress());

                return map;
            }
        };
        requestQueue.add(request);
    }

   }
