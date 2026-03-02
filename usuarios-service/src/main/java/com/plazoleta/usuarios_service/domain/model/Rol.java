package com.plazoleta.usuarios_service.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Rol {
    public static final Integer ADMIN_ID = 1;
    public static final Integer PROPIETARIO_ID = 2;
    public static final Integer EMPLEADO_ID = 3;
    public static final Integer CLIENTE_ID = 4;

    public static final String ADMIN = "ADMIN";
    public static final String PROPIETARIO = "PROPIETARIO";
    public static final String EMPLEADO = "EMPLEADO";
    public static final String CLIENTE = "CLIENTE";

    private Integer id;
    private String nombre;
    private String descripcion;
}
