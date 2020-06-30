package com.mitocode.controller;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.net.URI;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.mitocode.exception.ModeloNotFoundException;
import com.mitocode.model.Examen;
import com.mitocode.service.IExamenService;

@RestController
@RequestMapping("/examenes")
public class ExamenController {

    @Autowired
    private IExamenService service;

    @GetMapping
    public ResponseEntity<List<Examen>> listar() throws Exception {
        List<Examen> lista = service.listar();
        return new ResponseEntity<>(lista, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Examen> listarPorId(@PathVariable("id") Integer id) throws Exception {
        Examen obj = service.listarPorId(id);
        if (obj == null) {
            throw new ModeloNotFoundException("ID NO ENCONTRADO " + id);
        }
        return new ResponseEntity<>(obj, HttpStatus.OK);
    }

    //https://docs.spring.io/spring-hateoas/docs/current/reference/html/
    //Hateoas 1.0 = > Spring Boot 2.2
    @GetMapping("/hateoas/{id}")
    public EntityModel<Examen> listarPorIdHateoas(@PathVariable("id") Integer id) throws Exception {

        Examen pac = service.listarPorId(id);

        //localhost:8080/examens/{id}
        EntityModel<Examen> recurso = new EntityModel<>(pac);
        WebMvcLinkBuilder linkTo = linkTo(methodOn(this.getClass()).listarPorId(id));
        recurso.add(linkTo.withRel("examen-recurso"));
        return recurso;
    }

    /*@PostMapping
	public ResponseEntity<Examen> registrar(@Valid @RequestBody Examen examen){
		Examen obj = service.registrar(examen);
		return new ResponseEntity<Examen>(obj, HttpStatus.CREATED);
	}*/
    @PostMapping
    public ResponseEntity<Object> registrar(@Valid @RequestBody Examen examen) throws Exception {
        Examen obj = service.registrar(examen);
        //localhost:8080/examens/5		
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(obj.getIdExamen()).toUri();
        return ResponseEntity.created(location).build();
    }

    @PutMapping
    public ResponseEntity<Examen> modificar(@Valid @RequestBody Examen examen) throws Exception {
        Examen obj = service.modificar(examen);
        return new ResponseEntity<>(obj, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> eliminar(@PathVariable("id") Integer id) throws Exception {
        Examen obj = service.listarPorId(id);
        if (obj == null) {
            throw new ModeloNotFoundException("ID NO ENCONTRADO " + id);
        }
        service.eliminar(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
