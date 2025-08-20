package ir.kyraserver.vpnclient;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;
import androidx.activity.result.ActivityResultLauncher;

public class MainActivity extends AppCompatActivity {

    private EditText configInput;
    private Button connectButton;
    private Button scanButton;
    private TextView statusText;
    private boolean isConnected = false;

    private ActivityResultLauncher<ScanOptions> barcodeLauncher = registerForActivityResult(new ScanContract(),
        result -> {
            if(result.getContents() == null) {
                Toast.makeText(MainActivity.this, "Cancelled", Toast.LENGTH_LONG).show();
            } else {
                configInput.setText(result.getContents());
                Toast.makeText(MainActivity.this, "Scanned: " + result.getContents(), Toast.LENGTH_LONG).show();
            }
        });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        configInput = findViewById(R.id.configInput);
        connectButton = findViewById(R.id.connectButton);
        scanButton = findViewById(R.id.scanButton);
        statusText = findViewById(R.id.statusText);

        connectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleConnection();
            }
        });

        scanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scanQRCode();
            }
        });
    }

    private void toggleConnection() {
        String config = configInput.getText().toString().trim();
        
        if (config.isEmpty()) {
            Toast.makeText(this, "Please enter V2Ray config", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!isConnected) {
            // Connect logic here
            isConnected = true;
            connectButton.setText("Disconnect");
            statusText.setText("Status: Connected");
            statusText.setTextColor(getResources().getColor(android.R.color.holo_green_dark));
            Toast.makeText(this, "Connected to V2Ray server", Toast.LENGTH_SHORT).show();
        } else {
            // Disconnect logic here
            isConnected = false;
            connectButton.setText("Connect");
            statusText.setText("Status: Disconnected");
            statusText.setTextColor(getResources().getColor(android.R.color.holo_red_dark));
            Toast.makeText(this, "Disconnected from V2Ray server", Toast.LENGTH_SHORT).show();
        }
    }

    private void scanQRCode() {
        ScanOptions options = new ScanOptions();
        options.setPrompt("Scan V2Ray QR Code");
        options.setBeepEnabled(false);
        options.setOrientationLocked(true);
        options.setCaptureActivity(com.journeyapps.barcodescanner.CaptureActivity.class);
        barcodeLauncher.launch(options);
    }
}
