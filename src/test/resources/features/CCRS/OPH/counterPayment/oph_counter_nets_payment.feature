@saleItemCounter @sanity @saleItem @saleItemNets
Feature: Sale Item
  In order to purchase sale item at counter
  As a Admin user, James will be able to purchase sale item at counter

  Background:
    Given James is at the CCRS login page

  Scenario Outline: Sale item purchase at ccrs counter using "<tender type>"
    James able to refund outstanding invoice

    When James as a "<user>" attempts to login to CCRS page
#    And he attempts to create sale item by choosing "<payment service category>","<payment service sub-category>", "<item name>" and "<unit price>"
#    And James opens deposit control for "<tender source>" tender source and "<tender type>" tender type
    And James attempts to purchase sale item at counter using nets payment method
        | tender type | payment service category        |           payment service sub-category              | quantity |
        |   NETS      | Plans & Publications            | Development & Building Control (DBC) Search Fee  |     4    |
#        |   Cash Card | Plans & Publications            | Development & Building Control (DBC) Search Fee  |     4    |
#        |   Flash Pay | Plans & Publications            | Development & Building Control (DBC) Search Fee  |     4    |
    When he retrieves all NETS-POS transactions details
    And he processes Nets-POS refund payment method for outstanding invoice
#      |  tender source|tender type| refund method |   refund reason   | business system |
#      |   COUNTER     | Flash Pay |     GIRO      | Disputed payment  | Development & Building Control |
#      |   COUNTER     | Flash Pay |     Cheque    | Excess payment    | Development & Building Control  |
#      |   COUNTER     | Cash Card |     GIRO      | Disputed payment  | Development & Building Control  |
#      |   COUNTER     | Cash Card |     Cheque    | Disputed payment  | Development & Building Control  |
#      |   COUNTER     | NETS      |     GIRO      | Disputed payment  | Development & Building Control  |
#      |   COUNTER     | NETS      |     Cheque    | Disputed payment  | Development & Building Control  |

    Examples:
      | user |payment service category|payment service sub-category|  tender source|tender type| item name |unit price|
      |Maker_DBC|Plans & Publications|Development & Building Control (DBC) Search Fee S7|COUNTER | NETS-POS    |automation| 100  |