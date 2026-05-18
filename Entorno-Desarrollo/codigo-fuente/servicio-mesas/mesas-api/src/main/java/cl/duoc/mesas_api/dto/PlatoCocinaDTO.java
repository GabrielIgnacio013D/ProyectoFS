package cl.duoc.mesas_api.dto;

public class PlatoCocinaDTO {
    private String plato;
    private Double precio;
    private Integer mesa;

    public PlatoCocinaDTO() {}

    public PlatoCocinaDTO(String plato, Double precio, Integer mesa) {
        this.plato = plato;
        this.precio = precio;
        this.mesa = mesa;
    }

    public String getPlato() { return plato; }
    public void setPlato(String plato) { this.plato = plato; }
    public Double getPrecio() { return precio; }
    public void setPrecio(Double precio) { this.precio = precio; }
    public Integer getMesa() { return mesa; }
    public void setMesa(Integer mesa) { this.mesa = mesa; }
}
