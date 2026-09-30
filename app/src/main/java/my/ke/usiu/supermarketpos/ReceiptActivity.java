package my.ke.usiu.supermarketpos;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

public class ReceiptActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receipt);

        TextView tvReceipt = findViewById(R.id.tvReceipt);
        Button btnBack = findViewById(R.id.btnBack);

        ArrayList<String> names = getIntent().getStringArrayListExtra("names");
        ArrayList<Integer> prices = getIntent().getIntegerArrayListExtra("prices");
        ArrayList<Integer> qtys = getIntent().getIntegerArrayListExtra("qtys");
        ArrayList<Integer> totals = getIntent().getIntegerArrayListExtra("totals");
        int grand = getIntent().getIntExtra("grand", 0);

        String date = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
                .format(new Date());
        int receiptNo = 1000 + new Random().nextInt(9000);

        String line = "----------------------------\n";
        StringBuilder sb = new StringBuilder();
        sb.append("     MIAMI SUPERMARKET\n");
        sb.append(line);
        sb.append("Receipt No: ").append(receiptNo).append("\n");
        sb.append("Date: ").append(date).append("\n");
        sb.append(line);
        sb.append(String.format(Locale.getDefault(), "%-11s %3s %5s %6s\n",
                "Item", "Qty", "Price", "Total"));
        sb.append(line);

        if (names != null) {
            for (int i = 0; i < names.size(); i++) {
                sb.append(String.format(Locale.getDefault(), "%-11s %3d %5d %6d\n",
                        names.get(i), qtys.get(i), prices.get(i), totals.get(i)));
            }
        }

        sb.append(line);
        sb.append(String.format(Locale.getDefault(), "%-20s %7d\n", "GRAND TOTAL (KES)", grand));
        sb.append(line);
        sb.append("  Thank you for shopping!\n");

        tvReceipt.setText(sb.toString());

        btnBack.setOnClickListener(v -> finish());
    }
}