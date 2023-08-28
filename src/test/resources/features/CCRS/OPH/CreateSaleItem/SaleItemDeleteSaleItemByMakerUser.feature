@saleItem @sanity @createSaleItem
Feature: Sale item
  In order to delete sale item
  James as a maker user, able to edit sale item

  Background:
    Given James is at the CCRS login page
    And he has created sale item
      |user  | payment service category | payment service sub-category | item name | unit price |
      |Maker_DBC| Plans & Publications |Development & Building Control (DBC) Search Fee | automation | 100 |

    Scenario Outline: delete sale item by maker user
      James as maker able to delete sale item
      When he as a "<user>" attempts to delete sale item
      Then he should be able to delete the sale item

      Examples:
        |user  |
        |Maker_DBC|
