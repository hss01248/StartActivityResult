package com.hss01248.transfrag;

import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public interface IEventCallback {

    void onActivityResult(int requestCode, int resultCode, @Nullable Intent data);

    void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults);

    void onStart();

    void onResume();

    void onFirstResume();

    void onPause();

    void onDestroy();


    void onStop();
}
