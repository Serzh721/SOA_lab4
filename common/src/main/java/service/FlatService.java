package service;

import model.Flat;
import model.FlatListWrap;

import javax.ejb.Remote;
import java.util.Map;

@Remote
public interface FlatService {
    FlatListWrap getFlatList(Map<String, String[]> params);

    Flat getFlat(Integer id);

    Flat addFlat(Flat flat);

    void deleteFlat(Integer id);

    Flat modifyFlat(Integer id, Flat flat);

    Long getNumberPriceLower(String s);

    FlatListWrap getNamesContain(String s);

    FlatListWrap getNamesStart(String s);
}
