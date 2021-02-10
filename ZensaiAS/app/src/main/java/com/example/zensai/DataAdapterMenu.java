package com.example.zensai;

import android.app.LauncherActivity;
import android.content.Context;
import android.content.Intent;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataAdapterMenu extends RecyclerView.Adapter<DataAdapterMenu.ViewHolder> {

    private List<DataModelMenu> listitems;
    private Context context;
    private final ClickListener listener;
    String get_nama;

    public DataAdapterMenu(List<DataModelMenu> listitems, Context context, ClickListener listener) {
        this.listitems = listitems;
        this.context = context;
        this.listener = listener;
    }

    public interface ClickListener {
        void onButtonClicked(String nama);
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.menu_customer,parent,false);

        return new ViewHolder(v, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        DataModelMenu listitem = listitems.get(position);

        holder.lblnamamakanan.setText(listitem.getNama());
        holder.lbldeskripsimakanan.setText(listitem.getDeskripsi());
        holder.lblhargamakanan.setText(listitem.getHarga());

        Picasso.get().load(listitem.getUrlgambar()).into(holder.imgmakanan);

    }

    @Override
    public int getItemCount() {
        return listitems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public TextView lblnamamakanan,lbldeskripsimakanan,lblhargamakanan;
        public Button btnaddcart;
        public ImageView imgmakanan;
        private WeakReference<ClickListener> listenerRef;

        public ViewHolder(@NonNull View itemView, ClickListener listener) {
            super(itemView);

            listenerRef = new WeakReference<>(listener);
            lblnamamakanan = (TextView) itemView.findViewById(R.id.lblnamamakanan);
            lbldeskripsimakanan = (TextView)itemView.findViewById(R.id.lbldeskripsimakanan);
            lblhargamakanan = (TextView)itemView.findViewById(R.id.lblhargamakanan);
            imgmakanan = (ImageView)itemView.findViewById(R.id.imgmakanan);
            btnaddcart = (Button)itemView.findViewById(R.id.btnaddcart);

            btnaddcart.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

            get_nama = ((TextView) itemView.findViewById(R.id.lblnamamakanan)).getText().toString();
            listenerRef.get().onButtonClicked(get_nama);

        }


    }



}
