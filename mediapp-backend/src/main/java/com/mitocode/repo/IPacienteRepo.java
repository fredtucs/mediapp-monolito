package com.mitocode.repo;

import com.mitocode.model.Consulta;
import com.mitocode.model.Paciente;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

//@Repository
public interface IPacienteRepo extends IGenericRepo<Paciente, Integer> {

    @Query("FROM Paciente p WHERE p.dni like %:keySearch% OR LOWER(p.nombres) like %:keySearch% or LOWER(p.apellidos) like %:keySearch%")
    List<Paciente> buscar(@Param("keySearch") String keySearch);

}
