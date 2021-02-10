package com.example.zensai;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.lang.ref.WeakReference;
import java.util.List;

public class DataAdapterCart extends RecyclerView.Adapter<DataAdapterCart.ViewHolder>{
    private List<DataModelCart> listitems;
    private Context context;
    private final ClickListener listener;
    String get_quantity, get_nama;

    public DataAdapterCart(List<DataModelCart> listitems, Context context, ClickListener listener) {
        this.listitems = listitems;
        this.context = context;
        this.listener = listener;
    }

    public interface ClickListener {
        void onPlusClicked(String nama);
        void onMinusClicked(String nama);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cart_customer,parent,false);

        return new ViewHolder(v, listener);
    }



    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        DataModelCart listitem = listitems.get(position);
        holder.lblnamamakanan_cart.setText(listitem.getNama());
        holder.lbldeskripsimakanan_cart.setText(listitem.getDeskripsi());
        holder.lblhargamakanan_cart.setText(listitem.getHarga());
        Picasso.get().load(listitem.getUrlgambar()).into(holder.imgmakanan_cart);
        holder.lblquantity_cart.setText(String.valueOf(listitem.getQuantity()));

    }

    @Override
    public int getItemCount() {
        return listitems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView lblnamamakanan_cart,lbldeskripsimakanan_cart,lblhargamakanan_cart, lblquantity_cart;
        public Button btnplus_cart, btnminus_cart;
        public ImageView imgmakanan_cart;
        private WeakReference<DataAdapterCart.ClickListener> listenerRef;

        public ViewHolder(@NonNull final View itemView, ClickListener listener) {
            super(itemView);

            listenerRef = new WeakReference<>(listener);
            lblnamamakanan_cart = (TextView) itemView.findViewById(R.id.lblnamamakanan_cart);
            lbldeskripsimakanan_cart = (TextView)itemView.findViewById(R.id.lbldeskripsimakanan_cart);
            lblhargamakanan_cart = (TextView)itemView.findViewById(R.id.lblhargamakanan_cart);
            imgmakanan_cart = (ImageView)itemView.findViewById(R.id.imgmakanan_cart);
            lblquantity_cart = (TextView)itemView.findViewById(R.id.lblquantity_cart);
            btnplus_cart = (Button)itemView.findViewById(R.id.btnplus_cart);
            btnminus_cart = (Button)itemView.findViewById(R.id.btnminus_cart);




            btnplus_cart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    get_quantity = ((TextView) itemView.findViewById(R.id.lblquantity_cart)).getText().toString();
                    get_nama = ((TextView) itemView.findViewById(R.id.lblnamamakanan_cart)).getText().toString();
                    listenerRef.get().onPlusClicked(get_nama);
                }
            });
            btnminus_cart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    get_quantity = ((TextView) itemView.findViewById(R.id.lblquantity_cart)).getText().toString();
                    get_nama = ((TextView) itemView.findViewById(R.id.lblnamamakanan_cart)).getText().toString();
                    listenerRef.get().onMinusClicked(get_nama);
                }
            });
        }



    }
}
