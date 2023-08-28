@saleItemCounter @sanity @saleItem @saleItemQR
Feature: Sale Item
  In order to purchase sale item at ccrs counter
  As a Admin user, James will be able to purchase sale item at ccrs counter

  Background:
    Given James is at the CCRS login page

  Scenario Outline: Sale item purchase at ccrs counter using "<tender type>"
    James able to refund outstanding invoice

    When James as a "<user>" attempts to login to CCRS page
#    And he attempts to create sale item by choosing "<payment service category>","<payment service sub-category>", "<item name>" and "<unit price>"
    And James opens deposit control for "<tender source>" tender source and "<tender type>" tender type
    And James attempts to purchase sale item at counter using nets payment method
      | tender type | payment service category |    payment service sub-category                     | quantity |
      | QR Code | Plans & Publications     | Development & Building Control (DBC) Search Fee|     4    |
    When he retrieves all QR code transactions details
#    Then he processes Nets-POS refund payment method for outstanding invoice
#      |tender source|tender type| refund method |refund reason   |business system                        |
#      |  COUNTER    |QR Code    |    GIRO       |Disputed payment|Enterprise Violation Management System |
#      |  COUNTER    |QR Code    |     Cheque    |Excess payment  |Enterprise Violation Management System |

    Examples:
      | user |payment service category|payment service sub-category|sale item|quantity|  tender source|tender type|organisation|pin code| mobile |refund method| sale item |unit price|
      |Maker_DBC|Plans & Publications|Development & Building Control (DBC) Search Fee S7|   | 1 |COUNTER | QR Code | WIPRO | 370066 |86801794| Cheque   |   automation  | 100  |