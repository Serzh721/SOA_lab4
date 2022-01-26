package service;

import lombok.SneakyThrows;
import model.Flat;
import model.FlatListWrap;
import model.House;
import util.FlatServiceException;
import util.JPAUtil;
import util.QueryParamsException;
import util.ValidationUtil;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.Date;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@Stateless
public class FlatServiceImpl implements FlatService {

    @Inject
    private ValidationUtil validationUtil;

    @Inject
    private JPAUtil jpaUtil;

    @Override
    public FlatListWrap getFlatList(Map<String, String[]> params) {
        try {
            return jpaUtil.getFlats(params);
        } catch (QueryParamsException e) {
            throw new FlatServiceException(e.getMessage(), 400);
        }
    }

    @Override
    public Flat getFlat(Integer id) {
        Optional.ofNullable(id)
                .orElseThrow(() -> new FlatServiceException("id is incorrect", 400));
        return Optional.ofNullable(jpaUtil.getFlat(id))
                .orElseThrow(() -> new FlatServiceException("flat with id " + id + " is not found", 404));
    }

    @SneakyThrows
    @Override
    public Flat addFlat(Flat flat) {
        if (flat == null) {
            throw new FlatServiceException("empty request body", 400);
        }
        flat.setCreationDate(new Date());
        flat.setId(null);
        String validationErrors = validationUtil.validate(flat);
        if (validationErrors.length() != 0) {
            throw new FlatServiceException(validationErrors, 400);
        }
        if (!Objects.isNull(flat.getHouse())) {
            jpaUtil.saveHouse(flat.getHouse());
        }
        jpaUtil.saveFlat(flat);
        return flat;
    }

    @Override
    public void deleteFlat(Integer id) {
        Flat flat = getFlat(id);
        jpaUtil.deleteFlat(flat);
    }

    @Override
    public Flat modifyFlat(Integer id, Flat newFlat) {
        Flat oldFlat = getFlat(id);
        if (newFlat == null) {
            throw new FlatServiceException("empty request body", 400);
        }
//        newFlat.setCreationDate(Date.from(Instant.now()));
        String validationErrors = validationUtil.validate(newFlat);
        if (validationErrors.length() != 0) {
            throw new FlatServiceException(validationErrors, 400);
        }
        if (!Objects.isNull(newFlat.getHouse())) {
            House newHouse = newFlat.getHouse();
            if (Objects.isNull(newHouse.getId())) {
                jpaUtil.saveHouse(newHouse);
            } else {
                House oldHouse = jpaUtil.getHouse(newHouse.getId());
                if (Objects.isNull(oldHouse)) {
                    throw new FlatServiceException("flat with id " + newHouse.getId() + " is not found", 404);
                }
                oldHouse.copy(newHouse);
                jpaUtil.saveHouse(oldHouse);
            }
        }
        oldFlat.copy(newFlat);
        jpaUtil.saveFlat(oldFlat);
        return oldFlat;
    }

    @Override
    public Long getNumberPriceLower(String s) {
        return jpaUtil.getNumberPriceLower(s);
    }

    @Override
    public FlatListWrap getNamesContain(String s) {
        return jpaUtil.getNamesContain(s);
    }

    @Override
    public FlatListWrap getNamesStart(String s) {
        return jpaUtil.getNamesStart(s);
    }
}
