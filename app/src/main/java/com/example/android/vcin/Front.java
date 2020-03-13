package com.example.android.vcin;


import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.RequiresPermission;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.vcin.Models.Classification;
import com.example.android.vcin.Models.Classifier;
import com.example.android.vcin.Models.TensorClassifier;
import com.theartofdev.edmodo.cropper.CropImage;
import org.tensorflow.contrib.android.TensorFlowInferenceInterface;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.example.android.vcin.Models.TensorClassifier.*;


public class Front extends AppCompatActivity {
    Button cpt, prc;
    int check = 0;
    ImageView img;
    private Uri uri;
    static final int RESULT_LOAD_IMG = 2;
    final int PIC_CROP = 2;
    TextView tv;
    private static final int PIXEL_WIDTH = 28;
    private List<Classifier> mClassifiers = new ArrayList<>();
    private byte[] b;
    Uri resultUri;
    boolean cam = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_front);

        /*loadModel();*/
        img = (ImageView) findViewById(R.id.show_img);
        cpt = (Button) findViewById(R.id.cambtn);
        prc=(Button)findViewById(R.id.infobtn);
        tv=(TextView)findViewById(R.id.pred);
        cpt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cam = true;
                Opencam();
                LoadModel();


            }
        });
        prc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String outcome="";
                try {
                     b= getBytes(getApplicationContext(),resultUri);
                    TensorClassifier tc=new TensorClassifier();
                    tc.recognize(b);
                } catch (IOException e) {

                }
                for (Classifier classifier : mClassifiers) {
                    //perform classification on the image
                    final Classification res = classifier.recognize(b);
                    //if it can't classify, output a question mark
                    if (res.getLabel() == null) {
                        outcome += classifier.name() + ": ?\n";
                    } else {
                        //else output its name
                        outcome+= String.format("%s: %s, %f\n", classifier.name(), res.getLabel(),
                                res.getConf());
                    }
                }

                tv.setText(outcome);
            }
        });

       /* chg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cam=false;
                OpenGal();


            }
        });*/

    }
    public void LoadModel(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    //add 2 classifiers to our classifier arraylist
                    //the tensorflow classifier and the keras classifier
                    mClassifiers.add(
                            TensorClassifier.create(getAssets(), "TensorFlow",
                                    "classify_mnist_graph_def.pb", "labels.txt", PIXEL_WIDTH,
                                    "input", "output", true));

                } catch (final Exception e) {
                    //if they aren't found, throw an error!
                    throw new RuntimeException("Error initializing classifiers!", e);
                }
            }
        }).start();
    }




    private void Opencam() {
        CropImage.activity().start(this);
    }


    /*private void OpenGal() {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, RESULT_LOAD_IMG);

    }*/
    /*public void ImageCrop()
    {
        try {
        // call the standard crop action intent (the user device may not
        // support it)
        Intent cropIntent = new Intent("com.android.camera.action.CROP");
        // indicate image type and Uri
        cropIntent.setDataAndType(fileUri, "image/*");
        // set crop properties
        cropIntent.putExtra("crop", "true");
        // indicate aspect of desired crop
        cropIntent.putExtra("aspectX", 1);
        cropIntent.putExtra("aspectY", 1);
        // indicate output X and Y
        cropIntent.putExtra("outputX", 256);
        cropIntent.putExtra("outputY", 256);
        // retrieve data on return
        cropIntent.putExtra("return-data", true);
        // start the activity - we handle returning in onActivityResult
        startActivityForResult(cropIntent, CROP_PIC);
    }
    // respond to users whose devices do not support the crop action
    catch (ActivityNotFoundException anfe) {
        Toast toast = Toast
                .makeText(this, "This device doesn't support the crop action!", Toast.LENGTH_SHORT);
        toast.show();
    }
    }
*/
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (cam) {

            check = 1;
            if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
                CropImage.ActivityResult result = CropImage.getActivityResult(data);
                if (resultCode == RESULT_OK) {
                    resultUri = result.getUri();
                    img.setImageURI(resultUri);






                } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                    Exception error = result.getError();
                    Toast.makeText(this, "" + error, Toast.LENGTH_SHORT).show();
                }
            }
        }


       /* if(!cam){
            if (resultCode == RESULT_OK) {
                check=1;
                try {
                    final Uri imageUri = data.getData();
                    final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                    final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);

                    img.setImageBitmap(selectedImage);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG).show();
                }

            }else {
                Toast.makeText(this, "You haven't picked Image",Toast.LENGTH_LONG).show();
            }

        }
    }
}
*/
    }


}