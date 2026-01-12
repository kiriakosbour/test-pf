package gr.deddie.pfr.model;

import java.util.List;

/**
 * Created by M.Masikos on 14/12/2016.
 */
public class UserDataRoleWithItems extends UserDataRole {
    private List<DataRoleItem> dataRoleItems;

    public List<DataRoleItem> getDataRoleItems() {
        return dataRoleItems;
    }

    public void setDataRoleItems(List<DataRoleItem> dataRoleItems) {
        this.dataRoleItems = dataRoleItems;
    }
}
