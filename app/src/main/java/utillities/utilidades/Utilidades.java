package utillities.utilidades;

public class Utilidades {

    //TODO los items necesarios para manejar la conexion

    public static String DB_NAME = "db_pindo";

    public static String USER = "postgres";
    public static String PASS = "postgres";
    public static int PORT = 5432;
    public static String DB = "pindo";
    public static String HOST = "10.0.2.2";
    public static String CLASS_DB = "org.postgresql.Driver";

    //Tabla Usuarios empleada para el inicio de session administrador
    public static final String TABLA_USUARIOS = "usuarios";
    public static final String USUARIOS_CAMPO_ID = "id";
    public static final String USUARIOS_CAMPO_USUARIO = "usuario";
    public static final String USUARIOS_CAMPO_PASSWORD = "password";

    public static final String CREATE_TABLA_USUARIOS = "CREATE TABLE " + TABLA_USUARIOS + " (" + USUARIOS_CAMPO_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + USUARIOS_CAMPO_USUARIO + " TEXT not null unique, " + USUARIOS_CAMPO_PASSWORD + " TEXT)";

    public static final String ADD_USUARIO_TABLA_USUARIO = "INSERT INTO " + TABLA_USUARIOS + " VALUES ('1', 'pindosa', 'pindo123456')";


    //Tabla de Configuracion de POSTGIS
    public static final String TABLA_POSTGIS_CONFIG = "postgis_config";
    public static final String POSTGIS_CONFIG_ID = "id";
    public static final String POSTGIS_CONFIG_USER = "user";
    public static final String POSTGIS_CONFIG_PASSWORD = "password";
    public static final String POSTGIS_CONFIG_HOST = "host";
    public static final String POSTGIS_CONFIG_DB = "db";
    public static final String POSTGIS_CONFIG_PORT = "port";

    public static final String CREATE_TABLA_POSTGIS_CONFIG = "CREATE TABLE " + TABLA_POSTGIS_CONFIG + " (" + POSTGIS_CONFIG_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + POSTGIS_CONFIG_USER + " TEXT, " + POSTGIS_CONFIG_PASSWORD + " TEXT, " + POSTGIS_CONFIG_HOST + " TEXT, " + POSTGIS_CONFIG_DB + " TEXT, " + POSTGIS_CONFIG_PORT + " INTEGER)";

    public static final String ADD_ENTIDAD_TABLA_POSTGIS = "INSERT INTO " + TABLA_POSTGIS_CONFIG + " VALUES ('1', 'postgres', 'postgres', '10.0.2.2', 'pindo', '5432')";
    //192.168.1.7 192.168.0.18


    //Creo la tabla Rodales
    public static final String TABLA_RODALES = "rodales";
    public static final String RODALES_idrodales = "idrodales";
    public static final String RODALES_cod_sap = "cod_sap";
    public static final String RODALES_campo = "campo";
    public static final String RODALES_uso = "uso";
    public static final String RODALES_finalizado = "finalizado";
    public static final String RODALES_empresa = "empresa";
    public static final String RODALES_geometry = "geometry";
    public static final String RODALES_LAT = "lat";
    public static final String RODALES_LONG = "longi";

    //Agregar estas variables tmb
    public static final String RODALES_Fecha_plantacion = "fecha_plantacion";
    public static final String RODALES_Fecha_inv = "fecha_inv";
    public static final String RODALES_especie = "especie";

    public static final String CREATE_TABLA_RODALES = "CREATE TABLE " + TABLA_RODALES + " (" + RODALES_idrodales + " INTEGER PRIMARY KEY, "
            + RODALES_cod_sap + " TEXT, " + RODALES_campo + " TEXT, " + RODALES_uso + " TEXT, " + RODALES_finalizado + " TEXT, " + RODALES_empresa
            + " TEXT, " + RODALES_geometry + " TEXT, " + RODALES_LAT + " REAL, " + RODALES_LONG + " REAL, " + RODALES_Fecha_plantacion + " TEXT, " +
            RODALES_Fecha_inv + " TEXT, " + RODALES_especie + " TEXT)";



    /**
     * RODALES RELEVAMIENTO
     */

    public static final String TABLA_RODALES_RELEVAMIENTO = "rodales_relevamiento";
    public static final String RODALES_RELEVAMIENTO_id = "id";
    public static final String RODALES_RELEVAMIENTO_idrodales = "idrodales";
    public static final String RODALES_RELEVAMIENTO_cod_sap = "cod_sap";
    public static final String RODALES_RELEVAMIENTO_campo = "campo";
    public static final String RODALES_RELEVAMIENTO_uso = "uso";
    public static final String RODALES_RELEVAMIENTO_finalizado = "finalizado";
    public static final String RODALES_RELEVAMIENTO_empresa = "empresa";
    public static final String RODALES_RELEVAMIENTO_geometry = "geometry";
    public static final String RODALES_RELEVAMIENTO_LAT = "lat";
    public static final String RODALES_RELEVAMIENTO_LONG = "longi";

    public static final String RODALES_RELEVAMIENTO_Fecha_plantacion = "fecha_plantacion";
    public static final String RODALES_RELEVAMIENTO_Fecha_inv = "fecha_inv";
    public static final String RODALES_RELEVAMIENTO_especie = "especie";

    public static final String CREATE_TABLA_RODALES_RELEVAMIENTO = "CREATE TABLE " + TABLA_RODALES_RELEVAMIENTO + " (" + RODALES_RELEVAMIENTO_id + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + RODALES_RELEVAMIENTO_idrodales + " INTEGER NOT NULL UNIQUE, " + RODALES_RELEVAMIENTO_cod_sap + " TEXT, " + RODALES_RELEVAMIENTO_campo + " TEXT, " + RODALES_RELEVAMIENTO_uso + " TEXT, "
            + RODALES_RELEVAMIENTO_finalizado + " TEXT, "
            + RODALES_RELEVAMIENTO_empresa + " TEXT, " + RODALES_RELEVAMIENTO_geometry + " TEXT, " + RODALES_RELEVAMIENTO_LAT + " REAL, " + RODALES_RELEVAMIENTO_LONG + " REAL, " +
            RODALES_RELEVAMIENTO_Fecha_plantacion + " TEXT, " + RODALES_RELEVAMIENTO_Fecha_inv + " TEXT, " + RODALES_RELEVAMIENTO_especie + " TEXT)";




    /*
    * TABLA PARCELAS RELEVAMIENTO
     */

    public static final String TABLA_PARCELAS_RELEVAMIENTO = "parcelas_relevamiento";
    public static final String PARCELAS_RELEVAMIENTO_id = "id"; //ID EN SQLITE
    public static final String PARCELAS_RELEVAMIENTO_cod_sap = "cod_sap";
    public static final String PARCELAS_RELEVAMIENTO_idparcela = "idparcela"; //ESTE CORRESPONDE AL idgis
    public static final String PARCELAS_RELEVAMIENTO_superficie = "superficie";
    public static final String PARCELAS_RELEVAMIENTO_pendiente = "pendiente";
    public static final String PARCELAS_RELEVAMIENTO_parcela = "parcela";
    public static final String PARCELAS_RELEVAMIENTO_lat = "lat"; //Pertenece al centroide
    public static final String PARCELAS_RELEVAMIENTO_longi = "longi"; //Pertenece al centroide
    public static final String PARCELAS_RELEVAMIENTO_comentarios = "comentarios";
    public static final String PARCELAS_RELEVAMIENTO_geometry = "geometry";
    public static final String PARCELAS_RELEVAMIENTO_releva = "releva"; //DEFAULT value es 0..
    public static final String PARCELAS_RELEVAMIENTO_idrodales = "idrodales";
    public static final String PARCELAS_RELEVAMIENTO_lat_point_rel = "lat_point_rel";
    public static final String PARCELAS_RELEVAMIENTO_longi_point_rel = "longi_point_rel";

    public static final String CREATE_TABLA_PARCELAS_RELEVAMIENTO = "CREATE TABLE " + TABLA_PARCELAS_RELEVAMIENTO + " (" + PARCELAS_RELEVAMIENTO_id + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + PARCELAS_RELEVAMIENTO_cod_sap + " TEXT, " + PARCELAS_RELEVAMIENTO_idparcela + " INTEGER NOT NULL UNIQUE, " + PARCELAS_RELEVAMIENTO_superficie + " REAL, " +
            PARCELAS_RELEVAMIENTO_pendiente + " REAL, " + PARCELAS_RELEVAMIENTO_parcela + " TEXT, " + PARCELAS_RELEVAMIENTO_lat + " REAL, " + PARCELAS_RELEVAMIENTO_longi + " REAL, " + PARCELAS_RELEVAMIENTO_comentarios + " TEXT, "
            + PARCELAS_RELEVAMIENTO_geometry + " TEXT, " + PARCELAS_RELEVAMIENTO_releva + " INTEGER DEFAULT 0, "
            + PARCELAS_RELEVAMIENTO_idrodales + " INTEGER, " + PARCELAS_RELEVAMIENTO_lat_point_rel + " REAL, " + PARCELAS_RELEVAMIENTO_longi_point_rel + " REAL)";



    public static final String TABLA_PARCELAS_REL = "parcelas_rel";
    public static final String PARCELAS_REL_id = "id"; //ID EN SQLITE
    public static final String PARCELAS_REL_idparcelarel = "idparcelarel";
    public static final String PARCELAS_REL_cod_sap = "cod_sap";
    public static final String PARCELAS_REL_superficie = "superficie"; //suma del rodal
    public static final String PARCELAS_REL_pendiente = "pendiente"; //Solo vacio
    public static final String PARCELAS_REL_comentarios = "comentarios";

    public static final String PARCELAS_REL_lat = "lat";
    public static final String PARCELAS_REL_longi = "longi";

    public static final String PARCELAS_REL_geometry = "geometry";

    public static final String PARCELAS_REL_idrodal = "idrodal"; //ID EN SQLITE

    public static final String PARCELAS_REL_releva = "releva";
    public static final String PARCELAS_REL_sincronizado = "sincronizado";
    public static final String PARCELAS_REL_idpostgres = "idpostgres";
    public static final String PARCELAS_REL_tipo = "tipo";



    public static final String CREATE_TABLA_PARCELAS_REL = "CREATE TABLE " + TABLA_PARCELAS_REL + "(" + PARCELAS_REL_id + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            PARCELAS_REL_idparcelarel + " INTEGER NOT NULL," + PARCELAS_REL_cod_sap + " TEXT," + PARCELAS_REL_superficie + " REAL, " + PARCELAS_REL_pendiente + " REAL, " +
            PARCELAS_REL_comentarios + " TEXT, " + PARCELAS_REL_lat + " REAL, " + PARCELAS_REL_longi + " REAL, " + PARCELAS_REL_geometry + " TEXT," +
            PARCELAS_REL_idrodal + " INTEGER, " + PARCELAS_RELEVAMIENTO_releva + " INTEGER DEFAULT 0, " + PARCELAS_REL_sincronizado + " INTEGER DEFAULT 0, "+
            PARCELAS_REL_idpostgres + " INTEGER, " + PARCELAS_REL_tipo + " TEXT)";


    //TABLA ARBOLES

    public static final String TABLA_ARBOLES = "arboles";
    public static final String ARBOLES_id = "id";
    public static final String ARBOLES_idrodal = "idrodal";
    public static final String ARBOLES_idparcela = "idparcela"; //Guardar el id de SQLITE para que no se pisen
    public static final String ARBOLES_marca = "marca";
    public static final String ARBOLES_dap = "dap";
    public static final String ARBOLES_altura = "altura";
    public static final String ARBOLES_altura_poda = "altura_poda";
    public static final String ARBOLES_dmsm = "dmsm";
    public static final String ARBOLES_calidad = "calidad";
    public static final String ARBOLES_cosechado = "cosechado";
    public static final String ARBOLES_sincronizado = "sincronizado"; //BOLEANO POR DEFECTO 0
    public static final String ARBOLES_idtablaarbolespostgres = "idtablaarbpostgres"; //Obtiene el id de sincronizacion
    public static final String ARBOLES_latitud = "lat";
    public static final String ARBOLES_longitud = "longi";
    public static final String ARBOLES_idpostgres = "idpostgres";
    public static final String ARBOLES_fecharel = "fecha_rel";
    //public static final String ARBOLES_idparcelapostgres = "idparcelapostgres"; //Usado para sincronizar la base de parcelas creadas manualmente


    public static final String CREATE_TABLA_ARBOLES = "CREATE TABLE " + TABLA_ARBOLES + " (" + ARBOLES_id + " INTEGER PRIMARY KEY AUTOINCREMENT, " + ARBOLES_idrodal + " INTEGER, " +
            ARBOLES_idparcela + " INTEGER, " + ARBOLES_marca + " TEXT, " + ARBOLES_dap + " REAL, " + ARBOLES_altura + " REAL, " + ARBOLES_altura_poda + " REAL," +
            ARBOLES_dmsm + " REAL, " + ARBOLES_calidad + " TEXT, " + ARBOLES_cosechado + " TEXT DEFAULT 'NO', " +  ARBOLES_sincronizado + " INTEGER DEFAULT 0, " +
            ARBOLES_idtablaarbolespostgres + " INTEGER UNIQUE, " + ARBOLES_latitud + " REAL," + ARBOLES_longitud + " REAL," + ARBOLES_fecharel + " TEXT, " +
            ARBOLES_idpostgres + " INTEGER)";




    public static final String TABLA_RESTRICCIONES = "restricciones";
    public static final String RESTRICCIONES_id = "id";
    public static final String RESTRICCIONES_dap = "dap";

    public static final String CREATE_TABLA_RESTRICCIONES = "CREATE TABLE " + TABLA_RESTRICCIONES + "(" + RESTRICCIONES_id + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            RESTRICCIONES_dap + " INTEGER DEFAULT 0)";




}
