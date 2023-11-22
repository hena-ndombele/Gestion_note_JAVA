package cd.hena.gestion_note_etudiant;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreference {
    private static final String PREF_NAME = "session";
    private static final String KEY_TOKEN = "token";
    private static final String KEY_USER_TYPE = "userType";
    public static final String USER_TYPE_PROFESSEUR = "professeur";
    public static final String USER_TYPE_ETUDIANT = "etudiant";

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private Context context;

    public SharedPreference(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public void saveToken(String token) {
        editor.putString(KEY_TOKEN, token);
        editor.apply();
    }

    public String getToken() {
        return sharedPreferences.getString(KEY_TOKEN, null);
    }

    public boolean isLoggedIn() {
        return getToken() != null;
    }

    public void clearSession() {
        editor.clear();
        editor.apply();
    }

    public void saveUserTypeProfesseur() {
        editor.putString(KEY_USER_TYPE, USER_TYPE_PROFESSEUR);
        editor.apply();
    }

    public void saveUserTypeEtudiant() {
        editor.putString(KEY_USER_TYPE, USER_TYPE_ETUDIANT);
        editor.apply();
    }

    public String getUserType() {
        return sharedPreferences.getString(KEY_USER_TYPE, null);
    }
}