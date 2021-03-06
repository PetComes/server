package com.pet.comes.service;

import com.pet.comes.dto.Rep.DogProfileRepDto;
import com.pet.comes.dto.Req.AnimalRegistrationReqDto;
import com.pet.comes.dto.Req.DogBodyInformationDto;
import com.pet.comes.dto.Req.DogReqDto;
import com.pet.comes.model.Entity.Dog;
import com.pet.comes.model.Entity.DogLog;
import com.pet.comes.model.Entity.Family;
import com.pet.comes.model.Entity.User;
import com.pet.comes.model.EnumType.DogStatus;
import com.pet.comes.repository.DogLogRepository;
import com.pet.comes.repository.DogRepository;
import com.pet.comes.repository.UserRepository;
import com.pet.comes.response.DataResponse;
import com.pet.comes.response.NoDataResponse;
import com.pet.comes.response.ResponseMessage;
import com.pet.comes.response.Status;
import com.pet.comes.util.openAPI.AnimalRegNoValidateService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class DogService {

    private final DogRepository dogRepository;
    private final Status status;
    private final ResponseMessage message;
    private final UserService userService;
    private final UserRepository userRepository;
    private final FamilyService familyService;
    private final AnimalRegNoValidateService animalRegNoValidateService;
    private final DogLogRepository dogLogRepository;

    /* H4 : 강아지 등록 API --Tony */
    public ResponseEntity addDog(Long userId, DogReqDto dogReqDto) {
        if (dogReqDto.getName() == null || dogReqDto.getAge() > 40) {

            return new ResponseEntity(NoDataResponse.response(status.NOT_ENTERED, message.NOT_ENTERED), HttpStatus.OK);

        }

        Family family = userService.userFamily(userId);
        if (family == null)
            return new ResponseEntity(NoDataResponse.response(status.INVALID_ID, message.NO_FAMILY + " : 강아지 등록해보기"), HttpStatus.OK);


        Dog dog = new Dog(userId, dogReqDto);
        dog.setFamily(family);// dog -> family 관계 매핑
        dog.setStatus(DogStatus.WITH);
        family.setDogs(dog);// family -> dog 관계 매핑
        familyService.addFamily(family);
        userService.setFamilyId(userId, family); // User -> Family 관계 매핑

        dogRepository.save(dog);

        return new ResponseEntity(DataResponse.response(status.SUCCESS,
                "반려견정보입력" + message.SUCCESS, dog.getId()), HttpStatus.OK);


    }

    /* H5 : 강아지별 프로필 조회 API -- Tony*/
    public ResponseEntity getDogProfile(String nickName, String dogName) {
        Optional<User> isUser = userRepository.findByNickname(nickName);
        if (!isUser.isPresent())
            return new ResponseEntity(NoDataResponse.response(status.INVALID_ID, message.INVALID_ACCOUNT), HttpStatus.OK);

        User user = isUser.get();
        Family family = user.getFamily();
        if (family == null)
            return new ResponseEntity(NoDataResponse.response(status.INVALID_FAMILYID, message.NO_FAMILY), HttpStatus.OK);

        List<Dog> dogs = family.getDogs();
        if (dogs.isEmpty())
            return new ResponseEntity(NoDataResponse.response(status.INVALID_DOGID, message.NO_DOG), HttpStatus.OK);

        Long dogId = 0L; // id가 0인 반려견은 없음. 유효성 검사에서 걸리게됨.
        for (Dog dog : dogs) {
            if (dog.getName().equals(dogName)) {
                dogId = dog.getId();
                break;
            }
        }
        Optional<Dog> isExist = dogRepository.findById(dogId);

        if (!isExist.isPresent())
            return new ResponseEntity(NoDataResponse.response(status.INVALID_ID, message.NO_DOG), HttpStatus.OK);

        Dog dog = isExist.get();
        DogProfileRepDto dogProfileRepDto = new DogProfileRepDto(dog);

        return new ResponseEntity(DataResponse.response(status.SUCCESS,
                "강아지별 프로필 조회 " + message.SUCCESS, dogProfileRepDto), HttpStatus.OK);

    }

    /* A1 : 동물등록번호 등록 시 유저 이름 내려주기 - Heather : 22-01-24 */
    public ResponseEntity getUserName(Long userId) {

        Optional<User> user = userRepository.findById(userId);
        if (user.isEmpty()) {
            return new ResponseEntity(NoDataResponse.response(status.INVALID_ID, message.INVALID_USER), HttpStatus.OK);
        }
        User userA = user.get();
        String username = userA.getUsername();

        return new ResponseEntity(DataResponse.response(status.SUCCESS, "이름 조회 성공", username), HttpStatus.OK);
    }

    /* A2 : 동물등록번호 추가 - Heather : 22-01-24 */
    public ResponseEntity registerAnimalRegistrationNo(AnimalRegistrationReqDto animalRegistrationReqDto) throws IOException {
        // 등록하려는 사람의 계정 유효성 검사
        Optional<User> user = userRepository.findById(animalRegistrationReqDto.getUserId());
        if (user.isEmpty()) {
            return new ResponseEntity(NoDataResponse.response(status.INVALID_ID, message.INVALID_USER), HttpStatus.OK);
        }
        User userForRegistration = user.get();

        // 동물등록번호 자리수 검사
        if(animalRegistrationReqDto.getDogRegNo().length() > 15) {
            return new ResponseEntity(NoDataResponse.response(status.TOO_LONG_VALUE, "동물등록번호는 15자리입니다."), HttpStatus.OK);
        }

        // 동물등록번호 유효성 검사 : 현재 OpenAPI body가 오고 있지 않음.
        String result = animalRegNoValidateService.isValidAnimalRegNo(animalRegistrationReqDto.getDogRegNo(), animalRegistrationReqDto.getBirthday(), animalRegistrationReqDto.getUserName());
        String resultMsg = "";
        // String dogRegNo = "";
        try {
            if(result != null) {
                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                DocumentBuilder documentBuilder = factory.newDocumentBuilder();

                InputStream is = new ByteArrayInputStream(result.getBytes());
                Document doc = documentBuilder.parse(is);
                Element element = doc.getDocumentElement();
                NodeList resultMsgNodeList = element.getElementsByTagName("resultMsg");
                //NodeList dogRegNoNodeList = element.getElementsByTagName("dogRegNo");

                for(int i = 0; i<resultMsgNodeList.getLength(); i++) {
                    Node item = resultMsgNodeList.item(i);
                    Node text = item.getFirstChild();
                    resultMsg = text.getNodeValue();

                    //Node dogRegNoItem = dogRegNoNodeList.item(i);
                    //dogRegNo = dogRegNoItem.getFirstChild().getNodeValue();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        if(!resultMsg.equals("NORMAL SERVICE.")) {
            return new ResponseEntity(NoDataResponse.response(status.INVALID_NO, "유효하지 않은 동물등록번호입니다."), HttpStatus.OK);
        }

        // 동물등록번호 등록
        Optional<Dog> dog = dogRepository.findById(animalRegistrationReqDto.getDogId());
        if(dog.isEmpty()) {
            return new ResponseEntity(NoDataResponse.response(status.INVALID_DOGID, "유효하지 않은 dogId 입니다."), HttpStatus.OK);
        }
        Dog dogForRegistration = dog.get();
        dogForRegistration.setRegisterationNo(animalRegistrationReqDto.getDogRegNo()); // body를 전달받을 경우 dogRegNo로 등록해야 함
        dogForRegistration.setModifiedBy(userForRegistration.getId());
        dogRepository.save(dogForRegistration);

        return new ResponseEntity(NoDataResponse.response(status.SUCCESS, "동물등록번호 등록 성공"), HttpStatus.OK);
    }

    /* A3 : 키, 몸무게 등록 및 수정 - Heather : 22-01-24 */
    public ResponseEntity registerDogBodyInformation(DogBodyInformationDto dogBodyInfo) {

        // 소유자 유효성 검사
        Optional<User> user = userRepository.findById(dogBodyInfo.getModifiedBy());
        if (user.isEmpty()) {
            return new ResponseEntity(NoDataResponse.response(status.INVALID_ID, message.INVALID_USER), HttpStatus.OK);
        }

        // 몸무게가 0일 경우 fail
        if(dogBodyInfo.getWeight() == 0.0f) {
            return new ResponseEntity(NoDataResponse.response(status.EMPTY_VALUE, "몸무게가 0입니다."), HttpStatus.OK);
        }

        // 유효한 강아지인지 확인
        Optional<Dog> dog = dogRepository.findById(dogBodyInfo.getDogId());
        if(dog.isEmpty()) {
            return new ResponseEntity(NoDataResponse.response(status.INVALID_DOGID, "유효하지 않은 dogId 입니다."), HttpStatus.OK);
        }
        Dog dogForUpdate = dog.get();

        // 등록된 몸무게 확인
        float weight = dogForUpdate.getWeight();

        // 몸무게가 이전에 등록되어 있었다면 dog_log에 저장 -> 몸무게 변경 시 항상 dog update, dogLog insert! (dogLog는 이력이 남는 것)
        if(weight != 0.0f) {
            DogLog dogLog = new DogLog(dogBodyInfo, dogForUpdate); // dogLog -> dog 매핑
            dogLogRepository.save(dogLog);
            dogForUpdate.addDogLog(dogLog); // dog -> dogLog 매핑
        }

        // 새로 받은 몸무게 정보 등록
        dogForUpdate.setWeight(dogBodyInfo.getWeight());
        dogForUpdate.setModifiedBy(dogBodyInfo.getModifiedBy());
        dogRepository.save(dogForUpdate);

        return new ResponseEntity(NoDataResponse.response(status.SUCCESS, "몸무게 등록 성공"), HttpStatus.OK);
    }

}
