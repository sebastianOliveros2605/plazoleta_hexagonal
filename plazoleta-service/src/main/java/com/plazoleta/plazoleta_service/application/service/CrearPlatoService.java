package com.plazoleta.plazoleta_service.application.service;

import com.plazoleta.plazoleta_service.domain.exception.CategoriaNoExisteException;
import com.plazoleta.plazoleta_service.domain.exception.NoEsPropietarioException;
import com.plazoleta.plazoleta_service.domain.exception.RestauranteNoExisteException;
import com.plazoleta.plazoleta_service.domain.model.Plato;
import com.plazoleta.plazoleta_service.domain.model.Restaurante;
import com.plazoleta.plazoleta_service.domain.ports.in.ICrearPlatoUseCase;
import com.plazoleta.plazoleta_service.domain.ports.out.ICategoriaRepositoryPort;
import com.plazoleta.plazoleta_service.domain.ports.out.IPlatoRepositoryPort;
import com.plazoleta.plazoleta_service.domain.ports.out.IRestauranteRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CrearPlatoService implements ICrearPlatoUseCase{

    private final IPlatoRepositoryPort platoRepositoryPort;
    private final IRestauranteRepositoryPort restauranteRepositoryPort;
    private final ICategoriaRepositoryPort categoriaRepositoryPort;

    @Override
    public void crearPlato(Plato plato, Integer idPropietario) {
        plato.validarReglasDeCreacion();

        Restaurante restaurante = restauranteRepositoryPort
                .buscarPorId(plato.getIdRestaurante())
                .orElseThrow(RestauranteNoExisteException::new);

        if (!restaurante.getIdPropietario().equals(idPropietario)) {
            throw new NoEsPropietarioException();
        }
        if(categoriaRepositoryPort.buscarPorId(plato.getIdCategoria())==null){
            throw new CategoriaNoExisteException();
        }
        platoRepositoryPort.guardar(plato);
    }
    
}
