package com.example.android.vcin.Models;

import android.content.Context;
import android.content.res.AssetManager;
import android.net.Uri;

import org.tensorflow.contrib.android.TensorFlowInferenceInterface;

import com.example.android.vcin.Front;
import com.example.android.vcin.Front.*;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;

public class TensorClassifier implements Classifier{

        private static final float THRESHOLD = 0.1f;

        private TensorFlowInferenceInterface tfHelper;
        private String name;
        private String inputName;
        private String outputName;
        private int inputSize;
        private boolean feedKeepProb;
        private List<String> labels;
        private float[] output;
        private String[] outputNames;

    private static List<String> readLabels(AssetManager am, String fileName) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(am.open(fileName)));

        String line;
        List<String> labels = new ArrayList<>();
        while ((line = br.readLine()) != null) {
            labels.add(line);
        }

        br.close();
        return labels;
    }
    public static TensorClassifier create(AssetManager assetManager, String name,
                                              String modelPath, String labelFile, int inputSize, String inputName, String outputName,
                                              boolean feedKeepProb) throws IOException {
        //intialize a classifier
        TensorClassifier c = new TensorClassifier();

        //store its name, input and output labels
        c.name = name;

        c.inputName = inputName;
        c.outputName = outputName;

        //read labels for label file
        c.labels = readLabels(assetManager, labelFile);

        //set its model path and where the raw asset files are
        c.tfHelper = new TensorFlowInferenceInterface(assetManager, modelPath);
        int numClasses = 10;

        //how big is the input?
        c.inputSize = inputSize;

        // Pre-allocate buffer.
        c.outputNames = new String[] { outputName };

        c.outputName = outputName;
        c.output = new float[numClasses];

        c.feedKeepProb = feedKeepProb;

        return c;
    }

    public static byte[] getBytes(Context context, Uri uri) throws IOException {
        InputStream iStream = context.getContentResolver().openInputStream(uri);
        try {
            return getBytes(iStream);
        } finally {
            // close the stream
            try {
                iStream.close();
            } catch (IOException ignored) { /* do nothing */ }
        }
    }
    public static byte[] getBytes(InputStream inputStream) throws IOException {

        byte[] bytesResult = null;
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];
        try {
            int len;
            while ((len = inputStream.read(buffer)) != -1) {
                byteBuffer.write(buffer, 0, len);
            }
            bytesResult = byteBuffer.toByteArray();
        } finally {
            // close the stream
            try{ byteBuffer.close(); } catch (IOException ignored){ /* do nothing */ }
        }
        return bytesResult;
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public Classification recognize(byte[] val) {
        tfHelper.feed(inputName,val,1,inputSize,inputSize,1);
        Classification ans = new Classification();
        if (feedKeepProb) {
            tfHelper.feed("keep_prob", new byte[] { 1 });
            tfHelper.run(outputNames);
            tfHelper.fetch(outputName, output);
            for (int i = 0; i < output.length; ++i) {
                System.out.println(output[i]);
                System.out.println(labels.get(i));
                if (output[i] > THRESHOLD && output[i] > ans.getConf()) {
                    ans.update(output[i], labels.get(i));
                }
            }

        }
       return ans;
    }
}
