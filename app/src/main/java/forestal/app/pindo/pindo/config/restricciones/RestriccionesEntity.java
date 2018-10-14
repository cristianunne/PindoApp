package forestal.app.pindo.pindo.config.restricciones;

public class RestriccionesEntity {

    private Integer id;
    private int dap;


    public RestriccionesEntity() {
        setId(-1);
        setDap(0);
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getDap() {
        return dap;
    }

    public void setDap(int dap) {
        this.dap = dap;
    }
}
