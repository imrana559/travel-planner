package ba.unsa.pmf.planerputovanja;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.UUID;

public class PutovanjeActivity extends SingleFragmentActivity {

    private static final String EXTRA_PUTOVANJE_ID =
            "com.bignerdranch.android.planerputovanja.putovanje_id";
    public static Intent newIntent(Context packageContext, UUID putovanjeId) {
        Intent intent = new Intent(packageContext, PutovanjeActivity.class);
        intent.putExtra(EXTRA_PUTOVANJE_ID, putovanjeId);
        return intent;
    }

    @Override
    protected Fragment createFragment() {
        /*return new PutovanjeFragment();*/
        UUID putovanjeId = (UUID) getIntent()
                .getSerializableExtra(EXTRA_PUTOVANJE_ID);
        return PutovanjeFragment.newInstance(putovanjeId);

    }
}
