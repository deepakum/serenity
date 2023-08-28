@saleItem @sanity @createSaleItem
Feature: Sale item
  In order to create sale item
  James as a maker user able to create sale item,
  James as a checker user able to verify and approve/reject sale item,
  James as a finance user able to approve/reject sale item

  Background:
    Given James is at the CCRS login page

  @createItem
  Scenario Outline: duplicate sale item
  James as maker able to create sale item
    When James as a "<user>" attempts to login to CCRS page
    And he tries to add sale item by choosing "<payment service category>","<payment service sub-category>", "<item name>" and "<unit price>"
    And he attempts to create sale item
    Then he should be able to create the sale item
    And he attempts to send sale item approval request to checker
    Then he should be able to send sale item approval to checker
    And he attempts to send sale item approval request to finance user
    Then he should be able to send sale item approval to finance user
    And he attempts to approve sale item request
    Then he should be able to approve sale item request
    When he attempts to duplicate approved sale item
    Then he will be able to duplicate approved sale item
    Examples:
      |user  | payment service category | payment service sub-category | item name | unit price |
      |Maker_DBC| Plans & Publications |Development & Building Control (DBC) Search Fee| automation | 100 |
#      |Maker_DBC| Seminars & events | Development & Building Control (DBC) Seminar Fee| automation | 200 |
#      |Maker_DBC| Heavy Vehicle Park Related Fine | Heavy Vehicle Park Related Fine | automation | 300 |
#      |Maker_DBC| DBC Processing Fees | Carpark Processing Fee | automation | 400 |