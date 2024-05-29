package tws.org.in.Spring.MongoDB.Service;

import java.util.List;
import java.util.Map;

import tws.org.in.Spring.MongoDB.Controller.DTO.Empresadto;

public interface EmpresaService {
void insert123(List<Empresadto.Request> d);
void update123(List<Empresadto.Request> d);
Map<String, List<String>> exists(List<String> rucProveedor);
}
