package com.example.dadmsql.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.dadmsql.db.util.DetalleEmpresa;

import java.util.ArrayList;
import java.util.List;

public class EmpresasDBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "empresas.db";

    public EmpresasDBHelper(Context context){
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(
                "CREATE TABLE empresa " +
                        "(id integer PRIMARY KEY, nombre text, url text, telefono text, email text, producto text, clasificacion text)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS empresa");
        onCreate(db);
    }

    /**
     *
     * @param nombreEmpresa
     * @param url
     * @param email
     * @param telefono
     * @param productos
     * @param clasificacion
     */
    public boolean registrarEmpresa(String nombreEmpresa, String url, String email, String telefono, String productos, String clasificacion){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(DetalleEmpresa.NOMBRE_COLUMNA_NOMBRE_EMPRESA.getNombre(), nombreEmpresa);
        contentValues.put(DetalleEmpresa.NOMBRE_COLUMNA_URL.getNombre(), url);
        contentValues.put(DetalleEmpresa.NOMBRE_COLUMNA_EMAIL_CONTACTO.getNombre(), email);
        contentValues.put(DetalleEmpresa.NOMBRE_COLUMNA_TELEFONO_CONTACTO.getNombre(), telefono);
        contentValues.put(DetalleEmpresa.NOMBRE_COLUMNA_PRODUCTOS.getNombre(), productos);
        contentValues.put(DetalleEmpresa.NOMBRE_COLUMNA_CLASIFICACION.getNombre(), clasificacion);

        db.insert("empresa", null, contentValues);

        return true;
    }

    /**
     *
     * @param id
     * @param nombreEmpresa
     * @param url
     * @param email
     * @param telefono
     * @param productos
     * @param clasificacion
     * @return
     */
    public boolean actualizarEmpresa (Integer id,String nombreEmpresa, String url, String email, String telefono, String productos, String clasificacion) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DetalleEmpresa.NOMBRE_COLUMNA_NOMBRE_EMPRESA.getNombre(), nombreEmpresa);
        contentValues.put(DetalleEmpresa.NOMBRE_COLUMNA_URL.getNombre(), url);
        contentValues.put(DetalleEmpresa.NOMBRE_COLUMNA_EMAIL_CONTACTO.getNombre(), email);
        contentValues.put(DetalleEmpresa.NOMBRE_COLUMNA_TELEFONO_CONTACTO.getNombre(), telefono);
        contentValues.put(DetalleEmpresa.NOMBRE_COLUMNA_PRODUCTOS.getNombre(), productos);
        contentValues.put(DetalleEmpresa.NOMBRE_COLUMNA_CLASIFICACION.getNombre(), clasificacion);

        db.update("empresa", contentValues, "id = ? ", new String[] { Integer.toString(id) } );

        return true;
    }

    /**
     *
     * @return
     */
    public List<String> obtenerEmpresasTodas() {
        List<String> empresas = new ArrayList<>();

        List<EmpresaDTO> empresasDTO = obtenerEmpresas();

        for(EmpresaDTO empresa : empresasDTO) {
            empresas.add(empresa.getNombre());
        }

        return empresas;
    }

    /**
     *
     * @return
     */
    public List<Integer> obtenerIdEmpresas() {
        List<Integer> empresasId = new ArrayList<>();

        List<EmpresaDTO> empresasDTO = obtenerEmpresas();

        for(EmpresaDTO empresa : empresasDTO) {
            empresasId.add(empresa.getId());
        }

        return empresasId;
    }

    /**
     *
     * @return
     */
    public List<String> obtenerEmpresasTodasFiltro(String filtroNombre, String filtroClasificacion) {
        List<String> empresas = new ArrayList<>();

        List<EmpresaDTO> empresasDTO = obtenerEmpresasFiltro(filtroNombre, filtroClasificacion);

        for(EmpresaDTO empresa : empresasDTO) {
            empresas.add(empresa.getNombre());
        }

        return empresas;
    }

    /**
     *
     * @return
     */
    public List<Integer> obtenerIdEmpresas(String filtroNombre, String filtroClasificacion) {
        List<Integer> empresasId = new ArrayList<>();

        List<EmpresaDTO> empresasDTO = obtenerEmpresasFiltro(filtroNombre, filtroClasificacion);

        for(EmpresaDTO empresa : empresasDTO) {
            empresasId.add(empresa.getId());
        }

        return empresasId;
    }

    private List<EmpresaDTO> obtenerEmpresas(){

        List<EmpresaDTO> empresas = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "SELECT * FROM empresa", null );
        res.moveToFirst();

        while(res.isAfterLast() == false){
            empresas.add(new EmpresaDTO(
                    res.getInt(res.getColumnIndex(DetalleEmpresa.NOMBRE_COLUMNA_ID.getNombre())),
                    res.getString(res.getColumnIndex(DetalleEmpresa.NOMBRE_COLUMNA_NOMBRE_EMPRESA.getNombre())),
                    res.getString(res.getColumnIndex(DetalleEmpresa.NOMBRE_COLUMNA_URL.getNombre())),
                    res.getString(res.getColumnIndex(DetalleEmpresa.NOMBRE_COLUMNA_TELEFONO_CONTACTO.getNombre())),
                    res.getString(res.getColumnIndex(DetalleEmpresa.NOMBRE_COLUMNA_EMAIL_CONTACTO.getNombre())),
                    res.getString(res.getColumnIndex(DetalleEmpresa.NOMBRE_COLUMNA_PRODUCTOS.getNombre())),
                    res.getString(res.getColumnIndex(DetalleEmpresa.NOMBRE_COLUMNA_CLASIFICACION.getNombre()))
            ));
            res.moveToNext();
        }

        return empresas;
    }

    /** Obtener empresas filtradas */
    private List<EmpresaDTO> obtenerEmpresasFiltro(String filtroNombre, String filtroClasificacion){
        List<EmpresaDTO> empresas = new ArrayList<>();

        StringBuilder sqlQuery = new StringBuilder();
        sqlQuery.append("SELECT * FROM empresa");

        if(filtroNombre != null || filtroClasificacion != null) {
            sqlQuery.append(" WHERE");
        }

        if(filtroNombre != null) {
            sqlQuery.append(" LOWER(nombre) LIKE '%");
            sqlQuery.append(filtroNombre.toLowerCase());
            sqlQuery.append("%'");
        }

        if(filtroNombre != null && filtroClasificacion != null){
            sqlQuery.append(" AND");
        }

        if(filtroClasificacion != null) {
            sqlQuery.append(" LOWER(clasificacion) LIKE '%");
            sqlQuery.append(filtroClasificacion.toLowerCase());
            sqlQuery.append("%'");
        }

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( sqlQuery.toString(), null );
        res.moveToFirst();

        while(res.isAfterLast() == false){
            empresas.add(new EmpresaDTO(
                    res.getInt(res.getColumnIndex(DetalleEmpresa.NOMBRE_COLUMNA_ID.getNombre())),
                    res.getString(res.getColumnIndex(DetalleEmpresa.NOMBRE_COLUMNA_NOMBRE_EMPRESA.getNombre())),
                    res.getString(res.getColumnIndex(DetalleEmpresa.NOMBRE_COLUMNA_URL.getNombre())),
                    res.getString(res.getColumnIndex(DetalleEmpresa.NOMBRE_COLUMNA_TELEFONO_CONTACTO.getNombre())),
                    res.getString(res.getColumnIndex(DetalleEmpresa.NOMBRE_COLUMNA_EMAIL_CONTACTO.getNombre())),
                    res.getString(res.getColumnIndex(DetalleEmpresa.NOMBRE_COLUMNA_PRODUCTOS.getNombre())),
                    res.getString(res.getColumnIndex(DetalleEmpresa.NOMBRE_COLUMNA_CLASIFICACION.getNombre()))
            ));
            res.moveToNext();
        }

        return empresas;
    }

    /**
     *
     * @param id
     * @return
     */
    public Integer eliminarEmpresa (Integer id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("empresa",
                "id = ? ",
                new String[] { Integer.toString(id) });
    }

    /**
     *
     * @param id
     * @return
     */
    public EmpresaDTO obtenerDetalles(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( String.format("SELECT * FROM empresa WHERE id = %s", id), null );
        res.moveToFirst();

        EmpresaDTO empresa =  new EmpresaDTO(
                res.getString(res.getColumnIndex(DetalleEmpresa.NOMBRE_COLUMNA_NOMBRE_EMPRESA.getNombre())),
                res.getString(res.getColumnIndex(DetalleEmpresa.NOMBRE_COLUMNA_URL.getNombre())),
                res.getString(res.getColumnIndex(DetalleEmpresa.NOMBRE_COLUMNA_TELEFONO_CONTACTO.getNombre())),
                res.getString(res.getColumnIndex(DetalleEmpresa.NOMBRE_COLUMNA_EMAIL_CONTACTO.getNombre())),
                res.getString(res.getColumnIndex(DetalleEmpresa.NOMBRE_COLUMNA_PRODUCTOS.getNombre())),
                res.getString(res.getColumnIndex(DetalleEmpresa.NOMBRE_COLUMNA_CLASIFICACION.getNombre()))
        );

        if(!res.isClosed()){
            res.close();
        }

        return empresa;
    }

    /**
     *
     * @return
     */
    public int numberOfRows(){
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, "empresa");

        return numRows;
    }
}
