package com.example.breram.ferreteriaisa2015;

/**
 * Created by BreRam on 11/10/2015.
 */
import java.util.ArrayList;
import java.util.List;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;
import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Register extends Activity implements OnClickListener{
    private EditText edtUsuario, edtApellidos,edtTelefono,edtCorreoElectronico,edtNombreUsuario,edtContraseña;
    private Button  btnRegistrar;

    public static String usuarioS;
    public static String apellidosS;
    public static String telefonoS;
    public static String correoS;
    public static String nombreUsuarioS;
    public static String contrasenaS;

    // Progress Dialog
    private ProgressDialog pDialog;

    // JSON parser class
    JSONParser jsonParser = new JSONParser();

    //Ruta del web services
    private static final String REGISTER_URL = "http://glezsoft.esy.es/webservices/register.php";

    //ids
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);

        edtUsuario = (EditText)findViewById(R.id.edtUsuario);
        edtApellidos = (EditText)findViewById(R.id.edtApellidos);
        edtTelefono = (EditText)findViewById(R.id.edtTelefono);
        edtCorreoElectronico = (EditText)findViewById(R.id.edtCorreoElectronico);
        edtNombreUsuario = (EditText)findViewById(R.id.edtNombreUsuario);
        edtContraseña = (EditText)findViewById(R.id.edtcontraseña);


        btnRegistrar = (Button)findViewById(R.id.btnRegister);
        btnRegistrar.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        //Recupero la informacion y la paso a las varibles staticas

        usuarioS = edtUsuario.getText().toString();
        apellidosS = edtApellidos.getText().toString();
        telefonoS = edtTelefono.getText().toString();
        correoS = edtCorreoElectronico.getText().toString();
        nombreUsuarioS = edtNombreUsuario.getText().toString();
        contrasenaS = edtContraseña.getText().toString();
        new CreateUser().execute();

    }

    class CreateUser extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(Register.this);
            pDialog.setMessage("Creando Usuario...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... args) {
            // TODO Auto-generated method stub
            // Check for success tag

            int success;

            try {
                // Building Parameters
                List params = new ArrayList();
                params.add(new BasicNameValuePair("usuario", usuarioS));
                params.add(new BasicNameValuePair("apellidos", apellidosS));
                params.add(new BasicNameValuePair("telefono", telefonoS));
                params.add(new BasicNameValuePair("correo", correoS));
                params.add(new BasicNameValuePair("nombreUsuario", nombreUsuarioS));
                params.add(new BasicNameValuePair("password", contrasenaS));

                Log.d("request!", "starting");

                //Posting user data to script
                JSONObject json = jsonParser.makeHttpRequest(
                        REGISTER_URL, "POST", params);

                // full json response
                Log.d("Registering attempt", json.toString());

                // json success element
                success = json.getInt(TAG_SUCCESS);
                if (success == 1) {
                    Log.d("Usuario Creado!!!", json.toString());
                    finish();
                    return json.getString(TAG_MESSAGE);
                }else{
                    Log.d("Registro Fallido!!!", json.getString(TAG_MESSAGE));
                    return json.getString(TAG_MESSAGE);

                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;

        }

        protected void onPostExecute(String file_url) {
            // dismiss the dialog once product deleted
            pDialog.dismiss();
            if (file_url != null){
                Toast.makeText(Register.this, file_url, Toast.LENGTH_LONG).show();
            }
        }
    }
}