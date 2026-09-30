package my.ke.usiu.supermarketpos;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    String[] itemNames = {"Select", "Bread", "Milk", "Sugar", "Rice",
            "Cooking Oil", "Eggs", "Soap", "Tea Leaves"};
    int[] itemPrices = {0, 60, 120, 180, 350, 300, 450, 90, 150};

    static final int ROWS = 5;
    Spinner[] spinners = new Spinner[ROWS];
    TextView[] tvPrices = new TextView[ROWS];
    EditText[] etQty = new EditText[ROWS];
    TextView[] tvTotals = new TextView[ROWS];
    TextView tvGrandTotal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        setContentView(R.layout.activity_main);

        LinearLayout container = findViewById(R.id.rowsContainer);
        tvGrandTotal = findViewById(R.id.tvGrandTotal);
        Button btnCheckout = findViewById(R.id.btnCheckout);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item, itemNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        LayoutInflater inflater = LayoutInflater.from(this);

        for (int i = 0; i < ROWS; i++) {
            View row = inflater.inflate(R.layout.row_item, container, false);

            spinners[i] = row.findViewById(R.id.spItem);
            tvPrices[i] = row.findViewById(R.id.tvUnitPrice);
            etQty[i] = row.findViewById(R.id.etQty);
            tvTotals[i] = row.findViewById(R.id.tvRowTotal);

            spinners[i].setAdapter(adapter);
            spinners[i].setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    calculate();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) { }
            });

            etQty[i].addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) { }

                @Override
                public void afterTextChanged(Editable s) {
                    calculate();
                }
            });

            container.addView(row);
        }

        btnCheckout.setOnClickListener(v -> {
            ArrayList<String> names = new ArrayList<>();
            ArrayList<Integer> prices = new ArrayList<>();
            ArrayList<Integer> qtys = new ArrayList<>();
            ArrayList<Integer> totals = new ArrayList<>();
            int grand = 0;

            for (int i = 0; i < ROWS; i++) {
                int pos = spinners[i].getSelectedItemPosition();
                int qty = getQty(i);
                if (pos > 0 && qty > 0) {
                    int total = itemPrices[pos] * qty;
                    names.add(itemNames[pos]);
                    prices.add(itemPrices[pos]);
                    qtys.add(qty);
                    totals.add(total);
                    grand += total;
                }
            }

            if (names.isEmpty()) {
                Toast.makeText(this, "Please select at least one item", Toast.LENGTH_SHORT).show();
                return;
            }

            Intent intent = new Intent(MainActivity.this, ReceiptActivity.class);
            intent.putStringArrayListExtra("names", names);
            intent.putIntegerArrayListExtra("prices", prices);
            intent.putIntegerArrayListExtra("qtys", qtys);
            intent.putIntegerArrayListExtra("totals", totals);
            intent.putExtra("grand", grand);
            startActivity(intent);
        });

        calculate();
    }

    private int getQty(int i) {
        try {
            return Integer.parseInt(etQty[i].getText().toString().trim());
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    private void calculate() {
        int grand = 0;
        for (int i = 0; i < ROWS; i++) {
            if (spinners[i] == null || tvTotals[i] == null) continue;

            int pos = spinners[i].getSelectedItemPosition();
            if (pos > 0) {
                int price = itemPrices[pos];
                int total = price * getQty(i);
                tvPrices[i].setText(String.valueOf(price));
                tvTotals[i].setText(String.valueOf(total));
                grand += total;
            } else {
                tvPrices[i].setText("");
                tvTotals[i].setText("");
            }
        }
        tvGrandTotal.setText("KES " + grand);
    }
}