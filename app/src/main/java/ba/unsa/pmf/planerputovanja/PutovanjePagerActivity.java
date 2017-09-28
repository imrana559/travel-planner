package ba.unsa.pmf.planerputovanja;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.NavUtils;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import java.util.List;
import java.util.UUID;

public class PutovanjePagerActivity extends AppCompatActivity {
    private static final String EXTRA_PUTOVANJE_ID =
            "com.bignerdranch.android.planerputovanja.putovanje_id";

    private ViewPager mViewPager;
    private List<Putovanje> mPutovanja;

    public static Intent newIntent(Context packageContext, UUID putovanjeId) {
        Intent intent = new Intent(packageContext, PutovanjePagerActivity.class);
        intent.putExtra(EXTRA_PUTOVANJE_ID, putovanjeId);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_putovanje_pager);

        UUID putovanjeId = (UUID) getIntent().getSerializableExtra(EXTRA_PUTOVANJE_ID);

        mViewPager = (ViewPager) findViewById(R.id.putovanje_view_pager);

        mPutovanja = PutovanjeLab.get(this).getPutovanja();
        FragmentManager fragmentManager = getSupportFragmentManager();
        mViewPager.setAdapter(new FragmentStatePagerAdapter(fragmentManager) {
            @Override
            public Fragment getItem(int position) {
                Putovanje putovanje = mPutovanja.get(position);
                return PutovanjeFragment.newInstance(putovanje.getId());
            }
            @Override
            public int getCount() {
                return mPutovanja.size();
            }
        });
        for (int i = 0; i < mPutovanja.size(); i++) {
            if (mPutovanja.get(i).getId().equals(putovanjeId)) {
                mViewPager.setCurrentItem(i);
                break;
            }
        }
    }
}
