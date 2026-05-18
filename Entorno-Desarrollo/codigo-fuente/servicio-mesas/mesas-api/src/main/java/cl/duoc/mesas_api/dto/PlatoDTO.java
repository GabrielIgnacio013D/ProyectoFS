package cl.duoc.mesas_api.dto;

public class PlatoDTO {
    private Long id;
    private String nombre;
    private Double precio;

    public PlatoDTO() {}

    public PlatoDTO(Long id, String nombre, Double precio) {
        this.id = id;
        this.nombre = nombre;
        this.precio = precio;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public Double getPrecio() { return precio; }
    public void setPrecio(Double precio) { this.precio = precio; }
}
