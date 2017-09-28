package ba.unsa.pmf.planerputovanja;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;

public class PopActivity extends Activity {

    private Button mOkButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pop);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int sirina = dm.widthPixels;
        int duzina = dm.heightPixels;

        getWindow().setLayout((int)(sirina*.8),(int)(duzina*.6));

        mOkButton = (Button) findViewById(R.id.okButton);
        mOkButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(new Intent(PopActivity.this, LokacijaActivity.class));
            }

        });
    }
}

