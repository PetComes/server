package com.pet.comes.service;

import com.pet.comes.dto.Req.DogReqDto;
import com.pet.comes.model.Entity.Dog;
import com.pet.comes.model.Entity.Family;
import com.pet.comes.model.Entity.User;
import com.pet.comes.repository.DogRepository;
import com.pet.comes.repository.FamilyRepository;
import com.pet.comes.repository.UserRepository;
import com.pet.comes.response.DataResponse;
import com.pet.comes.response.NoDataResponse;
import com.pet.comes.response.ResponseMessage;
import com.pet.comes.response.Status;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;


@Service
public class DogService {

    private final DogRepository dogRepository;
    private final Status status;
    private final ResponseMessage message;
    private final FamilyRepository familyRepository;
    private final UserService userService;
    private final UserRepository userRepository;

    @Autowired
    public DogService(UserRepository userRepository, UserService userService, FamilyRepository familyRepository, DogRepository dogRepository, Status status, ResponseMessage message) {
        this.dogRepository = dogRepository;
        this.familyRepository = familyRepository;
        this.userRepository = userRepository;
        this.userService = userService;
        this.status = status;
        this.message = message;
    }

    public void addFamily(Long userId) {

    }

    public ResponseEntity addDog(Long userId, DogReqDto dogReqDto) {
        if (dogReqDto.getName() == null || dogReqDto.getAge() > 25) {

            return new ResponseEntity(NoDataResponse.response(status.NOT_ENTERED, message.NOT_ENTERED), HttpStatus.OK);

        }

//        Long familyId = addFamily(userId);
//        Family family = familyRepository.findById(familyId).orElseGet(Family::new); // 없으면 Family 만들어주기

        Family family = new Family();
        Dog dog= new Dog();
        try { // 해당 User와 관계를 맺고 있는 family 객체가 있는지

            family = userRepository.findById(userId).get().getFamily();
            userService.setFamilyId(userId, family); // User -> Family 관계 매핑
            dog = new Dog(dogReqDto, family); // dog -> family 관계 매핑

            System.out.println("TRY 시도 ---------");
        } catch (NullPointerException e) { // 없다면
            family = new Family();
            userService.setFamilyId(userId, family); // User -> Family 관계 매핑
            dog = new Dog(dogReqDto, family); // dog -> family 관계 매핑

            System.out.println("CATCH : NULL 시도 ---------");

        }catch (IllegalArgumentException I){
            family = new Family();
            userService.setFamilyId(userId,family);
            dog = new Dog(dogReqDto, family); // dog -> family 관계 매핑

            System.out.println("CATCH : ILLe 시도 ---------");

        }finally {
            familyRepository.save(family); // dog <-> family 관계 매핑 끝난 family 객체 DB 반영
            dogRepository.save(dog);
        }


//        Dog dog = new Dog(dogReqDto, family); // dog -> family 관계 매핑

//        family.getDogs().add(dog); // family -> dog 관계 매핑



        return new ResponseEntity(DataResponse.response(status.SUCCESS,
                "반려견정보입력" + message.SUCCESS,dogReqDto.getName()), HttpStatus.OK);


    }


}
