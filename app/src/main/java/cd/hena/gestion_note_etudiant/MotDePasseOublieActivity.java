package cd.hena.gestion_note_etudiant;

import androidx.appcompat.app.AppCompatActivity;


import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class MotDePasseOublieActivity extends AppCompatActivity {


    private EditText oublie;
    private Button oublieBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mot_de_passe_oublie);
        init();
    }


    private void init(){
        oublie=findViewById(R.id.oublie);
        oublieBtn=findViewById(R.id.oublieBtn);
        ClicButton();
    }

    private void ClicButton(){
        oublieBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Bientôt disponible", Toast.LENGTH_SHORT).show();
            }
        });
    }






}