package pl.marczuk.model;

import java.io.Serializable;
import java.util.List;

public class ListTransporter<T> implements Serializable {
    private List<T> List;

    public ListTransporter(java.util.List<T> list) {
        List = list;
    }

    public java.util.List<T> getList() {
        return List;
    }
}
