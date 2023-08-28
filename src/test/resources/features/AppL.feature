@AppL
Feature: AppL
  In order edit the notices for offence
  As a admin user, James will be able to edit offence notice to the customer

  Background:
    Given James is at the CCRS home page

  @editNotice
  Scenario: Edit notice for offence
      James able to edit notice for the offence
      When James attempts to create notice
          | vehicleNo | identificationNo | identificationName | offenderAddressPostalCode | noticeStatus | displayFlag | contactNo | emailAddress | offenceGroupCode | offenceAmount | revenueCode |
          | A3033030601 | S303303062     | SR3033030602       | 680305                    |    2         |      p      | 86801794  | deepak.kumar@wipro.com | S_NO_CC_DIR | 200.00   | ERP_532     |
      And he attempts to edit the notice
          | contactNo | offenceAmount |
          | 12345678  | 300           |
      Then he will be to able to edit the notice

#  @searchNotice
#  Scenario: search notice
#    James wants to search notice
#    When James attempts to search notice
#    Then he will be able to search notice

  @Recon
  Scenario: Bank reconciliation
    James able to do bank reconciliation
    When James balances deposit control for notice
    And he uploads bank statement
    And he attempts to reconcile bank statement
    Then he is able to do bank reconciliation