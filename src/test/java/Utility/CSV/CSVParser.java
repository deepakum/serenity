package Utility.CSV;

import java.io.*;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

import Utility.Payment.TenderSource;
import Utility.others.DepositControlInfo;
import Utility.others.PropertiesUtil;
import Utility.SettlementFile.PaypalSettlement;
import com.opencsv.CSVWriter;
import com.opencsv.bean.*;
import io.cucumber.core.logging.Logger;
import io.cucumber.core.logging.LoggerFactory;
import tasks.MockSettlementFile;

import static Utility.SettlementFile.MockBankStatement.targetPath;
import static Utility.SettlementFile.MockXml.*;

public class CSVParser {

    Logger logger = LoggerFactory.getLogger(CSVParser.class);
    private static void writeToStripeCSV(List<StripeCsvBean> csvBeanReader, String csvFilePath) {

        FileWriter writer = null;
        CSVWriter csvWriter = null;
        try {
            writer = new FileWriter(new File(csvFilePath));
            csvWriter = new CSVWriter(writer);

            HeaderColumnNameMappingStrategy<StripeCsvBean> strategy = new HeaderColumnNameMappingStrategy<>();
            strategy.setType(StripeCsvBean.class);

            String headerLine = Arrays.stream(StripeCsvBean.class.getDeclaredFields())
                    .map(field -> field.getAnnotation(CsvBindByName.class))
                    .filter(Objects::nonNull)
                    .map(CsvBindByName::column)
                    .collect(Collectors.joining(","));

            try (StringReader reader = new StringReader(headerLine)) {
                CsvToBean<StripeCsvBean> csv = new CsvToBeanBuilder<StripeCsvBean>(reader)
                        .withType(StripeCsvBean.class)
                        .withMappingStrategy(strategy)
                        .build();
                for (StripeCsvBean csvRow : csv) {}
            }

            StatefulBeanToCsv<StripeCsvBean> beanWriter = new StatefulBeanToCsvBuilder<StripeCsvBean>(writer)
                    .withMappingStrategy(strategy)
                    .build();
            beanWriter.write(csvBeanReader);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                csvWriter.close();
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static void writeToPaypalCSV(List<PaypalCsvBean> csvBeanReader, String csvFilePath) {

        FileWriter writer = null;
        CSVWriter csvWriter = null;
        try {
            writer = new FileWriter(new File(csvFilePath));
            csvWriter = new CSVWriter(writer);

            HeaderColumnNameMappingStrategy<PaypalCsvBean> strategy = new HeaderColumnNameMappingStrategy<>();
            strategy.setType(PaypalCsvBean.class);

            String headerLine = Arrays.stream(PaypalCsvBean.class.getDeclaredFields())
                    .map(field -> field.getAnnotation(CsvBindByName.class))
                    .filter(Objects::nonNull)
                    .map(CsvBindByName::column)
                    .collect(Collectors.joining(","));

            try (StringReader reader = new StringReader(headerLine)) {
                CsvToBean<PaypalCsvBean> csv = new CsvToBeanBuilder<PaypalCsvBean>(reader)
                        .withType(PaypalCsvBean.class)
                        .withMappingStrategy(strategy)
                        .build();
                for (PaypalCsvBean csvRow : csv) {}
            }

            StatefulBeanToCsv<PaypalCsvBean> beanWriter = new StatefulBeanToCsvBuilder<PaypalCsvBean>(writer)
                    .withMappingStrategy(strategy)
                    .build();
            beanWriter.write(csvBeanReader);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                csvWriter.close();
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static void writeToBraintreeCSV(List<BrainTreeCsvBean> csvBeanReader, String csvFilePath) {
        FileWriter writer = null;
        CSVWriter csvWriter = null;
        try {
            writer = new FileWriter(new File(csvFilePath));
            csvWriter = new CSVWriter(writer);

            HeaderColumnNameMappingStrategy<BrainTreeCsvBean> strategy = new HeaderColumnNameMappingStrategy<>();
            strategy.setType(BrainTreeCsvBean.class);

            String headerLine = Arrays.stream(BrainTreeCsvBean.class.getDeclaredFields())
                    .map(field -> field.getAnnotation(CsvBindByName.class))
                    .filter(Objects::nonNull)
                    .map(CsvBindByName::column)
                    .collect(Collectors.joining(","));

            try (StringReader reader = new StringReader(headerLine)) {
                CsvToBean<BrainTreeCsvBean> csv = new CsvToBeanBuilder<BrainTreeCsvBean>(reader)
                        .withType(BrainTreeCsvBean.class)
                        .withMappingStrategy(strategy)
                        .build();
                for (BrainTreeCsvBean csvRow : csv) {}
            }

            StatefulBeanToCsv<BrainTreeCsvBean> beanWriter = new StatefulBeanToCsvBuilder<BrainTreeCsvBean>(writer)
                    .withMappingStrategy(strategy)
                    .build();
            beanWriter.write(csvBeanReader);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                csvWriter.close();
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static List<BrainTreeCsvBean> readBraintreeCSV(File csvPath) {

        List<BrainTreeCsvBean> csvBeanReader = null;

        try {
            csvBeanReader = new CsvToBeanBuilder<BrainTreeCsvBean>(new FileReader(csvPath))
                    .withType(BrainTreeCsvBean.class).withOrderedResults(true).build().parse();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return csvBeanReader;
    }

    public static List<StripeCsvBean> readStripeCSV(File csvPath) {

        List<StripeCsvBean> csvBeanReader = null;

        try {
            csvBeanReader = new CsvToBeanBuilder<StripeCsvBean>(new FileReader(csvPath))
                    .withType(StripeCsvBean.class).withOrderedResults(true).build().parse();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return csvBeanReader;
    }

    public static List<PaypalCsvBean> readPaypalCSV(File csvPath) {

        List<PaypalCsvBean> csvBeanReader = null;

        try {
            csvBeanReader = new CsvToBeanBuilder<PaypalCsvBean>(new FileReader(csvPath))
                    .withType(PaypalCsvBean.class).withOrderedResults(true).build().parse();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return csvBeanReader;
    }

    public static List<Object> readCSV(File csvPath) {

        List<Object> csvBeanReader = null;

        try {
            csvBeanReader = new CsvToBeanBuilder<>(new FileReader(csvPath))
                    .withOrderedResults(true).build().parse();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return csvBeanReader;
    }
    public static void updateStripeCSV(String created, String settlementDate, String paymentIntentID, String merchantTransactionReferenceID, String totalTenderAmount) {

        List<StripeCsvBean> csvBeanReader = readStripeCSV(getTemplateFile(".csv"));
        StripeCsvBean csvData =  csvBeanReader.get(csvBeanReader.size()-1);

        String dateString = created.replace("‑","-").replaceAll("[^A-Za-z0-9-:]"," ");
        csvData.setCreated(DateUtil.getCreatedDate(dateString));
        csvData.setCharge_created(DateUtil.getCreatedDate(dateString));
        csvData.setCreated_utc(DateUtil.getCreatedUTC(created));
        csvData.setCharge_created_utc(DateUtil.getCreatedUTC(created));
        csvData.setAvailable_on(DateUtil.getBusinessDateForCreditCardSettlement(settlementDate));
        csvData.setAvailable_on_utc(DateUtil.getBusinessDateForCreditCardSettlement(settlementDate));
        csvData.setAutomatic_payout_effective_at(DateUtil.getBusinessDateForCreditCardSettlement(settlementDate));
        csvData.setAutomatic_payout_effective_at_utc(DateUtil.getEffectiveAtUTC(settlementDate));
        csvData.setFee(getFeeValue(csvBeanReader));
        csvData.setGross(CSVUtil.getTenderAmount(totalTenderAmount));
        csvData.setCustomer_facing_amount(CSVUtil.getTenderAmount(totalTenderAmount));
        double netValue = Double.parseDouble(csvData.getGross()) - Double.parseDouble(csvData.getFee());
        csvData.setNet(String.valueOf(netValue));
        csvData.setPayment_method_type(CSV.paymentModeType);
        csvData.setCard_brand(CSV.cardBrand);
        csvData.setPayment_intent_id(paymentIntentID);
        csvData.setCard_funding(CSV.cardFunding);
        csvData.setCard_country(CSV.cardCountry);
        csvData.setPayment_metadata_order_id(merchantTransactionReferenceID);
        csvData.setPayment_metadata_business_app_id(CSV.businessIDPrefix+merchantTransactionReferenceID);
        csvData.setPayment_metadata_payment_mode(CSV.paymentMode);
        csvData.setBalance_transaction_id(CSVUtil.getBalanceTransactionID(paymentIntentID));
        csvData.setSource_id(CSVUtil.getSourceID(paymentIntentID));
        csvData.setAutomatic_payout_id(CSVUtil.getAutomaticPayoutID(paymentIntentID));
        csvData.setCharge_id(CSVUtil.getChargeID(paymentIntentID));

        CSVParser.writeToStripeCSV(csvBeanReader, getMockedCSVpath(settlementDate));
    }

    public static void updateMultiLineCSV(List<Map<String,String>> csvRows,Map<String, String> map) {

        List<StripeCsvBean> csvBeanList = new ArrayList<>();
        String settlementDate = map.get("SettlementDate");

        for(Map<String, String> csvMap : csvRows) {
//            List<StripeCsvBean> csvBeanReader1 = readStripeCSV(getTemplateFile(".csv"));
            List<StripeCsvBean> csvBeanReader = readStripeCSV(getTemplateFile("StripeSettlement"));
            StripeCsvBean csvData =  csvBeanReader.get(csvBeanReader.size()-1);
            String created = csvMap.get("CreatedDateAndTime");
            String totalTenderAmount = csvMap.get("Amount");
            String paymentIntentID = csvMap.get("PaymentIntentID");
            String merchantTransactionReferenceID = csvMap.get("MerchantID");
            String dateString = created.replace("‑", "-").replaceAll("[^A-Za-z0-9-:]", " ");
            csvData.setCreated(DateUtil.getCreatedDate(dateString));
            csvData.setCharge_created(DateUtil.getCreatedDate(dateString));
            csvData.setCreated_utc(DateUtil.getCreatedUTC(created));
            csvData.setCharge_created_utc(DateUtil.getCreatedUTC(created));
            csvData.setAvailable_on(DateUtil.getBusinessDateForCreditCardSettlement(settlementDate));
            csvData.setAvailable_on_utc(DateUtil.getBusinessDateForCreditCardSettlement(settlementDate));
            csvData.setAutomatic_payout_effective_at(DateUtil.getBusinessDateForCreditCardSettlement(settlementDate));
            csvData.setAutomatic_payout_effective_at_utc(DateUtil.getEffectiveAtUTC(settlementDate));
            csvData.setFee(getFeeValue(csvBeanReader));
            csvData.setGross(CSVUtil.getTenderAmount(totalTenderAmount));
            csvData.setCustomer_facing_amount(CSVUtil.getTenderAmount(totalTenderAmount));
            double netValue = Double.parseDouble(csvData.getGross()) - Double.parseDouble(csvData.getFee());
            csvData.setNet(String.valueOf(netValue));
            csvData.setPayment_method_type(CSV.paymentModeType);
            csvData.setCard_brand(CSV.cardBrand);
            csvData.setPayment_intent_id(paymentIntentID);
            csvData.setCard_funding(CSV.cardFunding);
            csvData.setCard_country(CSV.cardCountry);
            csvData.setPayment_metadata_order_id(merchantTransactionReferenceID);
            csvData.setPayment_metadata_business_app_id(CSV.businessIDPrefix + merchantTransactionReferenceID);
            csvData.setPayment_metadata_payment_mode(CSV.paymentMode);
            csvData.setBalance_transaction_id(CSVUtil.getBalanceTransactionID(paymentIntentID));
            csvData.setSource_id(CSVUtil.getSourceID(paymentIntentID));
            csvData.setAutomatic_payout_id(CSVUtil.getAutomaticPayoutID(paymentIntentID));
            csvData.setCharge_id(CSVUtil.getChargeID(paymentIntentID));
            csvBeanList.add(csvData);
        }
        if(MockSettlementFile.creditCardBeanList.size()>0){
            for(StripeCsvBean stripe : MockSettlementFile.creditCardBeanList){
                csvBeanList.add(stripe);
            }
        }

        CSVParser.writeToStripeCSV(csvBeanList, getMockedCSVpath(settlementDate));
    }

    public static List<StripeCsvBean> stripeRefundSettlementCSV(List<Map<String,String>> csvRows, Map<String, String> map) {

        List<StripeCsvBean> csvBeanList = new ArrayList<>();
        String settlementDate = map.get("SettlementDate");

        for(Map<String, String> csvMap : csvRows) {
//            List<StripeCsvBean> csvBeanReader1 = readStripeCSV(getTemplateFile(".csv"));
            List<StripeCsvBean> csvBeanReader = readStripeCSV(getTemplateFile("StripeRefundSettlement"));
            StripeCsvBean csvData =  csvBeanReader.get(csvBeanReader.size()-1);
            String created = csvMap.get("CreatedDateAndTime");
            String totalTenderAmount = "-".concat(csvMap.get("Amount"));
            String paymentIntentID = csvMap.get("PaymentIntentID");
            String merchantTransactionReferenceID = csvMap.get("MerchantID");
            String dateString = created.replace("‑", "-").replaceAll("[^A-Za-z0-9-:]", " ");
            csvData.setCreated(DateUtil.getCreatedDate(dateString));
            csvData.setCharge_created(DateUtil.getCreatedDate(dateString));
            csvData.setCreated_utc(DateUtil.getCreatedUTC(created));
            csvData.setCharge_created_utc(DateUtil.getCreatedUTC(created));
            csvData.setAvailable_on(DateUtil.getBusinessDateForCreditCardSettlement(settlementDate));
            csvData.setAvailable_on_utc(DateUtil.getBusinessDateForCreditCardSettlement(settlementDate));
            csvData.setAutomatic_payout_effective_at(DateUtil.getBusinessDateForCreditCardSettlement(settlementDate));
            csvData.setAutomatic_payout_effective_at_utc(DateUtil.getEffectiveAtUTC(settlementDate));
            csvData.setFee(getFeeValue(csvBeanReader));
            csvData.setGross(CSVUtil.getTenderAmount(totalTenderAmount));
            csvData.setCustomer_facing_amount(CSVUtil.getTenderAmount(totalTenderAmount));
            double netValue = Double.parseDouble(csvData.getGross()) - Double.parseDouble(csvData.getFee());
            csvData.setNet(String.valueOf(netValue));
            csvData.setPayment_method_type(CSV.paymentModeType);
            csvData.setCard_brand(CSV.cardBrand);
            csvData.setPayment_intent_id(paymentIntentID);
            csvData.setCard_funding(CSV.cardFunding);
            csvData.setCard_country(CSV.cardCountry);
            csvData.setPayment_metadata_order_id(merchantTransactionReferenceID);
            csvData.setPayment_metadata_business_app_id(CSV.businessIDPrefix + merchantTransactionReferenceID);
            csvData.setPayment_metadata_payment_mode(CSV.paymentMode);
            csvData.setBalance_transaction_id(CSVUtil.getBalanceTransactionID(paymentIntentID));
            csvData.setSource_id(CSVUtil.getRefundSourceID(paymentIntentID));
            csvData.setAutomatic_payout_id(CSVUtil.getAutomaticPayoutID(paymentIntentID));
            csvData.setCharge_id(CSVUtil.getChargeID(paymentIntentID));
            csvData.setRefund_metadata_refund_id(PropertiesUtil.getProperties("refund.id"));
            csvBeanList.add(csvData);
        }
        return csvBeanList;
    }

    public static void updateBrainTreeCSV(List<Map<String,String>> csvRows,Map<String, String> depositControlMap) {

        List<BrainTreeCsvBean> csvBeanList = new ArrayList<>();
        String settlementDate = depositControlMap.get("SettlementDate");

        for(Map<String, String> csvMap : csvRows) {
            List<BrainTreeCsvBean> csvBeanReader1 = readBraintreeCSV(getTemplateFile("BraintreeSettlement"));
            BrainTreeCsvBean csvData =  csvBeanReader1.get(csvBeanReader1.size()-1);
            csvData.setTransactionId(csvMap.get("PaymentIntentID"));
            csvData.setOrderId(csvMap.get("MerchantID"));
            csvData.setTransactionAmount(CSVUtil.getTenderAmount(csvMap.get("Amount")));
            csvData.setTransactionDateTime(DateUtil.getCreatedDate(csvMap.get("CreatedDateAndTime")));
            String[] str = csvData.getSettlementBatchId().split("_");
            str[0] = DateUtil.getCustomDate(DepositControlInfo.SETTLEMENT_DATE_FORMAT,CSV.BT_DATE_FORMAT,settlementDate);
            csvData.setSettlementBatchId(Arrays.stream(str).collect(Collectors.joining("_")));
            csvBeanList.add(csvData);
        }

        writeToBraintreeCSV(csvBeanList, getMockedCSVpath(settlementDate));
    }

    public static void updatePaypalCSV(List<Map<String,String>> csvRows,Map<String, String> depositControlMap) {

        List<PaypalCsvBean> csvBeanList = new ArrayList<>();
        String settlementDate = depositControlMap.get("SettlementDate");

        for(Map<String, String> csvMap : csvRows) {
            List<PaypalCsvBean> csvBeanReader = readPaypalCSV(getPaypalTemplateFile("PayPalBody"));
            PaypalCsvBean csvData =  csvBeanReader.get(csvBeanReader.size()-1);
            csvData.setCh("SB");
            csvData.setTransactionId(csvMap.get("PaymentIntentID"));
            csvData.setInvoiceID(csvMap.get("MerchantID"));
            int amount = (int) Float.parseFloat(CSVUtil.getTenderAmount(csvMap.get("Amount")))*100;
            csvData.setGrossTransactionAmount(String.valueOf(amount).replace("\"", ""));
            csvData.setTransactionInitiationDate(DateUtil.getCreatedDate(csvMap.get("CreatedDateAndTime")).concat(" +0800").replace("\"", ""));
            csvData.setTransactionCompletionDate(DateUtil.getCreatedDate(csvMap.get("CreatedDateAndTime")).concat(" +0800").replace("\"", ""));
            csvData.setConsumerID(csvMap.get("MerchantID"));

            csvBeanList.add(csvData);
        }

        writeToPaypalCSV(csvBeanList, getMockedCSVpath(settlementDate));
        PaypalSettlement.savePaypalMockedCsvFile();
    }

    private static String getMockedCsvFileName(String settlementDate) {

        SimpleDateFormat sdf = new SimpleDateFormat(DepositControlInfo.SETTLEMENT_DATE_FORMAT, Locale.ENGLISH);
        String fileDate;
        String csvFileName = null;
        try {
            Date date = sdf.parse(settlementDate);
            if(PropertiesUtil.getProperties("tender.source").equalsIgnoreCase(TenderSource.STRIPE)){
                sdf.applyPattern(CSV.ST_SETTLEMENT_FILE_DATE_FORMAT);
                csvFileName = CSV.ST_SETTLEMENT_FILE_NAME;
            }else if(PropertiesUtil.getProperties("tender.source").equalsIgnoreCase(TenderSource.BRAINTREE)){
                sdf.applyPattern(CSV.BT_SETTLEMENT_FILE_DATE_FORMAT);
                csvFileName = CSV.BT_SETTLEMENT_FILE_NAME;
            }else if(PropertiesUtil.getProperties("tender.source").equalsIgnoreCase(TenderSource.PAYPAL)){
                sdf.applyPattern(CSV.BT_SETTLEMENT_FILE_DATE_FORMAT);
                csvFileName = CSV.PAYPAL_SETTLEMENT_FILE_NAME;
            }

            fileDate = sdf.format(date);
            if(PropertiesUtil.getProperties("tender.source").equalsIgnoreCase(TenderSource.PAYPAL))
                fileDate += "".concat(".01.009");

        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        return csvFileName + fileDate + ".csv";
    }

    private static String getMockedCSVpath(String settlementDate){
        return targetPath + File.separator + getMockedCsvFileName(settlementDate);
    }

    private static String getFeeValue(List<StripeCsvBean> csvBeanReader) {
        double grossAmount = Double.parseDouble(csvBeanReader.get(0).getGross());
        double netValue = grossAmount * (CSV.fee/100);
        NumberFormat formatter = new DecimalFormat("##.00");
        return formatter.format(netValue);
    }
}
