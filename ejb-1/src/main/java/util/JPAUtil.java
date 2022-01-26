package util;

import model.Flat;
import model.FlatListWrap;
import model.House;

import java.util.Map;

public interface JPAUtil {
    void saveFlat(Flat flat);

    void saveHouse(House house);

    FlatListWrap getFlats(Map<String, String[]> params);

    Flat getFlat(Integer id);

    House getHouse(Integer id);

    void deleteFlat(Flat flat);

    Long getNumberPriceLower(String s);

    FlatListWrap getNamesContain(String s);

    FlatListWrap getNamesStart(String s);
}
