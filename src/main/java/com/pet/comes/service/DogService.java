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

    /* A1 : 동물등록번호 등록 시 유저 이름 내려주기 - Heather */
    public ResponseEntity getUserName(Long userId) {

        Optional<User> user = userRepository.findById(userId);
        if (!user.isPresent()) {
            return new ResponseEntity(NoDataResponse.response(status.INVALID_ID, message.INVALID_ACCOUNT), HttpStatus.OK);
        }
        User userA = user.get();
        String username = userA.getUsername();

        return new ResponseEntity(DataResponse.response(status.SUCCESS, "이름 조회 성공", username), HttpStatus.OK);
    }

    /* A2 : 동물등록번호 조회 - Heather */
    public ResponseEntity registerAnimalRegistrationNo(AnimalRegistrationReqDto animalRegistrationReqDto) throws IOException {
        // 소유자 유효성 검사
        Optional<User> user = userRepository.findById(animalRegistrationReqDto.getUserId());
        if (user.isEmpty()) {
            return new ResponseEntity(NoDataResponse.response(status.INVALID_ID, message.INVALID_ACCOUNT), HttpStatus.OK);
        }
        User userForRegistration = user.get();

        // 동물등록번호 자리수 검사
        if(animalRegistrationReqDto.getDogRegNo().length() > 15) {
            return new ResponseEntity(NoDataResponse.response(status.TOO_LONG_VALUE, "초과:동물등록번호는 15자리입니다."), HttpStatus.OK);
        }

        // 동물등록번호 유효성 검사
        String result = animalRegNoValidateService.isValidAnimalRegNo(animalRegistrationReqDto.getDogRegNo(), animalRegistrationReqDto.getBirthday(), userForRegistration.getName());
        String resultMsg = "";
        String dogRegNo = "";
        try {
            if(result != null) {
                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                DocumentBuilder documentBuilder = factory.newDocumentBuilder();

                InputStream is = new ByteArrayInputStream(result.getBytes());
                Document doc = documentBuilder.parse(is);
                Element element = doc.getDocumentElement();
                NodeList resultMsgNodeList = element.getElementsByTagName("resultMsg");
                NodeList dogRegNoNodeList = element.getElementsByTagName("dogRegNo");

                for(int i = 0; i<resultMsgNodeList.getLength(); i++) {
                    Node item = resultMsgNodeList.item(i);
                    Node text = item.getFirstChild();
                    resultMsg = text.getNodeValue();

                    Node dogRegNoItem = dogRegNoNodeList.item(i);
                    dogRegNo = dogRegNoItem.getFirstChild().getNodeValue();
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
        if(dog.isPresent()) {
            return new ResponseEntity(NoDataResponse.response(status.INVALID_DOGID, "유효하지 않은 dogId 입니다."), HttpStatus.OK);
        }
        Dog dogForRegistration = dog.get();
        dogForRegistration.setRegisterationNo(dogRegNo);
        dogRepository.save(dogForRegistration);

        return new ResponseEntity(NoDataResponse.response(status.SUCCESS, "동물등록번호 등록 성공"), HttpStatus.OK);
    }

    /* A3 : 키, 몸무게 등록 및 수정 - Heather */
    public ResponseEntity registerDogBodyInformation(DogBodyInformationDto dogBodyInfo) {

        // 키와 몸무게가 둘 다 0일 경우 fail
        if(dogBodyInfo.getHeight() == 0 && dogBodyInfo.getWeight() == 0) {
            return new ResponseEntity(NoDataResponse.response(status.EMPTY_VALUE, "키와 몸무게가 0입니다."), HttpStatus.OK);
        }
        // 유효한 강아지인지 확인
        Optional<Dog> dog = dogRepository.findById(dogBodyInfo.getDogId());
        // 유효한 강아지가 아니라면 fail
        if(dog.isPresent()) {
            return new ResponseEntity(NoDataResponse.response(status.INVALID_DOGID, "유효하지 않은 dogId 입니다."), HttpStatus.OK);
        }
        Dog dogForUpdate = dog.get();
        // 등록된 키, 몸무게 확인
        float height = dogForUpdate.getHeight();
        float weight = dogForUpdate.getWeight();

        // 이미 등록되어 있었다면 dog_log에 저장
        if(height == 0.0f && weight != 0.0f) { // 키만 등록되어 있는 경우
            DogLog dogLog = new DogLog(dogBodyInfo);
        }
        else if(height != 0.0f && weight == 0.0f) { // 몸무게만 등록되어 있는 경우

        }
        else {

        }
        // 새로 받은 키, 몸무게 정보 등록
        dog.get().setHeight(dogBodyInfo.getHeight());
        dog.get().setWeight(dogBodyInfo.getWeight());

        return new ResponseEntity(NoDataResponse.response(status.SUCCESS, "키, 몸무게 등록 성공"), HttpStatus.OK);
    }

}
