package com.mv.canvasdrawmarathigyan;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private DrawingView drawingView;
    private Button buttonNext, clearCanvasButton;
    TextView textViewTitle, textViewDrawThis;

    String name;

    int current = 0;
    int[] total = {0,1,2,3,4,5,6,7,8,9};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        name = getIntent().getExtras().getString("name");

        drawingView = findViewById(R.id.drawing_view);
        buttonNext = findViewById(R.id.buttonNext);
        clearCanvasButton = findViewById(R.id.clearCanvasButton);
        textViewTitle = findViewById(R.id.textViewTitle);
        textViewDrawThis = findViewById(R.id.textViewDrawThis);

        String titleText = "Welcome " + name;
        textViewTitle.setText(titleText);

        final String[] textViewDrawThisText = {"Draw " + current + " in marathi"};
        textViewDrawThis.setText(textViewDrawThisText[0]);

        buttonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isStoragePermissionGranted()) {
                    saveDrawing();
                    if(current < 9){
                        current++;
                        textViewDrawThisText[0] = "Draw " + current + " in marathi";
                        textViewDrawThis.setText(textViewDrawThisText[0]);
                        drawingView.clearCanvas();
                    }
                    else{
                        Toast.makeText(MainActivity.this, "Thank You!", Toast.LENGTH_SHORT).show();
                        finish();
                    }

                }
            }
        });

        clearCanvasButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawingView.clearCanvas();
            }
        });
    }

    public boolean isStoragePermissionGranted() {
        /*if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            return false;
        }*/
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                saveDrawing();
            } else {
                Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void saveDrawing() {
        drawingView.saveDrawing(name, String.valueOf(current) + ".png");
        Toast.makeText(this, "Saved-CanvasDrawMarathiGyanImages/MyDrawing.png", Toast.LENGTH_SHORT).show();
    }


}