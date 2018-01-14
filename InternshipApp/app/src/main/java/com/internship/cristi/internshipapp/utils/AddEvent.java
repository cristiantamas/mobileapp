package com.internship.cristi.internshipapp.utils;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.internship.cristi.internshipapp.model.Event;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

/**
 * Created by cristi on 1/14/18.
 */

public class AddEvent {
    Event e;
    DatabaseReference firebaseRoot;

    public AddEvent(Event e){
        firebaseRoot = FirebaseDatabase.getInstance().getReference();
        this.e = e;
    }


    public int addEvent() {

        FutureTask<String> futureTask = new FutureTask<String>(new Callable<String>() {
            @Override
            public String call() throws Exception {
                firebaseRoot.child("event").child(e.getName()).setValue(e);
                return null;
            }
        });

        ExecutorService executor = Executors.newFixedThreadPool(2);
        executor.execute(futureTask);

        while (true) {
            try {
                if (futureTask.isDone()) {
                    executor.shutdown();
                    return 1;
                }
            }
            catch (Exception e){
                e.printStackTrace();
                return 0;
            }
        }
    }
}
