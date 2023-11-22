
package cd.hena.gestion_note_etudiant;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class PersistenceSqlLite extends SQLiteOpenHelper {

    private static final String DATABASE_NAME="easy.db";
    private static final int DATABASE_VERSION=2;

    private static final String CREATE_TABLE_UTILISATEUR = "CREATE TABLE utilisateur (id INTEGER PRIMARY KEY AUTOINCREMENT,nom TEXT, postnom TEXT, prenom TEXT, email TEXT UNIQUE, password TEXT);";



    public PersistenceSqlLite(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }



    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_UTILISATEUR);
    }




    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        if (oldVersion < 2) {
            //db.execSQL("DROP TABLE IF EXISTS client");
            db.execSQL(CREATE_TABLE_UTILISATEUR);
        }
    }


    public long creerUtilisateur(Utilisateur utilisateur) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("prenom", utilisateur.getPrenom());
        values.put("postnom", utilisateur.getPostnom());
        values.put("nom", utilisateur.getNom());
        values.put("email", utilisateur.getEmail());
        values.put("password", utilisateur.getPassword());

        return db.insert("utilisateur", null, values);
    }

    public boolean verifierConnexion(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query("utilisateur", new String[]{"id"}, "email = ? AND password = ?", new String[]{email, password}, null, null, null);
        boolean isConnected = cursor.getCount() > 0;
        cursor.close();
        return isConnected;
    }

    public static class Utilisateur {
        private String prenom;
        private String nom;
        private String postnom;
        private String email;
        private String password;


        public Utilisateur(String postnom,String prenom, String nom, String email, String password ) {
            this.prenom = prenom;
            this.nom = nom;
            this.postnom = postnom;
            this.email = email;
            this.password = password;

        }

        public String getPrenom() {
            return prenom;
        }


        public String getPostnom() {
            return postnom;
        }

        public String getNom() {
            return nom;
        }

        public String getEmail() {

            return email;
        }

        public String getPassword() {
            return password;
        }

    }
}

