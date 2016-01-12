package net.agusharyanto.datamahasiswa;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by agus on 10/15/15.
 */
public class MahasiswaArrayAdapter extends ArrayAdapter<Mahasiswa> {
    Context context;

    public MahasiswaArrayAdapter(Context context, int resourceId,
                                 List<Mahasiswa> items) {
        super(context, resourceId, items);
        this.context = context;
    }

    /*private view holder class*/
    private class ViewHolder {
        //ImageView imageView;
        TextView txtNIM;
        TextView txtNama;
        TextView txtJurusan;
     //   TextView txtNIM2;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        Mahasiswa rowItem = getItem(position);
        LayoutInflater mInflater = (LayoutInflater) context
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.rowmahasiswa, null);
            holder = new ViewHolder();
            holder.txtNIM = (TextView) convertView.findViewById(R.id.textViewRowNIM);
            holder.txtNama = (TextView) convertView.findViewById(R.id.textViewRowNama);
            holder.txtJurusan = (TextView) convertView.findViewById(R.id.textViewRowJurusan);
     //       holder.txtNIM2 = (TextView) convertView.findViewById(R.id.textViewNIm2);
            //holder.imageView = (ImageView) convertView.findViewById(R.id.icon);
            convertView.setTag(holder);
        } else
            holder = (ViewHolder) convertView.getTag();
        holder.txtNIM.setText(rowItem.getNim());
        holder.txtNama.setText(rowItem.getNama());
        holder.txtJurusan.setText(rowItem.getJurusan());

        //holder.imageView.setImageResource(rowItem.getImageId());
        return convertView;
    }

}
