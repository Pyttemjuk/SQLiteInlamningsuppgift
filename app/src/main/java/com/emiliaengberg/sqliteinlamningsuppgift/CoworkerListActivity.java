package com.emiliaengberg.sqliteinlamningsuppgift;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import java.util.ArrayList;

public class CoworkerListActivity extends AppCompatActivity {

    public static final String EXTRA_POSITION = "position";
    public static final String EXTRA_NAME = "name";

    private Button mBackButton;
    private ArrayList<Coworker> mCoworkerList;
    private RecyclerView mRecyclerView;
    private RecyclerAdapter mRecyclerAdapter;
    private int mPosition;
    private String mName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coworker_list);

        //Add instance of coworker database helper
        CoworkerDBHelper coworkerDBHelper = new CoworkerDBHelper(this);

        //Get position that spinner had in MainActivity. Default value is 6 and is used for name
        //search
        mPosition = getIntent().getIntExtra(EXTRA_POSITION, 6);

        //Check if position matches shift number or 0 for all shifts and load data
        switch (mPosition) {
            case 0:
                mCoworkerList = coworkerDBHelper.getAllCoworkers();
                break;
            case 1:
                mCoworkerList = coworkerDBHelper.getCoworkerByShift("1");
                break;
            case 2:
                mCoworkerList = coworkerDBHelper.getCoworkerByShift("2");
                break;
            case 3:
                mCoworkerList = coworkerDBHelper.getCoworkerByShift("3");
                break;
            case 4:
                mCoworkerList = coworkerDBHelper.getCoworkerByShift("4");
                break;
            case 5:
                mCoworkerList = coworkerDBHelper.getCoworkerByShift("5");
                break;
        }

        //Get user input of name in MainActivity
        mName = getIntent().getStringExtra(EXTRA_NAME);

        //If position is the default value from extra then it is a search for a specific name
        //and if no match is found shows a toast with information to user.
        // Note: The search returns an array with all matches.
        if (mPosition == 6) {
            mCoworkerList = coworkerDBHelper.getCoworkerByName(mName);
            if (mCoworkerList.size() <= 0) {
                Toast.makeText(getApplicationContext(),
                        "Hittar ingen medarbetare med det namnet",
                        Toast.LENGTH_SHORT).show();
            }
        }

        //If there are items in the array then setup recycler view. The list will be empty if no
        //coworker is found from name search.
        if (mCoworkerList !=null) {
            setupRecyclerView();
        }

        //Setup back to main button
        setupBackButton();
    }

    //Method to setup recycler view
    private void setupRecyclerView() {
        mRecyclerView = findViewById(R.id.recyclerView);
        mRecyclerAdapter = new RecyclerAdapter(mCoworkerList);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        mRecyclerView.setAdapter(mRecyclerAdapter);

        //Adds a touch listener to the recycle view. When user clicks on coworker in the recycler
        //view it will start the CoworkerDetailsActivity where user can update information or
        //delete coworker. Sends position in array as an extra.
        mRecyclerView.addOnItemTouchListener(new RecyclerTouchListener(this,
                mRecyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, final int position) {

                int coworkerId = mCoworkerList.get(position).getId();

                Intent intent = new Intent(getApplicationContext(), CoworkerDetailsActivity.class);
                intent.putExtra(CoworkerDetailsActivity.EXTRA_COWORKER_ID, coworkerId);
                startActivity(intent);
            }

            @Override
            public void onLongClick(View view, int position) {
            }
        }));
    }

    //Setup back to main button
    private void setupBackButton() {
        mBackButton = findViewById(R.id.backListButton);
        mBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(CoworkerListActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }
}