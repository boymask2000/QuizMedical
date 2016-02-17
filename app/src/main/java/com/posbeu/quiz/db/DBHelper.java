package com.posbeu.quiz.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.widget.Toast;

import com.posbeu.quiz.db.bean.Domanda;
import com.posbeu.quiz.db.bean.Materia;
import com.posbeu.quiz.db.bean.Parametro;
import com.posbeu.quiz.db.bean.Risposta;
import com.posbeu.quiz.db.bean.SelectionParam;
import com.posbeu.quiz.db.bean.Statistiche;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class DBHelper extends SQLiteOpenHelper {
    private static final String DB_FILENAME = "quiz.db";
    private static final String DB_NAME = "quiz";
    private static final int DB_VERSION = 3;
    //private static final String HOTWORDS_SEP="\\|";
    private static final String HOTWORDS_SEP = ",";
    private static String TABLE_MATERIE = "Materie";
    private static String TABLE_DOMANDE = "domanda";
    private static String TABLE_RISPOSTE = "risposte";
    private static String TABLE_STAT = "statistiche";
    private static String TABLE_PARAMETRI = "parametri";
    private static DBHelper helper = null;
    //	private Context context;
    private boolean mInvalidDatabaseFile = false;
    private File DATABASE_FILE;

    private DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        DATABASE_FILE = context.getDatabasePath(DB_NAME);
        //	this.context = context;
        SQLiteDatabase db = null;
        try {
            db = getReadableDatabase();
            if (db != null) {
                db.close();
            }
            if (mInvalidDatabaseFile) {
                importDB(db, context);
            }
        } catch (SQLiteException e) {
        } finally {
            if (db != null && db.isOpen()) {
                db.close();
            }
        }

    }

    private static void init(SQLiteDatabase db) {
        db.execSQL("DELETE FROM " + TABLE_PARAMETRI);

        Parametro par = new Parametro();
        par.setParm(Parameter.CURRENT_LEVEL);
        par.setValue("0");
        insertParametro(db, par);

        par.setParm(Parameter.LEVEL_0_PROMOTION);
        par.setValue("2");
        insertParametro(db, par);

        par.setParm(Parameter.LEVEL_1_PROMOTION);
        par.setValue("2");
        insertParametro(db, par);

        par.setParm(Parameter.LEVEL_2_PROMOTION);
        par.setValue("2");
        insertParametro(db, par);

        par.setParm(Parameter.LEVEL_3_PROMOTION);
        par.setValue("2");
        insertParametro(db, par);

        par.setParm(Parameter.LEVEL_4_PROMOTION);
        par.setValue("2000000000000");
        insertParametro(db, par);
    }

    public static long insertParametro(SQLiteDatabase db, Parametro par) {


        ContentValues values = new ContentValues();

        values.put("parm", par.getParm());
        values.put("value", par.getValue());

        long id = db.insert(TABLE_PARAMETRI, null, values);

        return id;
    }

    public static DBHelper getHelper(Context context) {
        if (helper == null)
            helper = new DBHelper(context);
        return helper;
    }

    public static String getDbName() {
        return DB_NAME;
    }

    public static void exportDB(Context context) {
        exportDataBase(context);

    }

    private static void exportDataBase(Context context) {
        SQLiteDatabase db = getHelper(context).getReadableDatabase();

        String sd = Environment.getExternalStorageDirectory().getAbsolutePath();

        FileChannel source = null;
        FileChannel destination = null;

        String currentDBPath = db.getPath();
        System.out.println(currentDBPath);
        String backupDBPath = "quiz_backup.db";// BACKUP_DB;

        File currentDB = new File(currentDBPath);
        File backupDB = new File(sd + File.separator + backupDBPath);
        try {
            source = new FileInputStream(currentDB).getChannel();
            destination = new FileOutputStream(backupDB).getChannel();
            destination.transferFrom(source, 0, source.size());
            source.close();
            destination.close();
            Toast.makeText(context, "DB Exported!", Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

  /*  public static boolean isTableExists(SQLiteDatabase db, String tableName) {
        if (tableName == null || db == null || !db.isOpen()) {
            return false;
        }
        Cursor cursor = db
                .rawQuery(
                        "SELECT COUNT(*) FROM sqlite_master WHERE type = ? AND name = ?",
                        new String[]{"table", tableName});
        if (!cursor.moveToFirst()) {
            return false;
        }
        int count = cursor.getInt(0);
        cursor.close();
        return count > 0;
    }*/

    public static int exportDataBaseNet(Context context, String ip) {
        Socket s = null;
        int total = 0;
        OutputStream output = null;
        try {
            s = new Socket(ip, 22000);

            SQLiteDatabase db = getHelper(context).getReadableDatabase();
            db.close();

            String currentDBPath = db.getPath();
            //      long ss = (new File(currentDBPath)).length();
            System.out.println(currentDBPath);

            FileInputStream source = new FileInputStream(currentDBPath);

            output = s.getOutputStream();

            byte buffer[] = new byte[1024];

            int bytesRead;
            while ((bytesRead = source.read(buffer)) > 0) {
                output.write(buffer, 0, bytesRead);
                total += bytesRead;
            }
            output.flush();
            output.close();
            source.close();
        } catch (Exception e1) {
            e1.printStackTrace();
        } finally {
            try {
                if (s != null)
                    s.close();
                if (output != null)
                    output.close();
            } catch (IOException e) {

                e.printStackTrace();
            }
        }
        return total;
    }

    public static long insertParametro(Context context, Parametro par) {
        SQLiteDatabase db = getHelper(context).getWritableDatabase();

        return insertParametro(db, par);


    }

    public static int updateParametro(Context context, String par, String key) {
        SQLiteDatabase db = getHelper(context).getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put("value", key);
        String whereClause = "parm	=	?	";
        String[] whereArgs = {par};
        int r = db.update(TABLE_PARAMETRI, values, whereClause, whereArgs);

        return r;
    }

    public static String getParametro(Context context, String par) {
        Cursor cursor = null;
        SQLiteDatabase db = getHelper(context).getReadableDatabase();
        String val = "";
        String query = "select * from " + TABLE_PARAMETRI + " where parm=?";
        String[] selectionArgs = {par};
        cursor = db.rawQuery(query, selectionArgs);
        if (cursor.moveToNext()) {
            val = cursor.getString(2);

        }
        cursor.close();
        return val;
    }

    public static long insertMateria(Context context, Materia materia) {
        SQLiteDatabase db = getHelper(context).getWritableDatabase();
        Materia m = getMateria(context, materia.getDescrizione());
        if (m != null)
            deleteMateria(db, materia.getDescrizione());
        ContentValues values = new ContentValues();

        values.put("descrizione", materia.getDescrizione());
        values.put("codice", materia.getCodice());
        values.put("id_padre", materia.getId_padre());

        return db.insert(TABLE_MATERIE, null, values);
    }

    public static Materia getMateria(Context context, String descr) {
        Cursor cursor = null;
        SQLiteDatabase db = getHelper(context).getReadableDatabase();
        String query = null;
        Materia materia = null;

        query = "select * from " + TABLE_MATERIE + " where descrizione=?";
        String[] selectionArgs = {"" + descr};
        cursor = db.rawQuery(query, selectionArgs);
        if (cursor.moveToNext()) {
            long id0 = cursor.getLong(0);
            String descrizione = cursor.getString(1);
            String codice = cursor.getString(2);
            long id_padre = cursor.getLong(3);

            materia = new Materia();
            materia.setDescrizione(descrizione);
            materia.setCodice(codice);


        }
        cursor.close();
        return materia;
    }

    private static void deleteMateria(SQLiteDatabase db, String descr) {
        String whereClause = "descrizione	=	?";
        String[] whereArgs = {"" + descr};
        int r = db.delete(TABLE_MATERIE, whereClause, whereArgs);

    }

    public static void deleteStat(Context context) {
        SQLiteDatabase db = getHelper(context).getWritableDatabase();
        String whereClause = "";
        String[] whereArgs = {""};
        db.execSQL("delete from	" + TABLE_STAT);

    }

    public static long insertDomanda(Context context, Domanda domanda) {
        SQLiteDatabase db = getHelper(context).getWritableDatabase();

        Domanda m = getDomanda(context, domanda.getDomanda_id());
        if (m != null)
            deleteDomanda(db, domanda.getDomanda_id());

        ContentValues values = new ContentValues();

        values.put("domanda_id", domanda.getDomanda_id());
        values.put("spiegazione", domanda.getSpiegazione());
        values.put("ordine", domanda.getOrdine());
        values.put("punti", domanda.getPunti());
        values.put("image", domanda.getImage());
        values.put("difficolta", domanda.getDifficolta());
        values.put("testo_domanda", domanda.getTestoDomanda());
        values.put("materia_id", domanda.getMateria_id());
        values.put("categoria", domanda.getCategoria());
        values.put("argomento", domanda.getArgomento());
        values.put("capitolo", domanda.getCapitolo());
        values.put("parolechiave", domanda.getHotWords());
        // if( domanda.getImage()!=null)updateDomanda(context,domanda.getImage());

        return db.insert(TABLE_DOMANDE, null, values);
    }

    public static int updateDomanda(Context context, String image) {
        SQLiteDatabase db = getHelper(context).getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put("image", image);
        String whereClause = "";
        String[] whereArgs = {};
        return db.update("domanda", values, whereClause, whereArgs);

    }

    private static void deleteDomanda(SQLiteDatabase db, long id) {
        String whereClause = "domanda_id	=	?";
        String[] whereArgs = {"" + id};
        int r = db.delete(TABLE_RISPOSTE, whereClause, whereArgs);
        r = db.delete(TABLE_DOMANDE, whereClause, whereArgs);
    }

    public static Domanda getDomanda(Context context, long id) {
        Cursor cursor = null;
        SQLiteDatabase db = getHelper(context).getReadableDatabase();
        String query = null;
        Domanda domanda = null;

        query = "select * from " + TABLE_DOMANDE + " where domanda_id=?";
        String[] selectionArgs = {"" + id};
        cursor = db.rawQuery(query, selectionArgs);
        if (cursor.moveToNext()) {
            long id0 = cursor.getLong(0);

            String testoDomanda = cursor.getString(1);
            long difficolta = cursor.getLong(2);
            String image = cursor.getString(3);
            String spiegazione = cursor.getString(4);
            long punti = cursor.getLong(5);
            String categoria = cursor.getString(6);
            String argomento = cursor.getString(7);
            long ordine = cursor.getLong(8);
            long materia_id = cursor.getLong(9);
            long domanda_id = cursor.getLong(10);
            String hotWords = cursor.getString(11);
            String capitolo = cursor.getString(12);

            domanda = new Domanda();
            domanda.setPunti(punti);
            domanda.setMateria_id(materia_id);
            domanda.setDomanda_id(domanda_id);
            domanda.setCategoria(categoria);
            domanda.setArgomento(argomento);
            domanda.setDomanda_id(id0);
            domanda.setSpiegazione(spiegazione);
            domanda.setOrdine(ordine);
            domanda.setImage(image);
            domanda.setDifficolta(difficolta);
            domanda.setTestoDomanda(testoDomanda);
            domanda.setCapitolo(capitolo);
            domanda.setHotWords(hotWords);

        }
        cursor.close();
        return domanda;
    }

    public static List<String> getHotwords(Context context) {
        Cursor cursor = null;
        SQLiteDatabase db = getHelper(context).getReadableDatabase();
        String query = null;
        List<String> out = new ArrayList<String>();

        query = "select * from " + TABLE_DOMANDE + " where parolechiave is not null";
        String[] selectionArgs = {};
        cursor = db.rawQuery(query, selectionArgs);
        while (cursor.moveToNext()) {

            String hotWords = cursor.getString(11);
            parseHotword(out, hotWords);
            // out.add(hotWords);

        }
        cursor.close();
        filterDuplicates(out);
        return out;
    }

    private static void parseHotword(List<String> out, String s) {
        if (s == null || s.trim().length() == 0) return;
        String[] vals = s.split(HOTWORDS_SEP);
        for (int i = 0; i < vals.length; i++)
            if (vals[i] != null && vals[i].trim().length() > 0)
                out.add(vals[i]);
    }

    private static void filterDuplicates(List<String> out) {

        HashSet<String> hash = new HashSet<String>();
        for (String s : out)
            hash.add(s);

        out.clear();
        for (String s : hash) {
            out.add(s);
        }

        Collections.sort(out);
    }

    public static int updateDomanda(Context context, String image, long key) {
        SQLiteDatabase db = getHelper(context).getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put("image", image);
        String whereClause = "difficolta	=	?	";
        String[] whereArgs = {"" + key};
        return db.update("domanda", values, whereClause, whereArgs);

    }

    public static long insertRisposta(Context context, Risposta risposta) {
        SQLiteDatabase db = getHelper(context).getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("esatta", risposta.isEsatta() ? "S" : "");
        values.put("risposta", risposta.getRisposta());
        values.put("spiegazione", risposta.getSpiegazione());
        values.put("ordine", risposta.getOrdine());
        values.put("domanda_id", risposta.getDomanda_id());

        long id = db.insert(TABLE_RISPOSTE, null, values);

        return id;
    }

    public static List<Statistiche> getAllstat(Context context) {
        List<Statistiche> result = new ArrayList<Statistiche>();

        String query = "select * from " + TABLE_STAT + " order by _id desc";
        String[] selectionArgs = {};
        SQLiteDatabase db = getHelper(context).getReadableDatabase();

        Cursor cursor = db.rawQuery(query, selectionArgs);
        while (cursor.moveToNext()) {
            int id = cursor.getInt(0);
            String data = cursor.getString(1); // `data` TEXT,
            int risposte_ok = cursor.getInt(2);// `risposte_ok` INTEGER,
            int risposte_ko = cursor.getInt(3);// `risposte_ko` INTEGER,

            int risposte_ok_1 = cursor.getInt(4);// `` INTEGER,
            int risposte_ok_2 = cursor.getInt(5);// `` INTEGER,
            int risposte_ok_3 = cursor.getInt(6);// `` INTEGER,
            int risposte_ok_4 = cursor.getInt(7);// `` INTEGER,
            int risposte_ok_5 = cursor.getInt(8);// `` INTEGER,

            int risposte_ko_1 = cursor.getInt(9);// `` INTEGER,
            int risposte_ko_2 = cursor.getInt(10);// `` INTEGER,
            int risposte_ko_3 = cursor.getInt(11);// `` INTEGER,
            int risposte_ko_4 = cursor.getInt(12);// `` INTEGER,
            int risposte_ko_5 = cursor.getInt(13);// `` INTEGER,
            int num_domande = cursor.getInt(14);// `` INTEGER,
            String livello = cursor.getString(15);

            Statistiche d = new Statistiche();

            d.setData(data);
            d.setNumRisposteKO(risposte_ko);
            d.setNumRisposteOK(risposte_ok);
            d.setNumDomande(num_domande);
            d.setRispostaOK(risposte_ok_1, 0);
            d.setRispostaOK(risposte_ok_2, 1);
            d.setRispostaOK(risposte_ok_3, 2);
            d.setRispostaOK(risposte_ok_4, 3);
            d.setRispostaOK(risposte_ok_5, 4);

            d.setRispostaKO(risposte_ko_1, 0);
            d.setRispostaKO(risposte_ko_2, 1);
            d.setRispostaKO(risposte_ko_3, 2);
            d.setRispostaKO(risposte_ko_4, 3);
            d.setRispostaKO(risposte_ko_5, 4);
            d.setLivello(livello);
            result.add(d);

        }
        cursor.close();
        return result;
    }

    public static List<Materia> getAllMateria(Context context) {
        List<Materia> result = new ArrayList<Materia>();
        HashMap<String, String> map = new HashMap<String, String>();

        String query = "select * from " + TABLE_MATERIE;
        String[] selectionArgs = {};
        SQLiteDatabase db = getHelper(context).getReadableDatabase();

        Cursor cursor = db.rawQuery(query, selectionArgs);
        while (cursor.moveToNext()) {
            int id = cursor.getInt(0);
            String descrizione = cursor.getString(1);
            if (map.get(descrizione) != null) continue;
            map.put(descrizione, descrizione);
            String codice = cursor.getString(2);
            long idPadre = cursor.getInt(3);
            Materia d = new Materia();
            d.setMateria_id(id);
            d.setDescrizione(descrizione);
            d.setCodice(codice);
            d.setId_padre(idPadre);
            result.add(d);

        }
        cursor.close();
        return result;
    }

    public static List<Domanda> getDomande(Context context, SelectionParam param) {
        List<Domanda> result = new ArrayList<Domanda>();

        Cursor cursor = null;

        SQLiteDatabase db = getHelper(context).getReadableDatabase();
/*		String query = null;
        if (param.getLivello() == 0) {
			query = "select * from " + TABLE_DOMANDE + " where materia_id=?";
			String[] selectionArgs = { "" + param.getMateria().getMateria_id() };
			cursor = db.rawQuery(query, selectionArgs);
		} else {
			query = "select * from " + TABLE_DOMANDE
					+ " where materia_id=? and difficolta=?";
			String[] selectionArgs = { "" + param.getMateria().getMateria_id(),
					"" + param.getLivello() };
			cursor = db.rawQuery(query, selectionArgs);
		}
String[] selectionArgs = { "" + param.getMateria().getMateria_id() };*/
        String out = "";
        ArrayList<String> pp = new ArrayList<String>();
        if (param.getMateria() != null && param.getMateria().getDescrizione().trim().length() > 0 && !param.getMateria().isAll()) {
            out = "materia_id=?";
            pp.add("" + param.getMateria().getMateria_id());
        }
        if (param.getLivello() > 0) {
            if (out.length() > 0) out += " and ";
            out += "difficolta=?";
            pp.add("" + param.getLivello());
        }

        if (pp.size() == 0) {
            String query = "select * from " + TABLE_DOMANDE;
            String[] selectionArgs = {};
            cursor = db.rawQuery(query, selectionArgs);
        }

        if (pp.size() == 1) {
            String[] selectionArgs = {""};
            selectionArgs = (String[]) pp.toArray(selectionArgs);

            String query = "select * from " + TABLE_DOMANDE;
            if (out.length() > 0) query += " where " + out;
            cursor = db.rawQuery(query, selectionArgs);
        }

        if (pp.size() == 2) {
            String[] selectionArgs = {"", ""};
            selectionArgs = (String[]) pp.toArray(selectionArgs);

            String query = "select * from " + TABLE_DOMANDE;
            if (out.length() > 0) query += " where " + out;
            cursor = db.rawQuery(query, selectionArgs);
        }


        while (cursor.moveToNext()) {

            String testoDomanda = cursor.getString(1);
            long difficolta = cursor.getLong(2);
            String image = cursor.getString(3);
            long ordine = cursor.getLong(5);
            String spiegazione = cursor.getString(4);
            String capitolo = cursor.getString(12);
            long domanda_id = cursor.getLong(10);
            String hots = cursor.getString(11);

            Domanda domanda = new Domanda();
            domanda.setDomanda_id(domanda_id);
            domanda.setSpiegazione(spiegazione);
            domanda.setOrdine(ordine);
            domanda.setImage(image);
            domanda.setDifficolta(difficolta);
            domanda.setTestoDomanda(testoDomanda);
            domanda.setCapitolo(capitolo);
            domanda.setHotWords(hots);

            List<Risposta> ll = DBHelper.getAllRisposte(context, domanda);
            domanda.setRisposte(ll);

            result.add(domanda);

        }
        cursor.close();
        return result;
    }

    public static List<Materia> getAllMateriaWithParent0(Context context) {
        List<Materia> result = new ArrayList<Materia>();

        String query = "select * from " + TABLE_MATERIE + " where id_padre=?";
        String[] selectionArgs = {"0"};
        SQLiteDatabase db = getHelper(context).getReadableDatabase();

        Cursor cursor = db.rawQuery(query, selectionArgs);
        while (cursor.moveToNext()) {
            int id = cursor.getInt(0);
            String descrizione = cursor.getString(1);
            String codice = cursor.getString(2);
            long idPadre = cursor.getInt(3);
            Materia d = new Materia();
            d.setMateria_id(id);
            d.setDescrizione(descrizione);
            d.setCodice(codice);
            d.setId_padre(idPadre);
            result.add(d);

        }
        cursor.close();
        return result;
    }

    public static List<Risposta> getAllRisposte(Context context, Domanda domanda) {
        List<Risposta> result = new ArrayList<Risposta>();

        String query = "select * from " + TABLE_RISPOSTE
                + " where domanda_id=? order by ordine";
        String[] selectionArgs = {"" + domanda.getDomanda_id()};

        SQLiteDatabase db = getHelper(context).getReadableDatabase();

        Cursor cursor = db.rawQuery(query, selectionArgs);
        while (cursor.moveToNext()) {
            long id = cursor.getLong(0);
            long domanda_id = cursor.getLong(1);
            String esatta = cursor.getString(2);
            String testoRisposta = cursor.getString(3);
            int punti = cursor.getInt(4);
            int ordine = cursor.getInt(5);
            String spiegazione = cursor.getString(6);

            Risposta risposta = new Risposta();
            risposta.setRisposta_id(id);
            risposta.setDomanda_id(domanda_id);
            risposta.setRisposta(testoRisposta);
            risposta.setEsatta(esatta.equals("S"));

            risposta.setSpiegazione(spiegazione);

            result.add(risposta);

        }
        cursor.close();
        return result;
    }

    public static void writeStat(Context context, Statistiche stat) {
        SQLiteDatabase db = getHelper(context).getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put("data", stat.getData());
        values.put("num_domande", stat.getNumDomande());
        values.put("risposte_ok", stat.getNumRisposteOK());
        values.put("risposte_ko", stat.getNumRisposteKO());
        values.put("risposte_ok_1", stat.getRisposteOK()[0]);
        values.put("risposte_ok_2", stat.getRisposteOK()[1]);
        values.put("risposte_ok_3", stat.getRisposteOK()[2]);
        values.put("risposte_ok_4", stat.getRisposteOK()[3]);
        values.put("risposte_ok_5", stat.getRisposteOK()[4]);
        values.put("risposte_ko_1", stat.getRisposteKO()[0]);
        values.put("risposte_ko_2", stat.getRisposteKO()[1]);
        values.put("risposte_ko_3", stat.getRisposteKO()[2]);
        values.put("risposte_ko_4", stat.getRisposteKO()[3]);
        values.put("risposte_ko_5", stat.getRisposteKO()[4]);
        values.put("livello", stat.getLivello());

        long id = db.insert(TABLE_STAT, null, values);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        mInvalidDatabaseFile = true;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int old_version,
                          int new_version) {
        mInvalidDatabaseFile = true;
        if (new_version == 3 && new_version > old_version) {
            db.execSQL("ALTER TABLE " + TABLE_DOMANDE + " ADD COLUMN parolechiave TEXT");

            String sql = "create table " + TABLE_PARAMETRI + "(";
            sql += "_id INTEGER PRIMARY KEY,";
            sql += "parm TEXT,";
            sql += "value TEXT";
            sql += ")";
            db.execSQL(sql);

            init(db);

        }
    }

    public void importDB(SQLiteDatabase db, Context context) {

        //  SQLiteDatabase db = getHelper(context).getReadableDatabase();
//		String sd = Environment.getExternalStorageDirectory().getAbsolutePath();
//
//		File root = Environment.getExternalStorageDirectory();
        InputStream is = null;
        try {
            is = context.getAssets().open(DB_FILENAME);

            String currentDBPath = db.getPath();
            System.out.println(currentDBPath);
            // String backupDBPath = BACKUP_DB;

            File currentDB = new File(currentDBPath);
            FileOutputStream os = new FileOutputStream(currentDB);

            byte[] buffer = new byte[1024];
            try {
                int l;
                while ((l = is.read(buffer)) != -1) {
                    os.write(buffer, 0, l);
                }
                os.close();
                is.close();

                Toast.makeText(context, "DB loaded!", Toast.LENGTH_LONG).show();
            } catch (IOException e) {
                e.printStackTrace();
            }
            setDatabaseVersion();
            mInvalidDatabaseFile = false;
        } catch (Exception e) {

            e.printStackTrace();
        }

    }

    private void setDatabaseVersion() {
        SQLiteDatabase db = null;
        try {
            db = SQLiteDatabase.openDatabase(DATABASE_FILE.getAbsolutePath(),
                    null, SQLiteDatabase.OPEN_READWRITE);
            db.execSQL("PRAGMA user_version = " + DB_VERSION);
        } catch (SQLiteException e) {
        } finally {
            if (db != null && db.isOpen()) {
                db.close();
            }
        }
    }

}
