package handworks_cleaning_service.handworks_mobile.ui.pages.index;

import android.os.Bundle;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;

import handworks_cleaning_service.handworks_mobile.R;

public class FullscreenImageView extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_fullscreen_image_view);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        ImageView fullImage = findViewById(R.id.fullImage);

        int resId = getIntent().getIntExtra("image_res", 0);
        String url = getIntent().getStringExtra("image_url");

        if (resId != 0) {
            fullImage.setImageResource(resId);
        } else if (url != null) {
            Glide.with(this)
                    .load(url)
                    .placeholder(R.drawable.pfp_placeholder)
                    .error(R.drawable.error)
                    .into(fullImage);
        }

        fullImage.setOnClickListener(v -> finish());
    }
}