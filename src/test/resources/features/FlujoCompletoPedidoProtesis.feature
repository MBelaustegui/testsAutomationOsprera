@flujoPedidoCompletoProtesis
Feature: Flujo completo de pedido protesis
@admDelegProtesis
  Scenario: Crear un nuevo pedido de prótesis luego del login
    Given que ya inicié sesión
    And entro a SGP
    And ingreso el DNI del beneficiario
    When completo el pedido de protesis
    Then envio a auditoria provincial
@audDeleg
  Scenario: Auditar un pedido
    Given ingreso al sistema como auditor
    And entro al modulo de SGP
    When busco el pedido generado
    And autorizo el pedido
@audCentral
  Scenario: Auditar un pedido en sede Central
    Given ingreso al sistema como auditor central
    And entro al modulo de SGP para auditar
    When busco el pedido 
    And autorizo el pedido en sede central

