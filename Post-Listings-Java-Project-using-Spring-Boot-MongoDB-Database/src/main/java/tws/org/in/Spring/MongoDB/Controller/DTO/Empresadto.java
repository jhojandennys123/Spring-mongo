package tws.org.in.Spring.MongoDB.Controller.DTO;

import java.util.List;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Empresadto {
    @Getter
    @Setter
    public static class Request{
        @NotBlank(message = "El campo pais es requerido")
        @Size(max = 3, message = "El pais debe tener 3 caracteres")
        private String pais;
        @NotBlank(message = "El campo pais es requerido")
        @Size(max = 11, message = "El RUC de la empresa no puede tener más de 11 caracteres")
        private String rucProveedor;
        @NotBlank(message = "El razon social es requerido")
        private String nombreProveedor;
        private String direccionProveedor;
        private String ciudadProveedor;
        private String ubigeoFiscal;
        private String telefono;
        private String correoProveedor;
        private String tipoEmpresa;
        private List<String> actividadEconomica;
        @Size(max = 2, message = "La condicion de estado no puede tener más de 2 caracteres")
        @NotBlank(message = "El condicion en sunat es requerido")
        private String condicionSunat;
        private String agenteRetencion;
        private String exceptuadaIGV;
        private Boolean esCliente;
        private Boolean usaGuia;
        private Long fechaCreacion;
        private String correoUsuario;
        private Long fechaActualizacion;
        private String ubigeoProveedor;
        @NotBlank(message = "El estado en sunat es requerido")
        @Size(max = 2, message = "El estado de la sunat no puede tener más de 2 caracteres")
        private String estadoSunat;
        private String nombreContacto;
        private String rucAdquiriente;
        private String puestoContacto;
    }
}
