package com.mv.canvasdrawmarathigyan;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class StartActivity extends AppCompatActivity {

    EditText editTextName;
    Button buttonRandom, buttonStart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);


        buttonStart = findViewById(R.id.buttonStart);
        buttonRandom = findViewById(R.id.buttonRandom);
        editTextName = findViewById(R.id.editTextName);



        buttonRandom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String random = editTextName.getText().toString() + System.currentTimeMillis();
                editTextName.setText(random);
            }
        });

        buttonStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(editTextName.getText().toString().isEmpty()){
                    Toast.makeText(StartActivity.this, "Enter your name first!", Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent i = new Intent(StartActivity.this, MainActivity.class);
                i.putExtra("name",editTextName.getText().toString());
                startActivity(i);
                editTextName.setText("");
            }
        });

    }
}