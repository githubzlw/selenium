/*
 * Selling Partner API for Sellers
 * The Selling Partner API for Sellers lets you retrieve information on behalf of sellers about their seller account, such as the marketplaces they participate in. Along with listing the marketplaces that a seller can sell in, the API also provides additional information about the marketplace such as the default language and the default currency. The API also provides seller-specific information such as whether the seller has suspended listings in that marketplace.
 *
 * OpenAPI spec version: v1
 *
 *
 * NOTE: This class is auto generated by the swagger code generator program.
 * https://github.com/swagger-api/swagger-codegen.git
 * Do not edit the class manually.
 */


package com.zhz.selenium.test;

import cn.hutool.core.date.DateUtil;
import com.amazon.SellingPartnerAPIAA.AWSAuthenticationCredentials;
import com.amazon.SellingPartnerAPIAA.AWSAuthenticationCredentialsProvider;
import com.amazon.SellingPartnerAPIAA.LWAAuthorizationCredentials;
import io.swagger.client.ApiException;
import io.swagger.client.api.ReportsApi;
import io.swagger.client.model.CreateReportResponse;
import io.swagger.client.model.CreateReportSpecification;
import io.swagger.client.model.ReportOptions;
import jdk.nashorn.internal.ir.annotations.Ignore;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import org.threeten.bp.*;

/**
 * API tests for SellersApi
 */

@Ignore
public class SellersApiTest {

//    private final SellersApi api = new SellersApi();

    private static final String USER_ACCESS_KEY_ID = "AKIAZEBPOY3NSMRZEX23";
    private static final String USER_SECRET_ACCESS_KEY = "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";
    private static final String ROLE_ARN = "arn:aws:iam::627164759771:role/SellingPartner";
    private static final String APP_CLIENT_ID = "amzn1.application-oa2-client.784d99cb71d54d67bec7bcf3a940e02f";
    private static final String APP_CLIENT_SECRET = "aa7e97cc6113f3d33c730ce52bd9e5e37d4a3f5378f2791087a7d3c70c3cb5db";
    private static final String REFRESH_TOKEN = "Atzr|IwEBIKwY8fKEosCsC9u64lLor_iqGOgX2oUjsiMsQEsv68GbR-Xis8GadPdpGDa5MN1liqo0EFG5whyxI1Xbl9cBF69oDruhB8gmeqUUTf0I11f8cQus9eeH0GTXEtIbMmmiZhMA8fv-yeQEULM8u-zjz0tlhDcN-ViNw-ql0QTztGdq8a6naPmMjf4OrlSA8-apC2mX_oXG9kjvg4OvDHh3BK7VMAkNzF9pOaRtfcGRuGNpf7sd_halfCo8-QaQNb_-9gHYtwNBnmDTAydts_MPgSQmpMw912QgqZtrNip5plFmOzOI-tnJJ5qHuXzWHz5ah1E";

    public static void main(String[] args) throws ApiException {
//        GetMarketplaceParticipationsResponse response = api.getMarketplaceParticipations();

        // TODO: test validations

        /*
         * user credentials
         * */
        AWSAuthenticationCredentials awsAuthenticationCredentials = AWSAuthenticationCredentials.builder()
                .accessKeyId(USER_ACCESS_KEY_ID)
                .secretKey(USER_SECRET_ACCESS_KEY)
                .region("us-east-1")
                .build();

        /*
         * user IAM ARN
         * */
        AWSAuthenticationCredentialsProvider awsAuthenticationCredentialsProvider = AWSAuthenticationCredentialsProvider.builder()
                .roleArn(ROLE_ARN)
                .roleSessionName(getRandomNonce())
                .build();

        /*
         * application
         * */
        LWAAuthorizationCredentials lwaAuthorizationCredentials = LWAAuthorizationCredentials.builder()
                .clientId(APP_CLIENT_ID)
                .clientSecret(APP_CLIENT_SECRET)
                .refreshToken(REFRESH_TOKEN)
                .endpoint("https://api.amazon.com/auth/o2/token")
                .build();


        ReportsApi reportsApi = new ReportsApi.Builder().awsAuthenticationCredentials(awsAuthenticationCredentials)
                .awsAuthenticationCredentialsProvider(awsAuthenticationCredentialsProvider)
                .lwaAuthorizationCredentials(lwaAuthorizationCredentials)
                .endpoint("https://sellingpartnerapi-na.amazon.com")//请求地区
                .build();


        DateTimeFormatter df =  DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
        DateTime dtstart= DateTime.parse("2022-09-01 11:13:00", df);
        DateTime dtend= DateTime.parse("2022-09-10 11:13:00", df);


        CreateReportSpecification body=new CreateReportSpecification();

        LocalDateTime localStart=LocalDateTime.of(2022,9,18,0,0);
        LocalDateTime localEmd=LocalDateTime.of(2022,9,24,10,20);
        OffsetDateTime offsetDateTime1 = OffsetDateTime.of(localStart, ZoneOffset.ofHours(-4));
        OffsetDateTime offsetDateTime2 = OffsetDateTime.of(localEmd, ZoneOffset.ofHours(-4));
//         body.setReportType("GET_MERCHANT_LISTINGS_ALL_DATA");
        body.setReportType("GET_SALES_AND_TRAFFIC_REPORT");
        // and GET_VENDOR_SALES_TRAFFIC_REPORT
//        body.setDataStartTime(offsetDateTime1);
//        body.setDataEndTime(offsetDateTime2);
        ReportOptions rps=new ReportOptions();
//        rps.putIfAbsent("reportPeriod","WEEK");
//        rps.putIfAbsent("distributorView","SOURCING");
//        rps.putIfAbsent("sellingProgram","RETAIL");
        rps.putIfAbsent("reportPeriod","DAY");
        rps.putIfAbsent("asinGranularity","CHILD");
        rps.putIfAbsent("dataStartTime",new SimpleDateFormat("yyyy-MM-dd").format(DateUtil.offsetDay(DateUtil.parse(new SimpleDateFormat("yyyy-MM-dd").format(new Date())), -10))+"T20:11:24.000Z");
        rps.putIfAbsent("dataEndTime",new SimpleDateFormat("yyyy-MM-dd").format(DateUtil.offsetDay(DateUtil.parse(new SimpleDateFormat("yyyy-MM-dd").format(new Date())), -3))+"T20:11:24.000Z");

        body.setReportOptions(rps);
        List<String> ls =new ArrayList<>() ;
        ls.add("ATVPDKIKX0DER");

        body.setMarketplaceIds(ls);
        //       = "{\"reportType\": \"GET_MERCHANT_LISTINGS_ALL_DATA\",\"dataStartTime\": \"2022-8-10T20:11:24.000Z\",\"dataEndTime\":\"2022-8-30T20:11:24.000Z\",\"marketplaceIds\": [\"A2ZV50J4W1RKNI\"] }";
        CreateReportResponse response = reportsApi.createReport(body);

        // TODO: test validations



        System.out.println(response);



     /*  SellersApi sellersApi = new SellersApi.Builder()
                .awsAuthenticationCredentials(awsAuthenticationCredentials)
                .lwaAuthorizationCredentials(lwaAuthorizationCredentials)
                .awsAuthenticationCredentialsProvider(awsAuthenticationCredentialsProvider)
                .endpoint("https://sellingpartnerapi-na.amazon.com")
                .build();

        System.out.println(sellersApi.getMarketplaceParticipations());*/
    }


    public static String getRandomNonce() {
        return UUID.randomUUID().toString().replace("-", "");
    }

}
