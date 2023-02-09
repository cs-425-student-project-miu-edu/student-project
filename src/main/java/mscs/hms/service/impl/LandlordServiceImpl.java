package mscs.hms.service.impl;

import mscs.hms.model.Apartment;
import mscs.hms.model.Landlord;
import mscs.hms.repository.LandlordRepository;
import mscs.hms.service.LandlordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import java.util.List;
@Service
public class LandlordServiceImpl implements LandlordService {

    @Autowired
    private LandlordRepository landlordRepository;

    @Override
    public Landlord save(Landlord landlord) {
        return landlordRepository.save(landlord);
    }

    @Override
    public Landlord getById(Integer id) {
        return landlordRepository.findById(id).orElse(null);
    }

    @Override
    public void deleteById(Integer id) {
        landlordRepository.deleteById(id);
    }

    @Override
    public List<Landlord> findAll() {
        return landlordRepository.findAll();
    }

    public Page<Landlord> getAll(String searchString, Integer pageSize, Integer offset) {
        PageRequest pageRequest = PageRequest.of(offset,pageSize);
        if(searchString == null || searchString.isBlank())
            return landlordRepository.findAll(pageRequest);
        else
            return landlordRepository.findByNameContainsIgnoreCase(searchString, pageRequest);
    }
}
