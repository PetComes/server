package com.pet.comes.service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Map;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pet.comes.model.Entity.Address;
import com.pet.comes.model.Entity.Dog;
import com.pet.comes.model.Entity.DogLog;
import com.pet.comes.model.Entity.User;
import com.pet.comes.model.Entity.schedule.Drug;
import com.pet.comes.model.Entity.schedule.Feeding;
import com.pet.comes.model.Entity.schedule.Hospital;
import com.pet.comes.model.Entity.schedule.Potty;
import com.pet.comes.model.Entity.schedule.Snack;
import com.pet.comes.repository.AddressRepository;
import com.pet.comes.repository.DogLogRepository;
import com.pet.comes.repository.DogRepository;
import com.pet.comes.repository.UserRepository;
import com.pet.comes.repository.schedule.DrugRepository;
import com.pet.comes.repository.schedule.FeedingRepository;
import com.pet.comes.repository.schedule.HospitalRepository;
import com.pet.comes.repository.schedule.PottyRepository;
import com.pet.comes.repository.schedule.SnackRepository;
import com.pet.comes.response.NoDataResponse;
import com.pet.comes.response.Status;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ScheduleService {

    private final FeedingRepository feedingRepository;
    private final SnackRepository snackRepository;
    private final PottyRepository pottyRepository;
    private final DrugRepository drugRepository;
    private final HospitalRepository hospitalRepository;

    private final UserRepository userRepository;
    private final AddressRepository addressRepository;
    private final DogRepository dogRepository;
    private final DogLogRepository dogLogRepository;

    private final Status status;

    /* iconId */
    private final int FEEDING = 1;
    private final int SNACK = 2;
    private final int POTTY = 3;
    private final int DRUG = 4;
    private final int HOSPITAL = 5;
    private final int SALON = 6;
    private final int BATH = 7;
    private final int SLEEP = 8;
    private final int PLAYING = 9;
    private final int TRAINING = 10;
    private final int MENSTRUATION = 11;
    private final int WALK = 12;
    private final int ETC = 13;

    @Transactional
    public ResponseEntity<String> registerSchedule(Map<String, String> scheduleMap) {
        int iconId = Integer.parseInt(scheduleMap.get("iconId"));

        // userId로 user 찾기
        Optional<User> user = userRepository.findById(Long.parseLong(scheduleMap.get("userId")));
        if(user.isEmpty()) {
            return new ResponseEntity(NoDataResponse.response(status.INVALID_ID, "유효하지 않은 userId 입니다. userId : " + scheduleMap.get("userId")), HttpStatus.OK);
        }

        scheduleMap.putIfAbsent("date", LocalDate.now().toString().substring(0,10));
        scheduleMap.putIfAbsent("time", LocalTime.now().toString().substring(0,6) + "00");
        scheduleMap.putIfAbsent("memo", null);

        if(!scheduleMap.get("date").matches("^\\d{4}-(0[1-9]|1[0-2])-(0[1-9]|[12][0-9]|3[01])$")) {
            return new ResponseEntity(NoDataResponse.response(status.INVALID_DATE, "잘못된 형식입니다. date(YYYY-MM-DD) : " + scheduleMap.get("date")), HttpStatus.OK);
        }
        if(scheduleMap.get("time").length() == 5) {
            scheduleMap.put("time", scheduleMap.get("time") + ":00");
        }
        if(!scheduleMap.get("time").matches("^\\d{2}:([0-5][0-9]):(00)$")) {
            return new ResponseEntity(NoDataResponse.response(status.INVALID_TIME, "잘못된 형식입니다. time(HH:MM:00) : " + scheduleMap.get("time")), HttpStatus.OK);
        }

        if(iconId == FEEDING) {
            String dryOrWet = scheduleMap.get("dryOrWet");
            if(!dryOrWet.equals("DRY") && !dryOrWet.equals("WET")) {
                return new ResponseEntity(NoDataResponse.response(status.INVALID_VALUE, "dryOrWet 값은 DRY 또는 WET 중 하나여야 합니다. dryOrWet : " + dryOrWet), HttpStatus.OK);
            }
            Feeding feeding = new Feeding(scheduleMap, user.get());
            feedingRepository.save(feeding);
            return new ResponseEntity(NoDataResponse.response(status.SUCCESS, "schedule 등록 성공"), HttpStatus.OK);
        }
        if(iconId == SNACK) {
            String kind = scheduleMap.get("kind");
            if(!kind.contains("홈메이드 : ") && !kind.contains("구매 : ")) {
                return new ResponseEntity(NoDataResponse.response(status.INVALID_VALUE, "kind 값은 \'홈메이드 : \' 또는 \'구매 : \' 로 시작해야 합니다. kind : " + kind), HttpStatus.OK);
            }
            Snack snack = new Snack(scheduleMap, user.get());
            snackRepository.save(snack);
            return new ResponseEntity(NoDataResponse.response(status.SUCCESS, "schedule 등록 성공"), HttpStatus.OK);
        }
        if(iconId == POTTY) {
            String kind = scheduleMap.get("kind");
            if(!kind.equals("URINE") && !kind.equals("FECES")) {
                return new ResponseEntity(NoDataResponse.response(status.INVALID_VALUE, "kind 값은 URINE(소변) 또는 FECES(대변) 중 하나여야 합니다. kind : " + kind), HttpStatus.OK);
            }
            Potty potty = new Potty(scheduleMap, user.get());
            pottyRepository.save(potty);
            return new ResponseEntity(NoDataResponse.response(status.SUCCESS, "schedule 등록 성공"), HttpStatus.OK);
        }
        if(iconId == DRUG) {
            scheduleMap.putIfAbsent("kind", null);
            scheduleMap.putIfAbsent("prescriptionUrl", null);
            scheduleMap.putIfAbsent("expenses", "-1");

            Drug drug = new Drug(scheduleMap, user.get());
            drugRepository.save(drug);
            return new ResponseEntity(NoDataResponse.response(status.SUCCESS, "schedule 등록 성공"), HttpStatus.OK);
        }
        if(iconId == HOSPITAL) {
            if(scheduleMap.containsKey("weight")) { // 몸무게가 입력된 경우
                if(!scheduleMap.containsKey("dogId")) {
                    new ResponseEntity(NoDataResponse.response(status.INVALID_DOGID, "dogId가 입력되지 않았습니다."), HttpStatus.OK);
                }

                String dogId = scheduleMap.get("dogId");
                Optional<Dog> dog = dogRepository.findById(Long.parseLong(dogId));
                if(dog.isEmpty()) {
                    return new ResponseEntity(NoDataResponse.response(status.INVALID_DOGID, "유효하지 않은 dogId 입니다. dogId : " + dogId), HttpStatus.OK);
                }

                Dog dogForUpdate = dog.get();
                float beforeWeight = dogForUpdate.getWeight();
                float afterWeight = Float.parseFloat(scheduleMap.get("weight"));

                if(beforeWeight != 0.0f && beforeWeight != afterWeight) {
                    DogLog dogLog = new DogLog(dogForUpdate, afterWeight);
                    dogLogRepository.save(dogLog);
                    dogForUpdate.addDogLog(dogLog);

                    dogForUpdate.setWeight(afterWeight);
                    dogRepository.save(dogForUpdate);
                }
            }

            Address address;
            if(!scheduleMap.containsKey("address") && !scheduleMap.containsKey("x")
                && !scheduleMap.containsKey("x") && !scheduleMap.containsKey("locationName")) { // 주소가 입력되지 않은 경우
                address = null;
            }
            else { // 주소의 네가지 항목 중 하나라도 입력된 경우
                scheduleMap.putIfAbsent("address", null);
                scheduleMap.putIfAbsent("x", null);
                scheduleMap.putIfAbsent("y", null);
                scheduleMap.putIfAbsent("locationName", null);

                Optional<Address> optionalAddress = addressRepository.findByLocationName(scheduleMap.get("locationName"));
                if(optionalAddress.isEmpty()) {
                    address = new Address(scheduleMap);
                }
                else {
                    address = optionalAddress.get();
                }

                addressRepository.save(address);
            }

            scheduleMap.putIfAbsent("disease", null);
            scheduleMap.putIfAbsent("kind", null);
            scheduleMap.putIfAbsent("prescriptionUrl", null);
            scheduleMap.putIfAbsent("expenses", "-1");
            scheduleMap.putIfAbsent("weight", "-1");

            Hospital hospital = new Hospital(scheduleMap, user.get(), address);
            hospitalRepository.save(hospital);

            return new ResponseEntity(NoDataResponse.response(status.SUCCESS, "schedule 등록 성공"), HttpStatus.OK);
        }
        if(iconId == SALON) {

        }
        if(iconId == BATH) {

        }
        if(iconId == SLEEP) {

        }
        if(iconId == PLAYING) {

        }
        if(iconId == TRAINING) {

        }
        if(iconId == MENSTRUATION) {

        }
        if(iconId == WALK) {

        }
        if(iconId == ETC) {

        }

        return new ResponseEntity(NoDataResponse.response(status.INVALID_ID, "유효하지 않은 iconId 입니다. iconId : " + iconId), HttpStatus.OK);
    }
}
