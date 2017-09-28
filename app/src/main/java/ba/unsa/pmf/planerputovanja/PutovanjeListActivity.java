package ba.unsa.pmf.planerputovanja;

import android.support.v4.app.Fragment;

public class PutovanjeListActivity extends SingleFragmentActivity{
    @Override
    protected Fragment createFragment() {
        return new PutovanjeListFragment();
    }
}
