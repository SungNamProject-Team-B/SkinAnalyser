package com.Aha.pimple;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;

import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;






public class Camera extends AppCompatActivity {
    private static final int REQUEST_IMAGE_CAPTURE = 600;
    private String imageFilePath;
    private Uri photoUri;
    ArrayList<Skin> skinArrayList;
    RecyclerView recyclerView;
    LinearLayoutManager layoutManager;
    MyAdapter myAdapter;
    int count = 0;
    ImageButton buttonImage;

    private static final int CROP_PICTURE = 3;


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.camera);
        buttonImage = findViewById(R.id.btn_image);

        //RecyclerView 설정
        skinArrayList = new ArrayList<>();

        recyclerView = findViewById(R.id.rv);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        myAdapter = new MyAdapter(skinArrayList);
        recyclerView.setAdapter(myAdapter);

        //권한체크

        TedPermission.with(getApplicationContext())
                .setPermissionListener(permissionListner)
                .setRationaleMessage("카메라 권한이 필요합니다")
                .setDeniedMessage("거부 하셨습니다")
                .setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA)
                .check();



        findViewById(R.id.btn_image).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if(intent.resolveActivity(getPackageManager()) != null) {
                File photoFile = null;
                try{
                    photoFile = createimageFile();
                } catch(IOException e){

                }

                if(photoFile != null){
                    photoUri = FileProvider.getUriForFile(getApplicationContext(),getPackageName(),photoFile);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                    startActivityForResult(intent,REQUEST_IMAGE_CAPTURE);
                }

                }
            }
        });
    }

    private File createimageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "TEST_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,
                ".jpg",
                storageDir

        );
        imageFilePath= image.getAbsolutePath();
        return image;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bitmap bitmap = BitmapFactory.decodeFile(imageFilePath);
            ExifInterface exif = null;

            try {
                exif = new ExifInterface(imageFilePath);
            } catch (IOException e) {
                e.printStackTrace();
            }

            int exifOrientation;
            int exifDegree;

            if(exif != null){
                exifOrientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
                exifDegree = exifOrientationToDegrees(exifOrientation);
            }else{
                exifDegree = 0;
            }
            ((ImageView)findViewById(R.id.iv_result)).setImageBitmap(rotate(bitmap,exifDegree));


        }
    }
    public void cropImage(){
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.setDataAndType(photoUri, "image/*");

        intent.putExtra("outputX", 300);
        intent.putExtra("outputY", 300);
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("scale", true);
        startActivityForResult(intent, CROP_PICTURE);
    }

    private int exifOrientationToDegrees(int exifOrientation){
        if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_90){
            return 90;
        }else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_180){
            return 180;
        }
        else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_270){
            return 270;
        }
        return 0;
    }

    private Bitmap rotate(Bitmap bitmap, float degree){
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);

    }

    PermissionListener permissionListner = new PermissionListener() {
        @Override  //퍼미션 허용시
        public void onPermissionGranted() {
            Toast.makeText(getApplicationContext(),"권한이 허용됨",Toast.LENGTH_SHORT).show();

        }

        @Override //퍼미션 거부시
        public void onPermissionDenied(ArrayList<String> deniedPermissions) {
            Toast.makeText(getApplicationContext(),"권한이 거부됨",Toast.LENGTH_SHORT).show();

        }
    };

    public void setRecyclerViewData(String name, String path){
        Log.d("리사이클러뷰 데이터 추가", path+"");
        Skin skin = new Skin(name, path);
        skinArrayList.add(skin);
        myAdapter.notifyDataSetChanged();
    }




}
