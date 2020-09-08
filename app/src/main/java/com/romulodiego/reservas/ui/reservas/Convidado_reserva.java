package com.romulodiego.reservas.ui.reservas;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.romulodiego.reservas.MainActivity;
import com.romulodiego.reservas.R;
import com.romulodiego.reservas.model.Ambiente;
import com.romulodiego.reservas.model.Convidado;
import com.romulodiego.reservas.model.Reserva;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Convidado_reserva extends AppCompatActivity implements View.OnClickListener {

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    ListView listView;
    ArrayList<Convidado> listConvidados = new ArrayList<>();
    ArrayAdapter<Convidado> adapterFirebase;
    String ambienteDocumentID = "",minhaReservaDocumentID = "", reservaDocumentID = "";

    EditText edt_nome_convidado, edt_telefone_convidado;

    Button btn_add_convidado, btn_concluir;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_convidado_reserva);

        Intent iin= getIntent();
        Bundle b = iin.getExtras();

        if(b!=null)
        {
            ambienteDocumentID      =(String) b.get("ambienteDocumentID");
            reservaDocumentID       =(String) b.get("reservaDocumentID");
        }

        edt_nome_convidado = (EditText) findViewById(R.id.txt_nome_convidado);
        edt_telefone_convidado = (EditText)findViewById(R.id.txt_telefone_convidado);

        listView= (ListView) findViewById(R.id.list_add_convidados);

        adapterFirebase=new ArrayAdapter<Convidado>(this,android.R.layout.simple_list_item_1,listConvidados);

        btn_add_convidado = (Button)findViewById(R.id.btn_add_convidado);
        btn_concluir = (Button)findViewById(R.id.btn_concluir_add_convidados);

        btn_add_convidado.setOnClickListener(this);
        btn_concluir.setOnClickListener(this);

        carregaListConvidados();

    }

    private void carregaListConvidados() {
        listConvidados = new ArrayList<>();

        db.collection("ambientes")
                .document(ambienteDocumentID)
                .collection("reserva")
                .document(reservaDocumentID)
                .collection("convidados")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Convidado convidado = new Convidado();
                                convidado = document.toObject(Convidado.class);
                                listConvidados.add(convidado);
                            }

                            listView.setAdapter(adapterFirebase);

                        } else {
                            Log.w("## Ambientes ##", "Error getting documents.", task.getException());
                        }
                    }
                });
    }

    @Override
    public void onClick(View v) {
        if(v == btn_add_convidado){
            addConvidado();

            listView= (ListView) findViewById(R.id.list_add_convidados);

            adapterFirebase=new ArrayAdapter<Convidado>(this,android.R.layout.simple_list_item_1,listConvidados);
            carregaListConvidados();
        }else if(v == btn_concluir){

            Intent intentMain =  new Intent(getBaseContext(), MainActivity.class);
            startActivity(intentMain);
        }
    }


    public void addConvidado(){
        Map<String, Object> convidado = new HashMap<>();
        convidado.put("nome", edt_nome_convidado.getText().toString());
        convidado.put("telefone", edt_telefone_convidado.getText().toString());

        edt_nome_convidado.setText("");
        edt_telefone_convidado.setText("");


        db.collection("ambientes")
                .document(ambienteDocumentID)
                .collection("reserva")
                .document(reservaDocumentID)
                .collection("convidados")
                .add(convidado)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d("added with ID", "DocumentSnapshot added with ID: " + documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("Error adding document", "Error adding document", e);
                    }
                });


//        db.collection("minhas_reservas")
//                .document(minhaReservaDocumentID)
//                .collection("convidados")
//                .add(convidado)
//                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
//                    @Override
//                    public void onSuccess(DocumentReference documentReference) {
//                        Log.d("added with ID", "DocumentSnapshot added with ID: " + documentReference.getId());
//                    }
//                })
//                .addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        Log.w("Error adding document", "Error adding document", e);
//                    }
//                });


    }

}