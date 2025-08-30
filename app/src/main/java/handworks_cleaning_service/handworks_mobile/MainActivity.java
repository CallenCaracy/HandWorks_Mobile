package handworks_cleaning_service.handworks_mobile;

import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import handworks_cleaning_service.handworks_mobile.utils.ThemeUtil;

public class MainActivity extends AppCompatActivity {
    Button dayMode;
    Button nightMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        dayMode = findViewById(R.id.dayBtn);
        nightMode = findViewById(R.id.nightBtn);

        dayMode.setOnClickListener(v -> ThemeUtil.setTheme(this, false));
        nightMode.setOnClickListener(v -> ThemeUtil.setTheme(this, true));
    }
}