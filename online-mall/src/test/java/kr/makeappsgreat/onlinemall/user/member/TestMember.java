package kr.makeappsgreat.onlinemall.user.member;

import kr.makeappsgreat.onlinemall.model.Address;
import org.modelmapper.ModelMapper;

class TestMember {

    private static final String username = "simpleuser@email.com";

    public static Member getTestMember(ModelMapper modelMapper) {
        AgreementRequest request = new AgreementRequest();
        request.setTerms1(true);
        request.setTerms2(true);
        request.setTerms3(true);
        request.setMarketing(false);
        Agreement agreement = modelMapper.map(request, Agreement.class);

        Address address = new Address();
        address.setZipcode("42700");
        address.setAddress("대구광역시 달서구");

        Member member = Member.builder()
                .name("김가연")
                .email(username)
                .password("simple")
                .address(address)
                .mobileNumber("010-1234-5678")
                .build();
        member.setAgreement(agreement);


        return member;
    }

    public static String getUsername() { return username; }

}