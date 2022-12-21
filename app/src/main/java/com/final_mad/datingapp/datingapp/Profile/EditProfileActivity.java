package com.final_mad.datingapp.datingapp.Profile;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;

import androidx.appcompat.widget.SwitchCompat;
import androidx.core.app.ActivityCompat;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.final_mad.datingapp.datingapp.R;
import com.final_mad.datingapp.datingapp.Utils.Constants;
import com.final_mad.datingapp.datingapp.Utils.PreferenceManager;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class EditProfileActivity extends AppCompatActivity {
    private static final String TAG = "EditProfileActivity";
    private static final int PERMISSION_CALLBACK_CONSTANT = 100;
    //firebase
    private static final int REQUEST_PERMISSION_SETTING = 101;
    Button man, woman;
    ImageButton back;
    TextView man_text, women_text;
    ImageView imageView1, imageView;
    Bitmap myBitmap;
    Uri picUri;
    String[] permissionsRequired = new String[]{Manifest.permission.CAMERA,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private Context mContext = EditProfileActivity.this;
    private SharedPreferences permissionStatus;
    private boolean sentToSettings = false;
    private int REQUEST_CAMERA = 0, SELECT_FILE = 1;

//    fields of in4
    EditText username, birthday;
    SwitchCompat notAge, notDistance;
//    default true is male, false is female
    Boolean gender = true;
    Button btnUpdate;

    DocumentReference documentReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

//        get data from fire store
        PreferenceManager preferenceManager = new PreferenceManager(getApplicationContext());
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        documentReference = firestore
                .collection(Constants.KEY_COLLECTION_USERS)
                .document(preferenceManager.getString(Constants.KEY_USER_ID));

        permissionStatus = getSharedPreferences("permissionStatus", MODE_PRIVATE);
        requestMultiplePermissions();
        imageView1 = findViewById(R.id.image_view_1);
        username = findViewById(R.id.edt_username);
        birthday = findViewById(R.id.edt_dob);
        notAge = findViewById(R.id.notShowAge);
        notDistance = findViewById(R.id.notShowDistance);
        btnUpdate = findViewById(R.id.updateInfor);

        man = findViewById(R.id.man_button);
        woman = findViewById(R.id.woman_button);
        man_text = findViewById(R.id.man_text);
        women_text = findViewById(R.id.woman_text);
        back = findViewById(R.id.back);



//        loadLikeData();

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Log.e("not age?",notAge.isChecked()+"");
                Log.e("not dis?",notDistance.isChecked()+"");
                Log.e("is man?", gender+"");
                Log.e("username", username.getText().toString()+"");
                Log.e("dob", birthday.getText().toString()+"");*/
                // update
                if(username.getText().toString().trim().equals("")) {
                    Toast.makeText(EditProfileActivity.this,
                            "Username cannot be empty!", Toast.LENGTH_SHORT).show();
                }
                else if(birthday.getText().toString().trim().equals("")) {
                    Toast.makeText(EditProfileActivity.this,
                            "Date of birth cannot be empty!", Toast.LENGTH_SHORT).show();
                }
                else if(!isDateValid(birthday.getText().toString())) {
                    Toast.makeText(EditProfileActivity.this,
                            "Date of birth is not valid", Toast.LENGTH_SHORT).show();
                } else {
                    documentReference.update("username", username.getText().toString());
                    documentReference.update("dateOfBirth", birthday.getText().toString());
                    documentReference.update("sex", gender ? "male" : "female");
                    documentReference.update("notShowAge", notAge.isChecked());
                    documentReference.update("notShowDistance", notDistance.isChecked());

                    Toast.makeText(EditProfileActivity.this,
                            "Update your information successfully", Toast.LENGTH_SHORT).show();
                }
            }
        });

        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                username.setText(documentSnapshot.getString("username"));
                birthday.setText(documentSnapshot.getString("dateOfBirth"));
                if(documentSnapshot.getString("sex").equals("male")) {
                    man_text.setTextColor(R.color.colorAccent);
                    man.setBackgroundResource(R.drawable.ic_check_select);
                    women_text.setTextColor(R.color.black);
                    woman.setBackgroundResource(R.drawable.ic_check_unselect);
                } else {
                    women_text.setTextColor(R.color.colorAccent);
                    woman.setBackgroundResource(R.drawable.ic_check_select);
                    man_text.setTextColor(R.color.black);
                    man.setBackgroundResource(R.drawable.ic_check_unselect);
                }
                imageView1.setImageBitmap(getBitmapFromEncodedString(documentSnapshot.getString(Constants.KEY_USER_PROFILE_IMAGE)));
                notAge.setChecked(documentSnapshot.getBoolean("notShowAge"));
                notDistance.setChecked(documentSnapshot.getBoolean("notShowDistance"));
                Log.e("id", preferenceManager.getString(Constants.KEY_USER_ID));
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        woman.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View v) {
                gender = false;
                women_text.setTextColor(R.color.colorAccent);
                woman.setBackgroundResource(R.drawable.ic_check_select);
                man_text.setTextColor(R.color.black);
                man.setBackgroundResource(R.drawable.ic_check_unselect);
            }
        });

        man.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View v) {
                gender = true;
                man_text.setTextColor(R.color.colorAccent);
                man.setBackgroundResource(R.drawable.ic_check_select);
                women_text.setTextColor(R.color.black);
                woman.setBackgroundResource(R.drawable.ic_check_unselect);
            }
        });

        imageView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageView = imageView1;
                proceedAfterPermission();

            }
        });


    }

    public static boolean isDateValid(String date)
    {
        try {
            Calendar c = Calendar.getInstance();
            int curYear = c.get(Calendar.YEAR);
            int curMonth = c.get(Calendar.MONTH)+1;
            int curDay = c.get(Calendar.DAY_OF_MONTH);
            String[] as = date.split("-");
            Log.e("as0",as[0]+", "+curMonth);
            Log.e("as1",as[1]+", "+curDay);
            Log.e("as2",as[2]+", "+curYear);
            if(Integer.parseInt(as[2]) <= curYear
                    && Integer.parseInt(as[1]) <= curDay
                    && Integer.parseInt(as[0]) <= curMonth) {

                DateFormat df = new SimpleDateFormat("MM-dd-yyyy");
                df.setLenient(false);
                df.parse(date);
                return true;
            } else {
                return false;
            }
        } catch (ParseException e) {
            return false;
        }
    }

//    private void loadLikeData() {
//        Log.e("inra", documentReference.getId()+"");
//    }


    private void requestMultiplePermissions() {
        if (ActivityCompat.checkSelfPermission(EditProfileActivity.this, permissionsRequired[0]) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(EditProfileActivity.this, permissionsRequired[1]) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(EditProfileActivity.this, permissionsRequired[2]) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(EditProfileActivity.this, permissionsRequired[0])
                    || ActivityCompat.shouldShowRequestPermissionRationale(EditProfileActivity.this, permissionsRequired[1])
                    || ActivityCompat.shouldShowRequestPermissionRationale(EditProfileActivity.this, permissionsRequired[2])) {
                //Show Information about why you need the permission
                AlertDialog.Builder builder = new AlertDialog.Builder(EditProfileActivity.this);
                builder.setTitle("Need Multiple Permissions");
                builder.setMessage("This app needs Camera and Location permissions.");
                builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        ActivityCompat.requestPermissions(EditProfileActivity.this, permissionsRequired, PERMISSION_CALLBACK_CONSTANT);
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();
            } else if (permissionStatus.getBoolean(permissionsRequired[0], false)) {
                //Previously Permission Request was cancelled with 'Dont Ask Again',
                // Redirect to Settings after showing Information about why you need the permission
                AlertDialog.Builder builder = new AlertDialog.Builder(EditProfileActivity.this);
                builder.setTitle("Need Multiple Permissions");
                builder.setMessage("This app needs Camera and Location permissions.");
                builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        sentToSettings = true;
                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package", getPackageName(), null);
                        intent.setData(uri);
                        startActivityForResult(intent, REQUEST_PERMISSION_SETTING);
                        Toast.makeText(getBaseContext(), "Go to Permissions to Grant  Camera and Location", Toast.LENGTH_LONG).show();
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();
            } else {
                //just request the permission
                ActivityCompat.requestPermissions(EditProfileActivity.this, permissionsRequired, PERMISSION_CALLBACK_CONSTANT);
            }

            // txtPermissions.setText("Permissions Required");

            SharedPreferences.Editor editor = permissionStatus.edit();
            editor.putBoolean(permissionsRequired[0], true);
            editor.commit();
        } else {
            //You already have the permission, just go ahead.
            //proceedAfterPermission();
        }
    }

    private void proceedAfterPermission() {


        final CharSequence[] options = {"Take Photo", "Choose from Gallery", "Cancel"};


        AlertDialog.Builder builder = new AlertDialog.Builder(EditProfileActivity.this);

        builder.setTitle("Add Photo!");

        builder.setItems(options, new DialogInterface.OnClickListener() {

            @Override

            public void onClick(DialogInterface dialog, int item) {

                if (options[item].equals("Take Photo"))

                {

                    cameraIntent();

                } else if (options[item].equals("Choose from Gallery"))

                {

                    galleryIntent();


                } else if (options[item].equals("Cancel")) {

                    dialog.dismiss();

                }

            }

        });

        builder.show();

    }


    private void galleryIntent() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);//
        startActivityForResult(Intent.createChooser(intent, "Select File"), SELECT_FILE);
    }

    private void cameraIntent() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CAMERA);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_PERMISSION_SETTING) {
            if (ActivityCompat.checkSelfPermission(EditProfileActivity.this, permissionsRequired[0]) == PackageManager.PERMISSION_GRANTED) {
                //Got Permission
                proceedAfterPermission();
            }
        }
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_FILE)
                onSelectFromGalleryResult(data);
            else if (requestCode == REQUEST_CAMERA)
                onCaptureImageResult(data);
        }
    }

    private void onCaptureImageResult(Intent data) {
        Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);

        File destination = new File(Environment.getExternalStorageDirectory(),
                System.currentTimeMillis() + ".jpg");

        FileOutputStream fo;
        try {
            destination.createNewFile();
            fo = new FileOutputStream(destination);
            fo.write(bytes.toByteArray());
            fo.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        imageView.setImageBitmap(thumbnail);
    }

    @SuppressWarnings("deprecation")
    private void onSelectFromGalleryResult(Intent data) {

        Bitmap bm = null;
        if (data != null) {
            try {
                bm = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), data.getData());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        imageView.setImageBitmap(bm);
    }


    @Override
    protected void onPostResume() {
        super.onPostResume();
        if (sentToSettings) {
            if (ActivityCompat.checkSelfPermission(EditProfileActivity.this, permissionsRequired[0]) == PackageManager.PERMISSION_GRANTED) {
                //Got Permission
                proceedAfterPermission();
            }
        }
    }

    private Bitmap getBitmapFromEncodedString(String encodedImage) {
        if (encodedImage != null) {
            byte[] bytes = Base64.decode(encodedImage, Base64.DEFAULT);
            return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        } else {
            return null;
        }
    }
}
