package org.openchs.web;

import org.openchs.dao.AddressLevelTypeRepository;
import org.openchs.domain.AddressLevelType;
import org.openchs.service.LocationService;
import org.openchs.util.ReactAdminUtil;
import org.openchs.web.request.AddressLevelTypeContract;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.util.List;

@RestController
public class AddressLevelTypeController extends AbstractController<AddressLevelType> {

    private final AddressLevelTypeRepository addressLevelTypeRepository;
    private Logger logger;
    private LocationService locationService;

    @Autowired
    public AddressLevelTypeController(AddressLevelTypeRepository addressLevelTypeRepository, LocationService locationService) {
        this.addressLevelTypeRepository = addressLevelTypeRepository;
        this.locationService = locationService;
        this.logger = LoggerFactory.getLogger(this.getClass());
    }

    @GetMapping(value = "/addressLevelType")
    @PreAuthorize(value = "hasAnyAuthority('admin', 'organisation_admin')")
    @ResponseBody
    public Page<AddressLevelType> getAllNonVoidedAddressLevelType(Pageable pageable) {
        return addressLevelTypeRepository.findPageByIsVoidedFalse(pageable);
    }

    @GetMapping(value = "/addressLevelType/{id}")
    public ResponseEntity<?> getSingle(@PathVariable Long id) {
        return new ResponseEntity<>(addressLevelTypeRepository.findOne(id), HttpStatus.OK);
    }

    @PostMapping(value = "/addressLevelType")
    @PreAuthorize(value = "hasAnyAuthority('admin', 'organisation_admin')")
    @Transactional
    public ResponseEntity<?> createAddressLevelType(@RequestBody AddressLevelTypeContract contract) {
        AddressLevelType addressLevelType = locationService.createAddressLevel(contract);
        return new ResponseEntity<>(addressLevelType, HttpStatus.CREATED);
    }

    @PostMapping(value = "/addressLevelTypes")
    @PreAuthorize(value = "hasAnyAuthority('admin', 'organisation_admin')")
    @Transactional
    public ResponseEntity<?> save(@RequestBody List<AddressLevelTypeContract> addressLevelTypeContracts) {
        for (AddressLevelTypeContract addressLevelTypeContract : addressLevelTypeContracts) {
            logger.info(String.format("Processing addressLevelType request: %s", addressLevelTypeContract.getUuid()));
            locationService.createAddressLevel(addressLevelTypeContract);
        }
        return ResponseEntity.ok(null);
    }

    @PutMapping(value = "/addressLevelType/{id}")
    @PreAuthorize(value = "hasAnyAuthority('admin', 'organisation_admin')")
    @Transactional
    public ResponseEntity<?> updateAddressLevelType(@PathVariable("id") Long id, @RequestBody AddressLevelTypeContract contract) {
        AddressLevelType addressLevelType = addressLevelTypeRepository.findByUuid(contract.getUuid());
        addressLevelType.setName(contract.getName());
        addressLevelType.setLevel(contract.getLevel());
        addressLevelTypeRepository.save(addressLevelType);
        return new ResponseEntity<>(addressLevelType, HttpStatus.CREATED);
    }

    @DeleteMapping(value = "/addressLevelType/{id}")
    @PreAuthorize(value = "hasAnyAuthority('admin', 'organisation_admin')")
    @Transactional
    public ResponseEntity<?> voidAddressLevelType(@PathVariable("id") Long id) {
        AddressLevelType addressLevelType = addressLevelTypeRepository.findOne(id);
        if (addressLevelType == null) {
            return ResponseEntity.badRequest().body(ReactAdminUtil.generateJsonError(String.format("AddressLevelType with id %d not found", id)));
        }
        if (!addressLevelType.isVoidable()) {
            return ResponseEntity.badRequest().body(ReactAdminUtil.generateJsonError(
                    String.format("Cannot delete Type '%s' until all SubTypes are deleted or there are non-voided addresses depending on it", addressLevelType.getName())));
        }
        addressLevelType.setVoided(true);
        return new ResponseEntity<>(addressLevelType, HttpStatus.OK);
    }
}
