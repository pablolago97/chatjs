package com.example.pablomasterrace.chatjs;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft_17;
import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

import static android.R.attr.id;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final int MY_PERMISSIONS_REQUEST_INTERNET = 1;
    private WebSocketClient mWebSocketClient;

    String id = "";
    String getMensaje = "";
    String dest = "";
    String checkBox = "";

    EditText mensaje;
    EditText destinatario;

    JSONObject clienteRecibe;
    JSONObject clienteEnvia;

    CheckBox check;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.nav_user) {
            connectWebSocket();
        } else if (id == R.id.nav_gallery) {
            connectWebSocket();
        } else if (id == R.id.nav_slideshow) {
            connectWebSocket();
        } else if (id == R.id.nav_manage) {
            connectWebSocket();
        } else if (id == R.id.nav_share) {
            connectWebSocket();
        } else if (id == R.id.nav_send) {
            connectWebSocket();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void connectWebSocket() {
        URI uri;
        try {
            uri = new URI("ws://androidproject-pablovandam2.c9users.io:8081");
        } catch (URISyntaxException e) {
            e.printStackTrace();
            return;
        }

        Map<String, String> headers = new HashMap<>();
        mWebSocketClient = new WebSocketClient(uri, new Draft_17(), headers, 0) {

            @Override
            public void onOpen(ServerHandshake serverHandshake) {
                Log.i("Websocket", "Opened");

            }

            @Override
            public void onMessage(String s) {
                final String message = s;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        TextView textView = (TextView)findViewById(R.id.messages);
                        try {
                            textView.append(recibir() + "\n" + message);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }

            @Override
            public void onClose(int i, String s, boolean b) {
                Log.i("Websocket", "Closed " + s);
            }

            @Override
            public void onError(Exception e) {
                Log.i("Websocket", "Error " + e.getMessage());
            }
        };

        mWebSocketClient.connect();
    }


    public String recibir() throws JSONException {
        clienteEnvia = new JSONObject();
        id = clienteRecibe.getString("id");
        getMensaje = clienteRecibe.getString("mensaje");
        checkBox = clienteRecibe.getString("privado");
        dest = clienteRecibe.getString("destinatario");

        String mensaje = id + getMensaje + checkBox + dest;
        return mensaje;
    }

    public String enviar(){
        clienteRecibe = new JSONObject();
        mensaje = (EditText)findViewById(R.id.message);
        destinatario = (EditText)findViewById(R.id.userDest);
        check = (CheckBox)findViewById(R.id.smsPrivado);
        checkBox = check.isChecked() ? "true" : "false";
        getMensaje = mensaje.getText().toString();
        dest = destinatario.getText().toString();
        try{
            clienteRecibe.put("id", id);
            clienteRecibe.put("mensaje", getMensaje);
            clienteRecibe.put("privado", check);
            clienteRecibe.put("destinatario", dest);
        }catch (JSONException e){}

        String sms = clienteRecibe.toString();
        return sms;
    }

    public void enviarMensaje(View btn) {
        mWebSocketClient.send(enviar());
        mensaje.setText("");
        destinatario.setText("");
    }








}
