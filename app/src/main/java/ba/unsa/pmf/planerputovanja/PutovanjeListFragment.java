package ba.unsa.pmf.planerputovanja;

import android.annotation.TargetApi;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.util.List;
import java.util.UUID;

import ba.unsa.pmf.planerputovanja.database.PutovanjeDbSchema;

import static android.content.ContentValues.TAG;

public class PutovanjeListFragment extends Fragment{
    private static final String SAVED_SUBTITLE_VISIBLE = "subtitle";

    private RecyclerView mPutovanjeRecyclerView;
    private PutovanjeAdapter mAdapter;
    private Putovanje mPutovanje;
    private boolean mSubtitleVisible;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_putovanje_list, container, false);
        mPutovanjeRecyclerView = (RecyclerView) view
                .findViewById(R.id.putovanje_recycler_view);
        mPutovanjeRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        if (savedInstanceState != null) {
            mSubtitleVisible = savedInstanceState.getBoolean(SAVED_SUBTITLE_VISIBLE);
        }

        updateUI();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        updateUI();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(SAVED_SUBTITLE_VISIBLE, mSubtitleVisible);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_putovanje_list, menu);

        MenuItem subtitleItem = menu.findItem(R.id.show_subtitle);
        if (mSubtitleVisible) {
            subtitleItem.setTitle(R.string.hide_subtitle);
        } else {
            subtitleItem.setTitle(R.string.show_subtitle);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.new_putovanje:
                Putovanje putovanje = new Putovanje();
                PutovanjeLab.get(getActivity()).addPutovanje(putovanje);
                Intent intent = PutovanjePagerActivity.newIntent(getActivity(), putovanje.getId());
                startActivity(intent);
                return true;
            case R.id.show_subtitle:
                mSubtitleVisible = !mSubtitleVisible;
                getActivity().invalidateOptionsMenu();
                updateSubtitle();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void updateSubtitle() {
        PutovanjeLab putovanjeLab = PutovanjeLab.get(getActivity());
        int putovanjeCount = putovanjeLab.getPutovanja().size();
        String subtitle = getString(R.string.subtitle_format, putovanjeCount);

        if (!mSubtitleVisible) {
            subtitle = null;
        }

        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.getSupportActionBar().setSubtitle(subtitle);
    }

    private void updateUI() {
        PutovanjeLab putovanjeLab = PutovanjeLab.get(getActivity());
        List<Putovanje> putovanja = putovanjeLab.getPutovanja();
        if (mAdapter == null) {
            mAdapter = new PutovanjeAdapter(putovanja);
            mPutovanjeRecyclerView.setAdapter(mAdapter);
        }
        else {
            mAdapter.setPutovanja(putovanja);
            mAdapter.notifyDataSetChanged();
        }

        updateSubtitle();
    }

    private class PutovanjeHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView mTitleTextView;
        private TextView mDateTextView;
        private Putovanje mPutovanje;
        private ImageView mReservedImageView;


        public PutovanjeHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.list_item_putovanje, parent, false));
            itemView.setOnClickListener(this);
            mTitleTextView = (TextView) itemView.findViewById(R.id.putovanje_title);
            mDateTextView = (TextView) itemView.findViewById(R.id.putovanje_date);
            /*mReservedImageView = (ImageView) itemView.findViewById(R.id.putovanje_reserved);*/

        }

        @Override
        public void onClick(View view) {
            Intent intent = PutovanjePagerActivity.newIntent(getActivity(), mPutovanje.getId());
            startActivity(intent);
        }

        public void bind(Putovanje putovanje) {
            mPutovanje = putovanje;
            mTitleTextView.setText(mPutovanje.getTitle());
            mDateTextView.setText(android.text.format.DateFormat.format("EEEE, MMM dd yyyy", mPutovanje.getDate()));
            /*mReservedImageView.setVisibility(putovanje.isReserved() ? View.VISIBLE : View.GONE);*/

        }
    }
    private class PutovanjeAdapter extends RecyclerView.Adapter<PutovanjeHolder> {
        private List<Putovanje> mPutovanja;
        public PutovanjeAdapter(List<Putovanje> putovanja) {
            mPutovanja = putovanja;
        }
        @Override
        public PutovanjeHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            return new PutovanjeHolder(layoutInflater, parent);
        }
        @Override
        public void onBindViewHolder(PutovanjeHolder holder, int position) {
            Putovanje putovanje = mPutovanja.get(position);
            holder.bind(putovanje);

        }
        @Override
        public int getItemCount() {
            return mPutovanja.size();
        }

        public void setPutovanja(List<Putovanje> putovanja) {
            mPutovanja = putovanja;
        }
    }
}
