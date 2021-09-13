package com.emiliaengberg.sqliteinlamningsuppgift;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    public static final String SHARED_PREFERENCES = "sharedPreferences";
    public static final String CHECKBOX = "checkbox";
    public static final String SHIFT = "shift";

    private int mShift;
    private boolean mIsChecked;

    private Button mShowCoworkersButton;
    private Button mSearchMainButton;
    private Button mAddCoworkerListButton;
    private EditText mSearchNameMainEditText;
    private CheckBox mSavePreferenceMainCheckBox;
    private Spinner mShiftMainSpinner;
    private String[] mShiftNumbers = { "Alla skift", "1", "2", "3", "4", "5"};
    int mPosition;
    String mName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSavePreferenceMainCheckBox = findViewById(R.id.savePreferenceMainCheckBox);

        //Setup spinner for choosing shift
        setupSpinner();

        //Setup show coworker button
        setupShow();

        //Setup search field and button
        setupSearch();

        //Setup add button
        setupAdd();

        loadSavedPreferenceShift();
    }

    @Override
    protected void onPause() {
        super.onPause();
        savePreferenceShift();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadSavedPreferenceShift();
    }


    private void setupSpinner() {
        mShiftMainSpinner = findViewById(R.id.shiftMainSpinner);

        //Setup adapter for spinner
        ArrayAdapter arrayAdapter = new ArrayAdapter(this,
                android.R.layout.simple_spinner_item, mShiftNumbers);

        //Set the type of appearance for spinner to drop down menu
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        //Set the adapter
        mShiftMainSpinner.setAdapter(arrayAdapter);
        mShiftMainSpinner.setOnItemSelectedListener(this);
    }

    //Listener for spinner. Saves position of spinner in field
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        mPosition = position;
    }

    //Listener for spinner
    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }

    //Method to setup the button for showing coworkers for selected shift in spinner
    private void setupShow() {
        mShowCoworkersButton = findViewById(R.id.showCoworkersButton);

        //OnClick listener for button. Starts CoworkerListActivity with the position in spinner as
        //an extra
        mShowCoworkersButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CoworkerListActivity.class);
                intent.putExtra(CoworkerListActivity.EXTRA_POSITION, mPosition);
                startActivity(intent);
            }
        });
    }

    //Setup search functionality. CoworkerListActivity with the user input of name as an extra
    private void setupSearch() {
        mSearchNameMainEditText = findViewById(R.id.searchNameMainEditText);
        mSearchMainButton = findViewById(R.id.searchMainButton);

        //OnClick listener for button. Starts CoworkerListActivity with name in EditText as
        //an extra. Checks that user has entered a name into the EditText field, else shows
        //toast reminding user to enter name
        mSearchMainButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mName = mSearchNameMainEditText.getText().toString();
                mPosition = 6;
                if (mName != null && !mName.isEmpty()) {
                    Intent intent = new Intent(MainActivity.this, CoworkerListActivity.class);
                    intent.putExtra(CoworkerListActivity.EXTRA_NAME, mName);
                    startActivity(intent);
                } else {
                    Toast.makeText(getApplicationContext(),
                            "Inget namn på medarbetare inlagt",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    //Setup add coworker button
    private void setupAdd() {
        mAddCoworkerListButton = findViewById(R.id.addCoworkerListButton);

        //OnClick listener for button. Starts AddCoworkerActivity
        mAddCoworkerListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddCoworkerActivity.class);
                startActivity(intent);
            }
        });
    }

    //Method for saving preferred shift if the check box is checked
    private void savePreferenceShift() {
        //Creates instance of shared preferences. The editor enables saving to shared preferences.
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFERENCES, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        mIsChecked = mSavePreferenceMainCheckBox.isChecked();

        //Check if the checkbox is checked. If so save selected shift in spinner to shared
        //preferences, else save default state.
        if (mIsChecked){
            mShift = mShiftMainSpinner.getSelectedItemPosition();
        } else {
            mShift = 0;
        }

        //Saves shift number and if check box is checked in editor
        editor.putInt(SHIFT, mShift);
        editor.putBoolean(CHECKBOX, mIsChecked);

        //Applys changes and saves in shared preferences
        editor.apply();
    }

    //Method to load previously saved shared preferences
    private void loadSavedPreferenceShift() {
        //Creates an instance of shared preferences
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFERENCES, MODE_PRIVATE);

        //Gets values of shift and check box state from shared preferences
        mShift = sharedPreferences.getInt(SHIFT, 0);
        mIsChecked = sharedPreferences.getBoolean(CHECKBOX, false);

        //Changes state of spinner and check box according to data retrieved from shared preferences
        mSavePreferenceMainCheckBox.setChecked(mIsChecked);
        mShiftMainSpinner.setSelection(mShift);
    }
}