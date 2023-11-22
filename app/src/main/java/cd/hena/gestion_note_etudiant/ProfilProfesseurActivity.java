package cd.hena.gestion_note_etudiant;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class ProfilProfesseurActivity extends AppCompatActivity {

    private Button changerBtn;
    private TextView emailTextView;
    private Button deconnexionButton;
    ProgressDialog progressDialog;

    private SharedPreferences sharedPreferences;
    APIManager requete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil_professeur);
        sharedPreferences = getSharedPreferences("session", MODE_PRIVATE);
        init();
    }
    private void init(){

        changerBtn=findViewById(R.id.changerBtn);
        emailTextView = findViewById(R.id.emailTextView);
        String email = sharedPreferences.getString("email", "");
        emailTextView.setText(email);
        deconnexionButton=findViewById(R.id.deconnexionButton);
        requete = new APIManager(getApplicationContext());
        ClicDeconnexion();
        ClicChangerPasswod();
    }

    private void ClicChangerPasswod(){
        changerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(),ChangerPasswordActivity.class);
                startActivity(intent);
            }
        });
    }

    private void ClicDeconnexion(){
        deconnexionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deconnecter();
            }
        });
    }
    private void deconnecter() {
        String url = Constance.DECONNEXION_URL;
        String accessToken = "votre-jeton-d-acces";
        progressDialog = ProgressDialog.show(ProfilProfesseurActivity.this, "", "Deconnexion en cours...", true);

        StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Traitement de la réponse en cas de déconnexion réussie
                        // ...
                        progressDialog.dismiss();

                        Toast.makeText(getApplicationContext(), "vous êtes deconnecté", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(ProfilProfesseurActivity.this, IntroActivity.class);
                        startActivity(intent);
                        finish();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Gestion des erreurs lors de la déconnexion
                        // ...
                        Log.e("HomeActivity", "Erreur lors de la déconnexion: " + error.getMessage());
                        Toast.makeText(getApplicationContext(), "Erreur lors de la déconnexion", Toast.LENGTH_SHORT).show();
                        finish();

                    }
                }) {

            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + accessToken);
                return headers;
            }
        };

        // Ajoutez la requête à la file d'attente de Volley pour l'exécuter
        requete.requestQueue.add(request);
    }
}