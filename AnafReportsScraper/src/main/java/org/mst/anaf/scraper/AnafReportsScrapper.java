package org.mst.anaf.scraper;

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
import static org.mst.anaf.scraper.ReportFilter.ReportType.*;
import static org.mst.anaf.scraper.ReportFilter.SectorBugetar.*;

/**
 * Created by MarianStrugaru on 6/19/2017.
 *
 */
public class AnafReportsScrapper {

    private static Log log = LogFactory.getLog(AnafReportsScrapper.class);

    private static String ANAF_DOMAIN = "https://extranet.anaf.mfinante.gov.ro";
    private static String SEARCH_URL    = "https://extranet.anaf.mfinante.gov.ro/anaf/extranet/EXECUTIEBUGETARA/Rapoarte_Forexe/!ut/p/a1/jZBbU4MwEIV_DY-SbbG19Q0qovXSC6DASycNC42DgQmhXn69oaid8VJlwkz25Dt7siEJiUgi6JbnVPFS0KKtk-HKG11Ab-zDFIazEdhzcC6Gt1d9GIEGYg3AL58NX_zjoPUvrIXbv-uBN3j3HwD-lf8b4PX_8t-T5CDSdtgBB0ackiQvyvXuuWJbrK1RThKJGUqUZiO1vFGqqk8NMACflaQClUkFzczHjAsqFJp5uTVlaUCr7iED3MidhMGl64SeG9hL2wAusnLVKF7gT2mbslYkOhhCYv1kJ_uBZ8P5mR54HvassQ3gWeR-y_GJ-O3gQk6kIjG0e576jayp9usiReFWM1Z1FU_PkBXdnjX8U1cvFTqNblAjlWzTobb4bDhtUlQdWtEcSdzr9IBXS1qVbfR55By5-h_D7oyV6TmvGS0-gn1kymly3bLfCdeNoO8Bu0t2YI1MW3XCsWWxNakewzCCh0Gxvc5uAly_fl_2Gyx9j1o!/dl5/d5/L2dBISEvZ0FBIS9nQSEh/pw/Z7_G8H019S0JO6PD0APPU139A00G3/act/id=0/m=view/p=idSursa/p=idAn=0/p=cuiOcp/p=idDecl/p=frmPage=frmPage/p=nrCrt=1/p=codFiscal/p=search=Cauta/p=idSectBug=02/p=idLuna=0/p=idTipRaport=FXB-EXB-901/p=idJudet/p=denEp/p=page=5/p=denEpOcp/359130195711/=/#Z7_G8H019S0JO6PD0APPU139A00G3')";
    private static String SEARCH_URL_P1 = "https://extranet.anaf.mfinante.gov.ro/anaf/extranet/EXECUTIEBUGETARA/Rapoarte_Forexe/!ut/p/a1/jZBbU4MwEIV_DY-SbbG19Q0qovXSC6DASycNC42DgQmhXn69oaid8VJlwkz25Dt7siEJiUgi6JbnVPFS0KKtk-HKG11Ab-zDFIazEdhzcC6Gt1d9GIEGYg3AL58NX_zjoPUvrIXbv-uBN3j3HwD-lf8b4PX_8t-T5CDSdtgBB0ackiQvyvXuuWJbrK1RThKJGUqUZiO1vFGqqk8NMACflaQClUkFzczHjAsqFJp5uTVlaUCr7iED3MidhMGl64SeG9hL2wAusnLVKF7gT2mbslYkOhhCYv1kJ_uBZ8P5mR54HvassQ3gWeR-y_GJ-O3gQk6kIjG0e576jayp9usiReFWM1Z1FU_PkBXdnjX8U1cvFTqNblAjlWzTobb4bDhtUlQdWtEcSdzr9IBXS1qVbfR55By5-h_D7oyV6TmvGS0-gn1kymly3bLfCdeNoO8Bu0t2YI1MW3XCsWWxNakewzCCh0Gxvc5uAly_fl_2Gyx9j1o!/dl5/d5/L2dBISEvZ0FBIS9nQSEh/pw/Z7_G8H019S0JO6PD0APPU139A00G3";
    private static String SEARCH_URL_PARAMS_PATTERN = "/act/id=0/m=view/p=idSursa/p=idAn=%d/p=cuiOcp/p=idDecl/p=frmPage=frmPage/p=nrCrt=%s/p=codFiscal/p=search=Cauta/p=idSectBug=%s/p=idLuna=0/p=idTipRaport=%s/p=idJudet/p=denEp/p=page=%s";
    private static String SEARCH_URL_P2 = "/p=denEpOcp/359130195711/=/#Z7_G8H019S0JO6PD0APPU139A00G3')";

    public static String ANAF_REPORTS_DATA = "../anaf_reports/";

    public static void main(String[] args) throws IOException {

        Path dirPath = Paths.get(ANAF_REPORTS_DATA);

//        List<ReportType> reportTypes = Arrays.asList(ReportType.values());
//        List<SectorBugetar> sectors = Arrays.asList(SectorBugetar.values());

//        List<ReportType> reportTypes = Arrays.asList(ReportType.values());
//        List<SectorBugetar> sectors = Arrays.asList(BUGET_ASIG_SOC, BUGET_SOMAJ, BUGET_SANATATE);

        List<ReportType> reportTypes = Arrays.asList(TIP_BUGET_AGREGAT);
        List<SectorBugetar> sectors = Arrays.asList(BUGET_DE_STAT, BUGET_LOCAL);

        int year = 2017;

        extractAnafReports(reportTypes, sectors, year, dirPath);
    }

    public static void extractAnafReports(List<ReportType> reportTypes, List<SectorBugetar> sectors, int year, Path dirPath) throws IOException {
        for (ReportType rType : reportTypes) {
            for (SectorBugetar sector : sectors) {
                log.info(String.format("START Extracting ANAF reports with reportType=%s(%s) and sector=%s(%s), year=%d", rType, rType.getCode(), sector, sector.getCode(), year));
                try {
                    extractAnafReports(rType, sector, year, dirPath);
                } catch (Exception e) {
                    log.error(String.format("ERROR while extracting ANAF reports with reportType=%s and sector=%s", rType, sector), e);
                }
                log.info(String.format("END   Extracting ANAF reports with reportType=%s(%s) and sector=%s(%s), year=%d", rType, rType.getCode(), sector, sector.getCode(), year));
            }
        }
    }

    public static void extractAnafReports(ReportType reportType, SectorBugetar sector, int year, Path dirPath) throws IOException {
        boolean hasMoreRows = true;

        WebClient webClient = getWebClient(SEARCH_URL);
        int index = 0;
        int nrRowsPerpage = 25;
        int crtPage = 1;

        List<ReportEntry> resultList = new ArrayList<ReportEntry>();

        boolean forceExit = false;
        while (hasMoreRows && !forceExit && crtPage < 10000) {
            String params = String.format(SEARCH_URL_PARAMS_PATTERN, year, index, sector.getCode(),reportType.getCode(), crtPage);

            String url = SEARCH_URL_P1 + params + SEARCH_URL_P2;
            HtmlPage page = webClient.getPage(url);

            List<ReportEntry> partList = extractDataFromResultTable(page);
            if (partList.isEmpty()) {
                hasMoreRows = false;
            }

            FileUtils.saveResultFiles(reportType.getCode(), sector.getCode(), year, partList, dirPath);

            resultList.addAll(partList);
            log.info(String.format("Extracted report=%s(%s), sector=%s(%s), year=%d, page: %5s, index: %6s, url: %s", reportType, reportType.getCode(), sector, sector.getCode(), year, crtPage, index, url));

            index = index + nrRowsPerpage;
            crtPage++;
        }

        FileUtils.saveResultInCSVFile(reportType.getCode(), sector.getCode(), year, resultList, dirPath);
    }


        private static List<ReportEntry> extractDataFromResultTable(HtmlPage page) {
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

    public static WebClient getWebClient(String url) {

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
