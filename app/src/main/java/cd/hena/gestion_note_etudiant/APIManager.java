package cd.hena.gestion_note_etudiant;
import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class APIManager {

    private static final String TAG = "APIManager";
    private Context context;
    RequestQueue requestQueue;

    public APIManager(Context context) {
        this.context = context;
        requestQueue = Volley.newRequestQueue(context);
    }
}
