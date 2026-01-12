package gr.deddie.pfr.model;

import java.util.List;

/**
 * Created by M.Masikos on 9/10/2016.
 */
public class MenuItemDTO {
    private String title;
    private String href;
    private List<MenuItemDTO> subItems;

    public MenuItemDTO(String title, String href) {
        this.title = title;
        this.href = href;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

    public List<MenuItemDTO> getSubItems() {
        return subItems;
    }

    public void setSubItems(List<MenuItemDTO> subItems) {
        this.subItems = subItems;
    }
}
