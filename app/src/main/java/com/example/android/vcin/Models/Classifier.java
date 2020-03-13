package com.example.android.vcin.Models;

import android.net.Uri;

public interface Classifier {
    String name();
    Classification recognize(byte [] val);
}
