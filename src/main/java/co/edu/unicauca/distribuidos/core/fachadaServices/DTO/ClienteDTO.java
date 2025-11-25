package co.edu.unicauca.distribuidos.core.fachadaServices.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.*;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ClienteDTO {
	private Integer id;

	@NotBlank(message = "{cliente.codigo.vacio}")
	private String codigo;

	@NotBlank(message = "{cliente.nombre.vacio}")
	@Size(min = 5, max = 45, message = "{cliente.nombre.tamano}")
	private String nombre;

	@NotBlank(message = "{cliente.apellido.vacio}")
	@Size(min = 5, max = 45, message = "{cliente.apellido.tamano}")
	private String apellido;

	@NotBlank(message = "{cliente.email.vacio}")
	@Email(message = "{cliente.email.invalido}")
	private String email;

	private Date createAt;
}
