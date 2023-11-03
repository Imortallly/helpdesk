package com.gomes.helpdesk.resources;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
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

import com.gomes.helpdesk.domain.Cliente;
import com.gomes.helpdesk.domain.dtos.ClienteDTO;
import com.gomes.helpdesk.services.ClienteService;

import jakarta.validation.Valid;

@RestController 
@RequestMapping(value="/clientes")
public class ClienteResource {
	
	@Autowired
	private ClienteService tecnicoService;
	
	@GetMapping(value = "/{id}")
	public ResponseEntity<ClienteDTO> findById(@PathVariable Integer id) {
		Cliente obj = tecnicoService.findById(id);
		return ResponseEntity.ok().body(new ClienteDTO(obj));
	}
	@GetMapping
	public ResponseEntity<List<ClienteDTO>> findAll() {
		List<Cliente> list = tecnicoService.findAll();
		List<ClienteDTO> listDTO = list.stream().map(obj -> new ClienteDTO(obj)).collect(Collectors.toList());
		return ResponseEntity.ok().body(listDTO);
	}
	
	@PostMapping
	public ResponseEntity<ClienteDTO> create(@Valid @RequestBody ClienteDTO obj) {
		Cliente newObj = tecnicoService.create(obj);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(newObj.getId()).toUri();
		return ResponseEntity.created(uri).build();		
	}
	
	@PutMapping(value = "/{id}")
	public ResponseEntity<ClienteDTO> update(@PathVariable Integer id,@Valid @RequestBody ClienteDTO objDTO) {
		Cliente obj = tecnicoService.update(id, objDTO);
		return ResponseEntity.ok().body(new ClienteDTO(obj));
	}
	
	@DeleteMapping(value = "/{id}")
	public ResponseEntity<ClienteDTO> delete(@PathVariable Integer id){	
		tecnicoService.delete(id);
		return ResponseEntity.noContent().build();
	}

}
