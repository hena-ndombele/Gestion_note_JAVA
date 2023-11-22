package cd.hena.gestion_note_etudiant;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.material.navigation.NavigationView;

import java.util.HashMap;
import java.util.Map;

public class TableauDeBordProfesseurActivity extends AppCompatActivity  {
   // private DrawerLayout drawerLayout;

    private Button deconnexionButton;
    private Button aproposBtn;
    private Button profilBtn;
    private Button etudiantBtn;
    ProgressDialog progressDialog;
    private TextView emailTextView;


    private SharedPreferences sharedPreferences;

    APIManager requete;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tableau_de_bord_professeur);
        sharedPreferences = getSharedPreferences("session", MODE_PRIVATE);
        init();


/*
        androidx.appcompat.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //changer la couleur de l'icone et du texte de toolbar
        toolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.white));
        Drawable drawable = toolbar.getOverflowIcon();
        if (drawable != null) {
            drawable.setTint(ContextCompat.getColor(this, R.color.white));
        }
        // fin changer la couleur de l'icone et du texte de toolbar
        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open_nav, R.string.close_nav);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

       *//* if (savedInstanceState == null) {
            HomeFragement homeFragment = new HomeFragement();
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.fragement_container, homeFragment);
            fragmentTransaction.commit();
            navigationView.setCheckedItem(R.id.nav_home);
        }*//*
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.nav_note) {
            Intent intent = new Intent(getApplicationContext(), NoteActivity.class);
            startActivity(intent);
        } else if (itemId == R.id.nav_info) {
            Intent intent = new Intent(getApplicationContext(), AproposActivity.class);
            startActivity(intent);
        }
        else if (itemId == R.id.nav_info) {
            Intent intent = new Intent(getApplicationContext(), AproposActivity.class);
            startActivity(intent);
            // PartagerFragement partagerFragment = new PartagerFragement();
            // getSupportFragmentManager().beginTransaction().replace(R.id.fragement_container, partagerFragment).commit();
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }*/
}

private void init(){
    deconnexionButton=findViewById(R.id.deconnexionButton);
    aproposBtn=findViewById(R.id.aproposBtn);
    etudiantBtn=findViewById(R.id.etudiantBtn);
    emailTextView = findViewById(R.id.emailTextView);
    profilBtn= findViewById(R.id.profilBtn);
    String email = sharedPreferences.getString("email", "");
    emailTextView.setText(email);
    requete = new APIManager(getApplicationContext());
    ClicDeconnexion();
    ClicApropos();
    ClicNote();
    ClicProfil();
}


private void ClicProfil(){
    profilBtn.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent=new Intent(getApplicationContext(), ProfilProfesseurActivity.class);
            startActivity(intent);
        }
    });
}
private void ClicNote(){
    etudiantBtn.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(TableauDeBordProfesseurActivity.this, NoteActivity.class);
            startActivity(intent);
        }
    });
}

private void ClicApropos(){
    aproposBtn.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(TableauDeBordProfesseurActivity.this, AproposActivity.class);
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
        progressDialog = ProgressDialog.show(TableauDeBordProfesseurActivity.this, "", "Deconnexion en cours...", true);

        StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Traitement de la réponse en cas de déconnexion réussie
                        // ...
                        progressDialog.dismiss();

                        Toast.makeText(getApplicationContext(), "vous êtes deconnecté", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(TableauDeBordProfesseurActivity.this, IntroActivity.class);
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