package tws.org.in.Spring.MongoDB.Controller;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;
import tws.org.in.Spring.MongoDB.Controller.DTO.Empresadto;
import tws.org.in.Spring.MongoDB.Service.EmpresaService;
import java.util.List;
import java.util.Map;
@Validated
@RequiredArgsConstructor
@RestController
public class PostController {


   private final EmpresaService empresaService;

   @PostMapping("/inserts")
   public void addPost(@RequestBody List<Empresadto.Request> post) {
           empresaService.insert123(post);
   }
    @PutMapping("/updates")
    public void addUpdates(@RequestBody List<Empresadto.Request> post)
    {
        empresaService.update123(post);
    }
    @PostMapping("/check-ruc-exists")
    public ResponseEntity<?> checkRucExists(@RequestBody List<String> rucProveedor) {
        Map<String, List<String>> resultado = empresaService.exists(rucProveedor);
        return ResponseEntity.ok().body(resultado);
    }

}
