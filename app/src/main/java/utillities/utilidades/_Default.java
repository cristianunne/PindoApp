package utillities.utilidades;

public class _Default {

    protected String _mensaje;
    protected boolean _status;


    public _Default() {
        this._mensaje = "";
        this._status = true;
    }

    public String get_mensaje() {
        return _mensaje;
    }

    public void set_mensaje(String _mensaje) {
        this._mensaje = _mensaje;
    }

    public boolean is_status() {
        return _status;
    }

    public void set_status(boolean _status) {
        this._status = _status;
    }
}
