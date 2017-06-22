package org.mst.anaf.scraper;

import com.beust.jcommander.IStringConverter;
import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.ParameterException;
import com.beust.jcommander.converters.EnumConverter;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlTable;
import com.gargoylesoftware.htmlunit.html.HtmlTableRow;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mst.anaf.scraper.ReportFilter.ReportType;
import static org.mst.anaf.scraper.ReportFilter.SectorBugetar;

/**
 * Created by MarianStrugaru on 6/19/2017.
 */
public class AnafReportsScrapper {

    private static Log log = LogFactory.getLog(AnafReportsScrapper.class);

    private static String ANAF_DOMAIN = "https://extranet.anaf.mfinante.gov.ro";
    private static String SEARCH_URL = "https://extranet.anaf.mfinante.gov.ro/anaf/extranet/EXECUTIEBUGETARA/Rapoarte_Forexe/!ut/p/a1/jZBbU4MwEIV_DY-SbbG19Q0qovXSC6DASycNC42DgQmhXn69oaid8VJlwkz25Dt7siEJiUgi6JbnVPFS0KKtk-HKG11Ab-zDFIazEdhzcC6Gt1d9GIEGYg3AL58NX_zjoPUvrIXbv-uBN3j3HwD-lf8b4PX_8t-T5CDSdtgBB0ackiQvyvXuuWJbrK1RThKJGUqUZiO1vFGqqk8NMACflaQClUkFzczHjAsqFJp5uTVlaUCr7iED3MidhMGl64SeG9hL2wAusnLVKF7gT2mbslYkOhhCYv1kJ_uBZ8P5mR54HvassQ3gWeR-y_GJ-O3gQk6kIjG0e576jayp9usiReFWM1Z1FU_PkBXdnjX8U1cvFTqNblAjlWzTobb4bDhtUlQdWtEcSdzr9IBXS1qVbfR55By5-h_D7oyV6TmvGS0-gn1kymly3bLfCdeNoO8Bu0t2YI1MW3XCsWWxNakewzCCh0Gxvc5uAly_fl_2Gyx9j1o!/dl5/d5/L2dBISEvZ0FBIS9nQSEh/pw/Z7_G8H019S0JO6PD0APPU139A00G3/act/id=0/m=view/p=idSursa/p=idAn=0/p=cuiOcp/p=idDecl/p=frmPage=frmPage/p=nrCrt=1/p=codFiscal/p=search=Cauta/p=idSectBug=02/p=idLuna=0/p=idTipRaport=FXB-EXB-901/p=idJudet/p=denEp/p=page=5/p=denEpOcp/359130195711/=/#Z7_G8H019S0JO6PD0APPU139A00G3')";
    private static String SEARCH_URL_P1 = "https://extranet.anaf.mfinante.gov.ro/anaf/extranet/EXECUTIEBUGETARA/Rapoarte_Forexe/!ut/p/a1/jZBbU4MwEIV_DY-SbbG19Q0qovXSC6DASycNC42DgQmhXn69oaid8VJlwkz25Dt7siEJiUgi6JbnVPFS0KKtk-HKG11Ab-zDFIazEdhzcC6Gt1d9GIEGYg3AL58NX_zjoPUvrIXbv-uBN3j3HwD-lf8b4PX_8t-T5CDSdtgBB0ackiQvyvXuuWJbrK1RThKJGUqUZiO1vFGqqk8NMACflaQClUkFzczHjAsqFJp5uTVlaUCr7iED3MidhMGl64SeG9hL2wAusnLVKF7gT2mbslYkOhhCYv1kJ_uBZ8P5mR54HvassQ3gWeR-y_GJ-O3gQk6kIjG0e576jayp9usiReFWM1Z1FU_PkBXdnjX8U1cvFTqNblAjlWzTobb4bDhtUlQdWtEcSdzr9IBXS1qVbfR55By5-h_D7oyV6TmvGS0-gn1kymly3bLfCdeNoO8Bu0t2YI1MW3XCsWWxNakewzCCh0Gxvc5uAly_fl_2Gyx9j1o!/dl5/d5/L2dBISEvZ0FBIS9nQSEh/pw/Z7_G8H019S0JO6PD0APPU139A00G3";
    private static String SEARCH_URL_PARAMS_PATTERN = "/act/id=0/m=view/p=idSursa/p=idAn=%d/p=cuiOcp/p=idDecl/p=frmPage=frmPage/p=nrCrt=%s/p=codFiscal/p=search=Cauta/p=idSectBug=%s/p=idLuna=0/p=idTipRaport=%s/p=idJudet/p=denEp/p=page=%s";
    private static String SEARCH_URL_P2 = "/p=denEpOcp/359130195711/=/#Z7_G8H019S0JO6PD0APPU139A00G3')";

    public static String ANAF_REPORTS_DATA = "../anaf_reports/";

    @Parameter(names = "-reportType")
    private List<ReportType> reportTypes = Arrays.asList(ReportType.values());

    @Parameter(names = "-sector")
    private List<SectorBugetar> sectors = Arrays.asList(SectorBugetar.values());

    @Parameter(names = "-year", description = "An (2016, 2017)")
    private int year = 0;

    @Parameter(names = "-pageOffset", description = "Page offset to start extraction")
    private int pageOffset = 0;

    @Parameter(names = "-outPath", description = "Output folder")
    private String outPath = ANAF_REPORTS_DATA;

    public static void main(String[] args) throws IOException {

        AnafReportsScrapper main = new AnafReportsScrapper();
        JCommander.newBuilder()
                .addObject(main)
                .build()
                .parse(args);

        main.extractAnafReports();
    }

    public void extractAnafReports() throws IOException {
        Path dirPath = Paths.get(outPath);
        extractAnafReports(reportTypes, sectors, year, dirPath, pageOffset);
    }

    public void extractAnafReports(List<ReportType> reportTypes, List<SectorBugetar> sectors, int year, Path dirPath) throws IOException {
        extractAnafReports(reportTypes, sectors, year, dirPath, 0);
    }

    public void extractAnafReports(List<ReportType> reportTypes, List<SectorBugetar> sectors, int year, Path dirPath, int pageOffset) throws IOException {
        for (ReportType rType : reportTypes) {
            for (SectorBugetar sector : sectors) {
                log.info(String.format("START Extracting ANAF reports with reportType=%s(%s) and sector=%s(%s), year=%d", rType, rType.getCode(), sector, sector.getCode(), year));
                try {
                    extractAnafReports(rType, sector, year, dirPath, pageOffset);
                } catch (Exception e) {
                    log.error(String.format("ERROR while extracting ANAF reports with reportType=%s and sector=%s", rType, sector), e);
                }
                log.info(String.format("END   Extracting ANAF reports with reportType=%s(%s) and sector=%s(%s), year=%d", rType, rType.getCode(), sector, sector.getCode(), year));
            }
        }
    }

    public void extractAnafReports(ReportType reportType, SectorBugetar sector, int year, Path dirPath, int pageOffset) throws IOException {
        boolean hasMoreRows = true;

        WebClient webClient = getWebClient(SEARCH_URL);
        int nrRowsPerpage = 25;
        int index = pageOffset * nrRowsPerpage;
        int crtPage = pageOffset + 1;

        List<ReportEntry> resultList = new ArrayList<ReportEntry>();

        boolean forceExit = false;
        try {
            while (hasMoreRows && !forceExit && crtPage < 100000) {
                String params = String.format(SEARCH_URL_PARAMS_PATTERN, year, index, sector.getCode(), reportType.getCode(), crtPage);

                String url = SEARCH_URL_P1 + params + SEARCH_URL_P2;
                HtmlPage page = webClient.getPage(url);

                List<ReportEntry> partList = extractDataFromResultTable(page);
                if (partList.isEmpty()) {
                    hasMoreRows = false;
                }

                FileUtils.saveResultFiles(reportType.getCode(), sector.getCode(), year, partList, dirPath);

                resultList.addAll(partList);
                log.info(String.format("Extracted report=%s(%s), sector=%s(%s), year=%d, page: %5s, index: %6s, url: %s", reportType, reportType.getCode(), sector, sector.getCode(), year, crtPage, index, url));

                if (crtPage % 300 == 0) {
                    FileUtils.saveResultInCSVFile(reportType.getCode(), sector.getCode(), year, resultList, dirPath);
                    log.info(String.format("Extracted partially report data: report=%s(%s), sector=%s(%s), year=%d: %d reports in total", reportType, reportType.getCode(), sector, sector.getCode(), year, resultList.size()));
                }

                index += nrRowsPerpage;
                crtPage++;
            }
        } finally {
            FileUtils.saveResultInCSVFile(reportType.getCode(), sector.getCode(), year, resultList, dirPath);
            log.info(String.format("Extracted report=%s(%s), sector=%s(%s), year=%d: %d reports in total", reportType, reportType.getCode(), sector, sector.getCode(), year, resultList.size()));
        }
    }


    private List<ReportEntry> extractDataFromResultTable(HtmlPage page) {
        // extract datafrom table
        List<ReportEntry> list = new ArrayList<ReportEntry>(25);
        HtmlTable table = (HtmlTable) page.getByXPath("//table[@class='raport']").get(0);
        boolean firstRow = true;
        for (HtmlTableRow row : table.getRows()) {
            if (firstRow) {
                firstRow = false;
                continue;
            }

            ReportEntry entry = new ReportEntry();

            entry.setIndex(row.getCells().get(0).asText());
            entry.setReportType(row.getCells().get(1).asText());
            entry.setJudet(row.getCells().get(2).asText());
            entry.setReportPeriod(row.getCells().get(3).asText());
            entry.setEntity(row.getCells().get(4).asText());
            entry.setReportSector(row.getCells().get(5).asText());
            entry.setOrdonator(row.getCells().get(6).asText());

            for (DomElement el : row.getCells().get(7).getChildElements()) {
                entry.addUrl(el.getAttribute("href"));
            }

            list.add(entry);
        }
        return list;
    }

    public WebClient getWebClient(String url) {

        WebClient webClient = new WebClient();
        webClient.getOptions().setCssEnabled(false);
        webClient.getOptions().setJavaScriptEnabled(false);
        webClient.getOptions().setUseInsecureSSL(true);

        webClient.getOptions().setThrowExceptionOnFailingStatusCode(true);
        webClient.getOptions().setThrowExceptionOnScriptError(true);
        // webClient.getOptions().getProxyConfig().set...();

//        webClient.setAjaxController(new NicelyResynchronizingAjaxController());

        return webClient;
    }
}
