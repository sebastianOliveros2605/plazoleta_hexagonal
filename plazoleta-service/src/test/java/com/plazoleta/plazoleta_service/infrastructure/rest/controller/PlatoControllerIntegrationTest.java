package com.plazoleta.plazoleta_service.infrastructure.rest.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.isNull;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.plazoleta.plazoleta_service.domain.exception.PrecioInvalidoException;
import com.plazoleta.plazoleta_service.domain.model.PaginacionResultado;
import com.plazoleta.plazoleta_service.domain.model.Plato;
import com.plazoleta.plazoleta_service.domain.ports.in.ICambiarEstadoActivoPlato;
import com.plazoleta.plazoleta_service.domain.ports.in.IConsultarPlatoUseCase;
import com.plazoleta.plazoleta_service.domain.ports.in.ICrearPlatoUseCase;
import com.plazoleta.plazoleta_service.domain.ports.in.IModificarPlatoUseCase;
import com.plazoleta.plazoleta_service.infrastructure.rest.dto.request.ModificarPlatoRequestDTO;
import com.plazoleta.plazoleta_service.infrastructure.rest.dto.request.PlatoRequestDTO;
import com.plazoleta.plazoleta_service.infrastructure.rest.dto.response.PlatoResponseDTO;
import com.plazoleta.plazoleta_service.infrastructure.rest.mapper.PlatoRestMapper;
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
class PlatoControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ICrearPlatoUseCase crearPlatoUseCase;

    @MockBean
    private IModificarPlatoUseCase modificarPlatoUseCase;

    @MockBean
    private ICambiarEstadoActivoPlato cambiarEstadoActivoPlato;

    @MockBean
    private IConsultarPlatoUseCase consultarPlatoUseCase;

    @MockBean
    private PlatoRestMapper platoRestMapper;

    @Test
    @WithMockUser(username = "10", roles = "PROPIETARIO")
    void crearPlato_CuandoRolPropietario_DeberiaRetornarCreated() throws Exception {
        PlatoRequestDTO request = new PlatoRequestDTO();
        request.setNombre("Ajiaco");
        request.setDescripcion("Sopa tradicional");
        request.setPrecio(25000);
        request.setUrlImagen("https://img.test/ajiaco.png");
        request.setIdRestaurante(1L);
        request.setIdCategoria(1L);

        when(platoRestMapper.toDomain(any(PlatoRequestDTO.class))).thenReturn(new Plato());

        mockMvc.perform(post("/platos/crearPlato")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());

        verify(crearPlatoUseCase).crearPlato(any(Plato.class), org.mockito.ArgumentMatchers.eq(10));
    }

    @Test
    @WithMockUser(username = "10", roles = "CLIENTE")
    void crearPlato_CuandoRolNoPermitido_DeberiaRetornarForbidden() throws Exception {
        PlatoRequestDTO request = new PlatoRequestDTO();
        request.setNombre("Ajiaco");
        request.setDescripcion("Sopa tradicional");
        request.setPrecio(25000);
        request.setUrlImagen("https://img.test/ajiaco.png");
        request.setIdRestaurante(1L);
        request.setIdCategoria(1L);

        mockMvc.perform(post("/platos/crearPlato")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "15", roles = "PROPIETARIO")
    void modificarPlato_CuandoRolPropietario_DeberiaRetornarOk() throws Exception {
        ModificarPlatoRequestDTO request = new ModificarPlatoRequestDTO();
        request.setDescripcion("Descripcion actualizada");
        request.setPrecio(30000);

        when(platoRestMapper.toDomain(any(ModificarPlatoRequestDTO.class))).thenReturn(new Plato());

        mockMvc.perform(put("/platos/modificar/5")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());

        verify(modificarPlatoUseCase).modificarPlato(any(Plato.class), org.mockito.ArgumentMatchers.eq(15));
    }

    @Test
    @WithMockUser(username = "10", roles = "PROPIETARIO")
    void crearPlato_CuandoReglaNegocioFalla_DeberiaRetornarBadRequest() throws Exception {
        PlatoRequestDTO request = new PlatoRequestDTO();
        request.setNombre("Ajiaco");
        request.setDescripcion("Sopa tradicional");
        request.setPrecio(0);
        request.setUrlImagen("https://img.test/ajiaco.png");
        request.setIdRestaurante(1L);
        request.setIdCategoria(1L);

        when(platoRestMapper.toDomain(any(PlatoRequestDTO.class))).thenReturn(new Plato());
        doThrow(new PrecioInvalidoException()).when(crearPlatoUseCase).crearPlato(any(Plato.class), org.mockito.ArgumentMatchers.eq(10));

        mockMvc.perform(post("/platos/crearPlato")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = "15", roles = "PROPIETARIO")
    void cambiarEstadoPlato_CuandoRolPropietario_DeberiaRetornarOk() throws Exception {
        mockMvc.perform(put("/platos/cambiarEstadoPlato/5/false")
                        .with(csrf()))
                .andExpect(status().isOk());

        verify(cambiarEstadoActivoPlato)
                .habilitarDeshabilitarPlato(org.mockito.ArgumentMatchers.eq(5L),
                        org.mockito.ArgumentMatchers.eq(15),
                        org.mockito.ArgumentMatchers.eq(false));
    }

    @Test
    @WithMockUser(username = "10", roles = "CLIENTE")
    void cambiarEstadoPlato_CuandoRolNoPermitido_DeberiaRetornarForbidden() throws Exception {
        mockMvc.perform(put("/platos/cambiarEstadoPlato/5/false")
                        .with(csrf()))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "CLIENTE")
    void listarPlatosPorRestaurante_SinCategoria_CuandoRolPermitido_DeberiaRetornarOk() throws Exception {
        Plato plato = new Plato();
        plato.setId(10L);
        plato.setNombre("Ajiaco");

        PlatoResponseDTO dto = new PlatoResponseDTO();
        dto.setId(10L);
        dto.setNombre("Ajiaco");

        when(consultarPlatoUseCase.listarPlatosPorRestaurante(1L, null, 0, 2))
                .thenReturn(new PaginacionResultado<>(List.of(plato), 0, 2, 1, 1, true));
        when(platoRestMapper.toResponseList(any())).thenReturn(List.of(dto));

        mockMvc.perform(get("/platos/listar")
                        .param("idRestaurante", "1")
                        .param("page", "0")
                        .param("size", "2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(10))
                .andExpect(jsonPath("$.content[0].nombre").value("Ajiaco"));

        verify(consultarPlatoUseCase).listarPlatosPorRestaurante(1L, null, 0, 2);
    }

    @Test
    @WithMockUser(roles = "EMPLEADO")
    void listarPlatosPorRestaurante_ConCategoria_CuandoRolPermitido_DeberiaRetornarOk() throws Exception {
        when(consultarPlatoUseCase.listarPlatosPorRestaurante(1L, 3L, 0, 2))
                .thenReturn(new PaginacionResultado<>(List.of(), 0, 2, 0, 0, true));
        when(platoRestMapper.toResponseList(any())).thenReturn(List.of());

        mockMvc.perform(get("/platos/listar")
                        .param("idRestaurante", "1")
                        .param("idCategoria", "3")
                        .param("page", "0")
                        .param("size", "2"))
                .andExpect(status().isOk());

        verify(consultarPlatoUseCase).listarPlatosPorRestaurante(1L, 3L, 0, 2);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void listarPlatosPorRestaurante_CuandoRolNoPermitido_DeberiaRetornarForbidden() throws Exception {
        mockMvc.perform(get("/platos/listar")
                        .param("idRestaurante", "1")
                        .param("page", "0")
                        .param("size", "2"))
                .andExpect(status().isForbidden());

        verify(consultarPlatoUseCase, never()).listarPlatosPorRestaurante(anyLong(), isNull(), anyInt(), anyInt());
    }
}
