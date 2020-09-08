package com.romulodiego.reservas.ui.ambientes;

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


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.romulodiego.reservas.R;
import com.romulodiego.reservas.model.Ambiente;
import com.romulodiego.reservas.ui.reservas.NovaReserva;

import java.util.ArrayList;

public class AmbientesFragment extends Fragment {

    private AmbientesViewModel ambientesViewModel;

    FirebaseDatabase database;
    DatabaseReference ref;

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    ListView listView;
    TextView textView;
    String[] listItem;

    ArrayList<Ambiente> listFirebase = new ArrayList<>();
    ArrayAdapter<Ambiente> adapterFirebase;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        View root = inflater.inflate(R.layout.fragment_ambientes, container, false);
        listView=(ListView) root.findViewById(R.id.list_ambientes);


        adapterFirebase = new ArrayAdapter<Ambiente>(this.getContext(),
                android.R.layout.simple_list_item_1,android.R.id.text1, listFirebase);

        carregaListaAmbientes();

        return root;
    }

    public void carregaListaAmbientes(){
        db.collection("ambientes")
                .orderBy("nome")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Ambiente newAmbiente = document.toObject(Ambiente.class);
                                newAmbiente.setDocumentId(document.getId());
                                listFirebase.add(newAmbiente);
                            }

                            listView.setAdapter(adapterFirebase);

                        } else {
                            Log.w("## Ambientes ##", "Error getting documents.", task.getException());
                        }
                    }
                });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                // TODO Auto-generated method stub
                String nome = adapterFirebase.getItem(position).getNome();
                String documentID = adapterFirebase.getItem(position).getDocumentId();
//                Toast.makeText(getContext(),documentID + " / "+ nome ,Toast.LENGTH_SHORT).show();


                Intent intentReserva =  new Intent(getContext(), NovaReserva.class);
                intentReserva.putExtra("nomeAmbiente",nome);
                intentReserva.putExtra("idAmbiente",documentID);
                startActivity(intentReserva);
            }
        });
    }

}