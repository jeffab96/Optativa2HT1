package ec.edu.uce.optativa3.modelo;

public class RegistroLog {
    String idLog;
    String usuario;
    String tipo;
    String tiempo;
    String nombreDispositivo;
    String modeloDispositivo;
    String versionDispositivo;

    public RegistroLog(String idLog, String usuario, String tipo, String tiempo, String nombreDispositivo, String modeloDispositivo, String versionDispositivo) {
        this.idLog = idLog;
        this.usuario = usuario;
        this.tipo = tipo;
        this.tiempo = tiempo;
        this.nombreDispositivo = nombreDispositivo;
        this.modeloDispositivo = modeloDispositivo;
        this.versionDispositivo = versionDispositivo;
    }

    public String getIdLog() {
        return idLog;
    }

    public void setIdLog(String idLog) {
        this.idLog = idLog;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getTiempo() {
        return tiempo;
    }

    public void setTiempo(String tiempo) {
        this.tiempo = tiempo;
    }

    public String getNombreDispositivo() {
        return nombreDispositivo;
    }

    public void setNombreDispositivo(String nombreDispositivo) {
        this.nombreDispositivo = nombreDispositivo;
    }

    public String getModeloDispositivo() {
        return modeloDispositivo;
    }

    public void setModeloDispositivo(String modeloDispositivo) {
        this.modeloDispositivo = modeloDispositivo;
    }

    public String getVersionDispositivo() {
        return versionDispositivo;
    }

    public void setVersionDispositivo(String versionDispositivo) {
        this.versionDispositivo = versionDispositivo;
    }
}
