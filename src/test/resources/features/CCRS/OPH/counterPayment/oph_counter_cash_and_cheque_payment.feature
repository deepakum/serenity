@saleItemCounter @sanity @saleItem @saleItemCheque @counterPaymentChe
Feature: Sale item
  In order to purchase sale item at counter
  As a Admin user, James will be able to purchase sale item at counter

  Background:
    Given James is at the CCRS login page

  Scenario Outline: Sale item purchase at ccrs counter using "<tender type>"
  James able to refund outstanding invoice

    When James as a "<user>" attempts to login to CCRS page
   # And he attempts to create sale item by choosing "<payment service category>","<payment service sub-category>", "<sale item>" and "<unit price>"
#    And James opens deposit control for "<tender source>" tender source and "<tender type>" tender type
    And James attempts to purchase sale item at counter using "<tender type>" by choosing "<payment service category>", "<payment service sub-category>" and "<quantity>" item
    And he attempts to process refund by "<refund mode>" on "<business system>" purchased on "<application>"

    Examples:
      | user    |payment service category       |payment service sub-category                     |quantity |tender source|tender type|refund mode|reason          |business system               |sale item |unit price|application  |
#      |Maker_DBC|Plans & Publications           |Development & Building Control (DBC) Search Fee  |   1     |COUNTER      | Cash      | Cheque    |Disputed payment|Development & Building Control|automation|  100     | counter     |
      |Maker_DBC|Seminars & events              |Development & Building Control (DBC) Seminar Fee|   2     |COUNTER      | Cheque      | GIRO      |Disputed payment|Development & Building Control|automation|  200     | counter     |
#      |Maker_DBC|Heavy Vehicle Park Related Fine|Heavy Vehicle Park Related Fine                 |   3     |COUNTER      | Cheque    | Cheque    |Disputed payment|Development & Building Control|automation|  300     | counter     |
#      |Maker_DBC|DBC Processing Fees            |Carpark Processing Fee                          |   4     |COUNTER      | Cheque    | GIRO      |Disputed payment|Development & Building Control|automation|  500     | counter     |
