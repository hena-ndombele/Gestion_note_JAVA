package cd.hena.gestion_note_etudiant;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.content.Intent;

import android.os.Handler;

public class SplashActivity extends AppCompatActivity {

    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        sharedPreferences = getSharedPreferences("session", MODE_PRIVATE);
        // Vérifier si l'utilisateur est déjà connecté
        if (isLoggedIn()) {
            redirectToHome();
        } else {
            // Rediriger vers la page MainActivity après un délai de 3 secondes
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(getApplicationContext(), IntroActivity.class);
                    startActivity(intent);
                    finish();
                }
            }, 3000);
        }
    }

    private boolean isLoggedIn() {
        // Vérifier si la clé "isLoggedIn" est définie à true dans les SharedPreferences
        return sharedPreferences.getBoolean("isLoggedIn", false);
    }

    private void redirectToHome() {
        // Vérifier le type de l'utilisateur connecté
        String userType = sharedPreferences.getString("userType", "");
        Intent intent;
        if (userType.equals("professeur")) {
            intent = new Intent(SplashActivity.this, TableauDeBordProfesseurActivity.class);
        } else if (userType.equals("etudiant")) {
            intent = new Intent(SplashActivity.this, TableauDeBordEtudiant.class);
        } else {
            // Si le type d'utilisateur n'est pas défini correctement, rediriger vers la page de connexion
            intent = new Intent(SplashActivity.this, MainActivity.class);
        }
        startActivity(intent);
        finish();
    }
}