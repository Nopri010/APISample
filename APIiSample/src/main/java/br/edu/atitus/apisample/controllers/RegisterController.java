package br.edu.atitus.apisample.controllers;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import br.edu.atitus.apisample.dtos.RegisterDTO;
import br.edu.atitus.apisample.entities.RegisterEntity;
import br.edu.atitus.apisample.entities.UserEntity;
import br.edu.atitus.apisample.services.RegisterService;
import br.edu.atitus.apisample.services.UserService;

@RestController
@RequestMapping("/registers")
public class RegisterController {
    private final RegisterService service;
    private final UserService userService;

    public RegisterController(RegisterService service, UserService userService) {
        this.service = service;
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<RegisterEntity> createRegister(@RequestBody RegisterDTO registerDTO) throws Exception {
        // Validar latitude e longitude
        validateCoordinates(registerDTO.getLatitude(), registerDTO.getLongitude());

        // Converter DTO em entidade
        RegisterEntity newRegister = new RegisterEntity();
        BeanUtils.copyProperties(registerDTO, newRegister);

        // Buscar usuário (substituir por usuário autenticado no futuro)
        UserEntity user = userService.findAll().get(0); 
        newRegister.setUser(user);

        // Salvar registro
        service.save(newRegister);

        return ResponseEntity.status(HttpStatus.CREATED).body(newRegister);
    }

    @PutMapping("/{id}")
    public ResponseEntity<RegisterEntity> putRegister(@PathVariable UUID id, @RequestBody RegisterDTO dto) throws Exception {
        validateCoordinates(dto.getLatitude(), dto.getLongitude());
        RegisterEntity register = service.findById(id);
        BeanUtils.copyProperties(dto, register);

        service.save(register);

        return ResponseEntity.ok(register);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteRegister(@PathVariable UUID id) throws Exception {
        service.deleteById(id);
        return ResponseEntity.ok("Registro deletado!");
    }

    @GetMapping
    public ResponseEntity<List<RegisterEntity>> getRegisters() throws Exception {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<RegisterEntity> getOneRegister(@PathVariable UUID id) throws Exception {
        return ResponseEntity.ok(service.findById(id));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handlerException(Exception ex) {
        String message = ex.getMessage().replaceAll("\r\n", "");
        return ResponseEntity.badRequest().body(message);
    }

    private void validateCoordinates(double latitude, double longitude) throws Exception {
        if (latitude < -90 || latitude > 90)
            throw new Exception("Latitude inválida! Deve estar entre -90 e 90.");
        if (longitude < -180 || longitude > 180)
            throw new Exception("Longitude inválida! Deve estar entre -180 e 180.");
    }
}