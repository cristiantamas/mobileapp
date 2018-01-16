package com.internship.cristi.internshipapp;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.common.data.DataBufferObserver;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.internship.cristi.internshipapp.api.FirebaseService;
import com.internship.cristi.internshipapp.model.UserDetails;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;


/**

 * A simple {@link Fragment} subclass.
 */
public class AllUsersFragment extends Fragment implements Observer{


    FirebaseService firebaseService;

    ListView allUsersListView;

    ArrayList<UserDetails> allUsersList;


    public AllUsersFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.fragment_all_users, container, false);

        firebaseService= FirebaseService.getInstance();
      //  firebaseService.getObservers().add((Observer) getActivity());

        allUsersListView = view.findViewById(R.id.allUsersListView);
        populateListView();

        return view;
    }


    private void populateListView() {
        try {
            ArrayAdapter<String> userAdapter = new ArrayAdapter<String>(
                    getActivity(),
                    android.R.layout.simple_list_item_1,
                    firebaseService.getAllUsers());

            allUsersList = firebaseService.getAUsers();
            allUsersListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Intent intent = new Intent(getActivity(), UpdateUserActivity.class);
                    Bundle b = new Bundle();
                    b.putParcelable("userDetails", allUsersList.get((int) l));
                    intent.putExtra("userDetails", b);
                    startActivity(intent);
                }
            });

            allUsersListView.setAdapter(userAdapter);
        }
        catch (Exception e){
            Toast.makeText(getActivity(), "Cannot open profil", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void update(Observable observable, Object o) {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.detach(this).attach(this).commit();
    }
}
