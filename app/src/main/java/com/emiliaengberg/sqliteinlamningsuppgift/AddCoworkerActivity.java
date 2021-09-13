package com.emiliaengberg.sqliteinlamningsuppgift;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class AddCoworkerActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private Button mAddCoworkerButton;
    private Button mBackToMainAddButton;
    private EditText mNameEditText;
    private EditText mPhoneNumberEditText;
    private Spinner mShiftAddSpinner;
    private String[] mShiftNumbers = { "1", "2", "3", "4", "5"};
    CoworkerDBHelper mCoworkerDBHelper;
    private Coworker mCoworker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_coworker);

        //Add instance of coworker database helper
        mCoworkerDBHelper = new CoworkerDBHelper(this);

        mCoworker = new Coworker();

        mNameEditText = findViewById(R.id.nameEditText);
        mPhoneNumberEditText = findViewById(R.id.phoneNumberEditText);

        //Setup spinner for shift selection
        setupSpinner();

        //Setup add coworker functionality
        setupAdd();

        //Setup back to main button
        setupBackButton();
    }

    private void setupSpinner() {
        mShiftAddSpinner = findViewById(R.id.shiftAddSpinner);

        //Setup adapter for spinner
        ArrayAdapter arrayAdapter = new ArrayAdapter(this,
                android.R.layout.simple_spinner_item, mShiftNumbers);

        //Set the type of appearance for spinner to drop down menu
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        //Set the adapter
        mShiftAddSpinner.setAdapter(arrayAdapter);
        mShiftAddSpinner.setOnItemSelectedListener(this);
    }

    //Listener for spinner. Saves shift number in the coworker object
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        mCoworker.setShiftNumber(mShiftNumbers[position]);
    }

    //Listener for spinner.
    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }

    //Setup back to main intent. Used in several places
    private void backToMain() {
        //Back to main activity
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
    }

    //Method to save data in coworker object and adds to database when add coworker button
    //is clicked
    private void setupAdd() {
        mAddCoworkerButton = findViewById(R.id.addCoworkerButton);
        mAddCoworkerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Sets information about selected coworker. Note: shift is set in spinner
                mCoworker.setName(mNameEditText.getText().toString());
                mCoworker.setPhoneNumber(mPhoneNumberEditText.getText().toString());

                //Adds coworker to database
                mCoworkerDBHelper.addCoworker(mCoworker);

                //Inform user that coworker is added to database
                Toast.makeText(getApplicationContext(), "Medarbetare tillagd",
                        Toast.LENGTH_SHORT).show();

                //Send user back to main activity
                backToMain();
            }
        });
    }

    //Setup back to main button
    private void setupBackButton() {
        mBackToMainAddButton = findViewById(R.id.backToMainAddButton);
        mBackToMainAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Send user back to main activity
                backToMain();
            }
        });
    }
}