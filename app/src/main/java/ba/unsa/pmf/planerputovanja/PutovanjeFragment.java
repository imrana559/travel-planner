package ba.unsa.pmf.planerputovanja;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NavUtils;
import android.support.v4.content.FileProvider;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static android.R.id.message;

public class PutovanjeFragment extends Fragment{
    private static final String ARG_PUTOVANJE_ID = "putovanje_id";
    private static final String DIALOG_DATE = "DialogDate";

    private static final int REQUEST_DATE = 0;
    private static final int REQUEST_PHOTO= 2;

    public static final String INTENT_ACTION_SHOW_FOO = "show_foo";
    public static final String INTENT_ACTION_SHOW_BAR = "show_bar";

    private Putovanje mPutovanje;
    private File mPhotoFile;
    private EditText mTitleField;
    private Button mDateButton;
    private CheckBox mReservedCheckBox;
    private EditText mMjesta;
    private EditText mNapomena;
    private Button mPosaljiRecenziju;
    private Button mPozoviPrijatelje;
    private Button mLokacija;
    private ImageButton mPhotoButton;
    private ImageView mPhotoView;
    private Button mToDoList;
    private boolean subject;

    public static PutovanjeFragment newInstance(UUID putovanjeId) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_PUTOVANJE_ID, putovanjeId);
        PutovanjeFragment fragment = new PutovanjeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        UUID putovanjeId = (UUID) getArguments().getSerializable(ARG_PUTOVANJE_ID);
        mPutovanje = PutovanjeLab.get(getActivity()).getPutovanje(putovanjeId);
        mPhotoFile = PutovanjeLab.get(getActivity()).getPhotoFile(mPutovanje);
    }

    @Override
    public void onPause() {
        super.onPause();
        PutovanjeLab.get(getActivity()).updatePutovanje(mPutovanje);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_putovanje, container, false);

        mTitleField = (EditText) v.findViewById(R.id.putovanje_title);
        mTitleField.setText(mPutovanje.getTitle());
        mTitleField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(
                    CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(
                    CharSequence s, int start, int before, int count) {
                mPutovanje.setTitle(s.toString());
            }
            @Override
            public void afterTextChanged(Editable s) {
                // This one too
            }
        });

        mDateButton = (Button) v.findViewById(R.id.putovanje_date);
        updateDate();
        mDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager manager = getFragmentManager();
                DatePickerFragment dialog = DatePickerFragment.newInstance(mPutovanje.getDate());
                dialog.setTargetFragment(PutovanjeFragment.this, REQUEST_DATE);
                dialog.show(manager, DIALOG_DATE);
            }
        });

        mReservedCheckBox = (CheckBox)v.findViewById(R.id.putovanje_reserved);
        mReservedCheckBox.setChecked(mPutovanje.isReserved());
        mReservedCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mPutovanje.setReserved(isChecked);
            }
        });

        mMjesta = (EditText) v.findViewById(R.id.putovanje_mjesta);
        mMjesta.setText(mPutovanje.getMjesta());
        mMjesta.setBackgroundColor(Color.WHITE);
        mMjesta.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(
                    CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(
                    CharSequence s, int start, int before, int count) {
                mPutovanje.setMjesta(s.toString());
            }
            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mNapomena = (EditText) v.findViewById(R.id.putovanje_napomena);
        mNapomena.setText(mPutovanje.getNapomena());
        mNapomena.setBackgroundColor(Color.WHITE);
        mNapomena.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(
                    CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(
                    CharSequence s, int start, int before, int count) {
                mPutovanje.setNapomena(s.toString());
            }
            @Override
            public void afterTextChanged(Editable s) {

            }
        });



        mPosaljiRecenziju = (Button) v.findViewById(R.id.putovanje_report);
        mPosaljiRecenziju.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                        "mailto","planerputovanja@gmail.com", null));
                intent.putExtra(Intent.EXTRA_SUBJECT, subject);
                intent.putExtra(Intent.EXTRA_TEXT, message);
                startActivity(Intent.createChooser(intent, "Izaberite način slanja:"));
            }
        });

        mLokacija = (Button) v.findViewById(R.id.location);
        mLokacija.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), LokacijaActivity.class);
                startActivity(i);
            }
        });

        mPozoviPrijatelje = (Button) v.findViewById(R.id.putovanje_prijatelj);
        mPozoviPrijatelje.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_INSERT_OR_EDIT);
                i.setType(ContactsContract.Contacts.CONTENT_ITEM_TYPE);

                startActivity(i);
            }
        });

        PackageManager packageManager = getActivity().getPackageManager();

        mPhotoButton = (ImageButton) v.findViewById(R.id.putovanje_camera);
        final Intent captureImage = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        boolean canTakePhoto = mPhotoFile != null &&
                captureImage.resolveActivity(packageManager) != null;
        mPhotoButton.setEnabled(canTakePhoto);
        mPhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = FileProvider.getUriForFile(getActivity(),
                        "com.bignerdranch.android.planerputovanja.fileprovider",
                        mPhotoFile);
                captureImage.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                List<ResolveInfo> cameraActivities = getActivity()
                        .getPackageManager().queryIntentActivities(captureImage,
                                PackageManager.MATCH_DEFAULT_ONLY);
                for (ResolveInfo activity : cameraActivities) {
                    getActivity().grantUriPermission(activity.activityInfo.packageName,
                            uri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                }
                startActivityForResult(captureImage, REQUEST_PHOTO);
            }
        });

        mPhotoView = (ImageView) v.findViewById(R.id.putovanje_photo);
        updatePhotoView();

        return v;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_putovanje, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.delete_putovanje:
                UUID putovanjeId = mPutovanje.getId();
                PutovanjeLab.get(getActivity()).deletePutovanje(putovanjeId);
                getActivity().finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        if (requestCode == REQUEST_DATE) {
            Date date = (Date) data
                    .getSerializableExtra(DatePickerFragment.EXTRA_DATE);
            mPutovanje.setDate(date);
            updateDate();
        }else if (requestCode == REQUEST_PHOTO) {
            Uri uri = FileProvider.getUriForFile(getActivity(),
                    "com.bignerdranch.android.planerputovanja.fileprovider",
                    mPhotoFile);
            getActivity().revokeUriPermission(uri,
                    Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            updatePhotoView();
        }

    }

    private void updateDate() {
        mDateButton.setText(android.text.format.DateFormat.format("EEEE, MMM dd yyyy", mPutovanje.getDate()));
    }

    private void updatePhotoView() {
        if (mPhotoFile == null || !mPhotoFile.exists()) {
            mPhotoView.setImageDrawable(null);
        } else {
            Bitmap bitmap = PictureUtils.getScaledBitmap(
                    mPhotoFile.getPath(), getActivity());
            mPhotoView.setImageBitmap(bitmap);
        }
    }
}
