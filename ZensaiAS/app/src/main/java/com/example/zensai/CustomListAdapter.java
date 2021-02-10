package com.example.zensai;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.PicassoProvider;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class CustomListAdapter extends ArrayAdapter<IklanList> {
    ArrayList<IklanList> iklanl;
    Context context;;
    int resource;
    public CustomListAdapter(@NonNull Context context, int resource, @NonNull ArrayList<IklanList> iklanl) {
        super(context, resource, iklanl);
        this.iklanl = iklanl;
        this.context = context;
        this.resource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if(convertView == null){
            LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.list_check_iklan, null, true);

        }
        IklanList iklan = getItem(position);
//        ImageView img = (ImageView) convertView.findViewById(R.id.imgiklan_cekiklan);
        new DownloadImageTask((ImageView) convertView.findViewById(R.id.imgiklan_cekiklan)).execute(iklan.getImage());
//        Picasso.get().load(iklan.getImage()).into(img);
        TextView txt = (TextView) convertView.findViewById(R.id.keterangan_cekiklan);
        txt.setText(iklan.getKeterangan());
        return convertView;
    }
    public class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }
}
