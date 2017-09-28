package ba.unsa.pmf.planerputovanja;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;

import ba.unsa.pmf.planerputovanja.database.GalerijaBaseHelper;

public class GalerijaActivity extends AppCompatActivity {
    EditText mIme, mOpis;
    Button mIzaberiButton, mDodajButton, mListButton, mBackButton;
    ImageView imageView;

    final int REQUEST_CODE_GALLERY = 999;

    public static GalerijaBaseHelper sqLiteHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_galerija);

        init();

        sqLiteHelper = new GalerijaBaseHelper(this, "GalerijaDB.sqlite", null, 1);

        sqLiteHelper.queryData("CREATE TABLE IF NOT EXISTS GALERIJA(Id INTEGER PRIMARY KEY AUTOINCREMENT, ime VARCHAR, opis VARCHAR, slika BLOB)");

        mIzaberiButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityCompat.requestPermissions(
                        GalerijaActivity.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        REQUEST_CODE_GALLERY
                );
            }
        });

        mDodajButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    if((mIme.getText().toString() != null) || (mOpis.getText().toString() != null))
                    {
                        sqLiteHelper.insertData(
                                mIme.getText().toString().trim(),
                                mOpis.getText().toString().trim(),
                                imageViewToByte(imageView)
                        );
                        Toast.makeText(getApplicationContext(), "Uspješno dodano!", Toast.LENGTH_SHORT).show();
                    }
                    else Toast.makeText(getApplicationContext(), "Pokušajte ponovo!", Toast.LENGTH_SHORT).show();

                    mIme.setText("");
                    mOpis.setText("");
                    imageView.setImageResource(R.drawable.image_icon);
                }
                catch (Exception e){
                    Toast.makeText(getApplicationContext(), "Pokušajte ponovo!", Toast.LENGTH_SHORT).show();
                    /*e.printStackTrace();*/
                }
            }
        });

        mListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(GalerijaActivity.this, GalerijaList.class);
                startActivity(intent);
            }
        });

        mBackButton = (Button) findViewById(R.id.btnBack);
        mBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(GalerijaActivity.this, PocetnaActivity.class);
                startActivity(intent);
            }
        });
    }

    public static byte[] imageViewToByte(ImageView image) {
        Bitmap bitmap = ((BitmapDrawable)image.getDrawable()).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        return byteArray;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if(requestCode == REQUEST_CODE_GALLERY){
            if(grantResults.length >0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, REQUEST_CODE_GALLERY);
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

        if(requestCode == REQUEST_CODE_GALLERY && resultCode == RESULT_OK && data != null){
            Uri uri = data.getData();

            try {
                InputStream inputStream = getContentResolver().openInputStream(uri);

                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                imageView.setImageBitmap(bitmap);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    private void init(){
        mIme = (EditText) findViewById(R.id.edtIme);
        mOpis = (EditText) findViewById(R.id.edtOpis);
        mIzaberiButton = (Button) findViewById(R.id.btnIzbor);
        mDodajButton = (Button) findViewById(R.id.btnDodaj);
        mListButton = (Button) findViewById(R.id.btnList);
        imageView = (ImageView) findViewById(R.id.imageView);
    }
}

