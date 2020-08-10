package com.example.apphappyreminderz;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.work.Data;
import androidx.work.WorkManager;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {
    EditText EdtNombre, EdtApellido, EdtTelefono, EdtFechaNac, EdtEdad, EdtMensaje;
    TextView EdtFNot, EdtTiempo;
    Button BtnFNac, BtnFNot, BtnTiempo, BtnGuardar, BtnActivar, BtnCancelar, BtnMostrar;
    ImageView mImageView;

    final int REQUEST_CODE_GALLERY = 999;
    public static SQLiteHelper mSQLiteHelper;

    //CALCULAR EDAD
    Date txtFechaAct = new Date();
    int aa = 0;
    int ma = 0;
    int anio = 0, mes = 0, dia = 0;

    //CALENDAR
    Calendar actual = Calendar.getInstance();
    Calendar calendar = Calendar.getInstance();

    private int minuto, hora, diac, mesc, anioc;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Modificar actionbar
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("\uD83C\uDF89   Agregar Cumpleañero");

        //Texto
        EdtNombre = findViewById(R.id.edtMNombre);
        EdtApellido = findViewById(R.id.edtMApellido);
        EdtTelefono = findViewById(R.id.edtMTelefono);
        EdtFechaNac = findViewById(R.id.edtMFechaNac);
        EdtEdad = findViewById(R.id.edtMEdad);
        EdtMensaje = findViewById(R.id.edtMMensaje);
        EdtFNot = findViewById(R.id.txtMfechaNot);
        EdtTiempo = findViewById(R.id.txtMTiempoNot);

        //Button
        BtnFNac = findViewById(R.id.btnFechaNac);//
        BtnFNot = findViewById(R.id.btnFechaNot);//
        BtnTiempo = findViewById(R.id.btnTiempo);//
        BtnGuardar = findViewById(R.id.btnActualizar);
        BtnMostrar = findViewById(R.id.btnLista);
        BtnActivar = findViewById(R.id.btnActivar);//
        BtnCancelar = findViewById(R.id.btnCancelar);//

        //Imagen
        mImageView = findViewById(R.id.imageViewRecord);

        //CREANDO LA BASE DE DATOS
        mSQLiteHelper = new SQLiteHelper(this, "RECORDDBV2.sqlite", null, 1);

        //CREANDO LA TABLE DE LA BASE DE DATOS
        mSQLiteHelper.queryData("CREATE TABLE IF NOT EXISTS RECORDV2(id INTEGER PRIMARY KEY AUTOINCREMENT, apellido VARCHAR, nombre VARCHAR, telefono VARCHAR, fechaNac VARCHAR, edad VARCHAR, mensaje VARCHAR, fechaNot VARCHAR, tiempo VARCHAR, image BLOB)");

        // -----------------------------------------------------------------------

        mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityCompat.requestPermissions(
                        MainActivity.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        REQUEST_CODE_GALLERY
                );
            }
        });

        BtnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    mSQLiteHelper.insertData(
                            EdtApellido.getText().toString().trim(),
                            EdtNombre.getText().toString().trim(),
                            EdtTelefono.getText().toString().trim(),
                            EdtFechaNac.getText().toString().trim(),
                            EdtEdad.getText().toString().trim(),
                            EdtMensaje.getText().toString().trim(),
                            EdtFNot.getText().toString().trim(),
                            EdtTiempo.getText().toString().trim(),
                            imageViewToByte(mImageView)
                    );

                    Toast.makeText(MainActivity.this, "Guardado", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(MainActivity.this, RecordListActivity.class));
                    EdtApellido.setText("");
                    EdtNombre.setText("");
                    EdtTelefono.setText("");
                    EdtFechaNac.setText("");
                    EdtEdad.setText("");
                    EdtMensaje.setText("");
                    EdtFNot.setText("");
                    EdtTiempo.setText("");
                    mImageView.setImageResource(R.drawable.ic_addphoto);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        BtnMostrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, RecordListActivity.class));
            }
        });

        // -----------------------------------------------------------------------

        //ACTIVAR NOTIFICACION
        BtnFNot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                anioc = actual.get(Calendar.YEAR);
                mesc = actual.get(Calendar.MONTH);
                diac = actual.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(v.getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int y, int m, int d) {
                        calendar.set(Calendar.DAY_OF_MONTH, d);
                        calendar.set(Calendar.MONTH, m);
                        calendar.set(Calendar.YEAR, y);

                        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yy");
                        String strDate = format.format(calendar.getTime());
                        EdtFNot.setText(strDate);
                    }
                }, anioc, mesc, diac);
                datePickerDialog.show();
            }
        });

        BtnTiempo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hora = actual.get(Calendar.HOUR_OF_DAY);
                minuto = actual.get(Calendar.MINUTE);

                TimePickerDialog timePickerDialog = new TimePickerDialog(v.getContext(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int h, int m) {
                        calendar.set(Calendar.HOUR_OF_DAY, h);
                        calendar.set(Calendar.MINUTE, m);

                        EdtTiempo.setText(String.format("%20d:%02d", h, m));
                    }
                }, hora, minuto, true);
                timePickerDialog.show();
            }
        });

        BtnActivar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String tag = generateKey();
                long Alerttime = calendar.getTimeInMillis() - System.currentTimeMillis();
                int random = (int) (Math.random() * 50 + 1);

                Data data = guardarData("Notificacion", "soy un detalle", random);
                Notificacion.GuardarNotificacion(Alerttime, data, "tag1");
                Toast.makeText(MainActivity.this, "Alarma Guardada", Toast.LENGTH_SHORT).show();
            }
        });

        BtnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                eliminarNoti("tag1");
            }
        });
        //FIN DE NOTIFICACION

        //CALCULAR EDAD

        BtnFNac.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar ca = Calendar.getInstance();
                anio = ca.get(Calendar.YEAR);
                mes = ca.get(Calendar.MONTH);
                final int dia = ca.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog recogerFecha = new DatePickerDialog(MainActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int año, int mesi, int diaa) {
                        final int mesActual = mesi + 1;
                        String diaFormateado = (diaa < 10) ? "0" + String.valueOf(diaa) : String.valueOf(diaa);
                        String mesFormateado = (mesActual < 10) ? "0" + String.valueOf(mesActual) : String.valueOf(mesActual);
                        EdtFechaNac.setText("" + diaFormateado + "/" + mesFormateado + "/" + año);
                        aa = año;
                        ma = Integer.parseInt(mesFormateado);
                        EdtEdad.setText(calcular(anio, (mes + 1), aa, ma));
                    }
                }, anio, mes, dia);
                recogerFecha.show();
            }
        });
    }

    // --------------
    public static byte[] imageViewToByte(ImageView image) {
        Bitmap bitmap = ((BitmapDrawable) image.getDrawable()).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        return byteArray;
    }

    // --------------

    public String calcular(int a, int m, int aa, int ma) {
        int años = 0;
        int meses = 0;
        if (ma <= m) {
            años = a - aa;
            meses = m - ma;
        } else {
            años = a - aa - 1;
            meses = 12 - (ma - m);
        }
        return "" + años + " años";
    }
    //FIN CALCULAR EDAD

    //NOTIFICACION
    private void eliminarNoti(String tag) {
        WorkManager.getInstance(this).cancelAllWorkByTag(tag);
        Toast.makeText(MainActivity.this, "Alarma eliminada", Toast.LENGTH_SHORT).show();
    }

    private String generateKey() {
        return UUID.randomUUID().toString();
    }

    private Data guardarData(String titulo, String detalle, int id_noti) {
        return new Data.Builder()
                .putString("titulo", titulo)
                .putString("detalle", detalle)
                .putInt("id_noti", id_noti).build();
    }
    //FIN DE NOTIFICACION

    // ------------------------------------------------------------------------

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == REQUEST_CODE_GALLERY) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent, REQUEST_CODE_GALLERY);
            } else {
                Toast.makeText(this, "No tengo permiso para acceder a la ubicación del archivo.", Toast.LENGTH_SHORT).show();
            }
            return;
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if (requestCode == REQUEST_CODE_GALLERY && resultCode == RESULT_OK) {
            Uri imageUri = data.getData();
            CropImage.activity(imageUri)
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setAspectRatio(1, 1)
                    .start(this);
        }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();
                mImageView.setImageURI(resultUri);
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    // ------------------------------------------------------------------------
}