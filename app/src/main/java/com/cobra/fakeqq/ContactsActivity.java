package com.cobra.fakeqq;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class ContactsActivity extends AppCompatActivity {

    private ListView contactsView;
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);

        ArrayList<ContactsInfo> list = (ArrayList<ContactsInfo>) getIntent().getSerializableExtra("list");

        contactsView = findViewById(R.id.contacts_view);
        List<String> infos = new ArrayList<>();
        for (ContactsInfo info :
                list) {
            infos.add("name:" + info.getDisplayName()+"\n phone: " + info.getNumber());
        }
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_activated_1,infos);
        contactsView.setAdapter(adapter);
    }

}
