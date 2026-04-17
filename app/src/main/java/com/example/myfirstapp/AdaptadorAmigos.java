package com.example.myfirstapp;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class AdaptadorAmigos extends BaseAdapter {
    Context context;
    ArrayList<amigo> alAmigo;
    amigo misAmigo;
    LayoutInflater inflater;

    public AdaptadorAmigos(Context context, ArrayList<amigo> alAmigo) {
        this.context = context;
        this.alAmigo = alAmigo;
    }

    @Override
    public int getCount() {
        return alAmigo.size();
    }

    @Override
    public Object getItem(int position) {
        return alAmigo.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView = inflater.inflate(R.layout.fotos, parent, false);
        try{
            misAmigo = alAmigo.get(position);

            TextView tempVal = itemView.findViewById(R.id.lblNombreAdaptador);
            tempVal.setText(misAmigo.getNombre());

            tempVal = itemView.findViewById(R.id.lblTelefonoAdaptador);
            tempVal.setText(misAmigo.getTelefono());

            tempVal = itemView.findViewById(R.id.lblEmailAdaptador);
            tempVal.setText(misAmigo.getEmail());

            ImageView img = itemView.findViewById(R.id.imgFotoAdaptador);
            Bitmap bitmap = BitmapFactory.decodeFile(misAmigo.getFoto());
            img.setImageBitmap(bitmap);
        } catch (Exception e) {
            Toast.makeText(context, "Error: "+ e.getMessage(), Toast.LENGTH_LONG).show();
        }
        return itemView;
    }
}