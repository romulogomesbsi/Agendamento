package com.romulodiego.reservas.ui.reservas;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.romulodiego.reservas.R;
import com.romulodiego.reservas.model.Reserva;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;


public class NovaReserva extends AppCompatActivity implements View.OnClickListener {

    Button btn_data;
    Button btn_hora;
    Button btn_add_reserva;

    TextView edt_data;
    TextView edt_hora;
    TextView txtv_nome_ambiente;

    EditText edt_duracao;

    String documentID = "";
    private TimePicker timePicker = null;


    Date dataInicio;
    Date dataFim;

    Calendar calendar;
    DatePickerDialog datePickerDialog;
    private int mHour, mMinute;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    ArrayList<Reserva> listReservasExistentes = new ArrayList<>();

    String ambienteDocumentID = "";
    String reservaDocumentID = "";
    String minhaReservaDocumentID = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nova_reserva);


        edt_data =(TextView)findViewById(R.id.edt_data_inicio);
        edt_hora = (TextView)findViewById(R.id.edt_hora_inicio);

        btn_data =(Button)findViewById(R.id.btn_selecionar_data);
        btn_hora = (Button)findViewById(R.id.btn_selecionar_hora);
        btn_add_reserva = (Button)findViewById(R.id.btn_add_reserva);

        txtv_nome_ambiente = (TextView) findViewById(R.id.txtv_nome_ambiente);
        edt_duracao = (EditText) findViewById(R.id.edt_tot_horas);

        btn_data.setOnClickListener(this);
        btn_hora.setOnClickListener(this);
        btn_add_reserva.setOnClickListener(this);


        Intent iin= getIntent();
        Bundle b = iin.getExtras();

        if(b!=null)
        {
            String nomeAmbiente =(String) b.get("nomeAmbiente");
            txtv_nome_ambiente.setText(nomeAmbiente);
            documentID = (String) b.get("idAmbiente");
        }


    }

    @Override
    public void onClick(View v) {
        if(v == btn_data){
            final Calendar c = Calendar.getInstance();

            calendar = Calendar.getInstance();
            int dia = calendar.get(Calendar.DAY_OF_MONTH);
            int mes = calendar.get(Calendar.MONTH);
            int ano = calendar.get(Calendar.YEAR);

            datePickerDialog = new DatePickerDialog(NovaReserva.this,
                    new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                            edt_data.setText(dayOfMonth + "/" + (month + 1) + "/" + year);
                        }
                    }, dia, mes, ano);
            datePickerDialog.show();
        }else if(v == btn_hora){
            // Get Current Time
            final Calendar c = Calendar.getInstance();
            mHour = c.get(Calendar.HOUR_OF_DAY);
            mMinute = c.get(Calendar.MINUTE);

            // Launch Time Picker Dialog
            TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                    new TimePickerDialog.OnTimeSetListener() {

                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay,
                                              int minute) {

                            edt_hora.setText(hourOfDay + ":" + minute);
                        }
                    }, mHour, mMinute, false);
            timePickerDialog.show();

        }else if (v == btn_add_reserva){

//            consultaReservas();
            AddReserva();
        }
    }

    public void consultaReservas(){
                db.collection("ambientes").
                    document(documentID).
                    collection("reserva")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    Log.d("CONSULTA RESERVAS", "EXISTE RESERVAS NESSE PERÍODO: "+document.getData().toString());

                                    final Reserva reserva = document.toObject(Reserva.class);

                                    listReservasExistentes.add(reserva);
                                }

                            } else {
                                Log.w("## Ambientes ##", "Error getting documents.", task.getException());
                            }
                        }


                    });

            for (Reserva aux: listReservasExistentes) {
                Log.d("ReservaAux", aux.getData_inicio() + "" + aux.getData_fim());
            }



    }

    boolean AddReserva() {


        String dtStart = edt_data.getText().toString()+" "+edt_hora.getText().toString();
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        try {
            Date date = format.parse(dtStart);
            System.out.println("Data formatada: "+date);

            GregorianCalendar gc = new GregorianCalendar();
            gc.setTime(date);

            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
            System.out.println(sdf.format(gc.getTime()));

            gc.add(Calendar.HOUR,Integer.parseInt(edt_duracao.getText().toString()));
            System.out.println("Data com as horas somadas: "+sdf.format(gc.getTime()));

            dataInicio =  format.parse( edt_data.getText().toString()+" "+edt_hora.getText().toString());
            dataFim = format.parse(sdf.format(gc.getTime()));

        } catch (ParseException e) {
            e.printStackTrace();
        }

        // Create a new user with a first and last name
//        Map<String, Object> user = new HashMap<>();
//        user.put("first", "Ada");
//        user.put("last", "Lovelace");
//        user.put("born", 1815);

        Map<String, Object> reserva = new HashMap<>();
        reserva.put("nome_ambiente", txtv_nome_ambiente.getText().toString());
        reserva.put("data_inicio", dataInicio);
        reserva.put("data_fim", dataFim);
        reserva.put("hora_inicio", edt_hora.getText().toString());
        reserva.put("duracao", edt_duracao.getText().toString());
        reserva.put("data_criacao",  new Date());


        db.collection("ambientes").document(documentID).collection("reserva")
                .add(reserva)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        preencheDados(null, documentReference.getId());
                        Intent intentConvidado =  new Intent(getBaseContext(), Convidado_reserva.class);
                        intentConvidado.putExtra("ambienteDocumentID",documentID);
                        intentConvidado.putExtra("reservaDocumentID", documentReference.getId());
                        startActivity(intentConvidado);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("Error adding document", "Error adding document", e);
                    }
                });

//
//        db.collection("minhas_reservas")
//                .add(reserva)
//                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
//                    @Override
//                    public void onSuccess(DocumentReference documentReference) {
//                        Log.d("added with ID", "DocumentSnapshot added with ID: " + documentReference.getId());
//                        final String id = documentReference.getId();
//                        preencheDados(documentReference.getId(), null);
//                    }
//                })
//                .addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        Log.w("Error adding document", "Error adding document", e);
//                    }
//                });


        return true;
    }

    public void preencheDados(String minhaReservaDocumentIDparam, String reservaDocumentIDparam){
        if(minhaReservaDocumentIDparam != null){
            minhaReservaDocumentID = minhaReservaDocumentIDparam;
        }

        if(reservaDocumentIDparam != null){
            reservaDocumentID = reservaDocumentIDparam;
        }

    }

}

