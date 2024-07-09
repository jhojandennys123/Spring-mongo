package tws.org.in.Spring.MongoDB.Model;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import tws.org.in.Spring.MongoDB.Controller.DTO.Empresadto;

import java.io.Serializable;

@Getter
@Setter
@Data
@AllArgsConstructor
@RequiredArgsConstructor
@Builder
@Document(collection = "company")
public class Empresa implements Serializable {
    @Id
    @JsonSerialize(using = ToStringSerializer.class)
    private ObjectId id;
    private String pais;
    private String rucEmpresa;
    private String rucAdquiriente;
    private String condicionSunat;
    private String razonSocial;
    private String estadoSunat;
    private String direccionFiscal;
    private String ciudadFiscal;
    private String ubigeoFiscal;
    private String telefono;
    private String puestoContacto;
    private String nombreContacto;
    private String email;
    private String tipoEmpresa;
    private List<String> actividadEconomica;
    private String agenteRetencion;
    private String exceptuadaIGV;
    private Boolean esCliente;
        private Boolean usaGuia;
        private String modificadoPor;
        private Long fechaCreacion;
        private Long fechaActualizacion;
    public Empresa(Empresadto.Request data){
        this.pais = data.getPais();
        this.rucAdquiriente = data.getRucAdquiriente();
this.rucEmpresa = data.getRucProveedor();
this.razonSocial = data.getNombreProveedor();
this.direccionFiscal = data.getDireccionProveedor();
this.ciudadFiscal = data.getCiudadProveedor();
this.condicionSunat = data.getCondicionSunat();
this.ubigeoFiscal = data.getUbigeoProveedor();
this.puestoContacto = data.getPuestoContacto();
this.nombreContacto = data.getNombreContacto();
this.telefono = data.getTelefono();
this.email = data.getCorreoProveedor();
this.tipoEmpresa = data.getTipoEmpresa();
this.actividadEconomica = data.getActividadEconomica();
this.agenteRetencion = data.getAgenteRetencion();
this.exceptuadaIGV = data.getExceptuadaIGV();
this.esCliente = data.getEsCliente();
this.usaGuia = data.getUsaGuia();
this.fechaCreacion = data.getFechaCreacion();
this.modificadoPor = data.getCorreoUsuario();
this.fechaActualizacion = data.getFechaActualizacion();
this.estadoSunat = data.getEstadoSunat();
    }
}
