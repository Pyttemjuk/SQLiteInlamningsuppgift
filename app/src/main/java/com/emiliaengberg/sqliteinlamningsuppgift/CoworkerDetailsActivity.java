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
import java.util.ArrayList;

public class CoworkerDetailsActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    public static final String EXTRA_COWORKER_ID = "id";

    private Button mUpdateButton;
    private Button mDeleteButton;
    private Button mBackToMainDetailsButton;
    private EditText mNameDetailsEditText;
    private EditText mPhoneNumberDetailsEditText;
    private Spinner mShiftDetailsSpinner;
    private CoworkerDBHelper mCoworkerDBHelper;
    private Coworker mCoworker;
    private ArrayList<Coworker> mCoworkerList;
    private String[] mShiftNumbers = { "1", "2", "3", "4", "5"};
    private int mCoworkerID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coworker_details);

        //Add instance of coworker database helper
        mCoworkerDBHelper = new CoworkerDBHelper(this);

        //Get id for selected coworker from CoworkerListActivity
        mCoworkerID = getIntent().getIntExtra(EXTRA_COWORKER_ID, 0);

        //Add details about coworker for selected id
        mCoworker = mCoworkerDBHelper.getCoworkerById(mCoworkerID);

        mNameDetailsEditText = findViewById(R.id.nameDetailsEditText);
        mPhoneNumberDetailsEditText = findViewById(R.id.phoneNumberDetailsEditText);

        //Set EditText fields with data about selected coworker
        mNameDetailsEditText.setText(mCoworker.getName());
        mPhoneNumberDetailsEditText.setText(mCoworker.getPhoneNumber());

        //Setup spinner for shift selection
        setupSpinner();

        //Setup update functionality
        setupUpdate();

        //Setup delete functionality
        setupDelete();

        //Setup back to main button
        setupBackButton();
    }

    //Method that sets up spinner for choosing shift
    private void setupSpinner() {
        mShiftDetailsSpinner = findViewById(R.id.shiftDetailsSpinner);

        //Setup adapter for spinner
        ArrayAdapter arrayAdapter = new ArrayAdapter(this,
                android.R.layout.simple_spinner_item, mShiftNumbers);

        //Set the type of appearance for spinner to drop down menu
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        //Set the adapter
        mShiftDetailsSpinner.setAdapter(arrayAdapter);
        mShiftDetailsSpinner.setOnItemSelectedListener(this);

        //Check which shift number that selected coworker has in spinner
        switch (mCoworker.getShiftNumber()) {
            case "1":
                mShiftDetailsSpinner.setSelection(0);
                break;
            case "2":
                mShiftDetailsSpinner.setSelection(1);
                break;
            case "3":
                mShiftDetailsSpinner.setSelection(2);
                break;
            case "4":
                mShiftDetailsSpinner.setSelection(3);
                break;
            case "5":
                mShiftDetailsSpinner.setSelection(4);
                break;
            default:
                mShiftDetailsSpinner.setSelection(0);
                break;
        }
    }

    //When shift is changed in spinner the value is saved in the coworker object
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        mCoworker.setShiftNumber(mShiftNumbers[position]);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }

    //Setup back to main intent. Used in several places
    private void backToMain() {
        //Back to main activity
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
    }

    //Setup for update functionality. When user clicks update button the information in the EditText
    //fields is saved in coworker object. A toasts informs user that the information is updated and
    //sends user back to MainActivity
    private void setupUpdate() {
        mUpdateButton = findViewById(R.id.updateButton);
        mUpdateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Change information about selected coworker. Note: shift is set in spinner
                mCoworker.setName(mNameDetailsEditText.getText().toString());
                mCoworker.setPhoneNumber(mPhoneNumberDetailsEditText.getText().toString());

                boolean rowsUpdated = mCoworkerDBHelper.updateCoworker(mCoworker);

                //Check that coworker is updated then show toast and return to MainActivity
                if (rowsUpdated) {
                    Toast.makeText(getApplicationContext(), "Medarbetare uppdaterad", Toast.LENGTH_SHORT).show();
                    //Back to main activity
                    backToMain();
                }
            }
        });
    }

    //Setup delete functionality. Deletes the selected coworker from the database. A toast informs
    //user that the coworker is deleted and sends user back to MainActivity.
    private void setupDelete() {
        mDeleteButton = findViewById(R.id.deleteButton);
        mDeleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Delete selected coworker
                boolean rowsDeleted = mCoworkerDBHelper.deleteCoworker(mCoworker);

                //Check that coworker is deleted then show toast and return to MainActivity
                if(rowsDeleted) {
                    Toast.makeText(getApplicationContext(), "Medarbetare borttagen", Toast.LENGTH_SHORT).show();
                    //Back to main activity
                    backToMain();
                }
            }
        });
    }

    //Setup back to main button
    private void setupBackButton() {
        mBackToMainDetailsButton = findViewById(R.id.backToMainDetailsButton);
        mBackToMainDetailsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Back to main activity
                backToMain();
            }
        });
    }
}