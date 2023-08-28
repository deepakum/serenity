@EVMS
Feature: LTA fines and fees
  In order to clear fines at the ccrs counter
  As a vehicle user, James will be able to clear notice fines at ccrs counter

  Background:
    Given James is at the CCRS home page
    And James receives fine notice
      | vehicleNo | identificationNo | identificationName | offenderAddressPostalCode | noticeStatus | displayFlag | contactNo | emailAddress | offenceGroupCode | offenceAmount | revenueCode |
      | A3033030601 | S303303062     | SR3033030602       | 680305                    |    2         |      p      | 86801794  | deepak.kumar@wipro.com | S_NO_CC_DIR | 200.00   | ERP_532     |

  Scenario: Notice payment at ccrs counter using '<tender type>' payment method
    James able to pay fines at ccrs counter
    Then he will be able to process refund
