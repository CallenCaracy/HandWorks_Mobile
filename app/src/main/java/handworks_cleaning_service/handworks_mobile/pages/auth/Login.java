package handworks_cleaning_service.handworks_mobile.pages.auth;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import handworks_cleaning_service.handworks_mobile.R;
import handworks_cleaning_service.handworks_mobile.pages.index.Dashboard;

public class Login extends AppCompatActivity {

    Button signInBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Find your button by ID (make sure it's in activity_login.xml)
        signInBtn = findViewById(R.id.btnSignIn);

        // On click, navigate to dashboard
        signInBtn.setOnClickListener(v -> navigateToDashboard());

    }

    private void navigateToDashboard() {
        Intent intent = new Intent(this, Dashboard.class);
        startActivity(intent);
        finish(); // Optional: close login so user can’t go back
    }

}