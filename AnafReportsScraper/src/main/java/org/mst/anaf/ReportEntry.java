package org.mst.anaf;

public class ReportEntry {

    private String index;

    private String reportType;
    private String reportPeriod;
    private String judet;
    private String entity;
    private String reportSector;
    private String ordonator;

    private String urlPdf;
    private String urlXls;
    private String urlXml;

    private boolean missingData;

    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
    }

    public String getReportType() {
        return reportType;
    }

    public void setReportType(String reportType) {
        this.reportType = reportType;
    }

    public String getReportPeriod() {
        return reportPeriod;
    }

    public void setReportPeriod(String reportPeriod) {
        this.reportPeriod = reportPeriod;
    }

    public String getJudet() {
        return judet;
    }

    public void setJudet(String judet) {
        this.judet = judet;
    }

    public String getEntity() {
        return entity;
    }

    public void setEntity(String entity) {
        this.entity = entity;
    }

    public String getReportSector() {
        return reportSector;
    }

    public void setReportSector(String reportSector) {
        this.reportSector = reportSector;
    }

    public String getOrdonator() {
        return ordonator;
    }

    public void setOrdonator(String ordonator) {
        this.ordonator = ordonator;
    }

    public String getUrlPdf() {
        return urlPdf;
    }

    public void setUrlPdf(String urlPdf) {
        this.urlPdf = urlPdf;
    }

    public String getUrlXls() {
        return urlXls;
    }

    public void setUrlXls(String urlXls) {
        this.urlXls = urlXls;
    }

    public String getUrlXml() {
        return urlXml;
    }

    public void setUrlXml(String urlXml) {
        this.urlXml = urlXml;
    }

    public boolean isMissingData() {
        return missingData;
    }

    public void setMissingData(boolean missingData) {
        this.missingData = missingData;
    }

    @Override
    public String toString() {
        return "ReportEntry{" +
                "index='" + index + '\'' +
                ", reportType='" + reportType + '\'' +
                ", reportPeriod='" + reportPeriod + '\'' +
                ", judet='" + judet + '\'' +
                ", entity='" + entity + '\'' +
                ", reportSector='" + reportSector + '\'' +
                ", ordonator='" + ordonator + '\'' +
                ", urlPdf='" + urlPdf + '\'' +
                ", urlXls='" + urlXls + '\'' +
                ", urlXml='" + urlXml + '\'' +
                ", missingData='" + missingData + '\'' +
                '}';
    }

    public String asListString() {
        return "" + index +
                ", " + reportType +
                ", " + reportPeriod +
                ", " + judet +
                ", " + entity +
                ", " + reportSector +
                ", " + ordonator +
                ", " + urlPdf +
                ", " + urlXls +
                ", " + urlXml +
                ", " + missingData;
    }

    public void addUrl(String url) {
        String ext = url.substring(url.lastIndexOf(".") + 1).toLowerCase();
        switch (ext) {
            case "xml":
                setUrlXml(url);
                break;
            case "pdf":
                setUrlPdf(url);
                break;
            case "xls":
            case "xlsx":
                setUrlXls(url);
                break;
            default:
                break;
        }
    }

}
