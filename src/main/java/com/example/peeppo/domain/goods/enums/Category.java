package com.example.peeppo.domain.goods.enums;

import lombok.Getter;

@Getter
public enum Category {
    DIGITAL("디지털기기"),
    ART("예술/희귀/수집품"),
    FURNITURE("가구/인테리어"),
    BABY("유아동(유아물품)"),
    WOMAN("여성패션/잡화"),
    MAN("남성패션/잡화"),
    ELECTRONICS("가전제품"),
    KITCHEN("생활주방용품"),
    FOOD("가공식품"),
    SPORTS("스포츠/레저"),
    HOBBY("취미/게임/음반/굿즈"),
    BEAUTY("뷰티/미용"),
    PLANT("식물"),
    PET("반려동물용품"),
    TICKET("티켓/교환권"),
    BOOK("도서"),
    ETC("기타물물교환");

    private final String koreanValue;

    Category(String koreanValue) {
        this.koreanValue = koreanValue;
    }

}
