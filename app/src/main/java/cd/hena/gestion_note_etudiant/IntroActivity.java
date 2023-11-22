package cd.hena.gestion_note_etudiant;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.content.SharedPreferences;

import android.widget.Button;

public class IntroActivity extends AppCompatActivity {

    private Button profBtn;
    private Button etudiantsBtn;
    private SharedPreference sharedPreference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);
        sharedPreference = new SharedPreference(this);
        init();
    }

    private void init() {
        profBtn = findViewById(R.id.profBtn);
        etudiantsBtn = findViewById(R.id.etudiantsBtn);

        ClicProfButton();
        ClicEtudiantsButton();
    }

    private void ClicProfButton() {
        profBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Enregistrer le type d'utilisateur comme professeur dans les préférences partagées
                sharedPreference.saveUserTypeProfesseur();

                // Rediriger vers l'écran de connexion pour les professeurs
                Intent intent = new Intent(getApplicationContext(), ConnexionProfesseur.class);
                startActivity(intent);
            }
        });
    }

    private void ClicEtudiantsButton() {
        etudiantsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Enregistrer le type d'utilisateur comme étudiant dans les préférences partagées
                sharedPreference.saveUserTypeEtudiant();

                // Rediriger vers l'écran de connexion pour les étudiants
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Vérifier si l'utilisateur est déjà connecté
        if (sharedPreference.isLoggedIn()) {
            String userType = sharedPreference.getUserType();
            if (userType != null) {
                if (userType.equals(SharedPreference.USER_TYPE_PROFESSEUR)) {
                    // L'utilisateur est un professeur, rediriger vers l'écran approprié
                    Intent intent = new Intent(getApplicationContext(), TableauDeBordProfesseurActivity.class);
                    startActivity(intent);
                    finish();
                } else if (userType.equals(SharedPreference.USER_TYPE_ETUDIANT)) {
                    // L'utilisateur est un étudiant, rediriger vers l'écran approprié
                    Intent intent = new Intent(getApplicationContext(), TableauDeBordEtudiant.class);
                    startActivity(intent);
                    finish();
                }
            }
        }
    }
}