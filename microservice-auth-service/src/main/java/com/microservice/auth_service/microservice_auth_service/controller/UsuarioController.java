package com.microservice.auth_service.microservice_auth_service.controller;

import com.microservice.auth_service.microservice_auth_service.dto.ActualizarFotoPerfilRequest;
import com.microservice.auth_service.microservice_auth_service.dto.UsuarioRequest;
import com.microservice.auth_service.microservice_auth_service.dto.UsuarioResponse;
import com.microservice.auth_service.microservice_auth_service.service.UsuarioService;
import com.microservice.auth_service.microservice_auth_service.dto.LoginRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/auth/usuarios")
@Tag(name = "Usuarios", description = "Gestión de usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @Operation(
            summary = "Listado de usuarios",
            description = "Devuelve todos los usuarios registrados en el sistema."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Listado obtenido correctamente",
                    content = @Content(array = @ArraySchema(
                            schema = @Schema(implementation = UsuarioResponse.class)
                    ))
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Error interno del servidor",
                    content = @Content
            )
    })
    @GetMapping
    public ResponseEntity<List<UsuarioResponse>> getAll() {
        return ResponseEntity.ok(usuarioService.getAll());
    }

    @Operation(
            summary = "Obtener usuario por id",
            description = "Devuelve la información de un usuario específico según su ID."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Usuario encontrado",
                    content = @Content(schema = @Schema(implementation = UsuarioResponse.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Usuario no encontrado",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Error interno del servidor",
                    content = @Content
            )
    })
    @GetMapping("/{id}")
    public ResponseEntity<UsuarioResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(usuarioService.getById(id));
    }

    @Operation(
            summary = "Crear usuario",
            description = "Crea un nuevo usuario con los datos proporcionados."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Usuario creado correctamente",
                    content = @Content(schema = @Schema(implementation = UsuarioResponse.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Datos de entrada inválidos",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Error interno del servidor",
                    content = @Content
            )
    })
    @PostMapping
    public ResponseEntity<UsuarioResponse> create(@RequestBody UsuarioRequest request) {
        return ResponseEntity.ok(usuarioService.create(request));
    }

    @Operation(
            summary = "Actualizar usuario",
            description = "Actualiza la información de un usuario existente."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Usuario actualizado correctamente",
                    content = @Content(schema = @Schema(implementation = UsuarioResponse.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Datos de entrada inválidos",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Usuario no encontrado",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Error interno del servidor",
                    content = @Content
            )
    })
    @PutMapping("/{id}")
    public ResponseEntity<UsuarioResponse> update(@PathVariable Long id,
                                                  @RequestBody UsuarioRequest request) {
        return ResponseEntity.ok(usuarioService.update(id, request));
    }

    @Operation(
            summary = "Eliminar usuario",
            description = "Elimina un usuario por su ID. Si tiene datos asociados, se devuelve un conflicto."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "204",
                    description = "Usuario eliminado correctamente"
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "No se puede eliminar el usuario porque tiene datos asociados",
                    content = @Content(mediaType = MediaType.TEXT_PLAIN_VALUE)
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Usuario no encontrado",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Error interno del servidor",
                    content = @Content
            )
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        try {
            usuarioService.delete(id);
            return ResponseEntity.noContent().build();
        } catch (DataIntegrityViolationException ex) {
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body("No se puede eliminar el usuario porque tiene datos asociados (partidas, feedback, etc.)");
        }
    }

    @Operation(
            summary = "Obtener puntaje global de un usuario",
            description = "Devuelve el puntaje global acumulado de un usuario."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Puntaje obtenido correctamente",
                    content = @Content(schema = @Schema(implementation = Integer.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Usuario no encontrado",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Error interno del servidor",
                    content = @Content
            )
    })
    @GetMapping("/{id}/puntaje-global")
    public ResponseEntity<Integer> getPuntajeGlobal(@PathVariable Long id) {
        int puntaje = usuarioService.obtenerPuntajeGlobal(id);
        return ResponseEntity.ok(puntaje);
    }

    @Operation(
            summary = "Actualizar puntaje global sumando un delta",
            description = "Suma un valor al puntaje global del usuario y devuelve el nuevo total."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Puntaje actualizado correctamente",
                    content = @Content(schema = @Schema(implementation = Integer.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Parámetro delta inválido",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Usuario no encontrado",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Error interno del servidor",
                    content = @Content
            )
    })
    @PostMapping("/{id}/puntaje-global")
    public ResponseEntity<Integer> updatePuntajeGlobal(
            @PathVariable Long id,
            @RequestParam("delta") int delta
    ) {
        int nuevo = usuarioService.actualizarPuntajeGlobal(id, delta);
        return ResponseEntity.ok(nuevo);
    }

    @PatchMapping("/{id}/foto-perfil")
    @Operation(
            summary = "Actualizar la foto de perfil de un usuario",
            description = """
                    Permite actualizar la foto de perfil de un usuario. 
                    La imagen debe enviarse codificada en Base64.
                    Si se envía una cadena vacía, la foto será eliminada.
                    """
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "Foto de perfil actualizada correctamente"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Solicitud inválida (Base64 mal formado o JSON incorrecto)",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Usuario no encontrado con el id especificado",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Error interno del servidor",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            )
    })
    public ResponseEntity<Void> actualizarFotoPerfil(
            @PathVariable Long id,
            @RequestBody ActualizarFotoPerfilRequest request
    ) {
        usuarioService.actualizarFotoPerfil(id, request);
        return ResponseEntity.noContent().build();
    }

    @Operation(
        summary = "Login de usuario",
        description = "Valida el identificador (correo o nombre) y la contraseña. Devuelve los datos del usuario si las credenciales son válidas."
        )
        @ApiResponses({
                @ApiResponse(
                        responseCode = "200",
                        description = "Login correcto",
                        content = @Content(schema = @Schema(implementation = UsuarioResponse.class))
                ),
                @ApiResponse(
                        responseCode = "401",
                        description = "Credenciales inválidas",
                        content = @Content
                )
        })
        @PostMapping("/login")
        public ResponseEntity<UsuarioResponse> login(@RequestBody LoginRequest request) {
        try {
                UsuarioResponse usuario = usuarioService.loginPorIdentificadorYClave(
                        request.identificador(),
                        request.clave()
                );
                return ResponseEntity.ok(usuario);
        } catch (RuntimeException ex) {
                // Podrías diferenciar por mensaje si quieres
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        }

        @Operation(
        summary = "Actualizar contraseña por correo",
        description = "Actualiza la contraseña de un usuario buscando por correo (o identificador) y guardándola encriptada."
        )
        @ApiResponses({
                @ApiResponse(
                        responseCode = "204",
                        description = "Contraseña actualizada correctamente"
                ),
                @ApiResponse(
                        responseCode = "404",
                        description = "Usuario no encontrado",
                        content = @Content
                )
        })
        @PutMapping("/password")
        public ResponseEntity<Void> actualizarPasswordPorCorreo(
                @RequestParam("identificador") String identificador,
                @RequestParam("nuevaClave") String nuevaClave
        ) {
        usuarioService.actualizarPasswordPorCorreo(identificador, nuevaClave);
        return ResponseEntity.noContent().build();
        }

}
