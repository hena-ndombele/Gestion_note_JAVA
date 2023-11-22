package cd.hena.gestion_note_etudiant;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.ContactsContract;
import android.util.Log;


import com.google.gson.Gson;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

//import retrofit2.Call;
//import retrofit2.Callback;
//import retrofit2.Response;


public class NoteControllerActivity{
    private static NoteModelActivity notes;

    private static final String BASE_URL = Constance.BASE_NOTE_PROFESSEUR;
    // private static final String BASE_URL = "https://jsonplaceholder.typicode.com/users/";


    private Context context;


    public  NoteControllerActivity(Context context) {
        this.context = context;
    }

    public static DataNote noteEtudiant(long id,List<DataNote> noteList) {
        for (DataNote note : noteList){
            if(note.getEtudiant()==id){
                return note;}
        }
        return null;
    }

    public static String getCodeEtudiant(DataNote noteEtudiant) {
        if(noteEtudiant!=null){
            return noteEtudiant.getCote();
        }
        return " ";
    }





    public void fetchNotes(final OnNotesFetchedListener listener,String c) {
        OkHttpClient client = new OkHttpClient();

        String url = BASE_URL; // URL pour récupérer tous les étudiants

        Request request = new Request.Builder()
                .url(url+c+"/")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String responseBody = response.body().string();
                    Log.d("TAG", "reponseNote: " + response.body());
                    // Convertir responseBody en une liste d'objets Etudiant en utilisant Gson ou tout autre moyen
                    // ...

                    // List<Datum> etudiant = new Etudiant.setData
                    notes = convertResponseBodyToEtudiants(responseBody);

                    if (notes != null) {
                        Log.d("TAG", "body"+ notes);
                        listener.onNotesFetched(notes);

                    }
                } else {
                    String errorMessage = response.message();
                    listener.onFetchErrorNotes(errorMessage);
                }
            }

            @Override
            public void onFailure(Call call, IOException e) {
                String errorMessage = e.getMessage();
                listener.onFetchErrorNotes(errorMessage);
            }
        });
    }
    private NoteModelActivity convertResponseBodyToEtudiants(String responseBody) {
        Gson gson = new Gson();

        NoteModelActivity note = gson.fromJson(responseBody, NoteModelActivity.class);
        return note;
    }

    // Méthode pour récupérer la liste des étudiants depuis la base de données distante
    //public void fetchEtudiants(final OnEtudiantsFetchedListener listener) {
    // Effectuez une requête HTTP à votre API REST pour récupérer les données des étudiants
    // Utilisez des bibliothèques telles que Retrofit ou Volley pour effectuer la requête

    // Exemple d'utilisation de Retrofit :


    // Méthode pour enregistrer un étudiant localement à l'aide des SharedPreferences

    public void saveNoteLocally(DataNote data) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("Notes", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        // Convertir l'objet Note en une chaîne JSON à l'aide de Gson
        Gson gson = new Gson();
        String noteJson = gson.toJson(data);

        // Enregistrer la chaîne JSON dans les SharedPreferences
        editor.putString("note", noteJson);
        editor.apply();
    }



    // Méthode pour récupérer l'étudiant enregistré localement à partir des SharedPreferences





    // Interface pour notifier les événements de récupération des étudiants
    public static interface OnNotesFetchedListener {
        void onNotesFetched(NoteModelActivity Note);
        void onFetchErrorNotes(String errorMessage);
    }
    public void sendNote(Map<String,DataNote> notes, final OnNoteSentListener listener) {
        OkHttpClient client = new OkHttpClient();
        for (Map.Entry<String,DataNote> entry : notes.entrySet()) {


            String url = Constance.BASE_ETUDIANT_SEND;

            // Convertir l'objet Etudiant en JSON à l'aide de Gson ou tout autre moyen
            String jsonBody = convertNoteToJson(entry.getValue());

            RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), jsonBody);

            Request request = new Request.Builder()
                    .url(url)
                    .post(requestBody)
                    .build();

            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    if (response.isSuccessful()) {
                        listener.onNoteSent();
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
            });}


    }
    public static interface OnNoteSentListener {
        void onNoteSent();
        void onSendError(String errorMessage);
    }
    private String convertNoteToJson(DataNote note) {
        Gson gson = new Gson();
        return gson.toJson(note);
    }
    private String convertNoteUpdataToJson(DataNote note) {
        Gson gson = new Gson();
        return gson.toJson(note);
    }
    public void updateNote(Map<String,DataNote> notes, final OnNoteSentListener listener) {

        OkHttpClient client = new OkHttpClient();
        for (Map.Entry<String,DataNote> entry : notes.entrySet()) {
            String url =Constance.BASE_ETUDIANT_SEND + entry.getKey(); // URL pour mettre à jour les données de l'étudiant

            // Convertir l'objet Etudiant en JSON à l'aide de Gson ou tout autre moyen
            String jsonBody = convertNoteUpdataToJson(entry.getValue());

            RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), jsonBody);

            Request request = new Request.Builder()
                    .url(url)
                    .put(requestBody)
                    .build();

            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    if (response.isSuccessful()) {
                        listener.onNoteSent();
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
    }
}


