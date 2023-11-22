package cd.hena.gestion_note_etudiant;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

public class ChangerPasswordActivity extends AppCompatActivity {
    private EditText ancienPasswordEditText;
    private EditText nouveauPasswordEditText;
    private EditText confirmerPasswordEditText;
    private Button changerPasswordBtn;

    private APIManager apiManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_changer_password);
        init();
    }

    private void init() {
        ancienPasswordEditText = findViewById(R.id.ancienPasswordEditText);
        nouveauPasswordEditText = findViewById(R.id.nouveauPasswordEditText);
        confirmerPasswordEditText = findViewById(R.id.confirmerPasswordEditText);
        changerPasswordBtn = findViewById(R.id.changerPasswordBtn);
        apiManager = new APIManager(getApplicationContext());

        changerPasswordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ancienPassword = ancienPasswordEditText.getText().toString();
                String nouveauPassword = nouveauPasswordEditText.getText().toString();
                String confirmerPassword = confirmerPasswordEditText.getText().toString();

                // Vérification des champs de mot de passe
                if (ancienPassword.isEmpty() || nouveauPassword.isEmpty() || confirmerPassword.isEmpty()) {
                    Toast.makeText(ChangerPasswordActivity.this, "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show();
                } else if (!nouveauPassword.equals(confirmerPassword)) {
                    Toast.makeText(ChangerPasswordActivity.this, "Le nouveau mot de passe ne correspond pas à la confirmation", Toast.LENGTH_SHORT).show();
                } else {
                    // Appel de la méthode pour envoyer la requête de changement de mot de passe
                    changerMotDePasse(ancienPassword, nouveauPassword);
                }
            }
        });
    }

    private void changerMotDePasse(String ancienPassword, String nouveauPassword) {
        // URL de l'API pour changer le mot de passe
        String url = Constance.PASSWORD_URL;

        // Création de l'objet JSON à envoyer
        JSONObject jsonRequest = new JSONObject();
        try {
            jsonRequest.put("old_password", ancienPassword);
            jsonRequest.put("new_password", nouveauPassword);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Création de la requête HTTP POST avec le corps JSON
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, jsonRequest,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // Vérification de la réponse de l'API pour déterminer si le mot de passe a été changé
                        try {
                            boolean success = response.getBoolean("success");
                            String message = response.getString("message");
                            if (success) {
                                Toast.makeText(ChangerPasswordActivity.this, message, Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(ChangerPasswordActivity.this, MainActivity.class);
                                startActivity(intent);
                                finish();
                            } else {
                                Toast.makeText(ChangerPasswordActivity.this, message, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(ChangerPasswordActivity.this, "Erreur lors du traitement de la réponse de l'API", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Traitement de l'erreur de l'API
                        Toast.makeText(ChangerPasswordActivity.this, "Erreur lors du changement de mot de passe", Toast.LENGTH_SHORT).show();
                    }
                });

        // Ajout de la requête à la file d'attente de Volley
        apiManager.requestQueue.add(request);
    }
}