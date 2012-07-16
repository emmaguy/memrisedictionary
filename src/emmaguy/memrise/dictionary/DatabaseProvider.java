package emmaguy.memrise.dictionary;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class DatabaseProvider extends SQLiteOpenHelper
{
    private static final String DATABASE_NAME = "MemriseDictionary";
    private static final String WORDS_TABLE_NAME = "Words";

    private static final String WORD_COLUMN_NAME = "Word";
    private static final String DEFINITION_COLUMN_NAME = "Definition";

    private static final int DATABASE_VERSION = 1;

    DatabaseProvider(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public List<LearntWord> GetAllWords()
    {
        List<LearntWord> words = new ArrayList<LearntWord>();
        String[] columns = { WORD_COLUMN_NAME, DEFINITION_COLUMN_NAME};
        try
        {
            SQLiteDatabase database = getReadableDatabase();
            Cursor cur = database.query(WORDS_TABLE_NAME, columns, null, null, null, null, null);

            if(cur.moveToFirst())
            {
                do
                {
                    words.add(new LearntWord(cur.getString(0), cur.getString(1)));
                }
                while(cur.moveToNext());
            }
            cur.close();
            database.close();
        }
        catch(Exception ex)
        {
            Log.e("db", ex.toString());
        }
        return words;
    }

    public void SaveWords(List<LearntWord> words)
    {
        if(words.size() <= 0)
            return;
        try
        {
            SQLiteDatabase database = getWritableDatabase();
            DatabaseUtils.InsertHelper ih = new DatabaseUtils.InsertHelper(database, WORDS_TABLE_NAME);

            final int wordColumn = ih.getColumnIndex(WORD_COLUMN_NAME);
            final int defnitionColumn = ih.getColumnIndex(DEFINITION_COLUMN_NAME);

            try
            {
                for(LearntWord word : words)
                {
                    ih.prepareForInsert();

                    ih.bind(wordColumn, word.getWord());
                    ih.bind(defnitionColumn, word.getTranslatedWord());

                    ih.execute();
                }
            }
            finally
            {
                ih.close();
            }

            database.close();
        }
        catch (Exception e)
        {
            Log.e("db", e.toString());
        }
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL("CREATE TABLE " + WORDS_TABLE_NAME +
                " (WordId INTEGER PRIMARY KEY AUTOINCREMENT," +
                WORD_COLUMN_NAME + "  VARCHAR(128)," +
                DEFINITION_COLUMN_NAME + " Definition VARCHAR(128));");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        Log.w("db", "Upgrading database from version " + oldVersion + " to "
                + newVersion + ", which will destroy all old data");
        delete(db);
        onCreate(db);
    }

    public void create()
    {
        SQLiteDatabase database = getWritableDatabase();
        onCreate(database);
    }

    public void delete()
    {
        SQLiteDatabase database = getWritableDatabase();
        delete(database);
    }

    private void delete(SQLiteDatabase db)
    {
        db.execSQL("DROP TABLE IF EXISTS " + WORDS_TABLE_NAME);
    }
}