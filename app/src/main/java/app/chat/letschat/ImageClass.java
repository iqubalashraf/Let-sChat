package app.chat.letschat;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;

import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;

/**
 * Created by ashrafiqubal on 24/07/17.
 */

public class ImageClass extends AppCompatActivity {
    private final String IMAGE_STRING = "app.chat.letschat.IMAGE_STRING";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_class);
        SubsamplingScaleImageView imageView = (SubsamplingScaleImageView)findViewById(R.id.imageView);
        imageView.setImage(ImageSource.bitmap(parseStringToBitmap(getIntent().getStringExtra(IMAGE_STRING))));
    }

    private Bitmap parseStringToBitmap(String imageString){
        Bitmap bitmap = null;
        final String encodedString = "data:image/jpg;base64,"+imageString;
        final String pureBase64Encoded = encodedString.substring(encodedString.indexOf(",")  + 1);
        final byte[] decodedBytes = Base64.decode(pureBase64Encoded, Base64.DEFAULT);
        bitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
        return bitmap;
    }
}
