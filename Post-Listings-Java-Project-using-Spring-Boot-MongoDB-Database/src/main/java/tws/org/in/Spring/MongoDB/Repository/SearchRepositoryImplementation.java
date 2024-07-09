package tws.org.in.Spring.MongoDB.Repository;

import com.mongodb.client.result.UpdateResult;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import tws.org.in.Spring.MongoDB.Controller.DTO.Empresadto;
import tws.org.in.Spring.MongoDB.Model.Empresa;
import tws.org.in.Spring.MongoDB.Service.EmpresaService;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
@RequiredArgsConstructor
@AllArgsConstructor
public class SearchRepositoryImplementation implements EmpresaService {
 

    @Autowired
   private  MongoTemplate mongoTemplate;
 
 
    @Override
    public void insert123(List<Empresadto.Request> dtoRequests) {
        for (Empresadto.Request dtoRequest : dtoRequests) {
            System.out.println(dtoRequest);
            Empresa empresa = new Empresa(dtoRequest);
            insert(empresa);
            // Agregar la respuesta a la lista de respuestas
        }
    }
    private static final Object lock = new Object();
    private void insert(Empresa empresa) {
        synchronized(lock) {
        try {
            Query query = new Query(Criteria.where("rucEmpresa").is(empresa.getRucEmpresa()));
            Empresa existingEmpresa = mongoTemplate.findOne(query, Empresa.class);
        
            
            if (checkAndUpdateEmpresa(empresa)) {
                if (empresa.getFechaCreacion() == null) {
                    empresa.setFechaCreacion(System.currentTimeMillis());
                }
                empresa.setEsCliente(false);
                empresa.setUsaGuia(false);
                
if (empresa.getExceptuadaIGV() != null && !empresa.getExceptuadaIGV().isEmpty()) {
    empresa.setExceptuadaIGV(empresa.getExceptuadaIGV().toUpperCase());
}
if (empresa.getNombreContacto() != null && !empresa.getNombreContacto().isEmpty()) {
    empresa.setNombreContacto(empresa.getNombreContacto().toUpperCase());
}

if (empresa.getPuestoContacto() != null && !empresa.getPuestoContacto().isEmpty()) {
    empresa.setPuestoContacto(empresa.getPuestoContacto().toUpperCase());
}

// Verifica si el valor de getAgenteRetencion() no es nulo ni vacío antes de convertir a mayúsculas
if (empresa.getAgenteRetencion() != null && !empresa.getAgenteRetencion().isEmpty()) {
    empresa.setAgenteRetencion(empresa.getAgenteRetencion().toUpperCase());
}

if (empresa.getTipoEmpresa() != null && !empresa.getTipoEmpresa().isEmpty()) {
    empresa.setTipoEmpresa(empresa.getTipoEmpresa().toUpperCase());
}

if (empresa.getActividadEconomica() != null && !empresa.getActividadEconomica().isEmpty()) {
    List<String> actividadEconomicaMayusculas = empresa.getActividadEconomica().stream()
            .map(String::toUpperCase)
            .collect(Collectors.toList());
    empresa.setActividadEconomica(actividadEconomicaMayusculas);
}

    

                if (existingEmpresa != null) {
                    System.out.println("La empresa ya existe en la colección.");
                    return;
                }
                else{
                    mongoTemplate.insert(empresa);
                    System.out.println("Se insertó la empresa.");
                }
                } else {
                    System.out.println("Error al insertar la empresa: " );
                }
        } catch (Exception e) {

            System.out.println("Error al insertar la empresa: " + e.getMessage());
            e.printStackTrace();
        }
    }
    }

    public static String convertToUpper(String texto){
        return texto.toUpperCase();
    }
 
    private static final String API_URL = "https://api.migo.pe/api/v1/ruc";
    private static final String API_KEY = "KheBTxvGNgGXEoLBffHHDtks9KwlGCYypI8ZxfiqlToLaD98GKFasZYAXXwL"; 

    public Optional<JsonNode> checkRucExists(String rucEmpresa) {
        try {
            String jsonParams = "{\"token\":\"" + API_KEY + "\", \"ruc\":\"" + rucEmpresa + "\"}";
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(API_URL))
                    .header("Accept", "application/json")
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(jsonParams, StandardCharsets.UTF_8))
                    .build();
            HttpClient client = HttpClient.newHttpClient();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode rootNode = objectMapper.readTree(response.body());

                // Check the "success" field
                JsonNode successNode = rootNode.path("success");
                if (successNode.asBoolean()) {
                    return Optional.of(rootNode);
                } else {
                    System.out.println("API response indicates failure.");
                }
            } else {
                System.out.println("Error en la solicitud HTTP: " + response.statusCode());
            }
        } catch (IOException | InterruptedException e) {
            System.out.println("Error al procesar la respuesta de la API: " + e.getMessage());
            e.printStackTrace();
        }
        return Optional.empty();
    }


    public void updateEmpresa(Empresa empresa, JsonNode datosContribuyenteNode) {
        String nuevoCod = obtenerCondicion(datosContribuyenteNode.path("condicion_de_domicilio").asText());
        String nuevo = obtenerCodigoEstado(datosContribuyenteNode.path("estado_del_contribuyente").asText());
        String razonSocial = datosContribuyenteNode.path("nombre_o_razon_social").asText();
        String codUbigeo = datosContribuyenteNode.path("ubigeo").asText();
        String desDireccion = datosContribuyenteNode.path("direccion_simple").asText();
        String desDepartamento = datosContribuyenteNode.path("departamento").asText();
    
        updateEmpresaIfNotNull(empresa::setEstadoSunat, nuevo);
        updateEmpresaIfNotNull(empresa::setCondicionSunat, nuevoCod);
        updateEmpresaIfNotNull(empresa::setRazonSocial, razonSocial);
        updateEmpresaIfNotNull(empresa::setUbigeoFiscal, codUbigeo);
        updateEmpresaIfNotNull(empresa::setDireccionFiscal, desDireccion);
        updateEmpresaIfNotNull(empresa::setCiudadFiscal, desDepartamento);
    }

    public boolean checkAndUpdateEmpresa(Empresa empresa) {
        Optional<JsonNode> datosContribuyenteNode = checkRucExists(empresa.getRucEmpresa());
       if (datosContribuyenteNode.isPresent()) {
            updateEmpresa(empresa, datosContribuyenteNode.get());
            return true;
        }
        return false;
    }

    private <T> void updateEmpresaIfNotNull(Consumer<T> setter, T value) {
        if (value != null) {
            setter.accept(value);
        }
    }



    private static String obtenerCodigoEstado(String codEstado) {
        // Inicializa el mapa de estado a código
        Map<String, String> estadoCodigoMap = new HashMap<>();
        
        estadoCodigoMap.put("ACTIVO", "00");
        estadoCodigoMap.put("BAJA PROVICIONAL", "01");
        estadoCodigoMap.put("BAJA DEFINITIVA", "10");
        estadoCodigoMap.put("BAJA DE OFICIO", "11");
        
        // Obtiene el código de estado del mapa
        return estadoCodigoMap.get(codEstado.toUpperCase()); // Convertir a mayúsculas para evitar problemas de coincidencia
    }


    private static String obtenerCondicion(String codEstado) {
        // Inicializa el mapa de estado a código
        Map<String, String> estadoCodigoMap = new HashMap<>();
        
        estadoCodigoMap.put("HABIDO", "00");
        estadoCodigoMap.put("PENDIENTE", "09");
        estadoCodigoMap.put("POR VERIFICAR", "11");
        estadoCodigoMap.put("NO HABIDO", "12");
        estadoCodigoMap.put("NO HALLADO", "20");

        
        // Obtiene el código de estado del mapa
        return estadoCodigoMap.get(codEstado.toUpperCase()); // Convertir a mayúsculas para evitar problemas de coincidencia
    }


    

    @Override
public void update123(List<Empresadto.Request> empresaRequests) {
    List<Empresa> empresas = convertToEmpresas(empresaRequests);
    for (Empresadto.Request dtoRequest : empresaRequests) {
        System.out.println(dtoRequest);
        Empresa empresa = new Empresa(dtoRequest);
        checkAndUpdateEmpresa(empresa);
    }
    replaceDocuments(empresas);

}



private List<Empresa> convertToEmpresas(List<Empresadto.Request> empresaRequests) {
    // Aquí puedes implementar la lógica para convertir Empresadto.Request a Empresa
    // y devolver una lista de objetos Empresa
    List<Empresa> empresas = new ArrayList<>();
    for (Empresadto.Request empresaRequest : empresaRequests) {
        Empresa empresa = new Empresa(empresaRequest);
        // Configurar los atributos de empresa usando los datos de empresaRequest
        empresas.add(empresa);
    }
    return empresas;
}

public void replaceDocuments(List<Empresa> empresas) {
    empresas.forEach(empresa -> {
        if (empresa.getRucEmpresa() != null) {
            boolean anyFieldNonNull = Stream.of(empresa.getRucAdquiriente(),empresa.getPuestoContacto(),
                    empresa.getTelefono(),
                    empresa.getEmail(), empresa.getTipoEmpresa(),
                    empresa.getAgenteRetencion(), empresa.getExceptuadaIGV(), empresa.getEsCliente(),
                    empresa.getUsaGuia(),empresa.getNombreContacto(), empresa.getModificadoPor()
            ).anyMatch(Objects::nonNull);

            if (anyFieldNonNull) {
                // Construir el documento de actualización con el operador $set
                Update update = new Update();

                if (empresa.getTelefono() != null) {
                    update.set("telefono", empresa.getTelefono());
                }
                if (empresa.getEmail() != null) {
                    update.set("email", empresa.getEmail());
                }
                if (empresa.getTipoEmpresa() != null) {
                    update.set("tipoEmpresa", empresa.getTipoEmpresa().toUpperCase());
                }
                if (empresa.getActividadEconomica() != null) {
                    List<String> actividadEconomicaMayusculas = empresa.getActividadEconomica().stream()
                            .map(String::toUpperCase)
                            .collect(Collectors.toList());
                    update.set("actividadEconomica", actividadEconomicaMayusculas);
                }
                if (empresa.getAgenteRetencion() != null) {
                    update.set("agenteRetencion", empresa.getAgenteRetencion().toUpperCase());
                }
                if (empresa.getExceptuadaIGV() != null) {
                    update.set("exceptuadaIGV", empresa.getExceptuadaIGV().toUpperCase());
                }
                if (empresa.getEsCliente() != null) {
                    update.set("esCliente", empresa.getEsCliente());
                }
                if (empresa.getUsaGuia() != null) {
                    update.set("usaGuia", empresa.getUsaGuia());
                }
                if (empresa.getModificadoPor() != null) {
                    update.set("modificadoPor", empresa.getModificadoPor());
                }
                if (empresa.getNombreContacto() != null) {
                    update.set("nombreContacto", empresa.getNombreContacto().toUpperCase());
                }
                if (empresa.getRucAdquiriente() != null) {
                    update.set("rucAdquiriente", empresa.getRucAdquiriente());
                }
                if (empresa.getPuestoContacto() != null) {
                    update.set("puestoContacto", empresa.getPuestoContacto().toUpperCase());
                }

                // Obtener la fecha actual en milisegundos
                long currentTime = System.currentTimeMillis();

                // Verificar y obtener fecha de creación si es nulo
                Long fechaCreacion = empresa.getFechaCreacion();
                if (fechaCreacion == null) {
                    // Consultar la fecha de creación desde la base de datos
                    Query query = new Query(Criteria.where("rucEmpresa").is(empresa.getRucEmpresa()));
                    Empresa empresaBD = mongoTemplate.findOne(query, Empresa.class);
                    if (empresaBD != null) {
                        fechaCreacion = empresaBD.getFechaCreacion();
                    }
                }

                if (fechaCreacion != null) {
                    long diferenciaMinutos = (currentTime - fechaCreacion) / (1000 * 60);
                    int minutosMinimosParaActualizar = 1;

                    if (diferenciaMinutos >= minutosMinimosParaActualizar) {
                        update.set("fechaActualizacion", currentTime);
                    }
                } else {
                    System.out.println("No se pudo obtener la fecha de creación para la empresa: " + empresa.getRucEmpresa());
                }

                Query query = new Query(Criteria.where("rucEmpresa").is(empresa.getRucEmpresa()));
                UpdateResult updateResult = mongoTemplate.updateFirst(query, update, Empresa.class);
                if (updateResult.getModifiedCount() > 0) {
                    System.out.println("Colección reemplazada con éxito, rucEmpresa: " + empresa.getRucEmpresa());
                } else {
                    System.out.println("No hubo cambios, no se actualizó o no existe: " + empresa.getRucEmpresa());
                }
            } else {
                System.out.println("No se pudo actualizar debido a valores nulos en la empresa: " + empresa.getRucEmpresa());
            }
        } else {
            System.out.println("El campo rucEmpresa es nulo para la empresa: " + empresa);
        }
    });
}

@Override
public Map<String, List<String>> exists(List<String> rucProveedor) {
    Map<String, List<String>> resultado = new HashMap<>();
    List<String> rucExistentes = new ArrayList<>();
    List<String> rucNoExistentes = new ArrayList<>();
    List<String> rucInvalidos = new ArrayList<>();

    Set<String> rucCache = new HashSet<>();

    for (String ruc : rucProveedor) {
        if (!checkRucExists(ruc).isPresent()) {
            rucInvalidos.add(ruc);
            continue;
        }

        Query query = new Query(Criteria.where("rucEmpresa").is(ruc));
        Empresa existingEmpresa = mongoTemplate.findOne(query, Empresa.class);

        if (existingEmpresa != null) {
            if (!rucCache.contains(ruc)) {
                rucExistentes.add(ruc);
            }
        } else {
            rucNoExistentes.add(ruc);
        }
        rucCache.add(ruc);
    }

    resultado.put("existentes", rucExistentes);
    resultado.put("noExistentes", rucNoExistentes);
    resultado.put("invalidos", rucInvalidos);

    return resultado;
}
}
