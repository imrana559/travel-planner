package ba.unsa.pmf.planerputovanja;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ONamaActivity extends AppCompatActivity {
    private Button mONamaButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onama);

        mONamaButton = (Button) findViewById(R.id.go_button);
        mONamaButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent = new Intent(ONamaActivity.this, PocetnaActivity.class);
                startActivity(intent);
            }
        });
    }
}
