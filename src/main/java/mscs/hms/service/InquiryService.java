package mscs.hms.service;

import mscs.hms.model.Inquiry;

public interface InquiryService {
    public Inquiry save(Inquiry inquiry);
    public Inquiry getById(Integer id);
    public void deleteById(Integer id);
    public Iterable<Inquiry> findAll();
}
