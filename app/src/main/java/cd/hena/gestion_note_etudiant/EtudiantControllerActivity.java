package cd.hena.gestion_note_etudiant;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

//import retrofit2.Call;
//import retrofit2.Callback;
//import retrofit2.Response;


public class EtudiantControllerActivity {
    private static Etudiant etudiants;

    private static final String BASE_URL = Constance.BASE_ETUDIANT_ALL;
    // private static final String BASE_URL = "https://jsonplaceholder.typicode.com/users/";


    private Context context;
    public void sendEtudiant(Etudiant etudiant, final OnEtudiantSentListener listener) {
        OkHttpClient client = new OkHttpClient();

        String url = BASE_URL; // URL pour envoyer les données de l'étudiant

        // Convertir l'objet Etudiant en JSON à l'aide de Gson ou tout autre moyen
        String jsonBody = convertEtudiantToJson(etudiant);

        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), jsonBody);

        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    listener.onEtudiantSent();
                } else {
                    String errorMessage = response.message();
                    listener.onSendError(errorMessage);
                }
            }

            @Override
            public void onFailure(Call call, IOException e) {
                String errorMessage = e.getMessage();
                listener.onSendError(errorMessage);
            }
        });
    }

    public EtudiantControllerActivity(Context context) {
        this.context = context;
    }
    // Méthode pour enregistrer un nouvel étudiant à la fois dans la base de données distante et localement
    public void fetchEtudiants(final OnEtudiantsFetchedListener listener) {
        OkHttpClient client = new OkHttpClient();

        String url = BASE_URL; // URL pour récupérer tous les étudiants

        Request request = new Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String responseBody = response.body().string();
                    Log.d("TAG", "reponse: " + responseBody);
                    // Convertir responseBody en une liste d'objets Etudiant en utilisant Gson ou tout autre moyen
                    // ...

                    // List<Datum> etudiant = new Etudiant.setData
                    etudiants = convertResponseBodyToEtudiants(responseBody);

                    if (etudiants != null) {
                        Log.d("TAG", "body"+etudiants);
                        listener.onEtudiantsFetched(etudiants);

                    }
                } else {
                    String errorMessage = response.message();
                    listener.onFetchError(errorMessage);
                }
            }

            @Override
            public void onFailure(Call call, IOException e) {
                String errorMessage = e.getMessage();
                listener.onFetchError(errorMessage);
            }
        });
    }
    private Etudiant convertResponseBodyToEtudiants(String responseBody) {
        Gson gson = new Gson();

        Etudiant etudiants = gson.fromJson(responseBody, Etudiant.class);
        return etudiants;
    }

    // Méthode pour récupérer la liste des étudiants depuis la base de données distante
    //public void fetchEtudiants(final OnEtudiantsFetchedListener listener) {
    // Effectuez une requête HTTP à votre API REST pour récupérer les données des étudiants
    // Utilisez des bibliothèques telles que Retrofit ou Volley pour effectuer la requête

    // Exemple d'utilisation de Retrofit :


    // Méthode pour enregistrer un étudiant localement à l'aide des SharedPreferences
    public void saveEtudiantLocally(Etudiant etudiant) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        //editor.putString("etudiant", etudiant.toJson());
        //editor.apply();
    }
    // Méthode pour récupérer l'étudiant enregistré localement à partir des SharedPreferences
    public Etudiant getEtudiantLocally() {
        SharedPreferences sharedPreferences = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        String etudiantJson = sharedPreferences.getString("etudiant", null);
        if (etudiantJson != null) {
            try {
                JSONObject jsonObject = new JSONObject(etudiantJson);
                String nom = jsonObject.optString("nom");
                String prenom = jsonObject.optString("prenom");
                String post_nom = jsonObject.optString("post_nom");
                String promotion = jsonObject.optString("promotion");
                // Construisez et retournez l'objet Etudiant
                return new Etudiant();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
    // Interface pour notifier les événements de récupération des étudiants
    public static interface OnEtudiantsFetchedListener {
        void onEtudiantsFetched(Etudiant etudiant);



        void onFetchError(String errorMessage);


    }

    public interface OnEtudiantSentListener {
        void onEtudiantSent();
        void onSendError(String errorMessage);
    }
    private String convertEtudiantToJson(Etudiant etudiant) {
        Gson gson = new Gson();
        return gson.toJson(etudiant);
    }
    public static void processEtudiants(List<EtudiantModelActivity> etudiantsList,List<DataNote> notesList,String  c, TriConsumer<EtudiantModelActivity, String, DataNote, String > action) {
        Log.d("TAG", "process: " + etudiantsList);
        for (EtudiantModelActivity etudiant : etudiantsList) {
            long id_etudiant = etudiant.getID();
            String completNom = etudiant.getNom() + " " + etudiant.getPrenom();
            DataNote noteEtudiant = NoteControllerActivity.noteEtudiant((long) etudiant.getID(),notesList);


            String cote = NoteControllerActivity.getCodeEtudiant(noteEtudiant);

            action.accept(etudiant, cote, noteEtudiant,c);
        }
    }
    @FunctionalInterface
    public interface TriConsumer<T, U, DataNote,Y> {
        void accept(T t, U u, DataNote v,Y y);
    }
    public static void seachEtudiant(String c, EditText searchEditText, LinearLayout linearEtudiantContainer,List<DataNote> notesList) {
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Effectuer la recherche à chaque modification du texte
                performSearch(s.toString(), c,linearEtudiantContainer,notesList);


            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


    }

    private static void performSearch(String searchText, String c, LinearLayout linearEtudiantContainer, List<DataNote> notesList) {
        linearEtudiantContainer.removeAllViews();
        if (searchText.isEmpty()) {

            TextView textView = new TextView(new NoteActivity());
            textView.setText("Rechercher un etudiant");
            linearEtudiantContainer.addView(textView);
        } else {
            linearEtudiantContainer.removeAllViews();
            // Le texte n'est pas vide, filtrer la liste pour les éléments correspondants à la recherche
            List<EtudiantModelActivity> filteredList = new ArrayList<>(); // Initialisation de la liste vide

            for (EtudiantModelActivity item : etudiants.getData()) {
                if (item.getNom().contains(searchText) || item.getPrenom().contains(searchText)) {
                    filteredList.add(item);
                }
            }
            EtudiantControllerActivity.processEtudiants(etudiants.getData(), notesList,c,(completNom, cote, noteEtudiant,cours) -> {

            });

            // Créer une nouvelle interface avec les éléments correspondants à la recherche

        }

        Toast.makeText(new NoteActivity(), "Recherche : " + searchText, Toast.LENGTH_SHORT).show();
    }

}


