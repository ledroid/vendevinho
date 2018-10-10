package zap.com.example.app.appzap.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import zap.com.example.app.appzap.R;

public class ActivityMain extends AppCompatActivity implements View.OnClickListener {


    Button btnCli;
    Button btnSale;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        loadComponets();
    }


    public void loadComponets(){
        btnCli = (Button)findViewById(R.id.btnCli);
        btnCli.setOnClickListener(this);
        btnSale = (Button)findViewById(R.id.btnSale);
        btnSale.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnCli:{
                Intent in = new Intent(this, AplicationActivity.class);
                overridePendingTransition(100, 50);
                startActivity(in);
            }
            break;
            case R.id.btnSale:{
                Intent in = new Intent(this, SaleActivity.class);
                overridePendingTransition(100, 50);
                startActivity(in);
            }
            break;

            default:
                break;
        }
    }
}
