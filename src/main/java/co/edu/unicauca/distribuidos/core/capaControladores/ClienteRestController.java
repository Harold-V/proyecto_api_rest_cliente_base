package co.edu.unicauca.distribuidos.core.capaControladores;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import co.edu.unicauca.distribuidos.core.fachadaServices.DTO.ClienteDTO;
import co.edu.unicauca.distribuidos.core.fachadaServices.services.IClienteService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:4200", methods = { RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT,
		RequestMethod.DELETE })
public class ClienteRestController {

	@Autowired
	private IClienteService clienteService;

	@GetMapping("/clientes")
	public List<ClienteDTO> listarClientes() {
		return clienteService.findAll();
	}

	@GetMapping("/clientes/{id}")
	public ClienteDTO consultarCliente(@PathVariable Integer id) {
		ClienteDTO objCliente = null;
		objCliente = clienteService.findById(id);
		return objCliente;
	}

	@GetMapping("/clientes2")
	public ClienteDTO consultarCliente2(@RequestParam Integer id) {
		ClienteDTO objCliente = null;
		objCliente = clienteService.findById(id);
		return objCliente;
	}

	@GetMapping("clientes2/{name}/{age}")
	public String getMessage(@PathVariable String name,
			@PathVariable("age") String edad) {
		String msg = String.format("%s es %s años viejo", name, edad);
		System.out.println(msg);
		return msg;
	}

	@GetMapping("consultarClientes")
	public String consultarClientesConVariosParametros(@RequestParam String nombres,
			@RequestParam String apellidos) {
		String msg = String.format("buscando un cliente por nombre: %s, apellidos: %s", nombres, apellidos);
		System.out.println(msg);
		return msg;
	}

	// MODIFICADO: Retorna ResponseEntity con HttpStatus.CREATED
	// y permite que las excepciones se propaguen
	@PostMapping("/clientes")
	public ResponseEntity<ClienteDTO> crearCliente(@Valid @RequestBody ClienteDTO cliente) {
		ClienteDTO objCliente = clienteService.save(cliente);
		return new ResponseEntity<>(objCliente, HttpStatus.CREATED);
	}

	// MODIFICADO: Retorna ResponseEntity con HttpStatus.OK
	// Elimina la validación if porque ahora la excepción se lanza en el service
	@PutMapping("/clientes/{id}")
	public ResponseEntity<ClienteDTO> actualizarCliente(@Valid @RequestBody ClienteDTO cliente,
			@PathVariable Integer id) {
		ClienteDTO objCliente = clienteService.update(id, cliente);
		return new ResponseEntity<>(objCliente, HttpStatus.OK);
	}

	// MODIFICADO: Retorna ResponseEntity con HttpStatus.NO_CONTENT
	// Elimina la validación if porque ahora la excepción se lanza en el service
	@DeleteMapping("/clientes/{id}")
	public ResponseEntity<Boolean> eliminarCliente(@PathVariable Integer id) {
		Boolean bandera = clienteService.delete(id);
		return new ResponseEntity<>(bandera, HttpStatus.NO_CONTENT);
	}

	@GetMapping("/clientes/listarCabeceras")
	public void listarCabeceras(@RequestHeader Map<String, String> headers) {
		System.out.println("cabeceras");
		headers.forEach((key, value) -> {
			System.out.println(String.format("Cabecera '%s' = %s", key, value));
		});
	}

	@GetMapping("/clientes/codigo/{codigo}")
	public ResponseEntity<Boolean> existeClienteCodigo(@PathVariable String codigo) {
		Boolean bandera = clienteService.existeClienteCodigo(codigo);
		ResponseEntity<Boolean> objRespuesta = new ResponseEntity<Boolean>(bandera, HttpStatus.OK);
		return objRespuesta;
	}
}
