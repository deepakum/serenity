@saleItemCounter @sanity @saleItem @saleItemCC
Feature: Sale Item
  In order to purchase sale item at counter
  As a Admin user, James will be able to purchase sale item at counter

  Background:
    Given James is at the CCRS login page

  Scenario Outline: Sale item purchase at ccrs counter using "<tender type>"
    James able to refund outstanding invoice

    When James as a "<user>" attempts to login to CCRS page
#    And he attempts to create sale item by choosing "<payment service category>","<payment service sub-category>", "<item name>" and "<unit price>"
    And James opens deposit control for "<tender source>" tender source and "<tender type>" tender type
    And James attempts to purchase sale item at counter using nets payment method
        | tender type | payment service category |    payment service sub-category                     | quantity |
        | Credit Card | Plans & Publications     | Development & Building Control (DBC) Search Fee|     4    |
    When he retrieves credit card transactions details
#    And he processes Nets-POS refund payment method for outstanding invoice
#      |  tender source|tender type| refund method |   refund reason   | business system|
#      |   COUNTER     | Flash Pay |     GIRO      | Disputed payment  | Development & Building Control|
#      |   COUNTER     | Flash Pay |     Cheque    | Excess payment    | Development & Building Control|

    Examples:
      | user |payment service category|payment service sub-category|sale item|quantity|  tender source|tender type | item name |unit price|
      |Maker_DBC|Plans & Publications|Development & Building Control (DBC) Search Fee S7|   | 1 |COUNTER | Credit Card |automation| 100  |