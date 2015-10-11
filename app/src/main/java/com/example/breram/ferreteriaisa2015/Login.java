package com.example.breram.ferreteriaisa2015;

/**
 * Created by BreRam on 10/10/2015.
 */
import java.util.ArrayList;
import java.util.List;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Login extends Activity implements OnClickListener {

    private EditText user, pass;
    private Button mSubmit, mRegister;

    private ProgressDialog pDialog;

    public static String username2;
    public static String password2;

    // Clase JSONParser
    JSONParser jsonParser = new JSONParser();

    // "http://xxx.xxx.x.x:1234/cas/login.php";


    private static final String LOGIN_CLIENTE_URL = "http://glezsoft.esy.es/webservices/login_cliente.php";
    private static final String LOGIN_ADMINISTRADOR_URL = "http://glezsoft.esy.es/webservices/login_admin.php";

    // La respuesta del JSON es
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        // setup input fields
        user = (EditText) findViewById(R.id.username);
        pass = (EditText) findViewById(R.id.password);

        // setup buttons
        mSubmit = (Button) findViewById(R.id.login);
        mRegister = (Button) findViewById(R.id.register);

        // register listeners
        mSubmit.setOnClickListener(this);
        mRegister.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.login:
                username2= user.getText().toString();
                password2= pass.getText().toString();
                new AttemptLogin().execute();
                break;
            case R.id.register:
                Intent i = new Intent(this, Register.class);
                startActivity(i);
                break;

            default:
                break;
        }
    }

    class AttemptLogin extends AsyncTask<String, String, String> {



        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(Login.this);
            pDialog.setMessage("Espere...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... args) {
            int success;
            try {
                // Building Parameters
                List params = new ArrayList();
                params.add(new BasicNameValuePair("username", username2));//cambiar username
                params.add(new BasicNameValuePair("password", password2));//cambiar password

                Log.d("request!", "starting");
                // getting product details by making HTTP request
                JSONObject json = jsonParser.makeHttpRequest(LOGIN_CLIENTE_URL, "POST",
                        params);

                // check your log for json response
                Log.d("Login attempt", json.toString());

                // json success tag
                success = json.getInt(TAG_SUCCESS);
                if (success == 1) {
                    Log.d("Login Correcto!", json.toString());
                    // save user data
                    SharedPreferences sp = PreferenceManager
                            .getDefaultSharedPreferences(Login.this);
                    Editor edit = sp.edit();
                    edit.putString("username", username2);
                    edit.commit();

                    Intent i = new Intent(Login.this, ClienteActivity.class);
                    finish();
                    startActivity(i);
                    return json.getString(TAG_MESSAGE);
                }else if(success == 0){//SI NO ES CLIENTE TIENE QUE VERIFICAR SI ES ADMINISTRADOR
                    JSONObject jsonAdministrador = jsonParser.makeHttpRequest(LOGIN_ADMINISTRADOR_URL, "POST",
                            params);

                    // check your log for json response
                    Log.d("Login attempt", json.toString());

                    // json success tag
                    success = jsonAdministrador.getInt(TAG_SUCCESS);
                    if (success == 1) {
                        Log.d("Login Correcto!", json.toString());
                        // save user data
                        SharedPreferences sp = PreferenceManager
                                .getDefaultSharedPreferences(Login.this);
                        Editor edit = sp.edit();
                        edit.putString("username", username2);
                        edit.commit();

                        Intent i = new Intent(Login.this, AdministradorActivity.class);
                        finish();
                        startActivity(i);
                        return jsonAdministrador.getString(TAG_MESSAGE);
                    }else {//EL USUARIO NO EXISTE
                        Log.d("Login Incorrecto!", json.getString(TAG_MESSAGE));
                        return json.getString(TAG_MESSAGE);
                    }

                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;

        }

        protected void onPostExecute(String file_url) {
            // dismiss the dialog once product deleted
            pDialog.dismiss();
            if (file_url != null) {
                Toast.makeText(Login.this, file_url, Toast.LENGTH_LONG).show();
            }
        }
    }
}
