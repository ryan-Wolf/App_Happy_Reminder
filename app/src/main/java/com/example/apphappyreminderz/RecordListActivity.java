package com.example.apphappyreminderz;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class RecordListActivity extends AppCompatActivity {

    ListView mListView;
    ArrayList<Model> mList;
    RecordListAdapter mAdapter = null;

    ImageView imageViewIcon;

    FloatingActionButton btn_cambiar, btn_mapa, btn_compartir;

    Date txtFechaAct = new Date();
    int aa = 0;
    int ma = 0;
    int anio = 0, mes = 0, dia = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_list);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("\uD83C\uDF81 Lista de Cumpleañeros");

        btn_cambiar = findViewById(R.id.BtnFloat);
        btn_mapa = findViewById(R.id.btnMapa);
        btn_compartir = findViewById(R.id.btncompartir);

        btn_compartir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CompartirApp();
            }
        });

        btn_mapa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RecordListActivity.this, MapsActivity.class));
            }
        });

        btn_cambiar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RecordListActivity.this, MainActivity.class));

            }
        });

        mListView = findViewById(R.id.listView);
        mList = new ArrayList<>();
        mAdapter = new RecordListAdapter(this, R.layout.row, mList);
        mListView.setAdapter(mAdapter);

        Cursor cursor = MainActivity.mSQLiteHelper.getData("SELECT * FROM RECORDV2");
        mList.clear();

        while (cursor.moveToNext()) {
            int id = cursor.getInt(0);
            String apellido = cursor.getString(1);
            String nombre = cursor.getString(2);
            String telefono = cursor.getString(3);
            String fechaNac = cursor.getString(4);
            String edad = cursor.getString(5);
            String mensaje = cursor.getString(6);
            String fechaNot = cursor.getString(7);
            String tiempo = cursor.getString(8);
            byte[] image = cursor.getBlob(9);

            mList.add(new Model(id, apellido, nombre, telefono, fechaNac, edad, mensaje, fechaNot, tiempo, image));
        }

        mAdapter.notifyDataSetChanged();

        if (mList.size() == 0) {
            Toast.makeText(this, "Ningun record fue encontrado", Toast.LENGTH_SHORT).show();
        }

        mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int position, long l) {
                CharSequence[] items = {"Actualizar", "Eliminar"};

                AlertDialog.Builder dialog = new AlertDialog.Builder(RecordListActivity.this);
                dialog.setTitle("Operación");
                dialog.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (i == 0) {
                            Cursor c = MainActivity.mSQLiteHelper.getData("SELECT id FROM RECORDV2");
                            ArrayList<Integer> arrID = new ArrayList<Integer>();
                            while (c.moveToNext()) {
                                arrID.add(c.getInt(0));
                            }
                            showDialogUpdate(RecordListActivity.this, arrID.get(position));
                        }

                        if (i == 1) {
                            Cursor c = MainActivity.mSQLiteHelper.getData("SELECT id FROM RECORDV2");
                            ArrayList<Integer> arrID = new ArrayList<Integer>();
                            while (c.moveToNext()) {
                                arrID.add(c.getInt(0));
                            }

                            showDialogDelete(arrID.get(position));
                        }
                    }
                });
                dialog.show();
                return true;
            }
        });
    }

    private void CompartirApp() {
        try {
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("text/plain");
            intent.putExtra(Intent.EXTRA_SUBJECT, getResources().getString(R.string.app_name));
            String aux = "Descarga la App \uD83D\uDCEC \n";
            aux = aux + "https://play.google.com/store/apps/details?id=com.apputilose.teo.birthdayremember&hl=es_419" + getBaseContext().getPackageName();
            intent.putExtra(Intent.EXTRA_TEXT, aux);
            startActivity(intent);
        } catch (Exception e) {

        }
    }

    private void showDialogDelete(final int idRecord) {
        AlertDialog.Builder dialogDelete = new AlertDialog.Builder(RecordListActivity.this);
        dialogDelete.setTitle("Alerta!");
        dialogDelete.setMessage("Seguro que desea eliminar?");
        dialogDelete.setPositiveButton("Si", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                try {
                    MainActivity.mSQLiteHelper.deleteData(idRecord);
                    Toast.makeText(RecordListActivity.this, "Se elimino", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    Log.e("error", e.getMessage());
                }
                updateRecordList();
            }
        });
        dialogDelete.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        dialogDelete.show();
    }

    private void showDialogUpdate(Activity activity, final int position) {
        final Dialog dialog = new Dialog(activity);
        dialog.setContentView(R.layout.update_dialog);
        dialog.setTitle("Actualizar");

        imageViewIcon = dialog.findViewById(R.id.imageViewRecord);
        final EditText edtApellido = dialog.findViewById(R.id.edtMApellido);
        final EditText edtNombre = dialog.findViewById(R.id.edtMNombre);
        final EditText edtTelefono = dialog.findViewById(R.id.edtMTelefono);
        final EditText edtFechaNac = dialog.findViewById(R.id.edtMFechaNac);
        final EditText edtEdad = dialog.findViewById(R.id.edtMEdad);
        final EditText edtMensaje = dialog.findViewById(R.id.edtMMensaje);
        final TextView edtFechaNoti = dialog.findViewById(R.id.txtMfechaNot);
        final TextView edtTiempo = dialog.findViewById(R.id.txtMTiempoNot);
        Button btnActualizar = dialog.findViewById(R.id.btnActualizar);
        Button btnFecha = dialog.findViewById(R.id.btnFechaNac);

        // --
        Cursor cursor = MainActivity.mSQLiteHelper.getData("SELECT * FROM RECORDV2 WHERE id=" + position);
        mList.clear();

        while (cursor.moveToNext()) {
            int id = cursor.getInt(0);
            String apellido = cursor.getString(1);
            edtApellido.setText(apellido);

            String nombre = cursor.getString(2);
            edtNombre.setText(nombre);

            String telefono = cursor.getString(3);
            edtTelefono.setText(telefono);

            String fechaNac = cursor.getString(4);
            edtFechaNac.setText(fechaNac);

            String edad = cursor.getString(5);
            edtEdad.setText(edad);

            String mensaje = cursor.getString(6);
            edtMensaje.setText(mensaje);

            String fechaNot = cursor.getString(7);
            edtFechaNoti.setText(fechaNot);

            String tiempo = cursor.getString(8);
            edtTiempo.setText(tiempo);

            byte[] image = cursor.getBlob(9);
            imageViewIcon.setImageBitmap(BitmapFactory.decodeByteArray(image, 0, image.length));

            mList.add(new Model(id, apellido, nombre, telefono, fechaNac, edad, mensaje, fechaNot, tiempo, image));
        }
        // --

        int width = (int) (activity.getResources().getDisplayMetrics().widthPixels * 0.95);
        int height = (int) (activity.getResources().getDisplayMetrics().heightPixels * 0.6);
        dialog.getWindow().setLayout(width, height);
        dialog.show();

        imageViewIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityCompat.requestPermissions(
                        RecordListActivity.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        888
                );
            }
        });

        btnActualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    MainActivity.mSQLiteHelper.updateData(
                            edtApellido.getText().toString().trim(),
                            edtNombre.getText().toString().trim(),
                            edtTelefono.getText().toString().trim(),
                            edtFechaNac.getText().toString().trim(),
                            edtEdad.getText().toString().trim(),
                            edtMensaje.getText().toString().trim(),
                            edtFechaNoti.getText().toString().trim(),
                            edtTiempo.getText().toString().trim(),
                            MainActivity.imageViewToByte(imageViewIcon),
                            position
                    );
                    dialog.dismiss();
                    Toast.makeText(getApplicationContext(), "Actualización exitosa", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    Log.e("Update error ", e.getMessage());
                }

                updateRecordList();
            }
        });

        btnFecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar ca = Calendar.getInstance();
                anio = ca.get(Calendar.YEAR);
                mes = ca.get(Calendar.MONTH);
                final int dia = ca.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog recogerFecha = new DatePickerDialog(view.getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int año, int mesi, int diaa) {
                        final int mesActual = mesi + 1;
                        String diaFormateado = (diaa < 10) ? "0" + String.valueOf(diaa) : String.valueOf(diaa);
                        String mesFormateado = (mesActual < 10) ? "0" + String.valueOf(mesActual) : String.valueOf(mesActual);
                        edtFechaNac.setText("" + diaFormateado + "/" + mesFormateado + "/" + año);
                        aa = año;
                        ma = Integer.parseInt(mesFormateado);
                        edtEdad.setText(calcular(anio, (mes + 1), aa, ma));
                    }
                }, anio, mes, dia);
                recogerFecha.show();
            }
        });
    }

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

    private void updateRecordList() {
        Cursor cursor = MainActivity.mSQLiteHelper.getData("SELECT * FROM RECORDV2");
        mList.clear();
        while (cursor.moveToNext()) {
            int id = cursor.getInt(0);
            String apellido = cursor.getString(1);
            String nombre = cursor.getString(2);
            String telefono = cursor.getString(3);
            String fechaNac = cursor.getString(4);
            String edad = cursor.getString(5);
            String mensaje = cursor.getString(6);
            String fechaNoti = cursor.getString(7);
            String tiempo = cursor.getString(8);
            byte[] image = cursor.getBlob(9);

            mList.add(new Model(id, apellido, nombre, telefono, fechaNac, edad, mensaje, fechaNoti, tiempo, image));
        }
        mAdapter.notifyDataSetChanged();
    }

    public static byte[] imageViewToByte(ImageView image) {
        Bitmap bitmap = ((BitmapDrawable) image.getDrawable()).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        return byteArray;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == 888) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent, 888);
            } else {
                Toast.makeText(this, "No tengo permiso para acceder a la ubicación del archivo.", Toast.LENGTH_SHORT).show();
            }
            return;
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if (requestCode == 888 && resultCode == RESULT_OK) {
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
                imageViewIcon.setImageURI(resultUri);
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}