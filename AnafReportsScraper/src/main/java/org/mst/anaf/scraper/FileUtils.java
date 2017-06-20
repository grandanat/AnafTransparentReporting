package org.mst.anaf.scraper;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by MarianStrugaru on 6/18/2017.
 */
public class FileUtils {

    private static Log log = LogFactory.getLog(FileUtils.class);

    private static SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");

    public static void saveResultInCSVFile(String reportType, String sector, int year, List<ReportEntry> resultList, Path dirPath) throws IOException {
        String fileName = String.format("reports_%s_%s_%d__%s",
                reportType, sector, year, df.format(new Date()));
        Path path = dirPath.resolve(fileName);

        try (BufferedWriter writer = Files.newBufferedWriter(path))
        {
            for (ReportEntry entry : resultList) {
                writer.write(entry.asListString() + "\r\n");
            }
        }
    }

    public static void saveResultFiles(String reportType, String sector, int year, List<ReportEntry> resultList, Path dirPath) throws IOException {

        Path path = dirPath.resolve(String.format("./%s/%s/%d", reportType, sector, year));

        if (!Files.exists(path)) {
            Files.createDirectories(path);
        }

        for (ReportEntry entry : resultList) {

            if(!StringUtils.isEmpty(entry.getUrlPdf())) {
                boolean isMissingdata = downloadFile(entry.getUrlPdf(), entry.getIndex(), path);
                entry.setMissingData(isMissingdata);
            }
            if(!StringUtils.isEmpty(entry.getUrlXls())) {
                downloadFile(entry.getUrlXls(), entry.getIndex(), path);
            }
            if(!StringUtils.isEmpty(entry.getUrlXml())) {
                downloadFile(entry.getUrlXml(), entry.getIndex(), path);
            }
        }
    }


    public static boolean downloadFile(String url, String index, Path dirPath) {

            String fileName = url.substring(url.lastIndexOf("/") + 1);
            fileName = String.format("%5s__", index).replace(" ", "0") + fileName;

        try {
            org.apache.commons.io.FileUtils.copyURLToFile(new URL(url.replace("https", "http")), dirPath.resolve(fileName).toFile());
            return true;
        } catch (FileNotFoundException e) {
            log.warn("File not found :" + url);
            try {
                dirPath.resolve(fileName + ".missing").toFile().createNewFile();
            } catch (IOException e1) {
                log.error("Error creating empty file :" + url, e1);
            }
        } catch (Exception ex) {
            log.error("Error downloading file :" + url, ex);
        }

        return false;
    }
}
