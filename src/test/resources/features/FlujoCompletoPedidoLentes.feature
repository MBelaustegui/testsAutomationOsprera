@flujoPedidoCompletoLentes
Feature: Flujo completo de pedido lentes
@admDelegLentes
  Scenario: Crear un nuevo pedido de lentes
    Given inicié sesión
    And selecciono el modulo SGP
    And ingreso el beneficiario
    When completo el pedido de lentes
    And envio a auditar
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

