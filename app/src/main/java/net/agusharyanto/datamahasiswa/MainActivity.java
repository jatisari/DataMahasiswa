package net.agusharyanto.datamahasiswa;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.android.volley.Request;
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

public class MainActivity extends AppCompatActivity {
    private ListView listViewMahasiswa;
    private Button buttonTambah;
    private MahasiswaArrayAdapter adapter;
    private ArrayList<Mahasiswa> mahasiswaList = new ArrayList<Mahasiswa>();
    private DatabaseHelper databaseHelper;
    private SQLiteDatabase db;
    private Mahasiswa mahasiswa;
    private int selectedPosition=0;
    private ProgressDialog pDialog;

    private static  final int REQUEST_CODE_ADD =1;
    private static  final int REQUEST_CODE_EDIT =2;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mahasiswa = new Mahasiswa();
        pDialog = new ProgressDialog(MainActivity.this);
        databaseHelper = new DatabaseHelper(MainActivity.this);
        listViewMahasiswa = (ListView) findViewById(R.id.listViewMahasiswa);
        buttonTambah = (Button) findViewById(R.id.buttonAdd);
        //ambildatadari database;
        db = databaseHelper.getWritableDatabase();
       // mahasiswaList = databaseHelper.getDataMahasiswa(db);
       // gambarDatakeListView();

        loadDataServerVolley();
        buttonTambah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openTambahData();
            }
        });

    }


    private void loadDataServerVolley(){

        String url = "http://10.0.2.2/mahasiswa/listdata.php";
        pDialog.setMessage("Retieve Data Mahasiswa...");
        showDialog();

        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        hideDialog();
                        processResponse(response);
                        gambarDatakeListView();

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        hideDialog();
                        error.printStackTrace();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<>();
                // the POST parameters:
               // params.put("id","1");
                return params;
            }
        };
        Volley.newRequestQueue(this).add(postRequest);
    }

    private void processResponse(String response){

        try {
            JSONObject jsonObj = new JSONObject(response);
            JSONArray jsonArray = jsonObj.getJSONArray("data");
            Log.d("TAG", "data length: " + jsonArray.length());
            Mahasiswa objectmahasiswa = null;
            mahasiswaList.clear();
            for(int i = 0; i < jsonArray.length(); i++){
                JSONObject obj = jsonArray.getJSONObject(i);
                objectmahasiswa= new Mahasiswa();
                objectmahasiswa.setId(obj.getString("id"));

                objectmahasiswa.setNama(obj.getString("nama"));
                objectmahasiswa.setNim(obj.getString("nim"));
                objectmahasiswa.setJurusan(obj.getString("jurusan"));


                this.mahasiswaList.add(objectmahasiswa);
            }

        } catch (JSONException e) {
            Log.d("MainActivity", "errorJSON");
        }

    }
    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }


    private  void openTambahData(){
        Intent intent = new Intent(MainActivity.this, AddEditActivity.class);
        startActivityForResult(intent, REQUEST_CODE_ADD);

    }

    private void gambarDatakeListView() {
        adapter = new MahasiswaArrayAdapter(this,
                R.layout.rowmahasiswa, mahasiswaList);
        listViewMahasiswa.setAdapter(adapter);
        listViewMahasiswa.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mahasiswa = (Mahasiswa) parent.getAdapter().getItem(position);
                selectedPosition = position;
                Intent intent = new Intent(MainActivity.this, AddEditActivity.class);
                intent.putExtra("mahasiswa", mahasiswa);
                startActivityForResult(intent, REQUEST_CODE_EDIT);
            }
        });
     /*   listViewMahasiswa.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mahasiswa = (Mahasiswa) parent.getAdapter().getItem(position);
                selectedPosition = position;
                Intent intent = new Intent(MainActivity.this, AddEditServerActivity.class);
                intent.putExtra("mahasiswa", mahasiswa);
                startActivityForResult(intent, REQUEST_EDIT);
            }
        });*/


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_CODE_ADD: {
                if (resultCode == RESULT_OK && null != data) {
                    if (data.getStringExtra("refreshflag").equals("1")) {
                        //mahasiswaList = databaseHelper.getDataMahasiswa(db);
                        //gambarDatakeListView();
                        loadDataServerVolley();
                    }
                }
                break;
            }
            case REQUEST_CODE_EDIT: {
                if (resultCode == RESULT_OK && null != data) {
                    if (data.getStringExtra("refreshflag").equals("1")) {
                     //   mahasiswaList = databaseHelper.getDataMahasiswa(db);
                      //  gambarDatakeListView();
                        loadDataServerVolley();
                    }
                }
                break;
            }

        }
    }

    @Override
    public void onDestroy(){
        db.close();
        databaseHelper.close();
        super.onDestroy();
    }
}


