package util;

import model.*;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

@ApplicationScoped
public class JPAUtilImpl implements JPAUtil {
    private final EntityManager entityManager = Persistence.createEntityManagerFactory("hibernate").createEntityManager();
    private final HashMap<String, Function<String, Object>> possibleParams = createPossibleParams();

    private static HashMap<String, Function<String, Object>> createPossibleParams() {
        HashMap<String, Function<String, Object>> result = new HashMap<>();
        result.put("id", Integer::parseInt);
        result.put("name", s -> s);
        result.put("coordinates", Coordinates::fromString);
        result.put("creationDate", s -> LocalDateTime.parse(s, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")));
        result.put("area", Float::parseFloat);
        result.put("numberOfRooms", Long::parseLong);
        result.put("price", Double::parseDouble);
        result.put("kitchenArea", Float::parseFloat);
        result.put("view", View::valueOf);
        result.put("house", Integer::parseInt);
        return result;
    }

    @Override
    public void saveFlat(Flat flat) {
        entityManager.getTransaction().begin();
        entityManager.merge(flat);
        entityManager.flush();
        entityManager.getTransaction().commit();
    }

    @Override
    public void saveHouse(House house) {
        entityManager.getTransaction().begin();
        entityManager.merge(house);
        entityManager.flush();
        entityManager.getTransaction().commit();
    }

    @Override
    public FlatListWrap getFlats(Map<String, String[]> params) {
        try {
            CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
            CriteriaQuery<Flat> criteriaQuery = criteriaBuilder.createQuery(Flat.class);
            Root<Flat> flatRoot = criteriaQuery.from(Flat.class);
            //filtering
            List<Predicate> predicates = new ArrayList<>();
            for (String s : params.keySet()) {
                if (possibleParams.containsKey(s)) {
                    predicates.add(criteriaBuilder.equal(flatRoot.get(s), possibleParams.get(s).apply(params.get(s)[0])));
                }
            }
            criteriaQuery.select(flatRoot).where(predicates.toArray(new Predicate[0]));
            // sorting
            if (params.containsKey("orderBy")) {
                List<Order> orders = new ArrayList<>();
                for (String s : params.get("orderBy")) {
                    String[] order = s.split(",");
                    if (order.length != 2) {
                        throw new QueryParamsException(s + ": each order parameter should have 'asc' or 'desc' value");
                    }
                    if (possibleParams.containsKey(order[0]) && order[1].equals("desc")) {
                        orders.add(criteriaBuilder.desc(flatRoot.get(order[0])));
                    } else if (possibleParams.containsKey(order[0]) && order[1].equals("asc")) {
                        orders.add(criteriaBuilder.asc(flatRoot.get(order[0])));
                    } else {
                        throw new QueryParamsException(s + " is not a correct order parameter");
                    }
                }
                criteriaQuery.orderBy(orders);
            }
            // pagination
            TypedQuery<Flat> query = entityManager.createQuery(criteriaQuery);
            int countResults = query.getResultList().size();
            if (params.containsKey("pageNumber") && params.containsKey("pageSize")) {
                int pageNumber = Integer.parseInt(params.get("pageNumber")[0]);
                int perPage = Integer.parseInt(params.get("pageSize")[0]);

                if (((long) (pageNumber - 1) * perPage >= countResults && countResults > 0) || pageNumber <= 0) {
                    throw new QueryParamsException("pagination out of bounds");
                }
                query.setFirstResult((pageNumber - 1) * perPage);
                query.setMaxResults(perPage);
            }
            return new FlatListWrap(query.getResultList(), countResults);
        } catch (Exception e) {
            throw new QueryParamsException(e.getMessage());
        }

    }

    @Override
    public Flat getFlat(Integer id) {
        return entityManager.find(Flat.class, id);
    }

    @Override
    public House getHouse(Integer id) {
        return entityManager.find(House.class, id);
    }

    @Override
    public void deleteFlat(Flat flat) {
        entityManager.getTransaction().begin();
        entityManager.remove(entityManager.contains(flat) ? flat : entityManager.merge(flat));
        entityManager.getTransaction().commit();
    }

    @Override
    public Long getNumberPriceLower(String s) {
        Double a = Double.valueOf(s);
        Query countQuery = entityManager.createQuery("select count (f) from Flat f where f.price < :num").setParameter("num", a);
        return (Long) countQuery.getSingleResult();
    }

    @Override
    public FlatListWrap getNamesContain(String s) {
        return new FlatListWrap(entityManager.createQuery("select f from Flat f where f.name like :str", Flat.class).setParameter("str", "%" + s + "%").getResultList());
    }

    @Override
    public FlatListWrap getNamesStart(String s) {
        return new FlatListWrap(entityManager.createQuery("select f from Flat f where f.name like :str", Flat.class).setParameter("str", s + "%").getResultList());
    }
}
