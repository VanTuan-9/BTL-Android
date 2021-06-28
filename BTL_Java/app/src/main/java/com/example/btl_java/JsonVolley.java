package com.example.btl_java;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class JsonVolley {
    private static JsonVolley newJsonVolley;
    private RequestQueue requestQueue;

    private JsonVolley(Context context){
        if(requestQueue == null)
            requestQueue = Volley.newRequestQueue(context.getApplicationContext());
    }

    public static synchronized JsonVolley getInstance(Context context){
        if(newJsonVolley == null)
            newJsonVolley = new JsonVolley(context);
        return newJsonVolley;
    }

    public RequestQueue getRequestQueue() {
        return requestQueue;
    }
}
