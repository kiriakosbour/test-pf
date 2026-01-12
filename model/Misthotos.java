package gr.deddie.pfr.model;

public class Misthotos {
    private Long mhtroo;
    private String lastname;
    private String firstname;
    private String fathersname;
    private String kathgoria;
    private String eidikothta;
    private String misthodotiki_omada;

    public Misthotos() {
    }

    public Misthotos(Long mhtroo, String lastname, String firstname, String eidikothta) {
        this.mhtroo = mhtroo;
        this.lastname = lastname;
        this.firstname = firstname;
        this.eidikothta = eidikothta;
    }

    public Long getMhtroo() {
        return mhtroo;
    }

    public void setMhtroo(Long mhtroo) {
        this.mhtroo = mhtroo;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getFathersname() {
        return fathersname;
    }

    public void setFathersname(String fathersname) {
        this.fathersname = fathersname;
    }

    public String getKathgoria() {
        return kathgoria;
    }

    public void setKathgoria(String kathgoria) {
        this.kathgoria = kathgoria;
    }

    public String getEidikothta() {
        return eidikothta;
    }

    public void setEidikothta(String eidikothta) {
        this.eidikothta = eidikothta;
    }

    public String getMisthodotiki_omada() {
        return misthodotiki_omada;
    }

    public void setMisthodotiki_omada(String misthodotiki_omada) {
        this.misthodotiki_omada = misthodotiki_omada;
    }
}
