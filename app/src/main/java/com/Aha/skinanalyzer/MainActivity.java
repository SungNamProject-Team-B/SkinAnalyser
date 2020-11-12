package com.Aha.skinanalyzer;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import androidx.core.content.FileProvider;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static android.os.Environment.DIRECTORY_PICTURES;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_IMAGE_CAPTURE = 672;
    private static final int PICK_FROM_CAMERA = 1;
    private static final int CROP_PICTURE = 3;
    private String imageFilePath = " ", imageFileName= " ";
    private static Uri photoUri, albumUri;
    private String result = "";
    ImageView showView;
    File tempSelectFile;
    Button  upload;
    private String takePhoto;




    private MediaScanner mMediaScanner; // 사진 저장 시 갤러리 폴더에 바로 반영사항을 업데이트 시켜주려면 이 것이 필요하다(미디어 스캐닝)


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        upload= findViewById(R.id.btn_ImageSend);
        upload.setEnabled(false);
        upload.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                FileUploadUtils.send2Server(tempSelectFile);
            }
        });







        // 사진 저장 후 미디어 스캐닝을 돌려줘야 갤러리에 반영됨.
        mMediaScanner = MediaScanner.getInstance(getApplicationContext());


        // 권한 체크
        TedPermission.with(getApplicationContext())
                .setPermissionListener(permissionListener)
                .setRationaleMessage("카메라 권한이 필요합니다.")
                .setDeniedMessage("거부하셨습니다.")
                .setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA)
                .check();

        //카메라로 사진찍기
         findViewById(R.id.btn_capture).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (intent.resolveActivity(getPackageManager()) != null) {
                    File photoFile = null;
                    try {
                        photoFile = createImageFile();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    if (photoFile != null) {
                        Log.d("프로바이더", getPackageName()+"");
                        photoUri = FileProvider.getUriForFile(getApplicationContext(), getPackageName(), photoFile);
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                        startActivityForResult(intent, PICK_FROM_CAMERA);
                    }
                }
            }
        });


    }

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "TEST_" + timeStamp + "_";
       //File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File storageDir = new File(Environment.getExternalStoragePublicDirectory(DIRECTORY_PICTURES) + File.separator + "Disease" + File.separator);
        File image = File.createTempFile(
                imageFileName,
                ".jpg",
                storageDir
        );

        upload.setEnabled(true);
        imageFilePath = image.getAbsolutePath();
        return image;


    }


    /*   @Override
        protected void onActivityResult(int requestCode, int resultCode, Intent data) {
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

                if (exif != null) {
                    exifOrientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
                    exifDegree = exifOrientationToDegress(exifOrientation);

                } else {
                    exifDegree = 0;
                }




                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HHmmss", Locale.getDefault());
                Date curDate = new Date(System.currentTimeMillis());
                String filename = formatter.format(curDate);

                String strFolderName = Environment.getExternalStoragePublicDirectory(DIRECTORY_PICTURES) + File.separator + "Disease" + File.separator;
                File file = new File(strFolderName);
                if (!file.exists())
                    file.mkdirs();

                File f = new File(strFolderName + "/" + filename + ".png");
                result = f.getPath();

                FileOutputStream fOut = null;
                try {
                    fOut = new FileOutputStream(f);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    result = "Save Error fOut";
                }

                // 비트맵 사진 폴더 경로에 저장
                rotate(bitmap, exifDegree).compress(Bitmap.CompressFormat.PNG, 70, fOut);

                try {
                    fOut.flush();
                    Log.w("널포인트 익셉션"," 체크해볼것을 요망");
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    fOut.close();
                    // 방금 저장된 사진을 갤러리 폴더 반영 및 최신화
                    mMediaScanner.mediaScanning(strFolderName + "/" + filename + ".png");
                } catch (IOException e) {
                    e.printStackTrace();
                    result = "File close Error";
                }

                Uri dataUri = data.getData();
                showView.setImageURI(dataUri);

                assert dataUri != null;
                try {
                    InputStream in = getContentResolver().openInputStream(dataUri);
                    Bitmap image = BitmapFactory.decodeStream(in);
                    showView.setImageBitmap(image);

                    assert in != null;

                // 이미지 뷰에 비트맵을 set하여 이미지 표현
                ((ImageView) findViewById(R.id.iv_result)).setImageBitmap(rotate(bitmap, exifDegree));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            }


        }*/


 @Override
 protected void onActivityResult(int requestCode, int resultCode, Intent data) {
     super.onActivityResult(requestCode, resultCode, data);
     try {
         switch (requestCode) {
             // 카메라로 찍은 이미지 불러오는 경우
             case PICK_FROM_CAMERA: {
                 if (resultCode == RESULT_OK) {
                     File file = new File(imageFilePath);
                     Log.d("카메라 파일", imageFilePath+"");
                     BitmapFactory.Options options = new BitmapFactory.Options();
                     Bitmap imageBitmap = BitmapFactory.decodeFile(file.getAbsolutePath(), options);
                     Log.d("비트맵 변환 성공", "OK");

                     if (imageBitmap != null) {
                         android.media.ExifInterface ei = new android.media.ExifInterface(imageFilePath);
                         int orientation = ei.getAttributeInt(android.media.ExifInterface.TAG_ORIENTATION, android.media.ExifInterface.ORIENTATION_UNDEFINED);
                         int exifDegree = exifOrientationToDegrees(orientation);

                         Bitmap rotatedBitmap = rotateImage(imageBitmap, exifDegree);
                         Log.d("카메라 이미지 URI_re", photoUri+"");

                         saveImage(rotatedBitmap);   // 갤러리에 저장하는 함수
                         ((ImageView) findViewById(R.id.iv_result)).setImageBitmap(imageBitmap);
                     }

                     File cropFile = createImageFile();  // 새 크롭 이미지 (덮어쓰기X)
                     albumUri = Uri.fromFile(cropFile);
                     cropImage();
                 }
                 break;

             }

             case CROP_PICTURE:{
                 if(resultCode == Activity.RESULT_OK){
                     galleryAddPic();
                     File tempFile = new File(imageFilePath);
                     BitmapFactory.Options options = new BitmapFactory.Options();
                     Bitmap bitmap = BitmapFactory.decodeFile(tempFile.getAbsolutePath(), options);
                     Log.d("갤러리 절대경로", tempFile.getAbsolutePath()+"");

                     if(bitmap != null){
                           BitmapFactory.decodeFile(imageFileName);
                           // RecyclerView에 데이터 추가
                         Log.d("갤러리 이미지 URI_re", photoUri+"");
                     }

                 }
                 break;
             }
         }


     } catch (Exception error) {
         error.printStackTrace();
     }


 }

    private void galleryAddPic(){
        Log.d("갤러리애드픽", "OK");
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File file = new File(imageFilePath);
        Uri uri = Uri.fromFile(file);
        mediaScanIntent.setData(uri);
        sendBroadcast(mediaScanIntent);
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
        intent.putExtra("output", albumUri);
        startActivityForResult(intent, CROP_PICTURE);
    }

    private void saveImage(Bitmap finalBitmap){
        String imageFileForGalName = imageFileName;
        Log.d("갤러리 저장될 때", imageFileForGalName+"");
        File myDir = new File(Environment.getExternalStorageDirectory().toString());
        //File myDir = new File(Environment.getExternalStorageDirectory()+"/Pictures", "AcneAnalyzer");
        myDir.mkdirs();
        File file = new File(myDir, imageFileForGalName);
        try{
            FileOutputStream out = new FileOutputStream(file);
            finalBitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
            this.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(file)));
            out.flush();
            out.close();;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }




    // 이미지 회전하는 함수
    public static Bitmap rotateImage(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        Log.d("Rotate", angle+"");
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
    }
    public int exifOrientationToDegrees(int exifOrientation){
        if(exifOrientation == android.media.ExifInterface.ORIENTATION_ROTATE_90){
            return 90;
        }
        else if(exifOrientation == android.media.ExifInterface.ORIENTATION_ROTATE_180){
            return 180;
        }
        else if(exifOrientation == ExifInterface.ORIENTATION_ROTATE_270){
            return 270;
        }
        else{
            return 0;
        }
    }



    PermissionListener permissionListener = new PermissionListener() {
        @Override
        public void onPermissionGranted() {
            Toast.makeText(getApplicationContext(), "권한이 허용됨",Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onPermissionDenied(ArrayList<String> deniedPermissions) {
            Toast.makeText(getApplicationContext(), "권한이 거부됨",Toast.LENGTH_SHORT).show();
        }
    };
}






