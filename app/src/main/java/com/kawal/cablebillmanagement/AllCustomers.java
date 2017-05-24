package com.kawal.cablebillmanagement;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;

    public class AllCustomers extends AppCompatActivity implements AdapterView.OnItemClickListener {

    @InjectView(R.id.listView)
    ListView listView;

    @InjectView(R.id.editTextSearch)
    EditText editTextSearch;

    ArrayList<UserBean> objectList;

    CustomerAdapter adapter;
    UserBean uBean;
    int pos;
    RequestQueue requestQueue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_customers);
        ButterKnife.inject(this);
        requestQueue= Volley.newRequestQueue(this);

        editTextSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String str = s.toString();
                if(adapter!=null){
                    adapter.filter(str);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        retrieveFromCloud();

    }

    void retrieveFromCloud(){
        objectList = new ArrayList<>();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Util.RETRIEVE_USER_CUS_PHP, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    Log.i("respons", response);
                    if (jsonObject.getString("message").equals("Records Retrieved sucessfully")) {
                        JSONArray jsonArray = jsonObject.getJSONArray("users");
                        Log.i("RESP",response.toString());
                        Log.i("sizee", jsonArray.length() + "");
                        int id = 0 , u, s =0;
                        String n = "", m = "", e = "", p = "", a = "", c="";
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jObj = jsonArray.getJSONObject(i);

                            id = jObj.getInt("id");
                            n = jObj.getString("uName");
                            m = jObj.getString("uPhone");
                            e = jObj.getString("uEmail");
                            p = jObj.getString("uPassword");
                            a = jObj.getString("uAddress");
                            u = jObj.getInt("UserType");
                            c= jObj.getString("connectionType");
                            s = jObj.getInt("status");
                            objectList.add(new UserBean( id, n, m, e, p, a, u,c,s));
                        }
                    }
                    Log.i("objectlist",objectList.size()+"");

                    adapter = new CustomerAdapter(AllCustomers.this, R.layout.activity_show_managers, objectList);
                    listView.setAdapter(adapter);
                    listView.setOnItemClickListener(AllCustomers.this);
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(AllCustomers.this, "Some Exception", Toast.LENGTH_SHORT).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("error", error.getMessage());
                Toast.makeText(AllCustomers.this, "Some Error",Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(stringRequest);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        uBean = objectList.get(position);
        showOption();
    }
    void showOption(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        String[] items = {"view", "update", "delete"};
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                switch (i){
                    case 0:
                        showCustomer();

                        break;

                    case 1:
                        Intent intent = new Intent(AllCustomers.this, AdminRegistration.class);
                        intent.putExtra("keyUser",uBean);
                        startActivity(intent);
                        break;

                    case 2:
                        deleteCustomer();
                        break;
                }
            }
        });

        builder.create().show();
    }

    void showCustomer(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Details of "+uBean.getuName());
        builder.setMessage(uBean.toString());
        builder.setNegativeButton("Done",null);
        builder.create().show();
    }

    void deleteCustomer(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete "+uBean.getuName());
        builder.setMessage("Do you wish to Delete");
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {

                deleteFromCloud();
            }
        });
        builder.setNegativeButton("Cancel",null);
        builder.create().show();

    }

    public void deleteFromCloud(){
        // progressDialog.show();
        StringRequest request = new StringRequest(Request.Method.POST, Util.DELETE_USER_PHP, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    int success = jsonObject.getInt("success");
                    String message = jsonObject.getString("message");

                    if (success == 1) {
                        objectList.remove(pos);
                        adapter.notifyDataSetChanged();
                        Toast.makeText(AllCustomers.this, message, Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(AllCustomers.this, message, Toast.LENGTH_SHORT).show();
                    }
                    //   progressDialog.dismiss();

                } catch (JSONException e) {
                    e.printStackTrace();
                    //  progressDialog.dismiss();
                    Toast.makeText(AllCustomers.this, "Some exception", Toast.LENGTH_SHORT).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //   progressDialog.dismiss();
                Toast.makeText(AllCustomers.this, "Some volley error"+error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map =new HashMap<>();
                map.put("id",String.valueOf(uBean.getId()));
                return map;
            }
        };

        requestQueue.add(request);
    }
}

