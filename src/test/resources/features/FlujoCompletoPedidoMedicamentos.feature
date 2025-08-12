@flujoPedidoCompleto
Feature: Flujo completo de pedido medicamentos
@admDeleg
  Scenario: Crear un nuevo pedido luego del login
    Given que ya inicié sesión correctamente
    And entro al modulo SGP
    And selecciono al beneficiario
    When completo el pedido de medicamentos
    And envio a auditoria
    Then cierro el navegador
@audDeleg
  Scenario: Auditar un pedido
    Given ingreso al sistema como auditor
    And entro al modulo de SGP
    When busco el pedido generado
    And autorizo el pedido
@audCentral
  Scenario: Auditar un pedido
    Given ingreso al sistema como auditor central
    And entro al modulo de SGP para auditar
    When busco el pedido 
    And autorizo el pedido en sede central

