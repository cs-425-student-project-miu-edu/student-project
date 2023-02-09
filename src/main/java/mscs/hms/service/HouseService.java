package mscs.hms.service;

import mscs.hms.model.Address;
import mscs.hms.model.House;
import org.springframework.data.domain.Page;

import java.util.List;

public interface HouseService {
    public House saveHouse(House house);
    public House get(Integer id);
    public void delete(Integer id);
    public Iterable<House> findAll();
    public House save(House house);
    public Page<House> getAll(String searchString, Integer pageSize, Integer offset);
}
