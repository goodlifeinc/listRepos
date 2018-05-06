package com.example.evgeni.listrepos;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    TextView text;
    EditText input;
    Button button;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        text = (TextView)findViewById(R.id.text);
        input = (EditText)findViewById(R.id.input);
        button = (Button)findViewById(R.id.button);

        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view)
            {
                sendRequest(input.getText().toString());
            }
        });
    }

    public void sendRequest(String username) {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = String.format("https://api.github.com/users/%s/repos", username);

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JsonParser parser = new JsonParser();
                        JsonArray json = (JsonArray) parser.parse(response);
                        List<String> repos = new ArrayList<>();

                        for (Object o : json) {
                            if ( o instanceof JsonObject) {
                                repos.add(((JsonObject) o).get("full_name").getAsString());
                            }
                        }

                        text.setText(TextUtils.join(System.getProperty("line.separator"), repos));
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                text.setText("That didn't work!");
            }
        });

        queue.add(stringRequest);
    }
}