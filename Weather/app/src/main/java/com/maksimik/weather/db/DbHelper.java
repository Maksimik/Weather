package com.maksimik.weather.db;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;
import android.util.Log;

import com.maksimik.weather.db.annotations.Contract;
import com.maksimik.weather.db.annotations.Table;
import com.maksimik.weather.db.annotations.type.dbDouble;
import com.maksimik.weather.db.annotations.type.dbInteger;
import com.maksimik.weather.db.annotations.type.dbIntegerPrimaryKeyAutoincrement;
import com.maksimik.weather.db.annotations.type.dbLong;
import com.maksimik.weather.db.annotations.type.dbString;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Locale;

public class DbHelper extends SQLiteOpenHelper implements IDbOperations {

    private static final String SQL_TABLE_CREATE_TEMPLATE = "CREATE TABLE %s (%s);";
    private static final String SQL_TABLE_CREATE_FIELD_TEMPLATE = "%s %s";
    private static final String SQL_NAME = "weatherForecast.db";

    public DbHelper(Context context, int version) {
        super(context, SQL_NAME, null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        for (final Class<?> clazz : Contract.MODELS) {
            final String sql = getTableCreateQuery(clazz);
            if (sql != null) {
                Log.i("TAG", "--- onCreate database ---");
                db.execSQL(sql);
            }
        }
    }

    @Nullable
    public static String getTableCreateQuery(final Class<?> clazz) {
        final Table table = clazz.getAnnotation(Table.class);

        if (table != null) {
            final Field[] fields = clazz.getFields();
            final StringBuilder builder = new StringBuilder();
            for (int i = 0; i < fields.length - 1; i++) {
                Field field = fields[i];
                String value = null, type = null;
                try {
                    value = (String) field.get(null);

                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
                final Annotation[] annotations = field.getAnnotations();

                for (final Annotation annotation : annotations) {
                    if (annotation instanceof dbIntegerPrimaryKeyAutoincrement) {
                        type = ((dbIntegerPrimaryKeyAutoincrement) annotation).value();
                    } else if (annotation instanceof dbString) {
                        type = ((dbString) annotation).value();
                    } else if (annotation instanceof dbInteger) {
                        type = ((dbInteger) annotation).value();
                    } else if (annotation instanceof dbLong) {
                        type = ((dbLong) annotation).value();
                    } else if (annotation instanceof dbDouble) {
                        type = ((dbDouble) annotation).value();
                    }
                }

                //TODO // FIXME: 09.11.2016
                if (type != null) {

                    builder.append(String.format(Locale.US, SQL_TABLE_CREATE_FIELD_TEMPLATE, value, type));
                    if (i < fields.length - 2) {
                        builder.append(",");
                    }
                }
            }
            return String.format(Locale.US, SQL_TABLE_CREATE_TEMPLATE, table.name(), builder);
        }
        return null;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    @Override
    public Cursor query(final String sql, final String... args) {
        final SQLiteDatabase database = getReadableDatabase();

        return database.rawQuery(sql, args);
    }

    @Override
    public int delete(final Class<?> table, final String sql, final String... args) {
        final String name = getTableName(table);

        if (name != null) {
            final SQLiteDatabase database = getWritableDatabase();
            int count = 0;

            try {
                database.beginTransaction();

                count = database.delete(name, sql, args);

                database.setTransactionSuccessful();
            } finally {
                database.endTransaction();
            }

            return count;
        } else {
            throw new RuntimeException();
        }
    }

    @Override
    public long insert(final Class<?> table, final ContentValues values) {
        final String name = getTableName(table);

        if (name != null) {
            final SQLiteDatabase database = getWritableDatabase();
            long id;

            try {
                database.beginTransaction();

                id = database.insert(name, null, values);

                database.setTransactionSuccessful();
            } finally {
                database.endTransaction();
            }

            return id;
        } else {
            throw new RuntimeException();
        }
    }

    @Override
    public int bulkInsert(final Class<?> table, final List<ContentValues> values) {
        final String name = getTableName(table);

        if (name != null) {
            final SQLiteDatabase database = getWritableDatabase();
            int count = 0;

            try {
                database.beginTransaction();

                for (final ContentValues value : values) {
                    database.insert(name, null, value);

                    count++;
                }

                database.setTransactionSuccessful();
            } finally {
                database.endTransaction();
            }

            return count;
        } else {
            throw new RuntimeException();
        }
    }


    @Nullable
    public static String getTableName(final AnnotatedElement clazz) {
        final Table table = clazz.getAnnotation(Table.class);

        if (table != null) {
            return table.name();
        } else {
            return null;
        }
    }
}

