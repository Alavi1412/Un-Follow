package seyedmohammadhassanalavi.un_follow;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;
import org.json.JSONException;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.V;

public class MainActivity extends AppCompatActivity {

    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("native-lib");
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences token_sharedPreference = getSharedPreferences("TOKEN_PREF", Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor_token = token_sharedPreference.edit();

        final Intent goToHome = new Intent();
        goToHome.setClass(this, HomeActivity.class);

        if(token_sharedPreference.contains("TOKEN"))
        {
            String ACCESS_TOKEN = token_sharedPreference.getString("TOKEN", "default");
            Toast.makeText(this, ACCESS_TOKEN, Toast.LENGTH_SHORT).show();

            RequestQueue Queue = Volley.newRequestQueue(this);
            String url ="https://api.instagram.com/v1/users/self/?access_token=" + ACCESS_TOKEN;
            JsonObjectRequest JsObjReq = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>(){
                @Override
                public void onResponse(JSONObject response) {
                    Toast.makeText(getApplicationContext(), response.toString(), Toast.LENGTH_SHORT).show();
                    startActivity(goToHome);
                }
            }, new Response.ErrorListener(){
                @Override
                public void onErrorResponse(VolleyError error){
                    Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show();

                }
            });
            Queue.add(JsObjReq);
        }

        final WebView instagram_login = (WebView) findViewById(R.id.instagram_login);
        instagram_login.setWebViewClient(new WebViewClient(){

            public void onPageFinished(WebView view, String url){
                if (url.contains("#"))
                {
                    int equal_position = url.indexOf("=");
                    String ACCESS_TOKEN = url.substring(equal_position + 1);
                    Toast.makeText(getApplicationContext(),"با موفقیت وارد شدید ;-)" , Toast.LENGTH_LONG).show();
                    editor_token.putString("TOKEN", ACCESS_TOKEN);
                    editor_token.apply();
                    CookieSyncManager.createInstance(getApplicationContext());
                    CookieManager cookieManager = CookieManager.getInstance();
                    cookieManager.removeAllCookie();
                    startActivity(goToHome);
                }
            }

        });
        instagram_login.loadUrl("https://www.instagram.com/oauth/authorize/?client_id=" + getResources().getString(R.string.Client_ID) + "&redirect_uri=" + getResources().getString(R.string.Redirect_URI) +"&response_type=token");


    }


    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    public native String stringFromJNI();
}
