package ba.unsa.pmf.planerputovanja;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;

public class GalerijaList extends AppCompatActivity {

    GridView gridView;
    ArrayList<Galerija> list;
    GalerijaListAdapter adapter = null;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_galerija_list);

        gridView = (GridView) findViewById(R.id.gridView);
        list = new ArrayList<>();
        adapter = new GalerijaListAdapter(this, R.layout.galerija_items, list);
        gridView.setAdapter(adapter);

        //Dohvatimo sve podatke iz baze
        Cursor cursor = GalerijaActivity.sqLiteHelper.getData("SELECT * FROM GALERIJA");
        list.clear();
        while (cursor.moveToNext()) {
            int id = cursor.getInt(0);
            String ime = cursor.getString(1);
            String opis = cursor.getString(2);
            byte[] slika = cursor.getBlob(3);

            list.add(new Galerija(ime, opis, slika, id));
        }
        adapter.notifyDataSetChanged();

        gridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {

                CharSequence[] items = {"Uredi", "Izbriši"};
                AlertDialog.Builder dialog = new AlertDialog.Builder(GalerijaList.this);

                dialog.setTitle("Izaberite akciju:");
                dialog.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int item) {
                        if (item == 0) {
                            //Azuriranje
                            Cursor c = GalerijaActivity.sqLiteHelper.getData("SELECT id FROM GALERIJA");
                            ArrayList<Integer> arrID = new ArrayList<Integer>();
                            while (c.moveToNext()){
                                arrID.add(c.getInt(0));
                            }
                            showDialogUpdate(GalerijaList.this, arrID.get(position));

                        } else {
                            //Brisanje
                            Cursor c = GalerijaActivity.sqLiteHelper.getData("SELECT id FROM GALERIJA");
                            ArrayList<Integer> arrID = new ArrayList<Integer>();
                            while (c.moveToNext()){
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

    ImageView imageViewPutovanje;
    private void showDialogUpdate(Activity activity, final int position){

        final Dialog dialog = new Dialog(activity);
        dialog.setContentView(R.layout.activity_update_galerija);
        dialog.setTitle("Uredi");

        imageViewPutovanje = (ImageView) dialog.findViewById(R.id.imageViewGalerija);
        final EditText edtIme = (EditText) dialog.findViewById(R.id.edtIme);
        final EditText edtOpis = (EditText) dialog.findViewById(R.id.edtOpis);
        Button btnUpdate = (Button) dialog.findViewById(R.id.btnUpdate);

        //Postavljanje sirine za dialog
        int width = (int) (activity.getResources().getDisplayMetrics().widthPixels * 0.95);
        //Postavljanje visine za dialog
        int height = (int) (activity.getResources().getDisplayMetrics().heightPixels * 0.7);
        dialog.getWindow().setLayout(width, height);
        dialog.show();

        imageViewPutovanje.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Zahtjev za galeriju
                ActivityCompat.requestPermissions(
                        GalerijaList.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        888
                );
            }
        });

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    GalerijaActivity.sqLiteHelper.updateData(
                            edtIme.getText().toString().trim(),
                            edtOpis.getText().toString().trim(),
                            GalerijaActivity.imageViewToByte(imageViewPutovanje),
                            position
                    );
                    dialog.dismiss();
                    Toast.makeText(getApplicationContext(), "Izmjene su spremljene!",Toast.LENGTH_SHORT).show();
                }
                catch (Exception error) {
                    Log.e("Uredi error", error.getMessage());
                }
                updateGalerijaList();
            }
        });
    }

    private void showDialogDelete(final int idGalerija){
        final AlertDialog.Builder dialogDelete = new AlertDialog.Builder(GalerijaList.this);

        dialogDelete.setTitle("Upozorenje!");
        dialogDelete.setMessage("Da li sigurno želite izbrisati fotografiju?");
        dialogDelete.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                try {
                    GalerijaActivity.sqLiteHelper.deleteData(idGalerija);
                    Toast.makeText(getApplicationContext(), "Uspješno izbrisano!",Toast.LENGTH_SHORT).show();
                } catch (Exception e){
                    Log.e("error", e.getMessage());
                }
                updateGalerijaList();
            }
        });

        dialogDelete.setNegativeButton("Odustani", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        dialogDelete.show();
    }

    private void updateGalerijaList(){
        //Dohvati sve podatke iz galerije
        Cursor cursor = GalerijaActivity.sqLiteHelper.getData("SELECT * FROM GALERIJA");
        list.clear();
        while (cursor.moveToNext()) {
            int id = cursor.getInt(0);
            String ime = cursor.getString(1);
            String opis = cursor.getString(2);
            byte[] slika = cursor.getBlob(3);

            list.add(new Galerija(ime, opis, slika, id));
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if(requestCode == 888){
            if(grantResults.length >0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, 888);
            }
            else {
                Toast.makeText(getApplicationContext(), "Nemate dozvolu za pristup lokaciji datoteke!", Toast.LENGTH_SHORT).show();
            }
            return;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(requestCode == 888 && resultCode == RESULT_OK && data != null){
            Uri uri = data.getData();
            try {
                InputStream inputStream = getContentResolver().openInputStream(uri);
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                imageViewPutovanje.setImageBitmap(bitmap);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }
}
