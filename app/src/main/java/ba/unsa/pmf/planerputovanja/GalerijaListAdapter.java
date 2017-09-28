package ba.unsa.pmf.planerputovanja;

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

public class GalerijaListAdapter extends BaseAdapter {
    private Context context;
    private  int layout;
    private ArrayList<Galerija> galerijaList;

    public GalerijaListAdapter(Context context, int layout, ArrayList<Galerija> foodsList) {
        this.context = context;
        this.layout = layout;
        this.galerijaList = foodsList;
    }

    @Override
    public int getCount() {
        return galerijaList.size();
    }

    @Override
    public Object getItem(int position) {
        return galerijaList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private class ViewHolder{
        ImageView imageView;
        TextView txtIme, txtOpis;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {

        View row = view;
        ViewHolder holder = new ViewHolder();

        if(row == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(layout, null);

            holder.txtIme = (TextView) row.findViewById(R.id.txtIme);
            holder.txtOpis = (TextView) row.findViewById(R.id.txtOpis);
            holder.imageView = (ImageView) row.findViewById(R.id.imgGalerija);
            row.setTag(holder);
        }
        else {
            holder = (ViewHolder) row.getTag();
        }

        Galerija galerija = galerijaList.get(position);

        holder.txtIme.setText(galerija.getName());
        holder.txtOpis.setText(galerija.getPrice());

        byte[] galerijaImage = galerija.getImage();
        Bitmap bitmap = BitmapFactory.decodeByteArray(galerijaImage, 0, galerijaImage.length);
        holder.imageView.setImageBitmap(bitmap);

        return row;
    }
}
