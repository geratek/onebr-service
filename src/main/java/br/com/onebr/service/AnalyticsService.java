package br.com.onebr.service;

import br.com.onebr.controller.AnalyticsRes;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.analytics.AnalyticsScopes;
import com.google.api.services.analyticsreporting.v4.AnalyticsReporting;
import com.google.api.services.analyticsreporting.v4.model.DateRange;
import com.google.api.services.analyticsreporting.v4.model.Dimension;
import com.google.api.services.analyticsreporting.v4.model.DimensionFilter;
import com.google.api.services.analyticsreporting.v4.model.DimensionFilterClause;
import com.google.api.services.analyticsreporting.v4.model.GetReportsRequest;
import com.google.api.services.analyticsreporting.v4.model.GetReportsResponse;
import com.google.api.services.analyticsreporting.v4.model.Metric;
import com.google.api.services.analyticsreporting.v4.model.ReportRequest;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.annotation.PostConstruct;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AnalyticsService {

    @Value("${google.analytics.json.file.path}")
    private String analyticsFilePath;

    private String APP_NAME = "onebr-prod";

    private String VIEW_ID = "222212743";

    private String START_DATE = "2019-01-01";

    private String END_DATE = "today";

    private String METRIC_PAGE_VIEWS = "ga:pageviews";

    private String DIMENSION_PAGE_PATH = "ga:pagePath";

    private AnalyticsReporting analyticsReporting;

    @PostConstruct
    public void initAnalytics() throws GeneralSecurityException, IOException {
        final HttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
        final GoogleCredential credential = GoogleCredential
            .fromStream(new FileInputStream(analyticsFilePath))
            .createScoped(AnalyticsScopes.all());

        analyticsReporting = new AnalyticsReporting.Builder(httpTransport, GsonFactory.getDefaultInstance(), credential)
            .setApplicationName(APP_NAME).build();
    }

    @SneakyThrows
    public AnalyticsRes getUsersTotalCount(String path) {
        final DateRange dateRange = new DateRange();
        dateRange.setStartDate(START_DATE);
        dateRange.setEndDate(END_DATE);

        final Metric pageViews = new Metric();
        pageViews.setExpression(METRIC_PAGE_VIEWS);

        final Dimension pagePath = new Dimension().setName(DIMENSION_PAGE_PATH);
        final DimensionFilter dimensionFilter = new DimensionFilter();
        dimensionFilter.setDimensionName(DIMENSION_PAGE_PATH);
        dimensionFilter.setOperator("EXACT");
        dimensionFilter.setExpressions(Arrays.asList(path));
        final DimensionFilterClause dimensionFilterClause = new DimensionFilterClause();
        dimensionFilterClause.setFilters(Arrays.asList(dimensionFilter));

        ReportRequest request = new ReportRequest()
            .setViewId(VIEW_ID)
            .setDateRanges(Arrays.asList(dateRange))
            .setMetrics(Arrays.asList(pageViews))
            .setDimensions(Arrays.asList(pagePath))
            .setDimensionFilterClauses(Arrays.asList(dimensionFilterClause));

        List<ReportRequest> requests = new ArrayList<>();
        requests.add(request);

        // Create the GetReportsRequest object.
        GetReportsRequest getReport = new GetReportsRequest()
            .setReportRequests(requests);

        // Call the batchGet method.
        GetReportsResponse response = analyticsReporting.reports().batchGet(getReport).execute();

        return AnalyticsRes.builder()
            .count(Integer.valueOf(response
                .getReports().get(0)
                .getData()
                .getRows().get(0)
                .getMetrics().get(0)
                .getValues().get(0)))
            .build();
//
//        for (Report report : response.getReports()) {
//            ColumnHeader header = report.getColumnHeader();
//            List<String> dimensionHeaders = header.getDimensions();
//            List<MetricHeaderEntry> metricHeaders = header.getMetricHeader().getMetricHeaderEntries();
//            List<ReportRow> rows = report.getData().getRows();
//
//            if (rows == null) {
//                System.out.println("No data found for " + VIEW_ID);
//                return 0;
//            }
//
//            for (ReportRow row : rows) {
//                List<String> dimensions = row.getDimensions();
//                List<DateRangeValues> metrics = row.getMetrics();
//
//                for (int i = 0; i < dimensionHeaders.size() && i < dimensions.size(); i++) {
//                    System.out.println(dimensionHeaders.get(i) + ": " + dimensions.get(i));
//                }
//
//                for (int j = 0; j < metrics.size(); j++) {
//                    System.out.print("Date Range (" + j + "): ");
//                    DateRangeValues values = metrics.get(j);
//                    for (int k = 0; k < values.getValues().size() && k < metricHeaders.size(); k++) {
//                        System.out.println(metricHeaders.get(k).getName() + ": " + values.getValues().get(k));
//                    }
//                }
//            }
//        }
    }
}
