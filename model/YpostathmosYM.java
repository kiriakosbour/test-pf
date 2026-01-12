package gr.deddie.pfr.model;

import java.util.List;

public class YpostathmosYM {

    private String id;
    private String name;
    List<GrammiMT> grammiMTList;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<GrammiMT> getGrammiMTList() {
        return grammiMTList;
    }

    public void setGrammiMTList(List<GrammiMT> grammiMTList) {
        this.grammiMTList = grammiMTList;
    }
}
