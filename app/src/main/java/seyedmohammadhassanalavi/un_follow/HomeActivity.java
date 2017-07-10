package seyedmohammadhassanalavi.un_follow;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        SharedPreferences token_sharedPreference = getSharedPreferences("TOKEN_PREF", Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor_token = token_sharedPreference.edit();

        final Intent goToLogin = new Intent();
        goToLogin.setClass(this, MainActivity.class);

        Button Button_logout = (Button) findViewById(R.id.button_logout);

        Button_logout.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View V){
                editor_token.clear();
                editor_token.commit();
                startActivity(goToLogin);
            }
        });
    }
}
