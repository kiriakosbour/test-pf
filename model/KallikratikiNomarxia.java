package gr.deddie.pfr.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class KallikratikiNomarxia {
    public enum KLNOMePemptoEpipedoAnalysis{VOREIOS_TOMEAS_ATHINON("0901"),DYTIKOS_TOMEAS_ATHINON("0902"),KENTRIKOS_TOMEAS_ATHINON("0903"),
        NOTIOS_TOMEAS_ATHINON("0904"),PEIRAIOS("0905"),NHSON("0906"),ANATOLIKHS_ATTIKHS("0907"),DYTIKHS_ATTIKHS("0908"),THESSALONIKIS("0202"), ACHAIAS("0702");
        private String value;

        KLNOMePemptoEpipedoAnalysis (String s) {
            this.value = s;
        }

        public String getValue() {
            return value;
        }};

    private String klno;
    private String peri;
    private OldNomarxia oldNomarxia;
    private List<KallikratikosOTA> kallikratikosOTAList;

    public KallikratikiNomarxia() {
    }

    public KallikratikiNomarxia(String klno) {
        this.klno = klno;
    }

    public KallikratikiNomarxia(KallikratikiNomarxia kallikratikiNomarxia) {
        this.klno = kallikratikiNomarxia.getKlno();
        this.peri = kallikratikiNomarxia.getPeri();
        this.kallikratikosOTAList = kallikratikiNomarxia.getKallikratikosOTAList();
    }

    public String getKlno() {
        return klno;
    }

    public void setKlno(String klno) {
        this.klno = klno;
    }

    public String getPeri() {
        return peri;
    }

    public void setPeri(String peri) {
        this.peri = peri;
    }

    public OldNomarxia getOldNomarxia() {
        return oldNomarxia;
    }

    public void setOldNomarxia(OldNomarxia oldNomarxia) {
        this.oldNomarxia = oldNomarxia;
    }

    public List<KallikratikosOTA> getKallikratikosOTAList() {
        return kallikratikosOTAList;
    }

    public void setKallikratikosOTAList(List<KallikratikosOTA> kallikratikosOTAList) {
        this.kallikratikosOTAList = kallikratikosOTAList;
    }

    @Override
    public boolean equals(Object o) {
        if(o == null) {
            return false;
        } else if (!(o instanceof KallikratikiNomarxia)) {
            return false;
        } else {
            return ((KallikratikiNomarxia) o).getKlno().equals(this.getKlno());
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(klno);
    }

    @Override
    public String toString() {
        return "{" +
                "klno:\"" + klno + '\"' +
                ",peri:\"" + peri + '\"' +
                ",oldNomarxia:\"" + oldNomarxia + '\"' +
                ",kallikratikosOTAList:" + (kallikratikosOTAList == null ? "\"null\"" : kallikratikosOTAList.stream().map(n -> n.toString()).collect(Collectors.joining(",", "[", "]"))) +
                '}';
    }
}
