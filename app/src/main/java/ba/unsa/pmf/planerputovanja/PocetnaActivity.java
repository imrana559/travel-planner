package ba.unsa.pmf.planerputovanja;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.oguzdev.circularfloatingactionmenu.library.FloatingActionButton;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionMenu;
import com.oguzdev.circularfloatingactionmenu.library.SubActionButton;

import java.util.UUID;

public class PocetnaActivity extends AppCompatActivity {
    private Button mAboutUs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pocetna);

        ImageView icon = new ImageView(this);
        icon.setImageResource(R.drawable.go_button);
        final FloatingActionButton fab = new FloatingActionButton.Builder(this).setContentView(icon).build();

        SubActionButton.Builder builder = new SubActionButton.Builder(this);
        FloatingActionButton.LayoutParams params=new FloatingActionButton.LayoutParams(130,130);
        builder.setLayoutParams(params);

        ImageView galleryIcon = new ImageView(this);
        galleryIcon.setImageResource(R.drawable.gallery_icon);
        SubActionButton galleryBtn = builder.setContentView(galleryIcon).build();

        ImageView toDoList = new ImageView(this);
        toDoList.setImageResource(R.drawable.to_do_list);
        SubActionButton toDoBtn = builder.setContentView(toDoList).build();

        ImageView plannerIcon = new ImageView(this);
        plannerIcon.setImageResource(R.drawable.planner_icon);
        SubActionButton plannerBtn = builder.setContentView(plannerIcon).build();

        final FloatingActionMenu fam = new FloatingActionMenu.Builder(this)
                .addSubActionView(galleryBtn)
                .addSubActionView(toDoBtn)
                .addSubActionView(plannerBtn)
                .attachTo(fab)
                .build();


        toDoBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent = new Intent(PocetnaActivity.this, ToDoListActivity.class);
                startActivity(intent);
            }
        });

        plannerBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent = new Intent(PocetnaActivity.this, PutovanjeListActivity.class);
                startActivity(intent);
            }
        });

        galleryBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent = new Intent(PocetnaActivity.this, GalerijaActivity.class);
                startActivity(intent);
            }
        });

        mAboutUs = (Button) findViewById(R.id.o_nama);
        mAboutUs.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent = new Intent(PocetnaActivity.this, ONamaActivity.class);
                startActivity(intent);
            }
        });
    }

    public void clickExit(View view) {
        askToClose();
    }

    @Override
    public void onBackPressed() {
        askToClose();
    }

    private void askToClose (){
        AlertDialog.Builder builder = new AlertDialog.Builder(PocetnaActivity.this);
        builder.setMessage("Da li sigurno želite napustiti Planer putovanja?");
        builder.setCancelable(true);
        builder.setPositiveButton("Da", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                finish();
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_HOME);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
        builder.setNegativeButton("Ne", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();

            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }
}
