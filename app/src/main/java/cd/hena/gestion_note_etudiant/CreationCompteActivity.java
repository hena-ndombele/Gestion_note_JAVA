package cd.hena.gestion_note_etudiant;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class CreationCompteActivity extends AppCompatActivity {


    private EditText nomEditText;
    private EditText postnomEditText;
    private EditText prenomEditText;
    private EditText emailEditText;
    private EditText passwordEditText;
    private TextView connexionBtn;
    private Button creerCompteBtn;

    private APIManager requete;

    ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creation_compte);
        init();
    }


    private void init() {
        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        nomEditText = findViewById(R.id.nomEditText);
        postnomEditText = findViewById(R.id.postnomEditText);
        prenomEditText = findViewById(R.id.prenomEditText);
        creerCompteBtn = findViewById(R.id.creercomptesBtn);
        connexionBtn = findViewById(R.id.connexionBtn);

        requete = new APIManager(getApplicationContext());
        ClicCreerCompte();
        ClicConnexion();
    }

    private void ClicCreerCompte() {
        creerCompteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                creerCompte();
            }
        });
    }

    private void ClicConnexion() {
        connexionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });
    }

    private void creerCompte() {
        String nom = nomEditText.getText().toString();
        String postnom = postnomEditText.getText().toString();
        String prenom = prenomEditText.getText().toString();
        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();

        // Vérifier si les champs email et mot de passe ne sont pas vides
        if (nom.isEmpty() || postnom.isEmpty() || prenom.isEmpty() || email.isEmpty() || password.isEmpty()) {
            Toast.makeText(getApplicationContext(), "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show();
            return;
        }

        String url = Constance.REGISTER_URL;

        // Créer un objet JSON avec les paramètres de la requête
        JSONObject params = new JSONObject();
        try {
            params.put("nom", nom);
            params.put("email", email);
            params.put("prenom", prenom);
            params.put("postnom", postnom);
            params.put("password", password);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        progressDialog = ProgressDialog.show(CreationCompteActivity.this, "", "Création du compte en cours...", true);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, params,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        progressDialog.dismiss();
                        try {
                            Log.d("CreerCompteEtudiant", "Réponse JSON : " + response.toString());
                            if (response.has("status")) {
                                boolean status = response.getBoolean("status");
                                if (status) {
                                    // Compte créé avec succès
                                    Toast.makeText(getApplicationContext(), "Compte créé avec succès", Toast.LENGTH_SHORT).show();
                                    // Rediriger vers l'activité de connexion après la création du compte
                                    Intent intent = new Intent(CreationCompteActivity.this, MainActivity.class);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    if (response.has("error")) {
                                        String error = response.getString("error");
                                        Toast.makeText(getApplicationContext(), "Erreur : " + error, Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(getApplicationContext(), "Statut non attendu dans la réponse JSON", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            } else {
                                Toast.makeText(getApplicationContext(), "Clé 'status' non trouvée dans la réponse JSON", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        Log.e("CreationCompteActivity", "Erreur de la requête : " + error.getMessage());
                        Toast.makeText(getApplicationContext(), "Erreur de la requête", Toast.LENGTH_SHORT).show();
                    }
                }) {
            // Override la méthode getHeaders() pour ajouter les en-têtes de la requête
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");
                return headers;
            }
        };

        // Ajouter la requête à la file d'attente de Volley
        requete.requestQueue.add(request);

    }


}