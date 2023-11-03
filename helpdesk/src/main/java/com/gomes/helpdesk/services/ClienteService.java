package com.gomes.helpdesk.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.gomes.helpdesk.domain.Pessoa;
import com.gomes.helpdesk.domain.Cliente;
import com.gomes.helpdesk.domain.dtos.ClienteDTO;
import com.gomes.helpdesk.repositories.PessoaRepository;
import com.gomes.helpdesk.repositories.ClienteRepository;
import com.gomes.helpdesk.services.exceptions.ObjectNotFoundException;

import jakarta.validation.Valid;

@Service
public class ClienteService {
	
	@Autowired
	private ClienteRepository tecnicoRepository;
	
	@Autowired
	private PessoaRepository pessoaRepository;
	
	public Cliente findById(Integer id) {
		Optional<Cliente> obj = tecnicoRepository.findById(id);
		return obj.orElseThrow(() -> new ObjectNotFoundException("Objeto não encontrado! Id:" + id));
	}

	public List<Cliente> findAll() {
		
		return tecnicoRepository.findAll();
	}
	
	public Cliente create(ClienteDTO objDTO) {
		objDTO.setId(null);
		validaPorCpfEEmail(objDTO);
		Cliente newObj = new Cliente(objDTO);
		return tecnicoRepository.save(newObj);
	}
	
	public void delete(Integer id) {
		Cliente obj = findById(id);
		if(obj.getChamados().size() > 0) {
			throw new DataIntegrityViolationException("Cliente possui ordens de serviço e não pode ser deletado!");
		}
			tecnicoRepository.deleteById(id);
	}
	
	public Cliente update(Integer id, @Valid ClienteDTO objDTO) {
		objDTO.setId(id);
		Cliente oldObj = findById(id);
		validaPorCpfEEmail(objDTO);
		oldObj = new Cliente(objDTO);
		return tecnicoRepository.save(oldObj);
	}

	private void validaPorCpfEEmail(ClienteDTO objDTO) {
		Optional<Pessoa> obj = pessoaRepository.findByCpf(objDTO.getCpf());
		if(obj.isPresent() && obj.get().getId() != objDTO.getId()) {
			throw new DataIntegrityViolationException("CPF já cadastrado no sistema");
		}
		
		obj = pessoaRepository.findByEmail(objDTO.getEmail());
		
		if(obj.isPresent() && obj.get().getId() != objDTO.getId()) {
			throw new DataIntegrityViolationException("Email já cadastrado no sistema");
		}
	}


}
