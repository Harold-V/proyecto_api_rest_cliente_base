
package co.edu.unicauca.distribuidos.core.fachadaServices.services;

import java.util.Date;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.stereotype.Service;

import co.edu.unicauca.distribuidos.core.capaAccesoADatos.models.ClienteEntity;
import co.edu.unicauca.distribuidos.core.capaAccesoADatos.repositories.UsuarioRepository;
import co.edu.unicauca.distribuidos.core.excepciones.EntidadNoExisteException;
import co.edu.unicauca.distribuidos.core.excepciones.ReglaNegocioExcepcion;
import co.edu.unicauca.distribuidos.core.fachadaServices.DTO.ClienteDTO;

@Service
public class ClienteServiceImpl implements IClienteService {

	private UsuarioRepository servicioAccesoBaseDatos;

	private ModelMapper modelMapper;

	public ClienteServiceImpl(UsuarioRepository servicioAccesoBaseDatos, ModelMapper modelMapper) {
		this.servicioAccesoBaseDatos = servicioAccesoBaseDatos;
		this.modelMapper = modelMapper;
	}

	@Override
	public List<ClienteDTO> findAll() {

		List<ClienteEntity> clientesEntity = this.servicioAccesoBaseDatos.findAll();
		List<ClienteDTO> clientesDTO = this.modelMapper.map(clientesEntity, new TypeToken<List<ClienteDTO>>() {
		}.getType());
		return clientesDTO;
	}

	@Override
	public ClienteDTO findById(Integer id) {
		ClienteEntity objCLienteEntity = this.servicioAccesoBaseDatos.findById(id);
		ClienteDTO clienteDTO = this.modelMapper.map(objCLienteEntity, ClienteDTO.class);
		return clienteDTO;
	}

	@Override
	public ClienteDTO save(ClienteDTO cliente) {
		ClienteDTO clienteDTO = null;

		// VALIDAR QUE EL CÓDIGO NO SEA NULL O VACÍO
		if (cliente.getCodigo() == null || cliente.getCodigo().trim().isEmpty()) {
			ReglaNegocioExcepcion objException = new ReglaNegocioExcepcion("El código del cliente es obligatorio");
			throw objException;
		}

		// VALIDAR QUE EL CÓDIGO NO ESTÉ DUPLICADO
		// Llama a existeClienteCodigo que a su vez llama a findByCodigo
		if (this.existeClienteCodigo(cliente.getCodigo())) {
			ReglaNegocioExcepcion objException = new ReglaNegocioExcepcion(
					"Ya existe un cliente con el código " + cliente.getCodigo() + ", no se permite crear el cliente");
			throw objException;
		}

		// VALIDAR QUE EL CORREO NO ESTÉ DUPLICADO
		if (this.servicioAccesoBaseDatos.exiteClienteConCorreo(cliente.getEmail())) {
			ReglaNegocioExcepcion objException = new ReglaNegocioExcepcion(
					"Existe un cliente con el correo a registrar, no se permite crear el cliente");
			throw objException;
		}

		// Si pasa todas las validaciones, crear el cliente
		ClienteEntity clienteEntity = this.modelMapper.map(cliente, ClienteEntity.class);
		clienteEntity.setCreateAt(new Date());
		ClienteEntity objClienteEntity = this.servicioAccesoBaseDatos.save(clienteEntity);
		clienteDTO = this.modelMapper.map(objClienteEntity, ClienteDTO.class);

		return clienteDTO;
	}

	@Override
	public ClienteDTO update(Integer id, ClienteDTO cliente) {
		ClienteDTO clienteDTO = null;

		if (this.servicioAccesoBaseDatos.existeCliente(id)) {
			ClienteEntity clienteActual = this.servicioAccesoBaseDatos.findById(id);

			// VALIDAR SI SE QUIERE CAMBIAR EL CÓDIGO A UNO EXISTENTE
			String codigoAnterior = clienteActual.getCodigo();
			String codigoNuevo = cliente.getCodigo();

			// Solo validar si el código cambió
			if (codigoAnterior != null && !codigoAnterior.equals(codigoNuevo)) {
				// Verificar si el nuevo código ya existe
				if (this.existeClienteCodigo(codigoNuevo)) {
					ReglaNegocioExcepcion objException = new ReglaNegocioExcepcion(
							"Ya existe un cliente con el código " + codigoNuevo + ", no se permite actualizar");
					throw objException;
				}
			}

			// VALIDAR SI SE QUIERE CAMBIAR EL CORREO A UNO EXISTENTE
			String correoAnterior = clienteActual.getEmail();
			String correoNuevo = cliente.getEmail();

			// Solo validar si el correo cambió
			if (!correoAnterior.equals(correoNuevo)) {
				// Verificar si el nuevo correo ya existe
				if (this.servicioAccesoBaseDatos.exiteClienteConCorreo(correoNuevo)) {
					ReglaNegocioExcepcion objException = new ReglaNegocioExcepcion(
							"Existe un cliente con el correo registrado, no se permite actualizar");
					throw objException;
				}
			}

			// Si pasa todas las validaciones, actualizar
			ClienteEntity clienteEntity = this.modelMapper.map(cliente, ClienteEntity.class);
			ClienteEntity clienteActualizado = this.servicioAccesoBaseDatos.update(id, clienteEntity);
			clienteDTO = this.modelMapper.map(clienteActualizado, ClienteDTO.class);

		} else {
			EntidadNoExisteException objException = new EntidadNoExisteException(
					"Error, el usuario a actualizar no existe");
			throw objException;
		}

		return clienteDTO;
	}

	@Override
	public boolean delete(Integer id) {
		boolean bandera = false;

		if (this.servicioAccesoBaseDatos.existeCliente(id)) {
			bandera = this.servicioAccesoBaseDatos.delete(id);
		} else {
			EntidadNoExisteException objException = new EntidadNoExisteException(
					"Error, el usuario a eliminar no existe");
			throw objException;
		}

		return bandera;
	}

	@Override
	public boolean existeClienteCodigo(String codigo) {
		return this.servicioAccesoBaseDatos.findByCodigo(codigo);
	}
}
