package br.unipar.meuip;

import android.content.SharedPreferences;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;


public class MainActivity extends ActionBarActivity {

    private String ip_atual;
    private TextView meu_ip;
    private TextView anterior_ip;
    private SharedPreferences ipSalvo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        meu_ip = (TextView) findViewById(R.id.meu_ip);
        anterior_ip = (TextView) findViewById(R.id.anterior_ip);

        ipSalvo = this.getSharedPreferences("MeuIP", MODE_PRIVATE);

        ip_atual = ipSalvo.getString("ip", "000.000.000");
        anterior_ip.setText("Último ip foi: " + ip_atual);

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

    public void atualizar(View view) {
        Ion.with(MainActivity.this)
                .load("http://ip.jsontest.com/")
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        if (e == null) {
                            anterior_ip.setText("Último ip foi: " + ip_atual);

                            ip_atual = result.get("ip").getAsString();
                            meu_ip.setText("Meu ip é " + ip_atual);

                            //salvando os dados
                            SharedPreferences.Editor editor = ipSalvo.edit();

                            editor.putString("ip", ip_atual);
                            editor.commit();


                        } else {
                            Toast.makeText(MainActivity.this, "Erro ao tentar conectar", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }
}
