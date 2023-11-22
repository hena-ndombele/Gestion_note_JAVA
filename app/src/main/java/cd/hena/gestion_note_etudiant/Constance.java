package cd.hena.gestion_note_etudiant;

public class Constance {


    public static final String IP = "192.168.50.51";
    public static final String PORT = "8000";
    public static final String BASE_URL = "http://" + IP + ":" + PORT;


    //ENDPOINT

    public static final String LOGIN_URL=BASE_URL + "/api/auth/login";
    public static final String REGISTER_URL=BASE_URL + "/api/auth/register";
    public static final String DECONNEXION_URL=BASE_URL + "/api/auth/logout";

    public static final String PASSWORD_URL=BASE_URL + "api/auth/changerpassword";
   public static final String  BASE_COUR = BASE_URL + "/api/cours/getAllCoursesAsOfProfessor/";
   public static final String  BASE_NOTE_PROFESSEUR = BASE_URL + "/api/note/getAllNoteforCours";
   public static final String  BASE_ETUDIANT_ALL = BASE_URL + "/api/etudiant";

    public static final String  BASE_ETUDIANT_SEND = BASE_URL + "/api/note/";

}




