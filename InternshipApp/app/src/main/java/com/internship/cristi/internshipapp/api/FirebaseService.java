package com.internship.cristi.internshipapp.api;

import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;

import com.google.android.gms.common.data.DataBufferObserver;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.internship.cristi.internshipapp.model.Event;
import com.internship.cristi.internshipapp.model.Team;
import com.internship.cristi.internshipapp.model.User;
import com.internship.cristi.internshipapp.model.UserDetails;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import de.hdodenhof.circleimageview.CircleImageView;

/**
* Created by cristi on 1/16/18.
*/

public class FirebaseService extends Observable {
    private FirebaseDatabase firebase = FirebaseDatabase.getInstance();
    private FirebaseStorage storage = FirebaseStorage.getInstance();
    private FirebaseAuth auth = FirebaseAuth.getInstance();
    private static List<Observer> observers;
    private List<Event> eventList;
    private List<Team> teamList;
    private List<User> userList;
    private List<UserDetails> userDetailsList;
    private User currentUser;
    private UserDetails currentUserDetails;
    private static FirebaseService instance = null;

    public User getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }

    public UserDetails getCurrentUserDetails() {
        return currentUserDetails;
    }

    public void setCurrentUserDetails(UserDetails currentUserDetails) {
        this.currentUserDetails = currentUserDetails;
    }

    public static List<Observer> getObservers() {
        return observers;
    }

    private FirebaseService() {
        eventList = new ArrayList<>();
        teamList = new ArrayList<>();
        userDetailsList = new ArrayList<>();
        userList = new ArrayList<>();
        observers = new ArrayList<>();

        setOfflinePersistance();

        DatabaseReference database = getDatabase("userDetails");
        database.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                UserDetails ud = dataSnapshot.getValue(UserDetails.class);
                userDetailsList.add(ud);

                for(Observer observer: observers){
                    observer.update(FirebaseService.this, userDetailsList);
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                UserDetails ud = dataSnapshot.getValue(UserDetails.class);
                UserDetails old = null;
                for(UserDetails userDetails: userDetailsList){
                    if(ud.getUserId().compareTo(userDetails.getUserId()) == 0){
                        old = userDetails;
                        break;
                    }
                }

                if(old != null){
                    old.setName(ud.getName());
                    old.setSuername(ud.getSuername());
                    old.setAbout(ud.getAbout());
                    old.setMail(ud.getMail());
                    //Add notification
                }

                for(Observer observer: observers){
                    observer.update(FirebaseService.this, userDetailsList);
                }

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                UserDetails ud = dataSnapshot.getValue(UserDetails.class);
                for(int i = 0; i < userDetailsList.size(); i++){
                    if(userDetailsList.get(i).getUserId().compareTo(ud.getUserId()) == 0){
                        userDetailsList.remove(i);
                        //Add notification
                    }
                }

                for(Observer observer: observers){
                    observer.update(FirebaseService.this, userDetailsList);
                }
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        database = getDatabase("user");
        database.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                User u = dataSnapshot.getValue(User.class);
                userList.add(u);

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                User u = dataSnapshot.getValue(User.class);

                for(int i =0; i < userList.size(); i++){
                    if(u.getUsername().compareTo(userList.get(i).getUsername()) == 0){
                        userList.remove(i);
                    }
                }

                for(Observer observer: observers){
                    observer.update(FirebaseService.this, userList);
                }
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        database = getDatabase("team");
        database.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Team t = dataSnapshot.getValue(Team.class);
                teamList.add(t);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        database = getDatabase("event");
        database.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Event e= dataSnapshot.getValue(Event.class);
                eventList.add(e);

                for(Observer observer: observers){
                    observer.update(FirebaseService.this, eventList);
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                //TO-DO
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                //TO-DO
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public static FirebaseService getInstance() {
        if (instance != null) {
            return instance;
        }
        instance = new FirebaseService();
        return instance;
    }

    private DatabaseReference getDatabase(String ref) {
        return firebase.getReference(ref);
    }


    public UserDetails addUserDetails(UserDetails userDetails){
        DatabaseReference ref = getDatabase("userDetails");
        String id = ref.push().getKey();
        userDetails.setId(id);
        ref.child(id).setValue(userDetails);

        return userDetails;
    }

    public void setOfflinePersistance(){
        DatabaseReference ref = getDatabase("userDetails");
        ref.keepSynced(true);

        ref = getDatabase("user");
        ref.keepSynced(true);

        ref = getDatabase("event");
        ref.keepSynced(true);

        ref = getDatabase("team");
        ref.keepSynced(true);

    }

    public String addUser(User user){
        DatabaseReference ref = getDatabase("user");
        String id = ref.push().getKey();
        user.setId(id);
        ref.child(id).setValue(user);

        return id;
    }

    public void addEvent(Event e){
        DatabaseReference ref = getDatabase("event");
        ref.push();
        String id = e.getName();
        e.setId(e.getName());
        ref.child(id).setValue(e);
    }

    public void addTeam(Team t){
        DatabaseReference ref = getDatabase("team");
        ref.push();
        ref.child(t.getTechnology()).setValue(t);
    }

    public void updateUserDetails(UserDetails userDetails){
        DatabaseReference database = getDatabase("userDetails");
        database.child(userDetails.getId()).setValue(userDetails);
    }

    public void deleteUser(String id, String idusr){
        DatabaseReference database = getDatabase("user");
        database.child(id).setValue(null);

        database = getDatabase("userDetails");
        database.child(idusr).setValue(null);
    }

    public void deleteUserDetails(String id){
        DatabaseReference database = getDatabase("userDetails");
        database.child(id).setValue(null);
    }

    public void deleteEvent(String id){
        DatabaseReference database = getDatabase("event");
        database.child(id).setValue(null);
    }


    public ArrayList<String> getAllUsers(){
        ArrayList<String> nameList = new ArrayList<>();

        for(UserDetails ud: userDetailsList){
            if (ud.getType().compareTo("admin") == 0)
                nameList.add(ud.getName() + " " + ud.getSuername() + " (mentor)");
            else
                nameList.add(ud.getName() + " " + ud.getSuername());
        }

        return nameList;
    }


    public ArrayList<UserDetails> getUsersByTeam(String team){
        ArrayList<UserDetails> nameList = new ArrayList<>();

        for(UserDetails ud: userDetailsList){
            if(ud.getTeamId().compareTo(team) == 0)
                nameList.add(ud);
        }

        return nameList;
    }

    public ArrayList<String> getUsersNameByTeam(String teamId) {
        ArrayList<String> nameList = new ArrayList<>();

        for(UserDetails ud: userDetailsList){
            if(ud.getTeamId().compareTo(teamId) == 0) {
                if (ud.getType().compareTo("admin") == 0)
                    nameList.add(ud.getName() + " " + ud.getSuername() + " (mentor)");
                else
                    nameList.add(ud.getName() + " " + ud.getSuername());
            }
        }

        return nameList;
    }


    public ArrayList<String> getAllEvents(){
        ArrayList<String> nameList = new ArrayList<>();

        for(Event e: eventList){
            nameList.add((e.getName() + " hosted by " + e.getOwner() + "\n Room: " + e.getRoom() +"\nDate: " + e.getDatetime()));
        }

        return nameList;
    }

    public ArrayList<Float> getStats(){
        float nrTecs = teamList.size();
        ArrayList<Float> result = new ArrayList<>();
        for(int i =0;i<nrTecs; i++){
            result.add((countTech(teamList.get(i).getTechnology()) / nrTecs));
        }

        return result;
    }

    private int countTech(String tech){
        int counter = 0;
        for(UserDetails ud: userDetailsList){
            if(ud.getTeamId().compareTo(tech) == 0)
                counter +=1;
        }

        return counter;
    }

    public String uploadPhoto(CircleImageView profilePhoto, String username){
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageReference = storage.getReference().child(username + ".png");
        profilePhoto.setDrawingCacheEnabled(true);
        profilePhoto.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED), View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        profilePhoto.layout(0, 0, profilePhoto.getMeasuredWidth(), profilePhoto.getMeasuredHeight());
        profilePhoto.buildDrawingCache();
        Bitmap bitmap = Bitmap.createBitmap(profilePhoto.getDrawingCache());

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
        byte[] data = outputStream.toByteArray();

        UploadTask uploadTask = storageReference.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
            }
        });

        return storageReference.child(username + ".png").getDownloadUrl().toString();
    }

    public User getUserByUserName(String userText) {;
        for(User u: userList){
            if (u.getUsername().compareTo(userText) == 0) {
                return u;
            }
        }

        return  null;
    }

    public UserDetails getDetailsByUser(String id) {
        for(UserDetails ud: userDetailsList){
            if (ud.getUserId().compareTo(id) == 0)
                return ud;
        }

        return  null;
    }

    public ArrayList<String> getTeamNames() {
        ArrayList<String> teamNames = new ArrayList<>();

        for(Team t: teamList){
            teamNames.add(t.getTechnology());
        }

        return teamNames;
    }

    public ArrayList<UserDetails> getAUsers() {
        return (ArrayList<UserDetails>) userDetailsList;
    }
}
