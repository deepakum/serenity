@saleItem @sanity @createSaleItem
Feature: Sale item
  In order to edit sale item
  James as a maker user, able to edit sale item

  Background:
    Given James is at the CCRS login page
    And he has created sale item
      |user  | payment service category | payment service sub-category | item name | unit price |
      |Maker_DBC| Plans & Publications |Development & Building Control (DBC) Search Fee| automation | 100 |

    Scenario Outline: verification of editing sale item by maker user
      James as maker able to edit sale item
      When he tries to edit sale item by choosing "<payment service category>","<payment service sub-category>", "<item name>","<unit price>","<dynamic charge>", "<counter only>","<min amount>","<enter qty>","<single qty>","<email>","<ID type>" and "<comment>"
      Then he should be able to edit the sale item

      Examples:
        | payment service category        | payment service sub-category                    | item name  | unit price | dynamic charge| counter only  |min amount | enter qty | single qty  | email           | ID type                     |comment                  |
        | Plans & Publications            | Development & Building Control (DBC) Search Fee | automation | 100        | yes           |               | yes       |           |    yes      |deepak@gmail.com |Foreign Identification Number|                         |
        | Seminars & events               | Development & Building Control (DBC) Seminar Fee| automation | 200        |               | yes           |           |yes        |             |deepak@gmail.com |Singapore NRIC               |                         |
        | Heavy Vehicle Park Related Fine | Heavy Vehicle Park Related Fine                 | automation | 300        |               |               |           |           |             |                 | Unique Entity Number        |                         |
        | DBC Processing Fees             | Carpark Processing Fee                          | automation | 400        |               |               |           |           |             |                 |                             |  Carpark Processing Fee |
