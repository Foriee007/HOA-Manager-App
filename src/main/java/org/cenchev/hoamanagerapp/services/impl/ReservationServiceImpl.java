package org.cenchev.hoamanagerapp.services.impl;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.cenchev.hoamanagerapp.model.dto.AddressDTO;
import org.cenchev.hoamanagerapp.model.dto.GarageRequestDTO;
import org.cenchev.hoamanagerapp.model.dto.ReservationDTO;
import org.cenchev.hoamanagerapp.model.dto.ReservationRequestDTO;
import org.cenchev.hoamanagerapp.model.entities.*;
import org.cenchev.hoamanagerapp.repository.ReservationRepository;
import org.cenchev.hoamanagerapp.services.HomeService;
import org.cenchev.hoamanagerapp.services.ParkAvailabilityService;
import org.cenchev.hoamanagerapp.services.ReservationService;
import org.cenchev.hoamanagerapp.services.ResidentService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReservationServiceImpl implements ReservationService {

    private final ResidentService residentService;
    private final HomeService homeService;
    private final ReservationRepository reservationRepository;
    private final ParkAvailabilityService availabilityService;

    public ReservationServiceImpl(ResidentService residentService, HomeService homeService, ReservationRepository reservationRepository, ParkAvailabilityService availabilityService) {
        this.residentService = residentService;
        this.homeService = homeService;
        this.reservationRepository = reservationRepository;
        this.availabilityService = availabilityService;
    }

    @Override
    @Transactional
    public Reservation saveRequestReservation(ReservationRequestDTO reservationRequestDTO, Long residentId) {//save and map
                validateReservationDates(reservationRequestDTO.getStartDate(), reservationRequestDTO.getEndDate());

                Resident resident = residentService.findByUserId(residentId)
                        .orElseThrow(() -> new EntityNotFoundException("User not found with user ID: " + residentId));

                Home home = homeService.findHomeById(reservationRequestDTO.getHomeId())
                        .orElseThrow(() -> new EntityNotFoundException("Property not found with ID: " + reservationRequestDTO.getHomeId()));

                Reservation reservation = mapReservationRequestDTOToReservation(reservationRequestDTO, resident, home);

                return reservationRepository.save(reservation);
    }

    @Override//add @Transactional after full implementation
    public ReservationDTO confirmReservation(ReservationRequestDTO reservationRequestDTO, Long userId) {
        Reservation savedReservation = this.saveRequestReservation(reservationRequestDTO, userId );
        reservationRepository.save(savedReservation);

        availabilityService.updateAvailabilities(reservationRequestDTO.getHomeId(), reservationRequestDTO.getStartDate(),
                reservationRequestDTO.getEndDate(), reservationRequestDTO.getGarageRequestDTOS());
        BigDecimal price = reservationRequestDTO.getTotalPrice();

        return mapReservationToReservationDto(savedReservation);
    }


    @Override
    public ReservationDTO mapReservationToReservationDto(Reservation reservation) {

            AddressDTO addressDto = new AddressDTO();
                     addressDto.setAddressLine(reservation.getHome().getAddress().getAddressLine());
                     addressDto.setCity(reservation.getHome().getAddress().getCity());
                     addressDto.setState(reservation.getHome().getAddress().getState());
                     addressDto.setCity(reservation.getHome().getAddress().getUsZipCode());

            List<GarageRequestDTO> garageRequestDTOS = reservation.getBookedParking().stream()
                    .map(garage -> {GarageRequestDTO dto = new GarageRequestDTO();
                            dto.setSpaceType(garage.getSpaceType());
                            dto.setCount(garage.getCount());
                            return dto;})
                    .toList();

            User residentUser = reservation.getResident().getUser();
        BigDecimal totalPrice;
        if (!reservation.isPaid()) {
            totalPrice = BigDecimal.valueOf(0);
        } else {
            totalPrice = reservation.getPayment().getTotalPrice();
        }


        ReservationDTO reservationDTO = new ReservationDTO();
                reservationDTO.setId(reservation.getId());
                reservationDTO.setReservationDate(reservation.getBookingDate());
                reservationDTO.setResidentId(reservation.getResident().getId());
                reservationDTO.setHomeId(reservation.getHome().getId());
                reservationDTO.setStartDate(reservation.getStartDate());
                reservationDTO.setEndDate(reservation.getEndDate());
                reservationDTO.setGarageRequest(garageRequestDTOS);//Change Thyme to List of garages
                reservationDTO.setTotalPrice(totalPrice);//show "Not paid" if missing payment
                reservationDTO.setHomeName(reservation.getHome().getName());
                reservationDTO.setHomeAddress(addressDto);
                reservationDTO.setResidentName(residentUser.getFirstName() + " " + residentUser.getLastName());
                reservationDTO.setResidentEmail(residentUser.getUsername());
                reservationDTO.setApproved(false);
                reservationDTO.setPaid(false);
    //
             return reservationDTO;
     }

    @Override
    @Transactional
    public List<ReservationDTO> findReservationByManagerId(Long managerId) {
        List<Home> homes = homeService.findAllHomesByPropertyManagerId(managerId);
        return homes.stream()
                        .flatMap(home -> reservationRepository.findReservationByHomeId(home.getId()).stream())
                        .map(this::mapReservationToReservationDto)
                        .collect(Collectors.toList());
    }

    @Override
    public Reservation mapReservationRequestDTOToReservation(ReservationRequestDTO reservationRequestDTO, Resident resident, Home home) {
        Reservation reservation = new Reservation();
        reservation.setResident(resident);
        reservation.setHome(home);
        reservation.setStartDate(reservationRequestDTO.getStartDate());
        reservation.setEndDate(reservationRequestDTO.getEndDate());

        for (GarageRequestDTO garageRequest : reservationRequestDTO.getGarageRequestDTOS()) {
            if (garageRequest.getCount() > 0) {
                ReservedParking reservedParking = new ReservedParking();
                reservedParking.setReservation(reservation);
                reservedParking.setspaceType(garageRequest.getSpaceType());
                reservedParking.setCount(garageRequest.getCount());
                reservation.getBookedParking().add(reservedParking);//getBookedParking().add(reservedParking);
            }
        }

        return reservation;
    }


    private void validateReservationDates(LocalDate startDate, LocalDate endDate) {
        if (startDate.isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("Start date cannot be in the past");
        }
        if (endDate.isBefore(startDate.plusDays(1))) {
            throw new IllegalArgumentException("Reservation must be for at least one day");
        }
    }
}
