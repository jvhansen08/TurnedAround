package com.turnedaround.hackusuproject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.appcompat.app.AppCompatActivity;

public class SettingsActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options);
        Button saveGravButton = findViewById(R.id.save_grav_button);
        saveGravButton.setOnClickListener(v -> {
            RadioGroup gravGroup = findViewById(R.id.grav_button_group);
            RadioButton gravButton = findViewById(gravGroup.getCheckedRadioButtonId());
            RadioGroup colorGroup = findViewById(R.id.color_button_group);
            RadioButton colorButton = findViewById(colorGroup.getCheckedRadioButtonId());
            CheckBox portalBox = findViewById(R.id.portal_box);
            RadioGroup sizeGroup = findViewById(R.id.size_button_group);
            RadioButton sizeButton = findViewById(sizeGroup.getCheckedRadioButtonId());
            Intent returnIntent = new Intent();
            if(gravButton != null){
                returnIntent.putExtra("gravity", gravButton.getText().toString());}
            else{
                returnIntent.putExtra("gravity", "Earth");
            }
            if(colorButton != null){
                returnIntent.putExtra("color", colorButton.getText().toString());}
            else{
                returnIntent.putExtra("color", "#C8C8C8");
            }
            returnIntent.putExtra("portal", portalBox.isChecked());
            if(sizeButton != null){
                returnIntent.putExtra("mazeSize", sizeButton.getText().toString());}
            else{
                returnIntent.putExtra("mazeSize", "Medium");
            }
            setResult(Activity.RESULT_OK, returnIntent);
            finish();
        });
    }
}
