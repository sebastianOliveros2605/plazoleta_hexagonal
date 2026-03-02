package com.plazoleta.plazoleta_service.infrastructure.rest.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.plazoleta.plazoleta_service.domain.exception.UsuarioNoExisteException;
import com.plazoleta.plazoleta_service.domain.model.PaginacionResultado;
import com.plazoleta.plazoleta_service.domain.model.Restaurante;
import com.plazoleta.plazoleta_service.domain.ports.in.IConsultarRestauranteUseCase;
import com.plazoleta.plazoleta_service.domain.ports.in.ICrearRestauranteUseCase;
import com.plazoleta.plazoleta_service.infrastructure.rest.dto.request.CrearRestauranteRequest;
import com.plazoleta.plazoleta_service.infrastructure.rest.mapper.RestauranteRestMapper;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class RestauranteControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ICrearRestauranteUseCase crearRestauranteUseCase;

    @MockBean
    private IConsultarRestauranteUseCase consultarRestauranteUseCase;

    @MockBean
    private RestauranteRestMapper restauranteRestMapper;

    @Test
    @WithMockUser(roles = "ADMIN")
    void crearRestaurante_CuandoRolAdmin_DeberiaRetornarCreated() throws Exception {
        CrearRestauranteRequest request = new CrearRestauranteRequest();
        request.setNombre("Sazon Costena");
        request.setNit("900123");
        request.setDireccion("Calle 1");
        request.setTelefono("+573001112233");
        request.setUrlLogo("https://img.test/logo.png");
        request.setIdPropietario(7);

        when(restauranteRestMapper.toDomain(any(CrearRestauranteRequest.class))).thenReturn(new Restaurante());

        mockMvc.perform(post("/restaurantes/crear")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());

        verify(crearRestauranteUseCase).ejecutar(any(Restaurante.class));
    }

    @Test
    @WithMockUser(roles = "PROPIETARIO")
    void crearRestaurante_CuandoRolNoAdmin_DeberiaRetornarForbidden() throws Exception {
        CrearRestauranteRequest request = new CrearRestauranteRequest();
        request.setNombre("Sazon Costena");
        request.setNit("900123");
        request.setDireccion("Calle 1");
        request.setTelefono("+573001112233");
        request.setUrlLogo("https://img.test/logo.png");
        request.setIdPropietario(7);

        mockMvc.perform(post("/restaurantes/crear")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void crearRestaurante_CuandoUsuarioNoExiste_DeberiaRetornarNotFound() throws Exception {
        CrearRestauranteRequest request = new CrearRestauranteRequest();
        request.setNombre("Sazon Costena");
        request.setNit("900123");
        request.setDireccion("Calle 1");
        request.setTelefono("+573001112233");
        request.setUrlLogo("https://img.test/logo.png");
        request.setIdPropietario(99);

        when(restauranteRestMapper.toDomain(any(CrearRestauranteRequest.class))).thenReturn(new Restaurante());
        doThrow(new UsuarioNoExisteException()).when(crearRestauranteUseCase).ejecutar(any(Restaurante.class));

        mockMvc.perform(post("/restaurantes/crear")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(roles = "CLIENTE")
    void listarRestaurantes_CuandoRolCliente_DeberiaRetornarOk() throws Exception {
        Restaurante restaurante = new Restaurante();
        restaurante.setId(1L);
        restaurante.setNombre("Arepas House");
        restaurante.setUrlLogo("https://img.test/logo1.png");

        when(consultarRestauranteUseCase.listarRestaurantes(0, 2))
                .thenReturn(new PaginacionResultado<>(List.of(restaurante), 0, 2, 1, 1, true));

        mockMvc.perform(get("/restaurantes/listar")
                        .param("page", "0")
                        .param("size", "2"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "PROPIETARIO")
    void listarRestaurantes_CuandoRolNoPermitido_DeberiaRetornarForbidden() throws Exception {
        mockMvc.perform(get("/restaurantes/listar")
                        .param("page", "0")
                        .param("size", "2"))
                .andExpect(status().isForbidden());
    }
}
