package com.e.heroaddandget;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.content.CursorLoader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import HeroAPI.HeroAPI;
import URL.BaseUrl;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HeroAPIActivity extends AppCompatActivity {

    EditText etName,etDesc;
    Button btnHero;
    ImageView imgheroImage;
    String imagePath;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hero_api);

        etName=findViewById(R.id.Etname);
        etDesc=findViewById(R.id.Etdesc);
        imgheroImage=findViewById(R.id.Imghero);

        btnHero=findViewById(R.id.Btnhero);

        btnHero.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Add();
            }
        });

        imgheroImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BrowserImage();
            }
        });



    }

//    private void StrictMode(){
//        android.os.StrictMode.ThreadPolicy policy=new android.os.StrictMode.ThreadPolicy.Builder().permitAll().build();
//        android.os.StrictMode.setThreadPolicy(policy);
//    }
//
//    private void loadFromURL() {
//    StrictMode();
//try {
//    String imgURl="https://www.google.com/url?sa=i&source=images&cd=&ved=2ahUKEwjD0fLbpq7iAhXzmeYKHYezD0UQjRx6BAgBEAU&url=https%3A%2F%2Fmetro.co.uk%2F2019%2F04%2F27%2Fcan-you-enjoy-avengers-endgame-if-you-havent-seen-any-marvel-films-9315078%2F&psig=AOvVaw16GmMGuCn6exlFrXmzrdgO&ust=1558585770915651";
//    URL url=new URL(imgURl);
//    imgheroImage.setImageBitmap(BitmapFactory.decodeStream((InputStream)url.getContent()));
//}catch (IOException e){
//    Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
//}
//    }

    private void Add() {

        String name=etName.getText().toString();
        String desc=etDesc.getText().toString();
        Retrofit retrofit=new Retrofit.Builder()
                .baseUrl(BaseUrl.Base_Url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        HeroAPI heroAPI=retrofit.create(HeroAPI.class);
        Call<Void> heroesCall=heroAPI.addHero(name,desc);

        heroesCall.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if(!response.isSuccessful()) {
                    Toast.makeText(HeroAPIActivity.this, "Code" + response.code(), Toast.LENGTH_SHORT).show();
                    return;
                }
                Toast.makeText(HeroAPIActivity.this, "Successfully Hero is added", Toast.LENGTH_SHORT).show();
                etName.setText("");
                etDesc.setText("");
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(HeroAPIActivity.this, "Error"+t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }


    private  void BrowserImage(){

        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent,0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode==RESULT_OK){
            if (data== null){
                Toast.makeText(this,"Please select an image",Toast.LENGTH_SHORT).show();
            }
        }


    Uri uri = data.getData();
    imagePath =getRealPathFromUri(uri);
    previewImage(imagePath);

    }

    private String getRealPathFromUri(Uri uri){
    String[] projection = {MediaStore.Images.Media.DATA};
        CursorLoader loader = new CursorLoader(getApplicationContext(),uri,projection,null,
                null,null);
        Cursor cursor = loader.loadInBackground();
        int colIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String result =cursor.getString(colIndex);
        cursor.close();
        return result;

    }

    private void previewImage(String imagePath){
        File imgFile = new File(imagePath);
        if (imgFile.exists())
        {
            Bitmap mybitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
            imgheroImage.setImageBitmap(mybitmap);
        }
    }


}
