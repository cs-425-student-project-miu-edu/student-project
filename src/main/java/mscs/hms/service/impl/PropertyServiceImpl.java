package mscs.hms.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.ArrayList;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import mscs.hms.entity.Apartment;
import mscs.hms.entity.House;
import mscs.hms.entity.Property;
import mscs.hms.entity.PropertyComparators;
import mscs.hms.entity.paging.Column;
import mscs.hms.entity.paging.Order;
import mscs.hms.entity.paging.Page;
import mscs.hms.entity.paging.PageArray;
import mscs.hms.entity.paging.PagingRequest;
import mscs.hms.repository.ApartmentRepository;
import mscs.hms.repository.HouseRepository;
import mscs.hms.service.PropertyService;

@Service
public class PropertyServiceImpl extends AbsBaseService implements PropertyService {
    
    @Autowired
    HouseRepository houseRepository;

    @Autowired
    ApartmentRepository apartmentRepository;

    @Override
    public Property saveProperty(Property property) {
        if (property.getClass() == House.class)
            return houseRepository.save((House) property);
        else
            return apartmentRepository.save((Apartment) property);
    }

    @Override
    public Page<Property> getProperties(PagingRequest pagingRequest) {

        List<Property> properties = new ArrayList<>();
        houseRepository.findAll().forEach(x -> properties.add(x));
        apartmentRepository.findAll().forEach(x -> properties.add(x));

        return getPage(properties, pagingRequest);
    }

    @Override
    public PageArray getPropertyArray(PagingRequest pagingRequest) {
        pagingRequest.setColumns(Stream.of("noOfBathRooms", "noOfRooms")
                .map(Column::new)
                .collect(Collectors.toList()));
        Page<Property> employeePage = getProperties(pagingRequest);

        PageArray pageArray = new PageArray();
        pageArray.setRecordsFiltered(employeePage.getRecordsFiltered());
        pageArray.setRecordsTotal(employeePage.getRecordsTotal());
        pageArray.setDraw(employeePage.getDraw());
        pageArray.setData(employeePage.getData()
                .stream()
                .map(this::toStringList)
                .collect(Collectors.toList()));
        return pageArray;
    }

    private static final Comparator<Property> EMPTY_COMPARATOR = (e1, e2) -> 0;

    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");

    private List<String> toStringList(Property employee) {
        return Arrays.asList(employee.getNoOfBathRooms().toString(), employee.getNoOfRooms().toString());
    }

    private Page<Property> getPage(List<Property> properties, PagingRequest pagingRequest) {
        List<Property> filtered = properties.stream()
                .sorted(sortProperties(pagingRequest))
                .filter(filterProperties(pagingRequest))
                .skip(pagingRequest.getStart())
                .limit(pagingRequest.getLength())
                .collect(Collectors.toList());

        long count = properties.stream()
                .filter(filterProperties(pagingRequest))
                .count();

        Page<Property> page = new Page<>(filtered);
        page.setRecordsFiltered((int) count);
        page.setRecordsTotal((int) count);
        page.setDraw(pagingRequest.getDraw());

        return page;
    }

    private Predicate<Property> filterProperties(PagingRequest pagingRequest) {
        if (pagingRequest.getSearch() == null || StringUtils.isEmpty(pagingRequest.getSearch()
                .getValue())) {
            return employee -> true;
        }

        String value = pagingRequest.getSearch()
                .getValue();

        return employee -> employee.getNoOfBathRooms().toString()
                .toLowerCase()
                .contains(value)
                || employee.getNoOfRooms().toString()
                        .toLowerCase()
                        .contains(value);
    }

    private Comparator<Property> sortProperties(PagingRequest pagingRequest) {
        if (pagingRequest.getOrder() == null) {
            return EMPTY_COMPARATOR;
        }

        try {
            Order order = pagingRequest.getOrder()
                    .get(0);

            int columnIndex = order.getColumn();
            Column column = pagingRequest.getColumns()
                    .get(columnIndex);

            Comparator<Property> comparator = PropertyComparators.getComparator(column.getData(), order.getDir());
            if (comparator == null) {
                return EMPTY_COMPARATOR;
            }

            return comparator;

        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }

        return EMPTY_COMPARATOR;
    }

}
