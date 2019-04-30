package com.rangel.dibujo;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.UUID;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    ImageButton negro, blanco, verde, azul, rojo, btnAniadir, btnTrazo, btnBorrar, btnfoto, btnrectangulo, btnGuardar;
    Lienzo lienzo;

    float ppequenio, pmediano, pgrande, pdefecto;
    private Bitmap bitmap = null;


public static final String TAG="MainActivity";

    public static final int MY_WRITE_EXTERNAL_STORAGE = 1;
    public static final int MY_PERMISSIONS_REQUEST_CAMERA = 2;

    public static final int PICK_IMAGE_REQUEST = 3;
    public static final int REQUEST_IMAGE_CAPTURE = 4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        negro = findViewById(R.id.btnNegro);
        blanco = findViewById(R.id.btnBlanco);
        verde = findViewById(R.id.btnVerde);
        azul = findViewById(R.id.btnAzul);
        rojo = findViewById(R.id.btnRojo);

        btnAniadir = findViewById(R.id.btnAniadir);
        btnTrazo = findViewById(R.id.btnTrazo);
        btnBorrar = findViewById(R.id.btnBorrar);
        btnGuardar = findViewById(R.id.btnGuardar);
        btnrectangulo = findViewById(R.id.btnrectangulo);
        btnfoto = findViewById(R.id.btnfoto);

        btnfoto.setOnClickListener(this);
        negro.setOnClickListener(this);
        blanco.setOnClickListener(this);
        verde.setOnClickListener(this);
        azul.setOnClickListener(this);
        rojo.setOnClickListener(this);
        btnAniadir.setOnClickListener(this);
        btnTrazo.setOnClickListener(this);
        btnBorrar.setOnClickListener(this);
        btnrectangulo.setOnClickListener(this);
        btnGuardar.setOnClickListener(this);

        lienzo = (Lienzo) findViewById(R.id.lienzo);
        ppequenio = 10;
        pmediano = 20;
        pgrande = 30;
        pdefecto = pmediano;

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.CAMERA)) {
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_WRITE_EXTERNAL_STORAGE);
            }
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.CAMERA)) {
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, MY_PERMISSIONS_REQUEST_CAMERA);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri filePath = data.getData();
            try {
//Cómo obtener el mapa de bits de la Galería
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);

                DisplayMetrics metrics = new DisplayMetrics();
                getWindowManager().getDefaultDisplay().getMetrics(metrics);
                int width = metrics.widthPixels; // ancho absoluto en pixels
                int height = metrics.heightPixels; // alto absoluto en pixels

                final int maxSize = width;
                int outWidth;
                int outHeight;
                int inWidth = bitmap.getWidth();
                int inHeight = bitmap.getHeight();
                if (inWidth > inHeight) {
                    outWidth = maxSize;
                    outHeight = (inHeight * maxSize) / inWidth;
                } else {
                    outHeight = maxSize;
                    outWidth = (inWidth * maxSize) / inHeight;
                }


                bitmap = Bitmap.createScaledBitmap(bitmap, outWidth, outHeight, true);

//Configuración del mapa de bits en ImageView
                lienzo.setBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");


            final int maxSize = 1200;
            int outWidth;
            int outHeight;
            int inWidth = imageBitmap.getWidth();
            int inHeight = imageBitmap.getHeight();
            if (inWidth > inHeight) {
                outWidth = maxSize;
                outHeight = (inHeight * maxSize) / inWidth;
            } else {
                outHeight = maxSize;
                outWidth = (inWidth * maxSize) / inHeight;
            }

            bitmap = Bitmap.createScaledBitmap(imageBitmap, outWidth, outHeight, true);

            if(bitmap != null) {
                Toast.makeText(this, "si es diferente de null", Toast.LENGTH_SHORT).show();
            }
            lienzo.setBitmap(imageBitmap);
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case MY_WRITE_EXTERNAL_STORAGE: {
                // If request is cancelled, the result arrays are empty.
                if (
                        grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                } else {
                }
                return;
            }
            case MY_PERMISSIONS_REQUEST_CAMERA: {
                // If request is cancelled, the result arrays are empty.
                if (
                        grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                } else {

                }
                return;
            }
        }
    }

    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Imagen"), PICK_IMAGE_REQUEST);
    }

    private void showFileChooserTomar() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    public void onClick(View v) {
        String color = "#009688";
        switch (v.getId()) {
            case R.id.btnNegro:
                color = v.getTag().toString();
                lienzo.setColor(color);
                break;
            case R.id.btnBlanco:
                color = v.getTag().toString();
                lienzo.setColor(color);
                break;
            case R.id.btnVerde:
                color = v.getTag().toString();
                lienzo.setColor(color);
                break;
            case R.id.btnAzul:
                color = v.getTag().toString();
                lienzo.setColor(color);
                break;
            case R.id.btnRojo:
                color = v.getTag().toString();
                lienzo.setColor(color);
                break;
            case R.id.btnAniadir:
                final AlertDialog.Builder newDialog = new AlertDialog.Builder(this);
                newDialog.setTitle("nuevo dibujo");
                newDialog.setMessage("¿Comenzar un nuevo dibujo (perderar el dibujo actual)?");
                newDialog.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        lienzo.NuevoDibujo();
                        dialog.dismiss();
                    }
                });
                newDialog.setNegativeButton("cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                newDialog.show();
                break;
            case R.id.btnTrazo:
                final Dialog brusDialog = new Dialog(this);
                brusDialog.setTitle("Tamaño del punto");
                brusDialog.setContentView(R.layout.tamanio_trazo);
                TextView smallBtn = (TextView) brusDialog.findViewById(R.id.tpequenio);
                smallBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        lienzo.setErase(false);
                        lienzo.setTamanioTrazo(ppequenio);
                        brusDialog.dismiss();
                    }
                });
                TextView mediumBtn = (TextView) brusDialog.findViewById(R.id.tmediano);
                mediumBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        lienzo.setErase(false);
                        lienzo.setTamanioTrazo(pmediano);
                        brusDialog.dismiss();
                    }
                });
                TextView largeBtn = (TextView) brusDialog.findViewById(R.id.tgrande);
                largeBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        lienzo.setErase(false);
                        lienzo.setTamanioTrazo(pgrande);
                        brusDialog.dismiss();
                    }
                });
                brusDialog.show();
                break;
            case R.id.btnBorrar:
                final Dialog eraseDialog = new Dialog(this);
                eraseDialog.setTitle("Tamaño de borrar");
                eraseDialog.setContentView(R.layout.tamanio_trazo);
                TextView smallBtnBorrar = (TextView) eraseDialog.findViewById(R.id.tpequenio);
                smallBtnBorrar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        lienzo.setErase(true);
                        lienzo.setTamanioTrazo(ppequenio);
                        eraseDialog.dismiss();
                    }
                });
                TextView mediumBtnBorrar = (TextView) eraseDialog.findViewById(R.id.tmediano);
                mediumBtnBorrar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        lienzo.setErase(true);
                        lienzo.setTamanioTrazo(pmediano);
                        eraseDialog.dismiss();
                    }
                });
                TextView largeBtnBorrar = (TextView) eraseDialog.findViewById(R.id.tgrande);
                largeBtnBorrar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        lienzo.setErase(true);
                        lienzo.setTamanioTrazo(pgrande);
                        eraseDialog.dismiss();
                    }
                });
                eraseDialog.show();
                break;

            case R.id.btnfoto:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                //Setting message manually and performing action on button click
                builder.setMessage("¿De donde quieres tomar la foto?")
                        .setCancelable(false)
                        .setPositiveButton("Desde el celular", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                showFileChooser();
                            }
                        })
                        .setNegativeButton("Tomar foto", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                //  Action for 'NO' Button
                                showFileChooserTomar();
                            }
                        });

                //Creating dialog box
                AlertDialog alert = builder.create();
                //Setting the title manually
                alert.setTitle("FOTO");
                alert.show();

                break;

            case R.id.btnGuardar:
                AlertDialog.Builder saveDibujo = new AlertDialog.Builder(this);
                saveDibujo.setTitle("Guardar dibujo");
                saveDibujo.setMessage("Salvar el dibujo en la galeria");
                saveDibujo.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        lienzo.setDrawingCacheEnabled(true);
                        String imgSaved = MediaStore.Images.Media.insertImage(getContentResolver(), lienzo.getDrawingCache(), UUID.randomUUID().toString() + ".png", "drawing");
                        Log.d(TAG, "aqui adol before: " + imgSaved);
                        if (imgSaved != null) {
                            Log.d(TAG, "aqui adol" + imgSaved);
                            Toast.makeText(MainActivity.this, "¡dibujo salvado correctamente en la galería!", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(MainActivity.this, "¡ERROR! La imagen no se ha podido guardar", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                saveDibujo.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                saveDibujo.show();
                break;
            case R.id.btnrectangulo:
                lienzo.rectangulo();
                break;
            default:
                break;
        }
    }
}
