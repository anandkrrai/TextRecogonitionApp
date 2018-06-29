package com.akr.imagemlkit;

import android.content.Intent;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.text.FirebaseVisionText;
import com.google.firebase.ml.vision.text.FirebaseVisionTextDetector;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    ImageView imageView;
    Button snap,detect;
    TextView textView;
    private Bitmap bitmap;

    private static final int RequestCode=1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageView=findViewById(R.id.imageView);
        snap=findViewById(R.id.button);
        detect=findViewById(R.id.button2);
        textView=findViewById(R.id.textView);


        snap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent,RequestCode);
                imageView.setImageBitmap(bitmap);
            }
        });


        detect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    DetectText();
            }
        });
    }

    private void DetectText() {
        FirebaseVisionImage image= FirebaseVisionImage.fromBitmap(bitmap);
        FirebaseVisionTextDetector firebaseVisionTextDetector= FirebaseVision.getInstance().getVisionTextDetector();
        firebaseVisionTextDetector.detectInImage(image).addOnSuccessListener(new OnSuccessListener<FirebaseVisionText>() {
            @Override
            public void onSuccess(FirebaseVisionText firebaseVisionText) {
                    processText(firebaseVisionText);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });

    }

    private void processText(FirebaseVisionText firebaseVisionText) {
        String string ="";

        List<FirebaseVisionText.Block> blocks= firebaseVisionText.getBlocks();
        if(blocks.size()==0){

            Toast.makeText(getApplicationContext(),"no text could be read",Toast.LENGTH_LONG).show();
        }else{


            for(FirebaseVisionText.Block block : blocks){
                string+="\n"+block.getText();
            }
        }

        textView.setText(string);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    if(requestCode==RequestCode && resultCode==RESULT_OK){
        Bundle bundle = data.getExtras();

        bitmap=(Bitmap) bundle.get("data");

        }
    }
}
