package org.mst.anaf.scraper;

public class ReportFilter {

    public enum ReportType {
        TIP_BUGET_INDIVIDUAL("FXB-RBG-003"),
        TIP_BUGET_AGREGAT("FXB-EXB-901"),
        TIP_BUGET_DETALIAT("FXB-EXB-900"),
        TIP_FISA_ENTITATE("FISA-EP");

        private String code;
        ReportType(String code) {
            this.code = code;
        }

        public String getCode(){
            return code;
        }
    }

    public enum SectorBugetar {
        BUGET_DE_STAT("01"),
        BUGET_LOCAL("02"),
        BUGET_ASIG_SOC("03"),
        BUGET_SOMAJ("04"),
        BUGET_SANATATE("05");

        private String code;
        SectorBugetar(String code) {
            this.code = code;
        }

        public String getCode(){
            return code;
        }
    }


    private ReportType reportType;
    private SectorBugetar reportSector;
    private Integer year;

    public ReportFilter(ReportType reportType) {
        this.reportType = reportType;
    }

    public ReportFilter(ReportType reportType, SectorBugetar reportSector) {
        this.reportType = reportType;
        this.reportSector = reportSector;
    }

    public ReportFilter(ReportType reportType, SectorBugetar reportSector, Integer year) {
        this.reportType = reportType;
        this.reportSector = reportSector;
        this.year = year;
    }

    public int getReportTypeIdx() {
        return reportType.ordinal() + 1;
    }

    public int getReportSectorIdx() {
        return reportSector.ordinal() + 1;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

}
