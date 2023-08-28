package Utility.CSV;

import com.opencsv.bean.CsvBindByName;

public class StripeCsvBean {

    @CsvBindByName(column="balance_transaction_id")
    private String balance_transaction_id;
    @CsvBindByName(column="created_utc")
    private String created_utc;
    @CsvBindByName(column="created")
    private String created;
    @CsvBindByName(column="available_on_utc")
    private String available_on_utc;
    @CsvBindByName(column="available_on")
    private String available_on;
    @CsvBindByName(column="currency")
    private String currency;
    @CsvBindByName(column="gross")
    private String gross;
    @CsvBindByName(column="fee")
    private String fee;
    @CsvBindByName(column="net")
    private String net;
    @CsvBindByName(column="reporting_category")
    private String reporting_category;
    @CsvBindByName(column="source_id")
    private String source_id;
    @CsvBindByName(column="description")
    private String description;
    @CsvBindByName(column="customer_facing_amount")
    private String customer_facing_amount;
    @CsvBindByName(column="customer_facing_currency")
    private String customer_facing_currency;
    @CsvBindByName(column="regulatory_tag")
    private String regulatory_tag;
    @CsvBindByName(column="automatic_payout_id")
    private String automatic_payout_id;
    @CsvBindByName(column="automatic_payout_effective_at_utc")
    private String automatic_payout_effective_at_utc;
    @CsvBindByName(column="automatic_payout_effective_at")
    private String automatic_payout_effective_at;
    @CsvBindByName(column="charge_id")
    private String charge_id;
    @CsvBindByName(column="payment_intent_id")
    private String payment_intent_id;
    @CsvBindByName(column="charge_created_utc")
    private String charge_created_utc;
    @CsvBindByName(column="charge_created")
    private String charge_created;
    @CsvBindByName(column="invoice_id")
    private String invoice_id;
    @CsvBindByName(column="subscription_id")
    private String subscription_id;
    @CsvBindByName(column="payment_method_type")
    private String payment_method_type;
    @CsvBindByName(column="card_brand")
    private String card_brand;
    @CsvBindByName(column="card_funding")
    private String card_funding;
    @CsvBindByName(column="card_country")
    private String card_country;
    @CsvBindByName(column="statement_descriptor")
    private String statement_descriptor;
    @CsvBindByName(column="dispute_reason")
    private String dispute_reason;
    @CsvBindByName(column="connected_account_id")
    private String connected_account_id;
    @CsvBindByName(column="connected_account_name")
    private String connected_account_name;
    @CsvBindByName(column="connected_account_country")
    private String connected_account_country;
    @CsvBindByName(column="payment_metadata[order_id]")
    private String payment_metadata_order_id;
    @CsvBindByName(column="payment_metadata[business_app_id]")
    private String payment_metadata_business_app_id;
    @CsvBindByName(column="payment_metadata[payment_mode]")
    private String payment_metadata_payment_mode;
    @CsvBindByName(column="refund_metadata[refund_id]")
    private String refund_metadata_refund_id;
    @CsvBindByName(column="payment_metadata[payment_mode]")
    private String refund_metadata_business_app_id;

    //generate setter and getter

    public String getBalance_transaction_id() {
        return balance_transaction_id;
    }
    public void setBalance_transaction_id(String balance_transaction_id) {
        this.balance_transaction_id = balance_transaction_id;
    }
    public String getCreated_utc() {
        return created_utc;
    }
    public void setCreated_utc(String created_utc) {
        this.created_utc = created_utc;
    }
    public String getCreated() {
        return created;
    }
    public void setCreated(String string) {
        this.created = string;
    }
    public String getAvailable_on_utc() {
        return available_on_utc;
    }
    public void setAvailable_on_utc(String available_on_utc) {
        this.available_on_utc = available_on_utc;
    }
    public String getAvailable_on() {
        return available_on;
    }
    public void setAvailable_on(String available_on) {
        this.available_on = available_on;
    }
    public String getCurrency() {
        return currency;
    }
    public void setCurrency(String currency) {
        this.currency = currency;
    }
    public String getGross() {
        return gross;
    }
    public void setGross(String gross) {
        this.gross = gross;
    }
    public String getFee() {
        return fee;
    }
    public void setFee(String fee) {
        this.fee = fee;
    }
    public String getNet() {
        return net;
    }
    public void setNet(String net) {
        this.net = net;
    }
    public String getReporting_category() {
        return reporting_category;
    }
    public void setReporting_category(String reporting_category) {
        this.reporting_category = reporting_category;
    }
    public String getSource_id() {
        return source_id;
    }
    public void setSource_id(String source_id) {
        this.source_id = source_id;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public String getCustomer_facing_amount() {
        return customer_facing_amount;
    }
    public void setCustomer_facing_amount(String customer_facing_amount) {
        this.customer_facing_amount = customer_facing_amount;
    }
    public String getCustomer_facing_currency() {
        return customer_facing_currency;
    }
    public void setCustomer_facing_currency(String customer_facing_currency) {
        this.customer_facing_currency = customer_facing_currency;
    }
    public String getRegulatory_tag() {
        return regulatory_tag;
    }
    public void setRegulatory_tag(String regulatory_tag) {
        this.regulatory_tag = regulatory_tag;
    }
    public String getAutomatic_payout_id() {
        return automatic_payout_id;
    }
    public void setAutomatic_payout_id(String automatic_payout_id) {
        this.automatic_payout_id = automatic_payout_id;
    }
    public String getAutomatic_payout_effective_at_utc() {
        return automatic_payout_effective_at_utc;
    }
    public void setAutomatic_payout_effective_at_utc(String automatic_payout_effective_at_utc) {
        this.automatic_payout_effective_at_utc = automatic_payout_effective_at_utc;
    }
    public String getAutomatic_payout_effective_at() {
        return automatic_payout_effective_at;
    }
    public void setAutomatic_payout_effective_at(String automatic_payout_effective_at) {
        this.automatic_payout_effective_at = automatic_payout_effective_at;
    }
    public String getCharge_id() {
        return charge_id;
    }
    public void setCharge_id(String charge_id) {
        this.charge_id = charge_id;
    }
    public String getPayment_intent_id() {
        return payment_intent_id;
    }
    public void setPayment_intent_id(String payment_intent_id) {
        this.payment_intent_id = payment_intent_id;
    }
    public String getCharge_created_utc() {
        return charge_created_utc;
    }
    public void setCharge_created_utc(String charge_created_utc) {
        this.charge_created_utc = charge_created_utc;
    }
    public String getCharge_created() {
        return charge_created;
    }
    public void setCharge_created(String charge_created) {
        this.charge_created = charge_created;
    }
    public String getInvoice_id() {
        return invoice_id;
    }
    public void setInvoice_id(String invoice_id) {
        this.invoice_id = invoice_id;
    }
    public String getSubscription_id() {
        return subscription_id;
    }
    public void setSubscription_id(String subscription_id) {
        this.subscription_id = subscription_id;
    }
    public String getPayment_method_type() {
        return payment_method_type;
    }
    public void setPayment_method_type(String payment_method_type) {
        this.payment_method_type = payment_method_type;
    }
    public String getCard_brand() {
        return card_brand;
    }
    public void setCard_brand(String card_brand) {
        this.card_brand = card_brand;
    }
    public String getCard_funding() {
        return card_funding;
    }
    public void setCard_funding(String card_funding) {
        this.card_funding = card_funding;
    }
    public String getCard_country() {
        return card_country;
    }
    public void setCard_country(String card_country) {
        this.card_country = card_country;
    }
    public String getStatement_descriptor() {
        return statement_descriptor;
    }
    public void setStatement_descriptor(String statement_descriptor) {
        this.statement_descriptor = statement_descriptor;
    }
    public String getDispute_reason() {
        return dispute_reason;
    }
    public void setDispute_reason(String dispute_reason) {
        this.dispute_reason = dispute_reason;
    }
    public String getConnected_account_id() {
        return connected_account_id;
    }
    public void setConnected_account_id(String connected_account_id) {
        this.connected_account_id = connected_account_id;
    }
    public String getConnected_account_name() {
        return connected_account_name;
    }
    public void setConnected_account_name(String connected_account_name) {
        this.connected_account_name = connected_account_name;
    }
    public String getConnected_account_country() {
        return connected_account_country;
    }
    public void setConnected_account_country(String connected_account_country) {
        this.connected_account_country = connected_account_country;
    }
    public String getPayment_metadata_order_id() {
        return payment_metadata_order_id;
    }
    public void setPayment_metadata_order_id(String payment_metadata_order_id) {
        this.payment_metadata_order_id = payment_metadata_order_id;
    }
    public String getPayment_metadata_business_app_id() {
        return payment_metadata_business_app_id;
    }
    public void setPayment_metadata_business_app_id(String payment_metadata_business_app_id) {
        this.payment_metadata_business_app_id = payment_metadata_business_app_id;
    }
    public String getPayment_metadata_payment_mode() {
        return payment_metadata_payment_mode;
    }
    public void setPayment_metadata_payment_mode(String payment_metadata_payment_mode) {
        this.payment_metadata_payment_mode = payment_metadata_payment_mode;
    }
    public String getRefund_metadata_refund_id() {
        return refund_metadata_refund_id;
    }
    public void setRefund_metadata_refund_id(String refund_metadata_refund_id) {
        this.refund_metadata_refund_id = refund_metadata_refund_id;
    }
    public String getRefund_metadata_business_app_id() {
        return refund_metadata_business_app_id;
    }
    public void setRefund_metadata_business_app_id(String refund_metadata_business_app_id) {
        this.refund_metadata_business_app_id = refund_metadata_business_app_id;
    }

}


