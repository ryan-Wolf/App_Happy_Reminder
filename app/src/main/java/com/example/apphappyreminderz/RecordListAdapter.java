package com.example.apphappyreminderz;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class RecordListAdapter extends BaseAdapter {

    private Context context;
    private int layout;
    private ArrayList<Model> recordList;

    public RecordListAdapter(Context context, int layout, ArrayList<Model> recordList) {
        this.context = context;
        this.layout = layout;
        this.recordList = recordList;
    }

    @Override
    public int getCount() {
        return recordList.size();
    }

    @Override
    public Object getItem(int i) {
        return recordList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    private class ViewHolder {
        ImageView imageView;
        TextView txtApellido, txtNombre, txtTelefono, txtFechaNac, txtEdad, txtMensaje, txtFechaNot, txtTiempo;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View row = view;
        ViewHolder holder = new ViewHolder();
        if (row == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(layout, null);
            holder.txtApellido = row.findViewById(R.id.txtApellido);
            holder.txtNombre = row.findViewById(R.id.txtNombre);
            holder.txtTelefono = row.findViewById(R.id.txtTelefono);
            holder.txtFechaNac = row.findViewById(R.id.txtFecha);
            holder.txtEdad = row.findViewById(R.id.txtEdad);
            holder.txtMensaje = row.findViewById(R.id.txtMensaje);
            holder.txtFechaNot = row.findViewById(R.id.txtFechaNoti);
            holder.txtTiempo = row.findViewById(R.id.txtTiempo);
            holder.imageView = row.findViewById(R.id.imgIcon);
            row.setTag(holder);
        } else {
            holder = (ViewHolder) row.getTag();
        }

        Model model = recordList.get(i);

        holder.txtApellido.setText(model.getApellido());
        holder.txtNombre.setText(model.getNombre());
        holder.txtTelefono.setText(model.getTelefono());
        holder.txtFechaNac.setText(model.getFechaNac());
        holder.txtEdad.setText(model.getEdad());
        holder.txtMensaje.setText(model.getMensaje());
        holder.txtFechaNot.setText(model.getFechaNot());
        holder.txtTiempo.setText(model.getTiempo());

        byte[] recordImage=model.getImage();
        Bitmap bitmap = BitmapFactory.decodeByteArray(recordImage, 0, recordImage.length);
        holder.imageView.setImageBitmap(bitmap);
        return row;
    }
}
