Feature: Solicitud de pedido protesis

  Scenario: Crear un nuevo pedido luego del login
    Given que ya inicié sesión correctamente
    And entro al modulo SGP
    And selecciono al beneficiario con documento "31657724"
    When completo el pedido de protesis
    And envio a auditoria
    Then cierro el navegador