package cd.hena.gestion_note_etudiant;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
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

public class ConnexionProfesseur extends AppCompatActivity {

 private EditText mailEditText;
 private EditText passEditText;
    private String email;
    private String password;
    ProgressDialog progressDialog;
    private TextView passwordssoublies;

    private Button connectssBtn;
    String name;

    APIManager requete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connexion_professeur);
        init();
    }


    private void init() {
        passwordssoublies = findViewById(R.id.passwordssoublies);
        connectssBtn = findViewById(R.id.connectssBtn);
        mailEditText = findViewById(R.id.mailEditText);
        passEditText = findViewById(R.id.passEditText);
        requete = new APIManager(getApplicationContext());
        ClicConnexion();
        ClicPassword();
    }

    private void ClicPassword() {
        passwordssoublies.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MotDePasseOublieActivity.class);
                startActivity(intent);
            }
        });
    }

    private void ClicConnexion() {
        connectssBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                envoyerInformationPourVerification();
            }
        });
    }

    private void envoyerInformationPourVerification() {
        // Récupérer les valeurs des EditText lorsqu'on clique sur le bouton "Se connecter"
        email = mailEditText.getText().toString();
        password = passEditText.getText().toString();

        // Vérifier si les champs email et mot de passe ne sont pas vides
        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(getApplicationContext(), "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show();
            return;
        }

        connexionUtilisateur(email, password, name);
    }

    private void connexionUtilisateur(String email, String password, String name) {
        String url = Constance.LOGIN_URL;
        JSONObject params = new JSONObject();
        try {
            params.put("email", email);
            params.put("password", password);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        progressDialog = ProgressDialog.show(ConnexionProfesseur.this, "", "Connexion en cours...", true);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, params,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        progressDialog.dismiss();
                        try {
                            Log.d("ConnexionProfesseur", "Réponse JSON : " + response.toString());
                            if (response.has("status")) {
                                int status = response.getInt("status");
                                if (status == 200) {
                                    // Connexion réussie
                                    String token = response.getJSONObject("data").getString("token");
                                    Toast.makeText(getApplicationContext(), "vous êtes connecté", Toast.LENGTH_SHORT).show();
                                    // Rediriger vers l'activité suivante après la connexion réussie
                                    Intent intent = new Intent(ConnexionProfesseur.this, TableauDeBordProfesseurActivity.class);
                                    intent.putExtra("email", email);
                                    startActivity(intent);
                                    finish();
                                    // Enregistrer les informations de l'utilisateur dans les SharedPreferences
                                    SharedPreferences sharedPreferences = getSharedPreferences("session", MODE_PRIVATE);
                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                    editor.putString("username", name);
                                    editor.putString("email", email);
                                    editor.putBoolean("isLoggedIn", true);
                                    editor.apply();
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
                        Log.e("ConnexionProfesseur", "Erreur de la requête : " + error.getMessage());
                        Toast.makeText(getApplicationContext(), "Erreur de la requête", Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");
                return headers;
            }
        };

        requete.requestQueue.add(request);
    }
}