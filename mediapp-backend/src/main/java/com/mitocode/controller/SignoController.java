package com.mitocode.controller;

import com.mitocode.exception.ModeloNotFoundException;
import com.mitocode.model.Paciente;
import com.mitocode.model.Signo;
import com.mitocode.service.IPacienteService;
import com.mitocode.service.ISignoService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/signos")
public class SignoController {

    private final ISignoService service;

    private final IPacienteService pacienteService;

    public SignoController(ISignoService service, IPacienteService pacienteService) {
        this.service = service;
        this.pacienteService = pacienteService;
    }

    @GetMapping
    public ResponseEntity<List<Signo>> listar() throws Exception {
        List<Signo> lista = service.listar();
        return new ResponseEntity<>(lista, HttpStatus.OK);
    }

    @GetMapping("/paciente/{keySearch}")
    public ResponseEntity<List<Paciente>> buscarPaciente(@PathVariable("keySearch") String keySearch) throws Exception {
        keySearch = keySearch.toLowerCase();
        List<Paciente> lista = pacienteService.buscar(keySearch);
        return new ResponseEntity<>(lista, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Signo> listarPorId(@PathVariable("id") Integer id) throws Exception {
        Signo p = service.listarPorId(id);
        if (p == null) {
            throw new ModeloNotFoundException("ID NO ENCONTRADO " + id);
        }
        return new ResponseEntity<Signo>(p, HttpStatus.OK);
    }

    //https://docs.spring.io/spring-hateoas/docs/current/reference/html/
    //Hateoas 1.0 = > Spring Boot 2.2
    @GetMapping("/hateoas/{id}")
    public EntityModel<Signo> listarPorIdHateoas(@PathVariable("id") Integer id) throws Exception {
        Signo pac = service.listarPorId(id);
        EntityModel<Signo> recurso = new EntityModel<Signo>(pac);
        WebMvcLinkBuilder linkTo = linkTo(methodOn(this.getClass()).listarPorId(id));
        recurso.add(linkTo.withRel("paciente-recurso"));
        return recurso;
    }

    @PostMapping
    public ResponseEntity<Object> registrar(@Valid @RequestBody Signo signo) throws Exception {
        Signo p = service.registrar(signo);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(p.getIdSigno()).toUri();
        return ResponseEntity.created(location).build();
    }

    @PutMapping
    public ResponseEntity<Signo> modificar(@Valid @RequestBody Signo signo) throws Exception {
        Signo p = service.modificar(signo);
        return new ResponseEntity<Signo>(p, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> eliminar(@PathVariable("id") Integer id) throws Exception {
        Signo p = service.listarPorId(id);
        if (p == null) {
            throw new ModeloNotFoundException("ID NO ENCONTRADO " + id);
        }
        service.eliminar(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/pageable")
    public ResponseEntity<Page<Signo>> listarPageable(Pageable pageable) throws Exception {
        Page<Signo> pacientes = service.listarPageable(pageable);
        return new ResponseEntity<>(pacientes, HttpStatus.OK);
    }


}
