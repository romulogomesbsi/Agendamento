package com.romulodiego.reservas.ui.reservas;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.romulodiego.reservas.R;
import com.romulodiego.reservas.model.Reserva;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class ReservasFragment extends Fragment {

    private ReservasViewModel reservasViewModel;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    ListView listView;
    ArrayList<Reserva> listReservasFirebase = new ArrayList<>();
    ArrayAdapter<Reserva> adapterFirebase;

    TextView txt_ambiente,txt_data_criacao,txt_data_agendada,txt_duracao;

    SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm");


    ArrayList<String> listaAmbientes = new ArrayList<>();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        preencheAmbientes();
        reservasViewModel =
                ViewModelProviders.of(this).get(ReservasViewModel.class);
        View root = inflater.inflate(R.layout.fragment_reservas, container, false);

        listView=(ListView) root.findViewById(R.id.list_reservas);

        adapterFirebase = new ArrayAdapter<Reserva>(this.getContext(),
                android.R.layout.simple_list_item_1,android.R.id.text1, listReservasFirebase);
        carregaReservas();
        return root;
    }

    public void preencheAmbientes(){
        listaAmbientes.add("IZIWYPQgSTqjUO2HfKd0");
        listaAmbientes.add("PxRrw54nDoWVyiZRioQE");
        listaAmbientes.add("dw5SZ1aBz38RvmakEDhN");
        listaAmbientes.add("r7bawARd5sY6jRZh4luI");
        listaAmbientes.add("UNFl0FbUO9b6AeDat6qV");
        listaAmbientes.add("9RlYZ5LAZdazXD7AiuYS");
    }
    public void carregaReservas(){
        for (final String idAmbiente: listaAmbientes){
            db.collection("ambientes")
                    .document(idAmbiente)
                    .collection("reserva")
                    .orderBy("data_inicio")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    Log.d("Dc", document.getData().toString());

                                    Reserva reserva = document.toObject(Reserva.class);
                                    Reserva newReserva = new Reserva();
                                    newReserva.setData_criacao(reserva.getData_criacao());
                                    newReserva.setData_inicio(reserva.getData_inicio());
                                    newReserva.setData_fim(reserva.getData_fim());
                                    newReserva.setDuracao(reserva.getDuracao());
                                    newReserva.setDocumentID(document.getId());
                                    newReserva.setAmbienteID(idAmbiente);
                                    newReserva.setHora_inicio(reserva.getHora_inicio());
                                    newReserva.setNome_ambiente(reserva.getNome_ambiente());

                                    listReservasFirebase.add(newReserva);
                                }

                                listView.setAdapter(adapterFirebase);

                            } else {
                                Log.w("## Ambientes ##", "Error getting documents.", task.getException());
                            }
                        }
                    });
        }

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
//                // TODO Auto-generated method stub
                String nomeAmbiente = adapterFirebase.getItem(position).getNome_ambiente();
                String ambienteID = adapterFirebase.getItem(position).getAmbienteID();
                String reservaDocumentID = adapterFirebase.getItem(position).getDocumentID();
                String dataInicio = adapterFirebase.getItem(position).getData_inicio().toString();
                String duracao  = adapterFirebase.getItem(position).getDuracao().toString();


                Intent intentReserva =  new Intent(getContext(), ReservaDetalhes.class);
                intentReserva.putExtra("ambienteDocumentID",ambienteID);
                intentReserva.putExtra("reservaDocumentID",reservaDocumentID);
                intentReserva.putExtra("nomeAmbiente",nomeAmbiente);
                intentReserva.putExtra("dataInicio",dataInicio);
                intentReserva.putExtra("duracao",duracao);
                startActivity(intentReserva);
            }
        });
    }
}