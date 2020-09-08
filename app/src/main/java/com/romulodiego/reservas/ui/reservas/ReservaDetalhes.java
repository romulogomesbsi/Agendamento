package com.romulodiego.reservas.ui.reservas;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.romulodiego.reservas.MainActivity;
import com.romulodiego.reservas.R;
import com.romulodiego.reservas.model.Convidado;

import java.util.ArrayList;

public class ReservaDetalhes extends AppCompatActivity implements View.OnClickListener {

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    TextView txt_ambiente, txt_dt_inicio, txt_duracao;

    ListView listView;
    ArrayList<Convidado> listConvidados = new ArrayList<>();
    ArrayAdapter<Convidado> adapterFirebase;

    Button btn_cancela_reserva;

    String ambienteDocumentID = "", nomeAmbiente = "", reservaDocumentID = "", dataInicio = "", duracao = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reserva_detalhes);

        Intent iin= getIntent();
        Bundle b = iin.getExtras();

        if(b!=null)
        {
            ambienteDocumentID      =(String) b.get("ambienteDocumentID");
            reservaDocumentID       =(String) b.get("reservaDocumentID");
            nomeAmbiente            =(String) b.get("nomeAmbiente");
            dataInicio              =(String) b.get("dataInicio");
            duracao                 =(String) b.get("duracao");
        }

        txt_ambiente    = (TextView) findViewById(R.id.txtv_ambiente_detalhes);
        txt_dt_inicio   = (TextView) findViewById(R.id.txtv_dataInicio_detalhes);
        txt_duracao     = (TextView) findViewById(R.id.txtv_duracao_detalhes);

        txt_ambiente.setText(nomeAmbiente);
        txt_dt_inicio.setText(dataInicio);
        txt_duracao.setText(duracao);

        btn_cancela_reserva = (Button) findViewById(R.id.btn_cancela_reserva);
        btn_cancela_reserva.setOnClickListener(this);

        listView= (ListView) findViewById(R.id.list_detalhes_reserva);

        adapterFirebase=new ArrayAdapter<Convidado>(this,android.R.layout.simple_list_item_1,listConvidados);

        carregaListConvidados();
    }

    private void carregaListConvidados() {

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
        if (v == btn_cancela_reserva){
            db.collection("ambientes")
                    .document(ambienteDocumentID)
                    .collection("reserva")
                    .document(reservaDocumentID)
                    .delete()
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d("DELETE", "DocumentSnapshot successfully deleted!");
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w("ERROR", "Error deleting document", e);
                        }
                    });

            Intent intentMain =  new Intent(getBaseContext(), MainActivity.class);
            startActivity(intentMain);

        }
    }
}