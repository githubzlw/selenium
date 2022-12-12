//package com.zhz.selenium.controller;
//
//
//import cn.hutool.core.date.DateUtil;
//import com.amazon.SellingPartnerAPIAA.AWSAuthenticationCredentials;
//import com.amazon.SellingPartnerAPIAA.AWSAuthenticationCredentialsProvider;
//import com.amazon.SellingPartnerAPIAA.LWAAuthorizationCredentials;
//import com.zhz.selenium.service.AmazonSpapiService;
//import com.zhz.selenium.util.StringUtil;
//import io.swagger.client.api.ReportsApi;
//import io.swagger.client.model.CreateReportSpecification;
//import io.swagger.client.model.ReportOptions;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.scheduling.annotation.EnableScheduling;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.stereotype.Component;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//import org.threeten.bp.LocalDateTime;
//import org.threeten.bp.OffsetDateTime;
//import org.threeten.bp.ZoneOffset;
//
//import java.text.SimpleDateFormat;
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.List;
//import java.util.UUID;
//
//@RestController
//@Component
//@EnableScheduling
//public class AmazonSpapiController {
//    @Value("${USER_ACCESS_KEY_ID}")
//    private String accessKeyId;
//
//    @Value("${USER_SECRET_ACCESS_KEY}")
//    private String secretAccessKey;
//
//    @Value("${ROLE_ARN}")
//    private String roleArn;
//
//    @Value("${APP_CLIENT_ID}")
//    private String appClientId;
//
//    @Value("${APP_CLIENT_SECRET}")
//    private String appClientSecret;
//
//    @Value("${REFRESH_TOKEN}")
//    private String refreshToken;
//
//    @Autowired
//    private AmazonSpapiService amazonSpapiService;
//
//
//    @RequestMapping(value = "/importSpapi")
//    @Scheduled(cron="0 0 20 * * ?")
//    public void importSpapi() throws InterruptedException {
//        ReportsApi reportsApi = api();
//        String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
//        for (int i = 1; i < 4; i++) {
//            String time = new SimpleDateFormat("yyyy-MM-dd").format(DateUtil.offsetDay(DateUtil.parse(date), -i));
//            CreateReportSpecification body=new CreateReportSpecification();
//            body.setReportType("GET_SALES_AND_TRAFFIC_REPORT");
//            List<String> ls =new ArrayList<>() ;
//            ls.add("ATVPDKIKX0DER");
//            body.setMarketplaceIds(ls);
//            ReportOptions rps=new ReportOptions();
//            rps.putIfAbsent("reportPeriod","DAY");
//            rps.putIfAbsent("asinGranularity","CHILD");
//            body.setReportOptions(rps);
//            LocalDateTime localStart=LocalDateTime.of(Integer.parseInt(time.substring(0,4))
//                    ,Integer.parseInt(time.substring(5,7)),Integer.parseInt(time.substring(8,10)),0,0);
//            LocalDateTime localEmd=LocalDateTime.of(Integer.parseInt(time.substring(0,4))
//                    ,Integer.parseInt(time.substring(5,7)),Integer.parseInt(time.substring(8,10)),0,0);
//            OffsetDateTime offsetDateTime1 = OffsetDateTime.of(localStart, ZoneOffset.ofHours(-4));
//            OffsetDateTime offsetDateTime2 = OffsetDateTime.of(localEmd, ZoneOffset.ofHours(-4));
//            body.setDataStartTime(offsetDateTime1);
//            body.setDataEndTime(offsetDateTime2);
//            String url = url(reportsApi,body);
//            if (!StringUtil.isEmpty(url)){
//                amazonSpapiService.getSpapiReport(reportsApi, url, time);
//            }
//        }
//    }
//
//
//    @RequestMapping(value = "/importSpapiPrice")
//    @Scheduled(cron="0 0,30 20 * * ?")
//    public void importSpapiPrice() throws InterruptedException {
//        ReportsApi reportsApi = api();
//        CreateReportSpecification body=new CreateReportSpecification();
//        body.setReportType("GET_FLAT_FILE_OPEN_LISTINGS_DATA");
//        List<String> ls =new ArrayList<>() ;
//        ls.add("ATVPDKIKX0DER");
//        body.setMarketplaceIds(ls);
//        String url = url(reportsApi,body);
//        if (!StringUtil.isEmpty(url)){
//            amazonSpapiService.getSpapiPriceReport(reportsApi, url);
//        }
//    }
//
//
//    public String getRandomNonce() {
//        return UUID.randomUUID().toString().replace("-", "");
//    }
//
//    public ReportsApi api(){
//        AWSAuthenticationCredentials awsAuthenticationCredentials = AWSAuthenticationCredentials.builder()
//                .accessKeyId(accessKeyId)
//                .secretKey(secretAccessKey)
//                .region("us-east-1")
//                .build();
//
//        AWSAuthenticationCredentialsProvider awsAuthenticationCredentialsProvider = AWSAuthenticationCredentialsProvider.builder()
//                .roleArn(roleArn)
//                .roleSessionName(getRandomNonce())
//                .build();
//
//        LWAAuthorizationCredentials lwaAuthorizationCredentials = LWAAuthorizationCredentials.builder()
//                .clientId(appClientId)
//                .clientSecret(appClientSecret)
//                .refreshToken(refreshToken)
//                .endpoint("https://api.amazon.com/auth/o2/token")
//                .build();
//
//        ReportsApi reportsApi = new ReportsApi.Builder().awsAuthenticationCredentials(awsAuthenticationCredentials)
//                .awsAuthenticationCredentialsProvider(awsAuthenticationCredentialsProvider)
//                .lwaAuthorizationCredentials(lwaAuthorizationCredentials)
//                .endpoint("https://sellingpartnerapi-na.amazon.com")//请求地区
//                .build();
//        return reportsApi;
//    }
//
//    public String url(ReportsApi reportsApi,CreateReportSpecification body) throws InterruptedException {
//        String reportId = amazonSpapiService.getSpapiReportId(reportsApi, body);
//        if (!StringUtil.isEmpty(reportId)){
//            String spapiReportDocumentId = amazonSpapiService.getSpapiReportDocumentId(reportsApi, reportId);
//            if (!StringUtil.isEmpty(spapiReportDocumentId)){
//                String url = amazonSpapiService.getSpapiReportUrl(reportsApi, spapiReportDocumentId);
//                return url;
//            }
//        }
//        return null;
//    }
//
//}
