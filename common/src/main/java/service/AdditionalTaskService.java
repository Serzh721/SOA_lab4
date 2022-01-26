package service;

import model.FlatListWrap;

import javax.ejb.Remote;

@Remote
public interface AdditionalTaskService {
    Integer getMostExpensive(Integer id1, Integer id2, Integer id3);

    FlatListWrap getOrderedByArea(String view, String desc);
}
